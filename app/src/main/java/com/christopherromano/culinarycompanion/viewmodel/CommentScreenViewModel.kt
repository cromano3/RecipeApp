package com.christopherromano.culinarycompanion.viewmodel

import android.app.Application
import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import com.christopherromano.culinarycompanion.data.RecipeAppDatabase
import com.christopherromano.culinarycompanion.data.repository.CommentScreenRepository
import com.christopherromano.culinarycompanion.datamodel.CommentScreenDataModel
import com.christopherromano.culinarycompanion.datamodel.RecipeWithIngredientsAndInstructions
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class CommentScreenViewModel(application: Application): ViewModel() {

    private val repository: CommentScreenRepository

    private val coroutineScope = CoroutineScope(Dispatchers.IO)

    val uiState = MutableStateFlow(CommentScreenDataModel())
    var commentScreenData: LiveData<RecipeWithIngredientsAndInstructions>



    init{
        val appDb = RecipeAppDatabase.getInstance(application)
        val commentScreenDao = appDb.CommentScreenDao()
        repository = CommentScreenRepository(commentScreenDao)

        commentScreenData = repository.commentScreenData

    }

    fun updateReviewText(text: String){
        uiState.update {
            it.copy(reviewText = text)
        }
    }

    fun cancelReview(recipeName: String){
        coroutineScope.launch(Dispatchers.IO){
            repository.setReviewAsWritten(recipeName)
        }
    }


    fun triggerTooLongAlert(){
        uiState.update {
            it.copy(showTooLongAlert = true)
        }

    }

    fun cancelTooLongAlert(){
        uiState.update {
            it.copy(showTooLongAlert = false)
        }
    }





}