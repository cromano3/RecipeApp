package com.example.bearrecipebookapp.ui

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import com.example.bearrecipebookapp.datamodel.RecipeWithIngredients
import com.example.bearrecipebookapp.datamodel.RecipeWithIngredientsAndInstructions
import com.example.bearrecipebookapp.ui.components.RecipeCard

@Composable
fun FavoritesScreen(
    selectedRecipesList: List<RecipeWithIngredientsAndInstructions>,
    //selectedRecipesList: List<RecipeWithIngredients>,
    //  currentScreen: String,
    onClick: (RecipeWithIngredientsAndInstructions) -> Unit,
    onDetailsClick: (RecipeWithIngredientsAndInstructions) -> Unit,
){


    Surface(
        modifier = Modifier.fillMaxSize(),
        color = Color(0xFFd8af84)

    ) {
        Column(Modifier.verticalScroll(rememberScrollState())){
            for(x in 0 until selectedRecipesList.size){

                RecipeCard(
                    modifier = Modifier,
                    recipeWithIngredients = RecipeWithIngredients(selectedRecipesList[x].recipeEntity,selectedRecipesList[x].ingredientsList),
                    currentScreen = "WeeklyMenuScreen",
                    onClick = { onClick(selectedRecipesList[x]) },
                    onDetailsClick = { onDetailsClick(selectedRecipesList[x]) }
                )
            }

        }
        //Draw all recipes on the list
//        LazyColumn {
//            items(selectedRecipesList) {
//                //needs current screen parameter
//                RecipeCard(
//                    recipeWithIngredients = it,
//                    selected = true,
//                    currentScreen = "WeeklyMenuScreen",
//                    onClick = { onClick(it) },
//                    onDetailsClick = { onDetailsClick(it) }
//                )
//            }
//        }
    }
}


