package com.christopherromano.culinarycompanion.data.repository

import android.app.Application
import com.christopherromano.culinarycompanion.datamodel.AuthorDataWithComment
import com.christopherromano.culinarycompanion.datamodel.FirebaseResult
import com.christopherromano.culinarycompanion.datamodel.RecipeNameAndRating
import com.christopherromano.culinarycompanion.datamodel.RecipeNameAndReview
import com.google.android.gms.auth.api.identity.BeginSignInRequest
import com.google.android.gms.auth.api.identity.Identity
import com.google.android.gms.auth.api.identity.SignInClient
import com.google.firebase.auth.AuthCredential
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.firestore.FieldValue
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
                .setServerClientId("308295296237-aseqd0788lofjf8i5gqn52mi36vkvns9.apps.googleusercontent.com")
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
                .setServerClientId("308295296237-aseqd0788lofjf8i5gqn52mi36vkvns9.apps.googleusercontent.com")
                // Only show accounts previously used to sign in.
                .setFilterByAuthorizedAccounts(false)
                .build()
        )
        .build()


    //Delete comment from Firestore
    suspend fun deleteComment(commentID: String): String{
        println("try Soft Delete comment")

        var result = "Failed"

        db.collection("reviews").document(commentID).update("isDeleted", 1)
            .addOnSuccessListener {
                println("Comment successfully hidden")
                result = "Success"
            }
            .addOnFailureListener { e ->
                println("Error hiding document $e")
                result = "Failed"
            }.await()


        return result
    }

    //Write comment to Firestore
    suspend fun uploadComment(review: RecipeNameAndReview): String{
        println("try upload comment")

        var result = "Failed"

        try {

            val commentsCollection = db.collection("reviews")

            val email = auth.currentUser?.email

            if(email != null){

                val querySnapshot = commentsCollection.whereEqualTo("authorEmail", email)
                    .whereEqualTo("recipeName", review.recipeName).limit(1).get().await()
                if (querySnapshot.isEmpty) {
                    val newComment = hashMapOf(
                        "recipeName" to review.recipeName,
                        "reviewText" to review.reviewText,
                        "authorEmail" to email,
                        "likes" to 0,
                        "likedBy" to arrayListOf<String>(email),
                        "dislikedBy" to arrayListOf<String>(),
                        "timestamp" to serverTimestamp(),
                        "isModApproved" to 1,
                        "isDeleted" to 0,
                    )
                    db.collection("reviews").add(newComment)
                        .addOnSuccessListener { result = "Success" }
                        .addOnFailureListener { e -> result = "Failed with $e" }.await()
                } else {
                    result = "User Comment Already Exists"
                }

            }
            else{
                result = "UID is null"
            }
        }
        catch(e: Exception){
            println("Failed to upload comment with: $e")
        }

        println("tried to add comment and result is: $result")
        return result




    }


    //Write rating to Firestore
    suspend fun updateRating(rating: RecipeNameAndRating): String {


        var result = ""

        try {
            val currentRecipeDocument = db.collection("recipes").document(rating.recipeName)

            db.runTransaction { transaction ->

                val snapshot = transaction.get(currentRecipeDocument)

                val likedByList = snapshot.get("ratedBy") as? List<String> ?: listOf()

                var recipeRating: Double? = snapshot.getDouble("rating")

                if (recipeRating == null) {
                    result = "Null Rating"
                } else if (auth.currentUser?.uid == null) {
                    result = "Null Liker"
                } else if (likedByList.contains(auth.currentUser?.email)) {
                    result = "Failed Duplicate Rating"
                } else {
                    recipeRating += rating.rating
//                if(recipeRating > 99.0){
//                    recipeRating = 99.0
//                }
//                else if(recipeRating < 60){
//                    recipeRating = 60.0
//                }
//                val timestamp: Any = serverTimestamp()
                    transaction.update(
                        currentRecipeDocument,
                        "ratedBy",
                        FieldValue.arrayUnion(auth.currentUser?.email)
                    )
                    transaction.update(currentRecipeDocument, "rating", recipeRating)
//                transaction.update(currentRecipeDocument, "timestamp", timestamp)
                }

            }.addOnSuccessListener {
                result = "Success"
            }.addOnFailureListener { e ->
                result = "Failed with $e"
            }.await()
        }
        catch (e: Exception){
            println("Failed update rating with: $e")
            result = "Failed with $e"
        }

        println("update rating result: $result")
        return result

    }

    //upload local user likes to firestore
    suspend fun updateLike(likeId: String): String {

        var result = "Success"

        try {
            val reviewRef = db.collection("reviews").document(likeId)




            var authorEmail = ""

            var duplicate = ""

            db.runTransaction { transaction ->

                val snapshotReview = transaction.get(reviewRef)

                var commentLikes: Double? = snapshotReview.getDouble("likes")

                authorEmail = snapshotReview.getString("authorEmail") ?: ""

                var likedByList = snapshotReview.get("likedBy") as? List<String> ?: listOf()


                if (commentLikes == null) {
                    result = "Null Likes"
                } else if (auth.currentUser?.email == null) {
                    result = "Null Liker"
                } else if (likedByList.contains(auth.currentUser?.email)) {
                    result = "Failed Duplicate Like"
                } else {
                    commentLikes += 1
                    transaction.update(reviewRef, "likes", commentLikes)
                    transaction.update(
                        reviewRef,
                        "likedBy",
                        FieldValue.arrayUnion(auth.currentUser?.email)
                    )
                }

            }.addOnSuccessListener {

            }.addOnFailureListener { e ->
                result = "Failed with $e"
            }.await()


            if (result == "Success") {

                val query =
                    db.collection("users").whereEqualTo("email", authorEmail).limit(1).get().await()
                val authorDocSnapshot = query.documents[0]

                if (authorDocSnapshot != null) {


                    db.runTransaction { transaction ->

                        val snapshotAuthor = transaction.get(authorDocSnapshot.reference)

                        //you cant get karma from liking your own comments
                        if(auth.currentUser?.email != snapshotAuthor.getString("email")) {

                            var authorKarma: Double? = snapshotAuthor.getDouble("karma")

                            if (authorKarma != null) {
                                authorKarma += 1
                                transaction.update(
                                    authorDocSnapshot.reference,
                                    "karma",
                                    authorKarma
                                )
                            }
                        }

                    }.addOnSuccessListener {
                        result = "Success"
                    }.addOnFailureListener {
                        result = "Failed to update author karma"
                    }.await()
                }

            }
        }
        catch(e: Exception){
            println("failed to update comment with: $e")
        }

        println("update rating result: $result")
        return result

    }


    //upload local user likes to firestore
    suspend fun updateDislike(commentID: String): String {

        var result = "Success"

        try {
            val reviewRef = db.collection("reviews").document(commentID)


            var authorEmail = ""

            var duplicate = ""

            db.runTransaction { transaction ->

                val snapshotReview = transaction.get(reviewRef)

                var commentLikes: Double? = snapshotReview.getDouble("likes")

                authorEmail = snapshotReview.getString("authorEmail") ?: ""

                var dislikedByList = snapshotReview.get("dislikedBy") as? List<String> ?: listOf()


                if (commentLikes == null) {
                    result = "Null Likes"
                } else if (auth.currentUser?.email == null) {
                    result = "Null Liker"
                } else if (dislikedByList.contains(auth.currentUser?.email)) {
                    result = "Failed Duplicate Like"
                } else {
                    commentLikes -= 1
                    transaction.update(reviewRef, "likes", commentLikes)
                    transaction.update(
                        reviewRef,
                        "dislikedBy",
                        FieldValue.arrayUnion(auth.currentUser?.email)
                    )
                }

            }.addOnSuccessListener {

            }.addOnFailureListener { e ->
                result = "Failed with $e"
            }.await()


            if (result == "Success") {

                val query =
                    db.collection("users").whereEqualTo("email", authorEmail).limit(1).get().await()
                val authorDocSnapshot = query.documents[0]

                if (authorDocSnapshot != null) {


                    db.runTransaction { transaction ->

                        val snapshotAuthor = transaction.get(authorDocSnapshot.reference)

                        //you cant get karma from liking your own comments
                        if(auth.currentUser?.email != snapshotAuthor.getString("email")) {

                            var authorKarma: Double? = snapshotAuthor.getDouble("karma")

                            if (authorKarma != null) {
                                authorKarma -= 1
                                transaction.update(
                                    authorDocSnapshot.reference,
                                    "karma",
                                    authorKarma
                                )
                            }
                        }

                    }.addOnSuccessListener {
                        result = "Success"
                    }.addOnFailureListener {
                        result = "Failed to update author karma"
                    }.await()
                }

            }
        }
        catch(e: Exception){
            println("failed to update comment with: $e")
        }

        println("finished add new dislike to comment and result is: $result")
        return result

    }

    suspend fun submitReport(authorDataWithComment: AuthorDataWithComment) {
        println("try upload report")

        try {

            val email = auth.currentUser?.email

            if(email != null){
                    val newReport = hashMapOf(
                        "commentAuthor" to authorDataWithComment.comment.authorID,
                        "commentText" to authorDataWithComment.comment.commentText,
                        "commentID" to authorDataWithComment.comment.commentID,
                        "reporterID" to email,
                    )
                    db.collection("reports").add(newReport)
                        .addOnSuccessListener { println("Report successfully uploaded") }
                        .addOnFailureListener { e -> println("Failed to upload report with error: $e") }.await()
            }
            else{
               println("Failed to upload report because UID is null")
            }
        }
        catch(e: Exception){
            println("Failed to upload comment with: $e")
        }

        println("end of try to upload report")

    }



    //get UID
    fun getUid(): String{
        return auth.currentUser?.uid ?: ""
    }

    fun currentUser(): FirebaseUser? {
        return auth.currentUser
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

                val email = authResult.user?.email

                if (email != null) {
                    val userDoc = db.collection("users").whereEqualTo("email", email).get().await().documents.firstOrNull()
                    if (userDoc != null) {
                        // User already exists in Firestore, update their document ID
                        "ReturningUser"
                    }
                    else{
                        //they are brand new user
                        addUserToFirestore()
                        "NewUserSuccess"
                    }
                }
                else{
                    "Failed null email"
                }

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
            val backupData = hashMapOf(
                "email" to this.email,
                "uid" to this.uid,
                "name" to this.displayName,
            )
            db.collection("backup").add(backupData).await()
        }
    }

    private fun FirebaseUser.toUser(displayName: String?, email: String?) = mapOf(
        "name" to displayName,
        "display_name" to displayName,
        "email" to email,
        "user_photo" to photoUrl?.toString(),
        "timestamp" to serverTimestamp(),
        "karma" to 0,
    )

}
