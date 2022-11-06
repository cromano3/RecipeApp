package com.example.bearrecipebookapp.data

import androidx.lifecycle.LiveData
import com.example.bearrecipebookapp.datamodel.RecipeWithIngredientsAndInstructions
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class ShoppingListScreenRepository(private val shoppingListScreenDao: ShoppingListScreenDao) {

    private val coroutineScope = CoroutineScope(Dispatchers.Main)

    var shoppingListScreenData: LiveData<List<RecipeWithIngredientsAndInstructions>> = shoppingListScreenDao.getData()

    var selectedIngredients:  LiveData<List<IngredientEntity>> = shoppingListScreenDao.getNeededIngredients()

    fun setDetailsScreenTarget(recipeName: String){
        coroutineScope.launch(Dispatchers.IO) {
            shoppingListScreenDao.setDetailsScreenTarget(recipeName)
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