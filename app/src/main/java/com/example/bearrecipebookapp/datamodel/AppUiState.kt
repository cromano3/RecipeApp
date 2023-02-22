package com.example.bearrecipebookapp.datamodel

import com.google.android.gms.auth.api.identity.BeginSignInResult

data class AppUiState(
    val detailsScreenTarget: RecipeWithIngredientsAndInstructions = RecipeWithIngredientsAndInstructions(),
    val reviewScreenTarget: RecipeWithIngredientsAndInstructions = RecipeWithIngredientsAndInstructions(),
    val userIsOnlineStatus: Int = 0,
    val userId: String = "",
//    val userImageURL: String = "",
//    val userNickname: String = "",
    val showSignInAlert: Boolean = false,
    val showLoadingAlert: Boolean = false,
    val googleSignInState: String = "",
    val googleSignInResult: BeginSignInResult? = null,
    val firebaseSignInResult: String = "",
    val detailsScreenReviewsData: List<ReviewWithAuthorDataModel> = listOf(),
    val detailsScreenLocalUserReview: String = "",

    )
