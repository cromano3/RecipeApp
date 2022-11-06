package com.example.bearrecipebookapp.ui

import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.bearrecipebookapp.ui.components.SmallRecipeCard
import com.example.bearrecipebookapp.viewmodel.HomeScreenViewModel

@Composable
fun HomeScreen(homeScreenViewModel: HomeScreenViewModel = viewModel()){

    val homeScreenData by homeScreenViewModel.homeScreenData.observeAsState(listOf())




    SmallRecipecard(
        recipeName = ,
        onMenu = ,
        ingredientsList = ,
        isShown = ,
        onClick = { homeScreenViewModel.toggleFavorite(recipe_name)},
        onDetailsClick = { onDetailsClick(//navigate with parameter!)}
    )
    SmallRecipeCard(
        modifier = Modifier.padding(end = 16.dp),
        recipe = allRecipesWithIngredientsAndInstructions[x].recipeEntity,
        ingredients = allRecipesWithIngredientsAndInstructions[x].ingredientsList,
//                selected = selected,
//                currentScreen = "RecipeBookScreen",
        onClick = { onClick(allRecipesWithIngredientsAndInstructions[x]) },
        onDetailsClick = { onDetailsClick(allRecipesWithIngredientsAndInstructions[x]) }
    )
}