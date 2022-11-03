package com.example.bearrecipebookapp.ui

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.example.bearrecipebookapp.datamodel.RecipeWithIngredientsAndInstructions
import com.example.bearrecipebookapp.ui.components.SmallRecipeCard

@Composable
fun RecipeScreen(
    allRecipesWithIngredientsAndInstructions: List<RecipeWithIngredientsAndInstructions>,
  //  allRecipesWithIngredients: List<RecipeWithIngredients>,
//    viewModel: RecipeBookScreenViewModel,
//    recipeList: List<RecipeEntity>,
 //   selectedRecipesList: List<RecipeEntity>,
    // currentScreen: String,
    onClick: (RecipeWithIngredientsAndInstructions) -> Unit,
    onDetailsClick: (RecipeWithIngredientsAndInstructions) -> Unit
) {


    Surface(
        modifier = Modifier.fillMaxSize(),
        color = Color(0xFFd8af84)

    ) {
        Column(
            //To DO:
            modifier = Modifier
                .padding(top = 0.dp, start = 16.dp, end = 16.dp)
                .verticalScroll(
                    rememberScrollState()
                ),
            //horizontalArrangement = Arrangement.spacedBy(16.dp),
            //columns = GridCells.Fixed(2)
        )
        {

            for(x in allRecipesWithIngredientsAndInstructions.indices) {
                if ((x + 2) % 2 == 0) {
                    Row(){
                        SmallRecipeCard(
                            modifier = Modifier.padding(end = 16.dp),
                            recipe = allRecipesWithIngredientsAndInstructions[x].recipeEntity,
                            ingredients = allRecipesWithIngredientsAndInstructions[x].ingredientsList,
//                selected = selected,
//                currentScreen = "RecipeBookScreen",
                            onClick = { onClick(allRecipesWithIngredientsAndInstructions[x]) },
                            onDetailsClick = { onDetailsClick(allRecipesWithIngredientsAndInstructions[x]) }
                        )

                        if (x + 1 != allRecipesWithIngredientsAndInstructions.size) {
                            SmallRecipeCard(
                                modifier = Modifier,
                                recipe = allRecipesWithIngredientsAndInstructions[x+1].recipeEntity,
                                ingredients = allRecipesWithIngredientsAndInstructions[x+1].ingredientsList,
//                selected = selected,
//                currentScreen = "RecipeBookScreen",
                                onClick = { onClick(allRecipesWithIngredientsAndInstructions[x+1]) },
                                onDetailsClick = { onDetailsClick(allRecipesWithIngredientsAndInstructions[x+1]) }
                            )
                        }

                    }

                }
            }

        }
//            items(allRecipesWithIngredients.size) {
//
//                //
//                //This is used to show on the recipe book screen which recipes are on the Menu
//                //
////                var selected = false
////
////                for (x in 0 until selectedRecipesList.size) {
////                    if (selectedRecipesList[x].recipeName == recipeList[it].recipeName) {
////                        selected = true
////                    }
////                }
//
//
//                SmallRecipeCard(
//                    recipe = allRecipesWithIngredients[it].recipeEntity,
//                    ingredients = allRecipesWithIngredients[it].ingredientsList,
////                selected = selected,
////                currentScreen = "RecipeBookScreen",
//                    onClick = { onClick(allRecipesWithIngredients[it]) },
//                    onDetailsClick = { onDetailsClick(allRecipesWithIngredients[it]) }
//                )
//            }
    }
}



//@Preview("default")
//@Preview("dark theme", uiMode = Configuration.UI_MODE_NIGHT_YES)
//@Preview("large font", fontScale = 2f)
//@Composable
//fun SnackPreview() {
//    BearRecipeBookAppTheme {
////        val myList = DataSource().loadRecipeList()
////        RecipeScreen(recipeList = myList)
//
//
////        Row{
////            SmallRecipeCard()
////            SmallRecipeCard()
////        }
//
//    }
//}
