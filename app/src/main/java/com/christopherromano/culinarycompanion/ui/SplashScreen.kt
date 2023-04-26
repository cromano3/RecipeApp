package com.christopherromano.culinarycompanion.ui

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.ClickableText
import androidx.compose.material.Button
import androidx.compose.material.ButtonDefaults
import androidx.compose.material.CircularProgressIndicator
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
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
    trySignInWithGoogle: () -> Unit,
    continueWithoutSignIn: () -> Unit,
    onSplashFinished: () -> Unit,
){
    SplashTheme(){
        Surface(Modifier.fillMaxSize()){

            val uriHandler = LocalUriHandler.current
            val myText = splashConsentAnoString()

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
                        },
                        horizontalAlignment = Alignment.CenterHorizontally,
                        verticalArrangement = Arrangement.Center){
                        if(showLoading){
                            CircularProgressIndicator(color = Color(0xFF682300))
                        }
                        if(showSignInButtons) {
                            Button(
                                modifier = Modifier
                                    .width(260.dp)
                                    .height(41.dp)
                                    .padding(start = 8.dp, end = 8.dp),
                                onClick = { trySignInWithGoogle() },
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
                            Spacer(Modifier.height(16.dp))
                            Button(
                                onClick = { continueWithoutSignIn() },
                                modifier = Modifier
                                    .width(260.dp)
                                    .height(41.dp)
                                    .padding(start = 8.dp, end = 8.dp),
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
                                Box(modifier = Modifier.height(25.dp).width(260.dp)) {
                                    Text("Continue without signing in", modifier = Modifier.align(Alignment.Center))
                                }
                            }

                            Spacer(
                                Modifier
                                    .width(1.dp)
                                    .height(8.dp))

                            Surface(
                                Modifier
                                    .wrapContentWidth()
                                    .padding(8.dp),
                                color = Color.Transparent
                            ){
                                Row(
                                    Modifier
                                        .wrapContentWidth(),
                                    verticalAlignment = Alignment.CenterVertically,
                                    horizontalArrangement = Arrangement.Center
                                ) {
                                    ClickableText(
                                        text = myText,
                                        modifier = Modifier.padding(end = 4.dp),
                                        style = TextStyle.Default.copy(
                                            color = MaterialTheme.colors.onSurface,
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