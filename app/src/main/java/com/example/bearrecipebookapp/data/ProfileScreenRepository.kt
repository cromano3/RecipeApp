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
    var expToGive: LiveData<Int> = profileScreenDao.getExpToGive()
    var exp: LiveData<Int> = profileScreenDao.getExp()

    suspend fun getExpToGive(): Int{
        return profileScreenDao.getExpToGive1()
    }

    suspend fun getExp(): Int{
        return profileScreenDao.getExp1()
    }

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

    suspend fun addToExp(expChange: Int){
//        coroutineScope.launch(Dispatchers.IO) {
            profileScreenDao.addToExp(expChange)
//        }
    }

    suspend fun removeFromExpToGive(expChange: Int){
//        coroutineScope.launch(Dispatchers.IO) {
            profileScreenDao.removeFromExpToGive(expChange)
//        }
    }

    suspend fun clearExpToGive(){
//        coroutineScope.launch(Dispatchers.IO) {
            profileScreenDao.clearExpToGive()
//        }
    }

}