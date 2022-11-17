package com.example.bearrecipebookapp.viewmodel

import android.app.Application
import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import com.example.bearrecipebookapp.data.MenuScreenRepository
import com.example.bearrecipebookapp.data.RecipeAppDatabase
import com.example.bearrecipebookapp.datamodel.RecipeWithIngredients

class MenuScreenViewModel(application: Application): ViewModel() {

    private val repository: MenuScreenRepository

    var menuScreenData: LiveData<List<RecipeWithIngredients>>



    init {
        val appDb = RecipeAppDatabase.getInstance(application)
        val menuScreenDao = appDb.MenuScreenDao()
        repository = MenuScreenRepository(menuScreenDao)

        menuScreenData = repository.menuScreenData
    }

    fun setDetailsScreenTarget(recipeName: String){
        repository.setDetailsScreenTarget(recipeName)
    }


    fun removeFromMenu(recipe: RecipeWithIngredients){

        //this should always be true
//        if(recipe.recipeEntity.onMenu == 1){
        for(x in 0 until recipe.ingredientsList.size){
            repository.updateQuantityNeeded(recipe.ingredientsList[x].ingredientName, recipe.ingredientsList[x].quantityNeeded - 1)
            repository.setIngredientQuantityOwned(recipe.ingredientsList[x], 0)
        }
        repository.removeFromMenu(recipe.recipeEntity.recipeName)

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