package com.example.bearrecipebookapp.viewmodel

import android.app.Application
import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import com.example.bearrecipebookapp.data.IngredientEntity
import com.example.bearrecipebookapp.data.RecipeAppDatabase
import com.example.bearrecipebookapp.data.ShoppingListScreenRepository
import com.example.bearrecipebookapp.datamodel.RecipeWithIngredients
import com.example.bearrecipebookapp.datamodel.ShoppingScreenUiState
import com.example.bearrecipebookapp.datamodel.UiAlertStateShoppingScreenDataModel
import kotlinx.coroutines.*
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.update

class ShoppingListScreenViewModel (application: Application): ViewModel() {

    private val repository: ShoppingListScreenRepository
    private val coroutineScope = CoroutineScope(Dispatchers.IO)

    var shoppingListScreenData: LiveData<List<RecipeWithIngredients>>
    var selectedIngredients: LiveData<List<IngredientEntity>>


    val shoppingScreenUiState = MutableStateFlow(ShoppingScreenUiState())
    val uiAlertState = MutableStateFlow(UiAlertStateShoppingScreenDataModel())

    init {
        val appDb = RecipeAppDatabase.getInstance(application)
        val shoppingListScreenDao = appDb.ShoppingListScreenDao()
        repository = ShoppingListScreenRepository(shoppingListScreenDao)

        shoppingListScreenData = repository.shoppingListScreenData
        selectedIngredients = repository.selectedIngredients

        coroutineScope.launch(Dispatchers.IO) {
            async(Dispatchers.IO){repository.cleanIngredients()}
            async(Dispatchers.IO) {repository.cleanFilters()}
        }
    }



    fun filterBy(recipe: RecipeWithIngredients){


        coroutineScope.launch(Dispatchers.IO) {

            shoppingScreenUiState.value.isWorking = true


            if (recipe.recipeEntity.isShoppingFilter == 0 ||
                recipe.recipeEntity.isShoppingFilter == 1
            ) {

                    withContext(Dispatchers.IO) { repository.removeOtherFilters(recipe.recipeEntity.recipeName) }
                    withContext(Dispatchers.IO) { repository.filterBy(recipe.recipeEntity.recipeName) }

                var match = false

                for (x in 0 until (selectedIngredients.value?.size ?: 0)) {
                    for (y in 0 until recipe.ingredientsList.size) {
                        if (selectedIngredients.value?.get(x)?.ingredientName == recipe.ingredientsList[y].ingredientName) {
                            match = true
                            break
                        }
                    }
                    if (!match) {
                        selectedIngredients.value?.get(x)?.ingredientName?.let {
                            repository.setIngredientToNotShown(it)
                        }
                    } else {
                        selectedIngredients.value?.get(x)?.ingredientName?.let {
                            repository.setIngredientToShown(it)
                        }
                    }
                    match = false
                }

            } else if (recipe.recipeEntity.isShoppingFilter == 2) {

                    withContext(Dispatchers.IO) {
                        repository.cleanIngredients();
                        repository.cleanFilters()
                    }

            }

            shoppingScreenUiState.value.isWorking = false

        }

    }

    fun triggerAddRecipeOrCustomItemAlert(){
        uiAlertState.update {
            it.copy(showAddRecipeOrCustomItemAlert = true)
        }
    }

    fun cancelAddRecipeOrCustomItemAlert(){
        uiAlertState.update {
            it.copy(showAddRecipeOrCustomItemAlert = false)
        }
    }

    fun triggerAddCustomItemAlert() {
        uiAlertState.update {
            it.copy(showAddCustomItemAlert = true)
        }
    }

    fun cancelAddCustomItemAlert() {
        uiAlertState.update {
            it.copy(showAddCustomItemAlert = false)
        }
    }



    fun ingredientSelected(ingredientEntity: IngredientEntity){
        shoppingScreenUiState.update {
            it.copy(counter = shoppingScreenUiState.value.counter + 1)
        }

        repository.setIngredientToOwned(ingredientEntity)
    }

    fun ingredientDeselected(ingredientEntity: IngredientEntity){

        shoppingScreenUiState.update {
            it.copy(counter = shoppingScreenUiState.value.counter + 1)
        }

        repository.setIngredientToNotOwned(ingredientEntity)
    }



    fun setDetailsScreenTarget(recipeName: String){
        repository.setDetailsScreenTarget(recipeName)
    }
}