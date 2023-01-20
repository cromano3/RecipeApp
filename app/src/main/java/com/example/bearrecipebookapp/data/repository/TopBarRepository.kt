package com.example.bearrecipebookapp.data.repository

import androidx.lifecycle.LiveData
import com.example.bearrecipebookapp.data.dao.TopBarDao
import com.example.bearrecipebookapp.datamodel.HomeScreenDataModel
import com.example.bearrecipebookapp.datamodel.RecipeWithIngredientsAndInstructions
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch


class TopBarRepository(private val topBarDao: TopBarDao) {

    private val coroutineScope = CoroutineScope(Dispatchers.Main)

    var detailsScreenData: LiveData<RecipeWithIngredientsAndInstructions> = topBarDao.getData()
//    var textFieldValue: LiveData<String> = topBarDao.getTextFieldValue()
    var showResults: LiveData<Int> = topBarDao.getShowResults()


    fun updateFavorite(recipeName: String, isFavoriteStatus: Int) {
        coroutineScope.launch(Dispatchers.IO) {
            topBarDao.updateFavorite(recipeName, isFavoriteStatus)
        }
    }

    suspend fun getTextFieldValue(): String{
        return topBarDao.getTextFieldValue()
    }

    suspend fun setTextFieldValue(input: String){
        topBarDao.setTextFieldValue(input)
    }

    suspend fun getRecipeNamesReferenceList(): List<String>{
        return topBarDao.getRecipeNamesReferenceList()
    }

    suspend fun getIngredientNamesReferenceList(): List<String>{
        return topBarDao.getIngredientNamesReferenceList()
    }

    suspend fun getFilterNamesReferenceList(): List<String>{
        return topBarDao.getFilterNamesReferenceList()
    }



    suspend fun getRecipes(): List<HomeScreenDataModel>{
        return topBarDao.getRecipes()
    }

    suspend fun clearResults(){
//        coroutineScope.launch(Dispatchers.IO) {
        topBarDao.clearResults()
//        }
    }


    suspend fun setSearchResult(recipeName:String , isResult: Int){
//        coroutineScope.launch(Dispatchers.IO) {
        topBarDao.setSearchResult(recipeName, isResult)
//        }
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


//    fun setDetailsScreenTarget(recipeName: String){
//        coroutineScope.launch(Dispatchers.IO) {
//            menuScreenDao.setDetailsScreenTarget(recipeName)
//        }
//    }
}