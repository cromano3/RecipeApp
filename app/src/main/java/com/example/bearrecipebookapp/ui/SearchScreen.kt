package com.example.bearrecipebookapp.ui

import android.app.Application
import androidx.activity.compose.BackHandler
import androidx.compose.foundation.clickable
import androidx.compose.foundation.gestures.detectDragGestures
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.gestures.detectVerticalDragGestures
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.material.TextField
import androidx.compose.runtime.*
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.input.ImeAction
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewmodel.compose.LocalViewModelStoreOwner
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.bearrecipebookapp.ui.components.SmallRecipeCard
import com.example.bearrecipebookapp.viewmodel.SearchScreenViewModel

@OptIn(ExperimentalComposeUiApi::class)
@Composable
fun SearchScreen(
    onGoBackClick: () -> Unit,
    onDetailsClick: () -> Unit,
){

    val owner = LocalViewModelStoreOwner.current

    owner?.let { viewModelStoreOwner ->
        val searchScreenViewModel: SearchScreenViewModel = viewModel(
            viewModelStoreOwner = viewModelStoreOwner,
            key = "searchScreenViewModel",
            factory = SearchScreenViewModelFactory(LocalContext.current.applicationContext as Application)
        )


        val uiState by searchScreenViewModel.uiState.collectAsState()

        val focusRequester = remember { FocusRequester() }
        val focusManager = LocalFocusManager.current

        var text by remember { mutableStateOf("") }

        var isKeyboardOpen by remember { mutableStateOf(true) }

        LaunchedEffect(Unit) {
            focusRequester.requestFocus()
        }

        BackHandler(onBack = {focusManager.clearFocus()})



        Surface(){
            Column(){
                Row(){

                    //Go back button

                    //Search field
                    TextField(
                        value = text,
                        onValueChange = {
                            text = it
                            searchScreenViewModel.updatePreview(it)
                                        },
                        modifier = Modifier.focusRequester(focusRequester),
                        textStyle = TextStyle(color = Color(0xFF000000)),
                        keyboardOptions = KeyboardOptions(imeAction = ImeAction.Search),
                        keyboardActions = KeyboardActions(onDone = {focusManager.clearFocus()})
//                        colors =
                    )

                    //Profile Screen button
                }
                Surface(
                    Modifier
                        .fillMaxSize()
                        .pointerInput(Unit) {
                            detectTapGestures(
                                onTap = { focusManager.clearFocus() },
                                onPress = { focusManager.clearFocus() },
                            )
                            detectDragGestures { _, _ -> focusManager.clearFocus() }
                            detectVerticalDragGestures { _, _ -> focusManager.clearFocus() }

                        }
                )
                {
                    var listState = rememberLazyListState()

                    if(listState.isScrollInProgress){
                        focusManager.clearFocus()
                    }

                    if(!uiState.showResults){
                        LazyColumn(
                            state = listState,
                            modifier = Modifier.pointerInput(Unit) {
                            detectTapGestures(
                                onTap = { focusManager.clearFocus() },
                                onPress = { focusManager.clearFocus() },
                            )
                            detectVerticalDragGestures { _, _ -> focusManager.clearFocus()  }
                            detectDragGestures { _, _ -> focusManager.clearFocus() }

                        }
                        )
                        {
                            items(items = uiState.previewList, key = {it.name}){
                                Surface(
                                    Modifier
                                        .wrapContentSize()
                                        .clickable(onClick = {
                                            searchScreenViewModel.searchForClick(it)
                                            focusManager.clearFocus()
                                        })
                                )
                                {
                                    Text(text = it.name, color = Color(0xFF000000))
                                }
                            }
                        }
                    }

                    if(uiState.showResults){
                        LazyColumn(state = listState,
                            modifier = Modifier.pointerInput(Unit) {
                                detectTapGestures(
                                    onTap = { focusManager.clearFocus() },
                                    onPress = { focusManager.clearFocus() },
                                )
                                detectVerticalDragGestures { _, _ -> focusManager.clearFocus()  }
                                detectDragGestures { _, _ -> focusManager.clearFocus() }

                            }){
                            items(items = uiState.clickSearchResults, key = {it.recipeEntity.recipeName}){
                                SmallRecipeCard(
                                    modifier = Modifier,
                                    recipe = it.recipeEntity,
                                    ingredients = it.ingredientsList,
                                    onFavoriteClick = { /*TODO*/ focusManager.clearFocus()},
                                    onMenuClick = { /*TODO*/ focusManager.clearFocus()},
                                    onDetailsClick = {focusManager.clearFocus()},
                                )
                            }
                        }
                    }

                }


            }
        }

    }
}



class SearchScreenViewModelFactory(
    val application: Application,
): ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {

        return SearchScreenViewModel(
            application,
        ) as T
    }
}