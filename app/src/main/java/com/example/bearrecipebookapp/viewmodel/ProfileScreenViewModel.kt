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
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.update

class ProfileScreenViewModel(application: Application): ViewModel() {

    private val repository: ProfileScreenRepository

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
    }

    fun updateExp(level: Int, currentExp: Int, expToGive: Int, levelUp: Boolean){

        if(levelUp){

            var nextTotalTnl: Int = 0

            when(level){
                9 -> nextTotalTnl = 2700
                8 -> nextTotalTnl = 2200
                7 -> nextTotalTnl = 1750
                6 -> nextTotalTnl = 1350
                5 -> nextTotalTnl = 1000
                4 -> nextTotalTnl = 700
                3 -> nextTotalTnl = 450
                2 -> nextTotalTnl = 250
                1 -> nextTotalTnl = 100
            }

            val expChange = nextTotalTnl - currentExp

            repository.addToExp(expChange)
            repository.removeFromExpToGive(expChange)
        }
        else{

            repository.addToExp(expToGive)
            repository.clearExpToGive()

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