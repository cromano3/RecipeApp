package com.example.bearrecipebookapp.ui.components

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Close
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Shadow
import androidx.compose.ui.graphics.TileMode
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.bearrecipebookapp.R
import com.example.bearrecipebookapp.data.IngredientEntity
import com.example.bearrecipebookapp.data.RecipeEntity
import com.example.bearrecipebookapp.datamodel.RecipeWithIngredients
import com.example.bearrecipebookapp.ui.theme.BearRecipeBookAppTheme

//the recipe card with name, ingredients, image, and instructions
@Composable
fun RecipeCard(
    modifier: Modifier,
    recipeWithIngredients: RecipeWithIngredients,
    currentScreen: String,
    onClick: () -> Unit,
    onDetailsClick: () -> Unit,
    ){

    //temporary variable change to Parameter
    val pantry = listOf("Garlic", "Onion", "Flour", "Yeast", "Garlic Powder", "Vegan Butter")


    var expanded by remember { mutableStateOf(false) }


    var image = R.drawable.bagel

    image = when(recipeWithIngredients.recipeEntity.recipeName){
        "Bagels" -> R.drawable.bagel2
        "Garlic Knots" -> R.drawable.garlic2
        "Cauliflower Walnut Tacos" -> R.drawable.cauliflower
        "Lentil Sweet Potato Curry" -> R.drawable.curry
        "Thai Style Peanut Soup" -> R.drawable.thaisoup
        "Yummy Rice with Marinated Tofu" -> R.drawable.yummyrice
        "Corn Chowder" -> R.drawable.cornchowder
        "Vegan Eggplant Parmesan" -> R.drawable.eggplant
        "Mexican Style Rice" -> R.drawable.mexicanrice
        "Wild Rice Salad" -> R.drawable.wildrice
        "Rice Soup" -> R.drawable.ricesoup
        else -> R.drawable.bagel
    }




    val gradientWidth = with(LocalDensity.current) { 400.dp.toPx() }

    Column(){
        Surface(
            modifier = Modifier
                .padding(start = 8.dp, end = 8.dp, top = 16.dp,)
                .height(176.dp)
                .fillMaxWidth()
                .clickable(onClick = onDetailsClick),
            shape = RoundedCornerShape(10.dp),
            elevation = 8.dp,
        )
        {
            Image(
                modifier = Modifier
                    .height(176.dp)
                    .fillMaxWidth()
                    .clip(RoundedCornerShape(10.dp)),
                contentScale = ContentScale.Crop,
                painter = painterResource(image),
                contentDescription = null
            )
            Box(
                modifier = Modifier
                    .fillMaxSize(),
            ){
                Text(
                    modifier = Modifier.align(Alignment.Center),
                    text = recipeWithIngredients.recipeEntity.recipeName,
                    fontSize = 36.sp,
                    color = Color(0xFFFFFFFF),
                    textAlign = TextAlign.Center,
                    style = MaterialTheme.typography.h4.copy(
                        shadow = Shadow(
                            color = Color.Black,
                            offset = Offset(4f, 4f),
                            blurRadius = 8f
                        )
                    )
                )
            }
            //        Top Right corner delete button
            Box(
                modifier = Modifier
                    .fillMaxSize(),
            ){

                if (currentScreen == "WeeklyMenuScreen"){
                    IconButton(
                        modifier = Modifier
                            .align(Alignment.TopEnd)
                            .size(36.dp),
                        onClick = onClick
                    )
                    {
                        Icon(
                            modifier = Modifier,
                            imageVector = Icons.Outlined.Close,
                            tint = Color(0xFF000000),
                            contentDescription = null
                        )
                    }
                }
            }
        }
        Surface(
            modifier = modifier
                .padding(start = 8.dp , end = 8.dp)
                .height(40.dp)
                .fillMaxWidth()
                .background(
                    brush = Brush.horizontalGradient(
                        colors = listOf(Color(0xFF682300),Color(0xFFb15f33) ),
                        endX = gradientWidth,
                        tileMode = TileMode.Mirror
                    ),
                    shape = RoundedCornerShape((10.dp))
                ),
            shape = RoundedCornerShape(10.dp),
            elevation = 8.dp
        ){
            Box()
            {
                Text(
                    text = "stuff"
                )

            }
        }



            /*
            Title of Recipe
             */




//      Center Screen recipe name


    }
}


@Composable
private fun RowHelper(list: List<IngredientEntity>, pantry: List<String>) {

    var textColor = MaterialTheme.colors.primary

    Row(modifier = Modifier.fillMaxWidth()) {
        for(x in list.indices){
            textColor = if (!pantry.contains(list[x].ingredientName)) {
                MaterialTheme.colors.primaryVariant
            } else {
                MaterialTheme.colors.primary
            }
            Text(
                modifier = Modifier.weight(1f),
                text = list[x].ingredientName,
                textAlign = TextAlign.Center,
                style = MaterialTheme.typography.body1,
                color = textColor
            )
        }
    }
}



//**Currently unused (Checkbox add/remove menu button)
//@Composable
//private fun AddToMenuButton(
//    selected: Boolean,
//    onClick: () -> Unit,
//) {
//    IconButton(onClick = onClick){
//        Icon(
//            modifier = Modifier
//                .padding(top = 14.dp, start = 12.dp, end = 12.dp, bottom = 12.dp)
//                .size(36.dp),
//            imageVector = if (selected) Icons.Filled.CheckBox else Icons.Filled.CheckBoxOutlineBlank,
//            tint = MaterialTheme.colors.primary,
//            contentDescription = null
//        )
//    }
//}

//@Composable
//private fun ExpandInstructionsButton(
//    expanded: Boolean,
//    onClick: () -> Unit,
//    modifier: Modifier = Modifier
//) {
//    IconButton(onClick = onClick){
//        Icon(
//            modifier = modifier
//                .background(
//                    color = MaterialTheme.colors.primary,
//                    shape = MaterialTheme.shapes.small),
//            imageVector = if (expanded) Icons.Filled.ExpandLess else Icons.Outlined.ExpandMore,
//            tint = MaterialTheme.colors.surface,
//            contentDescription = null
//        )
//    }
//}


@Preview(showBackground = true, showSystemUi = true)
@Composable
fun DefaultPreview2() {
    BearRecipeBookAppTheme {

            var myRE: RecipeEntity = RecipeEntity(recipeName = "Cauliflower Walnut Tacos",
                onMenu = 0,1, timeToMake = 60, rating = 98)

            var myList: List<IngredientEntity> = listOf<IngredientEntity>(
                IngredientEntity(ingredientName = "Ing. Name", quantityOwned = 0, quantityNeeded = 1))

        Surface(
            modifier = Modifier.fillMaxSize(),
            color = Color(0xFFd8af84)

        ) {
            RecipeCard(
                modifier = Modifier,
                recipeWithIngredients = RecipeWithIngredients(
                    recipeEntity = myRE,
                    ingredientsList = myList
                ),
                currentScreen = "WeeklyMenuScreen",
                onClick = {},
                onDetailsClick = {}
            )
        }
    }
}