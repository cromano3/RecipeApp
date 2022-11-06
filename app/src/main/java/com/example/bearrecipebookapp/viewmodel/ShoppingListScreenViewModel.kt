package com.example.bearrecipebookapp.viewmodel

import android.app.Application
import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import com.example.bearrecipebookapp.data.IngredientEntity
import com.example.bearrecipebookapp.data.RecipeAppDatabase
import com.example.bearrecipebookapp.data.ShoppingListScreenRepository
import com.example.bearrecipebookapp.datamodel.RecipeWithIngredientsAndInstructions

class ShoppingListScreenViewModel (application: Application): ViewModel() {

    private val repository: ShoppingListScreenRepository

    var shoppingListScreenData: LiveData<List<RecipeWithIngredientsAndInstructions>>
    var selectedIngredients: LiveData<List<IngredientEntity>>


    init {
        val appDb = RecipeAppDatabase.getInstance(application)
        val shoppingListScreenDao = appDb.ShoppingListScreenDao()
        repository = ShoppingListScreenRepository(shoppingListScreenDao)

        shoppingListScreenData = repository.shoppingListScreenData
        selectedIngredients = repository.selectedIngredients
    }



    fun ingredientSelected(ingredientEntity: IngredientEntity){
        repository.setIngredientToOwned(ingredientEntity)
    }

    fun ingredientDeselected(ingredientEntity: IngredientEntity){
        repository.setIngredientToNotOwned(ingredientEntity)
    }



    fun setDetailsScreenTarget(recipeName: String){
        repository.setDetailsScreenTarget(recipeName)
    }
}