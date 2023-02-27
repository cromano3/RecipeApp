package com.example.bearrecipebookapp.data.repository

import android.app.Application
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FieldPath
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.tasks.await

class SettingsScreenFirebaseRepository(
    application: Application,
    private val db: FirebaseFirestore,
    private val auth: FirebaseAuth,
) {

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




}