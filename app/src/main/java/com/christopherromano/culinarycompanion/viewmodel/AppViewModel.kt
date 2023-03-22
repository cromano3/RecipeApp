package com.christopherromano.culinarycompanion.viewmodel

import android.app.Application
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.christopherromano.culinarycompanion.data.RecipeAppDatabase
import com.christopherromano.culinarycompanion.data.entity.RecipeEntity
import com.christopherromano.culinarycompanion.data.repository.AppRepository
import com.christopherromano.culinarycompanion.data.repository.FirebaseRepository
import com.christopherromano.culinarycompanion.datamodel.*
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
                appUiState.update {
                    it.copy(
                        showLoading = true,
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
                //they are already signed in
                if(currentFirestoreUserNotNull){
                    //back up upload sync should then try to upload any of their stuff that wasn't uploaded
                    appUiState.update {
                        it.copy(
                            showLoading = true,
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

    fun toggleConsentBoxCheck(){
        appUiState.update {
            it.copy(consentBoxChecked = !appUiState.value.consentBoxChecked)
        }
    }

    private fun checkAuth(): Boolean{
        return firebaseRepository.currentUser() != null
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

//        if(appUiState.value.reAuthForDelete && firebaseSignInWithGoogleResponse == "ReturningUserSuccess") {
//                appUiState.update {
//                    it.copy(
////                        reAuthForDelete = false,
////                        reAuthForDeleteSuccessful = true,
//                        googleSignInState = ""
//                    )
//                }
//        }
////        else if(appUiState.value.reAuthForDelete){
////            appUiState.update {
////                it.copy(
////                    reAuthForDelete = false,
////                    googleSignInState = ""
////                )
////            }
////        }
//        else
        if(firebaseSignInWithGoogleResponse == "ReturningUserSuccess"){
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
        else if(firebaseSignInWithGoogleResponse == "NewUserSuccess"){
            repository.setOnlineUserType(1)
            appUiState.update {
                it.copy(
                    userIsOnlineStatus = 1,
                    googleSignInState = "",
                    endSplash = true,
                )
            }
        }

        else
        {
            appUiState.update {
                it.copy(
                    firebaseSignInResult = firebaseSignInWithGoogleResponse,
                    googleSignInState = "",
                    endSplash = true,
                )
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


            ////Likes////

//            val unsyncedUserLikes = withContext(Dispatchers.IO) { repository.getUnsyncedUserLikes() }
//
//            println("before new likes upload")
//            if(unsyncedUserLikes.isNotEmpty()){
//                println("new likes not empty")
//                for(likeId in unsyncedUserLikes){
//                    val successStatus = firebaseRepository.updateLike(likeId)
//                    if(successStatus == "Success" || successStatus == "Failed Duplicate Like") {
//                        withContext(Dispatchers.IO) { repository.markLikeAsSynced(likeId) }
//                    }
//                }
//            }

        }
    }

//    private fun dataSync(){
//        viewModelScope.launch {
//            println("data sync coroutine start")
//
////            val uid = withContext(Dispatchers.IO) { getUserIdFromRoom() }
////            appUiState.update { it.copy(userId = uid) }
////
////            println("uid is $uid")
//
//            val uid = firebaseRepository.getUid()
//
//            ////Comments////
//
//            val unsyncedUserComments = withContext(Dispatchers.IO) { repository.getUnsyncedUserComments() }
//
//            println("before comments upload")
//            //upload comments and mark as synced
//            if(unsyncedUserComments.isNotEmpty()) {
//                print("comments not empty")
//                for (comment in unsyncedUserComments) {
//                    //upload comments
//                    firebaseRepository.uploadComment(comment)
//                    //mark as synced in local DB
//                    withContext(Dispatchers.IO) { repository.markCommentAsSynced(comment) }
//                }
//            }
//
//            ////Ratings////
//
//            val unsyncedUserRatings = withContext(Dispatchers.IO) { repository.getUnsyncedUserRatings() }
//
//            println("before ratings upload")
//            //upload ratings and mark as synced
//            if(unsyncedUserRatings.isNotEmpty()) {
//                println("ratings not empty")
//                for (rating in unsyncedUserRatings) {
//                    val successStatus = firebaseRepository.updateRating(rating)
//                    if(successStatus == "Success") {
//                        println("rating successfully updated")
//                        withContext(Dispatchers.IO) { repository.markRatingAsSynced(rating) }
//                    }
//                }
//            }
//
//
//            ////Likes////
//
//            val unsyncedUserLikes = withContext(Dispatchers.IO) { repository.getUnsyncedUserLikes() }
//
//            println("before new likes upload")
//            if(unsyncedUserLikes.isNotEmpty()){
//                println("new likes not empty")
//                for(likeId in unsyncedUserLikes){
//                    val successStatus = firebaseRepository.updateLike(likeId)
//                    if(successStatus == "Success") {
//                        withContext(Dispatchers.IO) { repository.markLikeAsSynced(likeId) }
//                    }
//                }
//            }
//
//
//            ////Do Download Sync////
//
////            //get comments from remote
////            /**this needs to be: "get comments that are timestamped (at least 2-5 mins.) after my last sync completion timestamp
////             * this way we dont get all the comments that the local db already has over and over again each time we sync data.
////             * the local DB needs to store a timestamp (of the same timezone as the firestore) when the data sync is completed to use
////             * to compare this value.
////             */
////            val commentsFromFirestore = firebaseRepository.getComments()
////
////            println("before comments DOWNLOAD")
////            //add comments and update likes in local db
////            if(commentsFromFirestore.isNotEmpty()){
////                println("new comments not empty")
////                for(comment in commentsFromFirestore){
////                    println("do comment sync (authors)")
////                    val authorData = firebaseRepository.getAuthorData(comment)
////                    withContext(Dispatchers.IO) {repository.addAuthor(authorData, comment.authorID)}
////
////                }
////                for(comment in commentsFromFirestore){
////                    println("do comment sync (comments)")
//////                    repository.updateLikes(comment)
////                    withContext(Dispatchers.IO) { repository.addComment(comment) }
////
////                }
////            }
//
//
////            //get ratings from remote
////            /**needs similar timing functionality as above*/
////            val recipeRatingsFromFirestore = firebaseRepository.getRecipeRatings()
////
////            println("before download ratings")
////            //update ratings in local db
////            if(recipeRatingsFromFirestore.isNotEmpty()){
////                println("new ratings not empty")
////                for(recipe in recipeRatingsFromFirestore){
////                    println("do ratings update")
////                    withContext(Dispatchers.IO) { repository.updateRecipeRating(recipe) }
////                }
////            }
//
//
//
//        }
//
//    }

    fun signInWithGoogle(){
        coroutineScope.launch {
            googleOneTapSignInOrUp()
        }
    }

//    fun confirmSignInWithGoogle(){
//        //if yes try google sign in/up
//        signIn()
//
//        appUiState.update {
//            it.copy(
//                showSignInAlert = false,
//            )
//        }
//    }
//
//
//    fun dismissSignInWithGoogle(){
//        if(appUiState.value.userIsOnlineStatus == -2) {
//
//            repository.setOnlineUserType(0)
//
//            appUiState.update {
//                it.copy(
//                    showSignInAlert = false,
//                    userIsOnlineStatus = 0
//                )
//            }
//
//        }
//        else{
//            appUiState.update {
//                it.copy(
//                    showSignInAlert = false,
//                )
//            }
//        }
//
//
//    }

    fun setupDetailsScreen(recipeName: String){
        val recipeData = repository.getRecipeWithIngredientsAndInstructions(recipeName)
        appUiState.update {
            it.copy(
                detailsScreenTarget = recipeData,
                detailsScreenReviewsData = listOf(),
            )
        }
    }

//    suspend fun setupDetailsScreenComments(recipeName: String){
//
//        println("setup $recipeName")
//        val recipeData = repository.getRecipe(recipeName)
//
//
//        var newCommentsList: MutableList<CommentsEntity> = mutableListOf()
//
////        val formatter = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'", Locale.US)
////        println("a")
////
////        val lastCommentSyncTime = recipeData.lastCommentSyncTime
//
//        val rating = withContext(Dispatchers.IO) { firebaseRepository.getRecipeRating(recipeName) }
//        if(rating > -1){
//            withContext(Dispatchers.IO) {repository.setGlobalRating(recipeName, rating)}
//        }
//
//
//        try{
//            val lastDownloadedCommentTimestamp = recipeData.lastDownloadedCommentTimestamp
//            val formatter = SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.US)
//            val date = formatter.parse(lastDownloadedCommentTimestamp)
//
////            val lastDownloadedCommentTimestampFormatted = formatter.parse(lastDownloadedCommentTimestamp)
//
//
//            newCommentsList = firebaseRepository.getComments(recipeName, date!!)
//
//        }
//        catch(e: Exception){
//            newCommentsList = firebaseRepository.getComments(recipeName)
//            println("failed to get new comments based on recent timestamp ${e.message}")
//        }
////        val lastRatingSyncTime = recipeData.lastRatingSyncTime
//
//
//
////        val lastDownloadedRatingTimestampFormatted = formatter.parse(lastDownloadedRatingTimestamp)
//
//
////        val oneHourAfterLastUpdate = Calendar.getInstance()
//
////        if (lastCommentSyncTime == "x") {
////            println("lastCommentSyncTime != empty ")
////            delay(5000)
//
////            val lastCommentSyncTimeFormatted = formatter.parse(lastCommentSyncTime)
//
////            if (lastCommentSyncTimeFormatted != null) {
//
////                oneHourAfterLastUpdate.time = lastCommentSyncTimeFormatted
////                oneHourAfterLastUpdate.add(Calendar.HOUR, 1)
//
////                val currentFirestoreTime = formatter.parse(firebaseRepository.getCurrentTime())
////                val currentTime = Calendar.getInstance()
//
////                if (currentFirestoreTime != null) {
//
////                    currentTime.time = currentFirestoreTime
////
////                    if(oneHourAfterLastUpdate > currentTime){
//
//            //we set this here to prevent abuse
////                        val currentTimeMarker = firebaseRepository.getCurrentTime()
////                        withContext(Dispatchers.IO) { repository.setTimeOfLastUpdate(recipeName, currentTimeMarker) }
////
////                        //do download
//
////        }
////                    }
////
//////                }
////
//////            }
//////        }
//
//////           if(lastRatingSyncTime == "")
//
////                println("lastCommentSyncTime == empty")
////                println("b")
////
////                //we set this here to prevent abuse
////                val currentTimeMarker = firebaseRepository.getCurrentTime()
////                withContext(Dispatchers.IO) { repository.setTimeOfLastUpdate(recipeName, currentTimeMarker) }
////
////                println("c")
////                delay(5000)
////                println("d")
////            //do download
//
//
//
//        if(newCommentsList.isNotEmpty()) {
//
//            val reviewsData = withContext(Dispatchers.IO) { repository.getReviewsData(recipeName) }
//
//            val authorsDataWithComments = firebaseRepository.getAuthorsData(newCommentsList)
//
//            for(comment in authorsDataWithComments){
//                println("for each comment in new comments ${comment.comment.commentText}")
//
//                var commentAlreadyInRoom = false
//                var myLike = 0
//                var myLikeWasSynced = 0
//
//                for (review in reviewsData){
//                    println("for each review in room recipe Name ${review.commentsEntity.recipeName}")
//                    if(review.commentsEntity.commentID == comment.comment.commentID) {
//                        println("already in room")
//                        commentAlreadyInRoom = true
//                        if (review.commentsEntity.likedByMe == 1) {
//                            println("already liked")
//                            myLike = 1
//                        }
//                        if (review.commentsEntity.myLikeWasSynced == 1) {
//                            println("already synced")
//                            myLikeWasSynced = 1
//                        }
//                        break
//                    }
//                }
//
//                if(commentAlreadyInRoom){
//                    println("already in room adding to room with myLike $myLike and was synced $myLikeWasSynced")
//                    withContext(Dispatchers.IO) { repository.addComment(
//                        CommentsEntity(
//                            comment.comment.commentID,
//                            comment.comment.recipeName,
//                            comment.comment.authorID,
//                            comment.comment.commentText,
//                            comment.comment.likes,
//                            myLike,
//                            myLikeWasSynced,
//                            comment.comment.timestamp
//                        )
//                    ) }
//                }
//                else{
//                    withContext(Dispatchers.IO) { repository.addComment(comment.comment) }
//                }
//
//                /**should store karma value here with rest of author data*/
//                withContext(Dispatchers.IO) { repository.addAuthor(comment.authorData, comment.comment.authorID) }
//
//
//
////                reviewsData.add(
////                    ReviewWithAuthorDataModel(
////                        comment.comment,
////                        CommentAuthorEntity(
////                            comment.comment.authorID,
////                            comment.authorData.userName,
////                            0,
////                            comment.authorData.userPhotoURL
////                        )
////                    )
////                )
//
//
//
//            }
//
//            //get latest timestamp from firestore
//            val newMostRecentCommentTimestamp = firebaseRepository.getMostRecentCommentTimestamp(newCommentsList)
//
//            if(!newMostRecentCommentTimestamp.contains("Failed")) {
//
//                withContext(Dispatchers.IO) {
//                    repository.setMostRecentCommentTimestamp(
//                        recipeName,
//                        newMostRecentCommentTimestamp
//                    )
//                }
//            }
//        }
//
////        appUiState.update {
////            it.copy(
////                detailsScreenReviewsData = reviewsData,
////            )
////        }
//
//    }

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
            it.copy(
                detailsScreenTarget = RecipeWithIngredientsAndInstructions(
                    RecipeEntity(
                        recipeName = appUiState.value.detailsScreenTarget.recipeEntity.recipeName,
                        onMenu = appUiState.value.detailsScreenTarget.recipeEntity.onMenu,
                        isDetailsScreenTarget = appUiState.value.detailsScreenTarget.recipeEntity.isDetailsScreenTarget,
                        timeToMake = appUiState.value.detailsScreenTarget.recipeEntity.timeToMake,
                        difficulty = appUiState.value.detailsScreenTarget.recipeEntity.difficulty,
                        globalRating = appUiState.value.detailsScreenTarget.recipeEntity.globalRating,
                        isShown = appUiState.value.detailsScreenTarget.recipeEntity.isShown,
                        isShoppingFilter = appUiState.value.detailsScreenTarget.recipeEntity.isShoppingFilter,
                        isFavorite = appUiState.value.detailsScreenTarget.recipeEntity.isFavorite,
                        isSearchResult = appUiState.value.detailsScreenTarget.recipeEntity.isSearchResult,
                        cookedCount = appUiState.value.detailsScreenTarget.recipeEntity.cookedCount,
                        isRated = 1,
                        userRating = appUiState.value.detailsScreenTarget.recipeEntity.userRating,
                        isRatingSynced = appUiState.value.detailsScreenTarget.recipeEntity.isRatingSynced,
                        isReviewed = appUiState.value.detailsScreenTarget.recipeEntity.isReviewed,
                        reviewText = appUiState.value.detailsScreenTarget.recipeEntity.reviewText,
                        isReviewScreenTarget = appUiState.value.detailsScreenTarget.recipeEntity.isReviewScreenTarget,
                        isReviewSynced = appUiState.value.detailsScreenTarget.recipeEntity.isReviewSynced,
                        lastCommentSyncTime = appUiState.value.detailsScreenTarget.recipeEntity.lastCommentSyncTime,
                        lastDownloadedCommentTimestamp = appUiState.value.detailsScreenTarget.recipeEntity.lastDownloadedCommentTimestamp),
                    appUiState.value.detailsScreenTarget.ingredientsList,
                    appUiState.value.detailsScreenTarget.instructionsList
                )
            )
        }
    }

    fun markAsReviewed(){
        appUiState.update {
            it.copy(
                detailsScreenTarget = RecipeWithIngredientsAndInstructions(
                    RecipeEntity(
                        recipeName = appUiState.value.detailsScreenTarget.recipeEntity.recipeName,
                        onMenu = appUiState.value.detailsScreenTarget.recipeEntity.onMenu,
                        isDetailsScreenTarget = appUiState.value.detailsScreenTarget.recipeEntity.isDetailsScreenTarget,
                        timeToMake = appUiState.value.detailsScreenTarget.recipeEntity.timeToMake,
                        difficulty = appUiState.value.detailsScreenTarget.recipeEntity.difficulty,
                        globalRating = appUiState.value.detailsScreenTarget.recipeEntity.globalRating,
                        isShown = appUiState.value.detailsScreenTarget.recipeEntity.isShown,
                        isShoppingFilter = appUiState.value.detailsScreenTarget.recipeEntity.isShoppingFilter,
                        isFavorite = appUiState.value.detailsScreenTarget.recipeEntity.isFavorite,
                        isSearchResult = appUiState.value.detailsScreenTarget.recipeEntity.isSearchResult,
                        cookedCount = appUiState.value.detailsScreenTarget.recipeEntity.cookedCount,
                        isRated = appUiState.value.detailsScreenTarget.recipeEntity.isRated,
                        userRating = appUiState.value.detailsScreenTarget.recipeEntity.userRating,
                        isRatingSynced = appUiState.value.detailsScreenTarget.recipeEntity.isRatingSynced,
                        isReviewed = 1,
                        reviewText = appUiState.value.detailsScreenTarget.recipeEntity.reviewText,
                        isReviewScreenTarget = appUiState.value.detailsScreenTarget.recipeEntity.isReviewScreenTarget,
                        isReviewSynced = appUiState.value.detailsScreenTarget.recipeEntity.isReviewSynced,
                        lastCommentSyncTime = appUiState.value.detailsScreenTarget.recipeEntity.lastCommentSyncTime,
                        lastDownloadedCommentTimestamp = appUiState.value.detailsScreenTarget.recipeEntity.lastDownloadedCommentTimestamp),
                    appUiState.value.detailsScreenTarget.ingredientsList,
                    appUiState.value.detailsScreenTarget.instructionsList
                )
            )
        }
    }

    fun markReviewAsSynced(){
        appUiState.update {
            it.copy(
                detailsScreenTarget = RecipeWithIngredientsAndInstructions(
                    RecipeEntity(
                        recipeName = appUiState.value.detailsScreenTarget.recipeEntity.recipeName,
                        onMenu = appUiState.value.detailsScreenTarget.recipeEntity.onMenu,
                        isDetailsScreenTarget = appUiState.value.detailsScreenTarget.recipeEntity.isDetailsScreenTarget,
                        timeToMake = appUiState.value.detailsScreenTarget.recipeEntity.timeToMake,
                        difficulty = appUiState.value.detailsScreenTarget.recipeEntity.difficulty,
                        globalRating = appUiState.value.detailsScreenTarget.recipeEntity.globalRating,
                        isShown = appUiState.value.detailsScreenTarget.recipeEntity.isShown,
                        isShoppingFilter = appUiState.value.detailsScreenTarget.recipeEntity.isShoppingFilter,
                        isFavorite = appUiState.value.detailsScreenTarget.recipeEntity.isFavorite,
                        isSearchResult = appUiState.value.detailsScreenTarget.recipeEntity.isSearchResult,
                        cookedCount = appUiState.value.detailsScreenTarget.recipeEntity.cookedCount,
                        isRated = appUiState.value.detailsScreenTarget.recipeEntity.isRated,
                        userRating = appUiState.value.detailsScreenTarget.recipeEntity.userRating,
                        isRatingSynced = appUiState.value.detailsScreenTarget.recipeEntity.isRatingSynced,
                        isReviewed = appUiState.value.detailsScreenTarget.recipeEntity.isReviewed,
                        reviewText = appUiState.value.detailsScreenTarget.recipeEntity.reviewText,
                        isReviewScreenTarget = appUiState.value.detailsScreenTarget.recipeEntity.isReviewScreenTarget,
                        isReviewSynced = 1,
                        lastCommentSyncTime = appUiState.value.detailsScreenTarget.recipeEntity.lastCommentSyncTime,
                        lastDownloadedCommentTimestamp = appUiState.value.detailsScreenTarget.recipeEntity.lastDownloadedCommentTimestamp),
                    appUiState.value.detailsScreenTarget.ingredientsList,
                    appUiState.value.detailsScreenTarget.instructionsList
                )
            )
        }
    }

    fun addedToFavoriteUiUpdate(){
        appUiState.update {
            it.copy(
                detailsScreenTarget = RecipeWithIngredientsAndInstructions(
                    RecipeEntity(
                        recipeName = appUiState.value.detailsScreenTarget.recipeEntity.recipeName,
                        onMenu = appUiState.value.detailsScreenTarget.recipeEntity.onMenu,
                        isDetailsScreenTarget = appUiState.value.detailsScreenTarget.recipeEntity.isDetailsScreenTarget,
                        timeToMake = appUiState.value.detailsScreenTarget.recipeEntity.timeToMake,
                        difficulty = appUiState.value.detailsScreenTarget.recipeEntity.difficulty,
                        globalRating = appUiState.value.detailsScreenTarget.recipeEntity.globalRating,
                        isShown = appUiState.value.detailsScreenTarget.recipeEntity.isShown,
                        isShoppingFilter = appUiState.value.detailsScreenTarget.recipeEntity.isShoppingFilter,
                        isFavorite = 1,
                        isSearchResult = appUiState.value.detailsScreenTarget.recipeEntity.isSearchResult,
                        cookedCount = appUiState.value.detailsScreenTarget.recipeEntity.cookedCount,
                        isRated = appUiState.value.detailsScreenTarget.recipeEntity.isRated,
                        userRating = appUiState.value.detailsScreenTarget.recipeEntity.userRating,
                        isRatingSynced = appUiState.value.detailsScreenTarget.recipeEntity.isRatingSynced,
                        isReviewed = appUiState.value.detailsScreenTarget.recipeEntity.isReviewed,
                        reviewText = appUiState.value.detailsScreenTarget.recipeEntity.reviewText,
                        isReviewScreenTarget = appUiState.value.detailsScreenTarget.recipeEntity.isReviewScreenTarget,
                        isReviewSynced = appUiState.value.detailsScreenTarget.recipeEntity.isReviewSynced,
                        lastCommentSyncTime = appUiState.value.detailsScreenTarget.recipeEntity.lastCommentSyncTime,
                        lastDownloadedCommentTimestamp = appUiState.value.detailsScreenTarget.recipeEntity.lastDownloadedCommentTimestamp),
                    appUiState.value.detailsScreenTarget.ingredientsList,
                    appUiState.value.detailsScreenTarget.instructionsList
                )
            )
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

//            withContext(Dispatchers.IO) { repository.deleteReview(commentID) }
//            println("mark NOT COMMENTED $recipeName")
//            withContext(Dispatchers.IO) { repository.markReviewAsNotCommented(recipeName) }



        }
    }

    fun updateLikes(commentID: String) {

//        val myList: MutableList<ReviewWithAuthorDataModel> = mutableListOf()
//
//        for(review in appUiState.value.detailsScreenReviewsData){
//            if (review.commentsEntity.commentID == commentID){
//                val myComment = CommentsEntity(
//                    review.commentsEntity.commentID,
//                    review.commentsEntity.recipeName,
//                    review.commentsEntity.authorID,
//                    review.commentsEntity.commentText,
//                    review.commentsEntity.likes + 1,
//                    1,
//                    review.commentsEntity.myLikeWasSynced,
//                    review.commentsEntity.timestamp)
//                myList.add(ReviewWithAuthorDataModel(myComment, review.authorEntity))
//            }
//            else{
//                myList.add(review)
//            }
//
//        }
//
//        appUiState.update {
//            it.copy(detailsScreenReviewsData = myList)
//        }


        coroutineScope.launch(Dispatchers.IO) {
//            withContext(Dispatchers.IO) { repository.setAsLiked(commentID) }

            val successStatus = firebaseRepository.updateLike(commentID)
//            if(successStatus == "Success") {
//                withContext(Dispatchers.IO) { repository.markLikeAsSynced(commentID) }
//            }

        }

    }

    fun updateDislikes(commentID: String){
        coroutineScope.launch(Dispatchers.IO) {
            val successStatus = firebaseRepository.updateDislike(commentID)
        }
    }

//    fun updateDetailsScreenWithJustWrittenReview(){
//        val localUserReview = repository.getLocalUserReviewData(appUiState.value.detailsScreenTarget.recipeEntity.recipeName) ?: ""
//        appUiState.update {
//            it.copy(
//                detailsScreenLocalUserReview = localUserReview,
//            )
//        }
//    }

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

    fun submitReport(authorDataWithComment: AuthorDataWithComment) {
        coroutineScope.launch (Dispatchers.IO){
            firebaseRepository.submitReport(authorDataWithComment)
        }
    }

}