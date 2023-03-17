package com.christopherromano.culinarycompanion.data.repository

import com.christopherromano.culinarycompanion.data.dao.SettingsScreenDao

class SettingsScreenRepository(private val settingsScreenDao: SettingsScreenDao) {

    fun setLocalUserAsNew(){
        settingsScreenDao.setLocalUserAsNew()
    }
}