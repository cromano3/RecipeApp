package com.example.bearrecipebookapp.viewmodel

import android.app.Application
import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import com.example.bearrecipebookapp.data.RecipeAppDatabase
import com.example.bearrecipebookapp.data.TopBarRepository
import com.example.bearrecipebookapp.datamodel.RecipeWithIngredientsAndInstructions

class TopBarViewModel(application: Application, ): ViewModel() {

    private val repository: TopBarRepository

    var detailsScreenData: LiveData<RecipeWithIngredientsAndInstructions>

//    val uiAlertState = MutableStateFlow(UiAlertStateDetailsScreenDataModel())

    init {
        val appDb = RecipeAppDatabase.getInstance(application)
        val topBarDao = appDb.TopBarDao()
        repository = TopBarRepository(topBarDao)

        detailsScreenData = repository.detailsScreenData
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