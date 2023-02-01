package com.example.bearrecipebookapp.data.repository

import androidx.lifecycle.LiveData
import com.example.bearrecipebookapp.data.dao.CommentScreenDao
import com.example.bearrecipebookapp.datamodel.RecipeWithIngredientsAndInstructions
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class CommentScreenRepository(private val commentScreenDao: CommentScreenDao) {

    private val coroutineScope = CoroutineScope(Dispatchers.IO)

    var commentScreenData: LiveData<RecipeWithIngredientsAndInstructions> = commentScreenDao.getCommentScreenTarget()


    fun setReviewAsWritten(recipeName: String){
        coroutineScope.launch(Dispatchers.IO){
            commentScreenDao.setReviewAsWritten(recipeName)
        }
    }

    fun setReview(recipeName: String, reviewText: String){
        coroutineScope.launch {
            commentScreenDao.setReview(recipeName, reviewText)
        }
    }
}