package com.example.bearrecipebookapp.datamodel

import com.example.bearrecipebookapp.data.RecipeEntity

data class UiAlertStateDetailsScreenDataModel(
    val showRemoveAlert: Boolean = false,
    val showCompletedAlert: Boolean = false,
    val recipe: RecipeWithIngredientsAndInstructions = RecipeWithIngredientsAndInstructions(RecipeEntity(), listOf(), listOf()),
)

