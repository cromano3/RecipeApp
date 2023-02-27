package com.example.bearrecipebookapp.viewmodel

import android.app.Application
import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.bearrecipebookapp.data.RecipeAppDatabase
import com.example.bearrecipebookapp.data.repository.SettingsScreenFirebaseRepository
import com.example.bearrecipebookapp.datamodel.UiAlertStateSettingsScreenDataModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class SettingsScreenViewModel(
    application: Application,
    private val settingsScreenFirebaseRepository: SettingsScreenFirebaseRepository
): ViewModel() {

    var authState: LiveData<Int>

    val uiAlertState = MutableStateFlow(UiAlertStateSettingsScreenDataModel())


    init {
        val appDb = RecipeAppDatabase.getInstance(application)
//        val homeScreenDao = appDb.HomeScreenDao()
//        repository = HomeScreenRepository(homeScreenDao)

        authState = settingsScreenFirebaseRepository.authState

    }

    fun updateDisplayName(){
        viewModelScope.launch {
            val currentDisplayName = settingsScreenFirebaseRepository.getCurrentDisplayName()

            uiAlertState.update {
                it.copy(
                    showChangeDisplayNameAlert = true,
                    displayName = currentDisplayName
                )
            }

        }


    }
}