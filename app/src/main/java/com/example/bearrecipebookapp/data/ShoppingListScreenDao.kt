package com.example.bearrecipebookapp.data

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Query
import androidx.room.Transaction
import com.example.bearrecipebookapp.datamodel.RecipeWithIngredients

@Dao
interface ShoppingListScreenDao {

    @Transaction
    @Query("SELECT * FROM recipe_table WHERE on_menu = 1 ORDER BY is_shopping_filter DESC, recipe_name ASC")
    fun getData(): LiveData<List<RecipeWithIngredients>>


    @Transaction
    @Query("SELECT * FROM ingredient_table WHERE quantity_needed > 0 ORDER BY is_shown DESC, ingredient_name ASC")
    fun getNeededIngredients(): LiveData<List<IngredientEntity>>





    @Transaction
    @Query("UPDATE recipe_table SET is_shopping_filter = 1")
    fun cleanFilters()

    @Transaction
    @Query("UPDATE ingredient_table SET is_shown = 1 WHERE quantity_needed > 0")
    fun cleanIngredients()



    @Transaction
    @Query("UPDATE recipe_table SET is_shopping_filter = 2 WHERE recipe_name = :name")
    fun filterBy(name: String)

    @Transaction
    @Query("UPDATE recipe_table SET is_shopping_filter = 0 WHERE recipe_name <> :name")
    fun removeOtherFilters(name: String)



    @Transaction
    @Query("UPDATE ingredient_table SET is_shown = 0 WHERE ingredient_name = :name")
    fun setIngredientToNotShown(name: String)

    @Transaction
    @Query("UPDATE ingredient_table SET is_shown = 1 WHERE ingredient_name = :name")
    fun setIngredientToShown(name: String)





    @Transaction
    @Query("UPDATE ingredient_table SET quantity_owned = quantity_needed WHERE ingredient_name = :name")
    fun setIngredientToOwned(name: String)

    @Transaction
    @Query("UPDATE ingredient_table SET quantity_owned = 0 WHERE ingredient_name = :name")
    fun setIngredientToNotOwned(name: String)





    @Transaction
    @Query("UPDATE details_screen_target_table SET target_name = :recipeName")
    fun setDetailsScreenTarget(recipeName: String)

}