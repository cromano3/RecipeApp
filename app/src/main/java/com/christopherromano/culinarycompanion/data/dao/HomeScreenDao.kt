package com.christopherromano.culinarycompanion.data.dao


import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Query
import androidx.room.Transaction
import com.christopherromano.culinarycompanion.data.entity.FilterEntity
import com.christopherromano.culinarycompanion.datamodel.HomeScreenDataModel
import com.christopherromano.culinarycompanion.datamodel.RecipeWithIngredients



@Dao
interface HomeScreenDao {

    @Transaction
    @Query("UPDATE recipe_table SET is_shopping_filter = 1")
    fun cleanShoppingFilters()

    @Transaction
    @Query("UPDATE ingredient_table SET is_shown = 1 WHERE quantity_needed > 0")
    fun cleanIngredients()


    @Transaction
    @Query("UPDATE filters_table SET is_active_filter = 2 WHERE filter_name = :name")
    fun filterBy(name: String)

    @Transaction
    @Query("UPDATE filters_table SET is_active_filter = 0 WHERE filter_name <> :name")
    fun removeOtherFilters(name: String)

    //

    @Transaction
    @Query("UPDATE recipe_table SET is_shown = 0 WHERE recipe_name = :name")
    fun setRecipeToNotShown(name: String)

    @Transaction
    @Query("UPDATE recipe_table SET is_shown = 1 WHERE recipe_name = :name")
    fun setRecipeToShown(name: String)

    //

    @Transaction
    @Query("UPDATE filters_table SET is_active_filter = 1")
    fun cleanFilters()

    @Transaction
    @Query("UPDATE recipe_table SET is_shown = 1")
    fun cleanRecipes()

    //

    @Transaction
    @Query("SELECT * FROM recipe_table WHERE is_shown = 1 ORDER BY recipe_name ASC")
    fun getRecipeList(): LiveData<List<RecipeWithIngredients>>




    //////




    @Transaction
    @Query("SELECT * FROM recipe_table")
    fun getUnfilteredList(): LiveData<List<RecipeWithIngredients>>

    @Transaction
    @Query("SELECT * FROM recipe_table WHERE is_shown = 1")
    fun getFilteredList1(): LiveData<List<RecipeWithIngredients>>

    @Transaction
    @Query("SELECT * FROM recipe_table WHERE is_shown = 2")
    fun getFilteredList2(): LiveData<List<RecipeWithIngredients>>





    @Transaction
    @Query("UPDATE filters_table SET is_shown = 1")
    fun setAllFiltersToShown()

    @Transaction
    @Query("UPDATE filters_table SET is_active_filter = 0")
    fun setAllFiltersToOff()

    @Transaction
    @Query("SELECT * FROM filters_table WHERE filter_name <> 'Unfiltered' ORDER BY is_active_filter DESC, filter_name ASC")
    fun getFilters(): LiveData<List<FilterEntity>>

    @Transaction
    @Query("SELECT * FROM recipe_table")
    suspend fun newGetData(): MutableList<HomeScreenDataModel>

    @Transaction
    @Query("SELECT * FROM filters_table WHERE filter_name <> 'Unfiltered'")
    suspend fun getUiData(): MutableList<FilterEntity>

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
    @Query("UPDATE recipe_table SET is_shown = 0")
    fun setAllToShown()

    @Transaction
    @Query("UPDATE recipe_table SET is_shown = :isShown WHERE recipe_name = :recipeName")
    fun setIsShown(recipeName: String, isShown: Int)

    @Transaction
    @Query("SELECT show_tutorial FROM user_table")
    fun getShowTutorial(): LiveData<String>

    @Transaction
    @Query("UPDATE user_table SET show_tutorial = 'false'")
    fun clearTutorialAlert()


//    @Transaction
//    @Query("SELECT * FROM recipe_table")
//    fun getData(): LiveData<List<HomeScreenDataModel>>


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

    @Transaction
    @Query("UPDATE filters_table SET is_shown = 0 WHERE filter_name <> :filterName")
    fun hideOtherFilters(filterName: String)




}