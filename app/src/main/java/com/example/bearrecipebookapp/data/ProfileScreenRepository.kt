package com.example.bearrecipebookapp.data

import androidx.lifecycle.LiveData
import com.example.bearrecipebookapp.datamodel.RecipeWithIngredients
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class ProfileScreenRepository(private val profileScreenDao: ProfileScreenDao) {

    private val coroutineScope = CoroutineScope(Dispatchers.IO)

    var favoritesData: LiveData<List<RecipeWithIngredients>> = profileScreenDao.getFavorites()
    var cookedData: LiveData<List<RecipeWithIngredients>> = profileScreenDao.getCooked()

    fun updateFavorite(recipeName: String, isFavoriteStatus: Int) {
        coroutineScope.launch(Dispatchers.IO) {
            profileScreenDao.updateFavorite(recipeName, isFavoriteStatus)
        }
    }

    fun setDetailsScreenTarget(recipeName: String){
        coroutineScope.launch(Dispatchers.IO) {
            profileScreenDao.setDetailsScreenTarget(recipeName)
        }
    }

}