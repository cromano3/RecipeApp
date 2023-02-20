package com.example.bearrecipebookapp.data.repository

import android.app.Application
import com.example.bearrecipebookapp.data.entity.CommentsEntity
import com.example.bearrecipebookapp.datamodel.AuthorData
import com.example.bearrecipebookapp.datamodel.FirebaseResult
import com.example.bearrecipebookapp.datamodel.RecipeNameAndRating
import com.example.bearrecipebookapp.datamodel.RecipeNameAndReview
import com.google.android.gms.auth.api.identity.BeginSignInRequest
import com.google.android.gms.auth.api.identity.Identity
import com.google.android.gms.auth.api.identity.SignInClient
import com.google.firebase.auth.AuthCredential
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.firestore.FieldValue.serverTimestamp
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.tasks.await
import java.sql.Timestamp

class FirebaseRepository(
    application: Application,
    private val db: FirebaseFirestore,
    private val auth: FirebaseAuth,
) {
    var oneTapClient: SignInClient = Identity.getSignInClient(application)

    private var signInRequest: BeginSignInRequest = BeginSignInRequest.builder()
        .setGoogleIdTokenRequestOptions(
            BeginSignInRequest.GoogleIdTokenRequestOptions.builder()
                .setSupported(true)
                .setServerClientId("135954228549-fin8hnctgqhnrdcpgrpvgbioeeg98t6n.apps.googleusercontent.com")
                // Only show accounts previously used to sign in.
                .setFilterByAuthorizedAccounts(true)
                .build()
        )
        .setAutoSelectEnabled(true)
        .build()

    private var signUpRequest: BeginSignInRequest = BeginSignInRequest.builder()
        .setGoogleIdTokenRequestOptions(
            BeginSignInRequest.GoogleIdTokenRequestOptions.builder()
                .setSupported(true)
                .setServerClientId("135954228549-fin8hnctgqhnrdcpgrpvgbioeeg98t6n.apps.googleusercontent.com")
                // Only show accounts previously used to sign in.
                .setFilterByAuthorizedAccounts(false)
                .build()
        )
        .build()


    //Write comment to Firestore
    suspend fun uploadComment(review: RecipeNameAndReview, authorId: String){
        println("try upload comment")
        val newComment = hashMapOf(
            "recipeName" to review.recipeName,
            "reviewText" to review.reviewText,
            "authorUid" to authorId,
            "likes" to 0,
            "timestamp" to serverTimestamp()
        )
        db.collection("reviews").add(newComment).await()
    }

    //Write rating to Firestore
    suspend fun updateRating(rating: RecipeNameAndRating, uid: String): String {

        val currentRecipeDocument = db.collection("recipes").document(rating.recipeName)
        var result = ""

        db.runTransaction { transaction ->

            val snapshot = transaction.get(currentRecipeDocument)

            var recipeRating: Double? = snapshot.getDouble("rating")

            if(recipeRating == null){
                result = "Null Rating"
            }
            else{
                recipeRating += rating.rating
                if(recipeRating > 99.0){
                    recipeRating = 99.0
                }
                else if(recipeRating < 60){
                    recipeRating = 60.0
                }
                val timestamp: Any = serverTimestamp()
                transaction.update(currentRecipeDocument, "rating", recipeRating)
                transaction.update(currentRecipeDocument, "timestamp", timestamp)
            }

        }.addOnSuccessListener {
            result = "Success"
        }.addOnFailureListener { e ->
            result = "Failed with $e"
        }.await()

        println("update rating result: $result")
        return result

    }

    //upload local user likes to firestore
    suspend fun updateLike(likeId: String): String {

        val reviewRef = db.collection("reviews").document(likeId)
        val usersRef = db.collection("users")
        var result = ""

        db.runTransaction { transaction ->

            val snapshotReview = transaction.get(reviewRef)
            val authorId = snapshotReview.getString("authorUid") ?: ""
            val snapshotAuthor = transaction.get(usersRef.document(authorId))
            val authorRef = usersRef.document(authorId)

            var commentLikes: Double? = snapshotReview.getDouble("likes")

            var authorKarma: Double? = snapshotAuthor.getDouble("karma")

            if(commentLikes == null){
                result = "Null Likes"
            }

            else{
                commentLikes += 1
                transaction.update(reviewRef, "likes", commentLikes)

                val timestamp: Any = serverTimestamp()
                transaction.update(reviewRef, "timestamp", timestamp)
                if(authorKarma != null){
                    authorKarma += 1
                    transaction.update(authorRef, "karma", authorKarma)
                }
            }

        }.addOnSuccessListener {
            result = "Success"
        }.addOnFailureListener { e ->
            result = "Failed with $e"
        }.await()

        println("update rating result: $result")
        return result

    }




    ////Download Stuff////

    //get all comments (room has no timestamp)
    suspend fun getComments(): MutableList<CommentsEntity>{

        val reviewsCollection = db.collection("reviews")
        val querySnapshot = reviewsCollection.get().await()

        val resultCommentsList: MutableList<CommentsEntity> = mutableListOf()

        if (querySnapshot != null) {
            for (document in querySnapshot.documents) {
                val commentId = document.id
                val recipeName = document.getString("recipeName")
                val reviewText = document.getString("reviewText")
                val authorUid = document.getString("authorUid")
                val likes = document.getDouble("likes")?.toInt()
                val timestamp = document.getTimestamp("timestamp")

                val comment = CommentsEntity(
                    commentID = commentId,
                    recipeName  = recipeName ?: "",
                    authorID = authorUid ?: "",
                    commentText = reviewText ?: "",
                    likes = likes ?: 0,
                    likedByMe = 0,
                    myLikeWasSynced = 0,
                    timestamp = timestamp?.toString() ?: ""
                )

                resultCommentsList.add(comment)

            }
        }
        return resultCommentsList

    }

    //get comments since timestamp
    suspend fun getComments(sinceTimestamp: String): MutableList<CommentsEntity>{

        val reviewsCollection = db.collection("reviews")
        val sTimestamp = Timestamp.valueOf(sinceTimestamp)
        val query = reviewsCollection.whereGreaterThan("timestamp", sTimestamp)
        val querySnapshot = query.get().await()

        val resultCommentsList: MutableList<CommentsEntity> = mutableListOf()

        if (querySnapshot != null) {
            for (document in querySnapshot.documents) {
                val commentId = document.id
                val recipeName = document.getString("recipeName")
                val reviewText = document.getString("reviewText")
                val authorUid = document.getString("authorUid")
                val likes = document.getDouble("likes")?.toInt()
                val timestamp = document.getTimestamp("timestamp")

                val comment = CommentsEntity(
                    commentID = commentId,
                    recipeName  = recipeName ?: "",
                    authorID = authorUid ?: "",
                    commentText = reviewText ?: "",
                    likes = likes ?: 0,
                    likedByMe = 0,
                    myLikeWasSynced = 0,
                    timestamp = timestamp?.toString() ?: ""
                )

                resultCommentsList.add(comment)

            }
        }
        return resultCommentsList

    }

    //get recipe ratings
    suspend fun getRecipeRatings(sinceTimestamp: String): MutableList<RecipeNameAndRating> {

        val recipesCollection = db.collection("recipes")
        val sTimestamp = Timestamp.valueOf(sinceTimestamp)
        val query = recipesCollection.whereGreaterThan("timestamp", sTimestamp)
        val querySnapshot = query.get().await()

        val resultRecipeList: MutableList<RecipeNameAndRating> = mutableListOf()

        if (querySnapshot != null) {
            for (document in querySnapshot.documents) {
                val recipeName = document.getString("recipeName")
                val rating = document.getDouble("rating")?.toInt()

                val comment = RecipeNameAndRating(recipeName ?: "", rating ?: 99)
                resultRecipeList.add(comment)

            }
        }

        return resultRecipeList

    }

    suspend fun getAuthorData(comment: CommentsEntity): AuthorData {

        val authorDoc = db.collection("users").document(comment.authorID).get().await()

        var authorData: AuthorData = AuthorData("","")

        if(authorDoc != null){

            val userName = authorDoc.getString("display_name")
            val userPhotoURL = authorDoc.getString("user_photo")

            authorData = AuthorData(userName ?: "", userPhotoURL ?: "")

        }

        return authorData

    }


    //get UID
    fun getUid(): String{
        return auth.currentUser?.uid ?: ""
    }

    fun currentUser(): FirebaseUser? {
        return auth.currentUser
    }

    suspend fun getUserData(uid: String): AuthorData{
        val userDoc = db.collection("users").document(uid).get().await()
        var userData: AuthorData = AuthorData("", "")

        if(userDoc != null){
            val userName = userDoc.getString("display_name")
            val userPhotoURL = userDoc.getString("user_photo")

            userData = AuthorData(userName ?: "", userPhotoURL ?: "")
        }

        return userData



    }




    ////Sign in stuff below////

    suspend fun googleOneTapSignInOrUp(): FirebaseResult {
        println("before try")
        return try {
            println("try in")
            val signInResult = oneTapClient.beginSignIn(signInRequest).await()
            FirebaseResult("Success", signInResult, null)
        } catch (e: Exception) {
            try {
                println("try up")
                val signUpResult = oneTapClient.beginSignIn(signUpRequest).await()
                FirebaseResult("Success", signUpResult, null)
            } catch (e: Exception) {
                println("error $e ???")
                FirebaseResult("Failed", null, e)
            }
        }
    }

    suspend fun googleOneTapSignIn(): FirebaseResult {
        println("before sign IN try")
        return try {
            println("try sign IN")
            val signInResult = oneTapClient.beginSignIn(signInRequest).await()
            FirebaseResult("Success", signInResult, null)
        } catch (e: Exception) {
            println("failed sign IN error: $e")
            FirebaseResult("Failed", null, e)

        }

    }

    suspend fun firebaseSignInWithGoogle(googleCredential: AuthCredential): String {
        println("in repo fire try")
        return try {
            println("in try of repo fire try")
            val authResult = auth.signInWithCredential(googleCredential).await()
            val isNewUser = authResult.additionalUserInfo?.isNewUser ?: false
            println("is new user? $isNewUser")
            if (isNewUser) {
                println("in is new")
                addUserToFirestore()
                "NewUserSuccess"
            }else{
                println("succeed!!!!")
                "ReturningUserSuccess"
            }
        } catch (e: Exception) {
            println("failed but SO CLOSE")
            "Failed: $e"
        }
    }

    private suspend fun addUserToFirestore() {
        println("in add new")
        auth.currentUser?.apply {
            val user = toUser(this.displayName, this.email)
            db.collection("users").document(uid).set(user).await()
        }
    }

    private fun FirebaseUser.toUser(displayName: String?, email: String?) = mapOf(
        "display_name" to displayName,
        "email" to email,
        "user_photo" to photoUrl?.toString(),
        "timestamp" to serverTimestamp()
    )

}