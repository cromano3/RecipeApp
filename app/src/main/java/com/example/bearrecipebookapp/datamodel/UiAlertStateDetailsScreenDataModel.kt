package com.example.bearrecipebookapp.datamodel

import com.example.bearrecipebookapp.data.entity.RecipeEntity

data class UiAlertStateDetailsScreenDataModel(
    val starCount: Int = 0,
    val showRemoveAlert: Boolean = false,
    val showCompletedAlert: Boolean = false,
    val showRatingAlert: Boolean = false,
    val showFavoriteAlert: Boolean = false,
    val showLeaveReviewAlert: Boolean = false,
    val isThumbDownSelected: Boolean = false,
    val isThumbUpSelected: Boolean = false,
//    val showWriteReviewAlert: Boolean = false,
//    val showReviewTextInputAlert: Boolean = false,
    val reviewText: String = "",
    val recipe: RecipeWithIngredientsAndInstructions = RecipeWithIngredientsAndInstructions(
        RecipeEntity(), listOf(), listOf()),
)

