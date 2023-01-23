package com.example.bearrecipebookapp.datamodel

import com.example.bearrecipebookapp.data.entity.RecipeEntity

data class UiAlertStateProfileScreenDataModel(
    val showRemoveFavoriteAlert: Boolean = false,
    val recipe: RecipeWithIngredientsAndInstructions = RecipeWithIngredientsAndInstructions(RecipeEntity(), listOf(), listOf()),
)
