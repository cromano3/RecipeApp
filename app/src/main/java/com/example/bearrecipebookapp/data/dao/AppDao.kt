package com.example.bearrecipebookapp.data.dao

import androidx.room.Dao
import androidx.room.Query
import androidx.room.Transaction
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
    @Query("UPDATE user_table SET user_id = :uid")
    fun setUid(uid: String)
}