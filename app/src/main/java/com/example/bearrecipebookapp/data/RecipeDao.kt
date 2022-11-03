package com.example.bearrecipebookapp.data

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Query
import androidx.room.Transaction
import com.example.bearrecipebookapp.datamodel.RecipeWithIngredients
import com.example.bearrecipebookapp.datamodel.RecipeWithIngredientsAndInstructions
import com.example.bearrecipebookapp.datamodel.RecipeWithInstructions

@Dao
interface RecipeDao {




    @Query("SELECT * FROM recipe_table")
    fun getAllRecipes(): LiveData<List<RecipeEntity>>

    @Query("SELECT * FROM recipe_table WHERE on_menu = 1")
    fun getSelectedRecipes(): LiveData<List<RecipeEntity>>


    @Transaction
    @Query("SELECT * FROM ingredient_table WHERE quantity_needed > 0")
    fun getNeededIngredients(): LiveData<List<IngredientEntity>>


    @Transaction
    @Query("SELECT * FROM recipe_table WHERE on_menu = 1")
    fun getSelectedRecipesWithIngredients(): LiveData<List<RecipeWithIngredients>>


    @Transaction
    @Query("SELECT * FROM recipe_table WHERE on_menu = 1")
    fun getSelectedRecipesWithInstructions(): LiveData<List<RecipeWithInstructions>>





    @Transaction
    @Query("SELECT * FROM recipe_table")
    fun getRecipesWithIngredientsAndInstructions(): LiveData<List<RecipeWithIngredientsAndInstructions>>

    @Transaction
    @Query("SELECT * FROM recipe_table WHERE on_menu = 1")
    fun getSelectedRecipesWithIngredientsAndInstructions(): LiveData<List<RecipeWithIngredientsAndInstructions>>



    // This cannot run on the main thread, so it needs a coroutine on IO. Since its in IO coroutine
    //we cannot return a value from it.  Two solutions are possible: 1) read all data at start as live data or
    //use multiple view models.  When second view model is created pass it a parameter that it can use for the SELECT
    //or look up query, this way there are no problems with initializing the variable as it can be initialized to the correct
    //value when the view model is first called.  Right now we are having problems choosing initializers for this value
    //because at start up it is unknown.  It seems like adding second view model that isn't called on app start, and
    //when it is called it already knows a parameter to use for the look up would solve this problem.

//    @Transaction
//    @Query("SELECT * FROM recipe_table WHERE recipe_name = :name")
//    fun getInstructions(name: String): RecipeWithInstructions






    @Transaction
    @Query("SELECT * FROM recipe_table WHERE is_details_screen_target = 1")
    fun getDetailsScreenTargetWithIngredientsAndInstructions(): LiveData<RecipeWithIngredientsAndInstructions>


    @Transaction
    @Query("SELECT * FROM recipe_table WHERE is_details_screen_target = 1")
    fun getDetailsScreenTarget(): LiveData<RecipeWithIngredients>


    @Transaction
    @Query("UPDATE recipe_table SET is_details_screen_target = 1 WHERE recipe_name = :name")
    fun setDetailsScreenTarget(name: String)

    @Transaction
    @Query("UPDATE recipe_table SET is_details_screen_target = 0 WHERE recipe_name = :name")
    fun removeAsDetailsScreenTarget(name: String)





    @Transaction
    @Query("UPDATE ingredient_table SET quantity_needed = :quantityNeeded WHERE ingredient_name = :name")
    fun updateQuantityNeeded(name: String, quantityNeeded: Int)


    @Query("UPDATE recipe_table SET on_menu = :onMenu WHERE recipe_name = :name")
    fun updateMenu(name: String, onMenu: Int)


    @Transaction
    @Query("SELECT * FROM recipe_table")
    fun getRecipesWithIngredients(): LiveData<List<RecipeWithIngredients>>


    @Transaction
    @Query("SELECT * FROM recipe_table")
    fun getRecipesWithInstructions(): LiveData<List<RecipeWithInstructions>>




    @Transaction
    @Query("UPDATE ingredient_table SET quantity_owned = quantity_needed WHERE ingredient_name = :name")
    fun setIngredientToOwned(name: String)

    @Transaction
    @Query("UPDATE ingredient_table SET quantity_owned = 0 WHERE ingredient_name = :name")
    fun setIngredientToNotOwned(name: String)

    @Transaction
    @Query("UPDATE ingredient_table SET quantity_owned = :quantityOwned WHERE ingredient_name = :name")
    fun setIngredientQuantityOwned(name: String, quantityOwned: Int)

//
//    @Query("SELECT ingredient_table.* FROM recipe_table "
//            +"INNER JOIN recipe_ingredient_join_table "
//            +"ON recipe_table.recipe_name = recipe_ingredient_join_table.recipe_name "
//            +"INNER JOIN ingredient_table "
//            +"ON ingredient_table.ingredient_name = recipe_ingredient_join_table.ingredient_name "
//            +"WHERE recipe_table.recipe_name = :recipeName")
//    fun getIngredientsUsingRecipeName(recipeName:String): List<IngredientEntity>




    ///////////////////////////Look out below...///////////////////////////////////////





//    @Query("SELECT * FROM ingredient_table WHERE ingredient_name = :recipeName")
//    fun getIngredientsUsingRecipeName(recipeName:String): List<IngredientEntity>




//    @Query("SELECT * FROM word_table ORDER BY word ASC")
//    fun getIngredientsList(): List<String>


//    @Query("SELECT DISTINCT recipe_name FROM recipe_table ORDER BY recipe_name ASC")
//    fun getAllRecipeNames(): List<String>
//
//    @Query("SELECT ingredient FROM recipe_table WHERE recipe_name = :name ORDER BY ingredient ASC")
//    fun getThisRecipesIngredients(name: String): List<String>
//
//    @Query("SELECT instruction FROM recipe_table WHERE recipe_name = :name ORDER BY instruction ASC")
//    fun getThisRecipesInstructions(name: String): List<String>
//
//    @Query("SELECT on_menu FROM recipe_table WHERE recipe_name = :name")
//    fun getThisRecipesOnMenuState(name: String): Int
//
//    @Query("SELECT image FROM recipe_table WHERE recipe_name = :name ORDER BY image ASC")
//    fun getThisRecipesImagesRefs(name: String): List<Int>
//    /////////
//    /////////
//
//    @Query("SELECT * FROM recipe_table ORDER BY recipe_name ASC, ingredient ASC")
//    fun getAllRecipes(): List<RecipeEntity>
//
//    @Query("SELECT * FROM recipe_table WHERE recipe_name = :name ORDER BY recipe_name ASC, ingredient ASC")
//    fun getRecipeByName(name: String): List<RecipeEntity>
//
//    @Query("SELECT * FROM recipe_table WHERE on_menu = 1 ORDER BY recipe_name ASC, ingredient ASC")
//    fun getMenuRecipes(): List<RecipeEntity>
//
//    @Query("UPDATE recipe_table SET on_menu = :onMenu WHERE recipe_name = :name")
//    fun updateOnMenu(name: String, onMenu: Int)

//    @Query("DELETE FROM products WHERE productName = :name")
//    fun deleteProduct(name: String)

//
//    @Query("SELECT * FROM recipe_table ORDER BY ingredient ASC")
//    fun getAlphabetizedWords(): List<String>
//
//    @Insert(onConflict = OnConflictStrategy.IGNORE)
//    suspend fun insert(word: Word)
//
//    @Query("DELETE FROM word_table")
//    suspend fun deleteAll()
}