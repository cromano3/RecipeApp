package com.example.bearrecipebookapp.datamodel

import com.example.bearrecipebookapp.data.entity.CommentsEntity

data class AuthorDataWithComment(
    val authorData: AuthorData,
    val comment: CommentsEntity
)

