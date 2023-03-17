package com.christopherromano.culinarycompanion.datamodel

import androidx.compose.ui.text.input.TextFieldValue

data class UiSearchScreenDataModel (
    val recipeNames: List<String> = listOf(),
    val ingredientNames: List<String> = listOf(),
    val filterNames: List<String> = listOf(),
    val previewList: List<SearchItemWithCategory> = listOf(),
    val clickSearchResults: List<HomeScreenDataModel> = listOf(),
    val showResults: Boolean = false,
    val currentInput: TextFieldValue = TextFieldValue(text = ""),
    val showAlert: Boolean = false,
    val alertRecipe: HomeScreenDataModel = HomeScreenDataModel(),
    )