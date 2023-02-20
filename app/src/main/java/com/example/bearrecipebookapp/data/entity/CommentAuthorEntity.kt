package com.example.bearrecipebookapp.data.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "comments_authors_table")
class CommentAuthorEntity (

    @PrimaryKey(autoGenerate = false)
    @ColumnInfo(name = "author_id")
    var authorID: String = "",

    @ColumnInfo(name = "author_name")
    var authorName: String = "",

    @ColumnInfo(name = "author_karma")
    var authorKarma: Int = 0,

    @ColumnInfo(name ="author_image_url")
    var authorImageURL: String = "",

    @ColumnInfo(name ="timestamp")
    var timestamp: String = "",


)