package com.christopherromano.culinarycompanion.data.repository

import com.christopherromano.culinarycompanion.data.dao.AppDao
import com.christopherromano.culinarycompanion.datamodel.RecipeNameAndRating
import com.christopherromano.culinarycompanion.datamodel.RecipeNameAndReview
import com.christopherromano.culinarycompanion.datamodel.RecipeWithIngredientsAndInstructions
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class AppRepository(private val appDao: AppDao) {

    private val coroutineScope = CoroutineScope(Dispatchers.IO)

    fun clearSearch(){
        coroutineScope.launch(Dispatchers.IO) {
            appDao.clearSearch()
        }
    }

    fun getRecipeWithIngredientsAndInstructions(recipeName: String): RecipeWithIngredientsAndInstructions {
        return appDao.getRecipeWithIngredientsAndInstructions(recipeName)
    }


    fun onlineUserType(): Int {
        return appDao.onlineUserType()
    }

    fun setOnlineUserType(type: Int){
        coroutineScope.launch (Dispatchers.IO) {
            appDao.setOnlineUserType(type)
        }
    }


    fun getUnsyncedUserComments(): List<RecipeNameAndReview> {
        return appDao.getUnsyncedUserComments()
    }

    fun markCommentAsSynced(comment: RecipeNameAndReview) {
        println("mark synced")
        appDao.markCommentAsSynced(comment.recipeName)
    }


    fun setUserRating(recipeName: String, rating: Int, syncStatus: Int){
        appDao.setLocalRating(recipeName, rating, syncStatus)
    }
    fun getUnsyncedUserRatings(): List<RecipeNameAndRating> {
        return appDao.getUnsyncedUserRatings()
    }

    fun markRatingAsSynced(rating: RecipeNameAndRating){
        appDao.markRatingAsSynced(rating.recipeName)
    }


    fun setReview(recipeName: String, reviewText: String){
        appDao.setReview(recipeName, reviewText)
    }

    fun setReviewIsSynced(name: String){
        appDao.setReviewIsSynced(name)
    }

    fun setReviewAsUnsynced(recipeName: String, reviewText: String){
        appDao.setReviewAsUnsynced(recipeName, reviewText)
    }
}