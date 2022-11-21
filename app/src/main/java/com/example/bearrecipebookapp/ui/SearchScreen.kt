package com.example.bearrecipebookapp.ui

import android.app.Application
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.material.TextField
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.TextStyle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewmodel.compose.LocalViewModelStoreOwner
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.bearrecipebookapp.ui.components.SmallRecipeCard
import com.example.bearrecipebookapp.viewmodel.SearchScreenViewModel

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
        var text by remember { mutableStateOf("") }

        LaunchedEffect(Unit) {
            focusRequester.requestFocus()
        }

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
                        textStyle = TextStyle(color = Color(0xFF000000))
//                        colors =
                    )

                    //Profile Screen button
                }
                if(!uiState.showResults){
                    LazyColumn(){
                        items(items = uiState.previewList, key = {it.name}){
                            Surface(
                                Modifier
                                    .wrapContentSize()
                                    .clickable(onClick = { searchScreenViewModel.searchForClick(it) }))
                            {
                                Text(text = it.name, color = Color(0xFF000000))
                            }
                        }
                    }
                }


                if(uiState.showResults){
                    LazyColumn(){
                        items(items = uiState.clickSearchResults, key = {it.recipeEntity.recipeName}){
                            SmallRecipeCard(
                                modifier = Modifier,
                                recipe = it.recipeEntity,
                                ingredients = it.ingredientsList,
                                onFavoriteClick = { /*TODO*/ },
                                onMenuClick = { /*TODO*/ },
                                onDetailsClick = {},
                            )
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