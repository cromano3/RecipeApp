package com.example.bearrecipebookapp.data

import androidx.lifecycle.LiveData
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers

class HomeScreenRepository(private val homeScreenDao: HomeScreenDao) {

    private val coroutineScope = CoroutineScope(Dispatchers.Main)

    var homeScreenData: LiveData<List<IngredientEntity>> = homeScreenDao.getData()
}