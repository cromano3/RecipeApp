package com.example.bearrecipebookapp.viewmodel

import android.app.Application
import androidx.lifecycle.ViewModel
import com.example.bearrecipebookapp.data.RecipeAppDatabase
import com.example.bearrecipebookapp.data.repository.AppRepository
import com.example.bearrecipebookapp.datamodel.AppUiState
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.update

class AppViewModel (application: Application): ViewModel() {

    private val repository: AppRepository
    private val coroutineScope = CoroutineScope(Dispatchers.IO)

    val appUiState = MutableStateFlow(AppUiState())


    init {
        val appDb = RecipeAppDatabase.getInstance(application)
        val appDao = appDb.AppDao()
        repository = AppRepository(appDao)
    }


    suspend fun setupDetailsScreen(recipeName: String){
        val result = repository.getRecipeWithIngredientsAndInstructions(recipeName)

        appUiState.update {
            it.copy(detailsScreenTarget = result)
        }

    }

    suspend fun setupReviewScreen(recipeName: String){
        val result = repository.getRecipeWithIngredientsAndInstructions(recipeName)

        appUiState.update {
            it.copy(reviewScreenTarget = result)
        }

    }

}