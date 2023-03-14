package com.example.bearrecipebookapp.ui

import android.app.Application
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.graphics.TileMode
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewmodel.compose.LocalViewModelStoreOwner
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.bearrecipebookapp.R
import com.example.bearrecipebookapp.data.repository.SettingsScreenFirebaseRepository
import com.example.bearrecipebookapp.ui.components.BasicAlert
import com.example.bearrecipebookapp.ui.components.CancelAlertButton
import com.example.bearrecipebookapp.ui.components.ConfirmAlertButton
import com.example.bearrecipebookapp.ui.components.OneButtonAlert
import com.example.bearrecipebookapp.ui.theme.Cabin
import com.example.bearrecipebookapp.viewmodel.SettingsScreenViewModel
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import kotlinx.coroutines.delay

@Composable
fun SettingsScreen(
    confirmSignInWithGoogle: () -> Unit,
    confirmReAuthForDeleteAccount: () -> Unit,
    clearReAuthForDeleteSignInResult: () -> Unit,
    reAuthForDeleteSignInResult: Boolean,
){
    val owner = LocalViewModelStoreOwner.current

    owner?.let { viewModelStoreOwner ->
        val settingsScreenViewModel: SettingsScreenViewModel = viewModel(
            viewModelStoreOwner = viewModelStoreOwner,
            key = "settingsScreenViewModel",
            factory = SettingsScreenViewModelFactory(LocalContext.current.applicationContext as Application)
        )

        val authState by settingsScreenViewModel.authState.observeAsState()
        val uiAlertState by settingsScreenViewModel.uiAlertState.collectAsState()

        val focusRequester = remember { FocusRequester() }

        Surface(Modifier.fillMaxSize()){
            Column()
            {
                if (authState == 0) {
                Box(
                    Modifier
                        .fillMaxWidth()
                        .padding(8.dp),
                    contentAlignment = Alignment.Center)
                    {
                        Button(
                            modifier = Modifier
                                .width(250.dp)
                                .padding(start = 8.dp, end = 8.dp),
                            onClick = confirmSignInWithGoogle,
                            elevation = ButtonDefaults.elevation(6.dp),
                            shape = RoundedCornerShape(10.dp),
                            border = BorderStroke(
                                width = 2.dp,
                                brush = (Brush.horizontalGradient(
                                    startX = -30f,
                                    colors = listOf(Color(0xFFb15f33), Color(0xFFb15f33)),
                                    tileMode = TileMode.Mirror
                                )),
                            ),
                            colors = ButtonDefaults.buttonColors(
                                backgroundColor = Color(0xFF682300),
                                contentColor = Color(0xFFd8af84)
                            )
                        ) {
                            Row(
                                modifier = Modifier
                                    .width(250.dp)
                                    .height(25.dp),
                                horizontalArrangement = Arrangement.SpaceEvenly,
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Image(
                                    painter = painterResource(id = R.drawable.ic_google_logo),
                                    contentDescription = null
                                )
                                Text("Sign in with Google")
                            }
                        }
                    }
                    Spacer(
                        Modifier
                            .height(2.dp)
                            .fillMaxWidth()
                            .border(
                                width = 2.dp,
                                brush = (Brush.horizontalGradient(
                                    colors = listOf(
                                        Color(0xFFd8af84),
                                        Color(0xFFb15f33)
                                    ),
                                    tileMode = TileMode.Mirror
                                )),
                                shape = RectangleShape
                            ),
                    )
                }
                if(authState == 1)
                {
                    Surface(
                        Modifier
                            .padding(8.dp)
                            .clickable { settingsScreenViewModel.updateDisplayName() })
                    {
                        Text(
                            text = "Update Display Name",
                            fontSize = 18.sp,
                            fontFamily = Cabin,
                            fontWeight = FontWeight.Bold,
                            color = Color(0xFF682300)
                        )
                    }
                    Spacer(
                        Modifier
                            .height(2.dp)
                            .fillMaxWidth()
                            .border(
                                width = 2.dp,
                                brush = (Brush.horizontalGradient(
                                    colors = listOf(
                                        Color(0xFFd8af84),
                                        Color(0xFFb15f33)
                                    ),
                                    tileMode = TileMode.Mirror
                                )),
                                shape = RectangleShape
                            ),
                    )
                }
                Surface(
                    Modifier
                        .padding(8.dp)
                        .clickable { })
                {
                    Text(
                        text = "Privacy Policy",
                        fontSize = 18.sp,
                        fontFamily = Cabin,
                        fontWeight = FontWeight.Bold,
                        color = Color(0xFF682300)
                    )
                }
                Spacer(
                    Modifier
                        .height(2.dp)
                        .fillMaxWidth()
                        .border(
                            width = 2.dp,
                            brush = (Brush.horizontalGradient(
                                colors = listOf(
                                    Color(0xFFd8af84),
                                    Color(0xFFb15f33)
                                ),
                                tileMode = TileMode.Mirror
                            )),
                            shape = RectangleShape
                        ),
                )
                val context = LocalContext.current
                Surface(
                    Modifier
                        .padding(8.dp)
                        .clickable { settingsScreenViewModel.showLicenses() }
                ){
                    Text(
                        text = "Licenses Info",
                        fontSize = 18.sp,
                        fontFamily = Cabin,
                        fontWeight = FontWeight.Bold,
                        color = Color(0xFF682300)
                    )
                }
                Spacer(
                    Modifier
                        .height(2.dp)
                        .fillMaxWidth()
                        .border(
                            width = 2.dp,
                            brush = (Brush.horizontalGradient(
                                colors = listOf(
                                    Color(0xFFd8af84),
                                    Color(0xFFb15f33)
                                ),
                                tileMode = TileMode.Mirror
                            )),
                            shape = RectangleShape
                        ),
                )
                Surface(
                    Modifier
                        .padding(8.dp)
                        .clickable { })
                {
                    Text(
                        text = "Legal Notice",
                        fontSize = 18.sp,
                        fontFamily = Cabin,
                        fontWeight = FontWeight.Bold,
                        color = Color(0xFF682300)
                    )
                }
                Spacer(
                    Modifier
                        .height(2.dp)
                        .fillMaxWidth()
                        .border(
                            width = 2.dp,
                            brush = (Brush.horizontalGradient(
                                colors = listOf(
                                    Color(0xFFd8af84),
                                    Color(0xFFb15f33)
                                ),
                                tileMode = TileMode.Mirror
                            )),
                            shape = RectangleShape
                        ),
                )
                Surface(
                    Modifier
                        .padding(8.dp)
                        .clickable { settingsScreenViewModel.triggerReAuthAlert() })
                {
                    Text(
                        text = "Delete Account",
                        fontSize = 18.sp,
                        fontFamily = Cabin,
                        fontWeight = FontWeight.Bold,
                        color = Color(0xFF682300)
                    )
                }
            }
            Box(Modifier.fillMaxSize()){
                if(uiAlertState.showLicenses){
                    OneButtonAlert(text = "The following libraries and components are used in this app and are released under the following licenses:\n" +
                            "\n" +
                            "- Android Jetpack libraries, including androidx.core, androidx.compose, androidx.activity, androidx.lifecycle, and androidx.navigation: Apache License, Version 2.0 (https://www.apache.org/licenses/LICENSE-2.0)\n" +
                            "\n" +
                            "- Material Icons Extended library: Apache License, Version 2.0 (https://www.apache.org/licenses/LICENSE-2.0)\n" +
                            "\n" +
                            "- Accompanist Navigation Animation library: Apache License, Version 2.0 (https://www.apache.org/licenses/LICENSE-2.0)\n" +
                            "\n" +
                            "- javax.inject library: Common Development and Distribution License (CDDL) version 1.0 and GNU General Public License (GPL) version 2 with Classpath Exception (https://opensource.org/licenses/CDDL-1.0, https://www.gnu.org/licenses/old-licenses/gpl-2.0.en.html)\n" +
                            "\n" +
                            "- Firebase libraries, including firebase-analytics-ktx, firebase-auth-ktx, firebase-firestore-ktx, and play-services-auth: Apache License, Version 2.0 (https://www.apache.org/licenses/LICENSE-2.0)\n" +
                            "\n" +
                            "- Coil library:Coil \n" +
                            "\n" +
                            "Copyright 2022 Coil Contributors\n" +
                            "\n" +
                            "Licensed under the Apache License, Version 2.0 (the \"License\");\n" +
                            "you may not use this file except in compliance with the License.\n" +
                            "You may obtain a copy of the License at\n" +
                            "\n" +
                            "   https://www.apache.org/licenses/LICENSE-2.0\n" +
                            "\n" +
                            "Unless required by applicable law or agreed to in writing, software\n" +
                            "distributed under the License is distributed on an \"AS IS\" BASIS,\n" +
                            "WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.\n" +
                            "See the License for the specific language governing permissions and\n" +
                            "limitations under the License.\n" +
                            "\n" +
                            "Please see the corresponding license files in the individual libraries, or the licenses text file in this apps package, or visit the links above for the full text of the licenses.",


                        confirmButtonText = "Close",
                        onConfirmClick = { settingsScreenViewModel.closeLicenses() })
                    {
                        settingsScreenViewModel.closeLicenses()
                    }
                }
                if(uiAlertState.showAccountWasDeletedMessage){
                    OneButtonAlert(
                        text = "Your account was deleted successfully.",
                        confirmButtonText = "Ok",
                        onConfirmClick = { settingsScreenViewModel.cancelAccountWasDeletedMessage() },
                        onDismiss = { settingsScreenViewModel.cancelAccountWasDeletedMessage() }
                    )
                }
                if(reAuthForDeleteSignInResult){
                    BasicAlert(
                        text = "Are you sure you want to delete your account? Your comments will be deleted and you will not be able to use this Google ID to make a new account again.",
                        confirmButtonText = "Yes I Am Sure",
                        cancelButtonText = "Cancel",
                        onConfirmClick =
                        {
                            clearReAuthForDeleteSignInResult()
                            settingsScreenViewModel.confirmDeleteAccount()
                                         },
                        onCancelClick = { clearReAuthForDeleteSignInResult() },
                        onDismiss = { clearReAuthForDeleteSignInResult() }
                    )
                }
                if(uiAlertState.showReAuthAlert){
                    BasicAlert(
                        text = "You must Sign in again to authorize account deletion.",
                        confirmButtonText = "Sign in",
                        cancelButtonText = "Cancel",
                        onConfirmClick =
                        {
                            settingsScreenViewModel.cancelReAuthAlert()
                            confirmReAuthForDeleteAccount()
                        },
                        onCancelClick = { settingsScreenViewModel.cancelReAuthAlert() },
                        onDismiss = { settingsScreenViewModel.cancelReAuthAlert() }
                    )
                }
                if(uiAlertState.showChangeDisplayNameAlert){
                    LaunchedEffect(Unit) {
                        delay(200)
                        focusRequester.requestFocus()
                    }

                    AlertDialog(
                        onDismissRequest = {
                            settingsScreenViewModel.cancelChangeDisplayNameAlert()
                        },
                        title = {
                            Text(text = "Enter item: ", color = Color(0xFF682300))
                        },
                        text = {
                            Column {
                                TextField(
                                    value = uiAlertState.inputText,
                                    placeholder = {
                                        Text(text = uiAlertState.displayName)
                                    },
                                    onValueChange = { settingsScreenViewModel.updateInputText(it) },
                                    modifier = Modifier.focusRequester(focusRequester),
                                    singleLine = true,
                                )
                                Text(
                                    text = "${uiAlertState.inputText.text.length}/20",
                                    color = if(uiAlertState.inputText.text.length > 20) Color.Red else Color.Black
                                )
                            }
                        },
                        buttons = {
                            Row(
                                modifier = Modifier
                                    .padding(all = 8.dp)
                                    .fillMaxWidth(),
                                horizontalArrangement = Arrangement.SpaceEvenly
                            ) {

                                CancelAlertButton(buttonText = "Cancel") {
                                    settingsScreenViewModel.cancelChangeDisplayNameAlert()
                                }
                                ConfirmAlertButton(buttonText = "Confirm") {
                                    if(uiAlertState.inputText.text.length < 20 && uiAlertState.inputText.text != "") {
                                        settingsScreenViewModel.confirmDisplayNameChange()
                                    }
                                }
                            }
                        }
                    )
                }
            }
        }




    }
}

class SettingsScreenViewModelFactory(
    val application: Application,
//    val recipeName: String,
) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {

        return SettingsScreenViewModel(
            application,
//            recipeName,
            SettingsScreenFirebaseRepository(application, Firebase.firestore, Firebase.auth)
        ) as T
    }
}