package com.example.bearrecipebookapp.ui

import androidx.activity.compose.BackHandler
import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Icon
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.AutoAwesome
import androidx.compose.material.icons.outlined.Star
import androidx.compose.material.icons.outlined.ThumbUp
import androidx.compose.material.icons.outlined.Timer
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.ExperimentalTextApi
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.bearrecipebookapp.R
import com.example.bearrecipebookapp.data.IngredientEntity
import com.example.bearrecipebookapp.data.InstructionEntity
import com.example.bearrecipebookapp.data.RecipeEntity
import com.example.bearrecipebookapp.datamodel.RecipeWithIngredients
import com.example.bearrecipebookapp.datamodel.RecipeWithIngredientsAndInstructions
import com.example.bearrecipebookapp.datamodel.RecipeWithInstructions
import com.example.bearrecipebookapp.ui.theme.BearRecipeBookAppTheme

@OptIn(ExperimentalTextApi::class)
@Composable
fun DetailsScreen(
    //recipeList: List<RecipeWithIngredients>,
    //recipeListWithInstructions: List<RecipeWithInstructions>,
    detailsScreenTarget: RecipeWithIngredientsAndInstructions,
    //onGoBackClick: (RecipeWithIngredients) -> Unit,
    onGoBackClick: () -> Unit,
    //onUpdateMenuClick: (RecipeWithIngredients) -> Unit
)

{


    val gradientWidth = with(LocalDensity.current) { 200.dp.toPx() }


//    var detailsScreenTarget = recipeList[0]
//
//
//    for(x in 0 until recipeList.size){
//        if(recipeList[x].recipeEntity.isDetailsScreenTarget == 1)
//            detailsScreenTarget = recipeList[x]
//    }

//    var detailsScreenTargetInstructions = recipeListWithInstructions[0]
//
//    for(x in 0 until recipeListWithInstructions.size){
//        if(recipeListWithInstructions[x].recipeEntity.isDetailsScreenTarget == 1)
//            detailsScreenTargetInstructions = recipeListWithInstructions[x]
//    }

   // BackHandler{ onGoBackClick(detailsScreenTarget) }
    BackHandler { onGoBackClick() }

    var image = R.drawable.bagel

    image = when(detailsScreenTarget.recipeEntity.recipeName){
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


    var formattedTime = ""
    var remainder = 0
    var quotient = 0

    if(detailsScreenTarget.recipeEntity.timeToMake <= 60){
        formattedTime = detailsScreenTarget.recipeEntity.timeToMake.toString() + " mins."
    }
    else{
        quotient = detailsScreenTarget.recipeEntity.timeToMake / 60
        remainder = detailsScreenTarget.recipeEntity.timeToMake % 60
        if(remainder == 0){
            formattedTime = "$quotient hrs."
        }
        else{
            if(quotient == 1){
                formattedTime = "$quotient hr. $remainder mins."
            }
            else{
                formattedTime = "$quotient hrs. $remainder mins."
            }
        }
    }




    val gradientWidthButton = with(LocalDensity.current) { 48.dp.toPx() }

    Surface(
        modifier = Modifier
            .fillMaxSize(),
        color = Color(0xFFd8af84)


    ) {
        Column(){
//            Row(
//                modifier = Modifier
//                    .fillMaxWidth()
//                    .height(54.dp)
//                    .background(Color(0xFF682300)),
//                verticalAlignment = Alignment.CenterVertically,
//                horizontalArrangement = Arrangement.Start
//            ){
//                IconButton(
//                    onClick = {onGoBackClick(detailsScreenTarget)},
//                    modifier =
//                    Modifier
//                        .size(48.dp)
//                        .align(Alignment.CenterVertically)
//                        .background(color = Color.Transparent),
//                    // color = Color.Transparent
//
//                ){
//                    Icon(
//                        imageVector = Icons.Outlined.ArrowBack,
//                        contentDescription = null,
//                        modifier = Modifier
//                            .size(24.dp),
//                        // .padding(start = 16.dp)
//                        // .clickable(onClick = {onGoBackClick(detailsScreenTarget)}),
//                        tint = Color(0xFFd8af84)
//
//
//                    )
//                }
//
//                Spacer(Modifier.weight(1f))
//
//
//                Text(
//                    text = detailsScreenTarget.recipeEntity.recipeName,
//                    modifier = Modifier.width(200.dp),
//                    //  .weight(1f),
//                    color = Color(0xFFd8af84),
//                    textAlign = TextAlign.Center,
//                    fontSize = 24.sp,
//                    fontFamily = Cabin,
//                    fontWeight = FontWeight.Bold,
//                    lineHeight = 1.0.em,
//                    style = MaterialTheme.typography.h4.merge(
//                        TextStyle(
//                            platformStyle = PlatformTextStyle(
//                                includeFontPadding = false
//                            ),
//                            lineHeightStyle = LineHeightStyle(
//                                alignment = LineHeightStyle.Alignment.Top,
//                                trim = LineHeightStyle.Trim.FirstLineTop)
//                        )
//                    ),
//
//                    )
//
//                Spacer(Modifier.weight(1f))
//
//                Icon(
//                    imageVector = Icons.Outlined.Share,
//                    contentDescription = null,
//                    modifier = Modifier
//                        .size(24.dp),
//                    tint = Color(0xFFd8af84)
//
//                )
//                Spacer(Modifier.size(16.dp))
//
//                val icon: ImageVector
//
//                if(detailsScreenTarget.recipeEntity.onMenu == 0){
//                    icon = Icons.Outlined.FavoriteBorder
//
//                }else
//                {
//                    icon = Icons.Outlined.Favorite
//                }
//
//                FloatingActionButton(
//                    onClick = {onUpdateMenuClick(detailsScreenTarget)},
//                    elevation = FloatingActionButtonDefaults.elevation(8.dp),
//                    modifier = Modifier
//                        .padding( end = 8.dp)
//                        .border(
//                            width = 2.dp,
//                            brush = (Brush.horizontalGradient(
//                                colors = listOf(
//                                    Color(0xFFd8af84),
//                                    Color(0xFFb15f33),
//
//                                    ),
//                                endX = gradientWidthButton,
//                                tileMode = TileMode.Mirror
//                            )),
//                            shape = CircleShape
//                        )
//                        // .align(Alignment.BottomEnd)
//                        .size(36.dp)
//                        //the background of the square for this button, it stays a square even tho
//                        //we have shape = circle shape.  If this is not changed you see a solid
//                        //square for the "background" of this button.
//                        .background(color = Color.Transparent),
//                    shape = CircleShape,
//                    //this is the background color of the button after the "Shaping" is applied.
//                    //it is different then the background attribute above.
//                    backgroundColor = Color(0xFF682300)
//                ) {
//                    Icon(
//                        icon,
//                        tint = Color(0xFFd8af84),
//                        modifier = Modifier.size(20.dp),
//                        // modifier = Modifier.background(color = Color(0xFFFFFFFF)),
//                        contentDescription = null
//                    )
//                }
//                Spacer(Modifier.size(8.dp))
//            }
        Column(
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(ScrollState(0), enabled = true),
            verticalArrangement = Arrangement.Top
        ) {
            Image(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(300.dp),
                contentScale = ContentScale.Crop,
                /*
                add correct image
                 */
                painter = painterResource(image),
                contentDescription = null
            )

            Row(
                modifier = Modifier
                    .height(IntrinsicSize.Min)
                    .fillMaxWidth()
            ) {
//                Text(
//                    text = "Ingredients List:",
//                    textDecoration = TextDecoration.Underline
//                )
//                LazyColumn(
//                    modifier = Modifier
//                        .weight(1f)
//                        .padding(start = 8.dp)
//                ){
//                    items(detailsScreenTarget.ingredientsList){
//                        Text(
//                            text = it.ingredientName
//                        )
//                    }
//                }

                Surface(
                    modifier = Modifier
                        .wrapContentSize()
                        .padding(4.dp)
                        .weight(1f),
                    shape = RoundedCornerShape(10.dp),
                    color = Color(0xFF682300),

                    ) {
                    Column() {
                        Text(
                            modifier = Modifier.padding(start = 8.dp, end = 8.dp, top = 8.dp),
                            text = "Ingredients List:",
                            textDecoration = TextDecoration.Underline,
                            color = Color(0xFFd8af84),
                            fontSize = 18.sp
                        )


                        for (x in 0 until detailsScreenTarget.ingredientsList.size) {
                            Text(
                                modifier = Modifier.padding(start = 8.dp, end = 8.dp),
                                text = detailsScreenTarget.ingredientsList[x].ingredientName,
                                color = Color(0xFFd8af84),
                                fontSize = 18.sp

                            )
                        }
                        Spacer(
                            modifier = Modifier
                                .background(color = Color.Transparent)
                                .height(8.dp)
                        )
                    }
                }



                Column(
                    modifier = Modifier
                        .weight(1f)
                        .fillMaxSize(),
                    verticalArrangement = Arrangement.Center,
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Surface(
                        modifier = Modifier
                            .padding(bottom = 8.dp)
                            .wrapContentSize(),

//                            .background(
//                                brush = Brush.horizontalGradient(
//                                    colors = listOf(Color(0xFF682300),Color(0xFFb15f33) ),
//                                    endX = gradientWidth,
//                                    tileMode = TileMode.Mirror
//                                ),
//                                shape = RoundedCornerShape((25.dp))
//                            ),
                        //   .clickable(enabled = !selected) { selected = !selected },
                        shape = RoundedCornerShape(10.dp),
                        color = Color(0xFFf8ea9a),
                        //color = Color(0xFF682300),//Color(0xFFd8af84),
                        contentColor = Color(0xFFd8af84),
                        elevation = 6.dp
                    ) {
                        Column(Modifier.padding(start = 4.dp)){

                            Row(verticalAlignment = Alignment.CenterVertically){
                                Icon(
                                    Icons.Outlined.Timer,
                                    tint = Color(0xFF000000),
                                    contentDescription = null
                                )
                                Text(
                                    text = "Time: " + formattedTime,
                                    modifier = Modifier
                                        .padding(start = 8.dp, end = 8.dp, top = 8.dp, bottom = 8.dp)
                                        .background(color = Color.Transparent),
                                    // .weight(1f),
                                    color = Color(0xFF682300),
                                    fontSize = 18.sp,
                                    textAlign = TextAlign.Center,
                                    fontWeight = FontWeight.Bold

                                )
                            }
                            //Difficulty
                            Row(verticalAlignment = Alignment.CenterVertically){
                                Icon(
                                    Icons.Outlined.AutoAwesome,
                                    tint = Color(0xFF000000),
                                    contentDescription = null
                                )
                                Text(
                                    text = "Difficulty: ${detailsScreenTarget.recipeEntity.difficulty}",
                                    modifier = Modifier
                                        .padding(start = 8.dp, end = 8.dp, top = 8.dp, bottom = 8.dp)
                                        .background(color = Color.Transparent),
                                    // .weight(1f),
                                    color = Color(0xFF682300),
                                    fontSize = 18.sp,
                                    textAlign = TextAlign.Center,
                                    fontWeight = FontWeight.Bold

                                )
                                for(x in 0 until detailsScreenTarget.recipeEntity.difficulty){
                                    Icon(
                                        Icons.Outlined.Star,
                                        tint = Color(0xFF000000),
                                        contentDescription = null
                                    )
                                }
                            }
                            //Rating
                            Row(verticalAlignment = Alignment.CenterVertically){
                                Icon(
                                    Icons.Outlined.ThumbUp,
                                    tint = Color(0xFF000000),
                                    contentDescription = null
                                )
                                Text(
                                    text = "Rating: ${detailsScreenTarget.recipeEntity.rating}" +"%",
                                    modifier = Modifier
                                        .padding(start = 8.dp, end = 8.dp, top = 8.dp, bottom = 8.dp)
                                        .background(color = Color.Transparent),
                                    // .weight(1f),
                                    color = Color(0xFF682300),
                                    fontSize = 18.sp,
                                    textAlign = TextAlign.Center,
                                    fontWeight = FontWeight.Bold

                                )
                            }
                        }


                    }

                    //Add to Shopping List
                    Surface(
                        modifier = Modifier
                            .padding(start = 4.dp, end = 8.dp)
                            .wrapContentSize(),

//                            .background(
//                                brush = Brush.horizontalGradient(
//                                    colors = listOf(Color(0xFF682300),Color(0xFFb15f33) ),
//                                    endX = gradientWidth,
//                                    tileMode = TileMode.Mirror
//                                ),
//                                shape = RoundedCornerShape((25.dp))
//                            ),
                        //   .clickable(enabled = !selected) { selected = !selected },
                        shape = RoundedCornerShape(25.dp),
                        color = Color(0xFFf8ea9a),
                        //color = Color(0xFF682300),//Color(0xFFd8af84),
                        contentColor = Color(0xFFd8af84),
                        elevation = 6.dp
                    ) {
                        Text(
                            text = "Add To Shopping List",
                            modifier = Modifier
                                .padding(start = 8.dp, end = 8.dp, top = 8.dp, bottom = 8.dp)
                                .background(color = Color.Transparent),
                               // .weight(1f),
                            color = Color(0xFF682300),
                            fontSize = 18.sp,
                            textAlign = TextAlign.Center,
                            fontWeight = FontWeight.Bold

                        )
                    }
                }
            }

            for (x in 0 until detailsScreenTarget.instructionsList.size) {
                Surface(
                    modifier = Modifier
                        .wrapContentHeight()
                        .fillMaxWidth()
                        .padding(16.dp)
                        .clickable {}
                        .align(Alignment.CenterHorizontally),

                    shape = RoundedCornerShape(10.dp),
                    elevation = 8.dp,
                    color = Color(0xFF682300)
                ) {
                    Text(
                        modifier = Modifier.padding(8.dp),
                        color = Color(0xFFd8af84),
                        text = detailsScreenTarget.instructionsList[x].instruction,
                        textAlign = TextAlign.Center
                    )
                }

            }
        }
    }
    }
}


@Preview
@Composable
fun MyPreview2(){
    BearRecipeBookAppTheme {

        var myRecipe: RecipeEntity = RecipeEntity(
            recipeName = "Cauliflower Walnut Tacos",
            onMenu = 0,
            1,
            rating = 98,
            timeToMake = 90,
            difficulty = 4
        )

        var ing1: IngredientEntity = IngredientEntity(ingredientName = "Ingredient 1", quantityOwned = 0, quantityNeeded = 0)
        var ing2: IngredientEntity = IngredientEntity(ingredientName = "Ingredient 2", quantityOwned = 0, quantityNeeded = 0)
        var ing3: IngredientEntity = IngredientEntity(ingredientName = "Ingredient 3", quantityOwned = 0, quantityNeeded = 0)

        var ins1: InstructionEntity = InstructionEntity(instructionID = 1, recipeID = "Bagels", instruction = "Munch it.")
        var ins2: InstructionEntity = InstructionEntity(instructionID = 1, recipeID = "Bagels", instruction = "Munch it!.")
        var ins3: InstructionEntity = InstructionEntity(instructionID = 1, recipeID = "Bagels", instruction = "Munch it!!.")

        var recList = listOf(
            RecipeWithIngredients(myRecipe,listOf(ing1, ing2, ing3)),
            RecipeWithIngredients(myRecipe,listOf(ing1, ing2, ing3)))

        var recInsList = listOf(
            RecipeWithInstructions(myRecipe, listOf(ins1, ins2, ins3)),
            RecipeWithInstructions(myRecipe, listOf(ins1, ins2, ins3)),
        )

        var recipeAll = RecipeWithIngredientsAndInstructions(myRecipe,listOf(ing1, ing2,ing1, ing2, ing1, ing2, ing1 ,ing2), listOf(ins1, ins2))

        DetailsScreen(
            //recList,
            // recInsList,
            recipeAll,
            // recList[0],
            {},
            //{}
            )
    }
}