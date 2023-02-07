package com.example.bearrecipebookapp.data.repository

import com.example.bearrecipebookapp.data.dao.AppDao
import com.example.bearrecipebookapp.datamodel.RecipeWithIngredientsAndInstructions
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers

class AppRepository(private val appDao: AppDao) {

    private val coroutineScope = CoroutineScope(Dispatchers.IO)

//    var commentScreenData: LiveData<RecipeWithIngredientsAndInstructions> = commentScreenDao.getCommentScreenTarget()

    suspend fun getRecipeWithIngredientsAndInstructions(recipeName: String): RecipeWithIngredientsAndInstructions{
        return appDao.getRecipeWithIngredientsAndInstructions(recipeName)
    }

//    fun setReviewAsWritten(recipeName: String){
//        coroutineScope.launch(Dispatchers.IO){
//            commentScreenDao.setReviewAsWritten(recipeName)
//        }
//    }
//
//    fun setReview(recipeName: String, reviewText: String){
//        coroutineScope.launch {
//            commentScreenDao.setReview(recipeName, reviewText)
//        }
//    }
}