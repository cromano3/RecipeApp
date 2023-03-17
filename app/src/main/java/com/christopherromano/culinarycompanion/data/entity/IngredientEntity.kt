package com.christopherromano.culinarycompanion.data.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "ingredient_table")
class IngredientEntity(
    @PrimaryKey(autoGenerate = false)
    @ColumnInfo(name = "ingredient_name")
    val ingredientName: String = "",

    @ColumnInfo(name = "quantity_owned")
    val quantityOwned: Int = 0,

    @ColumnInfo(name = "quantity_needed")
    val quantityNeeded: Int = 0,

    @ColumnInfo(name = "is_shown")
    val isShown: Int = 1,
)