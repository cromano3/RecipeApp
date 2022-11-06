package com.example.bearrecipebookapp.data

import androidx.room.ColumnInfo
import androidx.room.Entity

@Entity(tableName = "recipe_attributes_join_table",
    primaryKeys = ["recipe_name", "attribute_name"])
class RecipeAttributesJoinEntity(

    @ColumnInfo(name = "recipe_name")
    val recipeName: String,

    @ColumnInfo(name = "attribute_name")
    val attributeName: String,
)
