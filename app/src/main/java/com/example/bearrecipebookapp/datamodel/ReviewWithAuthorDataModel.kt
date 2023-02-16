package com.example.bearrecipebookapp.datamodel

import androidx.room.Embedded
import androidx.room.Relation
import com.example.bearrecipebookapp.data.entity.CommentAuthorEntity
import com.example.bearrecipebookapp.data.entity.CommentsEntity

data class ReviewWithAuthorDataModel (

    @Embedded
    val commentsEntity: CommentsEntity,

    @Relation(
        parentColumn = "author_id",
        entityColumn = "author_id",
    )
    val authorEntity: CommentAuthorEntity,


)
