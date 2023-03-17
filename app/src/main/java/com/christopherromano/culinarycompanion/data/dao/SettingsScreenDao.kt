package com.christopherromano.culinarycompanion.data.dao

import androidx.room.Dao
import androidx.room.Query
import androidx.room.Transaction

@Dao
interface SettingsScreenDao {

    @Transaction
    @Query("UPDATE user_table SET is_online_user_type = -2")
    fun setLocalUserAsNew()

}