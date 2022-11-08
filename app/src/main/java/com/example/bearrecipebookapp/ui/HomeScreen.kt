package com.example.bearrecipebookapp.ui

import android.app.Application
import androidx.compose.animation.*
import androidx.compose.animation.core.FastOutLinearInEasing
import androidx.compose.animation.core.TweenSpec
import androidx.compose.animation.core.tween
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.clickable
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CheckBox
import androidx.compose.material.icons.outlined.CheckBoxOutlineBlank
import androidx.compose.material.icons.outlined.Close
import androidx.compose.runtime.*
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewmodel.compose.LocalViewModelStoreOwner
import androidx.lifecycle.viewmodel.compose.viewModel
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

        val homeScreenData by homeScreenViewModel.homeScreenData.observeAsState(listOf())
        val filtersList by homeScreenViewModel.filtersList.observeAsState(listOf())
        val referenceList by homeScreenViewModel.referenceList.observeAsState(listOf())


        val uiState = homeScreenViewModel.uiState

        val unfilteredList by homeScreenViewModel.unfilteredList.observeAsState((listOf()))
        val filteredList1 by homeScreenViewModel.filteredList1.observeAsState(listOf())

        var isFiltered = homeScreenViewModel.isFiltered

        val isSecondFiltered = homeScreenViewModel.isSecondFiltered



        Surface(
            modifier = Modifier
                .fillMaxSize()
                .padding(top = 0.dp, bottom = 48.dp),
            color = Color(0xFFd8af84)

        ) {
            Column(){
                Row(Modifier.horizontalScroll(rememberScrollState())){
                    filtersList.forEach {
                        FiltersButton(
                            filterEntity = it,
                            onClickIngredientSelected = { homeScreenViewModel.applyFilter(it.filterName);
                                                        isFiltered = true
                                                        },
                            onClickIngredientDeselected = { homeScreenViewModel.removeFilter(it.filterName);
                                                          isFiltered = false},
                        )
                    }
                }

                var showFilteredList by remember { mutableStateOf(true) }

                if(isFiltered == true){
                    LaunchedEffect(Unit) {
                        delay(1.seconds)
                    showFilteredList = false
                    }
                }
                else{

                    showFilteredList = true

                }



            AnimatedVisibility(
                visible = showFilteredList,
                enter = EnterTransition.None,
                exit = ExitTransition.None,
//                enter = fadeIn(
//                    TweenSpec(40, 0, FastOutLinearInEasing)
//                ),
//                exit = fadeOut(
//                    animationSpec = TweenSpec(400, 550, FastOutLinearInEasing)
//                )
            ){
            LazyVerticalGrid(
                columns = GridCells.Fixed(2),
                modifier = Modifier
                    .padding(top = 0.dp, start = 16.dp, end = 16.dp, bottom = 0.dp),
                horizontalArrangement = Arrangement.spacedBy(16.dp)
                    ) {
                        items(unfilteredList.size, key = {it}) { index ->
                        var myInt = 0
                        if(index + 1 == unfilteredList.size){
                            myInt = 16
                        }
                        SmallRecipeCard(
                            modifier = Modifier
                                .padding(bottom = myInt.dp)
                                .animateItemPlacement(animationSpec = (tween(500)))
                                .animateEnterExit(
                                    enter = fadeIn(
                                        TweenSpec(400, 500, FastOutLinearInEasing)
                                    ),
                                    exit = fadeOut(
                                        animationSpec = TweenSpec(400, 50, FastOutLinearInEasing)
                                    )
                                )
                            ,
                            recipe = unfilteredList[index].recipeEntity,
                            ingredients = unfilteredList[index].ingredientsList,
                            //this does onMenu updates and related ingredients updates
                            //needs to be changed to menu button when we add menu button
                            onClick = { homeScreenViewModel.toggleFavorite(unfilteredList[index]) },
                            onDetailsClick = {
                                homeScreenViewModel.setDetailsScreenTarget(unfilteredList[index].recipeEntity.recipeName);
                                onDetailsClick()
                            }
                            )
                        }
                    }
                }




                    AnimatedVisibility(
                        visible = isFiltered,
                        enter = EnterTransition.None,
                        exit = ExitTransition.None,
//                enter = fadeIn(
//                    TweenSpec(40, 0, FastOutLinearInEasing)
//                ),
//                exit = fadeOut(
//                    animationSpec = TweenSpec(400, 550, FastOutLinearInEasing)
//                )
                    ){
                        LazyVerticalGrid(
                            columns = GridCells.Fixed(2),
                            modifier = Modifier
                                .padding(top = 0.dp, start = 16.dp, end = 16.dp, bottom = 0.dp),
                            horizontalArrangement = Arrangement.spacedBy(16.dp)
                        ) {
                            items(filteredList1.size, key = {it}) { index ->
                                var myInt = 0
                                if(index + 1 == filteredList1.size){
                                    myInt = 16
                                }
                                SmallRecipeCard(
                                    modifier = Modifier
                                        .padding(bottom = myInt.dp)
                                        .animateItemPlacement(animationSpec = (tween(500)))
                                        .animateEnterExit(
                                            enter = fadeIn(
                                                TweenSpec(400, 500, FastOutLinearInEasing)
                                            ),
                                            exit = fadeOut(
                                                animationSpec = TweenSpec(
                                                    400,
                                                    100,
                                                    FastOutLinearInEasing
                                                )
                                            )
                                        )
                                    ,
                                    recipe = filteredList1[index].recipeEntity,
                                    ingredients = filteredList1[index].ingredientsList,
                                    //this does onMenu updates and related ingredients updates
                                    //needs to be changed to menu button when we add menu button
                                    onClick = { homeScreenViewModel.toggleFavorite(filteredList1[index]) },
                                    onDetailsClick = {
                                        homeScreenViewModel.setDetailsScreenTarget(filteredList1[index].recipeEntity.recipeName);
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

@Composable
fun FiltersButton(
    filterEntity: FilterEntity,
    onClickIngredientSelected: () -> Unit,
    onClickIngredientDeselected: () -> Unit
){

    val selected: Boolean

    val gradientWidth = with(LocalDensity.current) { 200.dp.toPx() }

    val myIcon: ImageVector
    val checkBoxBackgroundColor: Color
    val decoration : TextDecoration
    val alphaLevel : Float


    if(filterEntity.isActiveFilter == 1
        ){

        selected = true
        myIcon = Icons.Filled.CheckBox
        checkBoxBackgroundColor = Color(0xFFd8af84)
        decoration = TextDecoration.LineThrough
        alphaLevel = 0.55f


    }
    else{

        selected = false
        myIcon = Icons.Outlined.CheckBoxOutlineBlank
        checkBoxBackgroundColor = Color(0xFFd8af84)
        decoration = TextDecoration.None
        alphaLevel = 1f

    }


    Surface(
        modifier = Modifier
            .padding(start = 8.dp, top = 8.dp)
            .width(150.dp)
            .height(36.dp)
            .alpha(alphaLevel)
//            .background(
//                brush = Brush.horizontalGradient(
//                    colors = listOf(Color(0xFF682300), Color(0xFFb15f33)),
//                    endX = gradientWidth,
//                    tileMode = TileMode.Mirror
//                ),
//                shape = RoundedCornerShape((14.dp))
//            )
            .clickable(
                enabled = !selected,
                onClick = onClickIngredientSelected,
            ),// { selected = !selected },
        shape = RoundedCornerShape(25.dp),
        color = Color(0xFF682300),
        elevation = 4.dp,
        //color = Color(0xFF682300),//Color(0xFFd8af84),
        contentColor = Color(0xFFd8af84),
    ){
        /*
            if selected then show X
         */
        if (selected){
            Box{
                IconButton(
                    modifier = Modifier
                        .align(Alignment.CenterEnd)
                        .size(36.dp),
                    onClick = onClickIngredientDeselected //{ selected = !selected }
                ){
                    Icon(
                        modifier = Modifier,
                        imageVector = Icons.Outlined.Close,
                        tint = Color(0xFFFFFFFF),
                        contentDescription = null
                    )
                }
            }
        }
        Row(
            Modifier.fillMaxSize(),
            horizontalArrangement = Arrangement.Start
        )
        {
            Icon(
                imageVector = myIcon,
                tint = checkBoxBackgroundColor,

                //  .background(color = Color(0xFFFFFFFF)),
                contentDescription = null,
                modifier = Modifier
                    .padding(start = 6.dp, top = 2.dp, end = 2.dp, bottom = 2.dp)
                    .size(28.dp)
                    .align(Alignment.CenterVertically)
                    .alpha(alphaLevel)
                //.weight(1f)

            )
            Text(
                modifier = Modifier
                    // .weight(1f)
                    .padding(start = 4.dp)
                    .align(Alignment.CenterVertically)
                    .alpha(alphaLevel),
                text = filterEntity.filterName,
                textDecoration = decoration,
                fontSize = 16.sp
            )
        }
    }
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