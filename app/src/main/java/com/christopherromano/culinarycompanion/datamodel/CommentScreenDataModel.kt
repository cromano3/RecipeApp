package com.christopherromano.culinarycompanion.datamodel

data class CommentScreenDataModel (
    val reviewText: String = "",
    val showTooLongAlert: Boolean = false,
    val showPendingApprovalAlert: Boolean = false,
)