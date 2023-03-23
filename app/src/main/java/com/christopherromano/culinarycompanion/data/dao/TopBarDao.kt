package com.christopherromano.culinarycompanion.data.dao

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Query
import androidx.room.Transaction
import com.christopherromano.culinarycompanion.datamodel.HomeScreenDataModel
import com.christopherromano.culinarycompanion.datamodel.RecipeWithIngredientsAndInstructions

@Dao
interface TopBarDao {

    @Transaction
    @Query("SELECT show_results FROM search_table")
    fun getShowResults(): LiveData<Int>

    @Transaction
    @Query("SELECT text_field_value FROM search_table")
    fun getTextFieldValue(): String

    @Transaction
    @Query("SELECT recipe_table.* FROM recipe_table INNER JOIN details_screen_target_table ON details_screen_target_table.target_name = recipe_table.recipe_name")
    fun getData(): LiveData<RecipeWithIngredientsAndInstructions>

    @Query("UPDATE recipe_table SET is_favorite = :isFavoriteStatus WHERE recipe_name = :name")
    fun updateFavorite(name: String, isFavoriteStatus: Int)


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

    @Transaction
    @Query("UPDATE recipe_table SET is_search_result = 0")
    fun clearResults()


    @Transaction
    @Query("UPDATE recipe_table SET is_search_result = :isResult WHERE recipe_name = :recipeName")
    fun setSearchResult(recipeName:String , isResult: Int)


    @Transaction
    @Query("UPDATE search_table SET show_results = :isShown")
    fun setShowResults(isShown: Int)

    @Transaction
    @Query("UPDATE search_table SET preview_list = :list")
    fun setPreviewList(list: String)

}