package com.example.bearrecipebookapp.viewmodel

import android.app.Application
import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import com.example.bearrecipebookapp.data.RecipeAppDatabase
import com.example.bearrecipebookapp.data.repository.SearchScreenRepository
import com.example.bearrecipebookapp.datamodel.HomeScreenDataModel
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

//    val allRecipes: LiveData<List<HomeScreenDataModel>>
    val results: LiveData<List<HomeScreenDataModel>>
    val previewList: LiveData<String>
    val showResults: LiveData<Int>

    init {

        val appDb = RecipeAppDatabase.getInstance(application)
        val searchScreenDao = appDb.SearchScreenDao()
        repository = SearchScreenRepository(searchScreenDao)

//        allRecipes = repository.allRecipes
        results = repository.results
        showResults = repository.showResults
        previewList = repository.previewList

//        coroutineScope.launch { repository.clearResults() }

        //Get reference list from DB and assign to uiState
//        setReferenceList()

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

//    private fun setReferenceList(){
//        coroutineScope.launch(Dispatchers.IO) {
//            val recipeNames = repository.getRecipeNamesReferenceList()
//            val ingredientNames = repository.getIngredientNamesReferenceList()
//            val filterNames = repository.getFilterNamesReferenceList()
//
//            uiState.update { currentState ->
//                currentState.copy(
//                    recipeNames = recipeNames,
//                    ingredientNames = ingredientNames,
//                    filterNames = filterNames
//                )
//            }
//        }
//    }

//    fun liveSearchForClick(){
//
//        val badChars =  "[\$&+,:;=?@#|/'<>.^*()%!-]".toCharArray()
//        var hasBad = false
//
//        for(x in badChars.indices){
//            if(uiState.value.currentInput.text.contains(badChars[x]))
//            {
//                hasBad = true
//            }
//        }
//
//        if(uiState.value.currentInput.text.isNotEmpty() && !hasBad){
//
//            coroutineScope.launch(Dispatchers.IO) {
//
//                withContext(Dispatchers.IO){repository.clearResults()}
//
//                val data: List<HomeScreenDataModel> = repository.getRecipes()
//
//                for(x in 0 until (data.size)){
//                    //check names
//                    if (uiState.value.currentInput.text.toRegex(RegexOption.IGNORE_CASE).containsMatchIn(data[x].recipeEntity.recipeName)){
//
//                        withContext(Dispatchers.IO){ repository.setSearchResult(recipeName = data[x].recipeEntity.recipeName, isResult = 1)}
//                    }
//
//                    for(y in 0 until (data[x].ingredientsList.size)){
//                        if (uiState.value.currentInput.text.toRegex(RegexOption.IGNORE_CASE).containsMatchIn(data[x].ingredientsList[y].ingredientName)){
//
//                            withContext(Dispatchers.IO){ repository.setSearchResult(recipeName = data[x].recipeEntity.recipeName, isResult = 1)}
//                        }
//                    }
//                    for (y in 0 until (data[x].filtersList.size)){
//                        if (uiState.value.currentInput.text.toRegex(RegexOption.IGNORE_CASE).containsMatchIn(data[x].filtersList[y].filterName)){
//
//                            withContext(Dispatchers.IO){ repository.setSearchResult(recipeName = data[x].recipeEntity.recipeName, isResult = 1)}
//
//                        }
//                    }
//                }
//
//                uiState.update {
//                    it.copy(
//                        showResults = true
//                    )
//                }
//
//            }
//
//        }
//
//    }

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

//                uiState.update {
//                    it.copy(
//                        currentInput = TextFieldValue(text = input),
//                        showResults = true
//                    )
//                }
            }

        }

    }


//    fun updatePreview(textFieldValueInput: TextFieldValue, input: String){
//        coroutineScope.launch(Dispatchers.IO) {
//
//            repository.clearResults()
//
//            uiState.update {
//                it.copy(
//                    showResults = false,
//                    currentInput = textFieldValueInput
//                )
//            }
//
//            var previewList = mutableListOf<SearchItemWithCategory>()
//
//            val badChars =  "[\$&+,:;=?@#|/'<>.^*()%!-]".toCharArray()
//            var hasBad = false
//
//            for(x in badChars.indices){
//                if(input.contains(badChars[x]))
//                {
//                    hasBad = true
//                }
//            }
//            if(input == "" || hasBad){
//                previewList = mutableListOf<SearchItemWithCategory>()
//
//            }
//            else{
//
//
//                for(x in 0 until uiState.value.recipeNames.size){
//                    if(input.toRegex(RegexOption.IGNORE_CASE).containsMatchIn(uiState.value.recipeNames[x])){
//                        previewList.add(SearchItemWithCategory(name = uiState.value.recipeNames[x], category = "Recipe"))
//                    }
//                }
//
//                for(x in 0 until uiState.value.ingredientNames.size){
//                    if(input.toRegex(RegexOption.IGNORE_CASE).containsMatchIn(uiState.value.ingredientNames[x])){
//                        previewList.add(SearchItemWithCategory(name = uiState.value.ingredientNames[x], category = "Ingredient"))
//                    }
//                }
//
//                for(x in 0 until uiState.value.filterNames.size){
//                    if(input.toRegex(RegexOption.IGNORE_CASE).containsMatchIn(uiState.value.filterNames[x])){
//                        previewList.add(SearchItemWithCategory(name = uiState.value.filterNames[x], category = "Filter"))
//                    }
//                }
//
//            }
//
//            if(previewList.size > 1){
//                previewList = previewList.sortedWith(compareBy{it.name}) as MutableList<SearchItemWithCategory>
//            }
//
//
//            uiState.update { currentState ->
//                currentState.copy(
//                    previewList = previewList
//                )
//            }
//
//        }
//    }

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

    suspend fun setDetailsScreenTarget(recipeName: String){
        repository.setDetailsScreenTarget(recipeName)
    }
}