package com.example.bearrecipebookapp.datamodel

import com.example.bearrecipebookapp.data.FilterEntity

data class HomeScreenUiStateDataModel(val isUnfiltered: Boolean = true,
                                     val isSecondFilter: Boolean = false,
                                     val filtersList: List<FilterEntity> = listOf(),
                                     val unfilteredList: List<RecipeWithIngredients> = listOf(),
                                     val filteredList1: List<RecipeWithIngredients> = listOf(),
                                     val filteredList2: List<RecipeWithIngredients> = listOf()
                                     )
