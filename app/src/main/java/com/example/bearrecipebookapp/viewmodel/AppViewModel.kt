package com.example.bearrecipebookapp.viewmodel

import android.app.Application
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.bearrecipebookapp.data.RecipeAppDatabase
import com.example.bearrecipebookapp.data.repository.AppRepository
import com.example.bearrecipebookapp.data.repository.FirebaseRepository
import com.example.bearrecipebookapp.datamodel.AppUiState
import com.example.bearrecipebookapp.datamodel.RecipeWithIngredientsAndInstructions
import com.google.android.gms.auth.api.identity.Identity
import com.google.android.gms.auth.api.identity.SignInClient
import com.google.firebase.auth.AuthCredential
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class AppViewModel(application: Application, private val firebaseRepository: FirebaseRepository): ViewModel() {

    private val repository: AppRepository
    private val coroutineScope = CoroutineScope(Dispatchers.IO)

    var oneTapClient: SignInClient = Identity.getSignInClient(application)


    val appUiState = MutableStateFlow(AppUiState())


    init {
        val appDb = RecipeAppDatabase.getInstance(application)
        val appDao = appDb.AppDao()
        repository = AppRepository(appDao)

        signIn(application)


    }

    private fun signIn(application: Application){
        /** Check DB, if online user == -2 or -1, then pop to ask if they want to sign-in,
         * if 0 then they are always offline user
         * if 1 then they are online user and we do auto sign-in
         * set view model online mode
         */


        viewModelScope.launch {

            println("1")
            val isNew = withContext(Dispatchers.IO) { repository.isNewUser() }
            println("4")
            println(isNew)
            appUiState.update {
                it.copy(
                    userIsOnlineStatus = isNew
                )
            }

            if (appUiState.value.userIsOnlineStatus == -2) {
                println("in new")
                googleOneTapSignInOrUp()

            }
        }




            //


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

    private suspend fun googleOneTapSignInOrUp(){
//        viewModelScope.launch {
            val result = firebaseRepository.googleOneTapSignInOrUp()
            if(result.result == "Success"){
                appUiState.update {
                    it.copy(
                        googleSignInState = "Success",
                        googleSignInResult = result.signInResult
                    )
                }
            }

//        }

    }

    fun firebaseSignInWithGoogle(googleCredential: AuthCredential) = viewModelScope.launch {
        println("in view firebase try")
        val firebaseSignInWithGoogleResponse = firebaseRepository.firebaseSignInWithGoogle(googleCredential)
        println(firebaseSignInWithGoogleResponse)
        appUiState.update {
            it.copy(
                firebaseSignInResult = firebaseSignInWithGoogleResponse
            )
        }

        if(firebaseSignInWithGoogleResponse == "Success" && appUiState.value.userIsOnlineStatus == -2){
            //set uid in local DB
            setNewUserId()
        }
        else if(firebaseSignInWithGoogleResponse == "Success"){
            dataSync()
        }


    }

    private suspend fun getUserIdFromRoom(): String{
        return repository.getUserId

    }

    private suspend fun setNewUserId(){

        val uid = firebaseRepository.getUid()
        if (uid == ""){
            println("failed to retrieve UID of new user")
        }
        else{
            withContext(Dispatchers.IO) { repository.setUid(uid) }
            appUiState.update {
                it.copy(
                    userId = uid
                )
            }
        }

    }


    private fun dataSync(){
        viewModelScope.launch {

            val uid = withContext(Dispatchers.IO) { getUserIdFromRoom() }

            //get local user comments from Room DB
            val unsyncedUserComments = withContext(Dispatchers.IO) { repository.getUnsyncedUserComments() }
            //if there are new comments
            if(unsyncedUserComments.isNotEmpty()) {

                //for each comment upload and then mark as synced in local DB
                for (comment in unsyncedUserComments) {
                    //upload comments
                    firebaseRepository.uploadComment(comment)
                    //mark as synced in local DB
                    withContext(Dispatchers.IO) { repository.markCommentAsSynced(comment) }


                }


            }



        }


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