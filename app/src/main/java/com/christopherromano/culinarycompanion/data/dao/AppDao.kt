package com.christopherromano.culinarycompanion.data.dao


import androidx.room.Dao
import androidx.room.Query
import androidx.room.Transaction
import com.christopherromano.culinarycompanion.datamodel.RecipeNameAndRating
import com.christopherromano.culinarycompanion.datamodel.RecipeNameAndReview
import com.christopherromano.culinarycompanion.datamodel.RecipeWithIngredientsAndInstructions

@Dao
interface AppDao {

    @Transaction
    @Query("SELECT * FROM recipe_table WHERE recipe_name = :recipeName")
    fun getRecipeWithIngredientsAndInstructions(recipeName: String): RecipeWithIngredientsAndInstructions





    @Transaction
    @Query("SELECT is_online_user_type FROM user_table")
    fun onlineUserType(): Int



    @Transaction
    @Query("SELECT recipe_name AS recipeName, review AS reviewText FROM recipe_table WHERE is_review_synced = 0 AND is_reviewed = 1")
    fun getUnsyncedUserComments(): List<RecipeNameAndReview>

    @Transaction
    @Query("UPDATE recipe_table SET is_review_synced = 1 WHERE recipe_name = :recipeName")
    fun markCommentAsSynced(recipeName: String)



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
    @Query("UPDATE recipe_table SET review = :reviewText, is_reviewed = 1, is_review_synced = 1 WHERE recipe_name = :name")
    fun setReview(name: String, reviewText: String)

    @Transaction
    @Query("UPDATE recipe_table SET is_reviewed = 1, is_review_synced = 1 WHERE recipe_name = :name")
    fun setReviewIsSynced(name: String)

    @Transaction
    @Query("UPDATE recipe_table SET review = :reviewText, is_reviewed = 1, is_review_synced = 0 WHERE recipe_name = :recipeName")
    fun setReviewAsUnsynced(recipeName: String, reviewText: String)




    @Transaction
    @Query("UPDATE recipe_table SET local_user_rating = :rating, is_rated = 1, is_rating_synced = :syncStatus WHERE recipe_name = :name")
    fun setLocalRating(name: String, rating: Int, syncStatus: Int)






}