package com.example.bearrecipebookapp.viewmodel

import android.app.Application
import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import com.example.bearrecipebookapp.data.RecipeAppDatabase
import com.example.bearrecipebookapp.data.entity.RecipeEntity
import com.example.bearrecipebookapp.data.repository.DetailsScreenRepository
import com.example.bearrecipebookapp.datamodel.RecipeWithIngredientsAndInstructions
import com.example.bearrecipebookapp.datamodel.UiAlertStateDetailsScreenDataModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class DetailsScreenViewModel(application: Application, ): ViewModel() {

    private val repository: DetailsScreenRepository

    private val coroutineScope = CoroutineScope(Dispatchers.IO)

    var detailsScreenData: LiveData<RecipeWithIngredientsAndInstructions>

    val uiAlertState = MutableStateFlow(UiAlertStateDetailsScreenDataModel())



    init {
        val appDb = RecipeAppDatabase.getInstance(application)
        val detailsScreenDao = appDb.DetailsScreenDao()
        repository = DetailsScreenRepository(detailsScreenDao)

        detailsScreenData = repository.detailsScreenData
    }

    fun removeFromMenu(recipe: RecipeWithIngredientsAndInstructions){

        repository.cleanIngredients()
        repository.cleanShoppingFilters()

        for(x in 0 until recipe.ingredientsList.size){
            repository.updateQuantityNeeded(recipe.ingredientsList[x].ingredientName, recipe.ingredientsList[x].quantityNeeded - 1)
            repository.setIngredientQuantityOwned(recipe.ingredientsList[x], 0)
        }
        repository.removeFromMenu(recipe.recipeEntity.recipeName)
    }

    fun addToMenu(recipe: RecipeWithIngredientsAndInstructions){

        repository.cleanIngredients()
        repository.cleanShoppingFilters()

        for(x in 0 until recipe.ingredientsList.size){
            repository.updateQuantityNeeded(recipe.ingredientsList[x].ingredientName, recipe.ingredientsList[x].quantityNeeded + 1)
        }
        repository.addToMenu(recipe.recipeEntity.recipeName)
    }

    fun addCooked(recipe: RecipeWithIngredientsAndInstructions){
        repository.addCooked(recipe)
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


    fun triggerCompletedAlert(recipe: RecipeWithIngredientsAndInstructions){
        uiAlertState.update { currentState ->
            currentState.copy(
                showCompletedAlert = true,
                recipe = recipe
            )
        }
    }

    fun cancelCompletedAlert(){
        uiAlertState.update { currentState ->
            currentState.copy(
                showCompletedAlert = false,
                recipe = RecipeWithIngredientsAndInstructions(RecipeEntity(), listOf(), listOf())
            )
        }
    }

    fun triggerRatingAlert(){
        uiAlertState.update { currentState ->
            currentState.copy(
                showCompletedAlert = false,
                showRatingAlert = true,
            )
        }
    }

    fun cancelRatingAlert(){
        uiAlertState.update { currentState ->
            currentState.copy(
                showRatingAlert = false,
                starCount = 0,
                reviewText = ""
            )
        }
    }

    fun confirmRating(recipeEntity: RecipeEntity) {


            if(uiAlertState.value.isThumbUpSelected && recipeEntity.isFavorite == 0){

                /** write rating to database here */

                uiAlertState.update { currentState ->
                    currentState.copy(
                        showFavoriteAlert = true,
                        showRatingAlert = false,
                        isThumbUpSelected = false,
                        isThumbDownSelected = false
                    )
                }

            }
            else if (uiAlertState.value.isThumbDownSelected || uiAlertState.value.isThumbUpSelected){

                /** write rating to database here */

                uiAlertState.update { currentState ->
                    currentState.copy(
                        showLeaveReviewAlert = true,
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

    fun addToFavorite(recipeName: String){

        coroutineScope.launch {
            repository.setAsFavorite(recipeName)
        }

        uiAlertState.update { currentState ->
            currentState.copy(
                showFavoriteAlert = false,
                showLeaveReviewAlert = true,

            )
        }
    }

    fun doNotAddToFavorite(){
        uiAlertState.update { currentState ->
            currentState.copy(
                showFavoriteAlert = false,
                showLeaveReviewAlert = true,

            )
        }
    }

    fun cancelFavoriteAlert(){
        uiAlertState.update { currentState ->
            currentState.copy(
                showFavoriteAlert = false,
            )
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

//    fun writeReview() {
//        uiAlertState.update { currentState ->
//            currentState.copy(
//                showLeaveReviewAlert = false,
//            )
//        }
//    }
//    fun doNotAddWriteReview() {
//        uiAlertState.update { currentState ->
//            currentState.copy(
//                showLeaveReviewAlert = false,
//            )
//        }
//    }
    fun cancelShowWriteReviewAlert() {
        uiAlertState.update { currentState ->
            currentState.copy(
                showLeaveReviewAlert = false
            )
        }
    }


    fun updateStarCount(count: Int){
        uiAlertState.update { currentState ->
            currentState.copy(
                starCount = count,
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

    fun toggleFavorite(recipe: RecipeWithIngredientsAndInstructions){
        if(recipe.recipeEntity.isFavorite == 0){
            repository.updateFavorite(recipe.recipeEntity.recipeName, 1)
        }
        else if(recipe.recipeEntity.isFavorite == 1){
            repository.updateFavorite(recipe.recipeEntity.recipeName, 0)
        }
    }
}