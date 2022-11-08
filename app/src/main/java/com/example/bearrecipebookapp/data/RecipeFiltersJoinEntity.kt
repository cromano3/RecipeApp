package com.example.bearrecipebookapp.data

import androidx.room.ColumnInfo
import androidx.room.Entity

@Entity(tableName = "recipe_filters_join_table",
    primaryKeys = ["recipe_name", "filter_name"])
class RecipeFiltersJoinEntity(

    @ColumnInfo(name = "recipe_name")
    val recipeName: String,

    @ColumnInfo(name = "filter_name")
    val filterName: String,
)
