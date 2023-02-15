package com.example.bearrecipebookapp.data.dao

import androidx.room.Dao
import androidx.room.Query
import androidx.room.Transaction
import com.example.bearrecipebookapp.datamodel.RecipeNameAndRating
import com.example.bearrecipebookapp.datamodel.RecipeNameAndReview
import com.example.bearrecipebookapp.datamodel.RecipeWithIngredientsAndInstructions

@Dao
interface AppDao {

    @Transaction
    @Query("SELECT * FROM recipe_table WHERE recipe_name = :recipeName")
    fun getRecipeWithIngredientsAndInstructions(recipeName: String): RecipeWithIngredientsAndInstructions

    @Transaction
    @Query("SELECT is_online FROM user_table")
    fun isNewUser(): Int



    @Transaction
    @Query("SELECT recipe_name, review FROM recipe_table WHERE is_review_synced = 0 AND is_reviewed = 1")
    fun getUnsyncedUserComments(): List<RecipeNameAndReview>

    @Transaction
    @Query("UPDATE recipe_table SET is_review_synced = 1 WHERE recipe_name = :recipeName")
    fun markCommentAsSynced(recipeName: String)




    @Transaction
    @Query("SELECT comment_id FROM comments_table WHERE local_user_like_was_synced = 0 AND liked_by_local_user = 1")
    fun getUnsyncedUserLikes(): List<String>

    @Transaction
    @Query("UPDATE comments_table SET local_user_like_was_synced = 1 WHERE comment_id = :likeId")
    fun markLikeAsSynced(likeId: String)






    @Transaction
    @Query("SELECT recipe_name, local_user_rating FROM recipe_table WHERE is_rating_synced = 0 AND is_rated = 1")
    fun getUnsyncedUserRatings(): List<RecipeNameAndRating>

    @Transaction
    @Query("UPDATE recipe_table SET is_rating_synced = 1 WHERE recipe_name = :recipeName")
    fun markRatingAsSynced(recipeName: String)




    @Transaction
    @Query("UPDATE user_table SET user_id = :uid")
    fun setUid(uid: String)

    @Transaction
    @Query("SELECT user_id FROM user_table")
    fun getUserId(): String


}