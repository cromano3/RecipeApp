package com.example.bearrecipebookapp.viewmodel

import android.app.Application
import androidx.lifecycle.ViewModel
import com.example.bearrecipebookapp.data.ProfileScreenRepository
import com.example.bearrecipebookapp.data.RecipeAppDatabase
import com.example.bearrecipebookapp.datamodel.ProfileScreenDataModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.update

class ProfileScreenViewModel(application: Application): ViewModel() {

    private val repository: ProfileScreenRepository

    val uiState = MutableStateFlow(ProfileScreenDataModel())

    init{
        val appDb = RecipeAppDatabase.getInstance(application)
        val profileScreenDao = appDb.ProfileScreenDao()
        repository = ProfileScreenRepository(profileScreenDao)
    }

    fun setActiveTab(tabName: String){
        uiState.update { currentState ->
            currentState.copy(
                activeTab = tabName
            )
        }
    }
}