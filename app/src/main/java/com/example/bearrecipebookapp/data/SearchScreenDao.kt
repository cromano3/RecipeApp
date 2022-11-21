package com.example.bearrecipebookapp.data

import androidx.room.Dao
import androidx.room.Query
import androidx.room.Transaction
import com.example.bearrecipebookapp.datamodel.HomeScreenDataModel


@Dao
interface SearchScreenDao {

    @Transaction
    @Query("SELECT recipe_name FROM recipe_table ORDER BY recipe_name ASC")
    fun getRecipeNamesReferenceList(): List<String>

    @Transaction
    @Query("SELECT ingredient_name FROM ingredient_table ORDER BY ingredient_name ASC")
    fun getIngredientNamesReferenceList(): List<String>
    @Transaction
    @Query("SELECT filter_name FROM filters_table ORDER BY filter_name ASC")
    fun getFilterNamesReferenceList(): List<String>

    @Transaction
    @Query("SELECT * FROM recipe_table")
    fun getRecipes(): List<HomeScreenDataModel>

}