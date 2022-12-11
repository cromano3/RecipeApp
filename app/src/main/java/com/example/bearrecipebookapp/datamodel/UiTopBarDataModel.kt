package com.example.bearrecipebookapp.datamodel

import androidx.compose.ui.text.input.TextFieldValue

data class UiTopBarDataModel(
    val recipeNames: List<String> = listOf(),
    val ingredientNames: List<String> = listOf(),
    val filterNames: List<String> = listOf(),
    val currentInput: TextFieldValue = TextFieldValue(text = ""),
    val showResults: Boolean = false,
)
