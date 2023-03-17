package com.christopherromano.culinarycompanion.data.repository

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Transformations
import com.christopherromano.culinarycompanion.data.dao.DetailsScreenDao
import com.christopherromano.culinarycompanion.data.entity.IngredientEntity
import com.christopherromano.culinarycompanion.data.entity.QuantitiesTableEntity
import com.christopherromano.culinarycompanion.datamodel.RecipeWithIngredientsAndInstructions
import com.christopherromano.culinarycompanion.datamodel.ReviewWithAuthorDataModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class DetailsScreenRepository(private val detailsScreenDao: DetailsScreenDao) {

    private val coroutineScope = CoroutineScope(Dispatchers.Main)

//    var detailsScreenData: LiveData<RecipeWithIngredientsAndInstructions> = detailsScreenDao.getData()
    private val recipeNameLiveData = MutableLiveData<String>()

    private val _ingredientQuantitiesList: LiveData<List<QuantitiesTableEntity>> = Transformations.switchMap(recipeNameLiveData) { recipeName ->
        detailsScreenDao.getIngredientQuantitiesList(recipeName)
    }

    val ingredientQuantitiesList: LiveData<List<QuantitiesTableEntity>> = Transformations.map(_ingredientQuantitiesList) {
        it.map { quantityEntity ->

            if(quantityEntity.quantity.isBlank()){
                quantityEntity
            }
            else
            {
                if (quantityEntity.quantity.toDouble().rem(1) == 0.0) {
                    QuantitiesTableEntity(
                        quantityEntity.id,
                        quantityEntity.recipeName,
                        quantityEntity.ingredientName,
                        quantityEntity.quantity,
                        quantityEntity.unit
                    )
                } else {
                    val remainder = quantityEntity.quantity.toDouble().rem(1)
                    val quotient = quantityEntity.quantity.toDouble().toInt()
                    var remainderAsString = ""
                    when (remainder) {
                        0.75 -> {
                            remainderAsString = "3/4"
                        }
                        0.5 -> {
                            remainderAsString = "1/2"
                        }
                        0.25 -> {
                            remainderAsString = "1/4"
                        }
                    }

                    val result = if(quotient != 0) quotient.toString() else "" + remainderAsString

                    QuantitiesTableEntity(
                        quantityEntity.id,
                        quantityEntity.recipeName,
                        quantityEntity.ingredientName,
                        result,
                        quantityEntity.unit
                    )
                }
            }

        }
    }

    val reviewsData: LiveData<List<ReviewWithAuthorDataModel>> = Transformations.switchMap(recipeNameLiveData) { recipeName ->
        detailsScreenDao.getReviewsData(recipeName)
    }

    val globalRating: LiveData<Int> = Transformations.switchMap(recipeNameLiveData) { recipeName ->
        detailsScreenDao.getGlobalRating(recipeName)
    }

    fun setGlobalRating(recipeName: String, globalRating: Int) {

        detailsScreenDao.setGlobalRating(recipeName, globalRating)

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