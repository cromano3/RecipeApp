package com.example.bearrecipebookapp.ui

import android.app.Application
import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CheckBox
import androidx.compose.material.icons.outlined.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.ExperimentalTextApi
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewmodel.compose.LocalViewModelStoreOwner
import androidx.lifecycle.viewmodel.compose.viewModel
import coil.compose.AsyncImage
import com.example.bearrecipebookapp.R
import com.example.bearrecipebookapp.data.IngredientEntity
import com.example.bearrecipebookapp.data.InstructionEntity
import com.example.bearrecipebookapp.data.RecipeEntity
import com.example.bearrecipebookapp.datamodel.RecipeWithIngredients
import com.example.bearrecipebookapp.datamodel.RecipeWithIngredientsAndInstructions
import com.example.bearrecipebookapp.datamodel.RecipeWithInstructions
import com.example.bearrecipebookapp.ui.theme.BearRecipeBookAppTheme
import com.example.bearrecipebookapp.viewmodel.DetailsScreenViewModel

@OptIn(ExperimentalTextApi::class)
@Composable
fun NewDetailsScreen(
    //recipeName: String,
    onGoBackClick: () -> Unit,
    onMenuAddClick: (RecipeWithIngredientsAndInstructions) -> Unit,
    onMenuRemoveClick: (RecipeWithIngredientsAndInstructions) -> Unit,
    onFavoriteClick: (RecipeWithIngredientsAndInstructions) -> Unit,
    onCompleteClick: (RecipeWithIngredientsAndInstructions) -> Unit,

) {

    val owner = LocalViewModelStoreOwner.current

    owner?.let {
        val detailsScreenViewModel: DetailsScreenViewModel = viewModel(
            it,
            "DetailsScreenViewModel",
            DetailsScreenViewModelFactory(
                LocalContext.current.applicationContext
                        as Application,
//                recipeName as String
            )
        )

        val detailsScreenData by detailsScreenViewModel.detailsScreenData.observeAsState(RecipeWithIngredientsAndInstructions())

        val uiAlertState by detailsScreenViewModel.uiAlertState.collectAsState()


//    val gradientWidth = with(LocalDensity.current) { 200.dp.toPx() }

//        BackHandler { onGoBackClick() }


        /**
         * Get the image based on the recipe Name
         */
        val image: Int = when (detailsScreenData.recipeEntity.recipeName) {
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



        /**
        format the "Time to make" from raw Int minutes to "X hr./hrs. Y mins."
        **/
        val formattedTime: String
        val remainder: Int
        val quotient: Int

        if (detailsScreenData.recipeEntity.timeToMake <= 60) {
            formattedTime = detailsScreenData.recipeEntity.timeToMake.toString() + " mins."
        } else {
            quotient = detailsScreenData.recipeEntity.timeToMake / 60
            remainder = detailsScreenData.recipeEntity.timeToMake % 60
            if (remainder == 0) {
                formattedTime = "$quotient hrs."
            } else {
                if (quotient == 1) {
                    formattedTime = "$quotient hr. $remainder mins."
                } else {
                    formattedTime = "$quotient hrs. $remainder mins."
                }
            }
        }

        val scrollState = rememberScrollState()


        Surface(
            modifier = Modifier
                .fillMaxSize(),
            color = Color(0xFFd8af84)


        ) {
//            BackHandler { onGoBackClick() }
            Column() {
//                Row(
//                    modifier = Modifier
//                        .fillMaxWidth()
//                        .height(48.dp)
//                        .background(Color(0xFF682300)),
//                    verticalAlignment = Alignment.CenterVertically,
//                    horizontalArrangement = Arrangement.Start
//                ) {
//                    IconButton(
//                        // onClick = {onGoBackClick(detailsScreenData)},
//                        onClick = { onGoBackClick() },
//                        modifier =
//                        Modifier
//                            .size(48.dp)
//                            .align(Alignment.CenterVertically)
//                            .background(color = Color.Transparent),
//                        // color = Color.Transparent
//
//                    ) {
//                        Icon(
//                            imageVector = Icons.Outlined.ArrowBack,
//                            contentDescription = null,
//                            modifier = Modifier
//                                .size(24.dp),
//                            // .padding(start = 16.dp)
//                            // .clickable(onClick = {onGoBackClick(detailsScreenData)}),
//                            tint = Color(0xFFd8af84)
//                        )
//                    }
//
//                    Spacer(Modifier.weight(1f))
//
//                    Text(
//                        text = detailsScreenData.recipeEntity.recipeName,
//                        modifier = Modifier.width(200.dp),
//                        //  .weight(1f),
//                        color = Color(0xFFd8af84),
//                        textAlign = TextAlign.Center,
//                        fontSize = 22.sp,
//                        fontFamily = Cabin,
//                        fontWeight = FontWeight.Bold,
//                        lineHeight = 1.0.em,
//                        style = MaterialTheme.typography.h4.merge(
//                            TextStyle(
//                                platformStyle = PlatformTextStyle(
//                                    includeFontPadding = false
//                                ),
//                                lineHeightStyle = LineHeightStyle(
//                                    alignment = LineHeightStyle.Alignment.Top,
//                                    trim = LineHeightStyle.Trim.FirstLineTop
//                                )
//                            )
//                        ),
//
//                        )
//
//                    Spacer(Modifier.weight(1f))
//
//                    Icon(
//                        imageVector = Icons.Outlined.Share,
//                        contentDescription = null,
//                        modifier = Modifier
//                            .size(24.dp),
//                        tint = Color(0xFFd8af84)
//                    )
//
//                    Spacer(Modifier.size(16.dp))
//
//                    val gradientWidthButton = with(LocalDensity.current) { 48.dp.toPx() }
//
//                    val icon: ImageVector = if (detailsScreenData.recipeEntity.isFavorite == 0) {
//                        Icons.Outlined.FavoriteBorder
//
//                    } else {
//                        Icons.Outlined.Favorite
//                    }
//
//                    FloatingActionButton(
//                        onClick = {
//                            onFavoriteClick(detailsScreenData)
//                            detailsScreenViewModel.toggleFavorite(detailsScreenData)
//                                  },
//                        elevation = FloatingActionButtonDefaults.elevation(8.dp),
//                        modifier = Modifier
//                            .padding(end = 8.dp)
//                            .border(
//                                width = 2.dp,
//                                brush = (Brush.horizontalGradient(
//                                    colors = listOf(
//                                        Color(0xFFd8af84),
//                                        Color(0xFFb15f33),
//
//                                        ),
//                                    endX = gradientWidthButton,
//                                    tileMode = TileMode.Mirror
//                                )),
//                                shape = CircleShape
//                            )
//                            // .align(Alignment.BottomEnd)
//                            .size(36.dp)
//                            //the background of the square for this button, it stays a square even tho
//                            //we have shape = circle shape.  If this is not changed you see a solid
//                            //square for the "background" of this button.
//                            .background(color = Color.Transparent),
//                        shape = CircleShape,
//                        //this is the background color of the button after the "Shaping" is applied.
//                        //it is different then the background attribute above.
//                        backgroundColor = Color(0xFF682300)
//                    ) {
//                        Icon(
//                            icon,
//                            tint = Color(0xFFd8af84),
//                            modifier = Modifier.size(20.dp),
//                            // modifier = Modifier.background(color = Color(0xFFFFFFFF)),
//                            contentDescription = null
//                        )
//                    }
//                    Spacer(Modifier.size(8.dp))
//                }


//            }
                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .verticalScroll(scrollState, enabled = true),
                    verticalArrangement = Arrangement.Top,
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    AsyncImage(
                        model = image,
                        contentDescription = null,
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(300.dp)
                            .padding(bottom = 16.dp),
                        contentScale = ContentScale.Crop,
                    )


                    Column(
                        modifier = Modifier
                            // .weight(1f)
                            .fillMaxSize(),
                        verticalArrangement = Arrangement.Center,
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        //Info box
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
                            //Info Box
                            Column(
                                Modifier.padding(start = 4.dp)
                            ) {

                                //Time
                                Row(verticalAlignment = Alignment.CenterVertically) {
                                    Icon(
                                        Icons.Outlined.Timer,
                                        tint = Color(0xFF000000),
                                        contentDescription = null
                                    )
                                    Text(
                                        text = "Time: $formattedTime",
                                        modifier = Modifier
                                            .padding(
                                                start = 8.dp,
                                                end = 8.dp,
                                                top = 8.dp,
                                                bottom = 8.dp
                                            )
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
                                            .padding(
                                                start = 8.dp,
                                                end = 0.dp,
                                                top = 8.dp,
                                                bottom = 8.dp
                                            )
                                            .background(color = Color.Transparent),
                                        // .weight(1f),
                                        color = Color(0xFF682300),
                                        fontSize = 18.sp,
                                        textAlign = TextAlign.Center,
                                        fontWeight = FontWeight.Bold

                                    )
                                    for (x in 0 until detailsScreenData.recipeEntity.difficulty) {
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
                                        text = "Rating: ${detailsScreenData.recipeEntity.rating}" + "%",
                                        modifier = Modifier
                                            .padding(
                                                start = 8.dp,
                                                end = 8.dp,
                                                top = 8.dp,
                                                bottom = 8.dp
                                            )
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
                                .wrapContentHeight()
                                .fillMaxWidth()
                                .padding(start = 16.dp, end = 16.dp, bottom = 16.dp),
                            //    .weight(1f),
                            shape = RoundedCornerShape(10.dp),
                            color = Color(0xFF682300),

                            ) {
                            Column(
                                Modifier.padding(
                                    top = 8.dp,
                                    bottom = 8.dp,
                                    start = 16.dp,
                                    end = 16.dp
                                )
                            ) {
                                Text(
                                    modifier = Modifier
                                        .padding(bottom = 8.dp)
                                        .align(Alignment.CenterHorizontally),
                                    text = "Ingredients List:",
                                    textDecoration = TextDecoration.Underline,
                                    color = Color(0xFFd8af84),
                                    fontSize = 18.sp
                                )


                                for (x in 0 until detailsScreenData.ingredientsList.size) {
                                    Text(
                                        //modifier = Modifier.padding(start = 8.dp, end = 8.dp),
                                        text = "- " + detailsScreenData.ingredientsList[x].ingredientName,
                                        color = Color(0xFFd8af84),
                                        fontSize = 18.sp

                                    )
                                }
                            }
                        }


                        val selected: Boolean

                        val myIcon: ImageVector
                        val checkBoxBackgroundColor: Color
                        val decoration: TextDecoration
                        val alphaLevel: Float
                        val myText: String
                        val myFinishedText: String


                        if (detailsScreenData.recipeEntity.onMenu == 1) {
                            selected = true
                            myIcon = Icons.Filled.CheckBox
                            checkBoxBackgroundColor = Color(0xFF682300)
//                            decoration = TextDecoration.LineThrough
                            alphaLevel = 0.55f
                            myText = "Remove from Menu"
                            myFinishedText = "Finished Cooking!"
                        } else {
                            selected = false
                            myIcon = Icons.Outlined.CheckBoxOutlineBlank
                            checkBoxBackgroundColor = Color(0xFF682300)
//                            decoration = TextDecoration.None
                            alphaLevel = 1f
                            myText = "Add to Menu"
                            myFinishedText = "I Made This!"
                        }

                        val alphaAnim: Float by animateFloatAsState(
                            targetValue = alphaLevel,
                            animationSpec = tween(
                                durationMillis = 150,
                                delayMillis = 0,
                                easing = LinearEasing,
                            )
                        )


//

                        Row(
                            Modifier.fillMaxWidth().wrapContentHeight(),
                            horizontalArrangement = Arrangement.SpaceEvenly) {
                            //Add/Remove from Menu button
                            Surface(
                                modifier = Modifier
                                    //.padding(start = 8.dp, top = 8.dp)
                                    .wrapContentSize()
                                    .alpha(alphaAnim)
                                    .clickable(
//                                    enabled = false,
                                        //!selected
                                        onClick = {
                                            if (selected) {
                                                detailsScreenViewModel.triggerRemoveAlert(
                                                    detailsScreenData
                                                )
                                            } else if (!selected) {
                                                detailsScreenViewModel.addToMenu(detailsScreenData)
                                                onMenuAddClick(detailsScreenData)
                                            }
                                        },
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
                                    modifier = Modifier.padding(
                                        start = 8.dp,
                                        end = 8.dp,
                                        top = 8.dp,
                                        bottom = 8.dp
                                    )
                                )
                                {
                                    Icon(
                                        imageVector = myIcon,
                                        tint = checkBoxBackgroundColor,

                                        //  .background(color = Color(0xFFFFFFFF)),
                                        contentDescription = null,
                                        modifier = Modifier
                                            .padding(
                                                start = 0.dp,
                                                top = 2.dp,
                                                end = 2.dp,
                                                bottom = 2.dp
                                            )
                                            .size(28.dp)
                                            .align(Alignment.CenterVertically)
                                            .alpha(alphaAnim)
                                        //.weight(1f)

                                    )
                                    Text(
                                        text = myText,
                                        modifier = Modifier
                                            // .weight(1f)
                                            //   .padding(start = 4.dp, end = 6.dp)
                                            .align(Alignment.CenterVertically)
                                            .alpha(alphaAnim),
                                        color = Color(0xFF682300),
//                                    textDecoration = decoration,
                                        fontSize = 18.sp,
                                        textAlign = TextAlign.Center,
                                        fontWeight = FontWeight.Bold
                                    )
                                }
                            }

                            //Finished Cooking Button
                            Surface(
                                modifier = Modifier
                                    //.padding(start = 8.dp, top = 8.dp)
                                    .wrapContentSize()
//                                    .alpha(alphaAnim)
                                    .clickable(
//                                    enabled = false,
                                        //!selected
                                        onClick = {
                                            if (selected) {
                                                detailsScreenViewModel.triggerCompletedAlert(
                                                    detailsScreenData
                                                )
                                            } else if (!selected) {
                                                detailsScreenViewModel.triggerCompletedAlert(
                                                    detailsScreenData
                                                )
                                            }
                                        },
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
                                    modifier = Modifier.padding(
                                        start = 8.dp,
                                        end = 8.dp,
                                        top = 12.dp,
                                        bottom = 12.dp
                                    )
                                )
                                {
                                    Text(
                                        text = myFinishedText,
                                        modifier = Modifier
                                            // .weight(1f)
                                            //   .padding(start = 4.dp, end = 6.dp)
                                            .align(Alignment.CenterVertically),
//                                            .alpha(alphaAnim),
                                        color = Color(0xFF682300),
//                                    textDecoration = decoration,
                                        fontSize = 18.sp,
                                        textAlign = TextAlign.Center,
                                        fontWeight = FontWeight.Bold
                                    )
                                }
                            }
                        }
                    }

                    //Instructions List
                    for (x in 0 until detailsScreenData.instructionsList.size) {
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
                                text = detailsScreenData.instructionsList[x].instruction,
                                textAlign = TextAlign.Center
                            )
                        }

                    }
                    Spacer(
                        Modifier
                            .size(64.dp)
                            .fillMaxWidth())
                }
            }
            Box(Modifier.fillMaxSize()){
                //Remove Alert
                if(uiAlertState.showRemoveAlert){
                    AlertDialog(
                        onDismissRequest = {},
                        text = {
                            Text(text = "Are you sure you want to remove " + uiAlertState.recipe.recipeEntity.recipeName +
                                    " from the Menu? (This will also remove it from the Shopping List.)" )
                        },
                        confirmButton = {
                            TextButton(
                                onClick = {
                                    onMenuRemoveClick(detailsScreenData)
                                    detailsScreenViewModel.removeFromMenu(uiAlertState.recipe)
                                    detailsScreenViewModel.cancelRemoveAlert()
                                }
                            ) {
                                Text("Yes")
                            }
                        },
                        dismissButton = {
                            TextButton(
                                onClick = {
                                    detailsScreenViewModel.cancelRemoveAlert()
                                }
                            ) {
                                Text("Cancel")
                            }
                        }
                    )
                }

                ////


                //Completed Alert
                if(uiAlertState.showCompletedAlert){

                    val finishedText: String = if(detailsScreenData.recipeEntity.onMenu == 1){
                        "Mark " +
                                uiAlertState.recipe.recipeEntity.recipeName +
                                " as completed and remove from the Menu? (This will also remove it from the Shopping List.)"
                    } else
                        "Great job! Add " +
                                uiAlertState.recipe.recipeEntity.recipeName  +
                                " to Cooked Recipes list?"

                    AlertDialog(
                        onDismissRequest = {},
                        text = {
                            Text(text = finishedText )
                        },
                        confirmButton = {
                            TextButton(
                                onClick = {
                                    /**
                                     * Add completed count +1 to Database
                                     */

                                    if(detailsScreenData.recipeEntity.onMenu == 1) {
                                        onCompleteClick(detailsScreenData)
                                        detailsScreenViewModel.removeFromMenu(uiAlertState.recipe)
                                    }
                                    detailsScreenViewModel.cancelCompletedAlert()
                                }
                            ) {
                                Text("Yes")
                            }
                        },
                        dismissButton = {
                            TextButton(
                                onClick = {
                                    detailsScreenViewModel.cancelCompletedAlert()
                                }
                            ) {
                                Text("Cancel")
                            }
                        }
                    )
                }
            }
        }
    }
}

@Preview
@Composable
fun MyPreview234(){
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

class DetailsScreenViewModelFactory(
    val application: Application,
//    val recipeName: String
    ) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {

        return DetailsScreenViewModel(
            application,
          //  recipeName
        ) as T
    }
}