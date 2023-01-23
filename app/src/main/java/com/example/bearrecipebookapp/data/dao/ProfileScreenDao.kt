package com.example.bearrecipebookapp.data.dao

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Query
import androidx.room.Transaction
import com.example.bearrecipebookapp.datamodel.RecipeWithIngredients
import com.example.bearrecipebookapp.datamodel.RecipeWithIngredientsAndInstructions


@Dao
interface ProfileScreenDao {

    @Transaction
    @Query("SELECT * FROM recipe_table WHERE is_favorite > 0")
    fun getFavorites(): LiveData<List<RecipeWithIngredientsAndInstructions>>

    @Transaction
    @Query("SELECT * FROM recipe_table WHERE cooked_count > 0 ORDER BY cooked_count DESC")
    fun getCooked(): LiveData<List<RecipeWithIngredients>>

    @Transaction
    @Query("SELECT exp_to_give FROM user_table")
    fun getExpToGive(): LiveData<Int>

    @Transaction
    @Query("SELECT exp_total FROM user_table")
    fun getExp(): LiveData<Int>

    @Transaction
    @Query("SELECT exp_to_give FROM user_table")
    fun getExpToGive1(): Int

    @Transaction
    @Query("SELECT exp_total FROM user_table")
    fun getExp1(): Int

    @Query("UPDATE recipe_table SET is_favorite = :isFavoriteStatus WHERE recipe_name = :name")
    fun updateFavorite(name: String, isFavoriteStatus: Int)

    @Transaction
    @Query("UPDATE details_screen_target_table SET target_name = :recipeName")
    fun setDetailsScreenTarget(recipeName: String)

    @Transaction
    @Query("UPDATE user_table SET exp_total = exp_total + :expChange")
    fun addToExp(expChange: Int)

    @Transaction
    @Query("UPDATE user_table SET exp_to_give = exp_to_give - :expChange")
    fun removeFromExpToGive(expChange: Int)

    @Transaction
    @Query("UPDATE user_table SET exp_to_give = 0")
    fun clearExpToGive()

}