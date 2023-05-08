package com.christopherromano.culinarycompanion.ui

import android.app.Application
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.gestures.detectDragGestures
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.gestures.detectVerticalDragGestures
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.graphics.TileMode
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewmodel.compose.LocalViewModelStoreOwner
import androidx.lifecycle.viewmodel.compose.viewModel
import com.christopherromano.culinarycompanion.data.entity.RecipeEntity
import com.christopherromano.culinarycompanion.ui.components.SmallRecipeCard
import com.christopherromano.culinarycompanion.viewmodel.SearchScreenViewModel

@OptIn(ExperimentalComposeUiApi::class)
@Composable
fun SearchScreen(
    isCompact: Boolean,
    onGoBackClick: () -> Unit,
    onDetailsClick: (String) -> Unit,
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

        val results by searchScreenViewModel.results.observeAsState(listOf())
        val previewList by searchScreenViewModel.previewList.observeAsState()

        val myPreviewList: List<String> = previewList?.split(",") ?: listOf("")

        val showResults by searchScreenViewModel.showResults.observeAsState()

        val focusManager = LocalFocusManager.current







        Surface(){
            Column(){
                Surface(
                    Modifier
                        .fillMaxSize()
                        .padding(top = 0.dp, bottom = 56.dp)
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
                    val listState = rememberScrollState()

                    if(listState.isScrollInProgress){
                        focusManager.clearFocus()
                    }

                    //Preview list
                    if(showResults == 0){
                        Column(
                            modifier = Modifier
                                .padding(bottom = 0.dp)
                                .verticalScroll(listState)
                                .pointerInput(Unit) {
                                    detectTapGestures(
                                        onTap = { focusManager.clearFocus() },
                                        onPress = { focusManager.clearFocus() },
                                    )
                                    detectVerticalDragGestures { _, _ -> focusManager.clearFocus() }
                                    detectDragGestures { _, _ -> focusManager.clearFocus() }

                                }
                        )
                        {
                            if(!previewList.isNullOrEmpty()){
                                for(item in myPreviewList){
                                    Surface(
                                        Modifier
                                            .height(48.dp)
                                            .fillMaxWidth()
                                            .clickable(onClick = {
                                                searchScreenViewModel.liveSearchForPush(item)
                                                focusManager.clearFocus()
                                            })
                                    ){
                                        Column(
                                            Modifier.fillMaxSize(),
                                            verticalArrangement = Arrangement.Center,
                                            horizontalAlignment = Alignment.Start
                                        ){
                                            Text(
                                                text = item,
                                                modifier = Modifier.padding(start = 16.dp, top = 0.dp),
                                                color = Color(0xFF682300)
                                            )
                                        }
                                    }
                                    Spacer(
                                        Modifier
                                            .height(2.dp)
                                            .fillMaxWidth()
                                            .border(
                                                width = 2.dp,
                                                brush = (Brush.horizontalGradient(
                                                    colors = listOf(
                                                        Color(0xFFd8af84),
                                                        Color(0xFFb15f33)
                                                    ),
                                                    tileMode = TileMode.Mirror
                                                )),
                                                shape = RectangleShape
                                            ),
                                    )
                                }


//                                    Spacer(
//                                        Modifier
//                                            .fillMaxWidth()
//                                            .height(56.dp))

                            }

                        }
                    }

                    //Results Cards
                    if(showResults == 1){

                        LazyVerticalGrid(
                            columns = if(isCompact) GridCells.Adaptive(140.dp) else  GridCells.Adaptive(180.dp),
                            modifier = Modifier
                                .padding(
                                    start = if(isCompact) 12.dp else 16.dp,
                                    end = if(isCompact) 12.dp else 16.dp
                                )
                                .pointerInput(Unit) {
                                    detectTapGestures(
                                        onTap = { focusManager.clearFocus() },
                                        onPress = { focusManager.clearFocus() },
                                    )
                                    detectVerticalDragGestures { _, _ -> focusManager.clearFocus() }
                                    detectDragGestures { _, _ -> focusManager.clearFocus() }

                                },
                            horizontalArrangement = Arrangement.spacedBy(if(isCompact) 12.dp else 16.dp)
                        ) {

                            items(results.size, key = { it }) { index ->
                                var bottomPadding = 0
                                if (index + 1 == results.size) {
                                    bottomPadding = if(isCompact) 12 else 16
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
                                    onDetailsClick =
                                    {
                                        focusManager.clearFocus()
                                        onDetailsClick(results[index].recipeEntity.recipeName)
                                    },
                                )


                            }
                        }

                        if(uiState.showAlert){
                            AlertDialog(
                                onDismissRequest = {},
                                text = {
                                    Text(
                                        text = "Are you sure you want to remove " + uiState.alertRecipe.recipeEntity.recipeName +
                                            " from the Menu? (This will also remove it from the Shopping List.)",
                                        color = Color(0xFF682300),
                                        fontSize = 16.sp,
                                        textAlign = TextAlign.Center
                                    )
                                },
                                buttons = {
                                    Row(
                                        modifier = Modifier
                                            .padding(all = 8.dp)
                                            .fillMaxWidth(),
                                        horizontalArrangement = Arrangement.SpaceEvenly
                                    ) {

                                        Button(
                                            modifier = Modifier.wrapContentSize(),
                                            onClick = {
                                                searchScreenViewModel.cancelAlert()
                                                      },
                                            elevation = ButtonDefaults.elevation(6.dp),
                                            shape = RoundedCornerShape(25.dp),
                                            border = BorderStroke(
                                                width = 2.dp,
                                                brush = (Brush.horizontalGradient(
                                                    startX = -10f,
                                                    colors = listOf(Color(0xFFd8af84), Color(0xFFb15f33)),
                                                    tileMode = TileMode.Mirror
                                                )),
                                            ),
                                            colors = ButtonDefaults.buttonColors(backgroundColor = Color(0xFFd8af84), contentColor = Color(0xFF682300))
                                        ) {
                                            Text("Cancel")
                                        }

                                        Button(
                                            modifier = Modifier.wrapContentSize(),
                                            onClick = {
                                                onMenuClick(uiState.alertRecipe.recipeEntity)
                                                searchScreenViewModel.toggleMenu(uiState.alertRecipe)
                                                searchScreenViewModel.cancelAlert()
                                            },
                                            elevation = ButtonDefaults.elevation(6.dp),
                                            shape = RoundedCornerShape(25.dp),
                                            border = BorderStroke(
                                                width = 2.dp,
                                                brush = (Brush.horizontalGradient(
                                                    startX = -10f,
                                                    colors = listOf(Color(0xFFd8af84), Color(0xFFb15f33)),
                                                    tileMode = TileMode.Mirror
                                                )),
                                            ),
                                            colors = ButtonDefaults.buttonColors(backgroundColor = Color(0xFFd8af84), contentColor = Color(0xFF682300))
                                        ) {
                                            Text("Yes")
                                        }
                                    }
                                },
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