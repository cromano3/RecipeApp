package com.christopherromano.culinarycompanion.viewmodel

import android.app.Application
import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import com.christopherromano.culinarycompanion.data.RecipeAppDatabase
import com.christopherromano.culinarycompanion.data.repository.SearchScreenRepository
import com.christopherromano.culinarycompanion.datamodel.HomeScreenDataModel
import com.christopherromano.culinarycompanion.datamodel.UiSearchScreenDataModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class SearchScreenViewModel (application: Application): ViewModel() {

    private val repository: SearchScreenRepository

    private val coroutineScope = CoroutineScope(Dispatchers.IO)

    val uiState = MutableStateFlow(UiSearchScreenDataModel())

    val results: LiveData<List<HomeScreenDataModel>>
    val previewList: LiveData<String>
    val showResults: LiveData<Int>

    init {

        val appDb = RecipeAppDatabase.getInstance(application)
        val searchScreenDao = appDb.SearchScreenDao()
        repository = SearchScreenRepository(searchScreenDao)

        results = repository.results
        showResults = repository.showResults
        previewList = repository.previewList

    }

    fun triggerAlert(recipe: HomeScreenDataModel){
        uiState.update { currentState ->
            currentState.copy(
                showAlert = true,
                alertRecipe = recipe
            )
        }
    }
    fun cancelAlert(){
        uiState.update { currentState ->
            currentState.copy(
                showAlert = false,
                alertRecipe = HomeScreenDataModel()
            )
        }
    }

    fun liveSearchForPush(input: String){

        if(input.isNotEmpty()){

            coroutineScope.launch(Dispatchers.IO) {

                repository.clearResults()

                val data: List<HomeScreenDataModel> = repository.getRecipes()

                for(x in 0 until (data.size)){
                    //check names
                    if (input.toRegex(RegexOption.IGNORE_CASE).containsMatchIn(data[x].recipeEntity.recipeName)){
                            repository.setSearchResult(recipeName = data[x].recipeEntity.recipeName, isResult = 1)
                    }

                    for(y in 0 until (data[x].ingredientsList.size)){
                        if (input.toRegex(RegexOption.IGNORE_CASE).containsMatchIn(data[x].ingredientsList[y].ingredientName)){
                                repository.setSearchResult(recipeName = data[x].recipeEntity.recipeName, isResult = 1)
                        }
                    }
                    for (y in 0 until (data[x].filtersList.size)){
                        if (input.toRegex(RegexOption.IGNORE_CASE).containsMatchIn(data[x].filtersList[y].filterName)){
                                repository.setSearchResult(recipeName = data[x].recipeEntity.recipeName, isResult = 1)
                        }
                    }
                }

                repository.setShowResults(1)
                repository.setTextFieldValue(input)

            }

        }

    }

    fun toggleFavorite(recipe: HomeScreenDataModel){
        if(recipe.recipeEntity.isFavorite == 0){
            repository.updateFavorite(recipe.recipeEntity.recipeName, 1)
        }
        else if(recipe.recipeEntity.isFavorite == 1){
            repository.updateFavorite(recipe.recipeEntity.recipeName, 0)
        }
    }


    fun toggleMenu(recipe: HomeScreenDataModel){
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