package com.example.bearrecipebookapp.data

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Query
import androidx.room.Transaction
import com.example.bearrecipebookapp.datamodel.RecipeWithIngredientsAndInstructions

@Dao
interface ShoppingListScreenDao {

    @Transaction
    @Query("SELECT * FROM recipe_table")
    fun getData(): LiveData<List<RecipeWithIngredientsAndInstructions>>

    @Transaction
    @Query("SELECT * FROM ingredient_table WHERE quantity_needed > 0")
    fun getNeededIngredients(): LiveData<List<IngredientEntity>>



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