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

//        signIn(application)

        userSetup()


    }

    private fun userSetup(){
        viewModelScope.launch {

            appUiState.update {
                it.copy(
                    showLoadingAlert = true
                )
            }

            val currentFirestoreUserNotNull = checkAuth()
            val localOnlineUserType =  repository.onlineUserType()

            if(localOnlineUserType == 0){
                //they are offline user do nothing

            }
            else if(localOnlineUserType == -2 || localOnlineUserType == -1){
                //user should be asked if they want to sign in/up for online stuff
                appUiState.update {
                    it.copy(
                        showLoadingAlert = false,
                        showSignInAlert = true
                    )
                }

                //if no set to =+ 1

                //if yes try google sign in/up

                    //if successful set to 1

                    //if failed set to 0 and error log, NEED ANON USERS FOR THIS ERROR LOG
            }
            else if(localOnlineUserType == 1){

                //they are already signed in
                if(currentFirestoreUserNotNull){
                    //back up upload sync should then try to upload any of their stuff that wasn't uploaded

                }else{
                    //try google sign in/up

                    //if success then back up upload sync should then try to upload any of their stuff that wasn't uploaded

                }




            }
        }



    }

    private fun checkAuth(): Boolean{
        return firebaseRepository.currentUser() != null
    }

    private fun signIn(application: Application){
        /** Check DB, if online user == -2 or -1, then pop to ask if they want to sign-in,
         * if 0 then they are always offline user
         * if 1 then they are online user and we do auto sign-in
         * set view model online mode
         */


        viewModelScope.launch {


            val onlineUserType = withContext(Dispatchers.IO) { repository.onlineUserType() }

            println("User type: $onlineUserType")

            appUiState.update {
                it.copy(
                    userIsOnlineStatus = onlineUserType
                )
            }

            if (appUiState.value.userIsOnlineStatus == -2) {
                println("in new to this device")
                googleOneTapSignInOrUp()

            }
            else if(appUiState.value.userIsOnlineStatus == 1){

                val isAuthed = firebaseRepository.currentUser()

                if(isAuthed != null){
                    println("before sync and is authed")
                    dataSync()
                }
                else{
                    println("before sign IN and is NOT authed")
                    googleOneTapSignIn()
                }
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
        val result = firebaseRepository.googleOneTapSignInOrUp()
        if(result.result == "Success"){
            appUiState.update {
                it.copy(
                    googleSignInState = "Success",
                    googleSignInResult = result.signInResult
                )
            }
        }
    }

    private suspend fun googleOneTapSignIn(){
        val result = firebaseRepository.googleOneTapSignIn()
        if(result.result == "Success"){
            appUiState.update {
                it.copy(
                    googleSignInState = "Success",
                    googleSignInResult = result.signInResult
                )
            }
        }
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

        //user is new and needs to wait timeout for data sync
        /**SET timeout start point here*/
        if(firebaseSignInWithGoogleResponse == "NewUserSuccess" && appUiState.value.userIsOnlineStatus == -2){
            //set user info in local DB (name and image for now)
            /**a similar function to this will eventually be used to sync user karma, but not in this exact location/call spot.*/
            setupUser()
        }
        //user is new to THIS DEVICE but not our project, they need to wait for timeout for data sync as well
        /**SET timeout start point here*/
        else if(firebaseSignInWithGoogleResponse == "ReturningUserSuccess" && appUiState.value.userIsOnlineStatus == -2){
            println("data sync from sign IN")
            setupUser()
        //user was successfully logged in and is not new to this device they also need to wait for timeout
            /**check for time out here, if passed do sync*/
        }else if(firebaseSignInWithGoogleResponse == "NewUserSuccess" || firebaseSignInWithGoogleResponse == "ReturningUserSuccess"){
            dataSync()
        }


    }

    private fun getUserIdFromRoom(): String{
        return repository.getUserId()
    }

    private suspend fun setupUser(){


        withContext(Dispatchers.IO) { repository.setOnlineUserType(1) }
        appUiState.update {
            it.copy(
                userIsOnlineStatus = 1
            )
        }

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

//            val userData = firebaseRepository.getUserData(uid)

            //try set user image URL
//            if(userData.userPhotoURL == ""){
//                println("failed to retrieve user image URL")
//            }
//            else{
//                withContext(Dispatchers.IO) { repository.setUserImageURL(userData.userPhotoURL) }
//                appUiState.update {
//                    it.copy(
//                        userImageURL = userData.userPhotoURL
//                    )
//                }
//            }
//
//            //try set user nickname
//            if(userData.userPhotoURL == ""){
//                println("failed to retrieve user nickname")
//            }
//            else{
//                withContext(Dispatchers.IO) { repository.setUserNickname(userData.userName) }
//                appUiState.update {
//                    it.copy(
//                        userNickname = userData.userName
//                    )
//                }
//            }

        }



    }


    private fun dataSync(){
        viewModelScope.launch {
            println("data sync coroutine start")

            setupUser()


//            val uid = withContext(Dispatchers.IO) { getUserIdFromRoom() }
//            appUiState.update { it.copy(userId = uid) }
//
//            println("uid is $uid")

            val uid = firebaseRepository.getUid()

            ////Comments////

            val unsyncedUserComments = withContext(Dispatchers.IO) { repository.getUnsyncedUserComments() }

            println("before comments upload")
            //upload comments and mark as synced
            if(unsyncedUserComments.isNotEmpty()) {
                print("comments not empty")
                for (comment in unsyncedUserComments) {
                    //upload comments
                    firebaseRepository.uploadComment(comment, uid)
                    //mark as synced in local DB
                    withContext(Dispatchers.IO) { repository.markCommentAsSynced(comment) }
                }
            }

            ////Ratings////

            val unsyncedUserRatings = withContext(Dispatchers.IO) { repository.getUnsyncedUserRatings() }

            println("before ratings upload")
            //upload ratings and mark as synced
            if(unsyncedUserRatings.isNotEmpty()) {
                println("ratings not empty")
                for (rating in unsyncedUserRatings) {
                    val successStatus = firebaseRepository.updateRating(rating, uid)
                    if(successStatus == "Success") {
                        println("rating successfully updated")
                        withContext(Dispatchers.IO) { repository.markRatingAsSynced(rating) }
                    }
                }
            }


            ////Likes////

            val unsyncedUserLikes = withContext(Dispatchers.IO) { repository.getUnsyncedUserLikes() }

            println("before new likes upload")
            if(unsyncedUserLikes.isNotEmpty()){
                println("new likes not empty")
                for(likeId in unsyncedUserLikes){
                    val successStatus = firebaseRepository.updateLike(likeId)
                    if(successStatus == "Success") {
                        withContext(Dispatchers.IO) { repository.markLikeAsSynced(likeId) }
                    }
                }
            }


            ////Do Download Sync////

            //get comments from remote
            /**this needs to be: "get comments that are timestamped (at least 2-5 mins.) after my last sync completion timestamp
             * this way we dont get all the comments that the local db already has over and over again each time we sync data.
             * the local DB needs to store a timestamp (of the same timezone as the firestore) when the data sync is completed to use
             * to compare this value.
             */
            val commentsFromFirestore = firebaseRepository.getComments()

            println("before comments DOWNLOAD")
            //add comments and update likes in local db
            if(commentsFromFirestore.isNotEmpty()){
                println("new comments not empty")
                for(comment in commentsFromFirestore){
                    println("do comment sync (authors)")
                    val authorData = firebaseRepository.getAuthorData(comment)
                    withContext(Dispatchers.IO) {repository.addAuthor(authorData, comment.authorID)}

                }
                for(comment in commentsFromFirestore){
                    println("do comment sync (comments)")
//                    repository.updateLikes(comment)
                    withContext(Dispatchers.IO) { repository.addComment(comment) }

                }
            }


            //get ratings from remote
            /**needs similar timing functionality as above*/
            val recipeRatingsFromFirestore = firebaseRepository.getRecipeRatings()

            println("before download ratings")
            //update ratings in local db
            if(recipeRatingsFromFirestore.isNotEmpty()){
                println("new ratings not empty")
                for(recipe in recipeRatingsFromFirestore){
                    println("do ratings update")
                    withContext(Dispatchers.IO) { repository.updateRecipeRating(recipe) }
                }
            }



        }

    }


    fun setupDetailsScreen(recipeName: String){
        val recipeData = repository.getRecipeWithIngredientsAndInstructions(recipeName)
        val reviewsData = repository.getReviewsData(recipeName)
        val localUserReview = repository.getLocalUserReviewData(recipeName) ?: ""
        appUiState.update {
            it.copy(
                detailsScreenTarget = recipeData,
                detailsScreenReviewsData = reviewsData,
                detailsScreenLocalUserReview = localUserReview,
            )
        }
    }

    fun updateDetailsScreenWithJustWrittenReview(){
        val localUserReview = repository.getLocalUserReviewData(appUiState.value.detailsScreenTarget.recipeEntity.recipeName) ?: ""
        appUiState.update {
            it.copy(
                detailsScreenLocalUserReview = localUserReview,
            )
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