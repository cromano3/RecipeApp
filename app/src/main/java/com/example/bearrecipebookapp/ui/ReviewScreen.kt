package com.example.bearrecipebookapp.ui

import android.app.Application
import androidx.compose.foundation.layout.*
import androidx.compose.material.OutlinedTextField
import androidx.compose.material.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.unit.dp
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewmodel.compose.LocalViewModelStoreOwner
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.bearrecipebookapp.ui.components.AlertButton
import com.example.bearrecipebookapp.viewmodel.ReviewScreenViewModel

@Composable
fun ReviewScreen(
    recipeName: String,
    onCancelClick: () -> Unit,
    onConfirmClick: () -> Unit,
){
    val owner = LocalViewModelStoreOwner.current

    owner?.let { viewModelStoreOwner ->
        val reviewScreenViewModel: ReviewScreenViewModel = viewModel(
            viewModelStoreOwner,
            "ReviewScreen",
            ReviewScreenViewModelFactory(
                LocalContext.current.applicationContext
                        as Application,
            )
        )

        val uiState by reviewScreenViewModel.uiState.collectAsState()

        val focusManager = LocalFocusManager.current

        Surface(Modifier.fillMaxWidth()
        ){
            Box(
                Modifier
                    .fillMaxWidth()
                    .padding(start = 16.dp, end = 16.dp)){
                Column(
                    Modifier.fillMaxWidth()
                ){

                    Row(
                        Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween)
                    {
                        AlertButton(buttonText = "Cancel") {onCancelClick()}
                        AlertButton(buttonText = "Confirm") {onConfirmClick()}

                    }


                    OutlinedTextField(
                        value = uiState.reviewText,
                        onValueChange = {reviewScreenViewModel.updateReviewText(it)}
                    )

                }
            }
        }




    }
}

class ReviewScreenViewModelFactory(
    val application: Application,
) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {

        return ReviewScreenViewModel(
            application,
        ) as T
    }
}