package com.christopherromano.culinarycompanion.datamodel

import androidx.room.Embedded
import androidx.room.Relation
import com.christopherromano.culinarycompanion.data.entity.CommentAuthorEntity
import com.christopherromano.culinarycompanion.data.entity.CommentsEntity


data class ReviewWithAuthorDataModel (

    @Embedded
    val commentsEntity: CommentsEntity,

    @Relation(
        parentColumn = "author_id",
        entityColumn = "author_id",
    )
    val authorEntity: CommentAuthorEntity,


    )
