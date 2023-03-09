package com.example.bearrecipebookapp.data.repository

import android.app.Application
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.example.bearrecipebookapp.data.entity.CommentsEntity
import com.example.bearrecipebookapp.datamodel.AuthorData
import com.example.bearrecipebookapp.datamodel.AuthorDataWithComment
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.firestore.EventListener
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.Query
import com.google.firebase.firestore.QuerySnapshot
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.cancel
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.tasks.await

class DetailsScreenFirebaseRepository(
    application: Application,
    private val db: FirebaseFirestore,
    private val auth: FirebaseAuth,
) {

    private val recipeNameLiveData = MutableLiveData<String>()
//    private val commentResultLimit = MutableLiveData<Int>()


    private val coroutineScope = CoroutineScope(Dispatchers.IO)


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

//    val globalRatingFirebaseLiveData: LiveData<Int> = MediatorLiveData<Int>().apply {
//        addSource(recipeNameLiveData) { recipeName ->
//            retrieveGlobalRatingFirebaseLiveData(recipeName) { rating ->
//                    value = rating
//            }
//        }
//    }


    fun setRecipeName(recipeName: String) {
        println("set name to $recipeName")
        recipeNameLiveData.postValue(recipeName)
    }

    fun currentUser(): FirebaseUser? {
        return auth.currentUser
    }

//    fun setCommentResultLimit(limit: Int) {
//        println("set limit to $limit set limit to $limit set limit to $limit set limit to $limit set limit to $limit set limit to $limit ")
//        commentResultLimit.postValue(limit)
//
//    }

//    private fun retrieveGlobalRatingFirebaseLiveData(recipeName: String, callback: (Int) -> Unit){
//        db.collection("recipes").whereEqualTo(FieldPath.documentId(), recipeName).limit(1).addSnapshotListener(){ snapshot, e ->
//            if(e != null){
//                println("couldn't get rating with: ${e.message}")
//                callback(0)
//            }
//            else{
//                try {
//                    val rating = snapshot!!.documents[0].getLong("rating")!!.toInt()
//                    callback(rating)
//                }
//                catch(e: Exception){
//                    println("couldn't get rating with: ${e.message}")
//                    callback(0)
//                }
//
//            }
//
//        }
//
//    }



    fun getCommentsList(recipeName: String, limit: Int): Flow<List<AuthorDataWithComment>> = callbackFlow {
        println("CALL BACK FLOW")
        val listener = EventListener<QuerySnapshot> { snapshot, exception ->
                if(exception != null){
                    println("FEIFJOEFJ $exception")
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
                        val authorEmail = comment.getString("authorEmail") ?: ""
                        val likes = comment.getDouble("likes")?.toInt() ?: 0

                        var likedByMe = 0

                        var likedByList = comment.get("likedBy") as? List<String> ?: listOf()

                        if(likedByList.contains(auth.currentUser?.email)){
                            likedByMe = 1
                        }

                        val thisCommentEntity = CommentsEntity(
                            commentID = commentId,
                            recipeName  = thisRecipeName,
                            authorID = "",
                            commentText = reviewText,
                            likes = likes,
                            likedByMe = likedByMe,
                            myLikeWasSynced = 0,
                            timestamp = ""
                        )

                        db.collection("users").whereEqualTo("email", authorEmail).get().addOnSuccessListener {

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
            .limit(limit.toLong())
            .orderBy("likes", Query.Direction.DESCENDING)
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

//    suspend fun getComments(recipeName: String): MutableList<CommentsEntity>{
//
//        val reviewsCollection = db.collection("reviews")
//        val querySnapshot = reviewsCollection.whereEqualTo("recipeName", recipeName).get().await()
//
//        val resultCommentsList: MutableList<CommentsEntity> = mutableListOf()
//
//        if (querySnapshot != null) {
//            for (document in querySnapshot.documents) {
//                val commentId = document.id
//                val thisRecipeName = document.getString("recipeName")
//                val reviewText = document.getString("reviewText")
//                val authorUid = document.getString("authorUid")
//                val likes = document.getDouble("likes")?.toInt()
//                val timestamp = document.getTimestamp("timestamp")
//                val date = timestamp?.toDate()
//                val formatter = SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.US)
//                var formattedDate = "Failed"
//                try{
//                    formattedDate = formatter.format(date!!)
//                }
//                catch (e: Exception){
//                    println("bad timestamp in firestore ${e.message}")
//                }
//
//                val comment = CommentsEntity(
//                    commentID = commentId,
//                    recipeName  = thisRecipeName ?: "",
//                    authorID = authorUid ?: "",
//                    commentText = reviewText ?: "",
//                    likes = likes ?: 0,
//                    likedByMe = 0,
//                    myLikeWasSynced = 0,
//                    timestamp = formattedDate
//                )
//
//                resultCommentsList.add(comment)
//
//            }
//        }
//        return resultCommentsList
//
//    }
}