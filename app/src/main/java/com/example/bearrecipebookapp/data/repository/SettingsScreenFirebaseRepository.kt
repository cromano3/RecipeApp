package com.example.bearrecipebookapp.data.repository

import android.app.Application
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FieldPath
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers.IO
import kotlinx.coroutines.tasks.await

class SettingsScreenFirebaseRepository(
    application: Application,
    private val db: FirebaseFirestore,
    private val auth: FirebaseAuth,
) {

    private val _authState = MutableLiveData<Int>()
    val authState: LiveData<Int>
        get() = _authState



    private val coroutineScope = CoroutineScope(IO)


    init {
        auth.addAuthStateListener { auth ->
            if (auth.currentUser != null) {
                _authState.value = 1
            } else {
                _authState.value = 0
            }
        }
    }

    suspend fun getCurrentDisplayName(): String{
        try {
            val reviewsCollection = db.collection("users")
            val query =
                reviewsCollection.whereEqualTo(FieldPath.documentId(), auth.currentUser?.uid)
            val querySnapshot = query.get().await()

            return if (querySnapshot != null) {
                querySnapshot.documents[0].getString("display_name") ?: ""
            } else {
                ""
            }
        }
        catch(e: Exception){
            println("Failed to get current display name with: $e")
            return ""
        }



    }

    fun updateDisplayName(newDisplayName: String) {

        db.collection("users").document(auth.currentUser?.uid ?: "").update("display_name", newDisplayName)
            .addOnSuccessListener { println("successfully updated display name")  }
            .addOnFailureListener{ println("failed to update display name") }

    }

    suspend fun deleteAccount(): String {

//        coroutineScope.launch(Dispatchers.IO) {


            val user = auth.currentUser
            val userEmail = user?.email

            var reauthResult = "Failed"

            var deleteAccountResult = "Failed"

            var idToken: String? = null

            if (userEmail != null) {

                if (user != null) {


//                    val r  = user.getIdToken(true).await()
//
//                    if (r != null) {
//                        val credential = GoogleAuthProvider.getCredential(r.token, null)
//
//
//                        user.reauthenticate(credential)
//                            .addOnSuccessListener {
//                                // User has been re-authenticated
//                                reauthResult = "Success"
//
//                            }
//                            .addOnFailureListener { exception ->
//                                // Re-authentication failed
//                                println("failed to RE-AUTH with $exception")
//                            }.await()
//                    }


//                    if (reauthResult == "Success") {





                    val collectionRef = db.collection("reviews")
                    val query = collectionRef.whereEqualTo("authorEmail", userEmail)

                    val querySnapshot = query.get().await()

                    if (querySnapshot != null) {
                        println("Documents to delete is not null")
                        println("Documents to delete size: ${querySnapshot.documents.size}")
                        println("user email is: $userEmail")

                        for (document in querySnapshot.documents) {
                            document.reference.delete()
                                .addOnSuccessListener {
                                    println("Review successfully deleted")

                                }
                                .addOnFailureListener { e ->
                                    println("Error deleting Review: $e")

                                }.await()
                        }
                    }

                    try{
                        val usersRef = db.collection("users")
                        val queryUser = usersRef.whereEqualTo("email", userEmail)
                        val userSnapshot = queryUser.get().await()

                        if (!userSnapshot.isEmpty && userSnapshot != null) {

                            for(document in userSnapshot.documents){
                                document.reference.delete()
                            }

                        }
                    }
                    catch (e: Exception){
                        println("Failed to delete user document with: $e")
                    }


                    try{
                        user.delete()
                            .addOnSuccessListener {
                                // User account has been deleted
                                println("account was deleted Successfully")
                                deleteAccountResult = "Success"
                            }
                            .addOnFailureListener { exception ->
                                // Failed to delete user account
                                deleteAccountResult = exception.message ?: ""
                                println("Could not delete account: $exception")
                            }.await()
                    }
                    catch(e: Exception){
                        deleteAccountResult = e.message ?: ""
                    }


//                    }

                }
            }
            return deleteAccountResult
//        }
    }




}