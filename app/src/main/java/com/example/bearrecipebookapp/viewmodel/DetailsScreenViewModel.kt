package com.example.bearrecipebookapp.viewmodel

import android.app.Application
import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import com.example.bearrecipebookapp.data.DetailsScreenRepository
import com.example.bearrecipebookapp.data.RecipeAppDatabase
import com.example.bearrecipebookapp.datamodel.RecipeWithIngredientsAndInstructions

class DetailsScreenViewModel(application: Application,
                        //     recipeName: String
): ViewModel() {

    private val repository: DetailsScreenRepository

    var detailsScreenData: LiveData<RecipeWithIngredientsAndInstructions>



    init {
        val appDb = RecipeAppDatabase.getInstance(application)
        val detailsScreenDao = appDb.DetailsScreenDao()
        repository = DetailsScreenRepository(detailsScreenDao)

        detailsScreenData = repository.detailsScreenData
    }

    fun toggleFavorite(recipe: RecipeWithIngredientsAndInstructions){
        if(recipe.recipeEntity.onMenu == 0){
            for(x in 0 until recipe.ingredientsList.size){
                repository.updateQuantityNeeded(recipe.ingredientsList[x].ingredientName, recipe.ingredientsList[x].quantityNeeded + 1)
            }
            repository.updateMenu(recipe.recipeEntity.recipeName, 1)
        }
        else if(recipe.recipeEntity.onMenu == 1){
            for(x in 0 until recipe.ingredientsList.size){
                repository.updateQuantityNeeded(recipe.ingredientsList[x].ingredientName, recipe.ingredientsList[x].quantityNeeded - 1)
                repository.setIngredientQuantityOwned(recipe.ingredientsList[x], 0)
            }
            repository.updateMenu(recipe.recipeEntity.recipeName, 0)
        }
    }
}