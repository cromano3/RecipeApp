package com.christopherromano.culinarycompanion.data.repository

import androidx.lifecycle.LiveData
import com.christopherromano.culinarycompanion.data.dao.SearchScreenDao
import com.christopherromano.culinarycompanion.data.entity.IngredientEntity
import com.christopherromano.culinarycompanion.datamodel.HomeScreenDataModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class SearchScreenRepository(private val searchScreenDao: SearchScreenDao) {

    private val coroutineScope = CoroutineScope(Dispatchers.Main)

//    var allRecipes: LiveData<List<HomeScreenDataModel>> = searchScreenDao.getAllRecipes()
    var results: LiveData<List<HomeScreenDataModel>> = searchScreenDao.getResults()
    var showResults: LiveData<Int> = searchScreenDao.getShowResults()
    var previewList: LiveData<String> = searchScreenDao.getPreviewList()

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

    suspend fun setSearchResult(recipeName:String , isResult: Int){
//        coroutineScope.launch(Dispatchers.IO) {
            searchScreenDao.setSearchResult(recipeName, isResult)
//        }
    }

    suspend fun clearResults(){
//        coroutineScope.launch(Dispatchers.IO) {
            searchScreenDao.clearResults()
//        }
    }

    fun setShowResults(isShown: Int){
        coroutineScope.launch(Dispatchers.IO) {
            searchScreenDao.setShowResults(isShown)
        }
    }

    fun setTextFieldValue(input: String){
        coroutineScope.launch(Dispatchers.IO) {
            searchScreenDao.setTextFieldValue(input)
        }
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

    suspend fun setDetailsScreenTarget(recipeName: String){
//        coroutineScope.launch(Dispatchers.IO) {
            searchScreenDao.setDetailsScreenTarget(recipeName)
//        }
    }
}