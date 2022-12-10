package com.example.bearrecipebookapp.ui

import android.app.Application
import androidx.compose.animation.EnterTransition
import androidx.compose.animation.ExitTransition
import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.animation.core.FastOutLinearInEasing
import androidx.compose.animation.core.TweenSpec
import androidx.compose.animation.scaleOut
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Favorite
import androidx.compose.material.icons.outlined.Reviews
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.*
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewmodel.compose.LocalViewModelStoreOwner
import androidx.lifecycle.viewmodel.compose.viewModel
import coil.compose.AsyncImage
import com.example.bearrecipebookapp.R
import com.example.bearrecipebookapp.datamodel.RecipeWithIngredients
import com.example.bearrecipebookapp.ui.components.RecipeCard
import com.example.bearrecipebookapp.ui.theme.BearRecipeBookAppTheme
import com.example.bearrecipebookapp.viewmodel.ProfileScreenViewModel

@OptIn(ExperimentalAnimationApi::class)
@Composable
fun ProfileScreen(
    onRemoveClick: (RecipeWithIngredients) -> Unit,
    onDetailsClick: () -> Unit,
) {

    val owner = LocalViewModelStoreOwner.current

    owner?.let { it ->
        val profileScreenViewModel: ProfileScreenViewModel = viewModel(
            it,
            "ProfileScreenViewModel",
            ProfileScreenViewModelFactory(LocalContext.current.applicationContext as Application,)
        )


        val uiState by profileScreenViewModel.uiState.collectAsState()

        val favoritesData by profileScreenViewModel.favoritesData.observeAsState(listOf())
        val cookedData by profileScreenViewModel.cookedData.observeAsState(listOf())

        val uiAlertState by profileScreenViewModel.uiAlertState.collectAsState()

        val fadedColors = listOf(Color(0x80D8AF84), Color(0x80B15F33))
        val fullColors = listOf(Color(0xFFd8af84), Color(0xFFb15f33))



        Column(modifier = Modifier.fillMaxSize()) {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(250.dp)
                    .background(
                        Brush.horizontalGradient(
                            colors = listOf(
                                Color(0xFFb15f33),
                                Color(0xFF682300)
                            ),
                            tileMode = TileMode.Mirror
                        )
                    ),
                contentAlignment = Alignment.Center
            ) {
                Column(
                    modifier = Modifier.wrapContentSize(),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Surface(modifier = Modifier.size(150.dp), shape = RoundedCornerShape(50.dp)) {
                        Box(
                            modifier = Modifier
                                .size(150.dp)
//                    .border(4.dp, color = Color(0xFFFFFFFF), shape = RoundedCornerShape(100.dp))
                                .border(
                                    width = 2.dp,
                                    brush = (Brush.verticalGradient(
                                        colors = listOf(
                                            Color(0xFFFFFFFF),
//                                //    Color(0xFFFFFFFF),
//                                    Color(0xFFE10600),
//                                    Color(0xFFFFFFFF),
//                                    Color(0xFF6769f1),
//                                    Color(0xFFFFFFFF),
//                                    Color(0xFFb15f33),
                                            Color(0xFFb15f33),
                                            Color(0xFFb15f33),
                                            Color(0xFFb15f33)
                                        ),
                                        tileMode = TileMode.Mirror
                                    )),
                                    shape = RoundedCornerShape(50.dp)
                                )
                                .background(color = Color(0xFFd8af84)),
                            contentAlignment = Alignment.Center,
                        ) {
                            Image(

                                painter = painterResource(R.drawable.chef),
                                contentScale = ContentScale.Fit,
                                alignment = Alignment.Center,
                                contentDescription = null,
                                modifier = Modifier
                                    .size(120.dp)
//                            .border(4.dp, color = Color(0xFFFFFFFF), shape = RoundedCornerShape(50.dp))
                                ,


                                )
                        }

                    }



                    Spacer(Modifier.height(4.dp))

                    Text(text = "lvl 2")

                    Spacer(Modifier.height(4.dp))

                    //Will be progress bar
                    Spacer(
                        Modifier
                            .width(50.dp)
                            .height(4.dp)
                            .background(Color(0xFFFFFFFF))
                    )
                }

//            Text(text = "lvl 2", modifier = Modifier
//                .align(Alignment.BottomStart)
//                .padding(bottom = 20.dp, start = 20.dp))

            }
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(80.dp)
            ) {
                Surface(
                    color = Color.Transparent,

                    modifier = Modifier
                        .fillMaxSize()
                        .weight(1f)
                        .clickable(onClick = { profileScreenViewModel.setActiveTab("favorites") })
                        .background(
                            brush = Brush.horizontalGradient(
                                listOf(
                                    Color(0xFF682300),
                                    Color(0xFF682300)
                                )
                            ),
                            alpha = if (uiState.activeTab == "favorites") 0.75f else 1f
                        ),
                ) {
                    Column(
                        modifier = Modifier.fillMaxSize(),
                        horizontalAlignment = Alignment.CenterHorizontally,
                        verticalArrangement = Arrangement.SpaceEvenly
                    ) {

                        //Favorite button
                        Surface(
                            elevation = 8.dp,
                            modifier = Modifier
                                .border(
                                    width = 2.dp,
                                    brush = (Brush.horizontalGradient(
                                        colors = if (uiState.activeTab == "favorites") fullColors else fadedColors,
                                        tileMode = TileMode.Mirror
                                    )),
                                    shape = CircleShape
                                )
                                .size(36.dp)
                                .alpha(if (uiState.activeTab == "favorites") 1f else 0.5f),
                            shape = CircleShape,
                            color = Color(0xFF682300)
                        ) {
                            Box(contentAlignment = Alignment.Center) {
                                Icon(
                                    Icons.Outlined.Favorite,
                                    tint = Color(0xFFd8af84),
                                    modifier = Modifier.size(20.dp),
                                    // modifier = Modifier.background(color = Color(0xFFFFFFFF)),
                                    contentDescription = null
                                )
                            }
                        }
                        Text("Favorites", modifier = Modifier.alpha(if (uiState.activeTab == "favorites") 1f else 0.5f), color = Color(0xFFd8af84))
                    }
                }
                Surface(
                    color = Color.Transparent,
                    modifier = Modifier
                        .fillMaxSize()
                        .weight(1f)
                        .clickable(onClick = { profileScreenViewModel.setActiveTab("cooked") })
                        .background(
                            brush = Brush.horizontalGradient(
                                listOf(
                                    Color(0xFF682300),
                                    Color(0xFF682300)
                                )
                            ), alpha = if (uiState.activeTab == "cooked") 0.75f else 1f
                        )
                ) {
                    Column(
                        modifier = Modifier.fillMaxSize(),
                        horizontalAlignment = Alignment.CenterHorizontally,
                        verticalArrangement = Arrangement.SpaceEvenly
                    ) {

                        //Cooked button
                        Surface(
                            elevation = 8.dp,
                            color = Color(0xFF682300),
                            modifier = Modifier
                                .border(
                                    width = 2.dp,
                                    brush = (Brush.horizontalGradient(
                                        colors = if (uiState.activeTab == "cooked") fullColors else fadedColors,
                                        tileMode = TileMode.Mirror
                                    )),
                                    shape = CircleShape
                                )
                                .size(36.dp)
                                .alpha(if (uiState.activeTab == "cooked") 1f else 0.5f),
                            shape = CircleShape,

                        ) {
                            Box(contentAlignment = Alignment.Center) {
                                Image(
                                    painter = painterResource(id = R.drawable.tray),
                                    contentDescription = null,
                                    modifier = Modifier.size(24.dp),
                                    colorFilter = ColorFilter.tint(Color(0xFFd8af84)),
                                )
                            }
                        }
                        Text("Cooked", color = Color(0xFFd8af84), modifier = Modifier.alpha(if (uiState.activeTab == "cooked") 1f else 0.5f))
                    }
                }
                Surface(
                    color = Color.Transparent,
                    modifier = Modifier
                        .fillMaxSize()
                        .weight(1f)
                        .clickable(onClick = { profileScreenViewModel.setActiveTab("reviews") })
                        .background(
                            brush = Brush.horizontalGradient(
                                listOf(
                                    Color(0xFF682300),
                                    Color(0xFF682300)
                                )
                            ), alpha = if (uiState.activeTab == "reviews") 0.75f else 1f
                        ),
                ) {
                    Column(
                        modifier = Modifier.fillMaxSize(),
                        horizontalAlignment = Alignment.CenterHorizontally,
                        verticalArrangement = Arrangement.SpaceEvenly
                    ) {

                        //Favorite button
                        Surface(
                            elevation = 8.dp,
                            modifier = Modifier
                                .border(
                                    width = 2.dp,
                                    brush = (Brush.horizontalGradient(
                                        colors = if (uiState.activeTab == "reviews") fullColors else fadedColors,
                                        tileMode = TileMode.Mirror
                                    )),
                                    shape = CircleShape
                                )
                                .size(36.dp)
                                .alpha(if (uiState.activeTab == "reviews") 1f else 0.5f),
                            shape = CircleShape,
                            color = Color(0xFF682300)
                        ) {
                            Box(contentAlignment = Alignment.Center) {
                                Icon(
                                    Icons.Outlined.Reviews,
                                    tint = Color(0xFFd8af84),
                                    modifier = Modifier.size(20.dp),
                                    // modifier = Modifier.background(color = Color(0xFFFFFFFF)),
                                    contentDescription = null
                                )
                            }
                        }
                        Text("Reviews", color = Color(0xFFd8af84), modifier = Modifier.alpha(if (uiState.activeTab == "reviews") 1f else 0.5f))
                    }
                }
            }

            //Favorites List
            if (uiState.activeTab == "favorites")
                Surface(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(bottom = 48.dp),
                    color = Color(0xFFd8af84)

                ) {

                    LazyColumn() {
                        items(favoritesData, key = { it.recipeEntity.recipeName }) {
                            androidx.compose.animation.AnimatedVisibility(
                                visible = it.recipeEntity.isFavorite == 1,
                                enter = EnterTransition.None,
                                exit = ExitTransition.None,
                            ) {
                                RecipeCard(
                                    modifier = Modifier

                                        .animateEnterExit(
                                            enter = EnterTransition.None,
                                            exit = scaleOut(
                                                animationSpec = TweenSpec(250, 0, FastOutLinearInEasing)
                                            )
                                        ),
//                                .animateItemPlacement(animationSpec = (TweenSpec(150, delay = 0))),
                                    recipeWithIngredients = it,
                                    currentScreen = "FavoritesTab",
                                    onFavoriteClick =
                                    {
                                        /*
                                        should trigger are you sure dialogue
                                         */
                                        profileScreenViewModel.triggerRemoveFavoriteAlert(it)

//                                        profileScreenViewModel.toggleFavorite(it)
//                                        onFavoriteClick(it)
                                    },
                                    onRemoveClick = {},
                                    onCompleteClick = {},
                                    onDetailsClick = {
//                                coroutineScope.launch(Dispatchers.IO) {
                                        profileScreenViewModel.setDetailsScreenTarget(it.recipeEntity.recipeName);
                                        onDetailsClick()
//                                }
                                    }
                                )
                            }
                        }

                        item() {
                            Spacer(
                                Modifier
                                    .fillMaxWidth()
                                    .padding(16.dp)
                            )
                        }
                    }

                    Box(Modifier.fillMaxSize()){

                        //Remove Alert
                        if(uiAlertState.showRemoveFavoriteAlert){
                            AlertDialog(
                                onDismissRequest = {},
                                text = {
                                    Text(text = "Are you sure you want to remove " + uiAlertState.recipe.recipeEntity.recipeName +
                                            " from your Favorites?" )
                                },
                                confirmButton = {
                                    TextButton(
                                        onClick = {
                                            onRemoveClick(uiAlertState.recipe)
                                            profileScreenViewModel.removeFavorite(uiAlertState.recipe)
                                            profileScreenViewModel.cancelRemoveAlert()
                                        }
                                    ) {
                                        Text("Yes")
                                    }
                                },
                                dismissButton = {
                                    TextButton(
                                        onClick = {
                                            profileScreenViewModel.cancelRemoveAlert()
                                        }
                                    ) {
                                        Text("Cancel")
                                    }
                                }
                            )
                        }
                    }
                }

            //Cooked list
            else if (uiState.activeTab == "cooked")
            {
                Surface(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(bottom = 48.dp),
                    color = Color(0xFFd8af84)

                ) {

                    LazyVerticalGrid(
                        columns = GridCells.Fixed(2),
                        modifier = Modifier
                            .padding(top = 0.dp, start = 16.dp, end = 16.dp, bottom = 0.dp),
                        horizontalArrangement = Arrangement.spacedBy(0.dp)
                    ) {
                        items(cookedData.size, key = { it }) { index ->
                            androidx.compose.animation.AnimatedVisibility(
                                visible = cookedData[index].recipeEntity.cookedCount > 0,
                                enter = EnterTransition.None,
                                exit = ExitTransition.None,
                            ) {
                                RecipeIcon(
                                    recipeWithIngredients = cookedData[index],
                                    onDetailsClick = {
                                        profileScreenViewModel.setDetailsScreenTarget(cookedData[index].recipeEntity.recipeName);
                                        onDetailsClick()
                                    })
                            }
                        }

//                        item() {
//                            Spacer(
//                                Modifier
//                                    .fillMaxWidth()
//                                    .padding(16.dp)
//                            )
//                        }
                    }

                    Box(Modifier.fillMaxSize()){

                        //Remove Alert
                        if(uiAlertState.showRemoveFavoriteAlert){
                            AlertDialog(
                                onDismissRequest = {},
                                text = {
                                    Text(text = "Are you sure you want to remove " + uiAlertState.recipe.recipeEntity.recipeName +
                                            " from your Favorites?" )
                                },
                                confirmButton = {
                                    TextButton(
                                        onClick = {
                                            onRemoveClick(uiAlertState.recipe)
                                            profileScreenViewModel.removeFavorite(uiAlertState.recipe)
                                            profileScreenViewModel.cancelRemoveAlert()
                                        }
                                    ) {
                                        Text("Yes")
                                    }
                                },
                                dismissButton = {
                                    TextButton(
                                        onClick = {
                                            profileScreenViewModel.cancelRemoveAlert()
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
            else if (uiState.activeTab == "reviews")
                LazyColumn() {

                }
        }
    }
}


class ProfileScreenViewModelFactory(val application: Application) : ViewModelProvider.Factory
{
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return ProfileScreenViewModel(application) as T
    }
}

@Composable
fun RecipeIcon(
    recipeWithIngredients: RecipeWithIngredients,
    onDetailsClick: () -> Unit
){


    var image = R.drawable.bagel

    image = when(recipeWithIngredients.recipeEntity.recipeName){
        "Bagels" -> R.drawable.bagel2
        "Garlic Knots" -> R.drawable.garlic2
        "Cauliflower Walnut Tacos" -> R.drawable.cauliflower
        "Lentil Sweet Potato Curry" -> R.drawable.curry
        "Thai Style Peanut Soup" -> R.drawable.thaisoup
        "Yummy Rice with Marinated Tofu" -> R.drawable.yummyrice
        "Corn Chowder" -> R.drawable.cornchowder
        "Vegan Eggplant Parmesan" -> R.drawable.eggplant
        "Mexican Style Rice" -> R.drawable.mexicanrice
        "Wild Rice Salad" -> R.drawable.wildrice
        "Rice Soup" -> R.drawable.ricesoup
        else -> R.drawable.bagel
    }

    Column(
        Modifier
            .fillMaxWidth(),
        horizontalAlignment = Alignment.CenterHorizontally)
    {
        Surface(
            modifier = Modifier
                .size(120.dp)
                .padding(top = 8.dp, bottom = 4.dp)
                .align(Alignment.CenterHorizontally)
                .clickable(onClick = onDetailsClick),
            shape = RoundedCornerShape(15.dp),
            elevation = 6.dp,

            ){
            AsyncImage(
                model = image,
                contentDescription = null,
                modifier = Modifier.fillMaxSize(),
                contentScale = ContentScale.Crop,
            )
            Column(
                modifier = Modifier
                    .fillMaxSize(),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center
            ){
                Text(
                    text = recipeWithIngredients.recipeEntity.recipeName,
                    fontSize = 20.sp,
                    color = Color(0xFFFFFFFF),
                    textAlign = TextAlign.Center,
                    style = MaterialTheme.typography.h4.copy(
                        shadow = Shadow(
                            color = Color.Black,
                            offset = Offset(4f, 4f),
                            blurRadius = 8f
                        )
                    )
                )
            }
        }

        Spacer(
            Modifier
                .size(2.dp)
                .fillMaxWidth())


        Surface(
            modifier = Modifier
                // .padding(top = 4.dp)
                .wrapContentSize(),
            //               .background(
//                    brush = Brush.horizontalGradient(
//                        colors = listOf(Color(0xFF682300), Color(0xFFb15f33)),
//                        endX = gradientWidth,
//                        tileMode = TileMode.Mirror
//                    ),
//                    shape = RoundedCornerShape((25.dp))
//                ),
            //   .clickable(enabled = !selected) { selected = !selected },
            shape = RoundedCornerShape(25.dp),
            color = Color(0xFF682300),
            elevation = 6.dp,
            //color = Color(0xFF682300),//Color(0xFFd8af84),
            contentColor = Color(0xFFd8af84),
        ){
            Text(
                text = "Times Cooked: ${recipeWithIngredients.recipeEntity.cookedCount}",
                modifier = Modifier
                    .padding(start = 8.dp, end = 8.dp, top = 8.dp, bottom = 8.dp)
                    .background(color = Color.Transparent),
                color = Color(0xFFd8af84),
                fontSize = 16.sp,
                textAlign = TextAlign.Center,

                )
        }

        Spacer(
            Modifier
                .size(16.dp)
                .fillMaxWidth())
    }

}

@Preview
@Composable
fun ProfilePreview(){
    BearRecipeBookAppTheme {
//        ProfileScreen()
    }
}