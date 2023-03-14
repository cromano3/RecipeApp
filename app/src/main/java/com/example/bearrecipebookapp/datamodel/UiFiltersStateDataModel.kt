package com.example.bearrecipebookapp.datamodel

import com.example.bearrecipebookapp.data.entity.FilterEntity

data class UiFiltersStateDataModel(
    val filtersList: List<FilterEntity> = listOf(),
    val isWorking: Boolean = false,
    val showAllRecipes: Boolean = true,
    val triggerScroll: Boolean = false,
    val isFiltered: Boolean = false,
    val isScrollable: Boolean = true,
)
