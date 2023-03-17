package com.christopherromano.culinarycompanion.datamodel

import com.christopherromano.culinarycompanion.data.entity.ShoppingListCustomItemsEntity

data class ShoppingScreenUiState(
    val customItems: List<ShoppingListCustomItemsEntity> = listOf(),
    var isWorking: Boolean = false,
    var isFiltered: Boolean = false,
    var counter: Int = 0)
