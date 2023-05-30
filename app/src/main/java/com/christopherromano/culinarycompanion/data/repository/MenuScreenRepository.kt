package com.christopherromano.culinarycompanion.data.repository

import androidx.lifecycle.LiveData
import com.christopherromano.culinarycompanion.data.dao.MenuScreenDao
import com.christopherromano.culinarycompanion.data.entity.IngredientEntity
import com.christopherromano.culinarycompanion.datamodel.RecipeNamesWithRatings
import com.christopherromano.culinarycompanion.datamodel.RecipeWithIngredientsAndInstructions
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

    fun addExpToGive(expToGive: Int){
        menuScreenDao.addExpToGive(expToGive)
    }


    fun setReviewAsWritten(recipeName: String){
        menuScreenDao.setReviewAsWritten(recipeName)
    }

    fun setLocalRating(recipeName: String, rating: Boolean){
        menuScreenDao.setLocalRating(recipeName, if(rating) 1 else 0)
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


    fun removeFromMenu(recipeName: String){
        menuScreenDao.removeFromMenu(recipeName)
    }

    fun setToFadeOut(recipeName: String){
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


    fun updateQuantityNeeded(ingredientName: String, quantityNeeded: Int){
        menuScreenDao.updateQuantityNeeded(ingredientName, quantityNeeded)
    }

    fun setIngredientQuantityOwned(ingredientEntity: IngredientEntity, quantityOwned: Int){
        menuScreenDao.setIngredientQuantityOwned(ingredientEntity.ingredientName, quantityOwned)
    }
}