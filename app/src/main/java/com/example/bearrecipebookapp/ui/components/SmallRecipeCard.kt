package com.example.bearrecipebookapp.ui.components

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Favorite
import androidx.compose.material.icons.outlined.FavoriteBorder
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.TileMode
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.ExperimentalTextApi
import androidx.compose.ui.text.PlatformTextStyle
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.style.LineHeightStyle
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.em
import com.example.bearrecipebookapp.R
import com.example.bearrecipebookapp.data.IngredientEntity
import com.example.bearrecipebookapp.data.RecipeEntity
import com.example.bearrecipebookapp.ui.theme.BearRecipeBookAppTheme

@OptIn(ExperimentalTextApi::class)
@Composable
fun SmallRecipeCard(
    modifier: Modifier,
    recipe: RecipeEntity,
    ingredients: List<IngredientEntity>,
    onClick: ()->Unit,
    onDetailsClick: () -> Unit
){

    val gradientWidth = with(LocalDensity.current) { 170.dp.toPx() }
    val gradientWidthButton = with(LocalDensity.current) { 48.dp.toPx() }
    val gradientImageVertical = with(LocalDensity.current) { 135.dp.toPx() }

    var ingredientsListAsString = ingredients[0].ingredientName

    var image = R.drawable.bagel

    for(x in ingredients.indices)
    {
        if(x != 0){
            ingredientsListAsString = ingredientsListAsString + ", " + ingredients[x].ingredientName
        }
    }

    image = when(recipe.recipeName){
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


//    val gradientWidth = with(LocalDensity.current) {
//        (6 * (HighlightCardWidth + HighlightCardPadding).toPx())
//    }

    Surface(
        elevation = 4.dp,
        modifier = modifier
            .padding(top = 16.dp, bottom = 16.dp)
            .size(
                width = 170.dp,
                height = 270.dp
            )
            .clickable(onClick = onDetailsClick),
        shape = RoundedCornerShape(15.dp),
        color = Color(0xFFdfe0fb)
        //theme is assigning this value automatically so we don't need to assign it again here
        //color = MaterialTheme.colors.surface
    ){
        Column{
            Box(
                modifier = Modifier
                    .height(185.dp)
                    .fillMaxWidth()
            ){
                Box(
                    modifier = Modifier
                        .height(140.dp)
                        .fillMaxWidth()
                        //.background(color = MaterialTheme.colors.secondaryVariant)
                        .background(
                            Brush.horizontalGradient(
                                colors = listOf(
                                    Color(0xFFb15f33),
                                    Color(0xFF682300)
                                ),
                                endX = gradientWidth,
                                tileMode = TileMode.Mirror
                            )
                        )
                ){
                    Box(
                        Modifier
                            .fillMaxWidth()
                            .padding(4.dp)
                            .align(alignment = Alignment.TopCenter)
                            .height(40.dp)
                    ){
                        Text(
                            modifier = Modifier
                                .align(alignment = Alignment.Center)
                                //.padding(top = 4.dp)
                                .fillMaxWidth()
                                .wrapContentHeight(),
                            //recipe name
                            text = recipe.recipeName,
                            style = MaterialTheme.typography.h4.merge(
                                TextStyle(
                                    platformStyle = PlatformTextStyle(
                                        includeFontPadding = false
                                    ),
                                    lineHeightStyle = LineHeightStyle(
                                        alignment = LineHeightStyle.Alignment.Top,
                                        trim = LineHeightStyle.Trim.FirstLineTop))
                            ),
                            textAlign = TextAlign.Center
                        )
                    }

                }
                Spacer(modifier = Modifier
                    .align(Alignment.BottomCenter)
                    .padding(bottom = 2.dp)
                    .fillMaxWidth()
                    .height(2.dp)
                    .background(color = Color(0xFFE10600)))
            //Image Surface
                Surface(
                    modifier = Modifier
                        .align(Alignment.BottomCenter)
                        .size(135.dp)
                        .border(
                            width = 2.dp,
                            brush = (Brush.verticalGradient(
                                colors = listOf(
                                    Color(0xFFFFFFFF),
//                                //    Color(0xFFFFFFFF),
//                                    Color(0xFFE10600),
//                                    Color(0xFFFFFFFF),
//                                    Color(0xFF6769f1),
//                                    Color(0xFFFFFFFF),
//                                    Color(0xFFb15f33),
                                    Color(0xFFb15f33),
                                    Color(0xFFb15f33),
                                    Color(0xFFb15f33)
                                ),
                                endY = gradientImageVertical,
                                tileMode = TileMode.Mirror
                            )),
                            shape = CircleShape
                        ),
                    shape = CircleShape
                ){

                    Image(
                        modifier = Modifier
                            .fillMaxSize(),
                        contentScale = ContentScale.Crop,
                    /*
                    add correct image
                     */
                        painter = painterResource(image),
                        //painterResource(recipe.image),
                        contentDescription = null
                    )
                }

                /* TO DO:
                Here we should check if the recipe.on_menu is true.
                If it is true we don't need to update on click. Instead we send a Toast or Snackbar
                message saying this item is already in the list OR just do nothing.
                 */

                val icon: ImageVector

                if(recipe.onMenu == 0){
                    icon = Icons.Outlined.FavoriteBorder

                }else
                {
                    icon = Icons.Outlined.Favorite
                }

                FloatingActionButton(
                    onClick = onClick,
                    elevation = FloatingActionButtonDefaults.elevation(8.dp),
                    modifier = Modifier
                        .padding(top = 4.dp, end = 8.dp)
                        .border(
                            width = 2.dp,
                            brush = (Brush.horizontalGradient(
                                colors = listOf(
                                    Color(0xFFd8af84),
                                    Color(0xFFb15f33),

                                ),
                                endX = gradientWidthButton,
                                tileMode = TileMode.Mirror
                            )),
                            shape = CircleShape
                        )
                        .align(Alignment.BottomEnd)
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
//                FloatingActionButton(
//                    onClick = onClick,
//                    elevation = FloatingActionButtonDefaults.elevation(8.dp),
//                    modifier = Modifier
//                        .padding(top = 4.dp, end = 8.dp)
//                        .border(
//                            width = 4.dp,
//                            brush = (Brush.horizontalGradient(
//                                colors = listOf(
//                                    MaterialTheme.colors.secondaryVariant,
//                                    Color(0xFFb15f33)
//                                ),
//                                endX = gradientWidthButton,
//                                tileMode = TileMode.Mirror
//                            )),
//                            shape = CircleShape
//                        )
//                        .align(Alignment.BottomEnd)
//                        .size(48.dp)
//                        //the background of the square for this button, it stays a square even tho
//                        //we have shape = circle shape.  If this is not changed you see a solid
//                        //square for the "background" of this button.
//                        .background(color = Color.Transparent),
//                    shape = CircleShape,
//                   //this is the background color of the button after the "Shaping" is applied.
//                    //it is different then the background attribute above.
//                    backgroundColor = Color(0xFFb15f33)
//                ) {
//                    Icon(
//                        Icons.Outlined.Add,
//                        tint = Color(0xFFFFFFFF),
//                       // modifier = Modifier.background(color = Color(0xFFFFFFFF)),
//                        contentDescription = null
//                    )
//                }

            //Implement Async image later
//                AsyncImage(
//                    model = ImageRequest.Builder(LocalContext.current)
//                        .data(imageUrl)
//                        .crossfade(true)
//                        .build(),
//                    contentDescription = "",
//                    placeholder = painterResource(R.drawable.placeholder),
//                    modifier = Modifier.fillMaxSize(),
//                    contentScale = ContentScale.Crop,
//                )
            }
            Box(Modifier.fillMaxSize()) {



                Box(
                    Modifier
                        .padding(top = 17.dp)
                        .height(1.dp)
                        .fillMaxWidth()
                        .background(Color(0xFF6769f1))) {
                }
                Box(
                    Modifier
                        .padding(top = 38.dp)
                        .height(1.dp)
                        .fillMaxWidth()
                        .background(Color(0xFF6769f1))) {
                }
                Box(
                    Modifier
                        .padding(top = 59.dp)
                        .height(1.dp)
                        .fillMaxWidth()
                        .background(Color(0xFF6769f1))) {
                }
                Box(
                    Modifier
                        .padding(top = 80.dp)
                        .height(1.dp)
                        .fillMaxWidth()
                        .background(Color(0xFF6769f1))) {
                }
                Text(
                //text = "Butter, Yeast, Onion, Sesame Seeds, Red Pepper",
                    text = ingredientsListAsString,
                    style = MaterialTheme.typography.body2,
                    lineHeight = (1.5).em,
                    modifier = Modifier.padding(start = 2.dp, end = 2.dp)
                )
                /*
                        0/7 quantity owned/quantity needed
                 */
//                Surface(
//                    modifier = Modifier
//                        .align(Alignment.BottomEnd)
//                        .padding(bottom = 8.dp, start = 2.dp),
//                    color = Color.Transparent
//                ){
//                    Text(
//                        text = "0/7",
//                        style = MaterialTheme.typography.body2,
//                        color = MaterialTheme.colors.primary,
//                        fontSize = 12.sp,
//                        modifier = Modifier.padding(end = 2.dp)
//                    )
//                }

            }
        }

    }
}

@Preview("default")
@Preview("large font", fontScale = 2f)
@Composable
fun SnackCardPreview() {
    BearRecipeBookAppTheme {

        var myRE: RecipeEntity =
            RecipeEntity(recipeName = "Cauliflower Walnut Tacos", onMenu = 0,1, timeToMake = 60, rating = 98)
        var myList: List<IngredientEntity> = listOf<IngredientEntity>(
            IngredientEntity(ingredientName = "Ing. Name", quantityOwned = 0, quantityNeeded = 1)
        )

        Row{
        SmallRecipeCard(modifier = Modifier, recipe = myRE, onClick = { }, ingredients =  myList, onDetailsClick =  {})
        SmallRecipeCard(modifier = Modifier, recipe = myRE, onClick = { }, ingredients =  myList, onDetailsClick =  {})
        }

    }
}
