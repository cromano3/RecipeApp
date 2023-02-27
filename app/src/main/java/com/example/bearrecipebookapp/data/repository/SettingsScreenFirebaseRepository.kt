package com.example.bearrecipebookapp.data.repository

import android.app.Application
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.GoogleAuthProvider
import com.google.firebase.firestore.FieldPath
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Dispatchers.IO
import kotlinx.coroutines.launch
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
        val reviewsCollection = db.collection("users")
        val query = reviewsCollection.whereEqualTo(FieldPath.documentId(), auth.currentUser?.uid)
        val querySnapshot = query.get().await()

        return if(querySnapshot != null){
            querySnapshot.documents[0].getString("display_name") ?: ""
        } else{
            ""
        }


    }

    fun updateDisplayName(newDisplayName: String) {

        db.collection("users").document(auth.currentUser?.uid ?: "").update("display_name", newDisplayName)
            .addOnSuccessListener { println("successfully updated display name")  }
            .addOnFailureListener{ println("failed to update display name") }

    }

    fun deleteAccount() {

        coroutineScope.launch(Dispatchers.IO) {


            val user = auth.currentUser
            val userEmail = user?.email

            var reauthResult = "Failed"

            if (userEmail != null) {
                val credential = GoogleAuthProvider.getCredential(userEmail, null)

                if (user != null) {
                    user.reauthenticate(credential)
                        .addOnSuccessListener {
                            // User has been re-authenticated
                            reauthResult = "Success"

                        }
                        .addOnFailureListener { exception ->
                            // Re-authentication failed
                            println("failed to RE-AUTH with $exception")
                        }.await()
                }

                if(reauthResult == "Success"){
                    val collectionRef = db.collection("reviews")
                    val query = collectionRef.whereEqualTo("authorUid", user.uid)

                    val querySnapshot = query.get().await()

                    if(querySnapshot != null) {
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

                    user.delete()
                        .addOnSuccessListener {
                            // User account has been deleted
                            println("account was deleted Successfully")
                        }
                        .addOnFailureListener { exception ->
                            // Failed to delete user account
                            println("Could not delete account: $exception")
                        }.await()

                }

            }
        }
    }




}