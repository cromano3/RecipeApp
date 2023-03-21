package com.christopherromano.culinarycompanion.data.repository

import android.app.Application
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
        println("try Delete comment")

        var result = "Failed"

        db.collection("reviews").document(commentID).delete()
            .addOnSuccessListener {
                println("DocumentSnapshot successfully deleted!")
                result = "Success"
            }
            .addOnFailureListener { e ->
                println("Error deleting document $e")
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
                        "isModApproved" to 0,
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



    ////Download Stuff////

    //get all comments (room has no timestamp)
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

    //get comments since timestamp
//    suspend fun getComments(recipeName: String, sinceTimestamp: Date): MutableList<CommentsEntity>{
//
//        val reviewsCollection = db.collection("reviews")
//        val sTimestamp = com.google.firebase.Timestamp(sinceTimestamp)
//        val query = reviewsCollection.whereGreaterThan("timestamp", sTimestamp).whereEqualTo("recipeName", recipeName)
//        val querySnapshot = query.get().await()
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
//
//                val comment = CommentsEntity(
//                    commentID = commentId,
//                    recipeName  = thisRecipeName ?: "",
//                    authorID = authorUid ?: "",
//                    commentText = reviewText ?: "",
//                    likes = likes ?: 0,
//                    likedByMe = 0,
//                    myLikeWasSynced = 0,
//                    timestamp = timestamp?.toString() ?: ""
//                )
//
//                resultCommentsList.add(comment)
//
//            }
//        }
//        return resultCommentsList
//
//    }

//    fun getMostRecentCommentTimestamp(newCommentsList: MutableList<CommentsEntity>): String {
//        try {
//
//            val formatter = SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.US)
//            val date = formatter.parse(newCommentsList[0].timestamp)
//            var newestTime = Timestamp(date!!)
//
//            for(comment in newCommentsList) {
//
//                val formattedDate: String = comment.timestamp
//                val thisDate = formatter.parse(formattedDate)
//                val timestamp = Timestamp(thisDate!!)
//
//                if(timestamp > newestTime){
//                    newestTime = timestamp
//                }
//            }
//
//            val finalDate = newestTime.toDate()
//            return formatter.format(finalDate)
//
//        }
//        catch(e: Exception){
//            return "Failed ${e.message}"
//        }
//
//
//
//    }

//    //get recipe ratings
//    suspend fun getRecipeRatings(sinceTimestamp: String): MutableList<RecipeNameAndRating> {
//
//        val recipesCollection = db.collection("recipes")
//        val sTimestamp = Timestamp.valueOf(sinceTimestamp)
//        val query = recipesCollection.whereGreaterThan("timestamp", sTimestamp)
//        val querySnapshot = query.get().await()
//
//        val resultRecipeList: MutableList<RecipeNameAndRating> = mutableListOf()
//
//        if (querySnapshot != null) {
//            for (document in querySnapshot.documents) {
//                val recipeName = document.getString("recipeName")
//                val rating = document.getDouble("rating")?.toInt()
//
//                val comment = RecipeNameAndRating(recipeName ?: "", rating ?: 99)
//                resultRecipeList.add(comment)
//
//            }
//        }
//
//        return resultRecipeList
//
//    }

    //get recipe ratings
//    suspend fun getRecipeRating(recipeName: String): Int {
//
//        val recipesCollection = db.collection("recipes")
//        val query = recipesCollection.whereEqualTo(FieldPath.documentId(), recipeName)
//        val querySnapshot = query.limit(1).get().await()
//
//        var rating: Long? = -1
//
//        if (querySnapshot != null) {
//            rating = querySnapshot.documents[0].getLong("rating")
//        }
//
//        return rating?.toInt() ?: -1
//
//    }

//    suspend fun getAuthorsData(commentsList: MutableList<CommentsEntity>): MutableList<AuthorDataWithComment> {
//
//        val reviewsCollection = db.collection("users")
//        val querySnapshot = reviewsCollection.get().await()
//
//        val resultAuthorDataWithComment: MutableList<AuthorDataWithComment> = mutableListOf()
//
//        if (querySnapshot != null) {
//            for(comment in commentsList){
//                for (user in querySnapshot.documents) {
//                    if(comment.authorID == user.id) {
//
//                        /** can get karma values here */
//                        val userName = user.getString("display_name")  ?: ""
//                        val userPhotoURL = user.getString("user_photo") ?: ""
//
//
//                        resultAuthorDataWithComment.add(AuthorDataWithComment(AuthorData(userName, userPhotoURL), comment))
//
//                        break
//                    }
//                }
//            }
//        }
//
//        return resultAuthorDataWithComment
//
//    }

//    suspend fun getAuthorData(commentsList: MutableList<CommentsEntity>): AuthorData {
//
//        val authorDoc = db.collection("users").document(comment.authorID).get().await()
//
//        var authorData: AuthorData = AuthorData("","")
//
//        if(authorDoc != null){
//
//            val userName = authorDoc.getString("display_name")
//            val userPhotoURL = authorDoc.getString("user_photo")
//
//            authorData = AuthorData(userName ?: "", userPhotoURL ?: "")
//
//        }
//
//        return authorData
//
//    }


    //get UID
    fun getUid(): String{
        return auth.currentUser?.uid ?: ""
    }

    fun currentUser(): FirebaseUser? {
        return auth.currentUser
    }

//    suspend fun getUserData(uid: String): AuthorData{
//        val userDoc = db.collection("users").document(uid).get().await()
//        var userData: AuthorData = AuthorData("", "")
//
//        if(userDoc != null){
//            val userName = userDoc.getString("display_name")
//            val userPhotoURL = userDoc.getString("user_photo")
//
//            userData = AuthorData(userName ?: "", userPhotoURL ?: "")
//        }
//
//        return userData
//
//
//
//    }




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
