package com.example.bearrecipebookapp.data.repository

import androidx.lifecycle.LiveData
import com.example.bearrecipebookapp.data.dao.MenuScreenDao
import com.example.bearrecipebookapp.data.entity.IngredientEntity
import com.example.bearrecipebookapp.datamodel.RecipeNamesWithRatings
import com.example.bearrecipebookapp.datamodel.RecipeWithIngredientsAndInstructions
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class MenuScreenRepository(private val menuScreenDao: MenuScreenDao)
{

    private val coroutineScope = CoroutineScope(Dispatchers.Main)

    var menuScreenData: LiveData<List<RecipeWithIngredientsAndInstructions>> = menuScreenDao.getData()



    fun getOnMenuNames(): List<String>{
        return menuScreenDao.getOnMenuNames()
    }

    fun setGlobalRatings(namesWithRatings: MutableList<RecipeNamesWithRatings>) {
        for(item in namesWithRatings){
            println("SET RATING name ${item.recipeName} rating ${item.rating}")
            menuScreenDao.setGlobalRatings(item.recipeName, item.rating)
        }
    }

    suspend fun addExpToGive(expToGive: Int){
        menuScreenDao.addExpToGive(expToGive)
    }

    suspend fun setDetailsScreenTarget(recipeName: String){
//        coroutineScope.launch(Dispatchers.IO) {
            menuScreenDao.setDetailsScreenTarget(recipeName)
//        }
    }

    fun setReviewAsWritten(recipeName: String){
        menuScreenDao.setReviewAsWritten(recipeName)
    }

    fun setLocalRating(recipeName: String, rating: Boolean){
        menuScreenDao.setLocalRating(recipeName, if(rating) 1 else 0)
    }

    suspend fun setReviewTarget(recipeName: String){
        menuScreenDao.setReviewTarget(recipeName)
    }

    suspend fun cleanReviewTarget(){
        menuScreenDao.cleanReviewTarget()
    }


    fun addTutorialAlert(){
        coroutineScope.launch(Dispatchers.IO) {
            menuScreenDao.addTutorialAlert()
        }
    }


    fun addCooked(recipeName: String){
        coroutineScope.launch(Dispatchers.IO) {
            menuScreenDao.addCooked(recipeName)
        }
    }

    fun cleanShoppingFilters(){
        coroutineScope.launch(Dispatchers.IO) {
            menuScreenDao.cleanShoppingFilters()
        }
    }

    fun cleanIngredients(){
        coroutineScope.launch(Dispatchers.IO) {
            menuScreenDao.cleanIngredients()
        }
    }


    suspend fun removeFromMenu(recipeName: String){
//        coroutineScope.launch(Dispatchers.IO) {
            menuScreenDao.removeFromMenu(recipeName)
//        }
    }

    suspend fun setToFadeOut(recipeName: String){
        menuScreenDao.setToFadeOut(recipeName)
    }


    fun updateFavorite(recipeName: String, isFavoriteStatus: Int) {
        coroutineScope.launch(Dispatchers.IO) {
            menuScreenDao.updateFavorite(recipeName, isFavoriteStatus)
        }
    }

    fun setAsFavorite(recipeName: String){
        menuScreenDao.setAsFavorite(recipeName)
    }


    suspend fun updateQuantityNeeded(ingredientName: String, quantityNeeded: Int){
//        coroutineScope.launch(Dispatchers.IO) {
            menuScreenDao.updateQuantityNeeded(ingredientName, quantityNeeded)
//        }
    }

    suspend fun setIngredientQuantityOwned(ingredientEntity: IngredientEntity, quantityOwned: Int){
//        coroutineScope.launch(Dispatchers.IO) {
            menuScreenDao.setIngredientQuantityOwned(ingredientEntity.ingredientName, quantityOwned)
//        }
    }
}