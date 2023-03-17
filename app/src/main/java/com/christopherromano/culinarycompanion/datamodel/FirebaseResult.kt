package com.christopherromano.culinarycompanion.datamodel

import com.google.android.gms.auth.api.identity.BeginSignInResult

data class FirebaseResult(var result: String = "",  var signInResult: BeginSignInResult?, var error: Exception?)
