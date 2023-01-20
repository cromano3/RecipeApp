package com.example.bearrecipebookapp.data.dao

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Query
import androidx.room.Transaction
import com.example.bearrecipebookapp.datamodel.RecipeWithIngredients

@Dao
interface MenuScreenDao {

    @Transaction
    @Query("SELECT * FROM recipe_table WHERE on_menu > 0")
    fun getData(): LiveData<List<RecipeWithIngredients>>

    @Transaction
    @Query("UPDATE recipe_table SET is_shopping_filter = 1")
    fun cleanShoppingFilters()

    @Transaction
    @Query("UPDATE ingredient_table SET is_shown = 1 WHERE quantity_needed > 0")
    fun cleanIngredients()

    @Transaction
    @Query("UPDATE details_screen_target_table SET target_name = :recipeName")
    fun setDetailsScreenTarget(recipeName: String)

    @Transaction
    @Query("UPDATE ingredient_table SET quantity_needed = :quantityNeeded WHERE ingredient_name = :name")
    fun updateQuantityNeeded(name: String, quantityNeeded: Int)

    @Transaction
    @Query("UPDATE ingredient_table SET quantity_owned = :quantityOwned WHERE ingredient_name = :name")
    fun setIngredientQuantityOwned(name: String, quantityOwned: Int)

    @Transaction
    @Query("UPDATE recipe_table SET on_menu = 0 WHERE recipe_name = :recipeName")
    fun removeFromMenu(recipeName: String)

    @Transaction
    @Query("UPDATE recipe_table SET on_menu = 2 WHERE recipe_name = :recipeName")
    fun setToFadeOut(recipeName: String)

    @Query("UPDATE recipe_table SET is_favorite = :isFavoriteStatus WHERE recipe_name = :name")
    fun updateFavorite(name: String, isFavoriteStatus: Int)

    @Transaction
    @Query("UPDATE recipe_table SET cooked_count = cooked_count + 1 WHERE recipe_name = :name")
    fun addCooked(name: String)

    @Transaction
    @Query("UPDATE user_table SET exp_to_give = exp_to_give + :expToGive")
    fun addExpToGive(expToGive: Int)


    /**
     * This query executes successfully and as intended in the DB Builder.
     * However, it will not compile here.
     *
     *
     * SOLVED: Because Room does not support UPDATE FROM
     *
     */
//    @Transaction
//    @Query("UPDATE ingredient_table AS it SET quantity_needed = it.quantity_needed + 1 " +
//            "FROM recipe_ingredient_join_table " +
//            "JOIN ingredient_table " +
//            "ON it.ingredient_name = recipe_ingredient_join_table.ingredient_name " +
//            "JOIN recipe_table " +
//            "ON recipe_ingredient_join_table.recipe_name = recipe_table.recipe_name " +
//            "WHERE recipe_table.recipe_name = :name")
//    fun update(name:String)

}