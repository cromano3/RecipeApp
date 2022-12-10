package com.example.bearrecipebookapp

import android.annotation.SuppressLint
import android.app.Application
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.CutCornerShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.TileMode
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.text.ExperimentalTextApi
import androidx.compose.ui.text.PlatformTextStyle
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.LineHeightStyle
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.em
import androidx.compose.ui.unit.sp
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewmodel.compose.LocalViewModelStoreOwner
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.example.bearrecipebookapp.datamodel.RecipeWithIngredientsAndInstructions
import com.example.bearrecipebookapp.ui.*
import com.example.bearrecipebookapp.ui.theme.BearRecipeBookAppTheme
import com.example.bearrecipebookapp.ui.theme.Cabin
import com.example.bearrecipebookapp.viewmodel.TopBarViewModel
import kotlinx.coroutines.launch


class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            BearRecipeBookAppTheme {
                // A surface container using the 'background' color from the theme
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colors.background
                ){
                        BearRecipeApp()


                }
            }
        }
    }
}

@SuppressLint("UnusedMaterialScaffoldPaddingParameter")
@Composable
fun BearRecipeApp(
){

    val navController: NavHostController = rememberNavController()
    val currentBackStackEntry by navController.currentBackStackEntryAsState()

    val currentScreen = currentBackStackEntry?.destination?.route ?: "RecipeScreen"

    val coroutineScope = rememberCoroutineScope()
    val scaffoldState = rememberScaffoldState()


    Scaffold(
        scaffoldState = scaffoldState,
        topBar = { BearAppTopBar(
            currentScreen = currentScreen,
            navController = navController,
            onHomeClick = {},
            onBackClick = { navController.popBackStack() },
            onProfileClick = { navController.navigate("ProfileScreen") },
            onSearchClick = { navController.navigate("SearchScreen") },
            onFavoriteClick =
            {coroutineScope.launch{
                if(it.recipeEntity.isFavorite == 1)
                    scaffoldState.snackbarHostState.showSnackbar(
                        message = "Removed " + it.recipeEntity.recipeName + " from Favorites.",
                        duration = SnackbarDuration.Short)
                else if(it.recipeEntity.isFavorite == 0)
                    scaffoldState.snackbarHostState.showSnackbar(
                        message = "Added " + it.recipeEntity.recipeName + " to Favorites.",
                        duration = SnackbarDuration.Short)
            }},
        ) },
        bottomBar = { BearAppBottomBar(navController = navController) },

        snackbarHost = {
            SnackbarHost(it) { data ->
                Snackbar(
                    modifier = Modifier.padding(8.dp),
                    shape = CutCornerShape(0.dp),
                    backgroundColor = Color(0xFF000000),
                )
                {
                    Text(
                        text = data.message,
                        color = Color(0xFFFFFFFF)
                    )
                }
            }
//                modifier = Modifier.align(Alignment.BottomCenter),
//                hostState = snackbarHostState,

        }
    ){
        NavHost(
            navController = navController,
            startDestination = "RecipeScreen",
        ){
            composable(route = "ProfileScreen"){
                ProfileScreen(
                    onDetailsClick = { navController.navigate("DetailsScreen")},
                    onRemoveClick = {coroutineScope.launch{
                    if(it.recipeEntity.isFavorite == 1)
                        scaffoldState.snackbarHostState.showSnackbar(
                            message = "Removed " + it.recipeEntity.recipeName + " from Favorites.",
                            duration = SnackbarDuration.Short)
                    else if(it.recipeEntity.isFavorite == 0)
                        scaffoldState.snackbarHostState.showSnackbar(
                            message = "Added " + it.recipeEntity.recipeName + " to Favorites.",
                            duration = SnackbarDuration.Short)
                }},)
            }

            composable(route = "RecipeScreen"){
                //Recipe Book Main Screen
                HomeScreen(
//                    onSearchClick = {navController.navigate("SearchScreen")},
                    onDetailsClick = {
                        navController.navigate("DetailsScreen",
//                            navOptions { popUpTo("DetailsScreen") { inclusive = true } }
                        ) },
                    onFavoriteClick = {coroutineScope.launch{
                        if(it.recipeEntity.isFavorite == 1)
                            scaffoldState.snackbarHostState.showSnackbar(
                                message = "Removed " + it.recipeEntity.recipeName + " from Favorites.",
                                duration = SnackbarDuration.Short)
                        else if(it.recipeEntity.isFavorite == 0)
                            scaffoldState.snackbarHostState.showSnackbar(
                                message = "Added " + it.recipeEntity.recipeName + " to Favorites.",
                                duration = SnackbarDuration.Short)
                    }},
                    onMenuClick = {coroutineScope.launch {
                        scaffoldState.snackbarHostState.showSnackbar(
                            message = "Added " + it.recipeEntity.recipeName + " to the Menu.",
                            duration = SnackbarDuration.Short
                        )
                    }},
                    onMenuRemovedClick = {coroutineScope.launch {
                        scaffoldState.snackbarHostState.showSnackbar(
                            message = "Removed " + it.recipeEntity.recipeName + " from the Menu.",
                            duration = SnackbarDuration.Short
                        )
                    }},
                )
            }

            composable(route = "WeeklyMenuScreen"){
                //Weekly menu screen
                MenuScreen(
                    onDetailsClick = { navController.navigate("DetailsScreen")},
                    onFavoriteClick = {coroutineScope.launch{
                        if(it.recipeEntity.isFavorite == 1)
                            scaffoldState.snackbarHostState.showSnackbar(
                                message = "Removed " + it.recipeEntity.recipeName + " from Favorites.",
                                duration = SnackbarDuration.Short)
                        else if(it.recipeEntity.isFavorite == 0)
                            scaffoldState.snackbarHostState.showSnackbar(
                                message = "Added " + it.recipeEntity.recipeName + " to Favorites.",
                                duration = SnackbarDuration.Short)
                    }},
                    onRemoveClick = {coroutineScope.launch{
                            scaffoldState.snackbarHostState.showSnackbar(
                                message = "Removed " + it.recipeEntity.recipeName + " from the Menu.",
                                duration = SnackbarDuration.Short)

                    }},
                    onCompleteClick = {coroutineScope.launch{
                            scaffoldState.snackbarHostState.showSnackbar(
                                message = "Completed cooking " + it.recipeEntity.recipeName + "!!",
                                duration = SnackbarDuration.Short)
                    }},
                    onSystemBackClick = {
                        navController.navigate("RecipeScreen"){

                            popUpTo(navController.graph.findStartDestination().id)
                            restoreState = true

                        }
                    }
                )
            }

            composable(route = "ShoppingScreen"){
                ShoppingListScreen(
                    onDetailsClick = { navController.navigate("DetailsScreen") },
                    onSystemBackClick = {
                        navController.navigate("RecipeScreen"){

                            popUpTo(navController.graph.findStartDestination().id)
//                            {
//                                saveState = true
//                            }
//                            launchSingleTop = true
                            restoreState = true

                        }
                    }
                )

            }

            composable(route = "DetailsScreen") {

                NewDetailsScreen(
                    onGoBackClick = { navController.popBackStack() },
                    onMenuAddClick = {coroutineScope.launch {
                        scaffoldState.snackbarHostState.showSnackbar(
                            message = "Added " + it.recipeEntity.recipeName + " to the Menu.",
                            duration = SnackbarDuration.Short
                        )
                    }},
                    onMenuRemoveClick = {coroutineScope.launch {
                        scaffoldState.snackbarHostState.showSnackbar(
                            message = "Removed " + it.recipeEntity.recipeName + " from the Menu.",
                            duration = SnackbarDuration.Short
                        )
                    }},
                    onFavoriteClick = {coroutineScope.launch{
                        if(it.recipeEntity.isFavorite == 1)
                            scaffoldState.snackbarHostState.showSnackbar(
                                message = "Removed " + it.recipeEntity.recipeName + " from Favorites.",
                                duration = SnackbarDuration.Short)
                        else if(it.recipeEntity.isFavorite == 0)
                            scaffoldState.snackbarHostState.showSnackbar(
                                message = "Added " + it.recipeEntity.recipeName + " to Favorites.",
                                duration = SnackbarDuration.Short)
                    }},
                    onCompleteClick = {coroutineScope.launch{
                        scaffoldState.snackbarHostState.showSnackbar(
                            message = "Completed cooking " + it.recipeEntity.recipeName + "!!",
                            duration = SnackbarDuration.Short)
                    }}
                )
            }

            composable(route = "SearchScreen"){
                SearchScreen(
                    onGoBackClick = { navController.popBackStack() },
                    onDetailsClick = { navController.navigate("DetailsScreen") },
                    onFavoriteClick = {coroutineScope.launch{
                        if(it.isFavorite == 1)
                            scaffoldState.snackbarHostState.showSnackbar(
                                message = "Removed " + it.recipeName + " from Favorites.",
                                duration = SnackbarDuration.Short)
                        else if(it.isFavorite == 0)
                            scaffoldState.snackbarHostState.showSnackbar(
                                message = "Added " + it.recipeName + " to Favorites.",
                                duration = SnackbarDuration.Short)
                    }},
                    onMenuClick = {coroutineScope.launch{
                        if(it.onMenu == 1)
                            scaffoldState.snackbarHostState.showSnackbar(
                                message = "Removed " + it.recipeName + " from Menu.",
                                duration = SnackbarDuration.Short)
                        else if(it.onMenu == 0)
                            scaffoldState.snackbarHostState.showSnackbar(
                                message = "Added " + it.recipeName + " to Menu.",
                                duration = SnackbarDuration.Short)
                    }},
                )

            }


            /** How to pass a string along with the Nav controller to its destination.
             * This was ultimately not used as the variable is now written to the database.
             */
//            composable(route = "DetailsScreen/{recipeName}"){ it ->
//                val recipeName = it.arguments?.getString("recipeName")
//                recipeName?.let{NewDetailsScreen(recipeName = recipeName, onGoBackClick = {})}

        }
    }
}


@Composable
fun BearAppBottomBar(
    navController: NavHostController,
)
{
    BottomNavigation(
        backgroundColor = Color(0xFF682300),
        contentColor = Color(0xFFd8af84)
    ) {

        val backStackEntry by navController.currentBackStackEntryAsState()
        val currentRoute = backStackEntry?.destination?.route
        val queue = navController.backQueue

        val routes = listOf("RecipeScreen", "WeeklyMenuScreen", "ShoppingScreen")

        var selected = false
        var alpha = 1f


        routes.forEach{ it ->
            if(it == currentRoute){
                selected = true
                alpha = 1f
            }
            else{
                selected = false
                alpha = 0.5f
            }
            BottomNavigationItem(
                selected = selected
//                (it == currentRoute)
                ,
                onClick = {

                    if(currentRoute == "SearchScreen" && it == "RecipeScreen"){
                        navController.popBackStack()
                    }
                    if(currentRoute == "DetailsScreen"){
                        navController.popBackStack()
                    }
                    if(currentRoute == "ProfileScreen"){
                        navController.popBackStack()
                    }

                      navController.navigate(it){

                          popUpTo(navController.graph.findStartDestination().id) {
                              saveState = true
                          }
                          launchSingleTop = true
                          restoreState = true

                      }
                },
                icon = {
                    when(it){
                        "RecipeScreen" -> Icon(Icons.Outlined.MenuBook, contentDescription = null)
                        "WeeklyMenuScreen" -> Icon(Icons.Outlined.Restaurant, contentDescription = null)
                        "ShoppingScreen" -> Icon(Icons.Outlined.ShoppingCart, contentDescription = null)
                    }
                },
                modifier = Modifier
                    .alpha(alpha)
                    .background(
                        brush = Brush.horizontalGradient(
                            listOf(
                                Color(0xFFFFFFFF),
                                Color(0xFFFFFFFF)
                            )
                        ), alpha = 0.1f
                    )

            )
        }

    }
}

@OptIn(ExperimentalTextApi::class)
@Composable
fun BearAppTopBar(
    currentScreen: String,
    navController: NavHostController,
    onHomeClick: () -> Unit,
    onBackClick: () -> Unit,
    onProfileClick: () -> Unit,
    onSearchClick: () -> Unit,
    onFavoriteClick: (RecipeWithIngredientsAndInstructions) -> Unit,
    ) {

    val owner = LocalViewModelStoreOwner.current

    owner?.let { viewModelStoreOwner ->
        val topBarViewModel: TopBarViewModel = viewModel(
            viewModelStoreOwner,
            "TopBarViewModel",
            TopBarViewModelFactory(
                LocalContext.current.applicationContext
                        as Application,
            )
        )

        var title = ""
        var show = false
        var showShare = false
        var showSearch = false
        var icon = Icons.Outlined.Home
        var icon2 = Icons.Outlined.Home
        var clickEffectLeft = onHomeClick
        var clickEffectRight = onHomeClick
        var textModifier = Modifier.wrapContentWidth()

        val detailsScreenData by topBarViewModel.detailsScreenData.observeAsState(
            RecipeWithIngredientsAndInstructions()
        )

        when (currentScreen) {
            "RecipeScreen" -> {
                show = true
                textModifier = Modifier.wrapContentWidth()
                icon = Icons.Outlined.Home
                icon2 = Icons.Outlined.Person
                clickEffectLeft = onHomeClick
                clickEffectRight = onProfileClick
                showSearch = true
                showShare = false
            }
            "WeeklyMenuScreen" -> {
                show = true
                textModifier = Modifier.wrapContentWidth()
                title = "My Menu"
                icon = Icons.Outlined.Home
                icon2 = Icons.Outlined.Person
                clickEffectLeft = onHomeClick
                clickEffectRight = onProfileClick
                showSearch = false
                showShare = false
            }
            "ShoppingScreen" -> {
                show = true
                textModifier = Modifier.wrapContentWidth()
                title = "Shopping list"
                icon = Icons.Outlined.Home
                icon2 = Icons.Outlined.Person
                clickEffectLeft = onHomeClick
                clickEffectRight = onProfileClick
                showSearch = false
                showShare = false
            }
            "SearchScreen" -> {
                show = false
            }
            "DetailsScreen" -> {
                show = true

                title = detailsScreenData.recipeEntity.recipeName
                textModifier = Modifier.width(200.dp)
                icon = Icons.Outlined.ArrowBack

                clickEffectLeft = onBackClick
                clickEffectRight = {
                    topBarViewModel.toggleFavorite(detailsScreenData)
                    onFavoriteClick(detailsScreenData)
                }
                showSearch = false
                showShare = true

                icon2 = if (detailsScreenData.recipeEntity.isFavorite == 0) {
                    Icons.Outlined.FavoriteBorder

                } else {
                    Icons.Outlined.Favorite
                }
            }
        }

        if (show) {

            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(64.dp)
                    .background(Color(0xFF682300)),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.Start
            ) {
                if(showSearch){
                    Surface(
                        Modifier
                            .height(56.dp)
                            .clickable(onClick = onSearchClick)
                            .padding(start = 8.dp, top = 8.dp, bottom = 8.dp)
                            .border(
                                width = 2.dp,
                                brush = (Brush.horizontalGradient(
                                    colors = listOf(Color(0xFFd8af84), Color(0xFFb15f33)),
                                    tileMode = TileMode.Mirror
                                )),
                                shape = RoundedCornerShape(25.dp)
                            ),
//                        color = Color(0xFF682300),
                        shape = RoundedCornerShape(25.dp)
                    ){
                        TextField(
                            value = "",
                            onValueChange = {},
                            modifier = Modifier
//                                .alpha(.8f)
                            ,
                            enabled = false,
                            readOnly = true,
                            leadingIcon = { Icon(Icons.Outlined.Search, contentDescription = null, tint = Color(0xFF000000)) },
                            shape = RoundedCornerShape(25.dp),
                            colors = TextFieldDefaults.textFieldColors(backgroundColor = Color(0xFFd8af84), textColor = Color(0xFF000000))
                        )
                    }
                }
                else{
                    IconButton(
                        onClick = { clickEffectLeft() },
                        modifier =
                        Modifier
                            .size(48.dp)
                            .align(Alignment.CenterVertically)
                            .background(color = Color.Transparent),
                        // color = Color.Transparent

                    ) {
                        Icon(
                            imageVector = icon,
                            contentDescription = null,
                            modifier = Modifier
                                .size(24.dp),
                            // .padding(start = 16.dp)
                            // .clickable(onClick = {onGoBackClick(detailsScreenData)}),
                            tint = Color(0xFFd8af84)
                        )
                    }
                }

                Spacer(Modifier.weight(1f))

                Text(
                    text = title,
                    modifier = textModifier,
                    //  .weight(1f),
                    color = Color(0xFFd8af84),
                    textAlign = TextAlign.Center,
                    fontSize = 22.sp,
                    fontFamily = Cabin,
                    fontWeight = FontWeight.Bold,
                    lineHeight = 1.0.em,
                    style = MaterialTheme.typography.h4.merge(
                        TextStyle(
                            platformStyle = PlatformTextStyle(
                                includeFontPadding = false
                            ),
                            lineHeightStyle = LineHeightStyle(
                                alignment = LineHeightStyle.Alignment.Top,
                                trim = LineHeightStyle.Trim.FirstLineTop
                            )
                        )
                    ),
                )

                Spacer(Modifier.weight(1f))

                if(showShare){
                    Icon(
                        imageVector = Icons.Outlined.Share,
                        contentDescription = null,
                        modifier = Modifier
                            .size(24.dp),
                        tint = Color(0xFFd8af84)
                    )
                }

                Spacer(Modifier.size(16.dp))

                val gradientWidthButton = with(LocalDensity.current) { 48.dp.toPx() }

                FloatingActionButton(
                    onClick = {
                        clickEffectRight()
                    },
                    elevation = FloatingActionButtonDefaults.elevation(8.dp),
                    modifier = Modifier
                        .padding(end = 8.dp)
                        .border(
                            width = 2.dp,
                            brush = (Brush.horizontalGradient(
                                colors = listOf(
                                    Color(0xFFd8af84),
                                    Color(0xFFb15f33),

                                    ),
                                endX = gradientWidthButton,
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
                        icon2,
                        tint = Color(0xFFd8af84),
                        modifier = Modifier.size(20.dp),
                        // modifier = Modifier.background(color = Color(0xFFFFFFFF)),
                        contentDescription = null
                    )
                }
                Spacer(Modifier.size(8.dp))
            }
        }
    }
}

class TopBarViewModelFactory(
    val application: Application,
) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {

        return TopBarViewModel(
            application,
        ) as T
    }
}

//
//@Preview(showBackground = true, showSystemUi = true)
//@Composable
//fun DefaultPreview() {
//    BearAppTopBar(menuCount = 2, onClick = {}, showTopBar = false, newDetailsScreenTarget = RecipeWithIngredientsAndInstructions(),
//        onGoBackClick = {},
//        onUpdateMenuClick = {}
//    )
//}
