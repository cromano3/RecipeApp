package com.example.bearrecipebookapp.ui

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Icon
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Favorite
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.TileMode
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.bearrecipebookapp.R
import com.example.bearrecipebookapp.ui.theme.BearRecipeBookAppTheme

@Composable
fun ProfileScreen(){

    var target = "favorites"

    Column(modifier = Modifier.fillMaxSize()){
        Box(modifier = Modifier
            .fillMaxWidth()
            .height(250.dp)
            .background(
                Brush.horizontalGradient(
                    colors = listOf(
                        Color(0xFFb15f33),
                        Color(0xFF682300)
                    ),
                    tileMode = TileMode.Mirror
                )
            ),
            contentAlignment = Alignment.Center){
            Column(modifier = Modifier.wrapContentSize(), horizontalAlignment = Alignment.CenterHorizontally) {
                Surface(modifier = Modifier.size(150.dp), shape = RoundedCornerShape(50.dp)){
                    Box(
                        modifier = Modifier
                            .size(150.dp)
//                    .border(4.dp, color = Color(0xFFFFFFFF), shape = RoundedCornerShape(100.dp))
                            .border(
                                width = 2.dp,
                                brush = (Brush.verticalGradient(
                                    colors = listOf(
                                        Color(0xFFFFFFFF),
//                                //    Color(0xFFFFFFFF),
//                                    Color(0xFFE10600),
//                                    Color(0xFFFFFFFF),
//                                    Color(0xFF6769f1),
//                                    Color(0xFFFFFFFF),
//                                    Color(0xFFb15f33),
                                        Color(0xFFb15f33),
                                        Color(0xFFb15f33),
                                        Color(0xFFb15f33)
                                    ),
                                    tileMode = TileMode.Mirror
                                )),
                                shape = RoundedCornerShape(50.dp)
                            )
                            .background(color = Color(0xFFd8af84)),
                        contentAlignment = Alignment.Center,
                    ) {
                        Image(

                            painter = painterResource(R.drawable.chef),
                            contentScale = ContentScale.Fit,
                            alignment = Alignment.Center,
                            contentDescription = null,
                            modifier = Modifier
                                .size(120.dp)
//                            .border(4.dp, color = Color(0xFFFFFFFF), shape = RoundedCornerShape(50.dp))
                            ,


                            )
                    }

                }



                Spacer(Modifier.height(4.dp))

                Text(text = "lvl 2")

                Spacer(Modifier.height(4.dp))

                //Will be progress bar
                Spacer(
                    Modifier
                        .width(50.dp)
                        .height(4.dp)
                        .background(Color(0xFFFFFFFF)))
            }

//            Text(text = "lvl 2", modifier = Modifier
//                .align(Alignment.BottomStart)
//                .padding(bottom = 20.dp, start = 20.dp))

        }
        Row(modifier = Modifier
            .fillMaxWidth()
            .height(80.dp)){
            Column(modifier = Modifier
                .fillMaxHeight()
                .weight(1f)) {
                Icon(Icons.Outlined.Favorite, contentDescription = null)
            }
            Column(modifier = Modifier
                .fillMaxHeight()
                .weight(1f)) {
                Icon(Icons.Outlined.Favorite, contentDescription = null)

            }
            Column(modifier = Modifier
                .fillMaxHeight()
                .weight(1f)) {
                Icon(Icons.Outlined.Favorite, contentDescription = null)

            }
        }
        if(target == "favorites")
            LazyColumn(){

            }
        else if(target == "cooked")
            LazyColumn(){

            }
        else if(target == "reviews")
            LazyColumn(){

            }
    }
}

@Preview
@Composable
fun ProfilePreview(){
    BearRecipeBookAppTheme {
        ProfileScreen()
    }
}