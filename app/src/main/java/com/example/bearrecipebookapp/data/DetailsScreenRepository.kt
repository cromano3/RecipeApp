package com.example.bearrecipebookapp.data

import androidx.lifecycle.LiveData
import com.example.bearrecipebookapp.datamodel.RecipeWithIngredientsAndInstructions
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class DetailsScreenRepository(private val detailsScreenDao: DetailsScreenDao) {

    private val coroutineScope = CoroutineScope(Dispatchers.Main)

    var detailsScreenData: LiveData<RecipeWithIngredientsAndInstructions> = detailsScreenDao.getData()


    fun removeFromMenu(recipeName: String){
        coroutineScope.launch(Dispatchers.IO) {
            detailsScreenDao.removeFromMenu(recipeName)
        }
    }

    fun addToMenu(recipeName: String){
        coroutineScope.launch(Dispatchers.IO) {
            detailsScreenDao.addToMenu(recipeName)
        }
    }

    fun updateMenu(recipeName: String, onMenuStatus: Int) {
        coroutineScope.launch(Dispatchers.IO) {
            detailsScreenDao.updateMenu(recipeName, onMenuStatus)
        }
    }

    fun updateFavorite(recipeName: String, isFavoriteStatus: Int) {
        coroutineScope.launch(Dispatchers.IO) {
            detailsScreenDao.updateFavorite(recipeName, isFavoriteStatus)
        }
    }

    fun updateQuantityNeeded(ingredientName: String, quantityNeeded: Int){
        coroutineScope.launch(Dispatchers.IO) {
            detailsScreenDao.updateQuantityNeeded(ingredientName, quantityNeeded)
        }
    }

    fun setIngredientQuantityOwned(ingredientEntity: IngredientEntity, quantityOwned: Int){
        coroutineScope.launch(Dispatchers.IO) {
            detailsScreenDao.setIngredientQuantityOwned(ingredientEntity.ingredientName, quantityOwned)
        }
    }
}