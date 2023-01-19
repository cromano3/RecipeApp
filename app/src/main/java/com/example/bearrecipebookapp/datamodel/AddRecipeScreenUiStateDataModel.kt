package com.example.bearrecipebookapp.datamodel

data class AddRecipeScreenUiStateDataModel(
    val recipeTitle: String = "",
    val ingredients: String = "",
    val instructionsList: MutableList<String> = mutableListOf<String>(""),
)
