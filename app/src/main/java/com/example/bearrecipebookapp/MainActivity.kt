package com.example.bearrecipebookapp

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
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
    //IS THIS THE SAME AS KEEPING IT IN THE PARAMETERS LIST? Yes?
    val navController: NavHostController = rememberNavController()

    // Get current back stack entry
    val currentBackStackEntry by navController.currentBackStackEntryAsState()

    // Get the name of the current screen
    val currentScreen = currentBackStackEntry?.destination?.route ?: "RecipeScreen"



    var showTopBar by rememberSaveable { mutableStateOf(true) }

    showTopBar = when (currentScreen){
        "DetailsScreen" -> false
        else -> true
    }

    Scaffold(
        bottomBar = {
            BearAppTopBar(
                showTopBar = showTopBar,
                onClick = {navController.navigate(it)},
            )
        }
    ){
        NavHost(
            navController = navController,
            startDestination = "RecipeScreen",
        ){
            composable(route = "RecipeScreen"){
                //Recipe Book Main Screen
                HomeScreen(
                    onDetailsClick = { navController.navigate("DetailsScreen") }
                )
            }
            composable(route = "WeeklyMenuScreen"){
                //Weekly menu screen
                MenuScreen(
                    onDetailsClick = { navController.navigate("DetailsScreen")}
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
                    text = { Text(title) },
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
