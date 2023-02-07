package com.example.bearrecipebookapp.ui

import android.app.Application
import androidx.activity.compose.BackHandler
import androidx.compose.animation.EnterTransition
import androidx.compose.animation.ExitTransition
import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.animation.core.FastOutLinearInEasing
import androidx.compose.animation.core.TweenSpec
import androidx.compose.animation.scaleOut
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.FloatingActionButton
import androidx.compose.material.FloatingActionButtonDefaults
import androidx.compose.material.Icon
import androidx.compose.material.Surface
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Add
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.graphics.TileMode
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.unit.dp
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewmodel.compose.LocalViewModelStoreOwner
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.bearrecipebookapp.R
import com.example.bearrecipebookapp.datamodel.RecipeWithIngredientsAndInstructions
import com.example.bearrecipebookapp.ui.components.AnnotatedStringAlert
import com.example.bearrecipebookapp.ui.components.BasicAlert
import com.example.bearrecipebookapp.ui.components.RecipeCard
import com.example.bearrecipebookapp.ui.components.ThumbsRatingAlert
import com.example.bearrecipebookapp.viewmodel.MenuScreenViewModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers

@OptIn(ExperimentalAnimationApi::class)
@Composable
fun MenuScreen(
    onDetailsClick: (String) -> Unit,
    onFavoriteClick: (RecipeWithIngredientsAndInstructions) -> Unit,
    onAddedToFavoriteFromAlertClick: (String) -> Unit,
    onCompleteClick: (RecipeWithIngredientsAndInstructions) -> Unit,
    onRemoveClick: (RecipeWithIngredientsAndInstructions) -> Unit,
    onConfirmWriteReviewClick: (String) -> Unit,
    onAddRecipeClick: () -> Unit,
    onSystemBackClick: () -> Unit,
) {

    val owner = LocalViewModelStoreOwner.current

    owner?.let { it ->
        val menuScreenViewModel: MenuScreenViewModel = viewModel(
            it,
            "MenuScreenViewModel",
            MenuScreenViewModelFactory(
                LocalContext.current.applicationContext
                        as Application,
            )
        )

        val menuScreenData by menuScreenViewModel.menuScreenData.observeAsState(listOf())

        val uiAlertState by menuScreenViewModel.uiAlertState.collectAsState()

        val coroutineScope = CoroutineScope(Dispatchers.Main)

//        val coroutineScope = rememberCoroutineScope()

        BackHandler {  onSystemBackClick() }


        Surface(
            modifier = Modifier
                .fillMaxSize()
                .padding(bottom = 48.dp),
            color = Color(0xFFd8af84)

        ) {

            LazyColumn {
                items(menuScreenData, key = { it.recipeEntity.recipeName }) {
                    androidx.compose.animation.AnimatedVisibility(
                        visible = it.recipeEntity.onMenu == 1,
                        enter = EnterTransition.None,
                        exit = ExitTransition.None,
                    ) {
                        RecipeCard(
                            modifier = Modifier

                                .animateEnterExit(
                                    enter = EnterTransition.None,
                                    exit = scaleOut(
                                        animationSpec = TweenSpec(250, 0, FastOutLinearInEasing)
                                    )
                                ),
//                                .animateItemPlacement(animationSpec = (TweenSpec(150, delay = 0))),
                            recipeWithIngredientsAndInstructions = it,
                            currentScreen = "WeeklyMenuScreen",
                            onFavoriteClick =
                            {
                                menuScreenViewModel.toggleFavorite(it)
                                onFavoriteClick(it)
                            },
                            onRemoveClick = { menuScreenViewModel.triggerRemoveAlert(it) },
                            onCompleteClick = { menuScreenViewModel.triggerCompletedAlert(it) },
                            onDetailsClick = {

                                onDetailsClick(it.recipeEntity.recipeName)

//                                /** main to IO coroutine */
//                                coroutineScope.launch(Dispatchers.Main) {
//                                    withContext(Dispatchers.IO){
//                                        menuScreenViewModel.setDetailsScreenTarget(it.recipeEntity.recipeName)
//                                    }
//                                    onDetailsClick()
//                                }


                            }
                        )

                    }
                }
                item {
                    Spacer(
                        Modifier
                            .fillMaxWidth()
                            .padding(16.dp)
                    )
                }

        }

            Box(Modifier.fillMaxSize()){
                if(menuScreenData.isEmpty()) {
                    Column(horizontalAlignment = Alignment.CenterHorizontally) {
                        Spacer(
                            Modifier
                                .fillMaxWidth()
                                .weight(1f))
//                        Text(text = "The Menu is empty", color = Color(0xFF682300))
                        Image(
                            painterResource(id = R.drawable.hungry),
                            contentDescription = null,
                            alpha = .5f,
                            colorFilter = ColorFilter.tint(Color(0xFF682300))
                        )
//                        Text(text = "add some recipes", color = Color(0xFF682300))
                        Spacer(
                            Modifier
                                .fillMaxWidth()
                                .weight(1f))
                    }

                        FloatingActionButton(
                            onClick = {
                                menuScreenViewModel.triggerAddRecipeAlert()
                                      },
                            elevation = FloatingActionButtonDefaults.elevation(8.dp),
                            modifier = Modifier
                                .padding(bottom = 70.dp, end = 24.dp)
                                .border(
                                    width = 2.dp,
                                    brush = (Brush.horizontalGradient(
                                        colors = listOf(
                                            Color(0xFFd8af84),
                                            Color(0xFFb15f33),

                                            ),
                                        tileMode = TileMode.Mirror
                                    )),
                                    shape = CircleShape
                                )
                                .align(Alignment.BottomEnd)
                                .size(56.dp)
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
                                imageVector = Icons.Outlined.Add,
                                tint = Color(0xFFd8af84),
                                modifier = Modifier.size(28.dp),
                                // modifier = Modifier.background(color = Color(0xFFFFFFFF)),
                                contentDescription = null
                            )
                        }

                }

                //Add Recipe Alert
                if(uiAlertState.showAddRecipeAlert){

                    BasicAlert(
                        text = "Would you like to add a recipe to your Menu?",
                        confirmButtonText = "Yes",
                        cancelButtonText = "Cancel",
                        onConfirmClick =
                        {
                            menuScreenViewModel.cancelAddRecipeAlert()
                            menuScreenViewModel.addTutorialAlert()
                            onAddRecipeClick()
                        },
                        onCancelClick = { menuScreenViewModel.cancelAddRecipeAlert() },
                        onDismiss = { menuScreenViewModel.cancelAddRecipeAlert() })

                }

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
                            onRemoveClick(uiAlertState.recipe)
                            menuScreenViewModel.removeFromMenu(uiAlertState.recipe)
                            menuScreenViewModel.cancelRemoveAlert()
                        },
                        onCancelClick = { menuScreenViewModel.cancelRemoveAlert() }) {

                    }

                }

                //Completed Alert
                if(uiAlertState.showCompletedAlert){

                    AnnotatedStringAlert(
                        text = buildAnnotatedString {
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
                        },
                        confirmButtonText = "Yes",
                        cancelButtonText = "Cancel",
                        onConfirmClick = {
                            onCompleteClick(uiAlertState.recipe)
                            menuScreenViewModel.addCooked(uiAlertState.recipe)
                            menuScreenViewModel.removeFromMenu(uiAlertState.recipe)
                            menuScreenViewModel.addExp(uiAlertState.recipe)
                            menuScreenViewModel.confirmCompletedAlert(uiAlertState.recipe.recipeEntity)
                                         },
                        onCancelClick = {
                            menuScreenViewModel.cancelCompletedAlert()
                            menuScreenViewModel.clearAlertRecipeTarget() }) {

                    }

                }

                //Rating Alert
                if(uiAlertState.showRatingAlert){
                    ThumbsRatingAlert(
                        confirmButtonText = "Confirm",
                        cancelButtonText = "Cancel",
                        onConfirmClick = { menuScreenViewModel.confirmRating(uiAlertState.recipe.recipeEntity) },
                        onCancelClick = { menuScreenViewModel.cancelRatingAlert() },
                        onDismiss = { menuScreenViewModel.cancelRatingAlert() },
                        onThumbDownClick = { menuScreenViewModel.thumbDownClicked() },
                        onThumbUpClick = { menuScreenViewModel.thumbUpClicked() },
                        text = "Did you enjoy ${uiAlertState.recipe.recipeEntity.recipeName}?",
                        isThumbDownSelected = uiAlertState.isThumbDownSelected,
                        isThumbUpSelected = uiAlertState.isThumbUpSelected,
                    )

                }

                //Favorite Alert
                if(uiAlertState.showFavoriteAlert){
                    BasicAlert(
                        text = "Add ${uiAlertState.recipe.recipeEntity.recipeName} to your Favorites?",
                        confirmButtonText = "Yes",
                        cancelButtonText = "No",
                        onConfirmClick =
                        {
                            menuScreenViewModel.addToFavorite(uiAlertState.recipe.recipeEntity)
                            onAddedToFavoriteFromAlertClick(uiAlertState.recipe.recipeEntity.recipeName)
                        },
                        onCancelClick = { menuScreenViewModel.doNotAddToFavorite(uiAlertState.recipe.recipeEntity) },
                        onDismiss = { menuScreenViewModel.cancelFavoriteAlert() }
                    )

                }

                //Comment Alert
                if(uiAlertState.showLeaveReviewAlert){
                    BasicAlert(
                        text = "Would you like to share a tip about this recipe for other chefs?",
                        confirmButtonText = "Yes",
                        cancelButtonText = "No",
                        onConfirmClick =
                        {

                            menuScreenViewModel.confirmShowWriteReviewAlert()
                            onConfirmWriteReviewClick(uiAlertState.recipe.recipeEntity.recipeName)

//                            /** Will be main thread query to ensure data is ready when user gets to Comment Screen */
//
//                            coroutineScope.launch(Dispatchers.Main) {
//                                withContext(Dispatchers.IO) {
//                                    menuScreenViewModel.confirmShowWriteReviewAlert(uiAlertState.recipe.recipeEntity)
//                                }
//                                onConfirmWriteReviewClick()
//                            }
                        },
                        onCancelClick = { menuScreenViewModel.doNotWriteReview(uiAlertState.recipe.recipeEntity) },
                        onDismiss = { menuScreenViewModel.cancelShowWriteReviewAlert() }
                    )
                }

            }
        }
    }
}


class MenuScreenViewModelFactory(
    val application: Application,
//    val recipeName: String
) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {

        return MenuScreenViewModel(
            application,
            //  recipeName
        ) as T
    }

}