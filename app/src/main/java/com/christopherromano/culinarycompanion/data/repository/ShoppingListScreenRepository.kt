package com.christopherromano.culinarycompanion.data.repository

import androidx.lifecycle.LiveData
import androidx.lifecycle.map
import com.christopherromano.culinarycompanion.data.dao.ShoppingListScreenDao
import com.christopherromano.culinarycompanion.data.entity.IngredientEntity
import com.christopherromano.culinarycompanion.data.entity.ShoppingListCustomItemsEntity
import com.christopherromano.culinarycompanion.datamodel.IngredientsWithQuantities
import com.christopherromano.culinarycompanion.datamodel.RecipeWithIngredients
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class ShoppingListScreenRepository(private val shoppingListScreenDao: ShoppingListScreenDao) {

    private val coroutineScope = CoroutineScope(Dispatchers.Main)

    var shoppingListScreenData: LiveData<List<RecipeWithIngredients>> = shoppingListScreenDao.getData()
    var selectedIngredients:  LiveData<List<IngredientEntity>> = shoppingListScreenDao.getNeededIngredients()
    var selectedIngredients2:  LiveData<List<IngredientsWithQuantities>> = (shoppingListScreenDao.getNeededIngredients2()).map { it ->
        it.groupBy { quantityEntity -> quantityEntity.ingredientName }
            .map { mapEntry ->
                val totalQuantity = mapEntry.value.sumOf {
                    try{
                        it.quantity.toDouble()
                    } catch(e: java.lang.NumberFormatException){
                        0.0
                    }
                }
                val firstIngredient = mapEntry.value.first()

                val remainder = totalQuantity.rem(1)
                val quotient = totalQuantity.toInt()
                var remainderAsString = ""
                when (remainder) {
                    0.75 -> {
                        remainderAsString = "3/4"
                    }
                    0.5 -> {
                        remainderAsString = "1/2"
                    }
                    0.25 -> {
                        remainderAsString = "1/4"
                    }
                }

                val result = if(quotient != 0) {

                    quotient.toString() + remainderAsString
                }
                else {
                    "" + remainderAsString
                }

                var unit = firstIngredient.unit

                if(quotient > 1){
                   unit = when(firstIngredient.unit){
                       "cup" -> "cups"
                       "can" -> "cans"
                       "handful" -> "handfuls"
                       "large jar" -> "large jars"
                       "quart" -> "quarts"
                       "tablespoon" -> "tablespoons"
                       "teaspoon" -> "teaspoons"
                       else -> firstIngredient.unit
                   }
                }


                IngredientsWithQuantities(
                    mapEntry.key,
                    firstIngredient.quantityOwned,
                    firstIngredient.quantityNeeded,
                    firstIngredient.isShown,
                    result,
                    unit
                )
            }
    }
    var customIngredients: LiveData<List<ShoppingListCustomItemsEntity>> = shoppingListScreenDao.getCustomIngredients()


    fun setIngredientToNotShown(name: String){
        coroutineScope.launch(Dispatchers.IO) {
            shoppingListScreenDao.setIngredientToNotShown(name)
        }
    }

    fun setIngredientToShown(name: String){
        coroutineScope.launch(Dispatchers.IO) {
            shoppingListScreenDao.setIngredientToShown(name)
        }
    }

    suspend fun setIngredientsToNotShown(names: List<String>){
        shoppingListScreenDao.setIngredientsToNotShown(names)
    }

    suspend fun updateIngredients(ingredientsNotToBeShown: List<String>, ingredientsToBeShown: List<String>){
        shoppingListScreenDao.updateIngredients(ingredientsNotToBeShown, ingredientsToBeShown)
    }

    fun getCustomItems(): List<ShoppingListCustomItemsEntity>{
        return shoppingListScreenDao.getCustomItems()
    }

    fun addCustomItem(item: ShoppingListCustomItemsEntity){
        shoppingListScreenDao.addCustomItem(item)
    }


    fun addTutorialAlert(){
        shoppingListScreenDao.addTutorialAlert()
    }



    fun cleanFilters(){
        coroutineScope.launch(Dispatchers.IO) {
            shoppingListScreenDao.cleanFilters()
        }
    }

    fun cleanIngredients(){
        coroutineScope.launch(Dispatchers.IO) {
            shoppingListScreenDao.cleanIngredients()
        }
    }



    suspend fun removeOtherFilters(name: String){
        shoppingListScreenDao.removeOtherFilters(name)
    }

    suspend fun filterBy(name: String){
        shoppingListScreenDao.filterBy(name)
    }


    fun setCustomItemToSelected(item: ShoppingListCustomItemsEntity){
        coroutineScope.launch(Dispatchers.IO) {
            shoppingListScreenDao.setCustomItemToSelected(item.item)
        }
    }

    fun setCustomItemToDeselected(item: ShoppingListCustomItemsEntity){
        coroutineScope.launch(Dispatchers.IO) {
            shoppingListScreenDao.setCustomItemToDeselected(item.item)
        }
    }

    fun deleteCustomItem(item: ShoppingListCustomItemsEntity){
        coroutineScope.launch(Dispatchers.IO) {
            shoppingListScreenDao.deleteCustomItem(item.item)
        }
    }

    fun clearAllCustomItems(){
        coroutineScope.launch(Dispatchers.IO) {
            shoppingListScreenDao.clearAllCustomItems()
        }
    }



    fun setIngredientToOwned(ingredientName: String){
        coroutineScope.launch(Dispatchers.IO) {
            shoppingListScreenDao.setIngredientToOwned(ingredientName)
        }
    }

    fun setIngredientToNotOwned(ingredientName: String){
        coroutineScope.launch(Dispatchers.IO) {
            shoppingListScreenDao.setIngredientToNotOwned(ingredientName)
        }
    }
}