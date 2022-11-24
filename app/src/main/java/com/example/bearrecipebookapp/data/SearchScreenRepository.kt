package com.example.bearrecipebookapp.data

import com.example.bearrecipebookapp.datamodel.HomeScreenDataModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class SearchScreenRepository(private val searchScreenDao: SearchScreenDao) {

    private val coroutineScope = CoroutineScope(Dispatchers.Main)

    suspend fun getRecipeNamesReferenceList(): List<String>{
        return searchScreenDao.getRecipeNamesReferenceList()
    }

    suspend fun getIngredientNamesReferenceList(): List<String>{
        return searchScreenDao.getIngredientNamesReferenceList()
    }

    suspend fun getFilterNamesReferenceList(): List<String>{
            return searchScreenDao.getFilterNamesReferenceList()
    }

    suspend fun getRecipes(): List<HomeScreenDataModel>{
        return searchScreenDao.getRecipes()
    }

    fun updateFavorite(recipeName: String, isFavoriteStatus: Int) {
        coroutineScope.launch(Dispatchers.IO) {
            searchScreenDao.updateFavorite(recipeName, isFavoriteStatus)
        }
    }

    fun updateMenu(recipeName: String, onMenuStatus: Int) {
        coroutineScope.launch(Dispatchers.IO) {
            searchScreenDao.updateMenu(recipeName, onMenuStatus)
        }
    }

    fun updateQuantityNeeded(ingredientName: String, quantityNeeded: Int){
        coroutineScope.launch(Dispatchers.IO) {
            searchScreenDao.updateQuantityNeeded(ingredientName, quantityNeeded)
        }
    }

    fun setIngredientQuantityOwned(ingredientEntity: IngredientEntity, quantityOwned: Int){
        coroutineScope.launch(Dispatchers.IO) {
            searchScreenDao.setIngredientQuantityOwned(ingredientEntity.ingredientName, quantityOwned)
        }
    }

    fun setDetailsScreenTarget(recipeName: String){
        coroutineScope.launch(Dispatchers.IO) {
            searchScreenDao.setDetailsScreenTarget(recipeName)
        }
    }
}