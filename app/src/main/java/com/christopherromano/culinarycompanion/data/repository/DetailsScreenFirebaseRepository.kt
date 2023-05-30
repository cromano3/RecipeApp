package com.christopherromano.culinarycompanion.data.repository

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.christopherromano.culinarycompanion.data.entity.CommentsEntity
import com.christopherromano.culinarycompanion.datamodel.AuthorData
import com.christopherromano.culinarycompanion.datamodel.AuthorDataWithComment
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.firestore.EventListener
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.Query
import com.google.firebase.firestore.QuerySnapshot
import kotlinx.coroutines.cancel
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.tasks.await

class DetailsScreenFirebaseRepository(
    private val db: FirebaseFirestore,
    private val auth: FirebaseAuth,
) {

    private val recipeNameLiveData = MutableLiveData<String>()


    private val _authState = MutableLiveData<Int>()
    val authState: LiveData<Int>
        get() = _authState



    init {
        auth.addAuthStateListener { auth ->
            if (auth.currentUser != null) {
                _authState.value = 1
            } else {
                _authState.value = 0
            }
        }
    }



    fun setRecipeName(recipeName: String) {
        println("set name to $recipeName")
        recipeNameLiveData.postValue(recipeName)
    }

    fun currentUser(): FirebaseUser? {
        return auth.currentUser
    }



    fun getCommentsList(recipeName: String, limit: Int): Flow<List<AuthorDataWithComment>> = callbackFlow {
        println("CALL BACK FLOW")
        val listener = EventListener<QuerySnapshot> { snapshot, exception ->
                if(exception != null){
                    cancel()
                }
                if (snapshot != null){
                    println("SNAPSHOT OK")

                    val numDocuments = snapshot.documents.size
                    println("size is: $numDocuments")
                    var numCallbacks = 0

                    val result = mutableListOf<AuthorDataWithComment>()
                    for(comment in snapshot){

                        val commentId = comment.id
                        val thisRecipeName = comment.getString("recipeName") ?: ""
                        val reviewText = comment.getString("reviewText") ?: ""
                        val authorUid = comment.getString("authorUid") ?: ""
                        val likes = comment.getDouble("likes")?.toInt() ?: 0

                        var likedByMe = 0

                        val likedByList = comment.get("likedBy") as? List<String> ?: listOf()

                        if(likedByList.contains(auth.currentUser?.uid)){
                            likedByMe = 1
                        }

                        var dislikedByMe = 0

                        val dislikedByList = comment.get("dislikedBy") as? List<String> ?: listOf()

                        if(dislikedByList.contains(auth.currentUser?.uid)){
                            dislikedByMe = 1
                        }

                        var reportedByMe = 0

                        val reportedByList = comment.get("reportedBy") as? List<String> ?: listOf()

                        if(reportedByList.contains(auth.currentUser?.uid)){
                            reportedByMe = 1
                        }

                        val thisCommentEntity = CommentsEntity(
                            commentID = commentId,
                            recipeName  = thisRecipeName,
                            authorID = "",
                            commentText = reviewText,
                            likes = likes,
                            likedByMe = likedByMe,
                            dislikedByMe = dislikedByMe,
                            reportedByMe = reportedByMe,
                        )

                        db.collection("users").whereEqualTo("uid", authorUid).get().addOnSuccessListener {

                            if(it != null && !it.isEmpty) {
                                result.add(
                                    AuthorDataWithComment(
                                        AuthorData(
                                            it.documents[0].getString("display_name") ?: "",
                                            it.documents[0].getString("user_photo") ?: "",
                                            karma = it.documents[0].getLong("karma")?.toInt() ?: 0
                                        ),
                                        thisCommentEntity
                                    )
                                )
                            }
                            else{
                                result.add(
                                    AuthorDataWithComment(
                                        AuthorData("", "", 0),
                                        thisCommentEntity
                                    )
                                )
                            }
                            numCallbacks++
                            if(numCallbacks == numDocuments){
                                trySend(result)
                            }

                        }.addOnFailureListener(){
                            //failed to get author
                            result.add(
                                AuthorDataWithComment(
                                    AuthorData("", "", 0),
                                    thisCommentEntity
                                )
                            )
                            numCallbacks++
                            if(numCallbacks == numDocuments){
                                trySend(result)
                            }
                        }
                    }

                } else{
                    //document does not exist or is empty
                }
            }

        val registration = db
            .collection("reviews")
            .whereEqualTo("recipeName", recipeName)
            .whereEqualTo("isModApproved", 1)
            .whereEqualTo("isDeleted", 0)
            .whereGreaterThan("likes", -2)
            .orderBy("likes", Query.Direction.DESCENDING)
            .limit(limit.toLong())
            .addSnapshotListener(listener)

        awaitClose {registration.remove()}
    }



    suspend fun getGlobalRating(recipeName: String): Int {


        var result: Int = 0

        try {
            val query =
                db.collection("recipes").whereEqualTo("recipeName", recipeName).limit(1).get().await()


            val thisRating = query.documents[0].getLong("rating")?.toInt() ?: 0

            result = thisRating
        }
        catch(e: Exception){
            println("Failed to get Detail Screen Rating from Firebase with: $e")
        }

        return result
    }


}