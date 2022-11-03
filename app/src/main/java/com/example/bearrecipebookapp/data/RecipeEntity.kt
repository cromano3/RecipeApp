package com.example.bearrecipebookapp.data

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "recipe_table")
class RecipeEntity(
    @PrimaryKey(autoGenerate = false)
    @ColumnInfo(name = "recipe_name")
    val recipeName: String = "",

    @ColumnInfo(name = "on_menu")
    var onMenu: Int = 0,

    @ColumnInfo(name = "is_details_screen_target")
    val isDetailsScreenTarget: Int = 0,

    @ColumnInfo(name = "time_to_make")
    val timeToMake: Int = 0,

    @ColumnInfo(name = "difficulty")
    val difficulty: Int = 0,

    @ColumnInfo(name = "review_score")
    val rating: Int = 0,

//
//    @ColumnInfo(name = "ingredients")
//    val ingredients: String,



//    @ColumnInfo(name = "images")
//    val images: Int

)
