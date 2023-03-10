package com.example.bearrecipebookapp.data.repository

import androidx.lifecycle.LiveData
import androidx.lifecycle.Transformations
import com.example.bearrecipebookapp.data.dao.ShoppingListScreenDao
import com.example.bearrecipebookapp.data.entity.IngredientEntity
import com.example.bearrecipebookapp.data.entity.ShoppingListCustomItemsEntity
import com.example.bearrecipebookapp.datamodel.IngredientsWithQuantities
import com.example.bearrecipebookapp.datamodel.RecipeWithIngredients
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class ShoppingListScreenRepository(private val shoppingListScreenDao: ShoppingListScreenDao) {

    private val coroutineScope = CoroutineScope(Dispatchers.Main)

    var shoppingListScreenData: LiveData<List<RecipeWithIngredients>> = shoppingListScreenDao.getData()
    var selectedIngredients:  LiveData<List<IngredientEntity>> = shoppingListScreenDao.getNeededIngredients()
    var selectedIngredients2:  LiveData<List<IngredientsWithQuantities>> = Transformations.map(shoppingListScreenDao.getNeededIngredients2()) { it ->
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

                val result = if(quotient != 0) quotient.toString() else "" + remainderAsString

                IngredientsWithQuantities(
                    mapEntry.key,
                    firstIngredient.quantityOwned,
                    firstIngredient.quantityNeeded,
                    firstIngredient.isShown,
                    result,
                    firstIngredient.unit
                )
            }
    }
    var customIngredients: LiveData<List<ShoppingListCustomItemsEntity>> = shoppingListScreenDao.getCustomIngredients()

    suspend fun setDetailsScreenTarget(recipeName: String){
//        coroutineScope.launch(Dispatchers.IO) {
            shoppingListScreenDao.setDetailsScreenTarget(recipeName)
//        }
    }

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

    fun getCustomItems(): List<ShoppingListCustomItemsEntity>{
        return shoppingListScreenDao.getCustomItems()
    }

    fun addCustomItem(item: ShoppingListCustomItemsEntity){
        coroutineScope.launch(Dispatchers.IO) {
            shoppingListScreenDao.addCustomItem(item)
        }
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



    fun removeOtherFilters(name: String){
        coroutineScope.launch(Dispatchers.IO) {
            shoppingListScreenDao.removeOtherFilters(name)
        }
    }

    fun filterBy(name: String){
        coroutineScope.launch(Dispatchers.IO) {
            shoppingListScreenDao.filterBy(name)
        }
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