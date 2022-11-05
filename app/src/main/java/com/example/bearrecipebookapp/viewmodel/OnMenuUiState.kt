package com.example.bearrecipebookapp.viewmodel

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "recipe_table2")
data class OnMenuUiState(

    @PrimaryKey(autoGenerate = false)
    @ColumnInfo(name = "recipe_name2")
    var onMenu: Int = 0
)
