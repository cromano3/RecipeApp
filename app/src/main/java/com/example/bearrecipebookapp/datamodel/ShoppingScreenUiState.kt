package com.example.bearrecipebookapp.datamodel

import com.example.bearrecipebookapp.data.entity.ShoppingListCustomItemsEntity

data class ShoppingScreenUiState(
    val customItems: List<ShoppingListCustomItemsEntity> = listOf(),
    var isWorking: Boolean = false,
    var isFiltered: Boolean = false,
    var counter: Int = 0)
