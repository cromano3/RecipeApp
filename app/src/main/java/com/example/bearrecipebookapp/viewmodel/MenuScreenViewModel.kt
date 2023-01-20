package com.example.bearrecipebookapp.viewmodel

import android.app.Application
import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import com.example.bearrecipebookapp.data.repository.MenuScreenRepository
import com.example.bearrecipebookapp.data.RecipeAppDatabase
import com.example.bearrecipebookapp.data.entity.RecipeEntity
import com.example.bearrecipebookapp.datamodel.RecipeWithIngredients
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

    var menuScreenData: LiveData<List<RecipeWithIngredients>>

    val uiAlertState = MutableStateFlow(UiAlertStateMenuScreenDataModel())



    init {
        val appDb = RecipeAppDatabase.getInstance(application)
        val menuScreenDao = appDb.MenuScreenDao()
        repository = MenuScreenRepository(menuScreenDao)

        menuScreenData = repository.menuScreenData
    }

    fun triggerCompletedAlert(recipe: RecipeWithIngredients){
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
                recipe = RecipeWithIngredients(RecipeEntity(), listOf())
            )
        }
    }

    fun triggerRemoveAlert(recipe: RecipeWithIngredients){
        uiAlertState.update { currentState ->
            currentState.copy(
                showRemoveAlert = true,
                recipe = recipe
            )
        }
    }

    fun addCooked(recipe: RecipeWithIngredients){
        repository.addCooked(recipe.recipeEntity.recipeName)
    }

    fun addExp(recipe: RecipeWithIngredients){

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

    fun cancelRemoveAlert(){
        uiAlertState.update { currentState ->
            currentState.copy(
                showRemoveAlert = false,
                recipe = RecipeWithIngredients(RecipeEntity(), listOf())
            )
        }
    }


    fun setDetailsScreenTarget(recipeName: String){
        repository.setDetailsScreenTarget(recipeName)
    }


    fun removeFromMenu(recipe: RecipeWithIngredients){

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

    fun toggleFavorite(recipe: RecipeWithIngredients){
        if(recipe.recipeEntity.isFavorite == 0){
            repository.updateFavorite(recipe.recipeEntity.recipeName, 1)
        }
        else if(recipe.recipeEntity.isFavorite == 1){
            repository.updateFavorite(recipe.recipeEntity.recipeName, 0)
        }

    }

}