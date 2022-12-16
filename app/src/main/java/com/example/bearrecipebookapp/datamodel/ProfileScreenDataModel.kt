package com.example.bearrecipebookapp.datamodel

data class ProfileScreenDataModel(
    val activeTab: String = "favorites",
    val previousTab: String = "",
    val doAnimation: Boolean = false,
    val resetAnimation: Boolean = false,
    val animationsPlayed: Int = 0,
    val totalAnimationsToPlay: Int = 0,
    val level: Int = 0,
    val title: String = "",
    val animationStartValue: Float = 0f,
    val animationTargetFirst: Float = 0f,
    val animationTargetSecond: Float = 0f,
    val xp: Int = 0,
    val xpToGive: Int = 0,
)
