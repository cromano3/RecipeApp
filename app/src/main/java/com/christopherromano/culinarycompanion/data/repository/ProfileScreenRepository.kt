package com.christopherromano.culinarycompanion.data.repository

import androidx.lifecycle.LiveData
import com.christopherromano.culinarycompanion.data.dao.ProfileScreenDao
import com.christopherromano.culinarycompanion.datamodel.RecipeWithIngredients
import com.christopherromano.culinarycompanion.datamodel.RecipeWithIngredientsAndInstructions
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class ProfileScreenRepository(private val profileScreenDao: ProfileScreenDao) {

    private val coroutineScope = CoroutineScope(Dispatchers.IO)

    var favoritesData: LiveData<List<RecipeWithIngredientsAndInstructions>> = profileScreenDao.getFavorites()
    var cookedData: LiveData<List<RecipeWithIngredients>> = profileScreenDao.getCooked()
    var expToGive: LiveData<Int> = profileScreenDao.getExpToGive()

    fun getExpToGive(): Int{
        return profileScreenDao.getExpToGive1()
    }

    fun getExp(): Int{
        return profileScreenDao.getExp1()
    }

    fun updateFavorite(recipeName: String, isFavoriteStatus: Int) {
        coroutineScope.launch(Dispatchers.IO) {
            profileScreenDao.updateFavorite(recipeName, isFavoriteStatus)
        }
    }

    fun setDetailsScreenTarget(recipeName: String){
        profileScreenDao.setDetailsScreenTarget(recipeName)
    }

    fun updateExp(expChange: Int){
        profileScreenDao.updateExp(expChange)
    }

}