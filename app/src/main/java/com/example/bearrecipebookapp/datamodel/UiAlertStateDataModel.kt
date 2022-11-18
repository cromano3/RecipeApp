package com.example.bearrecipebookapp.datamodel

import com.example.bearrecipebookapp.data.RecipeEntity

data class UiAlertStateDataModel(
    val showAlert: Boolean = false,
    val recipe: RecipeWithIngredients = RecipeWithIngredients(RecipeEntity(), listOf()),
    )
