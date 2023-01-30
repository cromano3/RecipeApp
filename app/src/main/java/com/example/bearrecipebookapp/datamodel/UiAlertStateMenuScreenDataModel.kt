package com.example.bearrecipebookapp.datamodel

import com.example.bearrecipebookapp.data.entity.RecipeEntity

data class UiAlertStateMenuScreenDataModel(
    val showRemoveAlert: Boolean = false,
    val showCompletedAlert: Boolean = false,
    val showAddRecipeAlert: Boolean = false,
    val showRatingAlert: Boolean = false,
    val showFavoriteAlert: Boolean = false,
    val showLeaveReviewAlert: Boolean = false,
    val isThumbUpSelected: Boolean = false,
    val isThumbDownSelected: Boolean = false,
    val recipe: RecipeWithIngredientsAndInstructions = RecipeWithIngredientsAndInstructions(RecipeEntity(), listOf(), listOf()),
)

