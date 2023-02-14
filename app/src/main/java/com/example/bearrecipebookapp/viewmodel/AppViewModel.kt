package com.example.bearrecipebookapp.viewmodel

import android.app.Application
import androidx.lifecycle.ViewModel
import com.example.bearrecipebookapp.data.RecipeAppDatabase
import com.example.bearrecipebookapp.data.repository.AppRepository
import com.example.bearrecipebookapp.datamodel.AppUiState
import com.example.bearrecipebookapp.datamodel.RecipeWithIngredientsAndInstructions
import com.google.android.gms.auth.api.identity.BeginSignInRequest
import com.google.android.gms.auth.api.identity.Identity
import com.google.android.gms.auth.api.identity.SignInClient
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.update

class AppViewModel (application: Application): ViewModel() {

    private val repository: AppRepository
    private val coroutineScope = CoroutineScope(Dispatchers.IO)

    private lateinit var oneTapClient: SignInClient
    private lateinit var signInRequest: BeginSignInRequest

    val appUiState = MutableStateFlow(AppUiState())


    init {
        val appDb = RecipeAppDatabase.getInstance(application)
        val appDao = appDb.AppDao()
        repository = AppRepository(appDao)

        /** Check DB, if online user == -2 or -1, then pop to ask if they want to sign-in,
         * if 0 then they are always offline user
         * if 1 then they are online user and we do auto sign-in
         * set view model online mode
         */

        oneTapClient = Identity.getSignInClient(application)
        signInRequest = BeginSignInRequest.builder()
            .setGoogleIdTokenRequestOptions(
                BeginSignInRequest.GoogleIdTokenRequestOptions.builder()
                    .setSupported(true)
                    //release client ID
//                  135954228549-olr3lptrcqu9h7jibf6lain0c6mm5mu9.apps.googleusercontent.com
                    //debug client ID
//                  135954228549-eng79jiknsvseb2osm2uvekrh8jj0c9p.apps.googleusercontent.com
                    .setServerClientId("135954228549-eng79jiknsvseb2osm2uvekrh8jj0c9p.apps.googleusercontent.com")
                    // Only show accounts previously used to sign in.
                    .setFilterByAuthorizedAccounts(true)
                    .build())
            .build()




        //do sign in

        /** if signed in and timestamp is ok, DO DATA SYNC*/

        //check if device is connected to internet

        //get data from local tables to upload

        /**for each data type: comment, likes, ratings DO: */

        //map data to firestore data types

        //loop through and send them in a transaction to firestore

        //if success mark local db entries as synced else don't update them

        /**end first loop, repeat for each data type (one transaction for each) */

        /**then do DOWNLOAD sync operation*/

        //check connectivity again




    }


    suspend fun setupDetailsScreen(recipeName: String){
        val result = repository.getRecipeWithIngredientsAndInstructions(recipeName)
        appUiState.update {
            it.copy(detailsScreenTarget = result)
        }
    }

    suspend fun setupReviewScreen(recipeName: String){
        val result = repository.getRecipeWithIngredientsAndInstructions(recipeName)
        appUiState.update {
            it.copy(reviewScreenTarget = result)
        }
    }

    fun toggleDetailsScreenUiFavorite(){

        val newRecipe = appUiState.value.detailsScreenTarget.recipeEntity.copy(isFavorite = if(appUiState.value.detailsScreenTarget.recipeEntity.isFavorite == 1) 0 else 1)

        appUiState.update {
            it.copy(detailsScreenTarget = RecipeWithIngredientsAndInstructions(
                newRecipe,
                appUiState.value.detailsScreenTarget.ingredientsList,
                appUiState.value.detailsScreenTarget.instructionsList
            ))
        }
    }

    fun updateDetailsScreenUiOnMenuStatus(){

        val newRecipe = appUiState.value.detailsScreenTarget.recipeEntity.copy(onMenu = if(appUiState.value.detailsScreenTarget.recipeEntity.onMenu == 1) 0 else 1)

        appUiState.update {
            it.copy(detailsScreenTarget = RecipeWithIngredientsAndInstructions(
                newRecipe,
                appUiState.value.detailsScreenTarget.ingredientsList,
                appUiState.value.detailsScreenTarget.instructionsList
            ))
        }
    }

}