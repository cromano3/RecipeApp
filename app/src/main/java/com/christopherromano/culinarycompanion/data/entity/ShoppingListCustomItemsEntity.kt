package com.christopherromano.culinarycompanion.data.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "shopping_list_custom_items_table")
class ShoppingListCustomItemsEntity (

    @PrimaryKey(autoGenerate = false)
    @ColumnInfo(name = "item")
    val item: String = "",

    @ColumnInfo(name = "selected")
    val selected: Int = 0,
)