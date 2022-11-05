package com.example.bearrecipebookapp

import android.app.Application
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.ExperimentalTextApi
import androidx.compose.ui.tooling.preview.Preview
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewmodel.compose.LocalViewModelStoreOwner
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.example.bearrecipebookapp.datamodel.RecipeWithIngredientsAndInstructions
import com.example.bearrecipebookapp.ui.DetailsScreen
import com.example.bearrecipebookapp.ui.RecipeScreen
import com.example.bearrecipebookapp.ui.ShoppingListScreen
import com.example.bearrecipebookapp.ui.WeeklyMenuScreen
import com.example.bearrecipebookapp.ui.theme.BearRecipeBookAppTheme
import com.example.bearrecipebookapp.viewmodel.RecipeBookScreenViewModel

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            BearRecipeBookAppTheme {
                // A surface container using the 'background' color from the theme
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colors.background
                ) {

                    val owner = LocalViewModelStoreOwner.current

                    owner?.let {
                        val viewModel: RecipeBookScreenViewModel = viewModel(
                            it,
                            "RecipeBookScreenViewModel",
                            RecipeBookScreenViewModelFactory(
                                LocalContext.current.applicationContext
                                        as Application
                            )
                        )
                        BearRecipeApp(viewModel)
                    }

                }
            }
        }
    }
}
//Need to implement Navigation root starting here
@Composable
fun BearRecipeApp(
    recipeBookScreenViewModel: RecipeBookScreenViewModel,

){
    //IS THIS THE SAME AS KEEPING IT IN THE PARAMETERS LIST????????????????????????????
    val navController: NavHostController = rememberNavController()
    //?????????????????????????????????????????????????????????????????????????????????

    // Get current back stack entry
    val currentBackStackEntry by navController.currentBackStackEntryAsState()

    // Get the name of the current screen
    val currentScreen = currentBackStackEntry?.destination?.route ?: "RecipeScreen"


    //These live data look ups are all GOOD, they are just currently unused
//    val allRecipes by recipeBookScreenViewModel.allRecipes.observeAsState(listOf())
//    val selectedRecipes by recipeBookScreenViewModel.selectedRecipes.observeAsState(listOf())
//    val allRecipesWithInstructions by recipeBookScreenViewModel.recipesWithInstructions.observeAsState(listOf())
//    val selectedRecipesWithIngredients by recipeBookScreenViewModel.selectedRecipesWithIngredients.observeAsState(listOf())
//    val allRecipesWithIngredients by recipeBookScreenViewModel.recipesWithIngredients.observeAsState(listOf())

    val selectedIngredients by recipeBookScreenViewModel.selectedIngredients.observeAsState(listOf())

    val allRecipesWithIngredientsAndInstructions by recipeBookScreenViewModel.recipesWithIngredientsAndInstructions.observeAsState(listOf())
    val selectedRecipesWithIngredientsAndInstructions by recipeBookScreenViewModel.selectedRecipesWithIngredientsAndInstructions.observeAsState(listOf())

    val detailsScreenTarget by recipeBookScreenViewModel.publicDetailsScreenUiState.collectAsState()

    val publicOnMenuState by recipeBookScreenViewModel.publicOnMenuState.collectAsState()









    var showTopBar by rememberSaveable { mutableStateOf(true) }

    showTopBar = when (currentScreen){
        "DetailsScreen" -> false
        else -> true
    }



    Scaffold(
        topBar = {
//            if (showTopBar)
            BearAppTopBar(
                menuCount = 1,//uiState.numberOfRecipesOnMenu,
                showTopBar = showTopBar,
               // recipeList = allRecipesWithIngredients,
                onClick = {navController.navigate(it)},
                newDetailsScreenTarget = detailsScreenTarget,

                onGoBackClick = {navController.popBackStack();
                    //recipeBookScreenViewModel.removeAsDetailsScreenTarget(it)
                                },
                onUpdateMenuClick = {recipeBookScreenViewModel.updateMenuWithInstructions(it);
                    recipeBookScreenViewModel.newSetDetailsScreenTarget(it)}
               // onGoBackClick = {navController.navigate(currentScreen); navController.popBackStack()}
            )
        }
    ){
        NavHost(
            navController = navController,
            startDestination = "RecipeScreen",
        ){
            composable(route = "RecipeScreen"){
                //Recipe book screen
                RecipeScreen(
              //      allRecipesWithIngredients = allRecipesWithIngredients,
                    allRecipesWithIngredientsAndInstructions = allRecipesWithIngredientsAndInstructions,
//                    viewModel = recipeBookScreenViewModel,
//                     allRecipes = allRecipes,
                //    recipeList = DataSource().loadRecipeList(),
                 //   selectedRecipesList = selectedRecipes,//uiState.selectedRecipesList,
                //    currentScreen = currentScreen,
                    onClick = {recipeBookScreenViewModel.updateMenuWithInstructions(it) },
                    onDetailsClick = {
                        recipeBookScreenViewModel.newSetDetailsScreenTarget(it);
                        //recipeBookScreenViewModel.setDetailsScreenTarget(it);
                    navController.navigate("DetailsScreen")}

                )
            }
            composable(route = "WeeklyMenuScreen"){
                //Weekly menu screen
                WeeklyMenuScreen(
                    selectedRecipesList = selectedRecipesWithIngredientsAndInstructions,
                   // selectedRecipesList = selectedRecipesWithIngredients,//selectedRecipes,//uiState.selectedRecipesList,
                //    currentScreen = currentScreen,
                    onClick = {recipeBookScreenViewModel.newRemoveFromMenu(it)
                        //recipeBookScreenViewModel.removeFromMenu(it)
                              },
                    onDetailsClick = {
                        recipeBookScreenViewModel.newSetDetailsScreenTarget(it);
                        //recipeBookScreenViewModel.setDetailsScreenTarget(it);
                    navController.navigate("DetailsScreen")}
                )
            }
            composable(route = "ShoppingScreen"){
                ShoppingListScreen(
                    selectedIngredients = selectedIngredients,
                    //selectedRecipes = selectedRecipesWithIngredients,
                    selectedRecipes = selectedRecipesWithIngredientsAndInstructions,
                    onClickIngredientSelected = {recipeBookScreenViewModel.ingredientSelected(it)},
                    onClickIngredientDeselected = {recipeBookScreenViewModel.ingredientDeselected(it)},
                    onDetailsClick = {
                        recipeBookScreenViewModel.newSetDetailsScreenTarget(it);
                        //recipeBookScreenViewModel.setDetailsScreenTarget(it);
                        navController.navigate("DetailsScreen")}
                )

            }
            composable(route = "DetailsScreen"){
                DetailsScreen(
                   // recipeList = allRecipesWithIngredients,
                   // recipeListWithInstructions = allRecipesWithInstructions,
                    detailsScreenTargetOnMenu = publicOnMenuState.onMenu,
                    detailsScreenTargetIngredients = detailsScreenTarget.ingredientsList,
                    detailsScreenTargetInstructions = detailsScreenTarget.instructionsList,
                    detailsScreenTarget = detailsScreenTarget,
                    onGoBackClick = {navController.popBackStack();
                                    //recipeBookScreenViewModel.removeAsDetailsScreenTarget(it)
                                    },
                    onUpdateMenuClick = {recipeBookScreenViewModel.updateMenuWithInstructions(it);
                        recipeBookScreenViewModel.setDetailsScreenTargetHeart()}
                )

            }
//            composable(route = "PantryScreen"){
//                //Weekly menu screen
//                PantryScreen(
//
//                    pantryList = listOf()//uiState.pantryList,
//
//                   // onClick = {recipeAppViewModel.incrementUpOrDownAmountOwned(it)}
//                )
//            }
        }
    }
}

@OptIn(ExperimentalTextApi::class)
@Composable
fun BearAppTopBar(
    menuCount: Int,
    modifier: Modifier = Modifier,
    showTopBar: Boolean,
    //recipeList: List<RecipeWithIngredients>,
    newDetailsScreenTarget: RecipeWithIngredientsAndInstructions,
    onClick: (String) -> Unit,
    //onGoBackClick: (RecipeWithIngredients) -> Unit,
    onGoBackClick: () -> Unit,
    onUpdateMenuClick: (RecipeWithIngredientsAndInstructions) -> Unit
    )
{
    var state by remember { mutableStateOf(0) }

//    val titles = listOf("Recipes", "Menu", "Pantry")

//    val topBarMenuItems = LinkedHashMap<String, String>()
//    topBarMenuItems["Recipes"] = "RecipeScreen"
//    topBarMenuItems["Menu"] = "WeeklyMenuScreen"
//    topBarMenuItems["Pantry"] = "PantryScreen"

//    val keys = listOf(topBarMenuItems.keys)



    val keys = listOf("Recipes", "Menu", "Shopping List")



   // BackHandler{onGoBackClick()}

//    Box(
//        modifier = modifier
//            .fillMaxWidth()
//           // .background(color = MaterialTheme.colors.primary),
//    ){

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
    else{
//        var detailsScreenTarget = recipeList[0]
//
//
//        for(x in 0 until recipeList.size){
//            if(recipeList[x].recipeEntity.isDetailsScreenTarget == 1)
//                detailsScreenTarget = recipeList[x]
//        }

        //BackHandler{ onGoBackClick(detailsScreenTarget) }

    }

//        Text(
//            modifier = Modifier.align(Alignment.TopCenter),
//            text = "Text tab ${state + 1} selected",
//            style = MaterialTheme.typography.body1
//        )
//    }



//    Row(
//        modifier = modifier
//            .fillMaxWidth()
//            .background(color = MaterialTheme.colors.primary),
//        verticalAlignment = Alignment.CenterVertically
//    ){
////        Text(
////            //text = stringResource(R.string.app_name),
////            modifier = Modifier.padding(top = 16.dp, bottom = 16.dp, start = 8.dp, end = 8.dp),
////            text = "Bear Recipes",
////            color = MaterialTheme.colors.onPrimary,
////            style = MaterialTheme.typography.h1,
////            textAlign = TextAlign.Center
////        )
//        Text(
//            // modifier = Modifier.padding(end = 8.dp),
//            text = menuCount.toString(),
//            color = MaterialTheme.colors.onPrimary,
//            style = MaterialTheme.typography.h1
//        )
////Small Bagel Image for Top Bar, should be replaced with App Emblem once we create one
////        Image(
////            modifier = Modifier
////                .size(64.dp)
////                .padding(start = 16.dp),
////            painter = painterResource(R.drawable.bagel),
////            contentDescription = null
////        )
//        Spacer(modifier.weight(1f))
//        /**
//         *
//         * Show current menu list count
//         *
//         **/
//
//        IconButton(onClick = { onClick("RecipeScreen") },
//            modifier = Modifier
//                .padding(24.dp)
//                .size(24.dp),) {
//            Icon(
//                imageVector = Icons.Outlined.MenuBook,
//                tint = MaterialTheme.colors.onBackground,
//                contentDescription = null
//            )
//        }
//        IconButton(onClick = { onClick("WeeklyMenuScreen") },
//            modifier = Modifier
//                .padding(24.dp)
//                .size(24.dp),){
//            Icon(
//                imageVector = Icons.Outlined.RestaurantMenu,
//                tint = MaterialTheme.colors.onBackground,
//                contentDescription = null
//            )
//        }
//        IconButton(onClick = { onClick("PantryScreen") },
//            modifier = Modifier
//                .padding(24.dp)
//                .size(24.dp),){
//            Icon(
//                imageVector = Icons.Outlined.Kitchen,
//                tint = MaterialTheme.colors.onBackground,
//                contentDescription = null
//            )
//        }
//    }
}
//
@Preview(showBackground = true, showSystemUi = true)
@Composable
fun DefaultPreview() {
    BearAppTopBar(menuCount = 2, onClick = {}, showTopBar = false, newDetailsScreenTarget = RecipeWithIngredientsAndInstructions(),
        onGoBackClick = {},
        onUpdateMenuClick = {}
    )
}


class RecipeBookScreenViewModelFactory(val application: Application) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {

            return RecipeBookScreenViewModel(application) as T
        }

}