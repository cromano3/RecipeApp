package com.example.bearrecipebookapp.data.repository

import com.example.bearrecipebookapp.data.dao.SettingsScreenDao

class SettingsScreenRepository(private val settingsScreenDao: SettingsScreenDao) {

    fun setLocalUserAsNew(){
        settingsScreenDao.setLocalUserAsNew()
    }
}