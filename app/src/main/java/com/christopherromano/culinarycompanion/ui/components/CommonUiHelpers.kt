package com.christopherromano.culinarycompanion.ui.components

import android.content.Intent
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Shadow
import androidx.compose.ui.graphics.TileMode
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import com.christopherromano.culinarycompanion.R
import com.christopherromano.culinarycompanion.data.entity.IngredientEntity
import com.christopherromano.culinarycompanion.data.entity.RecipeEntity
import com.christopherromano.culinarycompanion.datamodel.RecipeWithIngredientsAndInstructions
import com.christopherromano.culinarycompanion.ui.theme.CulinaryCompanionTheme


//the recipe card with name, ingredients, image, and instructions
@Composable
fun RecipeCard(
    modifier: Modifier,
    recipeWithIngredientsAndInstructions: RecipeWithIngredientsAndInstructions,
    currentScreen: String,
    onFavoriteClick: () -> Unit,
    onRemoveClick: () -> Unit,
    onCompleteClick: () -> Unit,
    onDetailsClick: () -> Unit,
    ){

    //temporary variable change to Parameter
//    val pantry = listOf("Garlic", "Onion", "Flour", "Yeast", "Garlic Powder", "Vegan Butter")


//    var expanded by remember { mutableStateOf(false) }


//    var image = R.drawable.bagel

    val image = when(recipeWithIngredientsAndInstructions.recipeEntity.recipeName){
        "Bagels" -> R.drawable.rice_soup2
        "Garlic Knots" -> R.drawable.garlic2
        "Cauliflower Walnut Tacos" -> R.drawable.cauliflower
        "Lentil Sweet Potato Curry" -> R.drawable.garlic
        "Thai Style Peanut Soup" -> R.drawable.garlic
        "Yummy Rice with Marinated Tofu" -> R.drawable.garlic
        "Corn Chowder" -> R.drawable.corn_chowder
        "Vegan Eggplant Parmesan" -> R.drawable.eggplant
        "Mexican Style Rice" -> R.drawable.garlic
        "Wild Rice Salad" -> R.drawable.garlic
        "Rice Soup" -> R.drawable.rice_soup
        "Miso Soup" -> R.drawable.miso_soup
        "Sweet Potato Tortilla" -> R.drawable.sweet_potato_tortilla
        "Tumbet" -> R.drawable.tumbet
        "Chinese Eggplant" -> R.drawable.garlic
        "Coca De Prebes" -> R.drawable.garlic
        "Banana Walnut Pancakes" -> R.drawable.garlic
        "Huevos Rotos" -> R.drawable.garlic
        else -> R.drawable.garlic
    }

    /*
    format the "Time to make" from raw Int minutes to "X hr./hrs. Y mins."
     */
    val formattedTime: String
    val remainder: Int
    val quotient: Int

    if (recipeWithIngredientsAndInstructions.recipeEntity.timeToMake <= 60) {
        formattedTime = recipeWithIngredientsAndInstructions.recipeEntity.timeToMake.toString() + " mins."
    } else {
        quotient = recipeWithIngredientsAndInstructions.recipeEntity.timeToMake / 60
        remainder = recipeWithIngredientsAndInstructions.recipeEntity.timeToMake % 60
        if (remainder == 0) {
            formattedTime = "$quotient hrs."
        } else {
            if (quotient == 1) {
                formattedTime = "$quotient hr.\n $remainder mins."
            } else {
                formattedTime = "$quotient hrs.\n $remainder mins."
            }
        }
    }


    Column(modifier = modifier){

        Surface(
            modifier = Modifier
                .padding(start = 8.dp, end = 8.dp, top = 16.dp,)
                .height(176.dp)
                .fillMaxWidth()
                .clickable(onClick = onDetailsClick)
                .background(
                    brush = Brush.horizontalGradient(
                        colors = listOf(Color(0xFFb15f33), Color(0xFF682300)),
                        tileMode = TileMode.Mirror
                    ),
                    shape = if (currentScreen == "FavoritesTab") RoundedCornerShape(
                        10.dp,
                        10.dp,
                        10.dp,
                        10.dp
                    ) else RoundedCornerShape(10.dp, 10.dp, 0.dp, 0.dp),
                ),
            shape = if(currentScreen == "FavoritesTab" ) RoundedCornerShape(10.dp, 10.dp, 10.dp, 10.dp) else RoundedCornerShape(10.dp, 10.dp, 0.dp, 0.dp),
            color = Color.Transparent,
//            elevation = 8.dp,
            )
        {
            Row(modifier = Modifier.fillMaxSize()){
                //Left Side Column
                if(currentScreen != "CookedTab"){
                    Column(
                        Modifier
                            .padding(start = 0.dp)
                            .width(60.dp)
                            .fillMaxHeight(),
                        verticalArrangement = Arrangement.SpaceBetween,
                        horizontalAlignment = Alignment.CenterHorizontally,
                    ) {

                        //Time
                        Column(
                            Modifier
                                .wrapContentHeight()
                                .fillMaxWidth()
                                .padding(top = 4.dp),
                            verticalArrangement = Arrangement.SpaceBetween,
                            horizontalAlignment = Alignment.CenterHorizontally,
                        ){
                            Icon(
                                Icons.Outlined.Timer,
                                contentDescription = null,
                                tint = Color(0xFF000000),
                                modifier = Modifier
                                    .size(20.dp),
                            )
                            Text(
                                text = formattedTime,
                                modifier = Modifier
                                    .padding(
                                        start = 0.dp,
                                        end = 0.dp,
                                        top = 0.dp,
                                        bottom = 0.dp
                                    )
                                    .background(color = Color.Transparent),
                                // .weight(1f),
                                color = Color(0xFF000000),
                                fontSize = 14.sp,
                                textAlign = TextAlign.Center,
                                fontWeight = FontWeight.Bold
                            )
                        }

                        //Difficulty
                        Column(
                            Modifier
                                .wrapContentHeight()
                                .fillMaxWidth(),
                            verticalArrangement = Arrangement.SpaceBetween,
                            horizontalAlignment = Alignment.CenterHorizontally,
                        ){
                            Icon(
                                Icons.Outlined.AutoAwesome,
                                contentDescription = null,
                                modifier = Modifier.size(20.dp),
                                tint = Color(0xFF000000),
                            )
                            Row(){
                                Icon(
                                    Icons.Outlined.Star,
                                    contentDescription = null,
                                    modifier = Modifier.size(20.dp),
                                    tint = Color(0xFF000000),
                                )
                                if(recipeWithIngredientsAndInstructions.recipeEntity.difficulty > 1){
                                    Icon(
                                        Icons.Outlined.Star,
                                        contentDescription = null,
                                        modifier = Modifier.size(20.dp),
                                        tint = Color(0xFF000000),
                                    )
                                }
                            }
                            if(recipeWithIngredientsAndInstructions.recipeEntity.difficulty > 2){
                                Icon(
                                    Icons.Outlined.Star,
                                    contentDescription = null,
                                    modifier = Modifier.size(20.dp),
                                    tint = Color(0xFF000000),
                                )
                            }
                        }

                        //Rating
                        Column(
                            Modifier
                                .wrapContentHeight()
                                .fillMaxWidth()
                                .padding(bottom = 4.dp),
                            verticalArrangement = Arrangement.SpaceBetween,
                            horizontalAlignment = Alignment.CenterHorizontally,
                        ){
                            Icon(
                                Icons.Outlined.ThumbUp,
                                contentDescription = null,
                                modifier = Modifier
                                    .padding(top = 0.dp)
                                    .size(20.dp),
                                tint = Color(0xFF000000),

                            )
                            Text(
                                text = "${recipeWithIngredientsAndInstructions.recipeEntity.globalRating}",
                                modifier = Modifier
                                    .padding(
                                        start = 0.dp,
                                        end = 0.dp,
                                        top = 0.dp,
                                        bottom = 0.dp
                                    )
                                    .background(color = Color.Transparent),
                                // .weight(1f),
                                color = Color(0xFF000000),
                                fontSize = 14.sp,
                                textAlign = TextAlign.Center,
                                fontWeight = FontWeight.Bold
                            )
                        }
                    }
                }

                Spacer(
                    Modifier
                        .fillMaxHeight()
                        .border(1.dp, Color(0xFFd8af84))
                        .width(1.dp))

                AsyncImage(
                    model = image,
                    contentDescription = null,
                    modifier = Modifier
                        .weight(1f)
                        .height(176.dp)
//                        .width(200.dp)
//                        .fillMaxWidth()
                        .clip(RoundedCornerShape(0.dp)),
//                        .border(1.dp, Color(0xFFd8af84)),
                    contentScale = ContentScale.Crop,
                )

                Spacer(
                    Modifier
                        .fillMaxHeight()
                        .border(1.dp, Color(0xFFd8af84))
                        .width(1.dp))

                //Right Side Column
                if(currentScreen != "CookedTab") {
                    Column(
                        Modifier
                            .padding(start = 0.dp)
                            .width(60.dp)
                            .fillMaxHeight(),
                        verticalArrangement = Arrangement.SpaceAround,
                        horizontalAlignment = Alignment.CenterHorizontally,
                    ) {

                        val gradientWidthButton = with(LocalDensity.current) { 48.dp.toPx() }

                        val icon: ImageVector =
                            if (recipeWithIngredientsAndInstructions.recipeEntity.isFavorite == 0) {
                                Icons.Outlined.FavoriteBorder

                            } else {
                                Icons.Outlined.Favorite
                            }

                        FloatingActionButton(
                            onClick = onFavoriteClick,
                            elevation = FloatingActionButtonDefaults.elevation(8.dp),
                            modifier = Modifier
//                            .padding(end = 8.dp)
                                .border(
                                    width = 2.dp,
                                    brush = (Brush.horizontalGradient(
                                        colors = listOf(Color(0xFFd8af84), Color(0xFFb15f33)),
                                        endX = gradientWidthButton,
                                        tileMode = TileMode.Mirror
                                    )),
                                    shape = CircleShape
                                )
                                .size(36.dp)
                                //the background of the square for this button, it stays a square even tho
                                //we have shape = circle shape.  If this is not changed you see a solid
                                //square for the "background" of this button.
                                .background(color = Color.Transparent),
                            shape = CircleShape,
                            //this is the background color of the button after the "Shaping" is applied.
                            //it is different then the background attribute above.
                            backgroundColor = Color(0xFF682300)
                        ) {
                            Icon(
                                icon,
                                tint = Color(0xFFd8af84),
                                modifier = Modifier.size(20.dp),
                                // modifier = Modifier.background(color = Color(0xFFFFFFFF)),
                                contentDescription = null
                            )
                        }

                        val context = LocalContext.current


                        IconButton(
                            onClick = {

                                var myString = recipeWithIngredientsAndInstructions.recipeEntity.recipeName + '\n' + '\n'

                                for(x in recipeWithIngredientsAndInstructions.ingredientsList.indices){
                                    myString += recipeWithIngredientsAndInstructions.ingredientsList[x].ingredientName + '\n'
                                }

                                myString += '\n'

                                for(x in recipeWithIngredientsAndInstructions.instructionsList.indices){
                                    myString += recipeWithIngredientsAndInstructions.instructionsList[x].instruction + '\n'
                                }

                                val sendIntent: Intent = Intent().apply {
                                    action = Intent.ACTION_SEND
                                    putExtra(Intent.EXTRA_TEXT, myString)
//                                    putExtra(Intent.EXTRA_TITLE, detailsScreenData.recipeEntity.recipeName)
                                    type = "text/plain"
                                }

                                val shareIntent = Intent.createChooser(sendIntent, null)

                                context.startActivity(shareIntent)

                            },
                            modifier = Modifier
                                .background(color = Color.Transparent)
                                .padding(start = 0.dp)
                                .border(
                                    width = 2.dp,
                                    brush = (Brush.horizontalGradient(
                                        colors = listOf(Color(0xFFd8af84), Color(0xFFb15f33)),
                                        tileMode = TileMode.Mirror,
                                    )),
                                    shape = CircleShape
                                )
                                .size(36.dp),
                            // color = Color.Transparent

                        ) {
                            Icon(
                                imageVector = Icons.Outlined.Share,
                                contentDescription = null,
                                modifier = Modifier
                                    .size(20.dp),
                                tint = Color(0xFFd8af84)
                            )
                        }


//                        Icon(
//                            imageVector = Icons.Outlined.Share,
//                            contentDescription = null,
//                            modifier = Modifier
//                                .size(36.dp),
//                            tint = Color(0xFFd8af84)
//                        )

                    }
                }

            }

            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(start = 64.dp, end = 64.dp),
            ){
                Text(
                    modifier = Modifier.align(Alignment.Center),
                    text = recipeWithIngredientsAndInstructions.recipeEntity.recipeName,
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
        }

        Spacer(
            Modifier
                .fillMaxWidth()
                .border(1.dp, Color(0xFFd8af84))
                .height(1.dp))

        //Bottom Bar surface
        if(currentScreen != "FavoritesTab" && currentScreen != "CookedTab"){

            BottomBarSurface(onRemoveClick, onCompleteClick)

        }
    }
}


@Composable

fun BottomBarSurface(
    onRemoveClick: () -> Unit,
    onCompleteClick: () -> Unit,
){
    Surface(
        modifier =
        Modifier
            .padding(start = 8.dp, end = 8.dp)
            .height(60.dp)
            .fillMaxWidth()
            .background(
                brush = Brush.horizontalGradient(
                    colors = listOf(Color(0xFFb15f33), Color(0xFF682300)),
                    tileMode = TileMode.Mirror
                ),
                shape = RoundedCornerShape(0.dp, 0.dp, 10.dp, 10.dp)
            ),
        shape = RoundedCornerShape(0.dp, 0.dp, 10.dp, 10.dp),

        color = Color.Transparent,

        ){
        Row(
            Modifier
                .padding(start = 4.dp, end = 4.dp)
                .fillMaxSize(),
            horizontalArrangement = Arrangement.SpaceEvenly,
            verticalAlignment = Alignment.CenterVertically,
        ) {

            DetailsScreenButton(
                onClick = onRemoveClick,
                borderStartColor = Color(0xFFb15f33),
                borderEndColor = Color(0xFF682300),
                textColor = Color(0xFF682300),
                backgroundColor = Color(0xFFd8af84),
                buttonText = "Remove"
            )

            DetailsScreenButton(
                onClick = onCompleteClick,
                borderStartColor = Color(0xFFd8af84),
                borderEndColor = Color(0xFFb15f33),
                textColor = Color(0xFFd8af84),
                backgroundColor = Color(0xFF682300),
                buttonText = "Finished Cooking!"
            )

        }
    }
}


//@Composable
//private fun RowHelper(list: List<IngredientEntity>, pantry: List<String>) {
//
//    var textColor = MaterialTheme.colors.primary
//
//    Row(modifier = Modifier.fillMaxWidth()) {
//        for(x in list.indices){
//            textColor = if (!pantry.contains(list[x].ingredientName)) {
//                MaterialTheme.colors.primaryVariant
//            } else {
//                MaterialTheme.colors.primary
//            }
//            Text(
//                modifier = Modifier.weight(1f),
//                text = list[x].ingredientName,
//                textAlign = TextAlign.Center,
//                style = MaterialTheme.typography.body1,
//                color = textColor
//            )
//        }
//    }
//}



//*Currently unused (Checkbox add/remove menu button)
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
    CulinaryCompanionTheme {

            val myRE = RecipeEntity(recipeName = "Cauliflower Walnut Tacos",
                onMenu = 0, isDetailsScreenTarget = 1, timeToMake = 60, globalRating = 98, difficulty = 3)

            val myList: List<IngredientEntity> = listOf<IngredientEntity>(
                IngredientEntity(ingredientName = "Ing. Name", quantityOwned = 0, quantityNeeded = 1)
            )

        Surface(
            modifier = Modifier.fillMaxSize(),
            color = Color(0xFFd8af84)

        ) {
            RecipeCard(
                modifier = Modifier,
                recipeWithIngredientsAndInstructions = RecipeWithIngredientsAndInstructions(
                    recipeEntity = myRE,
                    ingredientsList = myList
                ),
                currentScreen = "WeeklyMenuScreen",
                onCompleteClick = {},
                onRemoveClick = {},
                onDetailsClick = {},
                onFavoriteClick = {},
            )
        }
    }
}