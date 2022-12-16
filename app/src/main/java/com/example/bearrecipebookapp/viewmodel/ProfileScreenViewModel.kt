package com.example.bearrecipebookapp.viewmodel

import android.app.Application
import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import com.example.bearrecipebookapp.data.ProfileScreenRepository
import com.example.bearrecipebookapp.data.RecipeAppDatabase
import com.example.bearrecipebookapp.data.RecipeEntity
import com.example.bearrecipebookapp.datamodel.ProfileScreenDataModel
import com.example.bearrecipebookapp.datamodel.RecipeWithIngredients
import com.example.bearrecipebookapp.datamodel.UiAlertStateProfileScreenDataModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class ProfileScreenViewModel(application: Application): ViewModel() {

    private val repository: ProfileScreenRepository

    private val coroutineScope = CoroutineScope(Dispatchers.IO)

    val uiState = MutableStateFlow(ProfileScreenDataModel())

    val uiAlertState = MutableStateFlow(UiAlertStateProfileScreenDataModel())

    var favoritesData: LiveData<List<RecipeWithIngredients>>
    var cookedData: LiveData<List<RecipeWithIngredients>>

    var expToGive: LiveData<Int>
    var exp: LiveData<Int>

    init{
        val appDb = RecipeAppDatabase.getInstance(application)
        val profileScreenDao = appDb.ProfileScreenDao()
        repository = ProfileScreenRepository(profileScreenDao)

        favoritesData = repository.favoritesData
        cookedData = repository.cookedData
        expToGive = repository.expToGive
        exp = repository.exp

        animationSetup()

    }

    fun endAnimation(){
        coroutineScope.launch(Dispatchers.IO){
            delay(600)
            uiState.update {
                it.copy(
                    doAnimation = false,
                    totalAnimationsToPlay = uiState.value.totalAnimationsToPlay - 1
                )
            }

            updateExp()

            if(uiState.value.totalAnimationsToPlay > 0){
                uiState.update {
                    it.copy(
                        resetAnimation = true
                    )
                }

                delay(200)

                uiState.update{
                    it.copy(
                        resetAnimation = false,
                        doAnimation = true
                    )
                }
            }
        }
    }

//    fun startNextAnimation(){
//        coroutineScope.launch {
//            delay(100)
//            uiState.update {
//                it.copy(
//
//                )
//            }
//        }
//    }


    private fun animationSetup(){

        coroutineScope.launch(Dispatchers.IO) {

            val xpToGive = repository.getExpToGive()
            val xp = repository.getExp()

            uiState.update { currentState ->
                currentState.copy(
                    xp = xp,
                    xpToGive = xpToGive,
                )
            }

            levelHelper(xp)

//            if (xpToGive > 0){
            val totalAnimations = getAnimationTotal(xp, xpToGive)


            uiState.update { currentState ->
                currentState.copy(
                    totalAnimationsToPlay = totalAnimations,
                    doAnimation = true,
                )
            }
//                getFirstAnimationTarget()
//                getSecondOrMoreAnimationTarget()
//            }

        }

    }

    private fun getAnimationTotal(xp: Int, xpToGive: Int): Int{
        if(xpToGive == 0)
            return 1
        else if((uiState.value.level * 200) - xp > xpToGive){
            return 1
        }
        else{
            if(xpToGive - (uiState.value.level * 200) - xp > 200){
                return recursionRemainder(xpToGive - (uiState.value.level * 200) - xp) + 1
            }
            else
                return 2
        }

    }

    private fun recursionRemainder(remainder: Int): Int{
        if(remainder > 200)
            return recursionRemainder(remainder - 200) + 1
        else
            return 1
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
//                    animationStartValue = ((200 - (200 * 9 - totalCurrentExp)) / 200f),
                )
            }
        }
        else if (totalCurrentExp >= 1400){
            uiState.update { currentState ->
                currentState.copy(
                    level = 8,
                    title = "Pro Chef",
                    animationTargetFirst = if(200 * 8 - totalCurrentExp <= uiState.value.xpToGive) 1f else (200 - (200 * 8 - totalCurrentExp) + uiState.value.xpToGive) / 200f,
//                    animationStartValue = ((200 - (200 * 8 - totalCurrentExp)) / 200f),
                )
            }
        }
        else if (totalCurrentExp >= 1200){
            uiState.update { currentState ->
                currentState.copy(
                    level = 7,
                    title = "Expert Chef",
                    animationTargetFirst = if(200 * 7 - totalCurrentExp <= uiState.value.xpToGive) 1f else (200 - (200 * 7 - totalCurrentExp) + uiState.value.xpToGive) / 200f,
//                    animationStartValue = ((200 - (200 * 7 - totalCurrentExp)) / 200f),
                )
            }
        }
        else if (totalCurrentExp  >= 1000){

            uiState.update { currentState ->
                currentState.copy(
                    level = 6,
                    title = "Advanced Chef",
                    animationTargetFirst = if(200 * 6 - totalCurrentExp <= uiState.value.xpToGive) 1f else (200 - (200 * 6 - totalCurrentExp) + uiState.value.xpToGive) / 200f,
//                    animationStartValue = ((200 - (200 * 6 - totalCurrentExp)) / 200f),
                )
            }

        }
        else if (totalCurrentExp  >= 800){
            uiState.update { currentState ->
                currentState.copy(
                    level = 5,
                    title = "Skilled Chef",
                    animationTargetFirst = if(200 * 5 - totalCurrentExp <= uiState.value.xpToGive) 1f else (200 - (200 * 5 - totalCurrentExp) + uiState.value.xpToGive) / 200f,
//                    animationStartValue = ((200 - (200 * 5 - totalCurrentExp)) / 200f),
                )
            }
        }
        else if (totalCurrentExp  >= 600){
            uiState.update { currentState ->
                currentState.copy(
                    level = 4,
                    title = "Sous Chef",
                    animationTargetFirst = if(200 * 4 - totalCurrentExp <= uiState.value.xpToGive) 1f else (200 - (200 * 4 - totalCurrentExp) + uiState.value.xpToGive) / 200f,
//                    animationStartValue = ((200 - (200 * 4 - totalCurrentExp)) / 200f),
                )
            }

        }
        else if (totalCurrentExp >= 400){
            uiState.update { currentState ->
                currentState.copy(
                    level = 3,
                    title = "Apprentice Chef",
                    animationTargetFirst = if(200 * 3 - totalCurrentExp <= uiState.value.xpToGive) 1f else (200 - (200 * 3 - totalCurrentExp) + uiState.value.xpToGive) / 200f,
//                    animationStartValue = ((200 - (200 * 3 - totalCurrentExp)) / 200f),
                )
            }

        }
        else if (totalCurrentExp  >= 200){
            uiState.update { currentState ->
                currentState.copy(
                    level = 2,
                    title = "Novice Chef",
                    animationTargetFirst = if(200 * 2 - totalCurrentExp <= uiState.value.xpToGive) 1f else (200 - (200 * 2 - totalCurrentExp) + uiState.value.xpToGive) / 200f,
//                    animationStartValue = ((200 - (200 * 2 - totalCurrentExp)) / 200f),
                )
            }

        }
        else{
            uiState.update { currentState ->
                currentState.copy(
                    level = 1,
                    title = "Beginner Chef",
//                    animationStartValue = ((200 - (200 * 1 - totalCurrentExp)) / 200f),
                    animationTargetFirst = if(200 * 1 - totalCurrentExp <= uiState.value.xpToGive) 1f else (200 - (200 * 1 - totalCurrentExp) + uiState.value.xpToGive) / 200f,
                )
            }

        }
    }

//    private fun getFirstAnimationTarget(){
//        if(uiState.value.animationsPlayed + 1  == uiState.value.totalAnimationsToPlay){
//
//            uiState.update { currentState ->
//                currentState.copy(
//                    animationTargetFirst = uiState.value.xpToGive / 200f
//                )
//            }
//
//        }
//        else{
//
//            uiState.update { currentState ->
//                currentState.copy(
//                    animationTargetFirst = 1f
//                )
//            }
//
//        }
//    }
//
//    fun getSecondOrMoreAnimationTarget(){
//        if(uiState.value.animationsPlayed + 1 == uiState.value.totalAnimationsToPlay){
//
//            uiState.update { currentState ->
//                currentState.copy(
//                    animationTargetSecond = uiState.value.xpToGive / 200f
//                )
//            }
//
//        }
//        else{
//
//            uiState.update { currentState ->
//                currentState.copy(
//                    animationTargetSecond = 1f
//                )
//            }
//
//        }
//    }

    private fun updateExp(){

        coroutineScope.launch(Dispatchers.IO) {

            if(uiState.value.xpToGive > ((uiState.value.level * 200) - uiState.value.xp)){

                var nextTotalTnl: Int = 0

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

                levelHelper(uiState.value.xp + (nextTotalTnl - uiState.value.xp))

                repository.addToExp(expChange)
                repository.removeFromExpToGive(expChange)


            }

            else{

                repository.addToExp(uiState.value.xpToGive)
                repository.clearExpToGive()

            }

            val xpToGive = repository.getExpToGive()
            val xp = repository.getExp()

            uiState.update { currentState ->
                currentState.copy(
                    xp = xp,
                    xpToGive = xpToGive,
                    animationsPlayed =  uiState.value.animationsPlayed + 1
                )
            }

        }

    }

    fun toggleFavorite(recipe: RecipeWithIngredients){
        if(recipe.recipeEntity.isFavorite == 0){
            repository.updateFavorite(recipe.recipeEntity.recipeName, 1)
        }
        else if(recipe.recipeEntity.isFavorite == 1){
            repository.updateFavorite(recipe.recipeEntity.recipeName, 0)
        }

    }

    fun removeFavorite(recipe: RecipeWithIngredients){
            repository.updateFavorite(recipe.recipeEntity.recipeName, 0)

    }

    fun triggerRemoveFavoriteAlert(recipe: RecipeWithIngredients){
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
                recipe = RecipeWithIngredients(RecipeEntity(), listOf())
            )
        }
    }

    fun setDetailsScreenTarget(recipeName: String){
        repository.setDetailsScreenTarget(recipeName)
    }

    fun setActiveTab(tabName: String){
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