package com.example.bearrecipebookapp.ui


import android.app.Application
import androidx.compose.animation.*
import androidx.compose.animation.core.*
import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.AlertDialog
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.material.TextButton
import androidx.compose.runtime.*
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.graphics.TileMode
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewmodel.compose.LocalViewModelStoreOwner
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.bearrecipebookapp.R
import com.example.bearrecipebookapp.data.FilterEntity
import com.example.bearrecipebookapp.datamodel.RecipeWithIngredients
import com.example.bearrecipebookapp.ui.components.SmallRecipeCard
import com.example.bearrecipebookapp.viewmodel.HomeScreenViewModel
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch


@OptIn(ExperimentalFoundationApi::class, ExperimentalAnimationApi::class)
@Composable
fun HomeScreen(
//    onSearchClick: () -> Unit,
    onDetailsClick: () -> Unit,
    onFavoriteClick: (RecipeWithIngredients) -> Unit,
    onMenuClick: (RecipeWithIngredients) -> Unit,
    onMenuRemovedClick: (RecipeWithIngredients) -> Unit,
) {

    val owner = LocalViewModelStoreOwner.current

    owner?.let { viewModelStoreOwner ->
        val homeScreenViewModel: HomeScreenViewModel = viewModel(
            viewModelStoreOwner,
            "HomeScreenViewModel",
            HomeScreenViewModelFactory(
                LocalContext.current.applicationContext
                        as Application,
            )
        )



        /**
         * this is used in view model to compare values, it must be present here in order for the
         * data to be observed otherwise it is always null.  We need a better solution to this.
         */
        val referenceList by homeScreenViewModel.referenceList.observeAsState(listOf())




        val newRecipeList by homeScreenViewModel.newRecipesList.observeAsState(listOf())
        val filtersList by homeScreenViewModel.filtersList.observeAsState(listOf())

        val uiFiltersState by homeScreenViewModel.uiFiltersState.collectAsState()
        val uiAlertState by homeScreenViewModel.uiAlertState.collectAsState()


        var isScrollable by remember { mutableStateOf(true) }



        val coroutineScope = rememberCoroutineScope()
//        val snackbarHostState = remember {SnackbarHostState()}


//        val itemSize = 74.dp
//        val density = LocalDensity.current
//        val itemSizePx = with(density) { itemSize.toPx() }
        val listState = rememberLazyListState()

        if(uiFiltersState.triggerScroll){
            coroutineScope.launch {
                delay(200)
                listState.animateScrollToItem(0)
                homeScreenViewModel.cancelScroll()
            }
        }


            Surface(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(top = 0.dp, bottom = 48.dp),
                color = Color(0xFFd8af84)
            ){

                Column{

                    LazyRow(
                        modifier = Modifier.background(
                            Color(0xFFd8af84)
                        ),
                        state = listState,
                        userScrollEnabled = isScrollable
                    ){
                        items(filtersList, key = { it.filterName }) {

                            FiltersButton(
                                modifier = Modifier
                                    .animateItemPlacement(animationSpec = (TweenSpec(150, delay = 0))),
                                filterEntity = it,
                                isWorking = uiFiltersState.isWorking,
                                onFilterClick =
                                {
                                    homeScreenViewModel.filterBy(it)
                                    isScrollable = !isScrollable
                                },
                            )
                        }
                        item{
                            Spacer(Modifier.size(8.dp))
                        }
                    }

                    Spacer(
                        Modifier
                            .background(
                                Brush.horizontalGradient(
                                    colors = listOf(
                                        Color(0xFFb15f33),
                                        Color(0xFF682300)
                                    ), tileMode = TileMode.Mirror
                                )
                            )
                            .fillMaxWidth()
                            .height(2.dp))


                    Box {
                        androidx.compose.animation.AnimatedVisibility(
                            visible = uiFiltersState.showAllRecipes,
                            enter = EnterTransition.None,
                            exit = ExitTransition.None,
                        ) {

                            LazyVerticalGrid(
                                columns = GridCells.Fixed(2),
                                modifier = Modifier
                                    .padding(top = 0.dp, start = 16.dp, end = 16.dp, bottom = 0.dp),
                                horizontalArrangement = Arrangement.spacedBy(16.dp)
                            ) {

                                items(newRecipeList.size, key = { it }) { index ->
                                    var bottomPadding = 0
                                    if (index + 1 == newRecipeList.size) {
                                        bottomPadding = 16
                                    }

                                    SmallRecipeCard(
                                        modifier = Modifier
                                            .padding(bottom = bottomPadding.dp)
                                            .animateEnterExit(
                                                enter = scaleIn(
                                                    TweenSpec(150, 0, FastOutLinearInEasing)
                                                ),
                                                exit = scaleOut(
                                                    animationSpec = TweenSpec(
                                                        150,
                                                        0,
                                                        FastOutLinearInEasing
                                                    )
                                                )
                                            ),
                                        recipe = newRecipeList[index].recipeEntity,
                                        ingredients = newRecipeList[index].ingredientsList,
                                        onFavoriteClick =
                                        {
                                            onFavoriteClick(newRecipeList[index])
                                            homeScreenViewModel.toggleFavorite(newRecipeList[index])
                                        },
                                        onMenuClick =
                                        {
                                            if (newRecipeList[index].recipeEntity.onMenu == 0){
                                                homeScreenViewModel.toggleMenu(newRecipeList[index])
                                                onMenuClick(newRecipeList[index])
                                            }
                                            else if(newRecipeList[index].recipeEntity.onMenu == 1){
                                                homeScreenViewModel.triggerAlert(newRecipeList[index])
                                            }
                                        },
//
                                        onDetailsClick = {
                                            homeScreenViewModel.setDetailsScreenTarget(newRecipeList[index].recipeEntity.recipeName)
                                            onDetailsClick()
                                        }
                                    )
                                }
                            }
                        }
                    }
                }
                Box(Modifier.fillMaxSize()){
//                SnackbarHost(
//                    modifier = Modifier.align(Alignment.BottomCenter),
//                    hostState = snackbarHostState,
//                    snackbar = {
//                        Snackbar(
//                            backgroundColor = Color(0xFF000000),
//                            contentColor = Color(0xFFFFFFFF))
//                        {
//                            Text(
//                                text = it.message,
//                                color = Color(0xFFFFFFFF)
//                            )
//                        }
//                    }
//                )
                    if(uiAlertState.showAlert){
                        AlertDialog(
                            onDismissRequest = {},
                            text = {
                                Text(text = "Are you sure you want to remove " + uiAlertState.recipe.recipeEntity.recipeName +
                                        " from the Menu? (This will also remove it from the Shopping List.)" )
                            },
                            confirmButton = {
                                TextButton(
                                    onClick = {
                                        onMenuRemovedClick(uiAlertState.recipe)
                                        homeScreenViewModel.toggleMenu(uiAlertState.recipe)
                                        homeScreenViewModel.cancelAlert()
                                    }
                                ) {
                                    Text("Yes")
                                }
                            },
                            dismissButton = {
                                TextButton(
                                    onClick = {
                                        homeScreenViewModel.cancelAlert()
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



@Composable
fun FiltersButton(
    modifier: Modifier,
    filterEntity: FilterEntity,
    isWorking: Boolean,
    onFilterClick: () -> Unit,
){
    val image: Int = when (filterEntity.filterName) {
        "Asian" -> R.drawable.noodles
        "Breakfast" -> R.drawable.coffee
        "Chinese" -> R.drawable.chinesefood1
        "Curry" -> R.drawable.curryicon
        "Soups" -> R.drawable.soup
        "Thai" -> R.drawable.thai
        "Indian" -> R.drawable.samosa1
        "Italian" -> R.drawable.farfalle
        "Japanese" -> R.drawable.sushi
        "Mexican" -> R.drawable.taco
        "Baking" -> R.drawable.bake
        "Sweets" -> R.drawable.baking
        "Vegan" -> R.drawable.vegan
        else -> R.drawable.bagel
    }

    //    val gradientWidth = with(LocalDensity.current) { 200.dp.toPx() }

//    val myIcon: ImageVector
//    val checkBoxBackgroundColor: Color
//    val decoration : TextDecoration
//    val alphaLevel : Float


//    if(filterEntity.isActiveFilter == 1
//        ){
//
//        selected = true
//        myIcon = Icons.Filled.CheckBox
//        checkBoxBackgroundColor = Color(0xFFd8af84)
//        decoration = TextDecoration.LineThrough
//        alphaLevel = 0.55f
//
//    }
//    else{
//
//        selected = false
//        myIcon = Icons.Outlined.CheckBoxOutlineBlank
//        checkBoxBackgroundColor = Color(0xFFd8af84)
//        decoration = TextDecoration.None
//        alphaLevel = 1f
//
//    }

//    val isShownBool: Boolean = filterEntity.isShown == 1
    val isActiveFilterBool: Boolean = filterEntity.isActiveFilter == 1 || filterEntity.isActiveFilter == 2

    val alphaAnim: Float by animateFloatAsState(
        targetValue = if (filterEntity.isActiveFilter == 1 || filterEntity.isActiveFilter == 2) 1f else 0.20f,
        animationSpec = tween(
            durationMillis = 150,
            delayMillis = 0,
            easing = LinearEasing,
        )
    )


    Surface(
        modifier = modifier
            .padding(start = 8.dp, top = 8.dp, bottom = 8.dp)
            .width(74.dp)
            .height(74.dp)
            .border(
                width = 2.dp,
                brush = (Brush.horizontalGradient(
                    colors = listOf(Color(0xFFd8af84), Color(0xFFb15f33),),
                    tileMode = TileMode.Mirror
                )),
                shape = RoundedCornerShape(12.dp)
            )
            .alpha(alphaAnim)
//            .background(
//                brush = Brush.horizontalGradient(
//                    colors = listOf(Color(0xFF682300), Color(0xFFb15f33)),
//                    endX = gradientWidth,
//                    tileMode = TileMode.Mirror
//                ),
//                shape = RoundedCornerShape((14.dp))
//            )
            .clickable(
                enabled = isActiveFilterBool && !isWorking,
                onClick = onFilterClick,
//                if (isActiveFilterBool) onClickFilterDeselected else onClickFilterSelected,
            ),// { selected = !selected },
        shape = RoundedCornerShape(12.dp),
        color =
//        Color(0xFFf8ea9a)
                Color(0xFF682300),
        elevation = 4.dp,
        //color = Color(0xFF682300),//Color(0xFFd8af84),
        contentColor = Color(0xFFd8af84),
    ){
        Column(
            Modifier.fillMaxSize(),
            horizontalAlignment = Alignment.CenterHorizontally
        )
        {
            Image(
                painter = painterResource(id = image), null,
                //contentScale = ContentScale.Crop,
                modifier = Modifier
                    .size(48.dp)
                    .padding(start = 4.dp, top = 4.dp, end = 4.dp, bottom = 1.dp),
                colorFilter = ColorFilter.tint(Color(0xFFd8af84)),
            )

            Text(
                text = filterEntity.filterName,
                modifier = Modifier
                    // .weight(1f)
                    .padding(start = 1.dp, end = 1.dp, bottom = 2.dp)
                    .align(Alignment.CenterHorizontally),
//                    .alpha(alphaLevel),
                color =
                Color(0xFFd8af84)
//                Color(0xFF000000)
                ,
                fontSize = 12.sp
            )
        }
    }
}

@Composable
@Preview
fun MyPreview420() {
    HomeScreen(onDetailsClick = {}, onFavoriteClick = {}, onMenuClick = {}, onMenuRemovedClick = {} )


}

class HomeScreenViewModelFactory(
    val application: Application,
) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {

        return HomeScreenViewModel(
            application,
        ) as T
    }
}

//AnimatedVisibility(
//                            visible = shown,
//                            enter = fadeIn(
//                                TweenSpec(600, 50, FastOutLinearInEasing)
//                            ),
//                            exit = fadeOut(
//                                animationSpec = TweenSpec(600, 50, FastOutLinearInEasing)
//                            )
//                        ){

//                            var currentState = homeScreenData[index].recipeEntity.isShown
//                            val transition = updateTransition(currentState, label = "")
//
//                            val height by transition.animateInt(label = "") {it
//                                when(it){
//                                    1 -> 270
//                                    0 -> 0
//                                    else -> 0
//                                }
//                            }
//
//                            val width by transition.animateInt(label = "") {it
//                                when(it){
//                                    1 -> 170
//                                    0 -> 0
//                                    else -> 0
//                                }
//                            }

//                            val width: Int by animateIntAsState(
//                                targetValue = if (homeScreenData[index].recipeEntity.isShown == 1) 170 else 0,
//                                animationSpec = tween(durationMillis = 500, delayMillis = 100)
//                            )
//
//                            val height: Int by animateIntAsState(
//                                targetValue = if (homeScreenData[index].recipeEntity.isShown == 1) 270 else 0,
//                                animationSpec = tween(durationMillis = 500, delayMillis = 100)
//                            )