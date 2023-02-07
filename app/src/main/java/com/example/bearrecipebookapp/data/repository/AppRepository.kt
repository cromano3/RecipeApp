package com.example.bearrecipebookapp.data.repository

import androidx.lifecycle.LiveData
import com.example.bearrecipebookapp.data.dao.AppDao
import com.example.bearrecipebookapp.data.entity.IngredientEntity
import com.example.bearrecipebookapp.data.entity.ShoppingListCustomItemsEntity
import com.example.bearrecipebookapp.datamodel.RecipeWithIngredients
import com.example.bearrecipebookapp.datamodel.RecipeWithIngredientsAndInstructions
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class AppRepository(private val appDao: AppDao) {

    private val coroutineScope = CoroutineScope(Dispatchers.Main)

    //shopping screen live data
    var shoppingListScreenData: LiveData<List<RecipeWithIngredients>> = appDao.getData()
    var selectedIngredients:  LiveData<List<IngredientEntity>> = appDao.getNeededIngredients()
    var customIngredients: LiveData<List<ShoppingListCustomItemsEntity>> = appDao.getCustomIngredients()
    //profile screen live data
    var favoritesData: LiveData<List<RecipeWithIngredientsAndInstructions>> = appDao.getFavorites()
    var cookedData: LiveData<List<RecipeWithIngredients>> = appDao.getCooked()

    suspend fun setDetailsScreenTarget(recipeName: String){
//        coroutineScope.launch(Dispatchers.IO) {
        appDao.setDetailsScreenTarget(recipeName)
//        }
    }

    fun setIngredientToNotShown(name: String){
        coroutineScope.launch(Dispatchers.IO) {
            appDao.setIngredientToNotShown(name)
        }
    }

    fun setIngredientToShown(name: String){
        coroutineScope.launch(Dispatchers.IO) {
            appDao.setIngredientToShown(name)
        }
    }

    fun addCustomItem(item: ShoppingListCustomItemsEntity){
        coroutineScope.launch(Dispatchers.IO) {
            appDao.addCustomItem(item)
        }
    }


    fun addTutorialAlert(){
        appDao.addTutorialAlert()
    }



    fun cleanFilters(){
        coroutineScope.launch(Dispatchers.IO) {
            appDao.cleanFilters()
        }
    }

    fun cleanIngredients(){
        coroutineScope.launch(Dispatchers.IO) {
            appDao.cleanIngredients()
        }
    }



    fun removeOtherFilters(name: String){
        coroutineScope.launch(Dispatchers.IO) {
            appDao.removeOtherFilters(name)
        }
    }

    fun filterBy(name: String){
        coroutineScope.launch(Dispatchers.IO) {
            appDao.filterBy(name)
        }
    }


    fun setCustomItemToSelected(item: ShoppingListCustomItemsEntity){
        coroutineScope.launch(Dispatchers.IO) {
            appDao.setCustomItemToSelected(item.item)
        }
    }

    fun setCustomItemToDeselected(item: ShoppingListCustomItemsEntity){
        coroutineScope.launch(Dispatchers.IO) {
            appDao.setCustomItemToDeselected(item.item)
        }
    }

    fun deleteCustomItem(item: ShoppingListCustomItemsEntity){
        coroutineScope.launch(Dispatchers.IO) {
            appDao.deleteCustomItem(item.item)
        }
    }



    fun setIngredientToOwned(ingredientEntity: IngredientEntity){
        coroutineScope.launch(Dispatchers.IO) {
            appDao.setIngredientToOwned(ingredientEntity.ingredientName)
        }
    }

    fun setIngredientToNotOwned(ingredientEntity: IngredientEntity){
        coroutineScope.launch(Dispatchers.IO) {
            appDao.setIngredientToNotOwned(ingredientEntity.ingredientName)
        }
    }

    fun getDetailsScreenData(name: String): RecipeWithIngredientsAndInstructions{
        return appDao.getDetailsScreenData(name)
    }

}