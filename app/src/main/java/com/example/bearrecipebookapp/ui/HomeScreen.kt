package com.example.bearrecipebookapp.ui


import android.app.Application
import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.animation.core.*
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.*
import androidx.compose.foundation.gestures.animateScrollBy
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.GridItemSpan
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Restaurant
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
import androidx.compose.ui.text.ExperimentalTextApi
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewmodel.compose.LocalViewModelStoreOwner
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.bearrecipebookapp.R
import com.example.bearrecipebookapp.data.entity.FilterEntity
import com.example.bearrecipebookapp.datamodel.RecipeWithIngredients
import com.example.bearrecipebookapp.ui.components.AddRecipeCard
import com.example.bearrecipebookapp.ui.components.SmallRecipeCard
import com.example.bearrecipebookapp.viewmodel.HomeScreenViewModel
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch


@OptIn(ExperimentalFoundationApi::class, ExperimentalAnimationApi::class,
    ExperimentalTextApi::class
)
@Composable
fun HomeScreen(
//    triggerTutorialAlert: String,
    onDetailsClick: () -> Unit,
    onFavoriteClick: (RecipeWithIngredients) -> Unit,
    onMenuClick: (RecipeWithIngredients) -> Unit,
    onMenuRemovedClick: (RecipeWithIngredients) -> Unit,
    onCreateRecipeClick: () -> Unit,
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




        val shownRecipeList by homeScreenViewModel.shownRecipeList.observeAsState(listOf())
        val filtersList by homeScreenViewModel.filtersList.observeAsState(listOf())

        val showTutorial by homeScreenViewModel.showTutorial.observeAsState()

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
                homeScreenViewModel.cancelScroll()
                delay(420)
//                listState.animateScrollToItem(0)
                listState.animateScrollBy(value= -5000f, animationSpec = tween(durationMillis = 1000))

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
                                    .animateItemPlacement(animationSpec = (TweenSpec(400, delay = 0))),
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
                            .height(2.dp)
                    )


                    Box {
                        androidx.compose.animation.AnimatedVisibility(
                            visible = uiFiltersState.showAllRecipes,
                            enter = fadeIn(TweenSpec(150, 0, FastOutLinearInEasing)),
                            exit = fadeOut(TweenSpec(150, 0, FastOutLinearInEasing)),
                        ) {

                            LazyVerticalGrid(
                                columns = GridCells.Fixed(2),
                                modifier = Modifier
                                    .padding(top = 0.dp, start = 16.dp, end = 16.dp, bottom = 0.dp),
                                horizontalArrangement = Arrangement.spacedBy(16.dp)
                            ) {

                                items(shownRecipeList.size, key = { it }) { index ->
                                    var bottomPadding = 0
                                    if (index + 1 == shownRecipeList.size) {
                                        bottomPadding = 16
                                    }

                                    SmallRecipeCard(
                                        modifier = Modifier
                                            .padding(bottom = bottomPadding.dp)
//                                            .animateEnterExit(
//                                                enter = scaleIn(
//                                                    TweenSpec(150, 0, FastOutLinearInEasing)
//                                                ),
//                                                exit = scaleOut(
//                                                    animationSpec = TweenSpec(
//                                                        150,
//                                                        0,
//                                                        FastOutLinearInEasing
//                                                    )
//                                                )
//                                            )
                                        ,
                                        recipe = shownRecipeList[index].recipeEntity,
                                        ingredients = shownRecipeList[index].ingredientsList,
                                        onFavoriteClick =
                                        {
                                            onFavoriteClick(shownRecipeList[index])
                                            homeScreenViewModel.toggleFavorite(shownRecipeList[index])
                                        },
                                        onMenuClick =
                                        {
                                            if (shownRecipeList[index].recipeEntity.onMenu == 0){
                                                homeScreenViewModel.toggleMenu(shownRecipeList[index])
                                                onMenuClick(shownRecipeList[index])
                                            }
                                            else if(shownRecipeList[index].recipeEntity.onMenu == 1){
                                                homeScreenViewModel.triggerAlert(shownRecipeList[index])
                                            }
                                        },
//
                                        onDetailsClick = {
                                            homeScreenViewModel.setDetailsScreenTarget(shownRecipeList[index].recipeEntity.recipeName)
                                            onDetailsClick()
                                        }
                                    )
                                }

                                if(!uiFiltersState.isFiltered && shownRecipeList.isNotEmpty()) {
                                    item(span = { GridItemSpan(2) }) {

                                        Box(
                                            Modifier
                                                .background(
                                                    Brush.horizontalGradient(
                                                        colors = listOf(
                                                            Color(0xFFb15f33),
                                                            Color(0xFF682300)
                                                        ), tileMode = TileMode.Mirror
                                                    )
                                                )
                                                .height(2.dp)
                                                .fillMaxWidth()
//                                            .animateEnterExit(
//                                                enter = fadeIn(TweenSpec(150, 150, FastOutLinearInEasing)),
//                                                exit = fadeOut(animationSpec = TweenSpec(150, 0, FastOutLinearInEasing)),)
                                            ,
                                        )

                                        Spacer(
                                            Modifier
                                                .fillMaxWidth()
                                                .height(2.dp))

                                        Text(
                                            text = "Custom Recipes",
                                            fontSize = 18.sp,
                                            style = TextStyle(
                                                brush = Brush.horizontalGradient(
                                                    colors = listOf(
                                                        Color(0xFFb15f33),
                                                        Color(0xFF682300)
                                                    )
                                                )
                                            )
                                        )
                                    }


                                }

                                if(!uiFiltersState.isFiltered && shownRecipeList.isNotEmpty()) {
                                    item {
                                        var bottomPadding = 16
                                        AddRecipeCard(
                                            modifier = Modifier
                                                .padding(bottom = bottomPadding.dp)
//                                            .animateEnterExit(
//                                            enter = scaleIn(TweenSpec(150, 0, FastOutLinearInEasing)),
//                                            exit = scaleOut(animationSpec = TweenSpec(150, 0, FastOutLinearInEasing)),)
                                            ,
                                            onCreateRecipeClick = onCreateRecipeClick
                                        )
                                    }
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
                    if(showTutorial.toBoolean()){
                        AlertDialog(
                            onDismissRequest = {
                                homeScreenViewModel.cancelTutorialAlert()
                            },
                            text = {
                                Column(
                                    horizontalAlignment = Alignment.CenterHorizontally
                                ) {
                                    Text(
                                        "You can click the ",
                                        color = Color(0xFF682300),
                                        fontSize = 16.sp,
                                    )

                                    FloatingActionButton(
                                        onClick = { },
                                        elevation = FloatingActionButtonDefaults.elevation(8.dp),
                                        modifier = Modifier
                                            .padding(top = 8.dp, bottom = 8.dp)
                                            .border(
                                                width = 2.dp,
                                                brush = (Brush.horizontalGradient(
                                                    colors = listOf(
                                                        Color(0xFFd8af84),
                                                        Color(0xFFb15f33),

                                                        ),
                                                    tileMode = TileMode.Mirror
                                                )),
                                                shape = CircleShape
                                            )
                                            .size(36.dp)
                                            //the background of the square for this button, it stays a square even tho
                                            //we have shape = circle shape.  If this is not changed you see a solid
                                            //square for the "background" of this button.
                                            .background(color = Color.Transparent),
                                        shape = CircleShape,
                                        //this is the background color of the button after the "Shaping" is applied.
                                        //it is different then the background attribute above.
                                        backgroundColor = Color(0xFF682300)
                                    ) {
                                        Icon(
                                            Icons.Outlined.Restaurant,
                                            tint = Color(0xFFd8af84),
                                            modifier = Modifier.size(20.dp),
                                            // modifier = Modifier.background(color = Color(0xFFFFFFFF)),
                                            contentDescription = null
                                        )
                                    }

                                    Text(
                                        " to add a recipe to your Menu and its ingredients to your" +
                                                " Shopping List, or you can click on any of the recipe cards to see " +
                                                "all the detailed information about the recipe and then click on the ",
                                        color = Color(0xFF682300),
                                        fontSize = 16.sp,
                                        textAlign = TextAlign.Center
                                    )

                                    Surface(
                                        modifier = Modifier
                                            .padding(top = 8.dp, bottom = 8.dp)
                                            .wrapContentSize()
                                            .border(
                                                width = 2.dp,
                                                brush = (Brush.horizontalGradient(
                                                    colors = listOf(
                                                        Color(0xFFd8af84),
                                                        Color(0xFFb15f33)
                                                    ),
                                                    tileMode = TileMode.Mirror
                                                )),
                                                shape = CircleShape
                                            ),
                                        shape = RoundedCornerShape(25.dp),
                                        color = Color(0xFF682300),
                                        elevation = 4.dp,
                                    ) {
                                        Row(
                                            horizontalArrangement = Arrangement.Start,
                                            modifier = Modifier.padding(
                                                start = 12.dp,
                                                end = 12.dp,
                                                top = 12.dp,
                                                bottom = 12.dp
                                            )
                                        )
                                        {
                                            Text(
                                                text = "Add to Menu",
                                                modifier = Modifier
                                                    .align(Alignment.CenterVertically)
                                                    .alpha(1f),
                                                color = Color(0xFFd8af84),
                                                fontSize = 18.sp,
                                                textAlign = TextAlign.Center,
                                                fontWeight = FontWeight.Bold
                                            )
                                        }
                                    }

                                    Text(
                                        " button.",
                                        color = Color(0xFF682300),
                                        fontSize = 16.sp,
                                    )
                                }
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
                                            homeScreenViewModel.cancelTutorialAlert()
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
                                        Text("Got it!")
                                    }
                                }
                            },

                            )

                    }
                    if(uiAlertState.showAlert){
                        AlertDialog(
                            onDismissRequest = {},
                            text = {
                                Text(text = "Are you sure you want to remove " + uiAlertState.recipe.recipeEntity.recipeName +
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
                                            homeScreenViewModel.cancelAlert()
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
                                            onMenuRemovedClick(uiAlertState.recipe)
                                            homeScreenViewModel.toggleMenu(uiAlertState.recipe)
                                            homeScreenViewModel.cancelAlert()
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
    HomeScreen( onDetailsClick = {}, onFavoriteClick = {}, onMenuClick = {}, onMenuRemovedClick = {}, onCreateRecipeClick = {} )


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