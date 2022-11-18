package com.example.bearrecipebookapp.ui

import android.app.Application
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.AlertDialog
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.material.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewmodel.compose.LocalViewModelStoreOwner
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.bearrecipebookapp.datamodel.RecipeWithIngredients
import com.example.bearrecipebookapp.ui.components.RecipeCard
import com.example.bearrecipebookapp.viewmodel.MenuScreenViewModel

@Composable
fun MenuScreen(
    onDetailsClick: () -> Unit,
    onFavoriteClick: (RecipeWithIngredients) -> Unit,
) {

    val owner = LocalViewModelStoreOwner.current

    owner?.let {
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


        Surface(
            modifier = Modifier.fillMaxSize().padding(bottom = 48.dp),
            color = Color(0xFFd8af84)

        ) {
            Column(Modifier.verticalScroll(rememberScrollState())) {
                for (x in menuScreenData.indices) {

                    var myInt = 0
                    if(x + 1 == menuScreenData.size){
                        myInt = 16
                    }

                    RecipeCard(
                        modifier = Modifier.padding(bottom = myInt.dp),
                        recipeWithIngredients = menuScreenData[x],
                        currentScreen = "WeeklyMenuScreen",
                        onFavoriteClick =
                        {
                            menuScreenViewModel.toggleFavorite(menuScreenData[x])
                            onFavoriteClick(menuScreenData[x])
                        },
                        onRemoveClick = { menuScreenViewModel.triggerRemoveAlert(menuScreenData[x]) },
                        onCompleteClick = { menuScreenViewModel.triggerCompletedAlert(menuScreenData[x]) },
                        onDetailsClick = { menuScreenViewModel.setDetailsScreenTarget(menuScreenData[x].recipeEntity.recipeName);
                            onDetailsClick()
                        }
                    )
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
                                    menuScreenViewModel.removeFromMenu(uiAlertState.recipe)
                                    menuScreenViewModel.cancelRemoveAlert()
                                }
                            ) {
                                Text("Yes")
                            }
                        },
                        dismissButton = {
                            TextButton(
                                onClick = {
                                    menuScreenViewModel.cancelRemoveAlert()
                                }
                            ) {
                                Text("Cancel")
                            }
                        }
                    )
                }

                //Completed Alert
                if(uiAlertState.showCompletedAlert){
                    AlertDialog(
                        onDismissRequest = {},
                        text = {
                            Text(text = "Mark " + uiAlertState.recipe.recipeEntity.recipeName +
                                    " as completed and remove from the Menu? (This will also remove it from the Shopping List.)" )
                        },
                        confirmButton = {
                            TextButton(
                                onClick = {
                                    /**
                                     * Add completed count +1 to Database
                                     */
                                    menuScreenViewModel.removeFromMenu(uiAlertState.recipe)
                                    menuScreenViewModel.cancelCompletedAlert()
                                }
                            ) {
                                Text("Yes")
                            }
                        },
                        dismissButton = {
                            TextButton(
                                onClick = {
                                    menuScreenViewModel.cancelCompletedAlert()
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