package com.christopherromano.culinarycompanion.data.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "quantities_table")
class QuantitiesTableEntity(

    @PrimaryKey(autoGenerate = false)
    @ColumnInfo(name = "id")
    var id: Int,

    @ColumnInfo(name = "recipe_name")
    val recipeName: String,

    @ColumnInfo(name = "ingredient_name")
    val ingredientName: String,

    @ColumnInfo(name = "quantity")
    val quantity: String,

    @ColumnInfo(name = "unit")
    val unit: String,


)