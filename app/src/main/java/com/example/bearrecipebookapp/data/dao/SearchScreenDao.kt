package com.example.bearrecipebookapp.data.dao

import androidx.lifecycle.LiveData
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

    //
//    @Transaction
//    @Query("SELECT * FROM recipe_table")
//    fun getAllRecipes(): LiveData<List<HomeScreenDataModel>>

    @Transaction
    @Query("UPDATE search_table SET show_results = :isShown")
    fun setShowResults(isShown: Int)

    @Transaction
    @Query("UPDATE search_table SET text_field_value = :input")
    fun setTextFieldValue(input: String)

    @Transaction
    @Query("SELECT show_results FROM search_table")
    fun getShowResults(): LiveData<Int>

    @Transaction
    @Query("SELECT preview_list FROM search_table")
    fun getPreviewList(): LiveData<String>

    @Transaction
    @Query("SELECT * FROM recipe_table WHERE is_search_result = 1 ORDER BY recipe_name ASC")
    fun getResults(): LiveData<List<HomeScreenDataModel>>

    @Transaction
    @Query("UPDATE recipe_table SET is_search_result = :isResult WHERE recipe_name = :recipeName")
    fun setSearchResult(recipeName:String , isResult: Int)

    @Transaction
    @Query("UPDATE recipe_table SET is_search_result = 0")
    fun clearResults()

    //
    //

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
    @Query("UPDATE details_screen_target_table SET target_name = :recipeName")
    fun setDetailsScreenTarget(recipeName: String)


}