package com.christopherromano.culinarycompanion.ui

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.slideInVertically
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.TileMode
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import com.christopherromano.culinarycompanion.R
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
    Surface(
        Modifier
            .fillMaxSize(),
        color = Color.White
    ){

        CircularProgressIndicator(color = Color(0xFF682300))

//        val animatedAlpha = remember { Animatable(0f) }
        var visible by remember { mutableStateOf(false) }
        LaunchedEffect(Unit){
//            animatedAlpha.animateTo(1f)
            visible = true
        }

        if(endSplash){
            LaunchedEffect(Unit){
                delay(5000)
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
                    .fillMaxSize()
                    .align(Alignment.BottomCenter)
                    .padding(bottom = 60.dp)){
                Button(
                    modifier = Modifier
                        .width(250.dp)
                        .padding(start = 8.dp, end = 8.dp),
                    onClick =  trySignInWithGoogle,
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
                    colors = ButtonDefaults.buttonColors(backgroundColor = Color(0xFF682300), contentColor = Color(0xFFd8af84))
                ) {
                    Row(
                        modifier = Modifier
                            .width(250.dp)
                            .height(25.dp),
                        horizontalArrangement = Arrangement.SpaceEvenly,
                        verticalAlignment = Alignment.CenterVertically
                    ){
                        Image(
                            painter = painterResource(id = R.drawable.ic_google_logo),
                            contentDescription = null
                        )
                        Text("Sign in with Google")
                    }
                }
                Button(
                    modifier = Modifier
                        .width(250.dp)
                        .padding(8.dp),
                    onClick =  continueWithoutSignIn,
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
                    colors = ButtonDefaults.buttonColors(backgroundColor = Color(0xFFd8af84), contentColor = Color(0xFF682300))
                ){
                    Text("Continue without signing in")
                }
            }
        }



    }
}