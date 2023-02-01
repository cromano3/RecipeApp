package com.example.bearrecipebookapp.ui

import android.app.Application
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
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.TileMode
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.*
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewmodel.compose.LocalViewModelStoreOwner
import androidx.lifecycle.viewmodel.compose.viewModel
import coil.compose.AsyncImage
import com.example.bearrecipebookapp.R
import com.example.bearrecipebookapp.datamodel.RecipeWithIngredientsAndInstructions
import com.example.bearrecipebookapp.ui.components.AnnotatedStringAlert
import com.example.bearrecipebookapp.ui.components.BasicAlert
import com.example.bearrecipebookapp.ui.components.DetailsScreenButton
import com.example.bearrecipebookapp.ui.components.ThumbsRatingAlert
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
    showAddedToFavoritesSnackBarMessage: (recipeName: String) -> Unit,
    navigateToCommentScreen: (String) -> Unit,

) {

    val owner = LocalViewModelStoreOwner.current

    owner?.let {viewModelStoreOwner ->
        val detailsScreenViewModel: DetailsScreenViewModel = viewModel(
            viewModelStoreOwner,
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


        /*
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



        /*
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

                    Spacer(Modifier
                        .fillMaxWidth()
                        .border(1.dp, Color(0xFFd8af84))
                        .height(1.dp))

                    AsyncImage(
                        model = image,
                        contentDescription = null,
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(300.dp)
                            .padding(bottom = 0.dp),
                        contentScale = ContentScale.Crop,
                    )

                    Spacer(Modifier
                            .fillMaxWidth()
                            .border(1.dp, Color(0xFFd8af84))
                            .height(1.dp))


                    Column(
                        modifier = Modifier
                            // .weight(1f)
                            .fillMaxSize(),
                        verticalArrangement = Arrangement.Center,
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {

                        //Buttons Row beneath image
                        Surface(
                            modifier =
                            Modifier
                                .padding(start = 0.dp, end = 0.dp, bottom = 16.dp)
                                .height(60.dp)
                                .fillMaxWidth()
                                .background(
                                    brush = Brush.horizontalGradient(
                                        colors = listOf(Color(0xFFb15f33), Color(0xFF682300)),
                                        tileMode = TileMode.Mirror
                                    ),
                                    shape = RoundedCornerShape(0.dp, 0.dp, 0.dp, 0.dp)
                                ),
                            shape = RoundedCornerShape(0.dp, 0.dp, 0.dp, 0.dp),

                            color = Color.Transparent,

                            ) {

                            val selected: Boolean
//                            val alphaLevel: Float
                            val menuText: String
                            val finishedText: String

                            val removeTextColor = Color(0xFF682300)
                            val removeBackgroundColor = Color(0xFFd8af84)
                            val removeBorderStartColor = Color(0xFFb15f33)
                            val removeBorderEndColor = Color(0xFF682300)

                            val textColor = Color(0xFFd8af84)
                            val backgroundColor = Color(0xFF682300)
                            val borderStartColor = Color(0xFFd8af84)
                            val borderEndColor = Color(0xFFb15f33)

                            if (detailsScreenData.recipeEntity.onMenu == 1) {
                                selected = true
//                            alphaLevel = 1f
                                menuText = "Remove from Menu"
                                finishedText = "Finished Cooking!"
                            } else {
                                selected = false
//                            alphaLevel = 1f
                                menuText = "Add to Menu"
                                finishedText = "I Made This!"
                            }

//                        val alphaAnim: Float by animateFloatAsState(
//                            targetValue = alphaLevel,
//                            animationSpec = tween(
//                                durationMillis = 150,
//                                delayMillis = 0,
//                                easing = LinearEasing,
//                            )
//                        )

                            Row(
                                Modifier.fillMaxSize(),
                                horizontalArrangement = Arrangement.SpaceEvenly,
                                verticalAlignment = Alignment.CenterVertically,
                            ) {

                                //Add or Remove Menu button
                                DetailsScreenButton(
                                    onClick =
                                    {
                                        if (selected) {
                                            detailsScreenViewModel.triggerRemoveAlert(
                                                detailsScreenData
                                            )
                                        } else {
                                            detailsScreenViewModel.addToMenu(detailsScreenData)
                                            onMenuAddClick(detailsScreenData)
                                        }
                                    },
                                    borderStartColor = if (selected) removeBorderStartColor else borderStartColor,
                                    borderEndColor = if (selected) removeBorderEndColor else borderEndColor,
                                    textColor = if (selected) removeTextColor else textColor,
                                    backgroundColor = if (selected) removeBackgroundColor else backgroundColor,
                                    buttonText = menuText
                                )

                                //Finished cooking button
                                DetailsScreenButton(
                                    onClick =
                                    {
                                        if (selected) {
                                            detailsScreenViewModel.triggerCompletedAlert(
                                                detailsScreenData
                                            )
                                        } else {
                                            detailsScreenViewModel.triggerCompletedAlert(
                                                detailsScreenData
                                            )
                                        }

                                    },
                                    borderStartColor = borderStartColor,
                                    borderEndColor = borderEndColor,
                                    textColor = textColor,
                                    backgroundColor = backgroundColor,
                                    buttonText = finishedText
                                )

                            }
                        }


                        //Info box
                        Surface(
                            modifier = Modifier
                                .padding(bottom = 16.dp)
                                .wrapContentSize(),
                            shape = RoundedCornerShape(10.dp),
                            color = Color(0xFF682300),
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
                                        tint = Color(0xFFd8af84),
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
                                        color = Color(0xFFd8af84),
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
                                        tint = Color(0xFFd8af84),
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
                                        color = Color(0xFFd8af84),
                                        fontSize = 18.sp,
                                        textAlign = TextAlign.Center,
                                        fontWeight = FontWeight.Bold

                                    )
                                    for (x in 0 until detailsScreenData.recipeEntity.difficulty) {
                                        Icon(
                                            Icons.Outlined.Star,
                                            tint = Color(0xFFd8af84),
                                            contentDescription = null
                                        )
                                    }
                                }
                                //Rating
                                Row(verticalAlignment = Alignment.CenterVertically) {
                                    Icon(
                                        Icons.Outlined.ThumbUp,
                                        tint = Color(0xFFd8af84),
                                        contentDescription = null
                                    )
                                    Text(
                                        text = "Rating: ${detailsScreenData.recipeEntity.globalRating}" + "%",
                                        modifier = Modifier
                                            .padding(
                                                start = 8.dp,
                                                end = 8.dp,
                                                top = 8.dp,
                                                bottom = 8.dp
                                            )
                                            .background(color = Color.Transparent),
                                        // .weight(1f),
                                        color = Color(0xFFd8af84),
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

            //Alerts
            Box(Modifier.fillMaxSize()){

                //Remove Alert
                if(uiAlertState.showRemoveAlert){

                    AnnotatedStringAlert(
                        text = buildAnnotatedString {
                            append("Are you sure you want to remove ")
                            append(uiAlertState.recipe.recipeEntity.recipeName)
                            append(" from the ")
                            withStyle(style = SpanStyle(fontWeight = FontWeight.Bold)) {
                                append("Menu")
                            }
                            append("? (This will also remove its ingredients from the ")
                            withStyle(style = SpanStyle(fontWeight = FontWeight.Bold)){
                                append("Shopping List")
                            }
                            append(".")
                        },
                        confirmButtonText = "Yes",
                        cancelButtonText = "Cancel",
                        onConfirmClick =
                        {
                            onMenuRemoveClick(detailsScreenData)
                            detailsScreenViewModel.removeFromMenu(uiAlertState.recipe)
                            detailsScreenViewModel.cancelRemoveAlert()
                        },
                        onCancelClick = { detailsScreenViewModel.cancelRemoveAlert() },
                        onDismiss = {}
                    )
                }

                //Completed Alert
                if(uiAlertState.showCompletedAlert){

                    val finishedText: AnnotatedString = if(detailsScreenData.recipeEntity.onMenu == 0) {
                        buildAnnotatedString {
                            append("Great job! Add ")
                            append(uiAlertState.recipe.recipeEntity.recipeName)
                            append(" to the ")
                            withStyle(style = SpanStyle(fontWeight = FontWeight.Bold)) {
                                append("Cooked Recipes")
                            }
                            append(" list?")
                        }
                    }
                    else{
                        buildAnnotatedString {
                            append("Great job! Confirm that you have finished cooking ")
                            append(uiAlertState.recipe.recipeEntity.recipeName)
                            append(" and ")
                            withStyle(style = SpanStyle(fontWeight = FontWeight.Bold)) {
                                append("remove it")
                            }
                            append(" from your ")
                            withStyle(style = SpanStyle(fontWeight = FontWeight.Bold)) {
                                append("Menu")
                            }
                            append(" and its ingredients from your ")
                            withStyle(style = SpanStyle(fontWeight = FontWeight.Bold)) {
                                append("Shopping List")
                            }
                            append("?")
                        }
                    }

                    AnnotatedStringAlert(
                        text = finishedText,
                        confirmButtonText = "Yes",
                        cancelButtonText = "Cancel",
                        onConfirmClick =
                        {
                            onCompleteClick(detailsScreenData)
                            detailsScreenViewModel.addCooked(detailsScreenData)
                            detailsScreenViewModel.addExp(uiAlertState.recipe)
                            if(detailsScreenData.recipeEntity.onMenu == 1) {
                                detailsScreenViewModel.removeFromMenu(uiAlertState.recipe)
                            }
                            detailsScreenViewModel.confirmCompletedAlert(detailsScreenData.recipeEntity)
                         },
                        onCancelClick = { detailsScreenViewModel.cancelCompletedAlert() },
                        onDismiss = {}
                    )

                }
                
                if(uiAlertState.showRatingAlert){
                    ThumbsRatingAlert(
                        confirmButtonText = "Confirm",
                        cancelButtonText = "Cancel",
                        onConfirmClick = { detailsScreenViewModel.confirmRating(detailsScreenData.recipeEntity) },
                        onCancelClick = { detailsScreenViewModel.cancelRatingAlert() },
                        onDismiss = { detailsScreenViewModel.cancelRatingAlert() },
                        onThumbDownClick = { detailsScreenViewModel.thumbDownClicked() },
                        onThumbUpClick = { detailsScreenViewModel.thumbUpClicked() },
                        text = "Did you enjoy ${detailsScreenData.recipeEntity.recipeName}?",
                        isThumbDownSelected = uiAlertState.isThumbDownSelected,
                        isThumbUpSelected = uiAlertState.isThumbUpSelected,
                    )

                }

                if(uiAlertState.showFavoriteAlert){
                    BasicAlert(
                        text = "Add ${detailsScreenData.recipeEntity.recipeName} to your Favorites?",
                        confirmButtonText = "Yes",
                        cancelButtonText = "No",
                        onConfirmClick =
                        {
                            detailsScreenViewModel.addToFavorite(detailsScreenData.recipeEntity)
                            showAddedToFavoritesSnackBarMessage(detailsScreenData.recipeEntity.recipeName)
                        },
                        onCancelClick = { detailsScreenViewModel.doNotAddToFavorite(detailsScreenData.recipeEntity) },
                        onDismiss = { detailsScreenViewModel.cancelFavoriteAlert() }
                    )
                        


                }
                if(uiAlertState.showLeaveReviewAlert){
                    BasicAlert(
                        text = "Would you like to share a tip about this recipe for other chefs?",
                        confirmButtonText = "Yes",
                        cancelButtonText = "No",
                        onConfirmClick =
                        {
                            /** Will be main thread query to ensure data is ready when user gets to Comment Screen */
                            detailsScreenViewModel.confirmShowWriteReviewAlert(detailsScreenData.recipeEntity)
                            navigateToCommentScreen(detailsScreenData.recipeEntity.recipeName)
                        },
                        onCancelClick = { detailsScreenViewModel.doNotWriteReview(detailsScreenData.recipeEntity) },
                        onDismiss = { detailsScreenViewModel.cancelShowWriteReviewAlert() }
                    )
                }

            }
        }
    }
}

//@Preview
//@Composable
//fun MyPreview234(){
//    BearRecipeBookAppTheme {
//
//        val myRecipe = RecipeEntity(
//            recipeName = "Cauliflower Walnut Tacos",
//            onMenu = 0,
//            1,
//            rating = 98,
//            timeToMake = 90,
//            difficulty = 4
//        )
//
//        val ing1 = IngredientEntity(ingredientName = "Ingredient 1", quantityOwned = 0, quantityNeeded = 0)
//        val ing2 = IngredientEntity(ingredientName = "Ingredient 2", quantityOwned = 0, quantityNeeded = 0)
//        val ing3 = IngredientEntity(ingredientName = "Ingredient 3", quantityOwned = 0, quantityNeeded = 0)
//
//        val ins1 = InstructionEntity(instructionID = 1, recipeID = "Bagels", instruction = "Munch it.")
//        val ins2 = InstructionEntity(instructionID = 1, recipeID = "Bagels", instruction = "Munch it!.")
//        val ins3 = InstructionEntity(instructionID = 1, recipeID = "Bagels", instruction = "Munch it!!.")
//
//        var recList = listOf(
//            RecipeWithIngredients(myRecipe,listOf(ing1, ing2, ing3)),
//            RecipeWithIngredients(myRecipe,listOf(ing1, ing2, ing3)))
//
//        var recInsList = listOf(
//            RecipeWithInstructions(myRecipe, listOf(ins1, ins2, ins3)),
//            RecipeWithInstructions(myRecipe, listOf(ins1, ins2, ins3)),
//        )
//
//        val recipeAll = RecipeWithIngredientsAndInstructions(myRecipe,listOf(ing1, ing2,ing1, ing2, ing1, ing2, ing1 ,ing2), listOf(ins1, ins2))
//
//        NewDetailsScreen(
//            //recList,
//            // recInsList,
//            {},
//            // recList[0],
//            {},
//            {},
//            {},
//            {}
//            )
//    }
//}

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