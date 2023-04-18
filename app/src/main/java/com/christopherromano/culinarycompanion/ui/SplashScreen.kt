package com.christopherromano.culinarycompanion.ui

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.ClickableText
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CheckBox
import androidx.compose.material.icons.outlined.CheckBoxOutlineBlank
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.TileMode
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalUriHandler
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.constraintlayout.compose.ConstraintLayout
import com.christopherromano.culinarycompanion.R
import com.christopherromano.culinarycompanion.data.annotatedstrings.splashConsentAnoString
import com.christopherromano.culinarycompanion.ui.theme.SplashTheme
import kotlinx.coroutines.delay

@Composable
fun SplashScreen(
    showSignInButtons: Boolean,
    showLoading: Boolean,
    endSplash: Boolean,
    isConsentBoxChecked: Boolean,
    consentBoxClicked: () -> Unit,
    trySignInWithGoogle: () -> Unit,
    continueWithoutSignIn: () -> Unit,
    onSplashFinished: () -> Unit,
){
    SplashTheme(){
        Surface(
            Modifier
                .fillMaxSize(),
//            color = Color.White
        ){

            var visible by rememberSaveable { mutableStateOf(false) }

            var isError by rememberSaveable { mutableStateOf(false) }

            val uriHandler = LocalUriHandler.current

            val myText = splashConsentAnoString()

            LaunchedEffect(Unit){
                visible = true
            }

            if(endSplash){
                LaunchedEffect(Unit){
                    delay(1000)
                    onSplashFinished()
                }
            }

            ConstraintLayout(
                Modifier.fillMaxSize()
            ){

                val (image, buttons) = createRefs()

                Image(
                    painter = painterResource(id = R.drawable.mainlogo),
                    contentDescription = null,
                    modifier = Modifier.padding(start = 20.dp, end = 20.dp)
                        .constrainAs(image){
                            top.linkTo(parent.top)
                            bottom.linkTo(parent.bottom)
                            start.linkTo(parent.start)
                            end.linkTo(parent.end)
                            },
                    contentScale = ContentScale.Inside
                )
                if(showLoading || showSignInButtons){
                    Column(modifier = Modifier
                        .wrapContentSize()
                        .padding(top = 30.dp)
                        .constrainAs(buttons){
                            top.linkTo(image.bottom)
                            start.linkTo(parent.start)
                            end.linkTo(parent.end)
                        }){
                        if(showLoading){
                            CircularProgressIndicator(color = Color(0xFF682300))
                        }
                        if(showSignInButtons) {
                            Button(
                                modifier = Modifier
                                    .width(260.dp)
                                    .padding(start = 8.dp, end = 8.dp),
                                onClick = { if (isConsentBoxChecked) trySignInWithGoogle() else isError = true },
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
                                        .width(260.dp)
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
                            Button(
                                modifier = Modifier
                                    .width(260.dp)
                                    .padding(8.dp),
                                onClick = { if (isConsentBoxChecked) continueWithoutSignIn() else isError = true },
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
                                    backgroundColor = Color(0xFFd8af84),
                                    contentColor = Color(0xFF682300)
                                )
                            ) {
                                Text("Continue without signing in")
                            }

                            Spacer(
                                Modifier
                                    .width(1.dp)
                                    .height(12.dp))

                            Surface(
                                Modifier
                                    .clickable { consentBoxClicked() }
                                    .width(260.dp)
                                    .padding(8.dp),
                                color = Color.Transparent
                            ){
                                Row(
                                    Modifier
                                        .width(260.dp),
                                    verticalAlignment = Alignment.CenterVertically,
                                    horizontalArrangement = Arrangement.Center
                                ) {
                                    Icon(
                                        if(!isConsentBoxChecked) Icons.Outlined.CheckBoxOutlineBlank else Icons.Filled.CheckBox,
                                        contentDescription = "Consent to Privacy Policy, Terms and Conditions, and end user license agreement check box.",
                                        modifier = Modifier.padding(start = 4.dp, end = 10.dp),
                                        tint = if(isError && !isConsentBoxChecked) Color.Red else Color(0xFF682300)
                                    )
                                    Spacer(
                                        Modifier
                                            .width(8.dp)
                                            .height(1.dp))

                                    ClickableText(
                                        text = myText,
                                        modifier = Modifier.padding(end = 4.dp),
                                        style = TextStyle.Default.copy(
                                            color = if(isError && !isConsentBoxChecked) Color.Red else MaterialTheme.colors.onSurface,
                                            fontSize = 12.sp,
                                            textAlign = TextAlign.Center),
                                        onClick = { offset ->
                                            myText.getStringAnnotations(tag = "URL", start = offset, end = offset)
                                                .firstOrNull()?.let { uriHandler.openUri(it.item) }
                                        }
                                    )
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}