package com.example.bearrecipebookapp.data

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Query
import androidx.room.Transaction
import com.example.bearrecipebookapp.datamodel.RecipeWithIngredientsAndInstructions

@Dao
interface TopBarDao {

    @Transaction
    @Query("SELECT recipe_table.* FROM recipe_table INNER JOIN details_screen_target_table ON details_screen_target_table.target_name = recipe_table.recipe_name")
    fun getData(): LiveData<RecipeWithIngredientsAndInstructions>

    @Query("UPDATE recipe_table SET is_favorite = :isFavoriteStatus WHERE recipe_name = :name")
    fun updateFavorite(name: String, isFavoriteStatus: Int)

}