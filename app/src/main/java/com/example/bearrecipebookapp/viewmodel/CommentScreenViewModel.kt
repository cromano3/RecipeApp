package com.example.bearrecipebookapp.viewmodel

import android.app.Application
import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import com.example.bearrecipebookapp.data.RecipeAppDatabase
import com.example.bearrecipebookapp.data.repository.CommentScreenRepository
import com.example.bearrecipebookapp.datamodel.CommentScreenDataModel
import com.example.bearrecipebookapp.datamodel.RecipeWithIngredientsAndInstructions
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

    fun confirmReview(recipeName: String, reviewText: String){

        coroutineScope.launch(Dispatchers.IO){
            repository.setReview(recipeName, reviewText)
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