package com.example.bearrecipebookapp.ui.components

import androidx.compose.material.ScaffoldState
import androidx.compose.material.SnackbarDuration
import com.example.bearrecipebookapp.data.entity.RecipeEntity
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch

fun addMenuSnackBar(recipe: RecipeEntity, scaffoldState: ScaffoldState, coroutineScope: CoroutineScope){
    coroutineScope.launch {
        scaffoldState.snackbarHostState.showSnackbar(
            message = "Added " + recipe.recipeName + " to the Menu.",
            duration = SnackbarDuration.Short
        )
    }
}

fun removeMenuSnackBar(recipe: RecipeEntity, scaffoldState: ScaffoldState, coroutineScope: CoroutineScope){
    coroutineScope.launch {
        scaffoldState.snackbarHostState.showSnackbar(
            message = "Removed " + recipe.recipeName + " from the Menu.",
            duration = SnackbarDuration.Short
        )
    }

}

fun menuSnackBar(recipe: RecipeEntity, scaffoldState: ScaffoldState, coroutineScope: CoroutineScope){
    coroutineScope.launch{
        if(recipe.onMenu == 1)
            scaffoldState.snackbarHostState.showSnackbar(
                message = "Removed " + recipe.recipeName + " from Menu.",
                duration = SnackbarDuration.Short)
        else if(recipe.onMenu == 0)
            scaffoldState.snackbarHostState.showSnackbar(
                message = "Added " + recipe.recipeName + " to Menu.",
                duration = SnackbarDuration.Short)
    }

}

fun addFavoriteSnackBar(recipe: RecipeEntity, scaffoldState: ScaffoldState, coroutineScope: CoroutineScope){
    coroutineScope.launch{
        scaffoldState.snackbarHostState.showSnackbar(
            message = "Added ${recipe.recipeName} to Favorites.",
            duration = SnackbarDuration.Short)
    }

}

fun removeFavoriteSnackBar(recipe: RecipeEntity, scaffoldState: ScaffoldState, coroutineScope: CoroutineScope){
    coroutineScope.launch{
        scaffoldState.snackbarHostState.showSnackbar(
            message = "Removed " + recipe.recipeName + " from the Menu.",
            duration = SnackbarDuration.Short)

    }
}

fun favoriteSnackBar(recipe: RecipeEntity, scaffoldState: ScaffoldState, coroutineScope: CoroutineScope){
    coroutineScope.launch{
        if(recipe.isFavorite == 1)
            scaffoldState.snackbarHostState.showSnackbar(
                message = "Removed " + recipe.recipeName + " from Favorites.",
                duration = SnackbarDuration.Short)
        else if(recipe.isFavorite == 0)
            scaffoldState.snackbarHostState.showSnackbar(
                message = "Added " + recipe.recipeName + " to Favorites.",
                duration = SnackbarDuration.Short)
    }
}

fun completedCookingSnackBar(recipe: RecipeEntity, scaffoldState: ScaffoldState, coroutineScope: CoroutineScope){
    coroutineScope.launch{
        scaffoldState.snackbarHostState.showSnackbar(
            message = "Completed cooking " + recipe.recipeName + "!!",
            duration = SnackbarDuration.Short)
    }
}
