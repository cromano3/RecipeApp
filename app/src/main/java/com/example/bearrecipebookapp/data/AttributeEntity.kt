package com.example.bearrecipebookapp.data

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "attributes_table")
class AttributeEntity(
    @PrimaryKey(autoGenerate = false)
    @ColumnInfo(name = "attribute_name")
    val recipeName: String = "",
)
