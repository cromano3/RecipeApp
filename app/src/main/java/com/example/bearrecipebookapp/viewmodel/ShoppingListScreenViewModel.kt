package com.example.bearrecipebookapp.viewmodel

import android.app.Application
import androidx.compose.ui.text.input.TextFieldValue
import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import com.example.bearrecipebookapp.data.RecipeAppDatabase
import com.example.bearrecipebookapp.data.entity.IngredientEntity
import com.example.bearrecipebookapp.data.entity.ShoppingListCustomItemsEntity
import com.example.bearrecipebookapp.data.repository.ShoppingListScreenRepository
import com.example.bearrecipebookapp.datamodel.IngredientsWithQuantities
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
    var selectedIngredients2: LiveData<List<IngredientsWithQuantities>>
    var customIngredients: LiveData<List<ShoppingListCustomItemsEntity>>


    val shoppingScreenUiState = MutableStateFlow(ShoppingScreenUiState())
    val uiAlertState = MutableStateFlow(UiAlertStateShoppingScreenDataModel())

    init {
        val appDb = RecipeAppDatabase.getInstance(application)
        val shoppingListScreenDao = appDb.ShoppingListScreenDao()
        repository = ShoppingListScreenRepository(shoppingListScreenDao)

        shoppingListScreenData = repository.shoppingListScreenData
        selectedIngredients = repository.selectedIngredients
        selectedIngredients2 = repository.selectedIngredients2
        customIngredients = repository.customIngredients

        coroutineScope.launch(Dispatchers.IO) {
            async(Dispatchers.IO){repository.cleanIngredients()}
            async(Dispatchers.IO) {repository.cleanFilters()}
        }

        getCustomItems()
    }

    private fun getCustomItems(){
        coroutineScope.launch(Dispatchers.IO){
            val customItems = repository.getCustomItems()
            shoppingScreenUiState.update {
                it.copy(
                    customItems = customItems
                )
            }
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

                shoppingScreenUiState.update {
                    it.copy(isFiltered = true)
                }


            } else if (recipe.recipeEntity.isShoppingFilter == 2) {

                    withContext(Dispatchers.IO) {
                        repository.cleanIngredients();
                        repository.cleanFilters()
                    }

                shoppingScreenUiState.update {
                    it.copy(isFiltered = false)
                }

            }

            shoppingScreenUiState.value.isWorking = false

        }

    }

    fun addCustomItem(){

        if (uiAlertState.value.inputText.text.length > 20) {

            uiAlertState.update {
                it.copy(
                    inputText = TextFieldValue(""),
                )
            }

        }

        else {

            repository.addCustomItem(ShoppingListCustomItemsEntity(uiAlertState.value.inputText.text))

            val myList: MutableList<ShoppingListCustomItemsEntity> = mutableListOf<ShoppingListCustomItemsEntity>()

            var foundConflict = false

            for(listItem in shoppingScreenUiState.value.customItems){
                if(listItem.item != uiAlertState.value.inputText.text){
                    myList.add(listItem)
                }
                else{
                    foundConflict = true
                    break
                }
            }

            if(!foundConflict){
                myList.add(ShoppingListCustomItemsEntity(uiAlertState.value.inputText.text, 0))

                shoppingScreenUiState.update {
                    it.copy(
                        customItems = myList
                    )
                }
            }




            uiAlertState.update {
                it.copy(
                    showAddCustomItemAlert = false,
                    inputText = TextFieldValue(""),
                )
            }

        }

    }



    fun updateInputText(input: TextFieldValue){


        uiAlertState.update {
            it.copy(inputText = input)
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
            it.copy(
                showAddCustomItemAlert = false,
                inputText = TextFieldValue(""),
            )
        }
    }

    fun triggerClearAllCustomItemsAlert(){
        uiAlertState.update {
            it.copy(
                showClearAllCustomItemsAlert = true
            )
        }
    }

    fun cancelClearAllCustomItemsAlert(){
        uiAlertState.update {
            it.copy(
                showClearAllCustomItemsAlert = false
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

        val myList: MutableList<ShoppingListCustomItemsEntity> = mutableListOf<ShoppingListCustomItemsEntity>()

        for(listItem in shoppingScreenUiState.value.customItems){
            if(listItem.item == item.item){
                myList.add(ShoppingListCustomItemsEntity(listItem.item, 1))
            }
            else{
                myList.add(listItem)
            }
        }

        shoppingScreenUiState.update {
            it.copy(
                customItems = myList
            )
        }

    }

    fun customItemDeselected(item: ShoppingListCustomItemsEntity){
        repository.setCustomItemToDeselected(item)

        val myList: MutableList<ShoppingListCustomItemsEntity> = mutableListOf<ShoppingListCustomItemsEntity>()

        for(listItem in shoppingScreenUiState.value.customItems){
            if(listItem.item == item.item){
                myList.add(ShoppingListCustomItemsEntity(listItem.item, 0))
            }
            else{
                myList.add(listItem)
            }
        }

        shoppingScreenUiState.update {
            it.copy(
                customItems = myList
            )
        }

    }

    fun deleteCustomItem(item: ShoppingListCustomItemsEntity){

        repository.deleteCustomItem(item)

        val myList: MutableList<ShoppingListCustomItemsEntity> = mutableListOf<ShoppingListCustomItemsEntity>()

        for(listItem in shoppingScreenUiState.value.customItems){
            if(listItem.item != item.item){
                myList.add(listItem)
            }
        }

        shoppingScreenUiState.update {
            it.copy(
                customItems = myList
            )
        }

    }

    fun clearAllCustomItems(){
        repository.clearAllCustomItems()

        shoppingScreenUiState.update {
            it.copy(
                customItems = listOf()
            )
        }

        cancelClearAllCustomItemsAlert()
    }

    fun ingredientSelected(ingredientName: String){
        shoppingScreenUiState.update {
            it.copy(counter = shoppingScreenUiState.value.counter + 1)
        }

        repository.setIngredientToOwned(ingredientName)
    }

    fun ingredientDeselected(ingredientName: String){

        shoppingScreenUiState.update {
            it.copy(counter = shoppingScreenUiState.value.counter + 1)
        }

        repository.setIngredientToNotOwned(ingredientName)
    }



    suspend fun setDetailsScreenTarget(recipeName: String){
        repository.setDetailsScreenTarget(recipeName)
    }
}