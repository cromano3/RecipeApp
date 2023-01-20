package com.example.bearrecipebookapp.data.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "details_screen_target_table")
class DetailsScreenTargetEntity(

    @PrimaryKey(autoGenerate = false)
    @ColumnInfo(name = "target_name")
    val ingredientName: String = "",


    )