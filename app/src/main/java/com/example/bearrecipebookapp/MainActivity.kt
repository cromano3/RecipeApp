package com.example.bearrecipebookapp

import android.annotation.SuppressLint
import android.app.Activity
import android.app.Application
import android.content.Intent
import android.content.pm.ActivityInfo
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.animation.AnimatedContentScope
import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.CutCornerShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.*
import androidx.compose.runtime.*
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.TileMode
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.text.PlatformTextStyle
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.text.input.VisualTransformation
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
import androidx.navigation.compose.currentBackStackEntryAsState
import com.example.bearrecipebookapp.datamodel.RecipeWithIngredientsAndInstructions
import com.example.bearrecipebookapp.ui.*
import com.example.bearrecipebookapp.ui.theme.BearRecipeBookAppTheme
import com.example.bearrecipebookapp.ui.theme.Cabin
import com.example.bearrecipebookapp.viewmodel.TopBarViewModel
import com.google.accompanist.navigation.animation.AnimatedNavHost
import com.google.accompanist.navigation.animation.composable
import com.google.accompanist.navigation.animation.rememberAnimatedNavController
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

@OptIn(ExperimentalAnimationApi::class)
@SuppressLint("UnusedMaterialScaffoldPaddingParameter")
@Composable
fun BearRecipeApp(
){

    val navController: NavHostController = rememberAnimatedNavController()
    val currentBackStackEntry by navController.currentBackStackEntryAsState()

    val currentScreen = currentBackStackEntry?.destination?.route ?: "RecipeScreen"

    val coroutineScope = rememberCoroutineScope()
    val scaffoldState = rememberScaffoldState()

    val context = LocalContext.current

    (context as? Activity)?.requestedOrientation = ActivityInfo.SCREEN_ORIENTATION_PORTRAIT

    Scaffold(
        scaffoldState = scaffoldState,
        topBar = { BearAppTopBar(
            currentScreen = currentScreen,
            navController = navController,
            onHomeClick = {
                if(currentScreen == "ProfileScreen"){
                    navController.popBackStack()
                }
                navController.navigate("RecipeScreen"){
                    popUpTo(navController.graph.findStartDestination().id) {
                        saveState = true
                    }
                    launchSingleTop = true
                    restoreState = true

                }
            },
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
        AnimatedNavHost(
            navController = navController,
            startDestination = "RecipeScreen",
//            exitTransition = {fadeOut(animationSpec = tween(700))},
        ){
            composable(
                route = "ProfileScreen",
                enterTransition = {
                    fadeIn(animationSpec = tween(700))
                },
                exitTransition = {
                    fadeOut(animationSpec = tween(700))

                },
            ){
                ProfileScreen(
                    onDetailsClick = { navController.navigate("DetailsScreen") },
//                    { navController.navigate("DetailsScreen"){ popUpTo("ProfileScreen"){ inclusive = true } } },
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

            /**Comment Screen*/
            composable(
                route = "CommentScreen",
                enterTransition = {
                    fadeIn(animationSpec = tween(700))
                },
                exitTransition = {
                    fadeOut(animationSpec = tween(700))

                }
                ,
            ){
                CommentScreen(
                    onCancelClick = { navController.popBackStack() },
                    onConfirmClick = { navController.popBackStack() },
                )
            }

            composable(route = "AddRecipeScreen"){ AddRecipeScreen() }


            /** Home Screen */

            composable(route = "RecipeScreen",
                enterTransition = {
                    when (initialState.destination.route){
                        "WeeklyMenuScreen" -> slideIntoContainer(AnimatedContentScope.SlideDirection.Right, animationSpec = tween(700))
                        "ShoppingScreen" -> slideIntoContainer(AnimatedContentScope.SlideDirection.Right, animationSpec = tween(700))
                        "SearchScreen" -> slideIntoContainer(AnimatedContentScope.SlideDirection.Up, animationSpec = tween(700))
                        "DetailsScreen" -> fadeIn(animationSpec = tween(700))
                        "ProfileScreen" -> fadeIn(animationSpec = tween(700))
                        else -> fadeIn(animationSpec = tween(700))
                    }
                },
                exitTransition = {
                    when (targetState.destination.route) {
                        "WeeklyMenuScreen" -> slideOutOfContainer(AnimatedContentScope.SlideDirection.Left, animationSpec = tween(700))
                        "ShoppingScreen" -> slideOutOfContainer(AnimatedContentScope.SlideDirection.Left, animationSpec = tween(700))
                        "SearchScreen" -> slideOutOfContainer(AnimatedContentScope.SlideDirection.Down, animationSpec = tween(700))
                        "DetailsScreen" -> fadeOut(animationSpec = tween(700))
                        "ProfileScreen" -> fadeOut(animationSpec = tween(700))
                        "CommentScreen" -> fadeOut(animationSpec = tween(700))
                        "AddRecipeScreen" -> fadeOut(animationSpec = tween(700))
                        else -> fadeOut(animationSpec = tween(700))
                    }
                }
            ){

//                val triggerTutorialAlert = it.arguments?.getString("triggerTutorialAlert")

                //Recipe Book Main Screen
                HomeScreen(
//                    triggerTutorialAlert = triggerTutorialAlert ?: "false",
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
                    onCreateRecipeClick = {navController.navigate("AddRecipeScreen")}
                )
            }

            composable(route = "WeeklyMenuScreen",
                enterTransition = {
                    when (initialState.destination.route){
                        "RecipeScreen" -> slideIntoContainer(AnimatedContentScope.SlideDirection.Left, animationSpec = tween(700))
                        "ShoppingScreen" -> slideIntoContainer(AnimatedContentScope.SlideDirection.Right, animationSpec = tween(700))
                        "SearchScreen" -> fadeIn(animationSpec = tween(700))
                        "DetailsScreen" -> fadeIn(animationSpec = tween(700))
                        "ProfileScreen" -> fadeIn(animationSpec = tween(700))
                        else -> null
                    }
                },
                exitTransition = {
                    when (targetState.destination.route) {
                        "RecipeScreen" -> slideOutOfContainer(AnimatedContentScope.SlideDirection.Right, animationSpec = tween(700))
                        "ShoppingScreen" -> slideOutOfContainer(AnimatedContentScope.SlideDirection.Left, animationSpec = tween(700))
                        "SearchScreen" -> fadeOut(animationSpec = tween(700))
                        "DetailsScreen" -> fadeOut(animationSpec = tween(700))
                        "ProfileScreen" -> fadeOut(animationSpec = tween(700))
                        else -> null
                    }
                },
            ){
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
                    onAddedToFavoriteFromAlertClick = {coroutineScope.launch{
                            scaffoldState.snackbarHostState.showSnackbar(
                                message = "Added $it to Favorites.",
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
                    },
                    onConfirmWriteReviewClick = { navController.navigate("CommentScreen") },
                    onAddRecipeClick = {

                        navController.navigate("RecipeScreen"){

                            popUpTo(navController.graph.findStartDestination().id) {
                                saveState = true
                            }
                            launchSingleTop = true
                            restoreState = true

                        }

                        val backStackEntry = navController.currentBackStackEntry
                        val currentRoute = backStackEntry?.destination?.route

                        if(currentRoute == "SearchScreen") {
                            navController.popBackStack()
                        }

                    }
                )
            }

            composable(
                route = "ShoppingScreen",
                enterTransition = {
                    when (initialState.destination.route){
                        "RecipeScreen" -> slideIntoContainer(AnimatedContentScope.SlideDirection.Left, animationSpec = tween(700))
                        "WeeklyMenuScreen" -> slideIntoContainer(AnimatedContentScope.SlideDirection.Left, animationSpec = tween(700))
                        "SearchScreen" -> fadeIn(animationSpec = tween(700))
                        "DetailsScreen" -> fadeIn(animationSpec = tween(700))
                        "ProfileScreen" -> fadeIn(animationSpec = tween(700))
                        else -> null
                    }
                },
                exitTransition = {
                    when (targetState.destination.route) {
                        "RecipeScreen" -> slideOutOfContainer(AnimatedContentScope.SlideDirection.Right, animationSpec = tween(700))
                        "WeeklyMenuScreen" -> slideOutOfContainer(AnimatedContentScope.SlideDirection.Right, animationSpec = tween(700))
                        "SearchScreen" -> fadeOut(animationSpec = tween(700))
                        "DetailsScreen" -> fadeOut(animationSpec = tween(700))
                        "ProfileScreen" -> fadeOut(animationSpec = tween(700))
                        else -> null
                    }
                },){
                ShoppingListScreen(
                    onDetailsClick = { navController.navigate("DetailsScreen") },
                    onSystemBackClick = {
                        navController.navigate("RecipeScreen"){
                            popUpTo(navController.graph.findStartDestination().id)
                            restoreState = true
                        }
                    },
                    onAddRecipeClick = {

                        navController.navigate("RecipeScreen"){

                            popUpTo(navController.graph.findStartDestination().id) {
                                saveState = true
                            }
                            launchSingleTop = true
                            restoreState = true

                        }

                        val backStackEntry = navController.currentBackStackEntry
                        val currentRoute = backStackEntry?.destination?.route

                        if(currentRoute == "SearchScreen") {
                            navController.popBackStack()
                        }

                    }
                )
            }

            composable(
                route = "DetailsScreen",
                enterTransition = { fadeIn(animationSpec = tween(700))
//                    when (initialState.destination.route){
//                        "RecipeScreen" -> fadeIn(animationSpec = tween(700))
//                        "WeeklyMenuScreen" -> fadeIn(animationSpec = tween(700))
//                        "SearchScreen" -> fadeIn(animationSpec = tween(700))
//                        "ShoppingScreen" -> fadeIn(animationSpec = tween(700))
//                        "ProfileScreen" -> fadeIn(animationSpec = tween(700))
//                        else -> null
//                    }
                },
                exitTransition = {
                    fadeOut(animationSpec = tween(700));
//                    when (targetState.destination.route) {
//                        "RecipeScreen" -> fadeOut(animationSpec = tween(700))
//                        "WeeklyMenuScreen" -> fadeOut(animationSpec = tween(700))
//                        "SearchScreen" -> fadeOut(animationSpec = tween(700))
//                        "ShoppingScreen" -> fadeOut(animationSpec = tween(700))
//                        "ProfileScreen" -> fadeOut(animationSpec = tween(700))
//                        else -> null
//                    }
                },) {
                    NewDetailsScreen(
                        onGoBackClick = { navController.popBackStack() },
                        onMenuAddClick = {
                            coroutineScope.launch {
                                scaffoldState.snackbarHostState.showSnackbar(
                                    message = "Added " + it.recipeEntity.recipeName + " to the Menu.",
                                    duration = SnackbarDuration.Short
                                )
                            }
                        },
                        onMenuRemoveClick = {
                            coroutineScope.launch {
                                scaffoldState.snackbarHostState.showSnackbar(
                                    message = "Removed " + it.recipeEntity.recipeName + " from the Menu.",
                                    duration = SnackbarDuration.Short
                                )
                            }
                        },
                        onFavoriteClick = {
                            coroutineScope.launch {
                                if (it.recipeEntity.isFavorite == 1)
                                    scaffoldState.snackbarHostState.showSnackbar(
                                        message = "Removed " + it.recipeEntity.recipeName + " from Favorites.",
                                        duration = SnackbarDuration.Short
                                    )
                                else if (it.recipeEntity.isFavorite == 0)
                                    scaffoldState.snackbarHostState.showSnackbar(
                                        message = "Added " + it.recipeEntity.recipeName + " to Favorites.",
                                        duration = SnackbarDuration.Short
                                    )
                            }
                        },
                        onCompleteClick = {
                            coroutineScope.launch {
                                scaffoldState.snackbarHostState.showSnackbar(
                                    message = "Completed cooking " + it.recipeEntity.recipeName + "!!",
                                    duration = SnackbarDuration.Short
                                )
                            }
                        },
                        showAddedToFavoritesSnackBarMessage = {
                            coroutineScope.launch{
                                scaffoldState.snackbarHostState.showSnackbar(
                                    message = "Added $it to Favorites.",
                                    duration = SnackbarDuration.Short)
                            }
                        },
                        navigateToCommentScreen = { navController.navigate("CommentScreen") }
                    )

            }

            composable(
                route = "SearchScreen",
                enterTransition = {
                    when (initialState.destination.route) {
                        "RecipeScreen" -> slideIntoContainer(AnimatedContentScope.SlideDirection.Down, animationSpec = tween(700))
                        else -> fadeIn(animationSpec = tween(700))
                    }
                },
                exitTransition = {
                    when (targetState.destination.route) {
                        "RecipeScreen" -> slideOutOfContainer(AnimatedContentScope.SlideDirection.Up, animationSpec = tween(700))
                        else -> fadeOut(animationSpec = tween(700))
                    }
                },
            ){
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

    val backStackEntry by navController.currentBackStackEntryAsState()
    val currentRoute = backStackEntry?.destination?.route

    if(currentRoute != "CommentScreen") {
        BottomNavigation(
            backgroundColor = Color(0xFF682300),
            contentColor = Color(0xFFd8af84)
        ) {

//        val queue = navController.backQueue

            val routes = listOf("RecipeScreen", "WeeklyMenuScreen", "ShoppingScreen")

            var selected: Boolean
            var alpha: Float


            routes.forEach {
                if (it == currentRoute) {
                    selected = true
                    alpha = 1f
                } else {
                    selected = false
                    alpha = 0.5f
                }
                BottomNavigationItem(
                    selected = selected
//                (it == currentRoute)
                    ,
                    onClick = {

                        if (currentRoute == "SearchScreen" && it == "RecipeScreen") {
                            navController.popBackStack()
                        }
                        if (currentRoute == "AddRecipeScreen" && it == "RecipeScreen") {
                            navController.popBackStack()
                        }
                        if (currentRoute == "DetailsScreen") {
                            navController.popBackStack()
                        }
                        if (currentRoute == "ProfileScreen") {
                            navController.popBackStack()
                        }

                        navController.navigate(it) {

                            popUpTo(navController.graph.findStartDestination().id) {
                                saveState = true
                            }
                            launchSingleTop = true
                            restoreState = true

                        }
                    },
                    icon = {
                        when (it) {
                            "RecipeScreen" -> Icon(
                                Icons.Outlined.MenuBook,
                                contentDescription = null
                            )
                            "WeeklyMenuScreen" -> Icon(
                                Icons.Outlined.Restaurant,
                                contentDescription = null
                            )
                            "ShoppingScreen" -> Icon(
                                Icons.Outlined.ShoppingCart,
                                contentDescription = null
                            )
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
}

@OptIn(ExperimentalMaterialApi::class)
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

        val detailsScreenData by topBarViewModel.detailsScreenData.observeAsState(RecipeWithIngredientsAndInstructions())
        val uiState by topBarViewModel.uiState.collectAsState()

//        val textFieldValue by topBarViewModel.textFieldValue.observeAsState()
        val showResults by topBarViewModel.showResults.observeAsState()

        var title = ""
        var show = false
        var showShare = false
        var showSearchButton = false
        var showTitle = false
        var showSearchField = false
        var showIcon = false
        var showIcon2 = false
        var icon = Icons.Outlined.Home
        var icon2 = Icons.Outlined.Home
        var clickEffectLeft = onHomeClick
        var clickEffectRight = onHomeClick
        var textModifier = Modifier.wrapContentWidth()

        val focusManager = LocalFocusManager.current
        val focusRequester = remember { FocusRequester() }

//      if(!uiState.showResults && currentScreen == "SearchScreen") {
        if(showResults == 0 && currentScreen == "SearchScreen") {
            LaunchedEffect(Unit) {
//                topBarViewModel.updatePreview( TextFieldValue(""), "")
                focusRequester.requestFocus()
            }
        }

        if(showResults == 1 && currentScreen == "SearchScreen"){
            topBarViewModel.updateSearchText()
        }

        when (currentScreen) {
            "RecipeScreen" -> {
                show = true
                textModifier = Modifier.wrapContentWidth()
                icon = Icons.Outlined.Home
                icon2 = Icons.Outlined.Person
                clickEffectLeft = onHomeClick
                clickEffectRight = onProfileClick
                showSearchButton = true
                showTitle = false
                showSearchField = false
                showShare = false
                showIcon = false
                showIcon2 = true
            }
            "WeeklyMenuScreen" -> {
                show = true
                textModifier = Modifier.wrapContentWidth()
                title = "My Menu"
                icon = Icons.Outlined.Home
                icon2 = Icons.Outlined.Person
                clickEffectLeft = onHomeClick
                clickEffectRight = onProfileClick
                showTitle = true
                showSearchField = false
                showSearchButton = false
                showShare = false
                showIcon = true
                showIcon2 = true
            }
            "ShoppingScreen" -> {
                show = true
                textModifier = Modifier.wrapContentWidth()
                title = "Shopping list"
                icon = Icons.Outlined.Home
                icon2 = Icons.Outlined.Person
                clickEffectLeft = onHomeClick
                clickEffectRight = onProfileClick
                showTitle = true
                showSearchField = false
                showSearchButton = false
                showShare = false
                showIcon = true
                showIcon2 = true
            }
            "ProfileScreen" -> {
                show = true
                textModifier = Modifier.wrapContentWidth()
                title = "My Profile"
                icon = Icons.Outlined.ArrowBack
                icon2 = Icons.Outlined.Home
                clickEffectLeft = onBackClick
                clickEffectRight = onHomeClick
                showTitle = true
                showSearchField = false
                showSearchButton = false
                showShare = false
                showIcon = true
                showIcon2 = true
            }
            "SearchScreen" -> {
                show = true
                textModifier = Modifier.width(0.dp)
                title = ""
                icon = Icons.Outlined.ArrowBack
                icon2 = Icons.Outlined.Person
                clickEffectLeft = onBackClick
                clickEffectRight = {}
                showTitle = false
                showSearchField = true
                showSearchButton = false
                showShare = false
                showIcon = true
                showIcon2 = false

            }
            "AddRecipeScreen" -> {
                show = true
                textModifier = Modifier.wrapContentWidth()
                title = "Add Custom Recipe"
                icon = Icons.Outlined.ArrowBack
                icon2 = Icons.Outlined.Person
                clickEffectLeft = onBackClick
                clickEffectRight = {}
                showTitle = true
                showSearchField = false
                showSearchButton = false
                showShare = false
                showIcon = true
                showIcon2 = false

            }
            "CommentScreen" -> {
                show = true
                textModifier = Modifier.wrapContentWidth()
                title = "Leave a Tip/Comment"
                icon = Icons.Outlined.ArrowBack
                icon2 = Icons.Outlined.Person
                clickEffectLeft = {}
                clickEffectRight = {}
                showTitle = true
                showSearchField = false
                showSearchButton = false
                showShare = false
                showIcon = false
                showIcon2 = false

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
                showTitle = true
                showSearchField = false
                showSearchButton = false
                showShare = true

                showIcon = true

                showIcon2 = true

                icon2 = if (detailsScreenData.recipeEntity.isFavorite == 0) {
                    Icons.Outlined.FavoriteBorder

                } else {
                    Icons.Outlined.Favorite
                }
            }
        }



//        AnimatedVisibility(
//            visible = show,
//            enter = fadeIn(animationSpec = tween(700)) ,
//            exit = fadeOut(animationSpec = tween(700)) ,
//        ) {
            if (show) {
            Surface(
                modifier = Modifier
                    .fillMaxWidth()
                    .wrapContentHeight(),
                color = Color.Transparent){
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(64.dp)
                        .background(Color(0xFF682300)),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.Start
                ) {
                    Spacer(Modifier.size(8.dp))

                    //shows clickable only search button on main screen to go to search screen
                    if (showSearchButton) {
                        Surface(
                            Modifier
                                .height(44.dp)
                                .width(300.dp)
                                .clickable(onClick = onSearchClick)
                                .padding(start = 8.dp, top = 0.dp, bottom = 0.dp)
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
                        ) {
                            TextField(
                                value = "",
                                onValueChange = {},
                                modifier = Modifier
//                                .alpha(.8f)
                                ,
                                enabled = false,
                                readOnly = true,
                                leadingIcon = {
                                    Icon(
                                        Icons.Outlined.Search,
                                        contentDescription = null,
                                        tint = Color(0xFF000000)
                                    )
                                },
                                shape = RoundedCornerShape(25.dp),
                                colors = TextFieldDefaults.textFieldColors(
                                    backgroundColor = Color(
                                        0xFFd8af84
                                    ), textColor = Color(0xFF000000)
                                )
                            )
                        }
                    }
                    else if(showIcon){
                        IconButton(
                            onClick = {
//                                if(currentScreen == "SearchScreen") topBarViewModel.updatePreview( TextFieldValue(""), "");
                                clickEffectLeft()
                                      },
                            modifier =
                            Modifier
                                .size(48.dp)
                                .align(Alignment.CenterVertically)
                                .background(color = Color.Transparent)
                                .padding(start = 8.dp)
                                .border(
                                    width = 2.dp,
                                    brush = (Brush.horizontalGradient(
                                        colors = listOf(Color(0xFFd8af84), Color(0xFFb15f33)),
                                        tileMode = TileMode.Mirror,
                                    )),
                                    shape = CircleShape
                                ),
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
                    else{
                        Spacer(Modifier.width(48.dp))

                    }

                    Spacer(Modifier.weight(1f))

                    if(showTitle){
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
                    }


                    Spacer(Modifier.weight(1f))

                    if (showShare) {



                        val context = LocalContext.current


                        IconButton(
                            onClick = {

                                var myString = detailsScreenData.recipeEntity.recipeName + '\n' + '\n'

                                for(x in detailsScreenData.ingredientsList.indices){
                                    myString += detailsScreenData.ingredientsList[x].ingredientName + '\n'
                                }

                                myString += '\n'

                                for(x in detailsScreenData.instructionsList.indices){
                                    myString += detailsScreenData.instructionsList[x].instruction + '\n'
                                }

                                val sendIntent: Intent = Intent().apply {
                                    action = Intent.ACTION_SEND
                                    putExtra(Intent.EXTRA_TEXT, myString)
//                                    putExtra(Intent.EXTRA_TITLE, detailsScreenData.recipeEntity.recipeName)
                                    type = "text/plain"
                                }

                                val shareIntent = Intent.createChooser(sendIntent, null)

                                context.startActivity(shareIntent)


                            },
                            modifier = Modifier
                                .size(48.dp)
                                .align(Alignment.CenterVertically)
                                .background(color = Color.Transparent)
                                .padding(start = 8.dp)
                                .border(
                                    width = 2.dp,
                                    brush = (Brush.horizontalGradient(
                                        colors = listOf(Color(0xFFd8af84), Color(0xFFb15f33)),
                                        tileMode = TileMode.Mirror,
                                    )),
                                    shape = CircleShape
                                ),
                            // color = Color.Transparent

                        ) {
                            Icon(
                                imageVector = Icons.Outlined.Share,
                                contentDescription = null,
                                modifier = Modifier
                                    .size(24.dp),
                                tint = Color(0xFFd8af84)
                            )
                        }
                        Spacer(Modifier.size(16.dp))
                    }



                    val gradientWidthButton = with(LocalDensity.current) { 48.dp.toPx() }


                    if(currentScreen == "AddRecipeScreen" || currentScreen == "CommentScreen"){
                        Spacer(Modifier.width(48.dp))
                    }

                    if(showIcon2) {
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
                                        colors = listOf(Color(0xFFd8af84), Color(0xFFb15f33)),
                                        endX = gradientWidthButton,
                                        tileMode = TileMode.Mirror,
                                    )),
                                    shape = CircleShape
                                )
                                .size(48.dp)
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
                                modifier = Modifier.size(24.dp),
                                // modifier = Modifier.background(color = Color(0xFFFFFFFF)),
                                contentDescription = null
                            )
                        }
                    }
                    if(showSearchField){

                        val interactionSource = remember { MutableInteractionSource() }

                        BasicTextField(
                            value =  uiState.currentInput,
                            onValueChange =
                            { topBarViewModel.updatePreview( it, it.text) },
                            modifier = Modifier
                                .focusRequester(focusRequester)
                                .background(
                                    color = Color(0xFFd8af84),
                                    shape = RoundedCornerShape(25.dp)
                                )
                                .height(44.dp)
                                .width(300.dp)
                                .border(
                                    width = 2.dp,
                                    brush = (Brush.horizontalGradient(
                                        colors = listOf(Color(0xFFd8af84), Color(0xFFb15f33)),
                                        tileMode = TileMode.Mirror
                                    )),
                                    shape = RoundedCornerShape(25.dp)
                                ),
                            keyboardOptions = KeyboardOptions(imeAction = ImeAction.Search),
                            keyboardActions = KeyboardActions(
                                onSearch = {
                                    topBarViewModel.liveSearchForClick()
//                                  searchScreenViewModel.searchFor(text)
                                    focusManager.clearFocus()
                                }),
                            singleLine = true,
                        ){
                            TextFieldDefaults.TextFieldDecorationBox(
                                value = uiState.currentInput.text,
                                innerTextField = it,
                                enabled = true,
                                singleLine = true,
                                visualTransformation = VisualTransformation.None,
                                interactionSource = interactionSource,
                                leadingIcon = { Icon(Icons.Outlined.Search, contentDescription = null, tint = Color(0xFF000000)) },
                                trailingIcon =
                                {
                                    if(uiState.currentInput.text.isNotEmpty()){
                                        Surface(
                                            color = Color.Transparent,
                                            modifier = Modifier.clickable {  topBarViewModel.updatePreview( TextFieldValue(""), "") }
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

                                contentPadding = TextFieldDefaults.textFieldWithoutLabelPadding(
                                    top = 0.dp, bottom = 0.dp
                                )
                            )
                        }
//                        TextField(
//                            value =  uiState.currentInput,
//                            onValueChange =
//                            {
//                                topBarViewModel.updatePreview( it, it.text)
//                            },
//                            modifier = Modifier
//                                .focusRequester(focusRequester)
//                                .height(56.dp)
//                                .padding(top = 4.dp, bottom = 4.dp)
//                                .border(
//                                    width = 2.dp,
//                                    brush = (Brush.horizontalGradient(
//                                        colors = listOf(Color(0xFFd8af84), Color(0xFFb15f33)),
//                                        tileMode = TileMode.Mirror
//                                    )),
//                                    shape = RoundedCornerShape(25.dp)
//                                ),
//                            shape = RoundedCornerShape(25.dp),
//                            textStyle = TextStyle.Default.copy(color = Color(0xFF000000), fontSize = 14.sp),
//                            leadingIcon = { Icon(Icons.Outlined.Search, contentDescription = null, tint = Color(0xFF000000)) },
//                            trailingIcon =
//                            {
//                                if(uiState.currentInput.text.isNotEmpty()){
//                                    Surface(
//                                        color = Color.Transparent,
//                                        modifier = Modifier.clickable {  topBarViewModel.updatePreview( TextFieldValue(""), "") }
//                                    )
//                                    {
//                                        Icon(
//                                            Icons.Outlined.Close,
//                                            contentDescription = null,
//                                            tint = Color(0xFF000000)
//                                        )
//                                    }
//                                }
//                            },
//                            keyboardOptions = KeyboardOptions(imeAction = ImeAction.Search),
//                            keyboardActions = KeyboardActions(
//                                onSearch = {
//                                    topBarViewModel.liveSearchForClick()
////                                searchScreenViewModel.searchFor(text)
//                                    focusManager.clearFocus()
//                                }),
//                            singleLine = true,
//                            colors = TextFieldDefaults.textFieldColors(
//                                disabledTextColor = Color.Transparent,
//                                backgroundColor = Color(0xFFd8af84),
//                                focusedIndicatorColor = Color.Transparent,
//                                unfocusedIndicatorColor = Color.Transparent,
//                                disabledIndicatorColor = Color.Transparent
//                            )
//                        )
                        Spacer(Modifier.weight(1f))
                    }
                    Spacer(Modifier.size(8.dp))
                }
            }

//            }
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
