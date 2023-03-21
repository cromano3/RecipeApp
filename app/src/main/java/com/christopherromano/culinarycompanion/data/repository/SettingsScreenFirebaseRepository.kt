package com.christopherromano.culinarycompanion.data.repository

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

            val user = auth.currentUser
            val userEmail = user?.email

            var deleteAccountResult = "Failed"


        try {
            if (user != null) {


                val collectionRef = db.collection("reviews")
                val query = collectionRef.whereEqualTo("authorEmail", userEmail)

                val querySnapshot = query.get().await()

                if (querySnapshot != null) {

                    for (document in querySnapshot.documents) {
                        document.reference.update("isDeleted", 1)
                            .addOnSuccessListener {
                                deleteAccountResult = "Success"
                                println("Review successfully deleted")

                            }
                            .addOnFailureListener { e ->
                                deleteAccountResult = "Failed"
                                println("Error deleting Review: $e")

                            }.await()
                    }

                    val userData = hashMapOf("email" to userEmail)

                    db.collection("deletedUsers").document(user.uid).set(userData)
                        .addOnSuccessListener { deleteAccountResult = "Success" }
                        .addOnFailureListener { e ->
                            deleteAccountResult = "Failed with $e"
                            println("failed to delete account (add to deleted users list) with $e")
                        }.await()

                }

            }
        }
        catch (error: Exception){
            println("delete account try/catch error: $error")
        }

        return deleteAccountResult

    }

}