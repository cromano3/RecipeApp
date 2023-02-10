package com.example.bearrecipebookapp.ui.components

import androidx.compose.material.ScaffoldState
import androidx.compose.material.SnackbarDuration
import com.example.bearrecipebookapp.data.entity.RecipeEntity
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch

fun addMenuSnackBar(){

}

fun removeMenuSnackBar(){

}

fun menuSnackBar(){

}

fun addFavoriteSnackBar(){

}

fun removeFavoriteSnackBar(){

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