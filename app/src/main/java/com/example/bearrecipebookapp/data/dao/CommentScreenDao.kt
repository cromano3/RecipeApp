package com.example.bearrecipebookapp.data.dao

import androidx.room.Dao
import androidx.room.Query
import androidx.room.Transaction

@Dao
interface CommentScreenDao {

    @Transaction
    @Query("UPDATE recipe_table SET is_reviewed = 1 WHERE recipe_name = :name")
    fun setReviewAsWritten(name: String)

    @Transaction
    @Query("UPDATE recipe_table SET review = :reviewText, is_reviewed = 1 WHERE recipe_name = :name")
    fun setReview(name: String, reviewText: String)

}