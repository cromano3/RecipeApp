package com.example.bearrecipebookapp.data

import androidx.lifecycle.LiveData
import com.example.bearrecipebookapp.datamodel.RecipeWithIngredients
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class ShoppingListScreenRepository(private val shoppingListScreenDao: ShoppingListScreenDao) {

    private val coroutineScope = CoroutineScope(Dispatchers.Main)

    var shoppingListScreenData: LiveData<List<RecipeWithIngredients>> = shoppingListScreenDao.getData()

    var selectedIngredients:  LiveData<List<IngredientEntity>> = shoppingListScreenDao.getNeededIngredients()

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