package com.example.bearrecipebookapp.data

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Query
import androidx.room.Transaction
import com.example.bearrecipebookapp.datamodel.HomeScreenDataModel


@Dao
interface HomeScreenDao {


    @Transaction
    @Query("UPDATE filters_table SET is_active_filter = 0")
    fun setAllFiltersToOff()

    @Transaction
    @Query("SELECT * FROM filters_table WHERE filter_name <> 'Unfiltered'")
    fun getFilters(): LiveData<List<FilterEntity>>

    @Transaction
    @Query("SELECT * FROM recipe_table")
    suspend fun newGetData(): MutableList<HomeScreenDataModel>


//    @Transaction
//    @Query("SELECT * FROM recipe_table " +
//            "JOIN recipe_filters_join_table " +
//            "ON recipe_table.recipe_name = recipe_filters_join_table.recipe_name " +
//            "JOIN filters_table " +
//            "ON recipe_filters_join_table.filter_name = filters_table.filter_name " +
//            "WHERE is_active_filter = 1 ")
//    fun getData(): LiveData<List<HomeScreenDataModel>>

    @Transaction
    @Query("SELECT * FROM recipe_table WHERE is_shown = 1")
//    @Query("SELECT * FROM recipe_table")
    fun getData(): LiveData<List<HomeScreenDataModel>>

    @Transaction
    @Query("SELECT * FROM recipe_table")
    fun getReferenceList(): LiveData<List<HomeScreenDataModel>>


    @Transaction
    @Query("UPDATE recipe_table SET is_shown = 1")
    fun setAllToShown()

    @Transaction
    @Query("UPDATE recipe_table SET is_shown = :isShown WHERE recipe_name = :recipeName")
    fun setIsShown(recipeName: String, isShown: Int)


//    @Transaction
//    @Query("SELECT * FROM recipe_table")
//    fun getData(): LiveData<List<HomeScreenDataModel>>


    @Transaction
    @Query("UPDATE ingredient_table SET quantity_needed = :quantityNeeded WHERE ingredient_name = :name")
    fun updateQuantityNeeded(name: String, quantityNeeded: Int)

    @Query("UPDATE recipe_table SET on_menu = :onMenu WHERE recipe_name = :name")
    fun updateMenu(name: String, onMenu: Int)

    @Transaction
    @Query("UPDATE ingredient_table SET quantity_owned = :quantityOwned WHERE ingredient_name = :name")
    fun setIngredientQuantityOwned(name: String, quantityOwned: Int)


    @Transaction
    @Query("UPDATE details_screen_target_table SET target_name = :recipeName")
    fun setDetailsScreenTarget(recipeName: String)


    //
    @Transaction
    @Query("UPDATE filters_table SET is_active_filter = 0 WHERE filter_name <> 'Unfiltered'")
    fun clearFilters()

    @Transaction
    @Query("UPDATE filters_table SET is_active_filter = 1 WHERE filter_name = 'Unfiltered'")
    fun setAsUnfiltered()

    @Transaction
    @Query("UPDATE filters_table SET is_active_filter = 0 WHERE filter_name = 'Unfiltered'")
    fun setAsFiltered()

    @Transaction
    @Query("UPDATE filters_table SET is_active_filter = 1 WHERE filter_name = :filterName")
    fun addFilter(filterName: String)

    @Transaction
    @Query("UPDATE filters_table SET is_active_filter = 0 WHERE filter_name = :filterName")
    fun removeFilter(filterName: String)




}