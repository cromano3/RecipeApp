package com.example.bearrecipebookapp.data.repository

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Transformations
import com.example.bearrecipebookapp.data.dao.DetailsScreenDao
import com.example.bearrecipebookapp.data.entity.IngredientEntity
import com.example.bearrecipebookapp.datamodel.RecipeWithIngredientsAndInstructions
import com.example.bearrecipebookapp.datamodel.ReviewWithAuthorDataModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class DetailsScreenRepository(private val detailsScreenDao: DetailsScreenDao) {

    private val coroutineScope = CoroutineScope(Dispatchers.Main)

//    var detailsScreenData: LiveData<RecipeWithIngredientsAndInstructions> = detailsScreenDao.getData()
    val recipeNameLiveData = MutableLiveData<String>()

    val reviewsData: LiveData<List<ReviewWithAuthorDataModel>> = Transformations.switchMap(recipeNameLiveData) { recipeName ->
        detailsScreenDao.getReviewsData(recipeName)
    }


    fun setRecipeName(recipeName: String) {
        recipeNameLiveData.postValue(recipeName)
    }



    suspend fun removeFromMenu(recipeName: String){

            detailsScreenDao.removeFromMenu(recipeName)

    }

    suspend fun addToMenu(recipeName: String){

        detailsScreenDao.addToMenu(recipeName)

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

    suspend fun updateQuantityNeeded(ingredientName: String, quantityNeeded: Int){

            detailsScreenDao.updateQuantityNeeded(ingredientName, quantityNeeded)

    }

    suspend fun setIngredientQuantityOwned(ingredientEntity: IngredientEntity, quantityOwned: Int){

            detailsScreenDao.setIngredientQuantityOwned(ingredientEntity.ingredientName, quantityOwned)

    }

    fun addCooked(recipe: RecipeWithIngredientsAndInstructions){
        coroutineScope.launch(Dispatchers.IO) {
            detailsScreenDao.addCooked(recipe.recipeEntity.recipeName)
        }
    }

    fun setLocalRating(recipeName: String, rating: Int){
        detailsScreenDao.setLocalRating(recipeName, rating)
    }

    fun setReviewAsWritten(recipeName: String){
        detailsScreenDao.setReviewAsWritten(recipeName)
    }

    suspend fun setReviewTarget(recipeName: String){
        println("5")
        detailsScreenDao.setReviewTarget(recipeName)
        println("6")
    }

    suspend fun cleanReviewTarget(){
        println("3")
        detailsScreenDao.cleanReviewTarget()
        println("4")
    }

    suspend fun addExpToGive(expToGive: Int){
        detailsScreenDao.addExpToGive(expToGive)
    }

    fun setAsFavorite(recipeName: String){
        detailsScreenDao.setAsFavorite(recipeName)
    }

    fun cleanShoppingFilters(){
        coroutineScope.launch(Dispatchers.IO) {
            detailsScreenDao.cleanShoppingFilters()
        }
    }

    fun cleanIngredients(){
        coroutineScope.launch(Dispatchers.IO) {
            detailsScreenDao.cleanIngredients()
        }
    }

}