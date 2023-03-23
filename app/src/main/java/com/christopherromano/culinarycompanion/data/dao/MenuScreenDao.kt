package com.christopherromano.culinarycompanion.data.dao

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Query
import androidx.room.Transaction
import com.christopherromano.culinarycompanion.datamodel.RecipeWithIngredientsAndInstructions

@Dao
interface MenuScreenDao {

    @Transaction
    @Query("SELECT * FROM recipe_table WHERE on_menu > 0")
    fun getData(): LiveData<List<RecipeWithIngredientsAndInstructions>>

    @Transaction
    @Query("SELECT recipe_name FROM recipe_table WHERE on_menu > 0")
    fun getOnMenuNames(): List<String>

    @Transaction
    @Query("UPDATE recipe_table SET global_rating = :rating WHERE recipe_name = :recipeName")
    fun setGlobalRatings(recipeName: String, rating: Int)

    @Transaction
    @Query("UPDATE recipe_table SET is_shopping_filter = 1")
    fun cleanShoppingFilters()

    @Transaction
    @Query("UPDATE ingredient_table SET is_shown = 1 WHERE quantity_needed > 0")
    fun cleanIngredients()



    @Transaction
    @Query("UPDATE user_table SET show_tutorial = 'true' ")
    fun addTutorialAlert()

    @Transaction
    @Query("UPDATE recipe_table SET local_user_rating = :rating, is_rated = 1 WHERE recipe_name = :name")
    fun setLocalRating(name: String, rating: Int)

    @Transaction
    @Query("UPDATE recipe_table SET is_reviewed = 1 WHERE recipe_name = :name")
    fun setReviewAsWritten(name: String)


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
    @Query("UPDATE recipe_table SET is_favorite = 1 WHERE recipe_name = :name")
    fun setAsFavorite(name: String)

    @Transaction
    @Query("UPDATE recipe_table SET cooked_count = cooked_count + 1 WHERE recipe_name = :name")
    fun addCooked(name: String)

    @Transaction
    @Query("UPDATE user_table SET exp_to_give = exp_to_give + :expToGive")
    fun addExpToGive(expToGive: Int)


}