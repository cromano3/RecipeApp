package com.christopherromano.culinarycompanion.viewmodel

import android.app.Application
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Grade
import androidx.compose.material.icons.outlined.Grade
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.christopherromano.culinarycompanion.data.RecipeAppDatabase
import com.christopherromano.culinarycompanion.data.entity.RecipeEntity
import com.christopherromano.culinarycompanion.data.repository.ProfileScreenFirebaseRepository
import com.christopherromano.culinarycompanion.data.repository.ProfileScreenRepository
import com.christopherromano.culinarycompanion.datamodel.*
import kotlinx.coroutines.*
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.update

class ProfileScreenViewModel(application: Application, private val profileScreenFirebaseRepository: ProfileScreenFirebaseRepository): ViewModel() {

    private val repository: ProfileScreenRepository

    private val coroutineScope = CoroutineScope(Dispatchers.IO)

    val uiState = MutableStateFlow(ProfileScreenDataModel())
    val uiAlertState = MutableStateFlow(UiAlertStateProfileScreenDataModel())
    val uiStarsState = MutableStateFlow(ProfileScreenStarsDataModel())

    var favoritesData: LiveData<List<RecipeWithIngredientsAndInstructions>>
    var cookedData: LiveData<List<RecipeWithIngredients>>

    var expToGive: LiveData<Int>


    private val _commentsList = MutableStateFlow<List<AuthorDataWithComment>>(listOf())
    val commentsList: StateFlow<List<AuthorDataWithComment>>
        get() = _commentsList



    private fun getCommentsList(){
        viewModelScope.launch {

            val isAuthed = withContext(Dispatchers.IO) { profileScreenFirebaseRepository.currentUser() }

            if(isAuthed != null) {
                withContext(Dispatchers.IO) {
                    profileScreenFirebaseRepository.getCommentsList().collect {
                        _commentsList.value = it
                    }
                }
            }

        }
    }

    init{
        val appDb = RecipeAppDatabase.getInstance(application)
        val profileScreenDao = appDb.ProfileScreenDao()
        repository = ProfileScreenRepository(profileScreenDao)

        favoritesData = repository.favoritesData
        cookedData = repository.cookedData
        expToGive = repository.expToGive

    }

    fun stopDoAnimation(){
        uiState.update {
            it.copy(
                doAnimation = false
            )
        }
    }

    fun endAnimation(){
        coroutineScope.launch(Dispatchers.IO){

            uiState.update {
                it.copy(totalAnimationsToPlay = uiState.value.totalAnimationsToPlay - 1)
            }

            withContext(Dispatchers.IO){ updateExp() }

            if(uiState.value.totalAnimationsToPlay > 0){

                uiState.update {
                    it.copy(
                        resetAnimation = true
                    )
                }
            }
        }
    }

    private fun levelUpStar(){

            var myList = uiStarsState.value.starList

            /**
             * map or mapIndexed creates a new list object which allows recomposition to take place.
             * Using a mutable list to change list items doesn't create a new list and doesn't trigger
             * recomposition.
             */
            myList = myList.mapIndexed { index, it -> if(index == uiState.value.level) Icons.Filled.Grade else it }

            uiStarsState.update {
                it.copy(
                    starList = myList
                )
            }

    }

    fun stopReset(){
        uiState.update {
            it.copy(
                resetAnimation = false
            )
        }

    }

    fun startNextAnimation(){
            uiState.update { it.copy(doAnimation = true) }
    }


    fun animationSetup(){

        coroutineScope.launch(Dispatchers.IO) {

            uiStarsState.update {
                it.copy(
                    starList = listOf<ImageVector>(
                        Icons.Outlined.Grade,
                        Icons.Outlined.Grade,
                        Icons.Outlined.Grade,
                        Icons.Outlined.Grade,
                        Icons.Outlined.Grade,
                        Icons.Outlined.Grade,
                        Icons.Outlined.Grade,
                        Icons.Outlined.Grade,
                        Icons.Outlined.Grade,
                        Icons.Outlined.Grade)
                )
            }

            val xpToGive = repository.getExpToGive()
            val xp = repository.getExp()

            uiState.update { currentState ->
                currentState.copy(
                    xp = xp,
                    xpToGive = xpToGive,
                )
            }

            repository.updateExp(uiState.value.xpToGive)

            levelHelper(xp)

            delay(1000)

            drawStars()

            if(uiState.value.level != 10) {
                val totalAnimations = getAnimationTotal(xp, xpToGive)

                uiState.update { currentState ->
                    currentState.copy(
                        totalAnimationsToPlay = totalAnimations,
                        doAnimation = true,
                    )
                }
            }

        }

    }

    private suspend fun drawStars(){

        for(x in 0 until uiState.value.level){

            var myList = uiStarsState.value.starList

            /**
             * map or mapIndexed creates a new list object which allows recomposition to take place.
             * Using a mutable list to change list items doesn't create a new list and doesn't trigger
             * recomposition.
             */
            myList = myList.mapIndexed { index, it -> if(index == x) Icons.Filled.Grade else it }

            uiStarsState.update {
                it.copy(
                    starList = myList
                )
            }
            delay(200)
        }

    }

    private fun getAnimationTotal(xp: Int, xpToGive: Int): Int{

        println("xp to give $xpToGive")
        println("xp $xp")
        println("lvl ${uiState.value.level}")

        return if(xpToGive == 0){
            println("0 xp")
            1
        }
        else if((uiState.value.level * 200) - xp > xpToGive){
            println("no lvl up")
            1
        } else{
            if(xpToGive - ((uiState.value.level * 200) - xp) > 200){
                println("do recursion")
                recursionRemainder(xpToGive - ((uiState.value.level * 200) - xp))
            } else{
                println("1 lvl up")
                2
            }

        }

    }

    fun cancelAnimationStack(){
        uiState.update {
            it.copy(
                xp = uiState.value.xp + uiState.value.xpToGive,
                xpToGive = 0,
                totalAnimationsToPlay = 0,
            )
        }
    }

    private fun recursionRemainder(remainder: Int): Int{
        return if(remainder > 200){
            println("recursion again")
            recursionRemainder(remainder - 200) + 1
        }

        else {
            println("end recursion")
            2
        }
    }

    private fun levelHelper(totalCurrentExp: Int){

        if(totalCurrentExp >= 1800){

            uiState.update { currentState ->
                currentState.copy(
                    level = 10,
                    title = "Iron Chef",
                    animationTargetFirst = 1f,
                )
            }

        }
        else if (totalCurrentExp >= 1600){
            uiState.update { currentState ->
                currentState.copy(
                    level = 9,
                    title = "Master Chef",
                    animationTargetFirst = if(200 * 9 - totalCurrentExp <= uiState.value.xpToGive) 1f else (200 - (200 * 9 - totalCurrentExp) + uiState.value.xpToGive) / 200f,
                )
            }
        }
        else if (totalCurrentExp >= 1400){
            uiState.update { currentState ->
                currentState.copy(
                    level = 8,
                    title = "Pro Chef",
                    animationTargetFirst = if(200 * 8 - totalCurrentExp <= uiState.value.xpToGive) 1f else (200 - (200 * 8 - totalCurrentExp) + uiState.value.xpToGive) / 200f,
                )
            }
        }
        else if (totalCurrentExp >= 1200){
            uiState.update { currentState ->
                currentState.copy(
                    level = 7,
                    title = "Expert Chef",
                    animationTargetFirst = if(200 * 7 - totalCurrentExp <= uiState.value.xpToGive) 1f else (200 - (200 * 7 - totalCurrentExp) + uiState.value.xpToGive) / 200f,
                )
            }
        }
        else if (totalCurrentExp  >= 1000){

            uiState.update { currentState ->
                currentState.copy(
                    level = 6,
                    title = "Advanced Chef",
                    animationTargetFirst = if(200 * 6 - totalCurrentExp <= uiState.value.xpToGive) 1f else (200 - (200 * 6 - totalCurrentExp) + uiState.value.xpToGive) / 200f,
                )
            }

        }
        else if (totalCurrentExp  >= 800){
            uiState.update { currentState ->
                currentState.copy(
                    level = 5,
                    title = "Skilled Chef",
                    animationTargetFirst = if(200 * 5 - totalCurrentExp <= uiState.value.xpToGive) 1f else (200 - (200 * 5 - totalCurrentExp) + uiState.value.xpToGive) / 200f,
                )
            }
        }
        else if (totalCurrentExp  >= 600){
            uiState.update { currentState ->
                currentState.copy(
                    level = 4,
                    title = "Sous Chef",
                    animationTargetFirst = if(200 * 4 - totalCurrentExp <= uiState.value.xpToGive) 1f else (200 - (200 * 4 - totalCurrentExp) + uiState.value.xpToGive) / 200f,
                )
            }

        }
        else if (totalCurrentExp >= 400){
            uiState.update { currentState ->
                currentState.copy(
                    level = 3,
                    title = "Apprentice Chef",
                    animationTargetFirst = if(200 * 3 - totalCurrentExp <= uiState.value.xpToGive) 1f else (200 - (200 * 3 - totalCurrentExp) + uiState.value.xpToGive) / 200f,
                )
            }

        }
        else if (totalCurrentExp  >= 200){
            uiState.update { currentState ->
                currentState.copy(
                    level = 2,
                    title = "Novice Chef",
                    animationTargetFirst = if(200 * 2 - totalCurrentExp <= uiState.value.xpToGive) 1f else (200 - (200 * 2 - totalCurrentExp) + uiState.value.xpToGive) / 200f,
                )
            }

        }
        else{
            uiState.update { currentState ->
                currentState.copy(
                    level = 1,
                    title = "Beginner Chef",
                    animationTargetFirst = if(200 * 1 - totalCurrentExp <= uiState.value.xpToGive) 1f else (200 - (200 * 1 - totalCurrentExp) + uiState.value.xpToGive) / 200f,
                )
            }

        }
    }

    private fun updateExp(){

            if(uiState.value.xpToGive > ((uiState.value.level * 200) - uiState.value.xp)){

                levelUpStar()

                var nextTotalTnl = 0

                when(uiState.value.level){
                    9 -> nextTotalTnl = 1800
                    8 -> nextTotalTnl = 1600
                    7 -> nextTotalTnl = 1400
                    6 -> nextTotalTnl = 1200
                    5 -> nextTotalTnl = 1000
                    4 -> nextTotalTnl = 800
                    3 -> nextTotalTnl = 600
                    2 -> nextTotalTnl = 400
                    1 -> nextTotalTnl = 200
                }

                val expChange = nextTotalTnl - uiState.value.xp

                uiState.update { currentState ->
                    currentState.copy(
                        xp = uiState.value.xp + expChange,
                        xpToGive = uiState.value.xpToGive - expChange,
                    )
                }

            }

            else{

                uiState.update { currentState ->
                    currentState.copy(
                        xp = uiState.value.xp + uiState.value.xpToGive,
                        xpToGive = 0,
                    )
                }
            }

            uiState.update { currentState ->
                currentState.copy(
                    animationsPlayed =  uiState.value.animationsPlayed + 1
                )
            }

            levelHelper(uiState.value.xp)

    }

    fun toggleFavorite(recipe: RecipeWithIngredients){
        if(recipe.recipeEntity.isFavorite == 0){
            repository.updateFavorite(recipe.recipeEntity.recipeName, 1)
        }
        else if(recipe.recipeEntity.isFavorite == 1){
            repository.updateFavorite(recipe.recipeEntity.recipeName, 0)
        }

    }

    fun removeFavorite(recipe: RecipeWithIngredientsAndInstructions){
            repository.updateFavorite(recipe.recipeEntity.recipeName, 0)

    }

    fun triggerRemoveFavoriteAlert(recipe: RecipeWithIngredientsAndInstructions){
        uiAlertState.update { currentState ->
            currentState.copy(
                showRemoveFavoriteAlert = true,
                recipe = recipe
            )
        }
    }

    fun cancelRemoveAlert(){
        uiAlertState.update { currentState ->
            currentState.copy(
                showRemoveFavoriteAlert = false,
                recipe = RecipeWithIngredientsAndInstructions(RecipeEntity(), listOf(), listOf())
            )
        }
    }


    fun triggerDeleteReviewAlert(commentID: String, recipeName: String){
        uiAlertState.update {
            it.copy(
                showDeleteReviewAlert = true,
                recipeNameToDelete = recipeName,
                commentIDToDelete = commentID
            )
        }
    }

    fun cancelDeleteReviewAlert(){
        uiAlertState.update {
            it.copy(
                showDeleteReviewAlert = false,
                commentIDToDelete = "",
                recipeNameToDelete = "",
            )
        }
    }

    fun confirmDeleteReviewAlert(){

        /** DELETE FROM FIRESTORE HERE */

        uiAlertState.update {
            it.copy(
                showDeleteReviewAlert = false,
                commentIDToDelete = "",
                recipeNameToDelete = "",
            )
        }
    }


    fun setActiveTab(tabName: String){
        getCommentsList()
        uiState.update { currentState ->
            currentState.copy(
                previousTab = currentState.activeTab
            )
        }
        uiState.update { currentState ->
            currentState.copy(
                activeTab = tabName
            )
        }
    }
}