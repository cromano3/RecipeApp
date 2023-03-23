package com.christopherromano.culinarycompanion.data.entity

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

    @ColumnInfo(name = "disliked_by_local_user")
    var dislikedByMe: Int = 0,

)

