package com.example.bearrecipebookapp.datamodel

data class FirestoreCommentDataModel(
    val commentId: String,
    val recipeName: String,
    val reviewText: String,
    val authorUid: String,
    val likes: Int,
)
