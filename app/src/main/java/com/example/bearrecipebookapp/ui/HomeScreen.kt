package com.example.bearrecipebookapp.ui

import android.app.Application
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
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
import com.example.bearrecipebookapp.ui.components.SmallRecipeCard
import com.example.bearrecipebookapp.viewmodel.HomeScreenViewModel

@Composable
fun HomeScreen(
 //   homeScreenViewModel: HomeScreenViewModel = viewModel(),
    onDetailsClick: () -> Unit
) {


    val owner = LocalViewModelStoreOwner.current

    owner?.let {
        val homeScreenViewModel: HomeScreenViewModel = viewModel(
            it,
            "HomeScreenViewModel",
            HomeScreenViewModelFactory(
                LocalContext.current.applicationContext
                        as Application,
            )
        )

        val homeScreenData by homeScreenViewModel.homeScreenData.observeAsState(listOf())



        Surface(
            modifier = Modifier.fillMaxSize().padding(top = 0.dp, bottom = 48.dp),
            color = Color(0xFFd8af84)

        ) {
            LazyVerticalGrid(

                columns = GridCells.Fixed(2),
                modifier = Modifier
                    .padding(top = 0.dp, start = 16.dp, end = 16.dp, bottom = 0.dp),
//                verticalArrangement = Arrangement.spacedBy(16.dp),
                horizontalArrangement = Arrangement.spacedBy(16.dp)
                    ) {
                        items(homeScreenData.size) { index ->
                            var myInt = 0
                            if(index + 1 == homeScreenData.size){
                                myInt = 16
                            }
                            SmallRecipeCard(
                                modifier = Modifier.padding(bottom = myInt.dp),
                                recipe = homeScreenData[index].recipeEntity,
                                ingredients = homeScreenData[index].ingredientsList,
                                //this does onMenu updates and related ingredients updates
                                //needs to be changed to menu button when we add menu button
                                onClick = { homeScreenViewModel.toggleFavorite(homeScreenData[index]) },
                                onDetailsClick = {
                                    homeScreenViewModel.setDetailsScreenTarget(homeScreenData[index].recipeEntity.recipeName);
                                    onDetailsClick()
                                }

                            )
                        }
                    }
        }




    }

}

class HomeScreenViewModelFactory(
    val application: Application,
//    val recipeName: String
) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {

        return HomeScreenViewModel(
            application,
            //  recipeName
        ) as T
    }

}