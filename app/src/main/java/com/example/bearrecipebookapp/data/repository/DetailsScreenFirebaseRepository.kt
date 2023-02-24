package com.example.bearrecipebookapp.data.repository

import android.app.Application
import androidx.lifecycle.LiveData
import androidx.lifecycle.MediatorLiveData
import androidx.lifecycle.MutableLiveData
import com.example.bearrecipebookapp.data.entity.CommentsEntity
import com.example.bearrecipebookapp.datamodel.ReviewWithAuthorDataModel
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.tasks.await
import java.text.SimpleDateFormat
import java.util.*

class DetailsScreenFirebaseRepository(
    application: Application,
    private val db: FirebaseFirestore,
    private val auth: FirebaseAuth,
) {

    private val recipeNameLiveData = MutableLiveData<String>()
    private val commentResultLimit = MutableLiveData<Int>()

    val firebaseCommentsLiveData: LiveData<List<ReviewWithAuthorDataModel>> = MediatorLiveData<List<ReviewWithAuthorDataModel>>().apply {
        addSource(recipeNameLiveData) { recipeName ->
            val limit = commentResultLimit.value
            if (limit != null) {
                value = getReviewsData(recipeName, limit)
            }
        }
        addSource(commentResultLimit) { limit ->
            val recipeName = recipeNameLiveData.value
            if (!recipeName.isNullOrEmpty()) {
                value = getReviewsData(recipeName, limit)
            }
        }
    }

    fun setRecipeName(recipeName: String) {
        recipeNameLiveData.postValue(recipeName)
    }

    fun setCommentResultLimit(limit: Int) {
        commentResultLimit.postValue(limit)
    }

    private fun getReviewsData(recipeName: String, limit: Int): List<ReviewWithAuthorDataModel>? {
        db.collection("recipes").where

        //firestore db query goes here

        //get recipe doc ref

        //get all comments for this recipe and put in comment entity or new data type

        //for all comments get all author details and add to reviewWithAuthorResultList

        //for all comments -> for each comment query.get.likedByList where equals current user UID .limit(1)
        //if result is found then set comment to liked by me in comment entity else not liked by me


        //build this same query but for top comments to show first (choose how many to show, maybe top 3?)


        return result

    }




    suspend fun getComments(recipeName: String): MutableList<CommentsEntity>{

        val reviewsCollection = db.collection("reviews")
        val querySnapshot = reviewsCollection.whereEqualTo("recipeName", recipeName).get().await()

        val resultCommentsList: MutableList<CommentsEntity> = mutableListOf()

        if (querySnapshot != null) {
            for (document in querySnapshot.documents) {
                val commentId = document.id
                val thisRecipeName = document.getString("recipeName")
                val reviewText = document.getString("reviewText")
                val authorUid = document.getString("authorUid")
                val likes = document.getDouble("likes")?.toInt()
                val timestamp = document.getTimestamp("timestamp")
                val date = timestamp?.toDate()
                val formatter = SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.US)
                var formattedDate = "Failed"
                try{
                    formattedDate = formatter.format(date!!)
                }
                catch (e: Exception){
                    println("bad timestamp in firestore ${e.message}")
                }

                val comment = CommentsEntity(
                    commentID = commentId,
                    recipeName  = thisRecipeName ?: "",
                    authorID = authorUid ?: "",
                    commentText = reviewText ?: "",
                    likes = likes ?: 0,
                    likedByMe = 0,
                    myLikeWasSynced = 0,
                    timestamp = formattedDate
                )

                resultCommentsList.add(comment)

            }
        }
        return resultCommentsList

    }
}