package com.example.bearrecipebookapp.data.entity

import androidx.room.ColumnInfo
import androidx.room.Entity

@Entity(tableName = "recipe_ingredient_join_table",
    primaryKeys = ["recipe_name", "ingredient_name"])
class RecipeIngredientJoinEntity(

    @ColumnInfo(name = "recipe_name")
    val recipeName: String,

    @ColumnInfo(name = "ingredient_name")
    val ingredientName: String,


    )