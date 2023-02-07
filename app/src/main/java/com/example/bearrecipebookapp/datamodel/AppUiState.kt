package com.example.bearrecipebookapp.datamodel

data class AppUiState(
    val detailsScreenTarget: RecipeWithIngredientsAndInstructions = RecipeWithIngredientsAndInstructions(),
    val reviewScreenTarget: RecipeWithIngredientsAndInstructions = RecipeWithIngredientsAndInstructions()

)
