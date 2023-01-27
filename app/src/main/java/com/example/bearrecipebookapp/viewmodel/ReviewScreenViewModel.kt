package com.example.bearrecipebookapp.viewmodel

import android.app.Application
import androidx.lifecycle.ViewModel
import com.example.bearrecipebookapp.datamodel.ReviewScreenDataModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.update

class ReviewScreenViewModel(application: Application): ViewModel() {

    val uiState = MutableStateFlow(ReviewScreenDataModel())

    fun updateReviewText(text: String){
        uiState.update {
            it.copy(reviewText = text)
        }

    }

}