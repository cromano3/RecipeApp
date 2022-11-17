package com.example.bearrecipebookapp.datamodel

import com.example.bearrecipebookapp.data.FilterEntity

data class UiFiltersStateDataModel(
    val filtersList: List<FilterEntity> = listOf(),
    val isWorking: Boolean = false,
    val showAllRecipes: Boolean = true)
