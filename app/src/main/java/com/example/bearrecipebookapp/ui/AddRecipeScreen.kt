package com.example.bearrecipebookapp.ui

import android.app.Application
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewmodel.compose.LocalViewModelStoreOwner
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.bearrecipebookapp.viewmodel.AddRecipeScreenViewModel

@Composable
fun AddRecipeScreen() {
    val owner = LocalViewModelStoreOwner.current

    owner?.let { viewModelStoreOwner ->
        val addRecipeScreenViewModel: AddRecipeScreenViewModel = viewModel(
            viewModelStoreOwner,
            "AddRecipeScreenViewModel",
            AddRecipeScreenViewModelFactory(
                LocalContext.current.applicationContext
                        as Application,
            )
        )

        val uiState by addRecipeScreenViewModel.uiState.collectAsState()

        var text by remember { mutableStateOf("") }
        var ingredients by remember { mutableStateOf("") }

        val focusManager = LocalFocusManager.current

//        val density = LocalDensity.current
//        val configuration = LocalConfiguration.current
//        val screenHeightPx = with(density) { configuration.screenHeightDp.dp.roundToPx() }

        Surface(
            modifier = Modifier
                .fillMaxSize()
                .padding(start = 16.dp, end = 16.dp, bottom = 56.dp)
        ) {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .verticalScroll(rememberScrollState())) {

                Spacer(
                    Modifier
                        .fillMaxWidth()
                        .height(8.dp))

                Text(
                    text = "Recipe Title \n(32 characters max.)",
                    color = Color(0xFF000000)
                )
                TextField(
                    modifier = Modifier.fillMaxWidth(),
                    value = text,
                    onValueChange = { text = it },
                    singleLine = true,
                    colors = TextFieldDefaults.textFieldColors(textColor = Color(0xFF000000)),
                    keyboardOptions = KeyboardOptions(imeAction = ImeAction.Next),
                    keyboardActions = KeyboardActions(
                        onNext = { focusManager.moveFocus(androidx.compose.ui.focus.FocusDirection.Down) }
                    ),
                )

                Spacer(
                    Modifier
                        .fillMaxWidth()
                        .height(4.dp))

                Text(
                    text = "Ingredients List \n(use comma ' , ' to separate each item)",
                    color = Color(0xFF000000),
                    overflow = TextOverflow.Visible
                )
                TextField(
                    modifier = Modifier.fillMaxWidth(),
                    value = ingredients,
                    onValueChange = { ingredients = it },
                    singleLine = false,
                    colors = TextFieldDefaults.textFieldColors(textColor = Color(0xFF000000)),
                    keyboardOptions = KeyboardOptions(imeAction = ImeAction.Next),
                    keyboardActions = KeyboardActions(
                        onNext = { focusManager.moveFocus(androidx.compose.ui.focus.FocusDirection.Down) }
                    ),
                )

                Spacer(
                    Modifier
                        .fillMaxWidth()
                        .height(4.dp))

                Text(
                    text = "Instructions List",
                    color = Color(0xFF000000),
                    overflow = TextOverflow.Visible
                )


                for (x in 0..uiState.instructionsList.size) {

                    if(x != 0) Spacer(
                        Modifier
                            .fillMaxWidth()
                            .height(4.dp))

                    if (x != uiState.instructionsList.size) {

                        Text(
                            text = "Step ${x+1}",
                            color = Color(0xFF000000),
                        )


                        TextField(
                            modifier = Modifier.fillMaxWidth(),
                            value = uiState.instructionsList[x],
                            onValueChange = { addRecipeScreenViewModel.updateInstruction(text = it, x) },
                            singleLine = false,
                            colors = TextFieldDefaults.textFieldColors(textColor = Color(0xFF000000)),
                            keyboardOptions = KeyboardOptions(imeAction =  if(x == uiState.instructionsList.size - 1) ImeAction.Done else ImeAction.Next),
                            keyboardActions = KeyboardActions(
                                onNext = { focusManager.moveFocus(androidx.compose.ui.focus.FocusDirection.Down) },
                                onDone = { focusManager.clearFocus() }
                            ),
                        )

                    }

                    else{



                        if(uiState.instructionsList.size == 20) {

                                Row() {
                                Spacer(Modifier.weight(1f))

                                Button(
                                    onClick = { addRecipeScreenViewModel.deleteInstructionField() },
                                    colors = ButtonDefaults.buttonColors(
                                        backgroundColor = Color(
                                            0xFF682300
                                        ), contentColor = Color(0xFFd8af84)
                                    )
                                ) {

                                    Text(text = "Delete Step ${uiState.instructionsList.size}")

                                }
                            }
                        }
                        else if(uiState.instructionsList.size > 1){

                            Row(horizontalArrangement = Arrangement.SpaceEvenly){

                                Button(
                                    onClick = { addRecipeScreenViewModel.addInstructionField() },
                                    colors = ButtonDefaults.buttonColors(backgroundColor = Color(0xFF682300), contentColor = Color(0xFFd8af84))
                                ) {

                                    Text(text = "Add Step ${uiState.instructionsList.size + 1}")

                                }

                                Spacer(Modifier.weight(1f))

                                Button(
                                    onClick = { addRecipeScreenViewModel.deleteInstructionField() },
                                    colors = ButtonDefaults.buttonColors(backgroundColor = Color(0xFF682300), contentColor = Color(0xFFd8af84))
                                ) {

                                    Text(text = "Delete Step ${uiState.instructionsList.size}")

                                }
                            }
                        }
                        else{

                            Button(
                                onClick = { addRecipeScreenViewModel.addInstructionField() },
                                colors = ButtonDefaults.buttonColors(backgroundColor = Color(0xFF682300), contentColor = Color(0xFFd8af84))
                            ) {

                                Text(text = "Add Step ${uiState.instructionsList.size + 1}")

                            }

                        }

                    }
                }



            }
        }
    }
}

class AddRecipeScreenViewModelFactory(
    val application: Application,
) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {

        return AddRecipeScreenViewModel(
            application,
        ) as T
    }
}