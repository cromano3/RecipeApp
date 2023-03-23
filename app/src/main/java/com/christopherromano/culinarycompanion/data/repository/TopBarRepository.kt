package com.christopherromano.culinarycompanion.data.repository

import androidx.lifecycle.LiveData
import com.christopherromano.culinarycompanion.data.dao.TopBarDao
import com.christopherromano.culinarycompanion.datamodel.HomeScreenDataModel
import com.christopherromano.culinarycompanion.datamodel.RecipeWithIngredientsAndInstructions
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch


class TopBarRepository(private val topBarDao: TopBarDao) {

    private val coroutineScope = CoroutineScope(Dispatchers.Main)

    var detailsScreenData: LiveData<RecipeWithIngredientsAndInstructions> = topBarDao.getData()
    var showResults: LiveData<Int> = topBarDao.getShowResults()


    fun updateFavorite(recipeName: String, isFavoriteStatus: Int) {
        coroutineScope.launch(Dispatchers.IO) {
            topBarDao.updateFavorite(recipeName, isFavoriteStatus)
        }
    }

    fun getTextFieldValue(): String{
        return topBarDao.getTextFieldValue()
    }


    fun getRecipeNamesReferenceList(): List<String>{
        return topBarDao.getRecipeNamesReferenceList()
    }

    fun getIngredientNamesReferenceList(): List<String>{
        return topBarDao.getIngredientNamesReferenceList()
    }

    fun getFilterNamesReferenceList(): List<String>{
        return topBarDao.getFilterNamesReferenceList()
    }



    fun getRecipes(): List<HomeScreenDataModel>{
        return topBarDao.getRecipes()
    }

    fun clearResults(){
        topBarDao.clearResults()
    }


    fun setSearchResult(recipeName:String , isResult: Int){
        topBarDao.setSearchResult(recipeName, isResult)
    }

    fun setShowResults(isShown: Int){
        coroutineScope.launch(Dispatchers.IO) {
            topBarDao.setShowResults(isShown)
        }
    }

    fun setPreviewList(list: String){
        coroutineScope.launch(Dispatchers.IO) {
            topBarDao.setPreviewList(list)
        }
    }

}