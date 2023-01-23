package com.example.bearrecipebookapp.datamodel

import com.example.bearrecipebookapp.data.entity.RecipeEntity

data class UiAlertStateMenuScreenDataModel(
    val showRemoveAlert: Boolean = false,
    val showCompletedAlert: Boolean = false,
    val showAddRecipeAlert: Boolean = false,
    val recipe: RecipeWithIngredients = RecipeWithIngredients(RecipeEntity(), listOf()),
)

