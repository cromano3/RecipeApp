package com.example.bearrecipebookapp.viewmodel

import android.app.Application
import androidx.compose.ui.text.input.TextFieldValue
import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import com.example.bearrecipebookapp.data.RecipeAppDatabase
import com.example.bearrecipebookapp.data.entity.IngredientEntity
import com.example.bearrecipebookapp.data.entity.ShoppingListCustomItemsEntity
import com.example.bearrecipebookapp.data.repository.AppRepository
import com.example.bearrecipebookapp.datamodel.*
import kotlinx.coroutines.*
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.update

class AppViewModel(application: Application): ViewModel() {

    private val repository: AppRepository

    private val coroutineScope = CoroutineScope(Dispatchers.IO)

    //shopping list screen data models
    val shoppingListScreenUiState = MutableStateFlow(ShoppingScreenUiState())
    val shoppingListScreenUiAlertState = MutableStateFlow(UiAlertStateShoppingScreenDataModel())
    val detailsScreenUiState = MutableStateFlow(DetailsScreenUiState())
    //shopping list screen live data
    var shoppingListScreenData: LiveData<List<RecipeWithIngredients>>
    var selectedIngredients: LiveData<List<IngredientEntity>>
    var customIngredients: LiveData<List<ShoppingListCustomItemsEntity>>
    //profile screen data models
    val profileScreenUiState = MutableStateFlow(ProfileScreenDataModel())
    val profileScreenUiAlertState = MutableStateFlow(UiAlertStateProfileScreenDataModel())
    val uiStarsState = MutableStateFlow(ProfileScreenStarsDataModel())
    //profile screen live data
    var favoritesData: LiveData<List<RecipeWithIngredientsAndInstructions>>
    var cookedData: LiveData<List<RecipeWithIngredients>>

    init {
        val appDb = RecipeAppDatabase.getInstance(application)
        val appDao = appDb.AppDao()
        repository = AppRepository(appDao)

        //shopping list screen live data
        shoppingListScreenData = repository.shoppingListScreenData
        selectedIngredients = repository.selectedIngredients
        customIngredients = repository.customIngredients
        //profile screen live data
        favoritesData = repository.favoritesData
        cookedData = repository.cookedData

        coroutineScope.launch(Dispatchers.IO) {
            async(Dispatchers.IO) {repository.cleanIngredients()}
            async(Dispatchers.IO) {repository.cleanFilters()}
        }
    }



    /** shopping screen **/
    fun filterBy(recipe: RecipeWithIngredients){


        coroutineScope.launch(Dispatchers.IO) {

            shoppingListScreenUiState.value.isWorking = true


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

                shoppingListScreenUiState.update {
                    it.copy(isFiltered = true)
                }


            } else if (recipe.recipeEntity.isShoppingFilter == 2) {

                withContext(Dispatchers.IO) {
                    repository.cleanIngredients();
                    repository.cleanFilters()
                }

                shoppingListScreenUiState.update {
                    it.copy(isFiltered = false)
                }

            }

            shoppingListScreenUiState.value.isWorking = false

        }

    }

    fun addCustomItem(){
        if (shoppingListScreenUiAlertState.value.inputText.text.length > 20) {

            shoppingListScreenUiAlertState.update {
                it.copy(
                    inputText = TextFieldValue(""),
                )
            }

        }
        else {
            repository.addCustomItem(ShoppingListCustomItemsEntity(shoppingListScreenUiAlertState.value.inputText.text))

            shoppingListScreenUiAlertState.update {
                it.copy(
                    showAddCustomItemAlert = false,
                    inputText = TextFieldValue(""),
                )
            }

        }
    }


    fun updateInputText(input: TextFieldValue){


        shoppingListScreenUiAlertState.update {
            it.copy(inputText = input)
        }
    }

    fun triggerAddRecipeOrCustomItemAlert(){
        shoppingListScreenUiAlertState.update {
            it.copy(showAddRecipeOrCustomItemAlert = true)
        }
    }

    fun cancelAddRecipeOrCustomItemAlert(){
        shoppingListScreenUiAlertState.update {
            it.copy(showAddRecipeOrCustomItemAlert = false)
        }
    }

    fun triggerAddCustomItemAlert() {
        shoppingListScreenUiAlertState.update {
            it.copy(showAddCustomItemAlert = true)
        }
    }

    fun cancelAddCustomItemAlert() {
        shoppingListScreenUiAlertState.update {
            it.copy(
                showAddCustomItemAlert = false,
                inputText = TextFieldValue(""),
            )
        }
    }

    fun addTutorialAlert(){
        coroutineScope.launch(Dispatchers.IO){
            repository.addTutorialAlert()
        }
    }


    fun customItemSelected(item: ShoppingListCustomItemsEntity){
        repository.setCustomItemToSelected(item)
    }

    fun customItemDeselected(item: ShoppingListCustomItemsEntity){
        repository.setCustomItemToDeselected(item)
    }

    fun deleteCustomItem(item: ShoppingListCustomItemsEntity){
        repository.deleteCustomItem(item)
    }

    fun ingredientSelected(ingredientEntity: IngredientEntity){
        shoppingListScreenUiState.update {
            it.copy(counter = shoppingListScreenUiState.value.counter + 1)
        }

        repository.setIngredientToOwned(ingredientEntity)
    }

    fun ingredientDeselected(ingredientEntity: IngredientEntity){

        shoppingListScreenUiState.update {
            it.copy(counter = shoppingListScreenUiState.value.counter + 1)
        }

        repository.setIngredientToNotOwned(ingredientEntity)
    }


    suspend fun getDetailsScreenData(recipeName: String){

        val result = repository.getDetailsScreenData(recipeName)

        detailsScreenUiState.update {
            it.copy(detailsScreenTarget = result)
        }

    }

    /** shopping screen */

    /**profile screen */





}