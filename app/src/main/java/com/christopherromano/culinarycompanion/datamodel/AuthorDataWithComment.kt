package com.christopherromano.culinarycompanion.datamodel


import com.christopherromano.culinarycompanion.data.entity.CommentsEntity


data class AuthorDataWithComment(
    val authorData: AuthorData,
    val comment: CommentsEntity
)

