package com.example.bearrecipebookapp.viewmodel

import android.app.Application
import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.bearrecipebookapp.data.RecipeAppDatabase
import com.example.bearrecipebookapp.data.entity.RecipeEntity
import com.example.bearrecipebookapp.data.repository.MenuScreenFirebaseRepository
import com.example.bearrecipebookapp.data.repository.MenuScreenRepository
import com.example.bearrecipebookapp.datamodel.RecipeWithIngredientsAndInstructions
import com.example.bearrecipebookapp.datamodel.UiAlertStateMenuScreenDataModel
import kotlinx.coroutines.*
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.update

class MenuScreenViewModel(application: Application, private val menuScreenFirebaseRepository: MenuScreenFirebaseRepository): ViewModel() {

    private val coroutineScope = CoroutineScope(Dispatchers.IO)

    private val repository: MenuScreenRepository

    var menuScreenData: LiveData<List<RecipeWithIngredientsAndInstructions>>

    val uiAlertState = MutableStateFlow(UiAlertStateMenuScreenDataModel())



    init {
        val appDb = RecipeAppDatabase.getInstance(application)
        val menuScreenDao = appDb.MenuScreenDao()
        repository = MenuScreenRepository(menuScreenDao)

        menuScreenData = repository.menuScreenData

        println("menu screen init")
        getGlobalRatings()
    }

    private fun getGlobalRatings(){
        viewModelScope.launch {
            val names = withContext(Dispatchers.IO) { repository.getOnMenuNames() }

            if(names.isNotEmpty()) {
                val namesWithRatings = withContext(Dispatchers.IO) {
                    menuScreenFirebaseRepository.getGlobalRatings(names)
                }

                if(namesWithRatings.isNotEmpty()) {
                    withContext(Dispatchers.IO) { repository.setGlobalRatings(namesWithRatings) }
                }
            }
        }
    }

    fun triggerCompletedAlert(recipe: RecipeWithIngredientsAndInstructions, userIsOnlineStatus: Int){
        uiAlertState.update { currentState ->
            currentState.copy(
                userIsOnlineStatus = userIsOnlineStatus,
                showCompletedAlert = true,
                recipe = recipe
            )
        }
    }

    fun cancelCompletedAlert(){
        uiAlertState.update { currentState ->
            currentState.copy(
                showCompletedAlert = false,
            )
        }
    }

    fun confirmCompletedAlert(recipeEntity: RecipeEntity){

        val isAuthed = menuScreenFirebaseRepository.currentUser()


        if(isAuthed != null) {

            if (recipeEntity.isRated == 0) {
                uiAlertState.update { currentState ->
                    currentState.copy(
                        showCompletedAlert = false,
                        showRatingAlert = true,
                    )
                }
            } else if (recipeEntity.userRating == 1 && recipeEntity.isFavorite == 0) {
                uiAlertState.update { currentState ->
                    currentState.copy(
                        showCompletedAlert = false,
                        showFavoriteAlert = true,
                    )
                }
            } else if (recipeEntity.isReviewed == 0) {
                uiAlertState.update { currentState ->
                    currentState.copy(
                        showCompletedAlert = false,
                        showLeaveReviewAlert = true,
                    )
                }
            } else {
                uiAlertState.update { currentState ->
                    currentState.copy(
                        showCompletedAlert = false,
                    )
                }
            }
        }
        else{
            uiAlertState.update { currentState ->
                currentState.copy(
                    showCompletedAlert = false,
                )
            }
        }


    }

    fun clearAlertRecipeTarget(){
        uiAlertState.update {
            it.copy(
                recipe = RecipeWithIngredientsAndInstructions(RecipeEntity(), listOf(), listOf())
            )
        }
    }

    fun triggerRatingAlert(){
        uiAlertState.update {
            it.copy(
                showCompletedAlert = false,
                showRatingAlert = true

            )
        }
    }

    fun confirmRating(recipeEntity: RecipeEntity) {


        //show favorite alert
        if(uiAlertState.value.isThumbUpSelected && recipeEntity.isFavorite == 0) {

            //write rating to DB
            coroutineScope.launch(Dispatchers.IO) {
                repository.setLocalRating(recipeEntity.recipeName, true)


                uiAlertState.update { currentState ->
                    currentState.copy(
                        showFavoriteAlert = true,
                        showRatingAlert = false,
                        isThumbUpSelected = false,
                        isThumbDownSelected = false
                    )
                }

            }

        }
        //show write review alert
        else if (
            uiAlertState.value.isThumbDownSelected && recipeEntity.isReviewed == 0
            || uiAlertState.value.isThumbUpSelected && recipeEntity.isReviewed == 0){


            //write rating to DB
            coroutineScope.launch(Dispatchers.IO) {
                repository.setLocalRating(recipeEntity.recipeName, true)

                uiAlertState.update { currentState ->
                    currentState.copy(
                        showLeaveReviewAlert = true,
                        showRatingAlert = false,
                        isThumbUpSelected = false,
                        isThumbDownSelected = false
                    )
                }

            }

        }

        //finish alerts
        else if(uiAlertState.value.isThumbUpSelected || uiAlertState.value.isThumbDownSelected){
            uiAlertState.update { currentState ->
                currentState.copy(
                    showLeaveReviewAlert = false,
                    showRatingAlert = false,
                    isThumbUpSelected = false,
                    isThumbDownSelected = false
                )
            }
        }
        else{
//                uiAlertState.update { currentState ->
//                    currentState.copy(
//                        showRatingAlert = false,
//                        isThumbUpSelected = false,
//                        isThumbDownSelected = false
//                    )
//                }
        }





    }

    fun thumbDownClicked(){
        uiAlertState.update {
            it.copy(
                isThumbDownSelected = !it.isThumbDownSelected,
                isThumbUpSelected = false
            )
        }
    }

    fun thumbUpClicked(){
        uiAlertState.update {
            it.copy(
                isThumbDownSelected = false,
                isThumbUpSelected = !it.isThumbUpSelected
            )
        }
    }

    fun addToFavorite(recipeEntity: RecipeEntity){

        coroutineScope.launch (Dispatchers.IO) {

            repository.setAsFavorite(recipeEntity.recipeName)

            if(recipeEntity.isReviewed == 0) {
                uiAlertState.update { currentState ->
                    currentState.copy(
                        showFavoriteAlert = false,
                        showLeaveReviewAlert = true,
                    )
                }
            }
            else{
                cancelFavoriteAlert()
            }

        }
    }

    fun doNotAddToFavorite(recipeEntity: RecipeEntity){

        if(recipeEntity.isReviewed == 0) {
            uiAlertState.update { currentState ->
                currentState.copy(
                    showFavoriteAlert = false,
                    showLeaveReviewAlert = true,
                )
            }
        }
        else{
            cancelFavoriteAlert()
        }
    }

    fun cancelFavoriteAlert(){
        uiAlertState.update { currentState ->
            currentState.copy(
                showFavoriteAlert = false,
                recipe = RecipeWithIngredientsAndInstructions(RecipeEntity(), listOf(), listOf())
            )
        }
    }

    fun cancelRatingAlert(){
        uiAlertState.update {
            it.copy(
                showRatingAlert = false,
                recipe = RecipeWithIngredientsAndInstructions(RecipeEntity(), listOf(), listOf())
            )
        }
    }

    fun triggerRemoveAlert(recipe: RecipeWithIngredientsAndInstructions){
        uiAlertState.update { currentState ->
            currentState.copy(
                showRemoveAlert = true,
                recipe = recipe
            )
        }
    }

    fun cancelRemoveAlert(){
        uiAlertState.update { currentState ->
            currentState.copy(
                showRemoveAlert = false,
                recipe = RecipeWithIngredientsAndInstructions(RecipeEntity(), listOf(), listOf())
            )
        }
    }

    fun triggerAddRecipeAlert(){
        uiAlertState.update { currentState ->
            currentState.copy(
                showAddRecipeAlert = true,
            )
        }
    }

    fun cancelAddRecipeAlert(){
        uiAlertState.update { currentState ->
            currentState.copy(
                showAddRecipeAlert = false
            )
        }
    }

    fun confirmShowWriteReviewAlert() {

        uiAlertState.update { currentState ->
            currentState.copy(
                showLeaveReviewAlert = false
            )
        }

    }

//    suspend fun confirmShowWriteReviewAlert(recipeEntity: RecipeEntity) {
//
//        /** Will be main thread query to ensure data is ready when user gets to Comment Screen */
//
////        coroutineScope.launch(Dispatchers.IO) {
//
//            repository.cleanReviewTarget()
//            repository.setReviewTarget(recipeEntity.recipeName)
//
//            uiAlertState.update { currentState ->
//                currentState.copy(
//                    showLeaveReviewAlert = false
//                )
//            }
////        }
//
//    }

    fun doNotWriteReview(recipeEntity: RecipeEntity) {

        coroutineScope.launch(Dispatchers.IO) {

            repository.setReviewAsWritten(recipeEntity.recipeName)

            uiAlertState.update { currentState ->
                currentState.copy(
                    showLeaveReviewAlert = false
                )
            }
        }

    }

    fun cancelShowWriteReviewAlert() {
        uiAlertState.update { currentState ->
            currentState.copy(
                showLeaveReviewAlert = false
            )
        }
    }


    fun addTutorialAlert(){
        repository.addTutorialAlert()
    }


    fun addCooked(recipe: RecipeWithIngredientsAndInstructions){
        repository.addCooked(recipe.recipeEntity.recipeName)
    }



    fun addExp(recipe: RecipeWithIngredientsAndInstructions){

        coroutineScope.launch(Dispatchers.IO) {
            val cookedCountMultiplier =
                if(recipe.recipeEntity.cookedCount < 10){
                    1 - (recipe.recipeEntity.cookedCount * .05)
                }
                else{
                    .50
                }
            val exp = 50 + ((1 + (recipe.recipeEntity.difficulty - 1) * .25) * cookedCountMultiplier)

            repository.addExpToGive(exp.toInt())
        }

    }



    suspend fun setDetailsScreenTarget(recipeName: String){
        repository.setDetailsScreenTarget(recipeName)
    }


    fun removeFromMenu(recipe: RecipeWithIngredientsAndInstructions){

        repository.cleanIngredients()
        repository.cleanShoppingFilters()

        //this should always be true
//        if(recipe.recipeEntity.onMenu == 1){
        coroutineScope.launch(Dispatchers.IO) {
            repository.setToFadeOut(recipe.recipeEntity.recipeName)
            delay(300)
            for (x in 0 until recipe.ingredientsList.size) {
                repository.updateQuantityNeeded(recipe.ingredientsList[x].ingredientName, recipe.ingredientsList[x].quantityNeeded - 1)
                repository.setIngredientQuantityOwned(recipe.ingredientsList[x], 0)
            }
            repository.removeFromMenu(recipe.recipeEntity.recipeName)
        }

    }

    fun toggleFavorite(recipe: RecipeWithIngredientsAndInstructions){
        if(recipe.recipeEntity.isFavorite == 0){
            repository.updateFavorite(recipe.recipeEntity.recipeName, 1)
        }
        else if(recipe.recipeEntity.isFavorite == 1){
            repository.updateFavorite(recipe.recipeEntity.recipeName, 0)
        }

    }

}