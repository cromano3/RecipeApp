package com.example.bearrecipebookapp.data.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "user_table")
class UserEntity(

    @PrimaryKey(autoGenerate = false)
    @ColumnInfo(name = "user_pk")
    var userID: Int = 0,

    @ColumnInfo(name = "firestore_uid")
    var firestoreUid: String = "",

    @ColumnInfo(name = "exp_to_give")
    val expToGive: Int = 0,

    @ColumnInfo(name = "exp_total")
    var expTotal: Int = 0,

    @ColumnInfo(name = "show_tutorial")
    var showTutorial: String = "false",

    @ColumnInfo(name = "is_online_user_type")
    var isOnlineUser: Int = 0,

    @ColumnInfo(name = "user_image_url")
    var userImageUrl: String = "",

    @ColumnInfo(name = "user_nickname")
    var userNickname: String = "",

)