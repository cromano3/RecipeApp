package com.example.bearrecipebookapp.data.repository

import androidx.lifecycle.LiveData
import com.example.bearrecipebookapp.data.dao.ShoppingListScreenDao
import com.example.bearrecipebookapp.data.entity.IngredientEntity
import com.example.bearrecipebookapp.data.entity.ShoppingListCustomItemsEntity
import com.example.bearrecipebookapp.datamodel.RecipeWithIngredients
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class ShoppingListScreenRepository(private val shoppingListScreenDao: ShoppingListScreenDao) {

    private val coroutineScope = CoroutineScope(Dispatchers.Main)

    var shoppingListScreenData: LiveData<List<RecipeWithIngredients>> = shoppingListScreenDao.getData()
    var selectedIngredients:  LiveData<List<IngredientEntity>> = shoppingListScreenDao.getNeededIngredients()
    var customIngredients: LiveData<List<ShoppingListCustomItemsEntity>> = shoppingListScreenDao.getCustomIngredients()

    fun setDetailsScreenTarget(recipeName: String){
        coroutineScope.launch(Dispatchers.IO) {
            shoppingListScreenDao.setDetailsScreenTarget(recipeName)
        }
    }

    fun setIngredientToNotShown(name: String){
        coroutineScope.launch(Dispatchers.IO) {
            shoppingListScreenDao.setIngredientToNotShown(name)
        }
    }

    fun setIngredientToShown(name: String){
        coroutineScope.launch(Dispatchers.IO) {
            shoppingListScreenDao.setIngredientToShown(name)
        }
    }

    fun addCustomItem(item: ShoppingListCustomItemsEntity){
        coroutineScope.launch(Dispatchers.IO) {
            shoppingListScreenDao.addCustomItem(item)
        }
    }



    fun cleanFilters(){
        coroutineScope.launch(Dispatchers.IO) {
            shoppingListScreenDao.cleanFilters()
        }
    }

    fun cleanIngredients(){
        coroutineScope.launch(Dispatchers.IO) {
            shoppingListScreenDao.cleanIngredients()
        }
    }



    fun removeOtherFilters(name: String){
        coroutineScope.launch(Dispatchers.IO) {
            shoppingListScreenDao.removeOtherFilters(name)
        }
    }

    fun filterBy(name: String){
        coroutineScope.launch(Dispatchers.IO) {
            shoppingListScreenDao.filterBy(name)
        }
    }


    fun setCustomItemToSelected(item: ShoppingListCustomItemsEntity){
        coroutineScope.launch(Dispatchers.IO) {
            shoppingListScreenDao.setCustomItemToSelected(item.item)
        }
    }

    fun setCustomItemToDeselected(item: ShoppingListCustomItemsEntity){
        coroutineScope.launch(Dispatchers.IO) {
            shoppingListScreenDao.setCustomItemToDeselected(item.item)
        }
    }

    fun deleteCustomItem(item: ShoppingListCustomItemsEntity){
        coroutineScope.launch(Dispatchers.IO) {
            shoppingListScreenDao.deleteCustomItem(item.item)
        }
    }



    fun setIngredientToOwned(ingredientEntity: IngredientEntity){
        coroutineScope.launch(Dispatchers.IO) {
            shoppingListScreenDao.setIngredientToOwned(ingredientEntity.ingredientName)
        }
    }

    fun setIngredientToNotOwned(ingredientEntity: IngredientEntity){
        coroutineScope.launch(Dispatchers.IO) {
            shoppingListScreenDao.setIngredientToNotOwned(ingredientEntity.ingredientName)
        }
    }
}