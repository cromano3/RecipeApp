package com.example.bearrecipebookapp.datamodel

import com.example.bearrecipebookapp.data.RecipeEntity

data class UiAlertStateProfileScreenDataModel(
    val showRemoveFavoriteAlert: Boolean = false,
    val recipe: RecipeWithIngredients = RecipeWithIngredients(RecipeEntity(), listOf()),
)
