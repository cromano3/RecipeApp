package com.example.bearrecipebookapp.ui

import androidx.activity.compose.BackHandler
import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CheckBox
import androidx.compose.material.icons.outlined.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
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
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.LineHeightStyle
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.em
import androidx.compose.ui.unit.sp
import com.example.bearrecipebookapp.R
import com.example.bearrecipebookapp.data.IngredientEntity
import com.example.bearrecipebookapp.data.InstructionEntity
import com.example.bearrecipebookapp.data.RecipeEntity
import com.example.bearrecipebookapp.datamodel.RecipeWithIngredients
import com.example.bearrecipebookapp.datamodel.RecipeWithIngredientsAndInstructions
import com.example.bearrecipebookapp.datamodel.RecipeWithInstructions
import com.example.bearrecipebookapp.ui.theme.BearRecipeBookAppTheme
import com.example.bearrecipebookapp.ui.theme.Cabin

@OptIn(ExperimentalTextApi::class)
@Composable
fun DetailsScreen(
    //recipeList: List<RecipeWithIngredients>,
    //recipeListWithInstructions: List<RecipeWithInstructions>,
    detailsScreenTarget: RecipeWithIngredientsAndInstructions,
    detailsScreenTargetOnMenu: Int,
    detailsScreenTargetIngredients: List<IngredientEntity>,
    detailsScreenTargetInstructions: List<InstructionEntity>,
    //onGoBackClick: (RecipeWithIngredients) -> Unit,
    onGoBackClick: () -> Unit,
    onUpdateMenuClick: (RecipeWithIngredientsAndInstructions) -> Unit
)

{


//    val gradientWidth = with(LocalDensity.current) { 200.dp.toPx() }


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

    val image: Int = when(detailsScreenTarget.recipeEntity.recipeName){
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


    val formattedTime: String
    val remainder: Int
    val quotient: Int

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


    Surface(
        modifier = Modifier
            .fillMaxSize(),
        color = Color(0xFFd8af84)


    ) {
        BackHandler { onGoBackClick() }
Column {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .height(48.dp)
            .background(Color(0xFF682300)),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.Start
    ) {
        IconButton(
            // onClick = {onGoBackClick(detailsScreenTarget)},
            onClick = { onGoBackClick() },
            modifier =
            Modifier
                .size(48.dp)
                .align(Alignment.CenterVertically)
                .background(color = Color.Transparent),
            // color = Color.Transparent

        ) {
            Icon(
                imageVector = Icons.Outlined.ArrowBack,
                contentDescription = null,
                modifier = Modifier
                    .size(24.dp),
                // .padding(start = 16.dp)
                // .clickable(onClick = {onGoBackClick(detailsScreenTarget)}),
                tint = Color(0xFFd8af84)


            )
        }

        Spacer(Modifier.weight(1f))


        Text(
            text = detailsScreenTarget.recipeEntity.recipeName,
            modifier = Modifier.width(200.dp),
            //  .weight(1f),
            color = Color(0xFFd8af84),
            textAlign = TextAlign.Center,
            fontSize = 22.sp,
            fontFamily = Cabin,
            fontWeight = FontWeight.Bold,
            lineHeight = 1.0.em,
            style = MaterialTheme.typography.h4.merge(
                TextStyle(
                    platformStyle = PlatformTextStyle(
                        includeFontPadding = false
                    ),
                    lineHeightStyle = LineHeightStyle(
                        alignment = LineHeightStyle.Alignment.Top,
                        trim = LineHeightStyle.Trim.FirstLineTop
                    )
                )
            ),

            )

        Spacer(Modifier.weight(1f))

        Icon(
            imageVector = Icons.Outlined.Share,
            contentDescription = null,
            modifier = Modifier
                .size(24.dp),
            tint = Color(0xFFd8af84)

        )
        Spacer(Modifier.size(16.dp))

        var icon: ImageVector
        val gradientWidthButton = with(LocalDensity.current) { 48.dp.toPx() }

        if (detailsScreenTargetOnMenu == 0) {
            icon = Icons.Outlined.FavoriteBorder

        } else {
            icon = Icons.Outlined.Favorite
        }

        FloatingActionButton(
            onClick = { onUpdateMenuClick(detailsScreenTarget) },
            elevation = FloatingActionButtonDefaults.elevation(8.dp),
            modifier = Modifier
                .padding(end = 8.dp)
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
                // .align(Alignment.BottomEnd)
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
        Spacer(Modifier.size(8.dp))
    }

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
        verticalArrangement = Arrangement.Top,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Image(
            modifier = Modifier
                .fillMaxWidth()
                .height(300.dp)
                .padding(bottom = 16.dp),
            contentScale = ContentScale.Crop,
            /*
                add correct image
                 */
            painter = painterResource(image),
            contentDescription = null
        )

        // Row(
//                modifier = Modifier
//                    .height(IntrinsicSize.Min)
//                    .fillMaxWidth()
//            ) {

        Column(
            modifier = Modifier
                // .weight(1f)
                .fillMaxSize(),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            //Difficulty box
            Surface(
                modifier = Modifier
                    .padding(bottom = 16.dp)
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
                //Difficulty Column
                Column(
                    Modifier.padding(start = 4.dp)
                ) {

                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(
                            Icons.Outlined.Timer,
                            tint = Color(0xFF000000),
                            contentDescription = null
                        )
                        Text(
                            text = "Time: $formattedTime",
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
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        modifier = Modifier.padding(end = 4.dp)
                    ) {
                        Icon(
                            Icons.Outlined.AutoAwesome,
                            tint = Color(0xFF000000),
                            contentDescription = null
                        )
                        Text(
                            text = "Difficulty: ",
                            modifier = Modifier
                                .padding(start = 8.dp, end = 0.dp, top = 8.dp, bottom = 8.dp)
                                .background(color = Color.Transparent),
                            // .weight(1f),
                            color = Color(0xFF682300),
                            fontSize = 18.sp,
                            textAlign = TextAlign.Center,
                            fontWeight = FontWeight.Bold

                        )
                        for (x in 0 until detailsScreenTarget.recipeEntity.difficulty) {
                            Icon(
                                Icons.Outlined.Star,
                                tint = Color(0xFF000000),
                                contentDescription = null
                            )
                        }
                    }
                    //Rating
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(
                            Icons.Outlined.ThumbUp,
                            tint = Color(0xFF000000),
                            contentDescription = null
                        )
                        Text(
                            text = "Rating: ${detailsScreenTarget.recipeEntity.rating}" + "%",
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

            //Ingredients List //
            Surface(
                modifier = Modifier
                    .wrapContentSize()
                    .padding(bottom = 16.dp),
                //    .weight(1f),
                shape = RoundedCornerShape(10.dp),
                color = Color(0xFF682300),

                ) {
                Column(
                    Modifier.padding(top = 8.dp, bottom = 8.dp, start = 16.dp, end = 16.dp)
                ) {
                    Text(
                        modifier = Modifier.padding(bottom = 8.dp)
                            .align(Alignment.CenterHorizontally),
                        text = "Ingredients List:",
                        textDecoration = TextDecoration.Underline,
                        color = Color(0xFFd8af84),
                        fontSize = 18.sp
                    )


                    for (x in 0 until detailsScreenTarget.ingredientsList.size) {
                        Text(
                            //modifier = Modifier.padding(start = 8.dp, end = 8.dp),
                            text = "- " + detailsScreenTarget.ingredientsList[x].ingredientName,
                            color = Color(0xFFd8af84),
                            fontSize = 18.sp

                        )
                    }
//                            Spacer(
//                                modifier = Modifier
//                                    .background(color = Color.Transparent)
//                                    .height(8.dp)
//                            )
                }
            }


            //////////////////////

            val selected: Boolean


            val myIcon: ImageVector
            val checkBoxBackgroundColor: Color
            val decoration: TextDecoration
            val alphaLevel: Float


            if (detailsScreenTarget.recipeEntity.onMenu == 1) {

                selected = true
                myIcon = Icons.Filled.CheckBox
                checkBoxBackgroundColor = Color(0xFF682300)
                decoration = TextDecoration.LineThrough
                alphaLevel = 0.55f


            } else {

                selected = false
                myIcon = Icons.Outlined.CheckBoxOutlineBlank
                checkBoxBackgroundColor = Color(0xFF682300)
                decoration = TextDecoration.None
                alphaLevel = 1f

            }


            Surface(
                modifier = Modifier
                    //.padding(start = 8.dp, top = 8.dp)
                    .wrapContentSize()
                    .alpha(alphaLevel)
                    .clickable(
                        enabled = !selected,
                        onClick = {/* TO DO */ },
                    ),// { selected = !selected },
                shape = RoundedCornerShape(25.dp),
                color = Color(0xFFf8ea9a),
                elevation = 4.dp,
                //color = Color(0xFF682300),//Color(0xFFd8af84),
                contentColor = Color(0xFF682300),
            ) {
                Row(
                    //Modifier.fillMaxSize(),
                    horizontalArrangement = Arrangement.Start,
                    modifier = Modifier.padding(start = 8.dp, end = 8.dp, top = 8.dp, bottom = 8.dp)
                )
                {
                    Icon(
                        imageVector = myIcon,
                        tint = checkBoxBackgroundColor,

                        //  .background(color = Color(0xFFFFFFFF)),
                        contentDescription = null,
                        modifier = Modifier
                            .padding(start = 6.dp, top = 2.dp, end = 2.dp, bottom = 2.dp)
                            .size(28.dp)
                            .align(Alignment.CenterVertically)
                            .alpha(alphaLevel)
                        //.weight(1f)

                    )
                    Text(
                        text = "Add Ingredients to Shopping List!",
                        modifier = Modifier
                            // .weight(1f)
                            //   .padding(start = 4.dp, end = 6.dp)
                            .align(Alignment.CenterVertically)
                            .alpha(alphaLevel),
                        color = Color(0xFF682300),
                        textDecoration = decoration,
                        fontSize = 18.sp,
                        textAlign = TextAlign.Center,
                        fontWeight = FontWeight.Bold
                    )

                }
            }


        }
        //          }
        //Instructions List
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
fun MyPreview23(){
    BearRecipeBookAppTheme {

        val myRecipe = RecipeEntity(
            recipeName = "Cauliflower Walnut Tacos",
            onMenu = 0,
            1,
            rating = 98,
            timeToMake = 90,
            difficulty = 4
        )

        val ing1 = IngredientEntity(ingredientName = "Ingredient 1", quantityOwned = 0, quantityNeeded = 0)
        val ing2 = IngredientEntity(ingredientName = "Ingredient 2", quantityOwned = 0, quantityNeeded = 0)
        val ing3 = IngredientEntity(ingredientName = "Ingredient 3", quantityOwned = 0, quantityNeeded = 0)

        val ins1 = InstructionEntity(instructionID = 1, recipeID = "Bagels", instruction = "Munch it.")
        val ins2 = InstructionEntity(instructionID = 1, recipeID = "Bagels", instruction = "Munch it!.")
        val ins3 = InstructionEntity(instructionID = 1, recipeID = "Bagels", instruction = "Munch it!!.")

        var recList = listOf(
            RecipeWithIngredients(myRecipe,listOf(ing1, ing2, ing3)),
            RecipeWithIngredients(myRecipe,listOf(ing1, ing2, ing3)))

        var recInsList = listOf(
            RecipeWithInstructions(myRecipe, listOf(ins1, ins2, ins3)),
            RecipeWithInstructions(myRecipe, listOf(ins1, ins2, ins3)),
        )

        val recipeAll = RecipeWithIngredientsAndInstructions(myRecipe,listOf(ing1, ing2,ing1, ing2, ing1, ing2, ing1 ,ing2), listOf(ins1, ins2))

//        DetailsScreen(
//            //recList,
//            // recInsList,
//            recipeAll,
//            // recList[0],
//            {},
//            {recipeAll}
//            //{}
//            )
    }
}