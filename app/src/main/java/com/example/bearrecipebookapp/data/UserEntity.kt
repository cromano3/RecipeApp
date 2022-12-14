package com.example.bearrecipebookapp.data

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "user_table")
class UserEntity(

    @PrimaryKey(autoGenerate = false)
    @ColumnInfo(name = "user_id")
    var userID: Int = 0,

    @ColumnInfo(name = "exp_to_give")
    val expToGive: Int = 0,

    @ColumnInfo(name = "exp_total")
    var expTotal: Int = 0,

)