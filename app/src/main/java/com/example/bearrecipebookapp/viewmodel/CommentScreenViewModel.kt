package com.example.bearrecipebookapp.viewmodel

import android.app.Application
import androidx.lifecycle.ViewModel
import com.example.bearrecipebookapp.datamodel.CommentScreenDataModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.update

class CommentScreenViewModel(application: Application): ViewModel() {

    val uiState = MutableStateFlow(CommentScreenDataModel())

    fun updateReviewText(text: String){
        uiState.update {
            it.copy(reviewText = text)
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