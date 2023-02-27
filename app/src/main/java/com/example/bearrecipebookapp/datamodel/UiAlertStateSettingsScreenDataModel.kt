package com.example.bearrecipebookapp.datamodel

import androidx.compose.ui.text.input.TextFieldValue

data class UiAlertStateSettingsScreenDataModel(
    val showChangeDisplayNameAlert: Boolean = false,
    val displayName: String = "",
    val inputText: TextFieldValue = TextFieldValue(""),
)
