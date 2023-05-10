package com.christopherromano.culinarycompanion.data.dao


import androidx.lifecycle.LiveData
import androidx.room.*
import com.christopherromano.culinarycompanion.data.entity.IngredientEntity
import com.christopherromano.culinarycompanion.data.entity.ShoppingListCustomItemsEntity
import com.christopherromano.culinarycompanion.datamodel.IngredientsWithQuantities
import com.christopherromano.culinarycompanion.datamodel.RecipeWithIngredients


@Dao
interface ShoppingListScreenDao {

    @Transaction
    @Query("SELECT * FROM recipe_table WHERE on_menu = 1 ORDER BY recipe_name ASC")
    fun getData(): LiveData<List<RecipeWithIngredients>>


    @Transaction
    @Query("SELECT * FROM ingredient_table WHERE quantity_needed > 0 ORDER BY is_shown DESC, ingredient_name ASC")
    fun getNeededIngredients(): LiveData<List<IngredientEntity>>

    @Transaction
    @Query("SELECT DISTINCT ingredient_table.ingredient_name as ingredientName, " +
            "ingredient_table.quantity_owned as quantityOwned, " +
            "ingredient_table.quantity_needed as quantityNeeded, " +
            "ingredient_table.is_shown as isShown, " +
            "quantities_table.quantity as quantity, " +
            "quantities_table.unit as unit " +
            "FROM ingredient_table " +
            "INNER JOIN quantities_table ON ingredient_table.ingredient_name = quantities_table.ingredient_name " +
            "INNER JOIN recipe_ingredient_join_table ON quantities_table.recipe_name = recipe_ingredient_join_table.recipe_name " +
            "INNER JOIN recipe_table ON recipe_ingredient_join_table.recipe_name = recipe_table.recipe_name " +
            "WHERE ingredient_table.quantity_needed > 0 AND recipe_table.on_menu = 1 " +
            "ORDER BY ingredient_table.is_shown DESC, ingredient_table.ingredient_name ASC")
    fun getNeededIngredients2(): LiveData<List<IngredientsWithQuantities>>



    @Transaction
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun addCustomItem(item: ShoppingListCustomItemsEntity)

    @Transaction
    @Query("SELECT * FROM shopping_list_custom_items_table")
    fun getCustomIngredients(): LiveData<List<ShoppingListCustomItemsEntity>>

    @Transaction
    @Query("SELECT * FROM shopping_list_custom_items_table")
    fun getCustomItems(): List<ShoppingListCustomItemsEntity>


    @Transaction
    @Query("UPDATE user_table SET show_tutorial = 'true' ")
    fun addTutorialAlert()



    @Transaction
    @Query("UPDATE recipe_table SET is_shopping_filter = 1")
    fun cleanFilters()

    @Transaction
    @Query("UPDATE ingredient_table SET is_shown = 1 WHERE quantity_needed > 0")
    fun cleanIngredients()



    @Transaction
    @Query("UPDATE recipe_table SET is_shopping_filter = 2 WHERE recipe_name = :name")
    suspend fun filterBy(name: String)

    @Transaction
    @Query("UPDATE recipe_table SET is_shopping_filter = 0 WHERE recipe_name <> :name")
    suspend fun removeOtherFilters(name: String)



    @Transaction
    @Query("UPDATE ingredient_table SET is_shown = 0 WHERE ingredient_name = :name")
    fun setIngredientToNotShown(name: String)

    @Transaction
    @Query("UPDATE ingredient_table SET is_shown = 1 WHERE ingredient_name = :name")
    fun setIngredientToShown(name: String)

    @Transaction
    suspend fun updateIngredients(ingredientsToHide: List<String>, ingredientsToShow: List<String>){
        setIngredientsToNotShown(ingredientsToHide)
        setIngredientsToShown(ingredientsToShow)
    }

    @Query("UPDATE ingredient_table SET is_shown = 0 WHERE ingredient_name IN (:names)")
    suspend fun setIngredientsToNotShown(names: List<String>)

    @Query("UPDATE ingredient_table SET is_shown = 1 WHERE ingredient_name IN (:names)")
    suspend fun setIngredientsToShown(names: List<String>)




    @Transaction
    @Query("UPDATE shopping_list_custom_items_table SET selected = 1 WHERE item = :item")
    fun setCustomItemToSelected(item: String)

    @Transaction
    @Query("UPDATE shopping_list_custom_items_table SET selected = 0 WHERE item = :item")
    fun setCustomItemToDeselected(item: String)
    @Transaction
    @Query("DELETE FROM shopping_list_custom_items_table WHERE item = :item")
    fun deleteCustomItem(item: String)

    @Transaction
    @Query("DELETE FROM shopping_list_custom_items_table")
    fun clearAllCustomItems()






    @Transaction
    @Query("UPDATE ingredient_table SET quantity_owned = quantity_needed WHERE ingredient_name = :name")
    fun setIngredientToOwned(name: String)

    @Transaction
    @Query("UPDATE ingredient_table SET quantity_owned = 0 WHERE ingredient_name = :name")
    fun setIngredientToNotOwned(name: String)


}