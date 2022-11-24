package com.example.bearrecipebookapp

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.CutCornerShape
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.MenuBook
import androidx.compose.material.icons.outlined.Restaurant
import androidx.compose.material.icons.outlined.ShoppingCart
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.example.bearrecipebookapp.ui.*
import com.example.bearrecipebookapp.ui.theme.BearRecipeBookAppTheme
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

@Composable
fun BearRecipeApp(
){

    val navController: NavHostController = rememberNavController()
    val currentBackStackEntry by navController.currentBackStackEntryAsState()

    val currentScreen = currentBackStackEntry?.destination?.route ?: "RecipeScreen"



    var showBottomBar by rememberSaveable { mutableStateOf(true) }

    val coroutineScope = rememberCoroutineScope()
    val scaffoldState = rememberScaffoldState()

//    showTopBar = when (currentScreen){
////        "DetailsScreen" -> false
//        else -> true
//    }

    Scaffold(
        scaffoldState = scaffoldState,
        bottomBar = { BearAppBottomBar(navController = navController) },
//        bottomBar = {
//            BearAppTopBar(
//                showTopBar = showBottomBar,
//                onClick = {navController.navigate(it)},
//            )
//        },
        snackbarHost = {
            SnackbarHost(it){ data ->
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

            composable(route = "RecipeScreen"){
                //Recipe Book Main Screen
                HomeScreen(
                    onSearchClick = {navController.navigate("SearchScreen")},
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
                    }}
                )
            }

            composable(route = "ShoppingScreen"){
                ShoppingListScreen(
                    onDetailsClick = { navController.navigate("DetailsScreen") }
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
                        else if(it.isFavorite == 0)
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
//    showBottomBar: Boolean,
//    onClick: (String) -> Unit,
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


        println("here2")
        for(x in queue.indices){
            println("route $currentRoute queue " + queue[x].destination.route)
        }
        println("here3")

        routes.forEach{ it ->
            BottomNavigationItem(
                selected = (currentRoute == it),
                onClick = {
                    for(x in queue.indices){
                        println("BEFORE IF")
                        println("route $currentRoute clicked on $it queue " + queue[x].destination)
                    }

                    if(currentRoute == "SearchScreen" && it == "RecipeScreen"){
                        navController.popBackStack()
                    }
                    if(currentRoute == "DetailsScreen"){
                        for(x in queue.indices){
                            println("INSIDE IF")
                            println("route $currentRoute clicked on $it queue " + queue[x].destination)
                        }

                        navController.popBackStack()
                    }
                      navController.navigate(it){
//                          println("route $currentRoute destination $d")
                          popUpTo(navController.graph.findStartDestination().id) {
                              saveState = true
                          }
                          launchSingleTop = true
                          restoreState = true
                      }
//                    println("route $currentRoute destination $d")
                          },
                icon = {
                    when(it){
                        "RecipeScreen" -> Icon(Icons.Outlined.MenuBook, contentDescription = null)
                        "WeeklyMenuScreen" -> Icon(Icons.Outlined.Restaurant, contentDescription = null)
                        "ShoppingScreen" -> Icon(Icons.Outlined.ShoppingCart, contentDescription = null)
                    }
                }
            )
        }

    }
}

@Composable
fun BearAppTopBar(
    showTopBar: Boolean,
    onClick: (String) -> Unit,
    )
{
    var selectedTabIndex by rememberSaveable { mutableStateOf(0) }


    val routes = listOf("Recipes", "Menu", "Shopping List")


    if(showTopBar){
        TabRow(
            selectedTabIndex = selectedTabIndex,
            backgroundColor = Color(0xFF682300),
            contentColor = Color(0xFFd8af84)
        ) {
            routes.forEachIndexed { index, route ->
                Tab(
//                    text = { Text(title) },
                    icon =
                    {
                        when(route){
                            "Recipes" -> Icon(Icons.Outlined.MenuBook, contentDescription = null)
                            "Menu" -> Icon(Icons.Outlined.Restaurant, contentDescription = null)
                            "Shopping List" -> Icon(Icons.Outlined.ShoppingCart, contentDescription = null)
                        }
                    },
                    selected = selectedTabIndex == index,
                    onClick = { selectedTabIndex = index
                        when(route){
                            "Recipes" -> onClick("RecipeScreen")
                            "Menu" -> onClick("WeeklyMenuScreen")
                            "Shopping List" -> onClick("ShoppingScreen")
                        }
                    }
                )
            }
        }
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
