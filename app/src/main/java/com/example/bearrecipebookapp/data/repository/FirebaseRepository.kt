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
        val newComment = hashMapOf(
            "recipeName" to review.recipeName,
            "reviewText" to review.reviewText,
            "authorUid" to authorId,
            "likes" to 0)
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
            }

            transaction.update(currentRecipeDocument, "rating", recipeRating)


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

        val currentReviewDocument = db.collection("reviews").document(likeId)
        var result = ""

        db.runTransaction { transaction ->

            val snapshot = transaction.get(currentReviewDocument)

            var commentLikes: Double? = snapshot.getDouble("likes")

            if(commentLikes == null){
                result = "Null Likes"
            }
            else{
                commentLikes += 1
            }

            transaction.update(currentReviewDocument, "likes", commentLikes)


        }.addOnSuccessListener {
            result = "Success"
        }.addOnFailureListener { e ->
            result = "Failed with $e"
        }.await()

        println("update rating result: $result")
        return result

    }




    ////Download Stuff////

    //get comments
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

                val comment = CommentsEntity(commentId, recipeName ?: "", reviewText ?: "", authorUid ?: "", likes ?: 0)
                resultCommentsList.add(comment)

            }
        }
        return resultCommentsList

    }

    //get recipe ratings
    suspend fun getRecipeRatings(): MutableList<RecipeNameAndRating> {

        val recipesCollection = db.collection("recipes")
        val querySnapshot = recipesCollection.get().await()

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
                println("error")
                FirebaseResult("Failed", null, e)
            }
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
            }
            println("succeed!!!!")
            "Success"
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