package com.example.bearrecipebookapp.ui

import android.app.Application
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.Surface
import androidx.compose.runtime.Composable
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
import com.example.bearrecipebookapp.ui.components.RecipeCard
import com.example.bearrecipebookapp.viewmodel.MenuScreenViewModel

@Composable
fun MenuScreen(
    onDetailsClick: () -> Unit,
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
                        onClick = { menuScreenViewModel.removeFromMenu(menuScreenData[x]) },
                        onDetailsClick = { menuScreenViewModel.setDetailsScreenTarget(menuScreenData[x].recipeEntity.recipeName);
                            onDetailsClick()
                        }
                    )
                }

            }
            //Draw all recipes on the list
//        LazyColumn {
//            items(selectedRecipesList) {
//                //needs current screen parameter
//                RecipeCard(
//                    recipeWithIngredients = it,
//                    selected = true,
//                    currentScreen = "WeeklyMenuScreen",
//                    onClick = { onClick(it) },
//                    onDetailsClick = { onDetailsClick(it) }
//                )
//            }
//        }
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