package com.example.bearrecipebookapp.viewmodel

import android.app.Application
import androidx.lifecycle.ViewModel
import com.example.bearrecipebookapp.data.RecipeAppDatabase
import com.example.bearrecipebookapp.data.SearchScreenRepository
import com.example.bearrecipebookapp.datamodel.HomeScreenDataModel
import com.example.bearrecipebookapp.datamodel.SearchItemWithCategory
import com.example.bearrecipebookapp.datamodel.UiSearchScreenDataModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class SearchScreenViewModel (application: Application): ViewModel() {

    private val repository: SearchScreenRepository

    private val coroutineScope = CoroutineScope(Dispatchers.IO)

    val uiState = MutableStateFlow(UiSearchScreenDataModel())


    init {

        val appDb = RecipeAppDatabase.getInstance(application)
        val searchScreenDao = appDb.SearchScreenDao()
        repository = SearchScreenRepository(searchScreenDao)

        //Get reference list from DB and assign to uiState
        setReferenceList()

    }

    private fun setReferenceList(){
        coroutineScope.launch(Dispatchers.IO) {
            val recipeNames = repository.getRecipeNamesReferenceList()
            val ingredientNames = repository.getIngredientNamesReferenceList()
            val filterNames = repository.getFilterNamesReferenceList()

            uiState.update { currentState ->
                currentState.copy(
                    recipeNames = recipeNames,
                    ingredientNames = ingredientNames,
                    filterNames = filterNames
                )
            }
        }
    }

    fun searchFor(input: String) {

        coroutineScope.launch(Dispatchers.IO) {

            val recipeNameHits = mutableListOf<SearchItemWithCategory>()
            val ingredientNameHits = mutableListOf<SearchItemWithCategory>()
            val filterNameHits = mutableListOf<SearchItemWithCategory>()

            val data: List<HomeScreenDataModel> = repository.getRecipes()
            val results = mutableListOf<HomeScreenDataModel>()

            for (x in 0 until uiState.value.recipeNames.size) {
                if (input.toRegex(RegexOption.IGNORE_CASE)
                        .containsMatchIn(uiState.value.recipeNames[x])
                ) {
                    recipeNameHits.add(
                        SearchItemWithCategory(
                            name = uiState.value.recipeNames[x],
                            category = "Recipe"
                        )
                    )
                }
            }

            for (x in 0 until uiState.value.ingredientNames.size) {
                if (input.toRegex(RegexOption.IGNORE_CASE)
                        .containsMatchIn(uiState.value.ingredientNames[x])
                ) {
                    ingredientNameHits.add(
                        SearchItemWithCategory(
                            name = uiState.value.ingredientNames[x],
                            category = "Ingredient"
                        )
                    )
                }
            }

            for (x in 0 until uiState.value.filterNames.size) {
                if (input.toRegex(RegexOption.IGNORE_CASE)
                        .containsMatchIn(uiState.value.filterNames[x])
                ) {
                    filterNameHits.add(
                        SearchItemWithCategory(
                            name = uiState.value.filterNames[x],
                            category = "Filter"
                        )
                    )
                }
            }


            for (x in data.indices) {
                for (y in recipeNameHits.indices) {
                    if (recipeNameHits[y].name == data[x].recipeEntity.recipeName) {
                        if (!results.contains(data[x])) {
                            results.add(data[x])
                        }
                    }
                }
                for (y in data[x].ingredientsList.indices) {
                    for (z in ingredientNameHits.indices) {
                        if (ingredientNameHits[z].name == data[x].ingredientsList[y].ingredientName) {
                            if (!results.contains(data[x])) {
                                results.add(data[x])
                            }
                        }
                    }
                }
                for (y in data[x].filtersList.indices) {
                    for (z in filterNameHits.indices) {
                        if (filterNameHits[z].name == data[x].filtersList[y].filterName) {
                            if (!results.contains(data[x])) {
                                results.add(data[x])
                            }
                        }
                    }
                }
            }

            uiState.update {
                it.copy(
                    clickSearchResults = results,
                    showResults = true
                )
            }
        }
    }

    fun searchForClick(item: SearchItemWithCategory){

        coroutineScope.launch(Dispatchers.IO) {

            val data: List<HomeScreenDataModel> = repository.getRecipes()
            val results = mutableListOf<HomeScreenDataModel>()

            if(item.category == "Recipe"){
                for(x in data.indices){
                    if(data[x].recipeEntity.recipeName == item.name){
                        results.add(data[x])
                        break
                    }
                }
            }
            else if(item.category == "Ingredient"){
                for(x in data.indices){
                    for(y in data[x].ingredientsList.indices){
                        if(data[x].ingredientsList[y].ingredientName == item.name){
                            results.add(data[x])
                            break
                        }
                    }
                }

            }
            else if(item.category == "Filter"){
                for(x in data.indices){
                    for(y in data[x].filtersList.indices){
                        if(data[x].filtersList[y].filterName == item.name){
                            results.add(data[x])
                            break
                        }
                    }
                }
            }

            uiState.update {
                it.copy(
                    clickSearchResults = results,
                    showResults = true)
            }

        }

    }

    fun updatePreview(input: String){
        coroutineScope.launch(Dispatchers.IO) {

            uiState.update {
                it.copy(
                    showResults = false
                )
            }

            var previewList = mutableListOf<SearchItemWithCategory>()

            if(input == ""){
                previewList = mutableListOf<SearchItemWithCategory>()

            }
            else{
                for(x in 0 until uiState.value.recipeNames.size){
                    if(input.toRegex(RegexOption.IGNORE_CASE).containsMatchIn(uiState.value.recipeNames[x])){
                        previewList.add(SearchItemWithCategory(name = uiState.value.recipeNames[x], category = "Recipe"))
                    }
                }

                for(x in 0 until uiState.value.ingredientNames.size){
                    if(input.toRegex(RegexOption.IGNORE_CASE).containsMatchIn(uiState.value.ingredientNames[x])){
                        previewList.add(SearchItemWithCategory(name = uiState.value.ingredientNames[x], category = "Ingredient"))
                    }
                }

                for(x in 0 until uiState.value.filterNames.size){
                    if(input.toRegex(RegexOption.IGNORE_CASE).containsMatchIn(uiState.value.filterNames[x])){
                        previewList.add(SearchItemWithCategory(name = uiState.value.filterNames[x], category = "Filter"))
                    }
                }

            }

            if(previewList.size > 1){
                previewList = previewList.sortedWith(compareBy{it.name}) as MutableList<SearchItemWithCategory>
            }


            uiState.update { currentState ->
                currentState.copy(
                    previewList = previewList
                )
            }

        }
    }
}