package com.christopherromano.culinarycompanion.datamodel

import androidx.compose.ui.text.input.TextFieldValue

data class UiAlertStateShoppingScreenDataModel(
    val showAddRecipeOrCustomItemAlert: Boolean = false,
    val showAddCustomItemAlert: Boolean = false,
    val showClearAllCustomItemsAlert: Boolean = false,
    val inputText: TextFieldValue = TextFieldValue(""),
)
