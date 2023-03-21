package com.christopherromano.culinarycompanion.data.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "recipe_table")
data class RecipeEntity(
    @PrimaryKey(autoGenerate = false)
    @ColumnInfo(name = "recipe_name")
    val recipeName: String = "",

    @ColumnInfo(name = "on_menu")
    val onMenu: Int = 0,

    @ColumnInfo(name = "is_details_screen_target")
    val isDetailsScreenTarget: Int = 0,

    @ColumnInfo(name = "time_to_make")
    val timeToMake: Int = 0,

    @ColumnInfo(name = "difficulty")
    val difficulty: Int = 0,

    @ColumnInfo(name = "global_rating")
    val globalRating: Int = 0,

    @ColumnInfo(name = "is_shown")
    var isShown: Int = 1,

    @ColumnInfo(name = "is_shopping_filter")
    var isShoppingFilter: Int = 0,

    @ColumnInfo(name = "is_favorite")
    val isFavorite: Int = 0,

    @ColumnInfo(name = "is_search_result")
    var isSearchResult: Int = 0,

    @ColumnInfo(name = "cooked_count")
    var cookedCount: Int = 0,

    @ColumnInfo(name = "is_rated")
    var isRated: Int = 0,

    @ColumnInfo(name = "local_user_rating")
    var userRating: Int = 0,

    @ColumnInfo(name = "is_rating_synced")
    var isRatingSynced: Int = 0,

    @ColumnInfo(name = "is_reviewed")
    var isReviewed: Int = 0,

    @ColumnInfo(name = "review")
    var reviewText: String = "",

    @ColumnInfo(name = "is_review_synced")
    var isReviewSynced: Int = 0,

    @ColumnInfo(name = "is_review_screen_target")
    var isReviewScreenTarget: Int = 0,

//    @ColumnInfo(name = "when_user_last_synced_rating")
//    var lastRatingSyncTime: String = "",

    @ColumnInfo(name = "time_of_users_last_sync")
    var lastCommentSyncTime: String = "",

    @ColumnInfo(name = "timestamp_of_latest_received_comment")
    var lastDownloadedCommentTimestamp: String = "",


//
//    @ColumnInfo(name = "ingredients")
//    val ingredients: String,



//    @ColumnInfo(name = "images")
//    val images: Int

)