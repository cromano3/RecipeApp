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

    var results: LiveData<List<HomeScreenDataModel>> = searchScreenDao.getResults()
    var showResults: LiveData<Int> = searchScreenDao.getShowResults()
    var previewList: LiveData<String> = searchScreenDao.getPreviewList()



    fun getRecipes(): List<HomeScreenDataModel>{
        return searchScreenDao.getRecipes()
    }

    fun setSearchResult(recipeName:String , isResult: Int){
        searchScreenDao.setSearchResult(recipeName, isResult)
    }

    fun clearResults(){
        searchScreenDao.clearResults()
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

    fun setDetailsScreenTarget(recipeName: String){
        searchScreenDao.setDetailsScreenTarget(recipeName)
    }

}