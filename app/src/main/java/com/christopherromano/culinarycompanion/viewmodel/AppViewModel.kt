package com.christopherromano.culinarycompanion.viewmodel

import android.app.Application
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.christopherromano.culinarycompanion.data.RecipeAppDatabase
import com.christopherromano.culinarycompanion.data.repository.AppRepository
import com.christopherromano.culinarycompanion.data.repository.FirebaseRepository
import com.christopherromano.culinarycompanion.datamodel.AppUiState
import com.christopherromano.culinarycompanion.datamodel.AuthorDataWithComment
import com.christopherromano.culinarycompanion.datamodel.RecipeNameAndRating
import com.christopherromano.culinarycompanion.datamodel.RecipeNameAndReview
import com.christopherromano.culinarycompanion.datamodel.RecipeWithIngredientsAndInstructions
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

        userSetup()

    }

    private fun userSetup(){
        viewModelScope.launch {


            val localOnlineUserType =  withContext(Dispatchers.IO) { repository.onlineUserType() }

            appUiState.update {
                it.copy(
                    userIsOnlineStatus = localOnlineUserType
                )
            }

            if(localOnlineUserType == 0){
                //they are offline user do nothing
                appUiState.update {
                    it.copy(
//                        showLoading = true,
                        endSplash = true,
                    )
                }
            }
            else if(localOnlineUserType == -2){
                //user should be asked if they want to sign in/up for online stuff

                appUiState.update {
                    it.copy(
                        showSignInButtons = true
                    )
                }
            }
            else if(localOnlineUserType == 1){
                val currentFirestoreUserNotNull = checkAuth()
                //they are already signed in
                if(currentFirestoreUserNotNull){
                    //back up upload sync should then try to upload any of their stuff that wasn't uploaded
                    appUiState.update {
                        it.copy(
//                            showLoading = true,
                            endSplash = true,
                        )
                    }
                    dataSyncUploads()

                }else{
                    //try google sign in/up
                    appUiState.update {
                        it.copy(
                            showLoading = true,
                        )
                    }
                    signIn()
                }
            }
        }



    }

    private fun checkAuth(): Boolean{
        return firebaseRepository.currentUser() != null
    }

    fun dontSignIn(){
        repository.setOnlineUserType(0)
        appUiState.update {
            it.copy(
                userIsOnlineStatus = 0,
                showSignInButtons = false,
                showLoading = true,
                endSplash = true,
            )
        }

    }

    fun signIn() {

        viewModelScope.launch {

            if (appUiState.value.userIsOnlineStatus == -2) {
                println("in new to this device")
                appUiState.update {
                    it.copy(
                        showSignInButtons = false,
                        showLoading = true,
                    )
                }
                googleOneTapSignInOrUp()

            }
            else if(appUiState.value.userIsOnlineStatus == 1){

                val isAuthed = firebaseRepository.currentUser()

                if(isAuthed != null){
                    println("before sync and is authed")
                    appUiState.update {
                        it.copy(
                            showSignInButtons = false,
                            showLoading = true,
                            endSplash = true,
                        )
                    }
                    dataSyncUploads()
                }
                else{
                    println("before sign IN and is NOT authed")
                    appUiState.update {
                        it.copy(
                            showSignInButtons = false,
                            showLoading = true,
                        )
                    }
                    googleOneTapSignInOrUp()
                }
            }
        }

    }

    private suspend fun googleOneTapSignInOrUp(){
        val result = firebaseRepository.googleOneTapSignInOrUp()
        if(result.result == "Success"){
            appUiState.update {
                it.copy(
                    googleSignInState = "Success",
                    googleSignInResult = result.signInResult
                )
            }
        }
        else{
            println("Sign in or up failed, ending splash.")
            appUiState.update {
                it.copy(
                    googleSignInState = "",
                    endSplash = true,
                )
            }

        }
    }



    fun endSplashOnFail(){
        println("ending splash: Auth Failed.")
        appUiState.update {
            it.copy(
                googleSignInState = "",
                endSplash = true,
            )
        }

    }
    fun firebaseSignInWithGoogle(googleCredential: AuthCredential) = viewModelScope.launch {
        println("in view firebase try")
        val firebaseSignInWithGoogleResponse = firebaseRepository.firebaseSignInWithGoogle(googleCredential)
        println("firebase sign in result is:$firebaseSignInWithGoogleResponse")

        when (firebaseSignInWithGoogleResponse) {
            "ReturningUserSuccess" -> {
                repository.setOnlineUserType(1)
                appUiState.update {
                    it.copy(
                        userIsOnlineStatus = 1,
                        googleSignInState = "",
                        endSplash = true,

                        )
                }
                dataSyncUploads()
            }
            "NewUserSuccess" -> {
                repository.setOnlineUserType(1)
                appUiState.update {
                    it.copy(
                        userIsOnlineStatus = 1,
                        googleSignInState = "",
                        endSplash = true,
                    )
                }
            }
            else -> {
                appUiState.update {
                    it.copy(
                        firebaseSignInResult = firebaseSignInWithGoogleResponse,
                        googleSignInState = "",
                        endSplash = true,
                    )
                }
            }
        }



    }


    private fun dataSyncUploads(){
        viewModelScope.launch {
            println("data sync UPLOADS coroutine start")

            val uid = firebaseRepository.getUid()

            ////Comments////

            val unsyncedUserComments = withContext(Dispatchers.IO) { repository.getUnsyncedUserComments() }

            println("before comments upload")
            //upload comments and mark as synced
            if(unsyncedUserComments.isNotEmpty()) {
                print("comments not empty")
                for (comment in unsyncedUserComments) {
                    //upload comments
                    val result = firebaseRepository.uploadComment(comment)
                    //mark as synced in local DB
                    if(result == "Success" || result == "User Comment Already Exists") {
                        withContext(Dispatchers.IO) { repository.markCommentAsSynced(comment) }
                    }
                }
            }

            ////Ratings////

            val unsyncedUserRatings = withContext(Dispatchers.IO) { repository.getUnsyncedUserRatings() }

            println("before ratings upload")
            //upload ratings and mark as synced
            if(unsyncedUserRatings.isNotEmpty()) {
                println("ratings not empty")
                for (rating in unsyncedUserRatings) {
                    val successStatus = firebaseRepository.updateRating(rating)
                    if(successStatus == "Success" || successStatus == "Failed Duplicate Rating") {
                        println("rating successfully updated")
                        withContext(Dispatchers.IO) { repository.markRatingAsSynced(rating) }
                    }
                }
            }

        }
    }



    fun signInWithGoogle(){
        coroutineScope.launch {
            googleOneTapSignInOrUp()
        }
    }

    fun setupDetailsScreen(recipeName: String){
        val recipeData = repository.getRecipeWithIngredientsAndInstructions(recipeName)
        appUiState.update {
            it.copy(
                detailsScreenTarget = recipeData,
            )
        }
    }



    fun storeRating(rating: Int){

        viewModelScope.launch {

            val result = firebaseRepository.updateRating(RecipeNameAndRating(appUiState.value.detailsScreenTarget.recipeEntity.recipeName, rating))

            if(result == "Success" || result == "Failed Duplicate Rating"){
                withContext(Dispatchers.IO) {
                    repository.setUserRating(
                        appUiState.value.detailsScreenTarget.recipeEntity.recipeName,
                        rating,
                        1
                    )
                }
            }
            else{
                withContext(Dispatchers.IO) {
                    repository.setUserRating(
                        appUiState.value.detailsScreenTarget.recipeEntity.recipeName,
                        rating,
                        0
                    )
                }
            }

        }

    }

    fun markAsRated(){
        appUiState.update {
            it.copy(detailsScreenTarget = it.detailsScreenTarget.copy(recipeEntity = it.detailsScreenTarget.recipeEntity.copy(isRated = 1)))
        }
    }

    fun markAsReviewed(){
        appUiState.update {
            it.copy(detailsScreenTarget = it.detailsScreenTarget.copy(recipeEntity = it.detailsScreenTarget.recipeEntity.copy(isReviewed = 1)))
        }
    }

    fun markReviewAsSynced(){
        appUiState.update {
            it.copy(detailsScreenTarget = it.detailsScreenTarget.copy(recipeEntity = it.detailsScreenTarget.recipeEntity.copy(isReviewSynced = 1)))
        }
    }

    fun addedToFavoriteUiUpdate(){
        appUiState.update {
            it.copy(detailsScreenTarget = it.detailsScreenTarget.copy(recipeEntity = it.detailsScreenTarget.recipeEntity.copy(isFavorite = 1)))
        }
    }

    fun storeReview(recipeName: String, reviewText: String) {
        viewModelScope.launch {

            when (firebaseRepository.uploadComment(RecipeNameAndReview(recipeName, reviewText))) {
                "Success" -> {
                    withContext(Dispatchers.IO) {
                        repository.setReview(recipeName, reviewText)
                    }
                }
                "User Comment Already Exists" -> {
                    withContext(Dispatchers.IO) {
                        repository.setReviewIsSynced(recipeName)
                    }
                }
                else -> {
                    withContext(Dispatchers.IO) {
                        repository.setReviewAsUnsynced(recipeName, reviewText)
                    }
                }
            }

        }
    }

    fun deleteReview(commentID: String, recipeName: String) {
        viewModelScope.launch {
            val result = firebaseRepository.deleteComment(commentID)
            println("delete review result is: $result")
        }
    }

    fun updateLikes(commentID: String) {
        coroutineScope.launch(Dispatchers.IO) {
            val successStatus = firebaseRepository.updateLike(commentID)
            println("finished update likes with result: $successStatus")
        }
    }

    fun updateDislikes(commentID: String){
        coroutineScope.launch(Dispatchers.IO) {
            val successStatus = firebaseRepository.updateDislike(commentID)
            println("finished update dislikes with result: $successStatus")
        }
    }


    fun setupReviewScreen(recipeName: String){
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

    fun submitReport(authorDataWithComment: AuthorDataWithComment) {
        coroutineScope.launch (Dispatchers.IO){
            firebaseRepository.submitReport(authorDataWithComment)
        }
    }

}