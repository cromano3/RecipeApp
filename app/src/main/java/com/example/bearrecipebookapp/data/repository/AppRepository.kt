package com.example.bearrecipebookapp.data.repository

import com.example.bearrecipebookapp.data.dao.AppDao
import com.example.bearrecipebookapp.data.entity.CommentAuthorEntity
import com.example.bearrecipebookapp.data.entity.CommentsEntity
import com.example.bearrecipebookapp.datamodel.*
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class AppRepository(private val appDao: AppDao) {

    private val coroutineScope = CoroutineScope(Dispatchers.IO)



    fun getRecipeWithIngredientsAndInstructions(recipeName: String): RecipeWithIngredientsAndInstructions{
        return appDao.getRecipeWithIngredientsAndInstructions(recipeName)
    }



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



    fun getUnsyncedUserLikes(): List<String>{
        return appDao.getUnsyncedUserLikes()
    }

    fun markLikeAsSynced(likeId: String){
        appDao.markLikeAsSynced(likeId)
    }



    fun setOnlineUserType(type: Int){
        appDao.setOnlineUserType(type)
    }




    suspend fun updateLikes(comment: CommentsEntity){
        withContext(Dispatchers.IO) { appDao.updateLikes(comment.likes, comment.commentID) }
    }

    fun addComment(comment: CommentsEntity){
       appDao.addComment(comment)
    }



    fun addAuthor(authorData: AuthorData, id: String) {
        appDao.addAuthor(CommentAuthorEntity(
            authorID = id,
            authorName = authorData.userName,
            authorKarma = 0,
            authorImageURL = authorData.userPhotoURL))
    }




    fun updateRecipeRating(rating: RecipeNameAndRating){
        appDao.updateRecipeRating(rating.recipeName, rating.rating)
    }





    fun onlineUserType(): Int{
        val x = appDao.onlineUserType()
        return x
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