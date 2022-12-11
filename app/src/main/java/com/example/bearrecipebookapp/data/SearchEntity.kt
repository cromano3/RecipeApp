package com.example.bearrecipebookapp.data

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "search_table")
class SearchEntity(

    @ColumnInfo(name = "preview_list")
    val previewList: String = "",

    @ColumnInfo(name = "results_list")
    var resultsList: String = "",

    @ColumnInfo(name = "show_results")
    var showResults: Int = 0,

    @PrimaryKey(autoGenerate = false)
    @ColumnInfo(name = "text_field_value")
    var textFieldValue: String = ""


)