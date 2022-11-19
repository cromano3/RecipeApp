package com.example.bearrecipebookapp.datamodel

import com.example.bearrecipebookapp.data.RecipeEntity

data class UiAlertStateMenuScreenDataModel(
    val showRemoveAlert: Boolean = false,
    val showCompletedAlert: Boolean = false,
    val recipe: RecipeWithIngredients = RecipeWithIngredients(RecipeEntity(), listOf()),
)
