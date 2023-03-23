package com.christopherromano.culinarycompanion.ui

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.slideInVertically
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.ClickableText
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.CheckBox
import androidx.compose.material.icons.outlined.CheckBoxOutlineBlank
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.TileMode
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.platform.LocalUriHandler
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.christopherromano.culinarycompanion.R
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
    Surface(
        Modifier
            .fillMaxSize(),
        color = Color.White
    ){



//        val animatedAlpha = remember { Animatable(0f) }
        var visible by remember { mutableStateOf(false) }

        var myTextColor by remember { mutableStateOf(Color.Black) }

        val uriHandler = LocalUriHandler.current

        val myText = buildAnnotatedString {

            append("I agree to the ")

            pushStringAnnotation(
                tag = "URL",
                annotation = "https://www.ChristopherRomano.com/culinarycompaniontermsandconditions"
            )
            withStyle(
                style = SpanStyle(
                    color = myTextColor,
                    textDecoration = TextDecoration.Underline,
                    fontWeight = FontWeight.Bold
                )
            ) {
                append("Terms and Conditions")
            }

            pop()

            append(", ")

            pushStringAnnotation(
                tag = "URL",
                annotation = "https://www.ChristopherRomano.com/culinarycompanionprivacypolicy"
            )
            withStyle(
                style = SpanStyle(
                    color = myTextColor,
                    textDecoration = TextDecoration.Underline,
                    fontWeight = FontWeight.Bold
                )
            ) {
                append("Privacy Policy")
            }
            pop()

            append(", and ")

            pushStringAnnotation(
                tag = "URL",
                annotation = "https://www.ChristopherRomano.com/culinarycompanionEULA"
            )
            withStyle(
                style = SpanStyle(
                    color = myTextColor,
                    textDecoration = TextDecoration.Underline,
                    fontWeight = FontWeight.Bold
                )
            ) {
                append("EULA")
            }
            pop()
        }

        LaunchedEffect(Unit){
//            animatedAlpha.animateTo(1f)
            visible = true
        }

        if(endSplash){
            LaunchedEffect(Unit){
                delay(2000)
                onSplashFinished()
            }
        }

        Box(
            Modifier
                .fillMaxSize(),
        contentAlignment = Alignment.Center
        ){


            val density = LocalDensity.current
            AnimatedVisibility(
                visible = visible,
                enter = slideInVertically {
                    with(density) { -300.dp.roundToPx() }
                } + fadeIn(initialAlpha = 0.0f),

            ) {
                Image(
                    painter = painterResource(id = R.drawable.mainlogo),
                    contentDescription = null,
                    modifier = Modifier
//                        .alpha(animatedAlpha.value)
                )
            }

            Column(
                Modifier
                    .wrapContentSize()
                    .align(Alignment.BottomCenter)
                    .padding(bottom = 150.dp)){
                if(showLoading){
                    CircularProgressIndicator(color = Color(0xFF682300))
                }
                if(showSignInButtons) {
                    Button(
                        modifier = Modifier
                            .width(260.dp)
                            .padding(start = 8.dp, end = 8.dp),
                        onClick = { if (isConsentBoxChecked) trySignInWithGoogle() else myTextColor = Color.Red },
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
                        onClick = { if (isConsentBoxChecked) continueWithoutSignIn() else myTextColor = Color.Red },
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
                        Modifier.clickable { consentBoxClicked() }.width(260.dp).padding(8.dp),
                        color = Color.Transparent
                    ){
                        Row(
                            Modifier
                                .width(260.dp),
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.Center
                        ) {
                            Icon(
                                if(!isConsentBoxChecked) Icons.Outlined.CheckBoxOutlineBlank else Icons.Outlined.CheckBox,
                                contentDescription = "Consent to Privacy Policy, Terms and Conditions, and end user license agreement check box.",
                                tint = Color(0xFFd8af84)
                            )
                            Spacer(
                                Modifier
                                    .width(8.dp)
                                    .height(1.dp))

                            ClickableText(
                                text = myText,
                                modifier = Modifier,
                                style = TextStyle.Default.copy(color = myTextColor, fontSize = 12.sp),
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