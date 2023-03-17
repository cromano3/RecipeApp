package com.christopherromano.culinarycompanion.datamodel

import com.christopherromano.culinarycompanion.data.entity.RecipeEntity


data class UiAlertStateHomeScreenDataModel(
    val showAlert: Boolean = false,
    val recipe: RecipeWithIngredients = RecipeWithIngredients(RecipeEntity(), listOf()),
    )
