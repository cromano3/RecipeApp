package com.example.bearrecipebookapp.data

import com.example.bearrecipebookapp.datamodel.HomeScreenDataModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers

class SearchScreenRepository(private val searchScreenDao: SearchScreenDao) {

    private val coroutineScope = CoroutineScope(Dispatchers.Main)

    suspend fun getRecipeNamesReferenceList(): List<String>{
        return searchScreenDao.getRecipeNamesReferenceList()
    }

    suspend fun getIngredientNamesReferenceList(): List<String>{
        return searchScreenDao.getIngredientNamesReferenceList()
    }

    suspend fun getFilterNamesReferenceList(): List<String>{
            return searchScreenDao.getFilterNamesReferenceList()
    }

    suspend fun getRecipes(): List<HomeScreenDataModel>{
        return searchScreenDao.getRecipes()
    }
}