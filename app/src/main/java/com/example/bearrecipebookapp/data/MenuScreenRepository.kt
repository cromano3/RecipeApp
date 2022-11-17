package com.example.bearrecipebookapp.data

import androidx.lifecycle.LiveData
import com.example.bearrecipebookapp.datamodel.RecipeWithIngredients
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class MenuScreenRepository(private val menuScreenDao: MenuScreenDao)
{

    private val coroutineScope = CoroutineScope(Dispatchers.Main)

    var menuScreenData: LiveData<List<RecipeWithIngredients>> =
        menuScreenDao.getData()


    fun setDetailsScreenTarget(recipeName: String){
        coroutineScope.launch(Dispatchers.IO) {
            menuScreenDao.setDetailsScreenTarget(recipeName)
        }
    }

    fun removeFromMenu(recipeName: String){
        coroutineScope.launch(Dispatchers.IO) {
            menuScreenDao.removeFromMenu(recipeName)
        }
    }


    fun updateFavorite(recipeName: String, isFavoriteStatus: Int) {
        coroutineScope.launch(Dispatchers.IO) {
            menuScreenDao.updateFavorite(recipeName, isFavoriteStatus)
        }
    }


    fun updateQuantityNeeded(ingredientName: String, quantityNeeded: Int){
        coroutineScope.launch(Dispatchers.IO) {
            menuScreenDao.updateQuantityNeeded(ingredientName, quantityNeeded)
        }
    }

    fun setIngredientQuantityOwned(ingredientEntity: IngredientEntity, quantityOwned: Int){
        coroutineScope.launch(Dispatchers.IO) {
            menuScreenDao.setIngredientQuantityOwned(ingredientEntity.ingredientName, quantityOwned)
        }
    }
}