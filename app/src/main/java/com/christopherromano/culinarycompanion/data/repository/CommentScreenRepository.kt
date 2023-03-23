package com.christopherromano.culinarycompanion.data.repository

import androidx.lifecycle.LiveData
import com.christopherromano.culinarycompanion.data.dao.CommentScreenDao
import com.christopherromano.culinarycompanion.datamodel.RecipeWithIngredientsAndInstructions
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

}