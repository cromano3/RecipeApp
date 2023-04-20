package com.christopherromano.culinarycompanion.datamodel

import com.google.android.gms.auth.api.identity.BeginSignInResult

data class AppUiState(
    val detailsScreenTarget: RecipeWithIngredientsAndInstructions = RecipeWithIngredientsAndInstructions(),
    val reviewScreenTarget: RecipeWithIngredientsAndInstructions = RecipeWithIngredientsAndInstructions(),
    val userIsOnlineStatus: Int = 0,
    val userId: String = "",
    val showSignInButtons: Boolean = false,
    val showLoading: Boolean = false,
    val endSplash: Boolean = false,
    val googleSignInState: String = "",
    val googleSignInResult: BeginSignInResult? = null,
    val firebaseSignInResult: String = "",
    val detailsScreenLocalUserReview: String = "",

    )
