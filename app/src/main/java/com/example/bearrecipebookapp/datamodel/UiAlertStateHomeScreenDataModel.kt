package com.example.bearrecipebookapp.datamodel

import com.example.bearrecipebookapp.data.entity.RecipeEntity

data class UiAlertStateHomeScreenDataModel(
    val showAlert: Boolean = false,
    val recipe: RecipeWithIngredients = RecipeWithIngredients(RecipeEntity(), listOf()),
    )
