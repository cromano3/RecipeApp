package com.christopherromano.culinarycompanion.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Icon
import androidx.compose.material.Surface
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Add
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.TileMode
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.text.ExperimentalTextApi
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp

@OptIn(ExperimentalTextApi::class)
@Composable
fun AddRecipeCard(
    modifier: Modifier,
    onCreateRecipeClick: () -> Unit,
){

    val gradientWidth = with(LocalDensity.current) { 170.dp.toPx() }
    val gradientWidthButton = with(LocalDensity.current) { 48.dp.toPx() }
    val gradientImageVertical = with(LocalDensity.current) { 135.dp.toPx() }


    val image: Int


//    val gradientWidth = with(LocalDensity.current) {
//        (6 * (HighlightCardWidth + HighlightCardPadding).toPx())
//    }

    Surface(
        elevation = 0.dp,
        modifier = modifier
            .padding(top = 16.dp)
            .size(
                width = 180.dp,
                height = 270.dp
            )
            .border(
                width = 2.dp,
                brush = (Brush.horizontalGradient(
                    colors = listOf(
                        Color(0xFFd8af84),
                        Color(0xFFb15f33),

                        ),
                    tileMode = TileMode.Mirror
                )),
                shape = RoundedCornerShape(15.dp)
            )
            .clickable(onClick = onCreateRecipeClick)
//            .background(
//            Brush.horizontalGradient(
//                colors = listOf(
//                    Color(0xFFb15f33),
//                    Color(0xFF682300)
//                ),
//                endX = gradientWidth,
//                tileMode = TileMode.Mirror
//            ),
//                shape = RoundedCornerShape(15.dp)
//        )
            .alpha(1f),
        shape = RoundedCornerShape(15.dp),
        color = Color.Transparent
        //theme is assigning this value automatically so we don't need to assign it again here
        //color = MaterialTheme.colors.surface
    ){
        Column{
            Box(
                modifier = Modifier
                    .height(185.dp)
                    .fillMaxWidth()
            ){
                Box(
                    modifier = Modifier
                        .align(Alignment.BottomCenter)
                        .height(45.dp)
                        .fillMaxWidth()
                        .background(Brush.horizontalGradient(
                            colors = listOf(
                                Color(0xFFFFFFFF),
                                Color(0xFFFFFFFF)
                            )), alpha = 0.5f))
                Box(
                    modifier = Modifier
                        .height(140.dp)
                        .fillMaxWidth()
                        //.background(color = MaterialTheme.colors.secondaryVariant)
                        .background(
                            Brush.horizontalGradient(
                                colors = listOf(
                                    Color(0xFFb15f33),
                                    Color(0xFF682300)
                                ),
                                endX = gradientWidth,
                                tileMode = TileMode.Mirror
                            ),
                            alpha = 0.5f
                        )
                )
                Spacer(modifier = Modifier
                    .align(Alignment.BottomCenter)
                    .padding(bottom = 2.dp)
                    .fillMaxWidth()
                    .height(2.dp)
                    .background(color = Color(0xFFE10600)))
                //Image Surface
                Surface(
                    modifier = Modifier
                        .align(Alignment.BottomCenter)
                        .size(135.dp)
                        .border(
                            width = 2.dp,
                            brush = (Brush.verticalGradient(
                                colors = listOf(
                                    Color(0xFFFFFFFF),
                                    Color(0xFFb15f33),
                                    Color(0xFFb15f33),
                                    Color(0xFFb15f33)
                                ),
                                endY = gradientImageVertical,
                                tileMode = TileMode.Mirror
                            )),
                            shape = CircleShape
                        ),
                    color = Color(0xFF682300),
                    shape = CircleShape
                ){
                    Icon(
                        imageVector = Icons.Outlined.Add,
                        contentDescription = null,
                        tint = Color(0xFFd8af84),
                    )
                }
            }

            Box(Modifier.fillMaxSize().background(Brush.horizontalGradient(
                colors = listOf(
                    Color(0xFFFFFFFF),
                    Color(0xFFFFFFFF)
                )), alpha = 0.5f)) {
                Box(
                    Modifier
                        .padding(top = 17.dp)
                        .height(1.dp)
                        .fillMaxWidth()
                        .background(Color(0xFF6769f1))) {
                }
                Box(
                    Modifier
                        .padding(top = 38.dp)
                        .height(1.dp)
                        .fillMaxWidth()
                        .background(Color(0xFF6769f1))) {
                }
                Box(
                    Modifier
                        .padding(top = 59.dp)
                        .height(1.dp)
                        .fillMaxWidth()
                        .background(Color(0xFF6769f1))) {
                }
                Box(
                    Modifier
                        .padding(top = 80.dp)
                        .height(1.dp)
                        .fillMaxWidth()
                        .background(Color(0xFF6769f1))) {
                }
            }
        }
    }
}

@Preview
@Composable
fun Preview333(){
    AddRecipeCard(modifier = Modifier, onCreateRecipeClick = {})
}