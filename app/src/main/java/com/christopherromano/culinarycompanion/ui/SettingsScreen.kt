package com.christopherromano.culinarycompanion.ui

import android.app.Application
import android.content.Intent
import android.net.Uri
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
import com.christopherromano.culinarycompanion.R
import com.christopherromano.culinarycompanion.data.repository.SettingsScreenFirebaseRepository
import com.christopherromano.culinarycompanion.ui.components.BasicAlert
import com.christopherromano.culinarycompanion.ui.components.CancelAlertButton
import com.christopherromano.culinarycompanion.ui.components.ConfirmAlertButton
import com.christopherromano.culinarycompanion.ui.components.OneButtonAlert
import com.christopherromano.culinarycompanion.ui.theme.Cabin
import com.christopherromano.culinarycompanion.viewmodel.SettingsScreenViewModel
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import kotlinx.coroutines.delay

@Composable
fun SettingsScreen(
    confirmSignInWithGoogle: () -> Unit,
    confirmReAuthForDeleteAccount: () -> Unit,
    clearReAuthForDeleteSignInResult: () -> Unit,
    navigateToLicensesScreen: () -> Unit,
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

        val ctx = LocalContext.current

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
                        .clickable { navigateToLicensesScreen() }
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
                        .clickable {
                            val urlIntent = Intent(Intent.ACTION_VIEW, Uri.parse("https://www.ChristopherRomano.com"))
                            ctx.startActivity(urlIntent)
                        }
                ){
                    Text(
                        text = "Terms and Conditions",
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
                        .clickable {
                            val urlIntent = Intent(Intent.ACTION_VIEW, Uri.parse("https://www.ChristopherRomano.com"))
                            ctx.startActivity(urlIntent)
                        }
                ){
                    Text(
                        text = "EULA (End User License Agreement)",
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
                        .clickable {
                        val urlIntent = Intent(Intent.ACTION_VIEW, Uri.parse("https://www.ChristopherRomano.com"))
                            ctx.startActivity(urlIntent)
                        }
                ){
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