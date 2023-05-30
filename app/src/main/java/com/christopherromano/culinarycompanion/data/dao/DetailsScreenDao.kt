package com.christopherromano.culinarycompanion.data.dao


import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Query
import androidx.room.Transaction
import com.christopherromano.culinarycompanion.data.entity.InstructionEntity
import com.christopherromano.culinarycompanion.data.entity.QuantitiesTableEntity


@Dao
interface DetailsScreenDao {


    @Transaction
    @Query("SELECT global_rating FROM recipe_table WHERE recipe_name = :recipeName")
    fun getGlobalRating(recipeName: String): LiveData<Int>

    @Transaction
    @Query("SELECT * FROM instructions_table WHERE recipe_id = :recipeName ORDER BY instruction_id ASC")
    fun getInstructionsList(recipeName: String): LiveData<List<InstructionEntity>>

    @Transaction
    @Query("SELECT * FROM quantities_table WHERE recipe_name = :recipeName ORDER BY ingredient_name ASC")
    fun getIngredientQuantitiesList(recipeName: String): LiveData<List<QuantitiesTableEntity>>


    @Transaction
    @Query("UPDATE recipe_table SET global_rating = :rating WHERE recipe_name = :recipeName")
    fun setGlobalRating(recipeName: String, rating: Int)




    @Transaction
    @Query("UPDATE recipe_table SET cooked_count = cooked_count + 1 WHERE recipe_name = :name")
    fun addCooked(name: String)


    @Transaction
    @Query("UPDATE recipe_table SET is_shopping_filter = 1")
    fun cleanShoppingFilters()

    @Transaction
    @Query("UPDATE ingredient_table SET is_shown = 1 WHERE quantity_needed > 0")
    fun cleanIngredients()

    @Transaction
    @Query("UPDATE ingredient_table SET quantity_needed = :quantityNeeded WHERE ingredient_name = :name")
    fun updateQuantityNeeded(name: String, quantityNeeded: Int)

    @Query("UPDATE recipe_table SET on_menu = :onMenu WHERE recipe_name = :name")
    fun updateMenu(name: String, onMenu: Int)

    @Query("UPDATE recipe_table SET is_favorite = :isFavoriteStatus WHERE recipe_name = :name")
    fun updateFavorite(name: String, isFavoriteStatus: Int)

    @Transaction
    @Query("UPDATE recipe_table SET local_user_rating = :rating, is_rated = 1 WHERE recipe_name = :name")
    fun setLocalRating(name: String, rating: Int)

    @Transaction
    @Query("UPDATE recipe_table SET is_reviewed = 1, is_review_synced = 1 WHERE recipe_name = :name")
    fun setReviewAsWritten(name: String)

    @Transaction
    @Query("UPDATE ingredient_table SET quantity_owned = :quantityOwned WHERE ingredient_name = :name")
    fun setIngredientQuantityOwned(name: String, quantityOwned: Int)

    @Transaction
    @Query("UPDATE recipe_table SET on_menu = 0 WHERE recipe_name = :recipeName")
    fun removeFromMenu(recipeName: String)

    @Transaction
    @Query("UPDATE recipe_table SET on_menu = 1 WHERE recipe_name = :recipeName")
    fun addToMenu(recipeName: String)

    @Transaction
    @Query("UPDATE recipe_table SET is_favorite = 1 WHERE recipe_name = :recipeName")
    fun setAsFavorite(recipeName: String)

    @Transaction
    @Query("UPDATE user_table SET exp_to_give = exp_to_give + :expToGive")
    fun addExpToGive(expToGive: Int)
}