package com.example.bearrecipebookapp.ui

import android.app.Application
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.OutlinedTextField
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.material.TextFieldDefaults
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewmodel.compose.LocalViewModelStoreOwner
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.bearrecipebookapp.datamodel.RecipeWithIngredientsAndInstructions
import com.example.bearrecipebookapp.ui.components.CancelAlertButton
import com.example.bearrecipebookapp.ui.components.ConfirmAlertButton
import com.example.bearrecipebookapp.ui.components.OneButtonAlert
import com.example.bearrecipebookapp.viewmodel.CommentScreenViewModel
import kotlinx.coroutines.delay

@Composable
fun CommentScreen(
    commentScreenData: RecipeWithIngredientsAndInstructions,
    onCancelClick: () -> Unit,
    onConfirmClick: (String, String) -> Unit
) {
    val owner = LocalViewModelStoreOwner.current

    owner?.let { viewModelStoreOwner ->
        val commentScreenViewModel: CommentScreenViewModel = viewModel(
            viewModelStoreOwner,
            "CommentScreenViewModel",
            CommentScreenViewModelFactory(
                LocalContext.current.applicationContext
                        as Application,
            )
        )


        val uiState by commentScreenViewModel.uiState.collectAsState()

//        val commentScreenData by commentScreenViewModel.commentScreenData.observeAsState(RecipeWithIngredientsAndInstructions())

//        var text by remember { mutableStateOf("") }
//        var ingredients by remember { mutableStateOf("") }

        val focusManager = LocalFocusManager.current
        val focusRequester = remember { FocusRequester() }


        LaunchedEffect(Unit) {
            delay(200)
            focusRequester.requestFocus()
        }


        Surface(Modifier.fillMaxSize()
        ){
            Box(
                Modifier
                    .fillMaxWidth()
                    .padding(start = 16.dp, end = 16.dp)){
                Column(
                    Modifier.fillMaxWidth()
                ){

                    Row(
                        Modifier
                            .fillMaxWidth()
                            .padding(start = 12.dp, end = 12.dp, top = 12.dp, bottom = 12.dp),
                        horizontalArrangement = Arrangement.Center,
                        verticalAlignment = Alignment.CenterVertically)
                    {
                        CancelAlertButton(
                            buttonText = "Cancel",
                            onButtonClick = {
                                commentScreenViewModel.cancelReview(recipeName = commentScreenData.recipeEntity.recipeName )
                                onCancelClick()
                            }
                        )

                        Spacer(
                            Modifier
                                .size(1.dp)
                                .weight(1f))

                        Text(
                            text = commentScreenData.recipeEntity.recipeName,
                            modifier = Modifier.width(150.dp),
                            color = Color(0xFF682300),
                            fontWeight = FontWeight.Bold,
                            textAlign = TextAlign.Center)

                        Spacer(
                            Modifier
                                .size(1.dp)
                                .weight(1f))

                        ConfirmAlertButton(buttonText = "Confirm") {
                            if(uiState.reviewText.length > 1000) {
                                commentScreenViewModel.triggerTooLongAlert()
                            }
                            else{
//                                commentScreenViewModel.confirmReview(recipeName = commentScreenData.recipeEntity.recipeName, uiState.reviewText)
                                onConfirmClick(commentScreenData.recipeEntity.recipeName, uiState.reviewText)
                            }
                        }
                    }

                    OutlinedTextField(
                        value = uiState.reviewText,
                        onValueChange = {commentScreenViewModel.updateReviewText(it)},
                        modifier = Modifier
                            .height(366.dp)
                            .fillMaxWidth()
                            .padding(start = 16.dp, end = 16.dp)
                            .focusRequester(focusRequester),
                        keyboardOptions = KeyboardOptions(imeAction = ImeAction.Done),
                        keyboardActions = KeyboardActions(
                            onDone = { }
                        ),
                        maxLines = 16,
                        colors = TextFieldDefaults.textFieldColors(textColor = Color.Black)
                    )

                    Text(
                        text = "${uiState.reviewText.length}/1000 max length",
                        modifier = Modifier.padding(start = 16.dp),
                        color = if(uiState.reviewText.length > 1000) Color.Red else Color.Black)

                }
            }

            //Too long Alert
            if(uiState.showTooLongAlert){
                OneButtonAlert(
                    text = "The maximum size is 1000 characters.",
                    confirmButtonText = "Got it",
                    onConfirmClick = { commentScreenViewModel.cancelTooLongAlert() },
                    onDismiss = { commentScreenViewModel.cancelTooLongAlert() }
                )
            }

        }
    }
}

class CommentScreenViewModelFactory(
    val application: Application,
) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {

        return CommentScreenViewModel(
            application,
        ) as T
    }
}

//@Preview
//@Composable
//fun comprev(){
//    CommentScreen(
//        RecipeWithIngredientsAndInstructions(),
//        onCancelClick = { /*TODO*/ }) {}
//}