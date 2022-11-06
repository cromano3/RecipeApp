package com.example.bearrecipebookapp.data

import androidx.lifecycle.LiveData
import com.example.bearrecipebookapp.datamodel.HomeScreenDataModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class HomeScreenRepository(private val homeScreenDao: HomeScreenDao) {

    private val coroutineScope = CoroutineScope(Dispatchers.Main)

    var homeScreenData: LiveData<List<HomeScreenDataModel>> = homeScreenDao.getData()


    fun updateMenu(recipeName: String, onMenuStatus: Int) {
        coroutineScope.launch(Dispatchers.IO) {
            homeScreenDao.updateMenu(recipeName, onMenuStatus)
        }
    }

    fun updateQuantityNeeded(ingredientName: String, quantityNeeded: Int){
        coroutineScope.launch(Dispatchers.IO) {
            homeScreenDao.updateQuantityNeeded(ingredientName, quantityNeeded)
        }
    }

    fun setIngredientQuantityOwned(ingredientEntity: IngredientEntity, quantityOwned: Int){
        coroutineScope.launch(Dispatchers.IO) {
            homeScreenDao.setIngredientQuantityOwned(ingredientEntity.ingredientName, quantityOwned)
        }
    }

    fun setDetailsScreenTarget(recipeName: String){
        coroutineScope.launch(Dispatchers.IO) {
            homeScreenDao.setDetailsScreenTarget(recipeName)
        }
    }
}