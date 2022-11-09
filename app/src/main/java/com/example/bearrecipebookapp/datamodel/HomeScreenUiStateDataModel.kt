package com.example.bearrecipebookapp.datamodel

import com.example.bearrecipebookapp.data.FilterEntity

data class HomeScreenUiStateDataModel(val showUnfilteredList: Boolean = true,
                                      val showFilteredList1: Boolean = false,
                                      val showFilteredList2: Boolean = false,
                                      val filterObserver: Boolean = false,
                                     val filtersList: List<FilterEntity> = listOf(),
                                     val unfilteredList: List<RecipeWithIngredients> = listOf(),
                                     val filteredList1: List<RecipeWithIngredients> = listOf(),
                                     val filteredList2: List<RecipeWithIngredients> = listOf()
                                     )
