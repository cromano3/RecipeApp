package com.example.bearrecipebookapp.viewmodel

import android.app.Application
import androidx.compose.ui.text.input.TextFieldValue
import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import com.example.bearrecipebookapp.data.RecipeAppDatabase
import com.example.bearrecipebookapp.data.repository.TopBarRepository
import com.example.bearrecipebookapp.datamodel.HomeScreenDataModel
import com.example.bearrecipebookapp.datamodel.RecipeWithIngredientsAndInstructions
import com.example.bearrecipebookapp.datamodel.UiTopBarDataModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class TopBarViewModel(application: Application, ): ViewModel() {

    private val repository: TopBarRepository

    var detailsScreenData: LiveData<RecipeWithIngredientsAndInstructions>

//    var textFieldValue: LiveData<String>
    var showResults: LiveData<Int>

    private val coroutineScope = CoroutineScope(Dispatchers.IO)

    val uiState = MutableStateFlow(UiTopBarDataModel())

//    val uiAlertState = MutableStateFlow(UiAlertStateDetailsScreenDataModel())

    init {
        val appDb = RecipeAppDatabase.getInstance(application)
        val topBarDao = appDb.TopBarDao()
        repository = TopBarRepository(topBarDao)

        detailsScreenData = repository.detailsScreenData
//        textFieldValue = repository.textFieldValue
        showResults = repository.showResults

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

    fun  toggleFavorite(recipe: RecipeWithIngredientsAndInstructions){
        if(recipe.recipeEntity.isFavorite == 0){
            repository.updateFavorite(recipe.recipeEntity.recipeName, 1)
        }
        else if(recipe.recipeEntity.isFavorite == 1){
            repository.updateFavorite(recipe.recipeEntity.recipeName, 0)
        }
    }

    fun liveSearchForClick(){

        val badChars =  "[\$&+,:;=?@#|/'<>.^*()%!-]".toCharArray()
        var hasBad = false

        for(x in badChars.indices){
            if(uiState.value.currentInput.text.contains(badChars[x]))
            {
                hasBad = true
            }
        }

        if(uiState.value.currentInput.text.isNotEmpty() && !hasBad){

            coroutineScope.launch(Dispatchers.IO) {

                withContext(Dispatchers.IO){repository.clearResults()}

                val data: List<HomeScreenDataModel> = repository.getRecipes()

                for(x in 0 until (data.size)){
                    //check names
                    if (uiState.value.currentInput.text.toRegex(RegexOption.IGNORE_CASE).containsMatchIn(data[x].recipeEntity.recipeName)){

                        withContext(Dispatchers.IO){ repository.setSearchResult(recipeName = data[x].recipeEntity.recipeName, isResult = 1)}
                    }

                    for(y in 0 until (data[x].ingredientsList.size)){
                        if (uiState.value.currentInput.text.toRegex(RegexOption.IGNORE_CASE).containsMatchIn(data[x].ingredientsList[y].ingredientName)){

                            withContext(Dispatchers.IO){ repository.setSearchResult(recipeName = data[x].recipeEntity.recipeName, isResult = 1)}
                        }
                    }
                    for (y in 0 until (data[x].filtersList.size)){
                        if (uiState.value.currentInput.text.toRegex(RegexOption.IGNORE_CASE).containsMatchIn(data[x].filtersList[y].filterName)){

                            withContext(Dispatchers.IO){ repository.setSearchResult(recipeName = data[x].recipeEntity.recipeName, isResult = 1)}

                        }
                    }
                }

                repository.setShowResults(1)

//                uiState.update {
//                    it.copy(
//                        showResults = true
//                    )
//                }
            }
        }
    }

    fun setShowResults(isShown: Int){
        coroutineScope.launch(Dispatchers.IO) {
            repository.setShowResults(isShown)
        }
    }

    fun updateSearchText(){
        coroutineScope.launch(Dispatchers.IO){
            uiState.update {
                it.copy(
                    currentInput = TextFieldValue(repository.getTextFieldValue())
                )
            }
        }
    }

    fun updatePreview(textFieldValueInput: TextFieldValue, input: String){
        coroutineScope.launch(Dispatchers.IO) {

            repository.clearResults()
            repository.setShowResults(0)

            uiState.update {
                it.copy(
                    currentInput = textFieldValueInput
                )
            }

//            repository.setTextFieldValue(input)

//            var previewList = mutableListOf<SearchItemWithCategory>()
            var previewList = mutableListOf<String>()

            val badChars =  "[\$&+,:;=?@#|/'<>.^*()%!-]".toCharArray()
            var hasBad = false

            for(x in badChars.indices){
                if(input.contains(badChars[x]))
                {
                    hasBad = true
                }
            }
            if(input == "" || hasBad){
//                previewList = mutableListOf<SearchItemWithCategory>()
                previewList = mutableListOf<String>()

            }
            else{


                for(x in 0 until uiState.value.recipeNames.size){
                    if(input.toRegex(RegexOption.IGNORE_CASE).containsMatchIn(uiState.value.recipeNames[x])){
//                        previewList.add(SearchItemWithCategory(name = uiState.value.recipeNames[x], category = "Recipe"))
                        previewList.add(uiState.value.recipeNames[x])
                    }
                }

                for(x in 0 until uiState.value.ingredientNames.size){
                    if(input.toRegex(RegexOption.IGNORE_CASE).containsMatchIn(uiState.value.ingredientNames[x])){
//                        previewList.add(SearchItemWithCategory(name = uiState.value.ingredientNames[x], category = "Ingredient"))
                        previewList.add(uiState.value.ingredientNames[x])
                    }
                }

                for(x in 0 until uiState.value.filterNames.size){
                    if(input.toRegex(RegexOption.IGNORE_CASE).containsMatchIn(uiState.value.filterNames[x])){
//                        previewList.add(SearchItemWithCategory(name = uiState.value.filterNames[x], category = "Filter"))
                        previewList.add(uiState.value.filterNames[x])
                    }
                }

            }

            if(previewList.size > 1){
//                previewList = previewList.sortedWith(compareBy{it.name}) as MutableList<SearchItemWithCategory>
                previewList = previewList.sortedWith(compareBy{it}) as MutableList<String>
            }



            repository.setPreviewList(previewList.joinToString(separator = ","))

//            uiState.update { currentState ->
//                currentState.copy(
//                    previewList = previewList
//                )
//            }

        }
    }
}