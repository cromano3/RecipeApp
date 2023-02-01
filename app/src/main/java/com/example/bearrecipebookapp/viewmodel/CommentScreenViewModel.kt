package com.example.bearrecipebookapp.viewmodel

import android.app.Application
import androidx.lifecycle.ViewModel
import com.example.bearrecipebookapp.data.RecipeAppDatabase
import com.example.bearrecipebookapp.data.entity.RecipeEntity
import com.example.bearrecipebookapp.data.repository.CommentScreenRepository
import com.example.bearrecipebookapp.datamodel.CommentScreenDataModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class CommentScreenViewModel(application: Application): ViewModel() {

    private val repository: CommentScreenRepository

    val uiState = MutableStateFlow(CommentScreenDataModel())

    private val coroutineScope = CoroutineScope(Dispatchers.IO)

    init{
        val appDb = RecipeAppDatabase.getInstance(application)
        val commentScreenDao = appDb.CommentScreenDao()
        repository = CommentScreenRepository(commentScreenDao)

    }

    fun updateReviewText(text: String){
        uiState.update {
            it.copy(reviewText = text)
        }
    }

    fun cancelReview(recipeEntity: RecipeEntity){

        coroutineScope.launch(Dispatchers.IO){
            repository.setReviewAsWritten(recipeEntity.recipeName)
        }

    }

    fun confirmReview(recipeEntity: RecipeEntity, reviewText: String){

        coroutineScope.launch(Dispatchers.IO){
            repository.setReview(recipeEntity.recipeName, reviewText)
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