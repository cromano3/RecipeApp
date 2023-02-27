package com.example.bearrecipebookapp.data.dao

import androidx.room.*
import com.example.bearrecipebookapp.data.entity.CommentAuthorEntity
import com.example.bearrecipebookapp.data.entity.CommentsEntity
import com.example.bearrecipebookapp.data.entity.RecipeEntity
import com.example.bearrecipebookapp.datamodel.RecipeNameAndRating
import com.example.bearrecipebookapp.datamodel.RecipeNameAndReview
import com.example.bearrecipebookapp.datamodel.RecipeWithIngredientsAndInstructions
import com.example.bearrecipebookapp.datamodel.ReviewWithAuthorDataModel

@Dao
interface AppDao {

    @Transaction
    @Query("SELECT * FROM recipe_table WHERE recipe_name = :recipeName")
    fun getRecipeWithIngredientsAndInstructions(recipeName: String): RecipeWithIngredientsAndInstructions

    @Transaction
    @Query("SELECT * FROM recipe_table WHERE recipe_name = :recipeName")
    fun getRecipe(recipeName: String): RecipeEntity



    @Transaction
    @Query("SELECT is_online_user_type FROM user_table")
    fun onlineUserType(): Int


    @Transaction
    @Query("UPDATE recipe_table SET global_rating = :rating WHERE recipe_name = :recipeName")
    fun setGlobalRating(recipeName: String, rating: Int)




    @Transaction
    @Query("SELECT recipe_name AS recipeName, review AS reviewText FROM recipe_table WHERE is_review_synced = 0 AND is_reviewed = 1")
    fun getUnsyncedUserComments(): List<RecipeNameAndReview>

    @Transaction
    @Query("UPDATE recipe_table SET is_review_synced = 1 WHERE recipe_name = :recipeName")
    fun markCommentAsSynced(recipeName: String)





    @Transaction
    @Query("UPDATE comments_table SET liked_by_local_user = 1 WHERE comment_id = :commentID")
    fun setAsLiked(commentID: String)


    @Transaction
    @Query("SELECT comment_id FROM comments_table WHERE local_user_like_was_synced = 0 AND liked_by_local_user = 1")
    fun getUnsyncedUserLikes(): List<String>

    @Transaction
    @Query("UPDATE comments_table SET local_user_like_was_synced = 1 WHERE comment_id = :likeId")
    fun markLikeAsSynced(likeId: String)





    @Transaction
    @Query("SELECT recipe_name AS recipeName, local_user_rating AS rating FROM recipe_table WHERE is_rating_synced = 0 AND is_rated = 1")
    fun getUnsyncedUserRatings(): List<RecipeNameAndRating>

    @Transaction
    @Query("UPDATE recipe_table SET is_rating_synced = 1 WHERE recipe_name = :recipeName")
    fun markRatingAsSynced(recipeName: String)




    @Transaction
    @Query("UPDATE user_table SET is_online_user_type = :type")
    fun setOnlineUserType(type: Int)






    @Transaction
    @Query("UPDATE comments_table SET likes = :likes WHERE comment_id = :commentId")
    fun updateLikes(likes: Int, commentId: String)

    @Transaction
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun addComment(comment: CommentsEntity)




    @Transaction
    @Query("UPDATE recipe_table SET review = :reviewText, is_reviewed = 1, is_review_synced = 1 WHERE recipe_name = :name")
    fun setReview(name: String, reviewText: String)

    @Transaction
    @Query("UPDATE recipe_table SET is_reviewed = 1, is_review_synced = 1 WHERE recipe_name = :name")
    fun setReviewIsSynced(name: String)

    @Transaction
    @Query("UPDATE recipe_table SET review = :reviewText, is_reviewed = 1, is_review_synced = 0 WHERE recipe_name = :recipeName")
    fun setReviewAsUnsynced(recipeName: String, reviewText: String)

    @Transaction
    @Query("DELETE FROM comments_table WHERE comment_id = :commentID")
    fun deleteReview(commentID: String)


    @Transaction
    @Query("UPDATE recipe_table SET is_reviewed = 0, is_review_synced = 0 WHERE recipe_name = :recipeName")
    fun markReviewAsNotCommented(recipeName: String)




    @Transaction
    @Query("UPDATE recipe_table SET timestamp_of_latest_received_comment = :timestamp WHERE recipe_name = :recipeName")
    fun setMostRecentCommentTimestamp(recipeName: String, timestamp: String)

    @Transaction
    @Query("UPDATE recipe_table SET time_of_users_last_sync = :timestamp WHERE recipe_name = :recipeName")
    fun setTimeOfLastUpdate(recipeName: String, timestamp: String)



    @Transaction
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun addAuthor(author: CommentAuthorEntity)





    @Transaction
    @Query("UPDATE recipe_table SET global_rating = :rating WHERE recipe_name = :recipeName")
    fun updateRecipeRating(recipeName: String, rating: Int)


    @Transaction
    @Query("UPDATE recipe_table SET local_user_rating = :rating, is_rated = 1, is_rating_synced = :syncStatus WHERE recipe_name = :name")
    fun setLocalRating(name: String, rating: Int, syncStatus: Int)





    @Transaction
    @Query("SELECT * FROM comments_table WHERE recipe_name = :recipeName")
    fun getReviewsData(recipeName: String): List<ReviewWithAuthorDataModel>





    @Transaction
    @Query("SELECT review FROM recipe_table WHERE is_review_synced = 0 AND is_reviewed = 1 AND recipe_name = :recipeName")
    fun getLocalUserReviewData(recipeName: String): String?





    @Transaction
    @Query("UPDATE user_table SET firestore_uid = :uid")
    fun setUid(uid: String)

    @Transaction
    @Query("SELECT firestore_uid FROM user_table")
    fun getUserId(): String



    @Transaction
    @Query("UPDATE user_table SET user_image_url = :url")
    fun setUserImageURL(url: String)

    @Transaction
    @Query("UPDATE user_table SET user_nickname = :name")
    fun setUserNickname(name: String)



    @Transaction
    @Query("UPDATE user_table SET is_online_user_type = -2")
    fun setLocalUserAsNew()


}