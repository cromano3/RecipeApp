package com.example.bearrecipebookapp.data.repository

import com.example.bearrecipebookapp.data.entity.CommentsEntity
import com.example.bearrecipebookapp.datamodel.AuthorData
import com.example.bearrecipebookapp.datamodel.AuthorDataWithComment
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

class ProfileScreenFirebaseRepository(
    private val db: FirebaseFirestore,
    private val auth: FirebaseAuth,
) {



    fun currentUser(): FirebaseUser? {
        return auth.currentUser
    }

    fun getCommentsList(): Flow<List<AuthorDataWithComment>> = callbackFlow {

        println("do get comments")
        println("auth Email is :" + auth.currentUser?.email)
        val listener = EventListener<QuerySnapshot> { snapshot, exception ->
            if(exception != null){
                println("FAILED WITH: $exception")
                cancel()
            }
            if (snapshot != null){
                println("Success getting comments for profile screen")



                val numDocuments = snapshot.documents.size
                var numCallbacks = 0

                println("NUM DOCS IS: $numDocuments")

                val commentsResult = mutableListOf<CommentsEntity>()
                val result = mutableListOf<AuthorDataWithComment>()

                for(comment in snapshot){

                    val commentId = comment.id
                    val thisRecipeName = comment.getString("recipeName")
                    val reviewText = comment.getString("reviewText")
                    val authorEmail = comment.getString("authorEmail")
                    val likes = comment.getDouble("likes")?.toInt()

                    var likedByMe = 0

                    val likedByList = comment.get("likedBy") as? List<String> ?: listOf()

                    if(likedByList.contains(auth.currentUser?.email)){
                        likedByMe = 1
                    }

                    val thisCommentEntity = CommentsEntity(
                        commentID = commentId,
                        recipeName  = thisRecipeName ?: "",
                        authorID = "",
                        commentText = reviewText ?: "",
                        likes = likes ?: 0,
                        likedByMe = likedByMe,
                        myLikeWasSynced = 0,
                        timestamp = ""
                    )

                    commentsResult.add(thisCommentEntity)

                }

                db.collection("users").whereEqualTo("email", auth.currentUser?.email ?: "").get().addOnSuccessListener {
                    //found author

                    for(comment in commentsResult){
                        result.add(
                            AuthorDataWithComment(
                                AuthorData(
                                    it.documents[0].getString("display_name") ?: "",
                                    it.documents[0].getString("user_photo") ?: "",
                                    karma = it.documents[0].getLong("karma")?.toInt() ?: 0
                                ),
                                comment
                            )
                        )
                    }

                    trySend(result)

                }.addOnFailureListener(){
                    //failed to get author
                    for(comment in commentsResult){
                        result.add(AuthorDataWithComment(AuthorData("", "", 0), comment))
                    }

                    trySend(result)

                }

            } else{
                //document does not exist or is empty
            }
        }

        val registration = db
            .collection("reviews")
            .whereEqualTo("authorEmail", auth.currentUser?.email ?: "")
            .orderBy("likes", Query.Direction.DESCENDING)
            .addSnapshotListener(listener)

        awaitClose {registration.remove()}
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