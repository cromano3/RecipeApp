package com.example.bearrecipebookapp.data.repository

import android.app.Application
import androidx.lifecycle.LiveData
import androidx.lifecycle.MediatorLiveData
import androidx.lifecycle.MutableLiveData
import com.example.bearrecipebookapp.data.entity.CommentsEntity
import com.example.bearrecipebookapp.datamodel.AuthorData
import com.example.bearrecipebookapp.datamodel.AuthorDataWithComment
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FieldPath
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
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

    private val coroutineScope = CoroutineScope(Dispatchers.IO)

    val firebaseCommentsLiveData: LiveData<List<AuthorDataWithComment>> = MediatorLiveData<List<AuthorDataWithComment>>().apply {
        addSource(recipeNameLiveData) { recipeName ->
            val limit = commentResultLimit.value
            if (limit != null) {
                println("passed null")
                getReviewsData(recipeName, limit){ resultCommentsWithAuthorsList ->
                    value = resultCommentsWithAuthorsList
                }
            }
        }
        addSource(commentResultLimit) { limit ->
            val recipeName = recipeNameLiveData.value
            if (!recipeName.isNullOrEmpty()) {
                println("passed null")
                getReviewsData(recipeName, limit){ resultCommentsWithAuthorsList ->
                    value = resultCommentsWithAuthorsList
                }
            }
        }
    }

    fun setRecipeName(recipeName: String) {
        println("set name to $recipeName")
        recipeNameLiveData.postValue(recipeName)
    }

    fun setCommentResultLimit(limit: Int) {
        println("set limit to $limit")
        commentResultLimit.postValue(limit)
    }

    private fun getReviewsData(recipeName: String, limit: Int, callback: (List<AuthorDataWithComment>) -> Unit) {
        println("in get live")


        db.collection("reviews").whereEqualTo("recipeName", recipeName).limit(if(limit == 4)limit.toLong()else 50).addSnapshotListener() { snapshot, e ->
            if(e != null){
                println("failed first live query with: ${e.message}")
            }
            else {
                try {
                    val resultCommentsWithAuthorsList: MutableList<AuthorDataWithComment> =
                        mutableListOf()
                    println("try get live")
                    for (document in snapshot!!.documents) {
                        println("for try get live")

                        var authorData: AuthorData = AuthorData("", "")

                        val commentId = document.id
                        val thisRecipeName = document.getString("recipeName")
                        val reviewText = document.getString("reviewText")
                        val authorUid = document.getString("authorUid")
                        println("authorUid is $authorUid")

                        db.collection("users").whereEqualTo(FieldPath.documentId(), authorUid)
                            .limit(1).get().addOnSuccessListener { snapshot2 ->
                            try {
                                val userName =
                                    snapshot2!!.documents[0].getString("display_name")
                                val photoUrl = snapshot2.documents[0].getString("user_photo")
                                println(userName)
                                println(photoUrl)
                                authorData = AuthorData(userName!!, photoUrl!!)

                                val likes = document.getDouble("likes")?.toInt()


                                val comment = CommentsEntity(
                                    commentID = commentId,
                                    recipeName = thisRecipeName ?: "",
                                    authorID = authorUid ?: "",
                                    commentText = reviewText ?: "",
                                    likes = likes ?: 0,
                                    likedByMe = 0,
                                    myLikeWasSynced = 0,
                                    timestamp = ""
                                )
                                resultCommentsWithAuthorsList.add(
                                    AuthorDataWithComment(
                                        authorData,
                                        comment
                                    )
                                )
                                println("BELOW")
                                println(resultCommentsWithAuthorsList[0].authorData.userName)
                                println(resultCommentsWithAuthorsList[0].authorData.userPhotoURL)
                                println("ABOVE")
                                callback(resultCommentsWithAuthorsList)

                            } catch (e3: Exception) {
                                println("failed trying to get the author live ${e3.message}")
                                authorData = AuthorData("", "")
                            }
                        }



                    }


                } catch (e: Exception) {
                    println("failed to get live with: ${e.message}")
                }
            }
        }


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