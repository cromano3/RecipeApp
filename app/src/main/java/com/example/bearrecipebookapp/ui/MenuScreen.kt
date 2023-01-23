package com.example.bearrecipebookapp.ui

import android.app.Application
import androidx.activity.compose.BackHandler
import androidx.compose.animation.EnterTransition
import androidx.compose.animation.ExitTransition
import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.animation.core.FastOutLinearInEasing
import androidx.compose.animation.core.TweenSpec
import androidx.compose.animation.scaleOut
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
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
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewmodel.compose.LocalViewModelStoreOwner
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.bearrecipebookapp.R
import com.example.bearrecipebookapp.datamodel.RecipeWithIngredients
import com.example.bearrecipebookapp.ui.components.RecipeCard
import com.example.bearrecipebookapp.viewmodel.MenuScreenViewModel

@OptIn(ExperimentalFoundationApi::class, ExperimentalAnimationApi::class)
@Composable
fun MenuScreen(
    onDetailsClick: () -> Unit,
    onFavoriteClick: (RecipeWithIngredients) -> Unit,
    onCompleteClick: (RecipeWithIngredients) -> Unit,
    onRemoveClick: (RecipeWithIngredients) -> Unit,
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

//        val coroutineScope = rememberCoroutineScope()

        BackHandler {  onSystemBackClick() }


        Surface(
            modifier = Modifier
                .fillMaxSize()
                .padding(bottom = 48.dp),
            color = Color(0xFFd8af84)

        ) {

            LazyColumn() {
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
                            recipeWithIngredients = it,
                            currentScreen = "WeeklyMenuScreen",
                            onFavoriteClick =
                            {
                                menuScreenViewModel.toggleFavorite(it)
                                onFavoriteClick(it)
                            },
                            onRemoveClick = { menuScreenViewModel.triggerRemoveAlert(it) },
                            onCompleteClick = { menuScreenViewModel.triggerCompletedAlert(it) },
                            onDetailsClick = {
//                                coroutineScope.launch(Dispatchers.IO) {
                                    menuScreenViewModel.setDetailsScreenTarget(it.recipeEntity.recipeName);
                                    onDetailsClick()
//                                }

                            }
                        )

                    }
                }
                item() {
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
                }

                //Remove Alert
                if(uiAlertState.showRemoveAlert){
                    AlertDialog(
                        onDismissRequest = {},
                        text = {
                            Text(text = "Are you sure you want to remove " + uiAlertState.recipe.recipeEntity.recipeName +
                                    " from the Menu? (This will also remove it from the Shopping List.)",
                                color = Color(0xFF682300),
                                fontSize = 16.sp,
                                textAlign = TextAlign.Center
                            )
                        },
                        buttons = {
                            Row(
                                modifier = Modifier
                                    .padding(all = 8.dp)
                                    .fillMaxWidth(),
                                horizontalArrangement = Arrangement.SpaceEvenly
                            ) {

                                Button(
                                    modifier = Modifier.wrapContentSize(),
                                    onClick = {
                                        menuScreenViewModel.cancelRemoveAlert()
                                    },
                                    elevation = ButtonDefaults.elevation(6.dp),
                                    shape = RoundedCornerShape(25.dp),
                                    border = BorderStroke(
                                        width = 2.dp,
                                        brush = (Brush.horizontalGradient(
                                            startX = -10f,
                                            colors = listOf(Color(0xFFd8af84), Color(0xFFb15f33)),
                                            tileMode = TileMode.Mirror
                                        )),
                                    ),
                                    colors = ButtonDefaults.buttonColors(backgroundColor = Color(0xFFd8af84), contentColor = Color(0xFF682300))
                                ) {
                                    Text("Cancel")
                                }

                                Button(
                                    modifier = Modifier.wrapContentSize(),
                                    onClick = {
                                        onRemoveClick(uiAlertState.recipe)
                                        menuScreenViewModel.removeFromMenu(uiAlertState.recipe)
                                        menuScreenViewModel.cancelRemoveAlert()
                                    },
                                    elevation = ButtonDefaults.elevation(6.dp),
                                    shape = RoundedCornerShape(25.dp),
                                    border = BorderStroke(
                                        width = 2.dp,
                                        brush = (Brush.horizontalGradient(
                                            startX = -10f,
                                            colors = listOf(Color(0xFFd8af84), Color(0xFFb15f33)),
                                            tileMode = TileMode.Mirror
                                        )),
                                    ),
                                    colors = ButtonDefaults.buttonColors(backgroundColor = Color(0xFFd8af84), contentColor = Color(0xFF682300))
                                ) {
                                    Text("Yes")
                                }
                            }
                        },
                    )
                }

                //Completed Alert
                if(uiAlertState.showCompletedAlert){
                    AlertDialog(
                        onDismissRequest = {},
                        text = {
                            Text(text = "Mark " + uiAlertState.recipe.recipeEntity.recipeName +
                                    " as completed and remove from the Menu? (This will also remove it from the Shopping List.)",
                                color = Color(0xFF682300),
                                fontSize = 16.sp,
                                textAlign = TextAlign.Center
                            )
                        },
                        buttons = {
                            Row(
                                modifier = Modifier
                                    .padding(all = 8.dp)
                                    .fillMaxWidth(),
                                horizontalArrangement = Arrangement.SpaceEvenly
                            ) {

                                Button(
                                    modifier = Modifier.wrapContentSize(),
                                    onClick = {
                                        menuScreenViewModel.cancelCompletedAlert()
                                    },
                                    elevation = ButtonDefaults.elevation(6.dp),
                                    shape = RoundedCornerShape(25.dp),
                                    border = BorderStroke(
                                        width = 2.dp,
                                        brush = (Brush.horizontalGradient(
                                            startX = -10f,
                                            colors = listOf(Color(0xFFd8af84), Color(0xFFb15f33)),
                                            tileMode = TileMode.Mirror
                                        )),
                                    ),
                                    colors = ButtonDefaults.buttonColors(backgroundColor = Color(0xFFd8af84), contentColor = Color(0xFF682300))
                                ) {
                                    Text("Cancel")
                                }

                                Button(
                                    modifier = Modifier.wrapContentSize(),
                                    onClick = {
                                        /**
                                         * Add completed count +1 to Database
                                         */
                                        onCompleteClick(uiAlertState.recipe)
                                        menuScreenViewModel.addCooked(uiAlertState.recipe)
                                        menuScreenViewModel.removeFromMenu(uiAlertState.recipe)
                                        menuScreenViewModel.addExp(uiAlertState.recipe)
                                        menuScreenViewModel.cancelCompletedAlert()
                                    },
                                    elevation = ButtonDefaults.elevation(6.dp),
                                    shape = RoundedCornerShape(25.dp),
                                    border = BorderStroke(
                                        width = 2.dp,
                                        brush = (Brush.horizontalGradient(
                                            startX = -10f,
                                            colors = listOf(Color(0xFFd8af84), Color(0xFFb15f33)),
                                            tileMode = TileMode.Mirror
                                        )),
                                    ),
                                    colors = ButtonDefaults.buttonColors(backgroundColor = Color(0xFFd8af84), contentColor = Color(0xFF682300))
                                ) {
                                    Text("Yes")
                                }
                            }
                        },
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