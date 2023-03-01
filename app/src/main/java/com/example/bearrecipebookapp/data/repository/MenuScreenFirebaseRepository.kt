package com.example.bearrecipebookapp.data.repository

import android.app.Application
import com.example.bearrecipebookapp.datamodel.RecipeNamesWithRatings
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.tasks.await


class MenuScreenFirebaseRepository(
    application: Application,
    private val db: FirebaseFirestore,
    private val auth: FirebaseAuth,
) {

    suspend fun getGlobalRatings(recipeNames: List<String>): MutableList<RecipeNamesWithRatings> {


        val results: MutableList<RecipeNamesWithRatings> = mutableListOf()

        try {
            val query = db.collection("recipes").whereIn("recipeName", recipeNames).get().await()

            for (document in query.documents) {

                val thisName = document.getString("recipeName") ?: ""
                val thisRating = document.getLong("rating")?.toInt() ?: 0

                results.add(RecipeNamesWithRatings(thisName, thisRating))
            }
        }
        catch(e: Exception){
            println("Failed to get global ratings with $e")
        }

        return results
    }

    fun currentUser(): FirebaseUser? {
        return auth.currentUser
    }
}