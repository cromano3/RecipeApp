package com.example.bearrecipebookapp.data

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "filters_table")
class FilterEntity(
    @PrimaryKey(autoGenerate = false)
    @ColumnInfo(name = "filter_name")
    val filterName: String = "",

    @ColumnInfo(name = "is_active_filter")
    val isActiveFilter: Int = 0
)
