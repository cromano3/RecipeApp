package com.example.bearrecipebookapp.ui

import androidx.compose.runtime.Composable
import com.example.bearrecipebookapp.data.entity.RecipeEntity

@Composable
fun RecipeBookScreen(
    recipeList: List<RecipeEntity>,
    selectedRecipesList: List<RecipeEntity>,
   // currentScreen: String,
    onClick: (String) -> Unit){

//    //DrawRecipeList(recipeList)
//    LazyColumn{
//        items(recipeList){
//
//            var selected = false
//
//            for(x in 0 until selectedRecipesList.size){
//                if(selectedRecipesList[x].recipeName == it.recipeName){
//                    selected = true
//                }
//            }
//
//            RecipeCard(
//                recipe = it,
//                selected = selected,
//                currentScreen = "RecipeBookScreen",
//                onClick = { onClick(it.recipeName)}
//            )
//        }
//    }
}


//@Preview(showBackground = true, showSystemUi = true)
//@Composable
//fun DefaultPreview() {
//    BearRecipeBookAppTheme {
//        Surface(
//            modifier = Modifier.fillMaxSize(),
//            color = MaterialTheme.colors.background
//        ) {
//            RecipeBookScreen(onClick = {},
//                selectedRecipesList = listOf<Recipe>(),
//                recipeList = listOf()<Recipe>(),
//            )
//        }
//    }
//}