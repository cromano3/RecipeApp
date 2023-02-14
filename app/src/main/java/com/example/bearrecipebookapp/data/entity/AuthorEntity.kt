package com.example.bearrecipebookapp.data.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "comments_authors_table")
class AuthorEntity (

    @PrimaryKey(autoGenerate = false)
    @ColumnInfo(name = "author_id")
    var authorID: Int = 0,

    @ColumnInfo(name = "author_title")
    val authorTitle: String = "",

    @ColumnInfo(name = "author_id")
    var authorKarma: Int = 0,



)