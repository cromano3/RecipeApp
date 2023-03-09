package com.example.bearrecipebookapp.datamodel

data class IngredientsWithQuantities(
    val ingredientName: String = "",
    val quantityOwned: Int = 0,
    val quantityNeeded: Int = 0,
    val isShown: Int = 1,
    val quantity: String = "",
    val unit: String = "",
)
