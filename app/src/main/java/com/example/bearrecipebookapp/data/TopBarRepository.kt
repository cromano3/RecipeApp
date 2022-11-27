package com.example.bearrecipebookapp.data

import androidx.lifecycle.LiveData
import com.example.bearrecipebookapp.datamodel.RecipeWithIngredientsAndInstructions
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch


class TopBarRepository(private val topBarDao: TopBarDao) {

    private val coroutineScope = CoroutineScope(Dispatchers.Main)

    var detailsScreenData: LiveData<RecipeWithIngredientsAndInstructions> = topBarDao.getData()


    fun updateFavorite(recipeName: String, isFavoriteStatus: Int) {
        coroutineScope.launch(Dispatchers.IO) {
            topBarDao.updateFavorite(recipeName, isFavoriteStatus)
        }
    }


//    fun setDetailsScreenTarget(recipeName: String){
//        coroutineScope.launch(Dispatchers.IO) {
//            menuScreenDao.setDetailsScreenTarget(recipeName)
//        }
//    }
}