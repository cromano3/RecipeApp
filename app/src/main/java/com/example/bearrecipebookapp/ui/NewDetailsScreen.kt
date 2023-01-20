package com.example.bearrecipebookapp.ui

import android.app.Application
import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.AutoAwesome
import androidx.compose.material.icons.outlined.Star
import androidx.compose.material.icons.outlined.ThumbUp
import androidx.compose.material.icons.outlined.Timer
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.TileMode
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
import com.example.bearrecipebookapp.data.entity.IngredientEntity
import com.example.bearrecipebookapp.data.entity.InstructionEntity
import com.example.bearrecipebookapp.data.entity.RecipeEntity
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

                        val selected: Boolean
                        val alphaLevel: Float
                        val myText: String
                        val myFinishedText: String

                        if (detailsScreenData.recipeEntity.onMenu == 1) {
                            selected = true
                            alphaLevel = 0.55f
                            myText = "Remove from Menu"
                            myFinishedText = "Finished Cooking!"
                        } else {
                            selected = false
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

                        Row(
                            Modifier
                                .fillMaxWidth()
                                .wrapContentHeight()
                                .padding(bottom = 16.dp),
                            horizontalArrangement = Arrangement.SpaceEvenly) {
                            //Add/Remove from Menu button
                            Surface(
                                modifier = Modifier
                                    //.padding(start = 8.dp, top = 8.dp)
                                    .wrapContentSize()
//                                    .alpha(alphaAnim)
                                    .border(
                                        width = 2.dp,
                                        brush = (Brush.horizontalGradient(
                                            colors = listOf(Color(0xFFd8af84), Color(0xFFb15f33)),
                                            tileMode = TileMode.Mirror)),
                                        shape = CircleShape
                                    )
                                    .clickable(
//                                    enabled = false,
                                        //!selected
                                        onClick = {
                                            if (selected) {
                                                detailsScreenViewModel.triggerRemoveAlert(
                                                    detailsScreenData
                                                )
                                            } else {
                                                detailsScreenViewModel.addToMenu(detailsScreenData)
                                                onMenuAddClick(detailsScreenData)
                                            }
                                        },
                                    ),// { selected = !selected },
                                shape = RoundedCornerShape(25.dp),
                                color = Color(0xFF682300),
                                elevation = 4.dp,
                                //color = Color(0xFF682300),//Color(0xFFd8af84),

                            ) {

                                Row(
                                    //Modifier.fillMaxSize(),
                                    horizontalArrangement = Arrangement.Start,
                                    modifier = Modifier.padding(start = 12.dp, end = 12.dp, top = 12.dp, bottom = 12.dp)
                                )
                                {

                                    Text(
                                        text = myText,
                                        modifier = Modifier
                                            // .weight(1f)
//                                .padding(start = 0.dp, end = 0.dp)
                                            .align(Alignment.CenterVertically)
                                            .alpha(alphaAnim),
                                        color = Color(0xFFd8af84),
//                            textDecoration = decoration,
                                        fontSize = 18.sp,
                                        textAlign = TextAlign.Center,
                                        fontWeight = FontWeight.Bold
                                    )
                                }


                            }

                            //
                            Surface(
                                modifier = Modifier
                                    //.padding(start = 8.dp, top = 8.dp)
                                    .wrapContentSize()
                                    .border(
                                        width = 2.dp,
                                        brush = (Brush.horizontalGradient(
                                            colors = listOf(Color(0xFFd8af84), Color(0xFFb15f33)),
                                            tileMode = TileMode.Mirror)),
                                        shape = CircleShape
                                    )
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
                                    ),
                                shape = RoundedCornerShape(25.dp),
                                color = Color(0xFF682300),
                                elevation = 4.dp,
                            ) {
                                Row(
                                    horizontalArrangement = Arrangement.Start,
                                    modifier = Modifier.padding(start = 12.dp, end = 12.dp, top = 12.dp, bottom = 12.dp)
                                )
                                {

                                    Text(
                                        text = myFinishedText,
                                        modifier = Modifier
                                            .align(Alignment.CenterVertically),
                                        color = Color(0xFFd8af84),
                                        fontSize = 18.sp,
                                        textAlign = TextAlign.Center,
                                        fontWeight = FontWeight.Bold
                                    )
                                }
                            }
                        }
                        //Info box
                        Surface(
                            modifier = Modifier
                                .padding(bottom = 16.dp)
                                .wrapContentSize(),
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

                                    onCompleteClick(detailsScreenData)

                                    detailsScreenViewModel.addCooked(detailsScreenData)

                                    detailsScreenViewModel.addExp(uiAlertState.recipe)

                                    if(detailsScreenData.recipeEntity.onMenu == 1) {
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

        NewDetailsScreen(
            //recList,
            // recInsList,
            {},
            // recList[0],
            {},
            {},
            {},
            {}
            )
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