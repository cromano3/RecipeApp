package com.example.bearrecipebookapp.datamodel

data class UiSearchScreenDataModel (
    val recipeNames: List<String> = listOf(),
    val ingredientNames: List<String> = listOf(),
    val filterNames: List<String> = listOf(),
    val previewList: List<SearchItemWithCategory> = listOf(),
    val clickSearchResults: List<HomeScreenDataModel> = listOf(),
    val showResults: Boolean = false,
    )