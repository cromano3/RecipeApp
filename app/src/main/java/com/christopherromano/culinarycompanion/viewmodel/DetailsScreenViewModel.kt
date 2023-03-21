package com.christopherromano.culinarycompanion.viewmodel

import android.app.Application
import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.christopherromano.culinarycompanion.data.RecipeAppDatabase
import com.christopherromano.culinarycompanion.data.entity.CommentsEntity
import com.christopherromano.culinarycompanion.data.entity.QuantitiesTableEntity
import com.christopherromano.culinarycompanion.data.entity.RecipeEntity
import com.christopherromano.culinarycompanion.data.repository.DetailsScreenFirebaseRepository
import com.christopherromano.culinarycompanion.data.repository.DetailsScreenRepository
import com.christopherromano.culinarycompanion.datamodel.*
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class DetailsScreenViewModel(application: Application, recipeName: String, private val detailsScreenFirebaseRepository: DetailsScreenFirebaseRepository): ViewModel() {

    private val repository: DetailsScreenRepository

    private val coroutineScope = CoroutineScope(Dispatchers.IO)

//    private var job: Job? = null

//    var detailsScreenData: LiveData<RecipeWithIngredientsAndInstructions>

//    var firebaseCommentsLiveData: LiveData<List<AuthorDataWithComment>>


//    var globalRating: LiveData<Int>


//    var globalRatingFirebaseLiveData: LiveData<Int>

    var ingredientQuantitiesList: LiveData<List<QuantitiesTableEntity>>

    var globalRating: LiveData<Int>

    val uiAlertState = MutableStateFlow(UiAlertStateDetailsScreenDataModel())

    var authState: LiveData<Int>



//    val uiState = MutableStateFlow(DetailsScreenUiState())

    ////////////////
    private val _commentsList = MutableStateFlow<List<AuthorDataWithComment>>(listOf())
    val commentsList: StateFlow<List<AuthorDataWithComment>>
        get() = _commentsList

    private val _recipeName = recipeName



    private fun getCommentsList(recipeName: String, limit: Int){
//        job?.cancel()
//        job =
        viewModelScope.launch {

            val isAuthed = withContext(Dispatchers.IO) { detailsScreenFirebaseRepository.currentUser() }

            if(isAuthed != null) {

                withContext(Dispatchers.IO) {
                    detailsScreenFirebaseRepository.getCommentsList(recipeName, limit).collect {
                        _commentsList.value = it
                    }
                }

            }
        }
    }


    fun changeLimit(){
        getCommentsList(_recipeName, 50)
    }



    ///////////////



    init {
        val appDb = RecipeAppDatabase.getInstance(application)
        val detailsScreenDao = appDb.DetailsScreenDao()
        repository = DetailsScreenRepository(detailsScreenDao)

//        globalRating = repository.globalRating
//        detailsScreenData = repository.detailsScreenData

        repository.setRecipeName(recipeName)

//        firebaseCommentsLiveData = detailsScreenFirebaseRepository.firebaseCommentsLiveData
        detailsScreenFirebaseRepository.setRecipeName(recipeName)
        println("INIT INIT INIT INIT INIT")
//        detailsScreenFirebaseRepository.setCommentResultLimit(4)

//        globalRatingFirebaseLiveData = detailsScreenFirebaseRepository.globalRatingFirebaseLiveData

        globalRating = repository.globalRating

        ingredientQuantitiesList = repository.ingredientQuantitiesList

        getCommentsList(recipeName, 4)

        authState = detailsScreenFirebaseRepository.authState

        getGlobalRating(recipeName)






//        uiState.update {
//            it.copy(reviewsData = reviewsData)
//        }


    }

    private fun getGlobalRating(recipeName: String) {
        viewModelScope.launch {

            val isAuthed = withContext(Dispatchers.IO) { detailsScreenFirebaseRepository.currentUser() }

            if(isAuthed != null) {

                if (recipeName.isNotEmpty()) {
                    val globalRating = withContext(Dispatchers.IO) {
                        detailsScreenFirebaseRepository.getGlobalRating(recipeName)
                    }

                    if(globalRating != 0) {
                        withContext(Dispatchers.IO) {
                            repository.setGlobalRating(
                                recipeName,
                                globalRating
                            )
                        }
                    }
                }
            }

        }
    }

//    fun setCommentsLimit(limit: Int){
//        println("SET LIMIT SET LIMIT SET LIMIT SET LIMIT SET LIMIT SET LIMIT SET LIMIT $limit")
//        detailsScreenFirebaseRepository.setCommentResultLimit(limit)
//    }



    fun setLiked(commentID: String) {

        val myList: MutableList<ReviewWithAuthorDataModel> = mutableListOf()

//        for(review in uiState.value.reviewsData){
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

//        uiState.update {
//            it.copy(reviewsData = myList)
//        }

    }

    fun removeFromMenu(recipe: RecipeWithIngredientsAndInstructions){

        repository.cleanIngredients()
        repository.cleanShoppingFilters()

        coroutineScope.launch(Dispatchers.IO) {

            repository.removeFromMenu(recipe.recipeEntity.recipeName)

            for(x in 0 until recipe.ingredientsList.size){
                repository.updateQuantityNeeded(recipe.ingredientsList[x].ingredientName, recipe.ingredientsList[x].quantityNeeded - 1)
                repository.setIngredientQuantityOwned(recipe.ingredientsList[x], 0)
            }

        }

    }

    fun addToMenu(recipe: RecipeWithIngredientsAndInstructions){

        repository.cleanIngredients()
        repository.cleanShoppingFilters()

        coroutineScope.launch(Dispatchers.IO) {

            repository.addToMenu(recipe.recipeEntity.recipeName)

            for(x in 0 until recipe.ingredientsList.size){
                repository.updateQuantityNeeded(recipe.ingredientsList[x].ingredientName, recipe.ingredientsList[x].quantityNeeded + 1)
            }
        }

    }

    fun addCooked(recipe: RecipeWithIngredientsAndInstructions){
        repository.addCooked(recipe)
    }

    fun addExp(recipe: RecipeWithIngredientsAndInstructions){

        coroutineScope.launch(Dispatchers.IO) {
            val cookedCountMultiplier =
                if(recipe.recipeEntity.cookedCount < 5){
                    1 - (recipe.recipeEntity.cookedCount * .10)
                }
                else{
                    .50
                }
            val exp = (35  * cookedCountMultiplier)  * ((.05 * recipe.recipeEntity.difficulty) + 1)

            repository.addExpToGive(exp.toInt())
        }

    }


    fun triggerCompletedAlert(recipe: RecipeWithIngredientsAndInstructions){
        uiAlertState.update { currentState ->
            currentState.copy(
                showCompletedAlert = true,
                recipe = recipe
            )
        }
    }

    fun cancelCompletedAlert(){
        uiAlertState.update { currentState ->
            currentState.copy(
                showCompletedAlert = false,
                recipe = RecipeWithIngredientsAndInstructions(RecipeEntity(), listOf(), listOf())
            )
        }
    }

    fun confirmCompletedAlert(recipeEntity: RecipeEntity){

        val isAuthed = detailsScreenFirebaseRepository.currentUser()


        if(isAuthed != null) {

            if (recipeEntity.isRated == 0) {
                uiAlertState.update { currentState ->
                    currentState.copy(
                        showCompletedAlert = false,
                        showRatingAlert = true,
                    )
                }
            } else if (recipeEntity.userRating == 1 && recipeEntity.isFavorite == 0) {
                uiAlertState.update { currentState ->
                    currentState.copy(
                        showCompletedAlert = false,
                        showFavoriteAlert = true,
                    )
                }
            } else if (recipeEntity.isReviewed == 0) {
                uiAlertState.update { currentState ->
                    currentState.copy(
                        showCompletedAlert = false,
                        showLeaveReviewAlert = true,
                    )
                }
            } else {
                uiAlertState.update { currentState ->
                    currentState.copy(
                        showCompletedAlert = false,
                    )
                }
            }
        }
        else{
            uiAlertState.update { currentState ->
                currentState.copy(
                    showCompletedAlert = false,
                )
            }
        }


    }

    fun cancelRatingAlert(){
        uiAlertState.update { currentState ->
            currentState.copy(
                showRatingAlert = false,
                starCount = 0,
                reviewText = ""
            )
        }
    }

    fun confirmRating(recipeEntity: RecipeEntity) {


            //show favorite alert
            if(uiAlertState.value.isThumbUpSelected && recipeEntity.isFavorite == 0) {

                //write rating to DB
//                viewModelScope.launch(Dispatchers.IO) {

//                    withContext(Dispatchers.IO) { repository.setLocalRating(recipeEntity.recipeName, 1) }

                    uiAlertState.update { currentState ->
                        currentState.copy(
                            showFavoriteAlert = true,
                            showRatingAlert = false,
                            isThumbUpSelected = false,
                            isThumbDownSelected = false
                        )
                    }

//                }

            }
            //show write review alert
            else if (
                uiAlertState.value.isThumbDownSelected && recipeEntity.isReviewed == 0
                || uiAlertState.value.isThumbUpSelected && recipeEntity.isReviewed == 0){


                //write rating to DB
//                viewModelScope.launch(Dispatchers.IO) {

//                    withContext(Dispatchers.IO) {
//                        repository.setLocalRating(
//                            recipeEntity.recipeName,
//                            if (uiAlertState.value.isThumbUpSelected) 1 else -1
//                        )
//                    }

                    uiAlertState.update { currentState ->
                        currentState.copy(
                            showLeaveReviewAlert = true,
                            showRatingAlert = false,
                            isThumbUpSelected = false,
                            isThumbDownSelected = false
                        )
                    }

//                }

            }

            //finish alerts
            else if(uiAlertState.value.isThumbUpSelected || uiAlertState.value.isThumbDownSelected){
                uiAlertState.update { currentState ->
                    currentState.copy(
                        showLeaveReviewAlert = false,
                        showRatingAlert = false,
                        isThumbUpSelected = false,
                        isThumbDownSelected = false
                    )
                }
            }
            else{
//                uiAlertState.update { currentState ->
//                    currentState.copy(
//                        showRatingAlert = false,
//                        isThumbUpSelected = false,
//                        isThumbDownSelected = false
//                    )
//                }
            }





    }

    fun addToFavorite(recipeEntity: RecipeEntity){

        coroutineScope.launch (Dispatchers.IO) {

            repository.setAsFavorite(recipeEntity.recipeName)

            if(recipeEntity.isReviewed == 0) {
                uiAlertState.update { currentState ->
                    currentState.copy(
                        showFavoriteAlert = false,
                        showLeaveReviewAlert = true,
                    )
                }
            }
            else{
                cancelFavoriteAlert()
            }

        }
    }

    fun doNotAddToFavorite(recipeEntity: RecipeEntity){

        if(recipeEntity.isReviewed == 0) {
            uiAlertState.update { currentState ->
                currentState.copy(
                    showFavoriteAlert = false,
                    showLeaveReviewAlert = true,
                    )
            }
        }
        else{
            cancelFavoriteAlert()
        }
    }

    fun cancelFavoriteAlert(){
        uiAlertState.update { currentState ->
            currentState.copy(
                showFavoriteAlert = false,
            )
        }
    }

    fun thumbDownClicked(){
        uiAlertState.update {
            it.copy(
                isThumbDownSelected = !it.isThumbDownSelected,
                isThumbUpSelected = false
            )
        }
    }

    fun thumbUpClicked(){
        uiAlertState.update {
            it.copy(
                isThumbDownSelected = false,
                isThumbUpSelected = !it.isThumbUpSelected
            )
        }
    }

    fun confirmShowWriteReviewAlert() {

        uiAlertState.update { currentState ->
            currentState.copy(
                showLeaveReviewAlert = false
            )
        }


    }

//    suspend fun confirmShowWriteReviewAlert(recipeEntity: RecipeEntity) {
//
//        /** Will be main thread query to ensure data is ready when user gets to Comment Screen */
//
//            repository.cleanReviewTarget()
//            repository.setReviewTarget(recipeEntity.recipeName)
//
//            uiAlertState.update { currentState ->
//                currentState.copy(
//                    showLeaveReviewAlert = false
//                )
//            }
//
//
//    }

    fun doNotWriteReview(recipeEntity: RecipeEntity) {

        coroutineScope.launch(Dispatchers.IO) {

            repository.setReviewAsWritten(recipeEntity.recipeName)

            uiAlertState.update { currentState ->
                currentState.copy(
                    showLeaveReviewAlert = false
                )
            }
        }

    }

    fun cancelShowWriteReviewAlert() {
        uiAlertState.update { currentState ->
            currentState.copy(
                showLeaveReviewAlert = false
            )
        }
    }

    fun triggerRemoveAlert(recipe: RecipeWithIngredientsAndInstructions){
        uiAlertState.update { currentState ->
            currentState.copy(
                showRemoveAlert = true,
                recipe = recipe
            )
        }
    }

    fun cancelRemoveAlert(){
        uiAlertState.update { currentState ->
            currentState.copy(
                showRemoveAlert = false,
                recipe = RecipeWithIngredientsAndInstructions(RecipeEntity(), listOf(), listOf())
            )
        }
    }

    fun triggerReportAlert(authorDataWithComment: AuthorDataWithComment) {
        uiAlertState.update {
            it.copy(
                showReportAlert = true,
                reportedComment = authorDataWithComment
            )
        }
    }

    fun cancelReportAlert() {
        uiAlertState.update {
            it.copy(
                showReportAlert = false,
                reportedComment = AuthorDataWithComment(AuthorData("","",0), CommentsEntity())
            )
        }
    }

    fun toggleFavorite(recipe: RecipeWithIngredientsAndInstructions){
        if(recipe.recipeEntity.isFavorite == 0){
            repository.updateFavorite(recipe.recipeEntity.recipeName, 1)
        }
        else if(recipe.recipeEntity.isFavorite == 1){
            repository.updateFavorite(recipe.recipeEntity.recipeName, 0)
        }
    }
}