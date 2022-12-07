package com.example.bearrecipebookapp.data

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Query
import androidx.room.Transaction
import com.example.bearrecipebookapp.datamodel.RecipeWithIngredients


@Dao
interface ProfileScreenDao {

    @Transaction
    @Query("SELECT * FROM recipe_table WHERE is_favorite > 0")
    fun getFavorites(): LiveData<List<RecipeWithIngredients>>

    @Transaction
    @Query("SELECT * FROM recipe_table WHERE cooked_count > 0")
    fun getCooked(): LiveData<List<RecipeWithIngredients>>

    @Query("UPDATE recipe_table SET is_favorite = :isFavoriteStatus WHERE recipe_name = :name")
    fun updateFavorite(name: String, isFavoriteStatus: Int)

    @Transaction
    @Query("UPDATE details_screen_target_table SET target_name = :recipeName")
    fun setDetailsScreenTarget(recipeName: String)

}