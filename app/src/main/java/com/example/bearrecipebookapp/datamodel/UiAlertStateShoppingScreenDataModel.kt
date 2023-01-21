package com.example.bearrecipebookapp.datamodel

import androidx.compose.ui.text.input.TextFieldValue

data class UiAlertStateShoppingScreenDataModel(
    val showAddRecipeOrCustomItemAlert: Boolean = false,
    val showAddCustomItemAlert: Boolean = false,
    val inputText: TextFieldValue = TextFieldValue(""),
)