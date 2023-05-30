package com.christopherromano.culinarycompanion.data.dao

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Query
import androidx.room.Transaction
import com.christopherromano.culinarycompanion.datamodel.RecipeWithIngredientsAndInstructions


@Dao
interface CommentScreenDao {

    @Transaction
    @Query("SELECT * FROM recipe_table WHERE is_review_screen_target = 1")
    fun getCommentScreenTarget(): LiveData<RecipeWithIngredientsAndInstructions>

    @Transaction
    @Query("UPDATE recipe_table SET is_reviewed = 1, is_review_synced = 1 WHERE recipe_name = :name")
    fun setReviewAsWritten(name: String)

}