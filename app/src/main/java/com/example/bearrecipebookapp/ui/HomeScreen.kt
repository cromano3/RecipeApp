package com.example.bearrecipebookapp.ui


import android.app.Application
import androidx.compose.animation.*
import androidx.compose.animation.core.*
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.runtime.*
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.graphics.Color
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
import com.example.bearrecipebookapp.ui.components.SmallRecipeCard
import com.example.bearrecipebookapp.viewmodel.HomeScreenViewModel
import kotlinx.coroutines.delay
import kotlin.time.Duration.Companion.seconds


@OptIn(ExperimentalFoundationApi::class, ExperimentalAnimationApi::class)
@Composable
fun HomeScreen(
 //   homeScreenViewModel: HomeScreenViewModel = viewModel(),
    onDetailsClick: () -> Unit
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

//        val homeScreenData by homeScreenViewModel.homeScreenData.observeAsState(listOf())
        val referenceList by homeScreenViewModel.referenceList.observeAsState(listOf())


//        val uiState = homeScreenViewModel.uiState

        val uiFiltersState by homeScreenViewModel.uiFiltersState.collectAsState()

//        val filtersList by homeScreenViewModel.filtersList.observeAsState(listOf())

        val unfilteredList by homeScreenViewModel.unfilteredList.observeAsState((listOf()))
        val filteredList1 by homeScreenViewModel.filteredList1.observeAsState(listOf())
//        val filteredList2 by homeScreenViewModel.filteredList2.observeAsState(listOf())

        var showUnfilteredList by remember { mutableStateOf(true) }
        var showFilteredList1 by remember { mutableStateOf(false) }
//        var showFilteredList2 by remember { mutableStateOf(false) }
        var filterController by remember { mutableStateOf(false) }

        var filterSelectedClick by remember { mutableStateOf(false) }
        var filterDeselectedClick by remember { mutableStateOf(false) }

//        val coroutineScope = CoroutineScope(Dispatchers.Main)



        var isFiltered by remember { mutableStateOf(false) }
//        var listChange by remember { mutableStateOf(false) }

//        var visibilityDelay = 0
//        var positionDelay = 0

//        var sortedList: List<FilterEntity> = filtersList.sortedWith(compareBy{it.filterName})

//        if(isFiltered){
//
////            filtersList = filtersList.sortedWith(compareBy{it.isShown})
//
////            homeScreenViewModel.sortFilterShown()
//
//            visibilityDelay = 110
//            positionDelay = 120
//        }
//        else{
//
//            visibilityDelay = 0
//            positionDelay = 110
//
////            homeScreenViewModel.sortAlphabetical()
////            filtersList = filtersList.sortedWith(compareBy{it.filterName})
//
//        }


//        val itemSize = 74.dp
//        val density = LocalDensity.current
//        val itemSizePx = with(density) { itemSize.toPx() }

        val listState = rememberLazyListState()

        if(isFiltered){
            LaunchedEffect(Unit) {
                delay(150)
            listState.animateScrollToItem(0)
//                listState.animateScrollBy(

//                    /**
//                     * Try setting the scroll count to the correct index of the item to make animation smoother
//                     * and observe/better understand the effect
//                     */
//                    value = -(itemSizePx * uiFiltersState.filtersList.size),
//                    animationSpec = tween(durationMillis = 300, delayMillis = 0)
//                )
            }
        }


        Surface(
            modifier = Modifier
                .fillMaxSize()
                .padding(top = 0.dp, bottom = 48.dp),
            color = Color(0xFFd8af84)

        ) {

            Column{

                LazyRow(
                    state = listState,
                    userScrollEnabled = !isFiltered
                ){
                    items(uiFiltersState.filtersList, key = { it.filterName }) {

                        FiltersButton(
                            modifier = Modifier
                                .animateItemPlacement(animationSpec = (TweenSpec(150, delay = 0)))
                            ,
                            filterEntity = it,
                            onClickFilterSelected = {
                                homeScreenViewModel.applyFilter(it.filterName)
                                filterSelectedClick = !filterSelectedClick
                                isFiltered = true
//                                showUnfilteredList = false;
//                                homeScreenViewModel.sortFilterShown()
                            },
                            onClickFilterDeselected = {
                                isFiltered = false
//                                homeScreenViewModel.sortAlphabetical();
//                          coroutineScope.launch(Dispatchers.Main) {
                                filterDeselectedClick = !filterDeselectedClick

                                homeScreenViewModel.removeFilter(it.filterName)
//                                showUnfilteredList = true
//                          }
                            }
                        )
                    }

                    item{
                        Spacer(Modifier.size(8.dp))
                    }

                }

                if(filterSelectedClick){
                    LaunchedEffect(Unit) {

                        if(!filterController){

                            showUnfilteredList = false
//                            showFilteredList2 = false
                            delay(0.150.seconds)
                            showFilteredList1 = true
//                            filterController = true

                            filterSelectedClick = false
                        }
                        else{

                            showFilteredList1 = false
                            delay(0.150.seconds)
////                            showFilteredList2 = true
////                            filterController = false
//
                            filterSelectedClick = false
                        }
                    }
                }

                if(filterDeselectedClick)
                    LaunchedEffect(Unit) {
                        showFilteredList1 = false
//                        showFilteredList2 = false
                        delay(0.150.seconds)
                        showUnfilteredList = true
//                        filterController = false

                        filterDeselectedClick = false
                }

//                if(listChange){
//                    showUnfilteredList = false
//                }

//                if(uiFiltersState.isReady){
//                    showUnfilteredList = true
//                }


                Box {
                    //Unfiltered List
                    androidx.compose.animation.AnimatedVisibility(
                        visible = showUnfilteredList,
                        enter = EnterTransition.None,
                        exit = ExitTransition.None,
                    ) {

                        LazyVerticalGrid(
                            columns = GridCells.Fixed(2),
                            modifier = Modifier
                                .padding(top = 0.dp, start = 16.dp, end = 16.dp, bottom = 0.dp),
                            horizontalArrangement = Arrangement.spacedBy(16.dp)
                        ) {

                            items(unfilteredList.size, key = { it }) { index ->
                                var bottomPadding = 0
                                if (index + 1 == unfilteredList.size) {
                                    bottomPadding = 16
                                }

                                
                                SmallRecipeCard(
                                    modifier = Modifier
                                        .padding(bottom = bottomPadding.dp)
//                                        .animateItemPlacement(animationSpec = (tween(500)))
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
                                    recipe = unfilteredList[index].recipeEntity,
                                    ingredients = unfilteredList[index].ingredientsList,
                                    //this does onMenu updates and related ingredients updates
                                    //needs to be changed to menu button when we add menu button
                                    onClick = { homeScreenViewModel.toggleFavorite(unfilteredList[index]) },
                                    onDetailsClick = {
                                        homeScreenViewModel.setDetailsScreenTarget(unfilteredList[index].recipeEntity.recipeName)
                                        onDetailsClick()
                                    }
                                )
                            }
                        }
                    }


                    //Filtered list 1
                    androidx.compose.animation.AnimatedVisibility(
                        visible = showFilteredList1,
                        enter = EnterTransition.None,
                        exit = ExitTransition.None,
                    ) {

                        LazyVerticalGrid(
                            columns = GridCells.Fixed(2),
                            modifier = Modifier
                                .padding(top = 0.dp, start = 16.dp, end = 16.dp, bottom = 0.dp),
                            horizontalArrangement = Arrangement.spacedBy(16.dp)
                        ) {

                            items(filteredList1.size, key = { it }) { index ->
                                var myInt = 0
                                if (index + 1 == filteredList1.size) {
                                    myInt = 16
                                }

                                SmallRecipeCard(
                                    modifier = Modifier
                                        .padding(bottom = myInt.dp)
//                                        .animateItemPlacement(animationSpec = (tween(500)))
                                        .animateEnterExit(
                                            enter = scaleIn(
                                                TweenSpec(150, 150, FastOutLinearInEasing)
                                            ),
                                            exit = scaleOut(
                                                animationSpec = TweenSpec(
                                                    150,
                                                    0,
                                                    FastOutLinearInEasing
                                                )
                                            )
                                        ),
                                    recipe = filteredList1[index].recipeEntity,
                                    ingredients = filteredList1[index].ingredientsList,
                                    //this does onMenu updates and related ingredients updates
                                    //needs to be changed to menu button when we add menu button
                                    onClick = { homeScreenViewModel.toggleFavorite(filteredList1[index]) },
                                    onDetailsClick = {
                                        homeScreenViewModel.setDetailsScreenTarget(filteredList1[index].recipeEntity.recipeName)
                                        onDetailsClick()
                                    }
                                )
                            }
                        }
                    }
                }




            }
        }
    }
}



@Composable
fun FiltersButton(
    modifier: Modifier,
    filterEntity: FilterEntity,
    onClickFilterSelected: () -> Unit,
    onClickFilterDeselected: () -> Unit
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
        "Baked Goods" -> R.drawable.bake
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

    val isShownBool: Boolean = filterEntity.isShown == 1
    val isActiveFilterBool: Boolean = filterEntity.isActiveFilter == 1

    val alphaAnim: Float by animateFloatAsState(
        targetValue = if (filterEntity.isShown == 1) 1f else 0f,
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
                enabled = isShownBool,
                onClick = if (isActiveFilterBool) onClickFilterDeselected else onClickFilterSelected,
            ),// { selected = !selected },
        shape = RoundedCornerShape(12.dp),
        color = Color(0xFFf8ea9a),
        elevation = 4.dp,
        //color = Color(0xFF682300),//Color(0xFFd8af84),
        contentColor = Color(0xFFd8af84),
    ){
        /*
            if selected then show X
         */
//        if (selected){
//            Box{
//                IconButton(
//                    modifier = Modifier
//                        .align(Alignment.Center)
//                        .size(36.dp),
//                    onClick = onClickFilterDeselected //{ selected = !selected }
//                ){
//                    Icon(
//                        modifier = Modifier,
//                        imageVector = Icons.Outlined.Close,
//                        tint = Color(0xFF000000),
//                        contentDescription = null
//                    )
//                }
//            }
//        }
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
            )


//            Icon(
//                imageVector = myIcon,
//                tint = checkBoxBackgroundColor,
//
//                //  .background(color = Color(0xFFFFFFFF)),
//                contentDescription = null,
//                modifier = Modifier
//                    .padding(start = 6.dp, top = 2.dp, end = 2.dp, bottom = 2.dp)
//                    .size(28.dp)
//                    .align(Alignment.CenterVertically)
//                    .alpha(alphaLevel)
//                //.weight(1f)
//
//            )
            Text(
                text = filterEntity.filterName,
                modifier = Modifier
                    // .weight(1f)
                    .padding(bottom = 2.dp)
                    .align(Alignment.CenterHorizontally),
//                    .alpha(alphaLevel),
                color = Color(0xFF000000),

                //textDecoration = decoration,
                fontSize = 12.sp
            )
        }
    }
}

@Composable
@Preview
fun MyPreview420() {
    HomeScreen(onDetailsClick = {} )


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