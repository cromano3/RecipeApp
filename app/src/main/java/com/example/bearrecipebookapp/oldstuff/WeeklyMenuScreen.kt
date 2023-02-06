package com.example.bearrecipebookapp.oldstuff//package com.example.bearrecipebookapp.ui
//
//import androidx.compose.runtime.Composable
//import com.example.bearrecipebookapp.datamodel.RecipeWithIngredientsAndInstructions
//
//@Composable
//fun WeeklyMenuScreen(
//    selectedRecipesList: List<RecipeWithIngredientsAndInstructions>,
//    //selectedRecipesList: List<RecipeWithIngredients>,
//  //  currentScreen: String,
//    onClick: (RecipeWithIngredientsAndInstructions) -> Unit,
//    onDetailsClick: (RecipeWithIngredientsAndInstructions) -> Unit,
//){
//
//
////    Surface(
////        modifier = Modifier.fillMaxSize(),
////        color = Color(0xFFd8af84)
////
////    ) {
////        Column(Modifier.verticalScroll(rememberScrollState())){
////            for(x in 0 until selectedRecipesList.size){
////
////                RecipeCard(
////                    modifier = Modifier,
////                    recipeWithIngredients = RecipeWithIngredients(selectedRecipesList[x].recipeEntity,selectedRecipesList[x].ingredientsList),
////                            //selectedRecipesList[x],
////
////                    currentScreen = "WeeklyMenuScreen",
////                    onClick = { onClick(selectedRecipesList[x]) },
////                    onDetailsClick = { onDetailsClick(selectedRecipesList[x]) }
////                )
////            }
////
////        }
////        //Draw all recipes on the list
//////        LazyColumn {
//////            items(selectedRecipesList) {
//////                //needs current screen parameter
//////                RecipeCard(
//////                    recipeWithIngredients = it,
//////                    selected = true,
//////                    currentScreen = "WeeklyMenuScreen",
//////                    onClick = { onClick(it) },
//////                    onDetailsClick = { onDetailsClick(it) }
//////                )
//////            }
//////        }
////    }
//}
//
//
