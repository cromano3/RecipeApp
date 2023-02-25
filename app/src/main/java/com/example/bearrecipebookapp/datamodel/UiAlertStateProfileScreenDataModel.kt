package com.example.bearrecipebookapp.datamodel

import com.example.bearrecipebookapp.data.entity.RecipeEntity

data class UiAlertStateProfileScreenDataModel(
    val showRemoveFavoriteAlert: Boolean = false,
    val showDeleteReviewAlert: Boolean = false,
    val recipeNameToDelete: String = "",
    val commentIDToDelete: String = "",
    val recipe: RecipeWithIngredientsAndInstructions = RecipeWithIngredientsAndInstructions(RecipeEntity(), listOf(), listOf()),
)
