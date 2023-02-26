package com.example.bearrecipebookapp.ui

import android.app.Application
import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.LocalContext
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewmodel.compose.LocalViewModelStoreOwner
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.bearrecipebookapp.viewmodel.SettingsScreenViewModel

@Composable
fun SettingsScreen(){
    val owner = LocalViewModelStoreOwner.current

    owner?.let { viewModelStoreOwner ->
        val settingsScreenViewModel: SettingsScreenViewModel = viewModel(
            viewModelStoreOwner = viewModelStoreOwner,
            key = "settingsScreenViewModel",
            factory = SettingsScreenViewModelFactory(LocalContext.current.applicationContext as Application)
        )






    }
}

class SettingsScreenViewModelFactory(
    val application: Application,
//    val recipeName: String,
) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {

        return SettingsScreenViewModel(
            application,
//            recipeName,
//            DetailsScreenFirebaseRepository(application, Firebase.firestore, Firebase.auth)
        ) as T
    }
}