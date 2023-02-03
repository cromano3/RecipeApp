package com.example.bearrecipebookapp.ui.components


import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Icon
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ThumbUp
import androidx.compose.material.icons.outlined.ThumbUp
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.graphics.TileMode
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp


@Composable
fun ReviewWidget(){
    Surface(
        Modifier
            .fillMaxWidth()
            .height(280.dp)
            .padding(16.dp)
            .border(
                width = 2.dp,
                brush = (Brush.horizontalGradient(
                    colors = listOf(Color(0xFFb15f33), Color(0xFFb15f33),),
                    tileMode = TileMode.Mirror
                )),
                shape = RoundedCornerShape(0)
            ),
        shape = RoundedCornerShape(0),
        color = Color(0xFF682300)
//        Color(0xFFb15f33)
        ,
    ){
        Column{
            Row(
                Modifier
                    .padding(12.dp)
                    .height(65.dp),
                verticalAlignment = Alignment.CenterVertically
            ){
                Surface(
                    Modifier
                        .size(65.dp)
//                        .padding(12.dp)
                        .border(
                            width = 2.dp,
                            brush = (Brush.horizontalGradient(
                                colors = listOf(Color(0xFFb15f33), Color(0xFFb15f33),),
                                tileMode = TileMode.Mirror
                            )),
                            shape = CircleShape
                        ),
                    shape = CircleShape,
                    color = Color(0xFFd8af84)
                ){
                    Box(
                        Modifier.fillMaxSize(),
                        contentAlignment = Alignment.Center){
                        Text(
                            text = "C",
                            color = Color(0xFF682300),
                            fontSize = 36.sp
                            )
                    }
                }
                //Chef title and name
                Column(
                    Modifier
                        .padding(start = 12.dp)
                        .fillMaxHeight()){
                    Text(
                        "Chris",
                        color = Color(0xFFd8af84),
                        fontSize = 16.sp,
                        fontWeight = FontWeight.Bold)
                    Text(
                        "Sous Chef",
                        color = Color(0xFFd8af84),
                        fontSize = 16.sp,
                        fontWeight = FontWeight.Normal)


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

            Column(
                Modifier
                    .fillMaxSize()
                    .padding(12.dp)
            ) {
                Text(
                    text = "limit this to 160 and everything else is ...More with more clickable",
                    color = Color(0xFFd8af84),
                )

            }
        }

        Box(Modifier.fillMaxSize().padding(0.dp)){

                Surface(
                    Modifier
                        .padding(16.dp)
                        .align(Alignment.BottomStart)
                        .clickable { },
                    color = Color.Transparent
                ) {
                    Row(Modifier.wrapContentSize(),
                        horizontalArrangement = Arrangement.Center,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Icon(
                            if (false) Icons.Filled.ThumbUp else Icons.Outlined.ThumbUp,
                            contentDescription = null,
                            modifier = Modifier.size(30.dp),
                            tint = Color(0xFFd8af84)
                        )

                        Spacer(Modifier.width(6.dp))

                        Text(
                            "24",
                            color = Color(0xFFd8af84),
                            fontSize = 16.sp,
                            fontWeight = FontWeight.Bold
                        )
                }
            }
        }
    }
}

@Preview(showSystemUi = true)
@Composable
fun revwidprev(){
    ReviewWidget()
}