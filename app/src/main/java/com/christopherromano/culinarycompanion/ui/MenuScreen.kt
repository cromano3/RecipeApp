package com.christopherromano.culinarycompanion.ui

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
import androidx.compose.ui.unit.dp
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewmodel.compose.LocalViewModelStoreOwner
import androidx.lifecycle.viewmodel.compose.viewModel
import com.christopherromano.culinarycompanion.R
import com.christopherromano.culinarycompanion.data.annotatedstrings.confirmCompletedCookingAnoString
import com.christopherromano.culinarycompanion.data.annotatedstrings.confirmRemoveMenuAnoString
import com.christopherromano.culinarycompanion.data.entity.RecipeEntity
import com.christopherromano.culinarycompanion.data.repository.MenuScreenFirebaseRepository
import com.christopherromano.culinarycompanion.datamodel.RecipeWithIngredientsAndInstructions
import com.christopherromano.culinarycompanion.ui.components.AnnotatedStringAlert
import com.christopherromano.culinarycompanion.ui.components.BasicAlert
import com.christopherromano.culinarycompanion.ui.components.RecipeCard
import com.christopherromano.culinarycompanion.ui.components.ThumbsRatingAlert
import com.christopherromano.culinarycompanion.viewmodel.MenuScreenViewModel
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase

@OptIn(ExperimentalAnimationApi::class)
@Composable
fun MenuScreen(
    userIsOnlineStatus: Int,
    isExpandedHeight: Boolean,
    onDetailsClick: (String) -> Unit,
    onFavoriteClick: (RecipeWithIngredientsAndInstructions) -> Unit,
    onAddedToFavoriteFromAlertClick: (RecipeEntity) -> Unit,
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
                            recipeWithIngredientsAndInstructions = it,
                            isExpandedHeight = isExpandedHeight,
                            currentScreen = "WeeklyMenuScreen",
                            onFavoriteClick =
                            {
                                menuScreenViewModel.toggleFavorite(it)
                                onFavoriteClick(it)
                            },
                            onRemoveClick = { menuScreenViewModel.triggerRemoveAlert(it) },
                            onCompleteClick = { menuScreenViewModel.triggerCompletedAlert(it, userIsOnlineStatus) },
                            onDetailsClick = { onDetailsClick(it.recipeEntity.recipeName) }
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
                        Image(
                            painterResource(id = R.drawable.hungry),
                            contentDescription = null,
                            alpha = .5f,
                            colorFilter = ColorFilter.tint(Color(0xFF682300))
                        )
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
                                contentDescription = "Tap to add an item to your menu. Your menu is currently empty."
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
                        text = confirmRemoveMenuAnoString(uiAlertState.recipe.recipeEntity.recipeName),
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
                        text = confirmCompletedCookingAnoString(uiAlertState.recipe.recipeEntity.recipeName),
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
                            onAddedToFavoriteFromAlertClick(uiAlertState.recipe.recipeEntity)
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
) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {

        return MenuScreenViewModel(
            application,
            MenuScreenFirebaseRepository(Firebase.firestore, Firebase.auth)
        ) as T
    }

}