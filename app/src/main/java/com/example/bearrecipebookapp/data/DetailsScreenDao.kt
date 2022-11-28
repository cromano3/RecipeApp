package com.example.bearrecipebookapp.data

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Query
import androidx.room.Transaction
import com.example.bearrecipebookapp.datamodel.RecipeWithIngredientsAndInstructions

@Dao
interface DetailsScreenDao {

    @Transaction
    @Query("SELECT recipe_table.* FROM recipe_table INNER JOIN details_screen_target_table ON details_screen_target_table.target_name = recipe_table.recipe_name")
    fun getData(): LiveData<RecipeWithIngredientsAndInstructions>


    @Transaction
    @Query("UPDATE recipe_table SET is_shopping_filter = 1")
    fun cleanShoppingFilters()

    @Transaction
    @Query("UPDATE ingredient_table SET is_shown = 1 WHERE quantity_needed > 0")
    fun cleanIngredients()

    @Transaction
    @Query("UPDATE ingredient_table SET quantity_needed = :quantityNeeded WHERE ingredient_name = :name")
    fun updateQuantityNeeded(name: String, quantityNeeded: Int)

    @Query("UPDATE recipe_table SET on_menu = :onMenu WHERE recipe_name = :name")
    fun updateMenu(name: String, onMenu: Int)

    @Query("UPDATE recipe_table SET is_favorite = :isFavoriteStatus WHERE recipe_name = :name")
    fun updateFavorite(name: String, isFavoriteStatus: Int)

    @Transaction
    @Query("UPDATE ingredient_table SET quantity_owned = :quantityOwned WHERE ingredient_name = :name")
    fun setIngredientQuantityOwned(name: String, quantityOwned: Int)

    @Transaction
    @Query("UPDATE recipe_table SET on_menu = 0 WHERE recipe_name = :recipeName")
    fun removeFromMenu(recipeName: String)

    @Transaction
    @Query("UPDATE recipe_table SET on_menu = 1 WHERE recipe_name = :recipeName")
    fun addToMenu(recipeName: String)
}