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
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.example.bearrecipebookapp.ui.HomeScreen
import com.example.bearrecipebookapp.ui.MenuScreen
import com.example.bearrecipebookapp.ui.NewDetailsScreen
import com.example.bearrecipebookapp.ui.ShoppingListScreen
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



    var showTopBar by rememberSaveable { mutableStateOf(true) }

    val coroutineScope = rememberCoroutineScope()
    val scaffoldState = rememberScaffoldState()

//    showTopBar = when (currentScreen){
////        "DetailsScreen" -> false
//        else -> true
//    }

    Scaffold(
        scaffoldState = scaffoldState,
        bottomBar = {
            BearAppTopBar(
                showTopBar = showTopBar,
                onClick = {navController.navigate(it)},
            )
        },
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
                    onDetailsClick = { navController.navigate("DetailsScreen") },
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
                    }}
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
                    }}
                )
            }
            composable(route = "ShoppingScreen"){
                ShoppingListScreen(
                    onDetailsClick = { navController.navigate("DetailsScreen")}
                )

            }
            composable(route = "DetailsScreen") {

                NewDetailsScreen(onGoBackClick = { navController.popBackStack() }
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
fun BearAppTopBar(
    showTopBar: Boolean,
    onClick: (String) -> Unit,
    )
{
    var state by rememberSaveable { mutableStateOf(0) }


    val keys = listOf("Recipes", "Menu", "Shopping List")


    if(showTopBar){
        TabRow(
            selectedTabIndex = state,
            backgroundColor = Color(0xFF682300),
            contentColor = Color(0xFFd8af84)
        ) {
            keys.forEachIndexed { index, title ->
                Tab(
//                    text = { Text(title) },
                    icon =
                    {
                        when(title){
                            "Recipes" -> Icon(Icons.Outlined.MenuBook, contentDescription = null)
                            "Menu" -> Icon(Icons.Outlined.Restaurant, contentDescription = null)
                            "Shopping List" -> Icon(Icons.Outlined.ShoppingCart, contentDescription = null)
                        }
                    },
                    selected = state == index,
                    onClick = { state = index
                        when(title){
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
