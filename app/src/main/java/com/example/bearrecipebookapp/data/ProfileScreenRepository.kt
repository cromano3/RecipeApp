package com.example.bearrecipebookapp.data

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers

class ProfileScreenRepository(private val profileScreenDao: ProfileScreenDao) {

    private val coroutineScope = CoroutineScope(Dispatchers.IO)

}