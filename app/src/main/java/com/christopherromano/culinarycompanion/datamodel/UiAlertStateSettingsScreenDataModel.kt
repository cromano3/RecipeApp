package com.christopherromano.culinarycompanion.datamodel

import androidx.compose.ui.text.input.TextFieldValue

data class UiAlertStateSettingsScreenDataModel(
    val showChangeDisplayNameAlert: Boolean = false,
    val showDeleteAccountAlert: Boolean = false,
    val showAccountWasDeletedMessage: Boolean = false,
    val showLicenses: Boolean = false,
    val displayName: String = "",
    val inputText: TextFieldValue = TextFieldValue(""),
)
