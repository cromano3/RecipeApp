package com.christopherromano.culinarycompanion.ui

import android.app.Application
import android.content.Intent
import android.net.Uri
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.ClickableText
import androidx.compose.material.AlertDialog
import androidx.compose.material.Button
import androidx.compose.material.ButtonDefaults
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.material.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.graphics.TileMode
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.withStyle
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
    navigateToLicensesScreen: () -> Unit,
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

        val context = LocalContext.current

        val focusRequester = remember { FocusRequester() }

        Surface(Modifier.fillMaxSize()){
            Column()
            {
                Surface(
                    Modifier
                        .padding(8.dp)
                        .height(48.dp)
                        .clickable {
                            val urlIntent = Intent(
                                Intent.ACTION_VIEW,
                                Uri.parse("https://www.ChristopherRomano.com")
                            )
                            context.startActivity(urlIntent)
                        }
                ){
                    Column(
                        Modifier.fillMaxSize(),
                        verticalArrangement = Arrangement.Center,
                        horizontalAlignment = Alignment.Start
                    ){
                        Text(
                            text = "About Me",
                            fontSize = 18.sp,
                            fontFamily = Cabin,
                            fontWeight = FontWeight.Bold,
                            color = Color(0xFF682300)
                        )
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

                if(authState == 1)
                {
                    Surface(
                        Modifier
                            .padding(8.dp)
                            .height(48.dp)
                            .clickable { settingsScreenViewModel.updateDisplayName() })
                    {
                        Column(
                            Modifier.fillMaxSize(),
                            verticalArrangement = Arrangement.Center,
                            horizontalAlignment = Alignment.Start
                        ){
                            Text(
                                text = "Update Display Name",
                                fontSize = 18.sp,
                                fontFamily = Cabin,
                                fontWeight = FontWeight.Bold,
                                color = Color(0xFF682300)
                            )
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

                    Surface(
                        Modifier
                            .padding(8.dp)
                            .height(48.dp)
                            .clickable { settingsScreenViewModel.triggerDeleteAccountAlert() })
                    {
                        Column(
                            Modifier.fillMaxSize(),
                            verticalArrangement = Arrangement.Center,
                            horizontalAlignment = Alignment.Start
                        )
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
                        .height(48.dp)
                        .clickable { navigateToLicensesScreen() }
                ){
                    Column(
                        Modifier.fillMaxSize(),
                        verticalArrangement = Arrangement.Center,
                        horizontalAlignment = Alignment.Start
                    ) {
                        Text(
                            text = "Licenses Info",
                            fontSize = 18.sp,
                            fontFamily = Cabin,
                            fontWeight = FontWeight.Bold,
                            color = Color(0xFF682300)
                        )
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
                Surface(
                    Modifier
                        .padding(8.dp)
                        .height(48.dp)
                        .clickable {
                            val urlIntent = Intent(
                                Intent.ACTION_VIEW,
                                Uri.parse("https://www.ChristopherRomano.com/culinarycompaniontermsandconditions")
                            )
                            context.startActivity(urlIntent)
                        }
                ){
                    Column(
                        Modifier.fillMaxSize(),
                        verticalArrangement = Arrangement.Center,
                        horizontalAlignment = Alignment.Start
                    )
                    {
                        Text(
                            text = "Terms and Conditions",
                            fontSize = 18.sp,
                            fontFamily = Cabin,
                            fontWeight = FontWeight.Bold,
                            color = Color(0xFF682300)
                        )
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
                Surface(
                    Modifier
                        .padding(8.dp)
                        .height(48.dp)
                        .clickable {
                            val urlIntent = Intent(
                                Intent.ACTION_VIEW,
                                Uri.parse("https://www.ChristopherRomano.com/culinarycompanioneula")
                            )
                            context.startActivity(urlIntent)
                        }
                ){
                    Column(
                        Modifier.fillMaxSize(),
                        verticalArrangement = Arrangement.Center,
                        horizontalAlignment = Alignment.Start
                    )
                    {
                        Text(
                            text = "EULA (End User License Agreement)",
                            fontSize = 18.sp,
                            fontFamily = Cabin,
                            fontWeight = FontWeight.Bold,
                            color = Color(0xFF682300)
                        )
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
                Surface(
                    Modifier
                        .padding(8.dp)
                        .height(48.dp)
                        .clickable {
                            val urlIntent = Intent(
                                Intent.ACTION_VIEW,
                                Uri.parse("https://www.ChristopherRomano.com/culinarycompanionprivacypolicy")
                            )
                            context.startActivity(urlIntent)
                        }
                ){
                    Column(
                        Modifier.fillMaxSize(),
                        verticalArrangement = Arrangement.Center,
                        horizontalAlignment = Alignment.Start
                    )
                    {
                        Text(
                            text = "Privacy Policy",
                            fontSize = 18.sp,
                            fontFamily = Cabin,
                            fontWeight = FontWeight.Bold,
                            color = Color(0xFF682300)
                        )
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
                Surface(
                    Modifier
                        .padding(8.dp)
                        .height(48.dp)
                ){
                    val email = "bugreport.culinarycompanion@gmail.com"
                    val subject = "Bug Report"
                    val body = "Your device model and manufacturer: \n \n" +
                            "Your Android operating system version number: \n \n" +
                            "Steps to reproduce bug (this is critical for our development team to fix the problem.  Please reproduce the bug yourself first before submitting a report.): \n \n" +
                            "Detailed description of bug:"

                    val annotatedString = buildAnnotatedString {

                        pushStringAnnotation(
                            tag = "Email",
                            annotation = "bugreport.culinarycompanion@gmail.com"
                        )
                        withStyle(
                            style = SpanStyle(
                                fontSize = 18.sp,
                                fontFamily = Cabin,
                                fontWeight = FontWeight.Bold,
                                color = Color(0xFF682300))
                        ) {
                            append("Bug Report")
                        }

                        pop()

                    }

                    Column(
                        Modifier.fillMaxSize(),
                        verticalArrangement = Arrangement.Center,
                        horizontalAlignment = Alignment.Start
                    ){
                        ClickableText(
                            text = annotatedString,
                            onClick = { offset ->
                                annotatedString.getStringAnnotations(
                                    tag = "Email",
                                    start = offset,
                                    end = offset
                                )
                                    .firstOrNull()?.let {
                                        val i = Intent(Intent.ACTION_SEND)

                                        val emailAddress = arrayOf(email)
                                        i.putExtra(Intent.EXTRA_EMAIL, emailAddress)
                                        i.putExtra(Intent.EXTRA_SUBJECT, subject)
                                        i.putExtra(Intent.EXTRA_TEXT, body)

                                        i.type = "message/rfc822"

                                        context.startActivity(
                                            Intent.createChooser(
                                                i,
                                                "Choose an Email client: "
                                            )
                                        )
                                    }


                            }
                        )
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
                if(uiAlertState.showDeleteAccountAlert){
                    BasicAlert(
                        text = "Are you sure you want to delete your account? This process is permanent and cannot be reversed.",
                        confirmButtonText = "Yes I am sure",
                        cancelButtonText = "Cancel",
                        onConfirmClick =
                        {
                            settingsScreenViewModel.confirmDeleteAccount()
                                         },
                        onCancelClick = { settingsScreenViewModel.cancelDeleteAccountAlert() },
                        onDismiss = { settingsScreenViewModel.cancelDeleteAccountAlert() }
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
                                        Text(
                                            text = uiAlertState.displayName,
                                            modifier = Modifier.alpha(.3f))
                                    },
                                    onValueChange = { settingsScreenViewModel.updateInputText(it) },
                                    modifier = Modifier.focusRequester(focusRequester),
                                    singleLine = true,
                                )
                                Text(
                                    text = "${uiAlertState.inputText.text.length}/14",
                                    color = if(uiAlertState.inputText.text.length > 14) Color.Red else Color.Black
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
                                    if(uiAlertState.inputText.text.length <= 14 && uiAlertState.inputText.text != "") {
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
) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {

        return SettingsScreenViewModel(
            application,
            SettingsScreenFirebaseRepository(Firebase.firestore, Firebase.auth)
        ) as T
    }
}