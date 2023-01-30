package com.example.bearrecipebookapp.viewmodel

import android.app.Application
import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import com.example.bearrecipebookapp.data.RecipeAppDatabase
import com.example.bearrecipebookapp.data.entity.RecipeEntity
import com.example.bearrecipebookapp.data.repository.MenuScreenRepository
import com.example.bearrecipebookapp.datamodel.RecipeWithIngredientsAndInstructions
import com.example.bearrecipebookapp.datamodel.UiAlertStateMenuScreenDataModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class MenuScreenViewModel(application: Application): ViewModel() {

    private val coroutineScope = CoroutineScope(Dispatchers.IO)

    private val repository: MenuScreenRepository

    var menuScreenData: LiveData<List<RecipeWithIngredientsAndInstructions>>

    val uiAlertState = MutableStateFlow(UiAlertStateMenuScreenDataModel())



    init {
        val appDb = RecipeAppDatabase.getInstance(application)
        val menuScreenDao = appDb.MenuScreenDao()
        repository = MenuScreenRepository(menuScreenDao)

        menuScreenData = repository.menuScreenData
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
            )
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



    fun setDetailsScreenTarget(recipeName: String){
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