package com.christopherromano.culinarycompanion.data.repository

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

class ProfileScreenFirebaseRepository(
    private val db: FirebaseFirestore,
    private val auth: FirebaseAuth,
) {



    fun currentUser(): FirebaseUser? {
        return auth.currentUser
    }

    fun getCommentsList(): Flow<List<AuthorDataWithComment>> = callbackFlow {

        println("do get comments")
        println("auth uid is :" + auth.currentUser?.uid)
        val listener = EventListener<QuerySnapshot> { snapshot, exception ->
            if(exception != null){
                println("FAILED WITH: $exception")
                cancel()
            }
            if (snapshot != null){
                println("Success getting comments for profile screen")



                val numDocuments = snapshot.documents.size

                println("NUM DOCS IS: $numDocuments")

                val commentsResult = mutableListOf<CommentsEntity>()
                val result = mutableListOf<AuthorDataWithComment>()

                for(comment in snapshot){

                    val commentId = comment.id
                    val thisRecipeName = comment.getString("recipeName")
                    val reviewText = comment.getString("reviewText")
                    val likes = comment.getDouble("likes")?.toInt()


                    val thisCommentEntity = CommentsEntity(
                        commentID = commentId,
                        recipeName  = thisRecipeName ?: "",
                        authorID = "",
                        commentText = reviewText ?: "",
                        likes = likes ?: 0,
                        likedByMe = 1,
                        dislikedByMe = 0,
                        myLikeWasSynced = 0,
                        timestamp = ""
                    )

                    commentsResult.add(thisCommentEntity)

                }

                db.collection("users").whereEqualTo("uid", auth.currentUser?.uid ?: "").get().addOnSuccessListener {
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
            .whereEqualTo("authorUid", auth.currentUser?.uid ?: "")
            .orderBy("likes", Query.Direction.DESCENDING)
            .addSnapshotListener(listener)

        awaitClose {registration.remove()}
    }

}