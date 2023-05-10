package com.christopherromano.culinarycompanion.datamodel


import com.christopherromano.culinarycompanion.data.entity.FilterEntity


data class UiFiltersStateDataModel(
    val filtersList: List<FilterEntity> = listOf(),
    val isWorking: Boolean = false,
    val showAllRecipes: Boolean = true,
//    val triggerScroll: Boolean = false,
//    val isFiltered: Boolean = false,
//    val isScrollable: Boolean = true,
)
