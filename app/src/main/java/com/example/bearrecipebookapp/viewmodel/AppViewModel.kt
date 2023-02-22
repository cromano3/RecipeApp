package com.example.bearrecipebookapp.viewmodel

import android.app.Application
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.bearrecipebookapp.data.RecipeAppDatabase
import com.example.bearrecipebookapp.data.entity.CommentAuthorEntity
import com.example.bearrecipebookapp.data.entity.CommentsEntity
import com.example.bearrecipebookapp.data.repository.AppRepository
import com.example.bearrecipebookapp.data.repository.FirebaseRepository
import com.example.bearrecipebookapp.datamodel.AppUiState
import com.example.bearrecipebookapp.datamodel.RecipeNameAndRating
import com.example.bearrecipebookapp.datamodel.RecipeWithIngredientsAndInstructions
import com.example.bearrecipebookapp.datamodel.ReviewWithAuthorDataModel
import com.google.android.gms.auth.api.identity.Identity
import com.google.android.gms.auth.api.identity.SignInClient
import com.google.firebase.auth.AuthCredential
import kotlinx.coroutines.*
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.update
import java.text.SimpleDateFormat
import java.util.*

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

//            appUiState.update {
//                it.copy(
//                    showLoadingAlert = true
//                )
//            }

            val currentFirestoreUserNotNull = checkAuth()
            val localOnlineUserType =  withContext(Dispatchers.IO) { repository.onlineUserType() }

            appUiState.update {
                it.copy(
                    userIsOnlineStatus = localOnlineUserType
                )
            }

            if(localOnlineUserType == 0){
                //they are offline user do nothing
            }
            else if(localOnlineUserType == -2){
                //user should be asked if they want to sign in/up for online stuff

                /**if failed set to 0 and error log, NEED ANON USERS FOR THIS ERROR LOG*/

                appUiState.update {
                    it.copy(
                        showLoadingAlert = false,
                        showSignInAlert = true
                    )
                }
            }
            else if(localOnlineUserType == 1){
                //they are already signed in
                if(currentFirestoreUserNotNull){
                    //back up upload sync should then try to upload any of their stuff that wasn't uploaded
                    dataSyncUploads()
                }else{
                    //try google sign in/up
                    signIn()
                }
            }
        }



    }

    private fun checkAuth(): Boolean{
        return firebaseRepository.currentUser() != null
    }

    private fun signIn() {

        viewModelScope.launch {

            if (appUiState.value.userIsOnlineStatus == -2) {
                println("in new to this device")
                googleOneTapSignInOrUp()

            }
            else if(appUiState.value.userIsOnlineStatus == 1){

                val isAuthed = firebaseRepository.currentUser()

                if(isAuthed != null){
                    println("before sync and is authed")
                    dataSyncUploads()
                }
                else{
                    println("before sign IN and is NOT authed")
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

        if(firebaseSignInWithGoogleResponse == "NewUserSuccess" && appUiState.value.userIsOnlineStatus == -2){
            repository.setOnlineUserType(1)
            appUiState.update {
                it.copy(
                    userIsOnlineStatus = 1
                )
            }
        }
        else if(firebaseSignInWithGoogleResponse == "ReturningUserSuccess" && appUiState.value.userIsOnlineStatus == -2){
            repository.setOnlineUserType(1)
            appUiState.update {
                it.copy(
                    userIsOnlineStatus = 1
                )
            }
        }else if((firebaseSignInWithGoogleResponse == "NewUserSuccess" && appUiState.value.userIsOnlineStatus == 1)
            || (firebaseSignInWithGoogleResponse == "ReturningUserSuccess" && appUiState.value.userIsOnlineStatus == 1)){
            dataSyncUploads()
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
                    val successStatus = firebaseRepository.updateRating(rating)
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

        }
    }

    private fun dataSync(){
        viewModelScope.launch {
            println("data sync coroutine start")

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
                    val successStatus = firebaseRepository.updateRating(rating)
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

//            //get comments from remote
//            /**this needs to be: "get comments that are timestamped (at least 2-5 mins.) after my last sync completion timestamp
//             * this way we dont get all the comments that the local db already has over and over again each time we sync data.
//             * the local DB needs to store a timestamp (of the same timezone as the firestore) when the data sync is completed to use
//             * to compare this value.
//             */
//            val commentsFromFirestore = firebaseRepository.getComments()
//
//            println("before comments DOWNLOAD")
//            //add comments and update likes in local db
//            if(commentsFromFirestore.isNotEmpty()){
//                println("new comments not empty")
//                for(comment in commentsFromFirestore){
//                    println("do comment sync (authors)")
//                    val authorData = firebaseRepository.getAuthorData(comment)
//                    withContext(Dispatchers.IO) {repository.addAuthor(authorData, comment.authorID)}
//
//                }
//                for(comment in commentsFromFirestore){
//                    println("do comment sync (comments)")
////                    repository.updateLikes(comment)
//                    withContext(Dispatchers.IO) { repository.addComment(comment) }
//
//                }
//            }


//            //get ratings from remote
//            /**needs similar timing functionality as above*/
//            val recipeRatingsFromFirestore = firebaseRepository.getRecipeRatings()
//
//            println("before download ratings")
//            //update ratings in local db
//            if(recipeRatingsFromFirestore.isNotEmpty()){
//                println("new ratings not empty")
//                for(recipe in recipeRatingsFromFirestore){
//                    println("do ratings update")
//                    withContext(Dispatchers.IO) { repository.updateRecipeRating(recipe) }
//                }
//            }



        }

    }

    fun confirmSignInWithGoogle(){
        //if yes try google sign in/up
        signIn()

        appUiState.update {
            it.copy(
                showSignInAlert = false,
            )
        }
    }

    fun dismissSignInWithGoogle(){
        if(appUiState.value.userIsOnlineStatus == -2) {

            repository.setOnlineUserType(-1)

            appUiState.update {
                it.copy(
                    showSignInAlert = false,
                    userIsOnlineStatus = -1
                )
            }

        }
        else{
            appUiState.update {
                it.copy(
                    showSignInAlert = false,
                )
            }
        }


    }

    fun setupDetailsScreen(recipeName: String){
        val recipeData = repository.getRecipeWithIngredientsAndInstructions(recipeName)
        appUiState.update {
            it.copy(
                detailsScreenTarget = recipeData,
                detailsScreenReviewsData = listOf(),
            )
        }
    }

    suspend fun setupDetailsScreenComments(recipeName: String){

        val recipeData = repository.getRecipe(recipeName)
        val reviewsData = repository.getReviewsData(recipeName) as MutableList

        var newCommentsList: MutableList<CommentsEntity> = mutableListOf()

        val formatter = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'", Locale.US)
        println("a")

        val lastCommentSyncTime = recipeData.lastCommentSyncTime
        val lastDownloadedCommentTimestamp = recipeData.lastDownloadedCommentTimestamp
//        val lastRatingSyncTime = recipeData.lastRatingSyncTime

//        val lastDownloadedCommentTimestampFormatted = formatter.parse(lastDownloadedCommentTimestamp)
//        val lastDownloadedRatingTimestampFormatted = formatter.parse(lastDownloadedRatingTimestamp)



        val oneHourAfterLastUpdate = Calendar.getInstance()

        if (lastCommentSyncTime == "x") {
            println("lastCommentSyncTime != empty ")
            delay(5000)

            val lastCommentSyncTimeFormatted = formatter.parse(lastCommentSyncTime)

            if (lastCommentSyncTimeFormatted != null) {

                oneHourAfterLastUpdate.time = lastCommentSyncTimeFormatted
                oneHourAfterLastUpdate.add(Calendar.HOUR, 1)

                val currentFirestoreTime = formatter.parse(firebaseRepository.getCurrentTime())
                val currentTime = Calendar.getInstance()

                if (currentFirestoreTime != null) {

                    currentTime.time = currentFirestoreTime

                    if(oneHourAfterLastUpdate > currentTime){

                        //we set this here to prevent abuse
                        val currentTimeMarker = firebaseRepository.getCurrentTime()
                        withContext(Dispatchers.IO) { repository.setTimeOfLastUpdate(recipeName, currentTimeMarker) }

                        //do download
                        newCommentsList = firebaseRepository.getComments(lastDownloadedCommentTimestamp)
                    }

                }

            }
        }
        else
//            if(lastRatingSyncTime == "")
            {
                println("lastCommentSyncTime == empty")
                println("b")

                //we set this here to prevent abuse
                val currentTimeMarker = firebaseRepository.getCurrentTime()
                withContext(Dispatchers.IO) { repository.setTimeOfLastUpdate(recipeName, currentTimeMarker) }

                println("c")
                delay(5000)
                println("d")
            //do download
            newCommentsList = firebaseRepository.getComments()
            }

        if(newCommentsList.isNotEmpty()) {

            val authorsDataWithComments = firebaseRepository.getAuthorsData(newCommentsList)

            var newMostRecentCommentTimestamp = authorsDataWithComments[0].comment.timestamp

            for(comment in authorsDataWithComments){
                /**should update this for karma system*/
                withContext(Dispatchers.IO) { repository.addAuthor(comment.authorData, comment.comment.authorID) }
                withContext(Dispatchers.IO) { repository.addComment(comment.comment) }
                reviewsData.add(
                    ReviewWithAuthorDataModel(
                        comment.comment,
                        CommentAuthorEntity(
                            comment.comment.authorID,
                            comment.authorData.userName,
                            0,
                            comment.authorData.userPhotoURL
                        )
                    )
                )

                val newMostRecentCommentTimestampAsDate = formatter.parse(newMostRecentCommentTimestamp)
                val thisCommentsTimestampAsDate = formatter.parse(comment.comment.timestamp)


                if(thisCommentsTimestampAsDate != null && newMostRecentCommentTimestampAsDate != null) {
                    if (thisCommentsTimestampAsDate > newMostRecentCommentTimestampAsDate) {
                        newMostRecentCommentTimestamp = comment.comment.timestamp
                    }
                }

            }
            withContext(Dispatchers.IO) { repository.setMostRecentCommentTimestamp(recipeName, newMostRecentCommentTimestamp) }

        }

        appUiState.update {
            it.copy(
                detailsScreenReviewsData = reviewsData,
            )
        }

    }

    fun storeRating(rating: Int){

        viewModelScope.launch {

            val result = firebaseRepository.updateRating(RecipeNameAndRating(appUiState.value.detailsScreenTarget.recipeEntity.recipeName, rating))

            if(result == "Success"){
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

    fun updateLikes(commentID: String) {

        val myList: MutableList<ReviewWithAuthorDataModel> = mutableListOf()

        for(review in appUiState.value.detailsScreenReviewsData){
            if (review.commentsEntity.commentID == commentID){
                val myComment = CommentsEntity(
                    review.commentsEntity.commentID,
                    review.commentsEntity.recipeName,
                    review.commentsEntity.authorID,
                    review.commentsEntity.commentText,
                    review.commentsEntity.likes + 1,
                    1,
                    review.commentsEntity.myLikeWasSynced,
                    review.commentsEntity.timestamp)
                myList.add(ReviewWithAuthorDataModel(myComment, review.authorEntity))
            }
            else{
                myList.add(review)
            }

        }

        appUiState.update {
            it.copy(detailsScreenReviewsData = myList)
        }

        /** need to add like to firestore, need to include the person who liked it's UID, need to check
         * that this person did not already like the comment before adding this person's like to the comment
         * (there is potential for abuse/double liking if we don't do this)
         */

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