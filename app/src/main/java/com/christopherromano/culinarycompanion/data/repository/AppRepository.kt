package com.christopherromano.culinarycompanion.data.repository

import com.christopherromano.culinarycompanion.data.dao.AppDao
import com.christopherromano.culinarycompanion.data.entity.CommentAuthorEntity
import com.christopherromano.culinarycompanion.data.entity.CommentsEntity
import com.christopherromano.culinarycompanion.data.entity.RecipeEntity
import com.christopherromano.culinarycompanion.datamodel.*
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class AppRepository(private val appDao: AppDao) {

    private val coroutineScope = CoroutineScope(Dispatchers.IO)



    fun getRecipeWithIngredientsAndInstructions(recipeName: String): RecipeWithIngredientsAndInstructions {
        return appDao.getRecipeWithIngredientsAndInstructions(recipeName)
    }

    fun getRecipe(recipeName: String): RecipeEntity {
        return appDao.getRecipe(recipeName)
    }


//    fun setGlobalRating(recipeName: String, rating: Int){
//        appDao.setGlobalRating(recipeName, rating)
//    }

    fun getReviewsData(recipeName: String): List<ReviewWithAuthorDataModel> {
        return appDao.getReviewsData(recipeName)

    }


    fun getLocalUserReviewData(recipeName: String): String? {
        return appDao.getLocalUserReviewData(recipeName)
    }



    fun getUnsyncedUserComments(): List<RecipeNameAndReview> {
        return appDao.getUnsyncedUserComments()
    }

    fun markCommentAsSynced(comment: RecipeNameAndReview) {
        println("mark synced")
        appDao.markCommentAsSynced(comment.recipeName)
    }


    fun getUnsyncedUserRatings(): List<RecipeNameAndRating> {
        return appDao.getUnsyncedUserRatings()
    }

    fun markRatingAsSynced(rating: RecipeNameAndRating){
        appDao.markRatingAsSynced(rating.recipeName)
    }



    fun setAsLiked(commentID: String){
        appDao.setAsLiked(commentID)
    }


    fun getUnsyncedUserLikes(): List<String>{
        return appDao.getUnsyncedUserLikes()
    }

    fun markLikeAsSynced(likeId: String){
        appDao.markLikeAsSynced(likeId)
    }



    fun setOnlineUserType(type: Int){
        coroutineScope.launch (Dispatchers.IO) {
            appDao.setOnlineUserType(type)
        }
    }


    suspend fun updateLikes(comment: CommentsEntity){
        withContext(Dispatchers.IO) { appDao.updateLikes(comment.likes, comment.commentID) }
    }



    fun addComment(comment: CommentsEntity){
       appDao.addComment(comment)
    }

    fun setMostRecentCommentTimestamp(recipeName: String, timestamp: String){
        appDao.setMostRecentCommentTimestamp(recipeName, timestamp)
    }

    fun setTimeOfLastUpdate(recipeName: String, timestamp: String){
        appDao.setTimeOfLastUpdate(recipeName, timestamp)
    }


    fun addAuthor(authorData: AuthorData, id: String) {
        appDao.addAuthor(
            CommentAuthorEntity(
            authorID = id,
            authorName = authorData.userName,
            authorKarma = 0,
            authorImageURL = authorData.userPhotoURL)
        )
    }



    fun setUserRating(recipeName: String, rating: Int, syncStatus: Int){
        appDao.setLocalRating(recipeName, rating, syncStatus)
    }

//    fun updateRecipeRating(rating: RecipeNameAndRating){
//        appDao.updateRecipeRating(rating.recipeName, rating.rating)
//    }



    fun setReview(recipeName: String, reviewText: String){
        appDao.setReview(recipeName, reviewText)
    }

    fun setReviewIsSynced(name: String){
        appDao.setReviewIsSynced(name)
    }

    fun setReviewAsUnsynced(recipeName: String, reviewText: String){
        appDao.setReviewAsUnsynced(recipeName, reviewText)
    }

    fun deleteReview(commentID: String){
        appDao.deleteReview(commentID)
    }

    fun markReviewAsNotCommented(recipeName: String){
        appDao.markReviewAsNotCommented(recipeName)
    }

    fun onlineUserType(): Int {
        return appDao.onlineUserType()
    }

    fun setUid(uid: String){
        appDao.setUid(uid)
    }

    fun getUserId(): String{
        return appDao.getUserId()
    }

    fun setUserImageURL(url: String){
        appDao.setUserImageURL(url)
    }

    fun setUserNickname(name: String){
        appDao.setUserNickname(name)
    }

    fun setLocalUserAsNew(){
        appDao.setLocalUserAsNew()
    }

//    fun setReviewAsWritten(recipeName: String){
//        coroutineScope.launch(Dispatchers.IO){
//            commentScreenDao.setReviewAsWritten(recipeName)
//        }
//    }
//
//    fun setReview(recipeName: String, reviewText: String){
//        coroutineScope.launch {
//            commentScreenDao.setReview(recipeName, reviewText)
//        }
//    }
}