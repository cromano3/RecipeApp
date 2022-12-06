package com.example.bearrecipebookapp.ui

import android.app.Application
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.gestures.detectDragGestures
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.gestures.detectVerticalDragGestures
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.ArrowBack
import androidx.compose.material.icons.outlined.Close
import androidx.compose.material.icons.outlined.Search
import androidx.compose.runtime.*
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Alignment
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
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.unit.dp
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewmodel.compose.LocalViewModelStoreOwner
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.bearrecipebookapp.data.RecipeEntity
import com.example.bearrecipebookapp.ui.components.SmallRecipeCard
import com.example.bearrecipebookapp.viewmodel.SearchScreenViewModel

@OptIn(ExperimentalComposeUiApi::class)
@Composable
fun SearchScreen(
    onGoBackClick: () -> Unit,
    onDetailsClick: () -> Unit,
    onFavoriteClick: (RecipeEntity) -> Unit,
    onMenuClick: (RecipeEntity) -> Unit,
){

    val owner = LocalViewModelStoreOwner.current

    owner?.let { viewModelStoreOwner ->
        val searchScreenViewModel: SearchScreenViewModel = viewModel(
            viewModelStoreOwner = viewModelStoreOwner,
            key = "searchScreenViewModel",
            factory = SearchScreenViewModelFactory(LocalContext.current.applicationContext as Application)
        )

        val uiState by searchScreenViewModel.uiState.collectAsState()
//        val allRecipes by searchScreenViewModel.allRecipes.observeAsState()
        val results by searchScreenViewModel.results.observeAsState(listOf())

        val focusRequester = remember { FocusRequester() }
        val focusManager = LocalFocusManager.current

        val text by remember { mutableStateOf("") }

        var isKeyboardOpen by remember { mutableStateOf(true) }

        if(!uiState.showResults) {
            LaunchedEffect(Unit) {
                focusRequester.requestFocus()
            }
        }

//        BackHandler( onBack = {focusManager.clearFocus()} )



        Surface(){
            Column(){
                Row(){

                    //Go back button
                    IconButton(
                        onClick = { onGoBackClick() },
                        modifier =
                        Modifier
                            .size(48.dp)
                            .align(Alignment.CenterVertically)
                            .background(color = Color.Transparent),
                        // color = Color.Transparent

                    ) {
                        Icon(
                            imageVector = Icons.Outlined.ArrowBack,
                            contentDescription = null,
                            modifier = Modifier
                                .size(24.dp),
                            // .padding(start = 16.dp)
                            // .clickable(onClick = {onGoBackClick(detailsScreenData)}),
                            tint = Color(0xFF000000)
                        )
                    }

                    //Search field
                    TextField(
                        value = uiState.currentInput,
                        onValueChange =
                        {
                            searchScreenViewModel.updatePreview( it, it.text)
                        },
                        modifier = Modifier.focusRequester(focusRequester),
                        textStyle = TextStyle(color = Color(0xFF000000)),
                        leadingIcon = { Icon(Icons.Outlined.Search, contentDescription = null, tint = Color(0xFF000000)) },
                        trailingIcon =
                        {
                            if(uiState.currentInput.text.isNotEmpty()){
                                Surface(
                                    color = Color.Transparent,
                                    modifier = Modifier.clickable {  searchScreenViewModel.updatePreview( TextFieldValue(""), "") }
                                )
                                {
                                    Icon(
                                        Icons.Outlined.Close,
                                        contentDescription = null,
                                        tint = Color(0xFF000000)
                                    )
                                }
                            }
                        },
                        keyboardOptions = KeyboardOptions(imeAction = ImeAction.Search),
                        keyboardActions = KeyboardActions(
                            onSearch = {
                                searchScreenViewModel.liveSearchForClick()
//                                searchScreenViewModel.searchFor(text)
                                focusManager.clearFocus()
                            })
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
                            modifier = Modifier.padding(bottom = 0.dp).pointerInput(Unit) {
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
                                            searchScreenViewModel.liveSearchForPush(it.name)
//                                            searchScreenViewModel.searchForClick(it)
                                            focusManager.clearFocus()
                                        })
                                )
                                {
                                    Text(text = it.name, color = Color(0xFF000000))
                                }
                            }
                            item(){
                                Spacer(Modifier.fillMaxWidth().height(56.dp))
                            }
                        }
                    }

                    //search results
                    if(uiState.showResults){

                        LazyVerticalGrid(
                            columns = GridCells.Fixed(2),
                            modifier = Modifier
                                .padding(top = 0.dp, start = 16.dp, end = 16.dp, bottom = 0.dp).pointerInput(Unit) {
                                    detectTapGestures(
                                        onTap = { focusManager.clearFocus() },
                                        onPress = { focusManager.clearFocus() },
                                    )
                                    detectVerticalDragGestures { _, _ -> focusManager.clearFocus()  }
                                    detectDragGestures { _, _ -> focusManager.clearFocus() }

                                },
                            horizontalArrangement = Arrangement.spacedBy(16.dp)
                        ) {

                            items(results.size, key = { it }) { index ->
                                var bottomPadding = 0
                                if (index + 1 == results.size) {
                                    bottomPadding = 64
                                }

                                SmallRecipeCard(
                                    modifier = Modifier.padding(bottom = bottomPadding.dp),
                                    recipe = results[index].recipeEntity,
                                    ingredients = results[index].ingredientsList,
                                    onFavoriteClick = {
                                        onFavoriteClick(results[index].recipeEntity)
                                        searchScreenViewModel.toggleFavorite(results[index])
                                        focusManager.clearFocus()},
                                    onMenuClick =
                                    {
                                        if (results[index].recipeEntity.onMenu == 0){
                                            focusManager.clearFocus()
                                            onMenuClick(results[index].recipeEntity)
                                            searchScreenViewModel.toggleMenu(results[index])
                                        }
                                        else if(results[index].recipeEntity.onMenu == 1){
                                            focusManager.clearFocus()
                                            searchScreenViewModel.triggerAlert(results[index])
                                        }
                                    },
                                    onDetailsClick = {
                                        searchScreenViewModel.setDetailsScreenTarget(results[index].recipeEntity.recipeName)
                                        onDetailsClick()
                                        focusManager.clearFocus()},
                                )


                            }
                        }

//                        LazyColumn(state = listState,
//                            modifier = Modifier.padding(bottom = 0.dp).pointerInput(Unit) {
//                                detectTapGestures(
//                                    onTap = { focusManager.clearFocus() },
//                                    onPress = { focusManager.clearFocus() },
//                                )
//                                detectVerticalDragGestures { _, _ -> focusManager.clearFocus()  }
//                                detectDragGestures { _, _ -> focusManager.clearFocus() }
//
//                            }){
//
//                            items(
//                                items =
////                                uiState.clickSearchResults
//                                results
//                                ,
//                                key = {it.recipeEntity.recipeName}
//                            ){
//                                SmallRecipeCard(
//                                    modifier = Modifier,
//                                    recipe = it.recipeEntity,
//                                    ingredients = it.ingredientsList,
//                                    onFavoriteClick = {
//                                        onFavoriteClick(it.recipeEntity)
//                                        searchScreenViewModel.toggleFavorite(it)
//                                        focusManager.clearFocus()},
//                                    onMenuClick =
//                                    {
//                                        if (it.recipeEntity.onMenu == 0){
//                                            focusManager.clearFocus()
//                                            onMenuClick(it.recipeEntity)
//                                            searchScreenViewModel.toggleMenu(it)
//                                        }
//                                        else if(it.recipeEntity.onMenu == 1){
//                                            focusManager.clearFocus()
//                                            searchScreenViewModel.triggerAlert(it)
//                                        }
//                                    },
//                                    onDetailsClick = {
//                                        searchScreenViewModel.setDetailsScreenTarget(it.recipeEntity.recipeName)
//                                        onDetailsClick()
//                                        focusManager.clearFocus()},
//                                )
//                            }
//                            item(){
//                                Spacer(Modifier.fillMaxWidth().height(64.dp))
//                            }
//                        }
                        if(uiState.showAlert){
                            AlertDialog(
                                onDismissRequest = {},
                                text = {
                                    Text(text = "Are you sure you want to remove " + uiState.alertRecipe.recipeEntity.recipeName +
                                            " from the Menu? (This will also remove it from the Shopping List.)" )
                                },
                                confirmButton = {
                                    TextButton(
                                        onClick = {
                                            onMenuClick(uiState.alertRecipe.recipeEntity)
                                            searchScreenViewModel.toggleMenu(uiState.alertRecipe)
                                            searchScreenViewModel.cancelAlert()
                                        }
                                    ) {
                                        Text("Yes")
                                    }
                                },
                                dismissButton = {
                                    TextButton(
                                        onClick = {
                                            searchScreenViewModel.cancelAlert()
                                        }
                                    ) {
                                        Text("Cancel")
                                    }
                                }
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