package com.example.bearrecipebookapp.data.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "comments_table")
class CommentsEntity(

    @PrimaryKey(autoGenerate = false)
    @ColumnInfo(name = "comment_id")
    var commentID: String = "",

    @ColumnInfo(name = "recipe_name")
    val recipeName: String = "",

    @ColumnInfo(name = "author_id")
    var authorID: String = "",

    @ColumnInfo(name = "comment_text")
    var commentText: String = "",

    @ColumnInfo(name = "likes")
    var likes: Int = 0,

    @ColumnInfo(name = "liked_by_local_user")
    var likedByMe: Int = 0,

    @ColumnInfo(name = "local_user_like_was_synced")
    var myLikeWasSynced: Int = 0,

    @ColumnInfo(name ="timestamp")
    var timestamp: String = "",
)

