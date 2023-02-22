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
import androidx.compose.material.icons.outlined.ExpandLess
import androidx.compose.material.icons.outlined.ExpandMore
import androidx.compose.material.icons.outlined.ThumbUp
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.graphics.TileMode
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImagePainter
import coil.compose.SubcomposeAsyncImage
import coil.compose.SubcomposeAsyncImageContent
import coil.request.ImageRequest


@Composable
fun ReviewWidget(
    authorName: String,
    authorImageUrl: String,
    reviewText: String,
    karma: Int,
    likes: Int,
    likedByUser: Int,
    onLikeClick: () -> Unit,
){
    var expanded by remember { mutableStateOf(false) }
//    val modifier = if(expanded) Modifier.wrapContentHeight() else Modifier.height(280.dp)

    val modifier =Modifier.wrapContentHeight()

//    val reviewText = "This This This This This This This This This This This This This This This This This This This This This This This This This This This This This This This This This This This "
    val expandable = reviewText.length > 160
    val surfaceShape = if(expandable) RoundedCornerShape(10.dp, 10.dp, 25.dp, 25.dp) else RoundedCornerShape(10.dp)

    Surface(
        modifier
            .fillMaxWidth()
            .padding(16.dp)
            .border(
                width = 2.dp,
                brush = (Brush.horizontalGradient(
                    colors = listOf(Color(0xFFb15f33), Color(0xFFb15f33),),
                    tileMode = TileMode.Mirror
                )),
                shape = surfaceShape
            ),
        shape = surfaceShape,
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

                        SubcomposeAsyncImage(
                            model = ImageRequest.Builder(LocalContext.current).data(authorImageUrl).crossfade(true).build(),
                            contentDescription = null,
                            modifier = Modifier
                                .clip(CircleShape)
                                .fillMaxSize()
                        ){
                            val state = painter.state
                            if(state is AsyncImagePainter.State.Loading || state is AsyncImagePainter.State.Error){
                                Box(
                                    Modifier.fillMaxSize(),
                                    contentAlignment = Alignment.Center) {
                                    Text(
                                        text = "T",
                                        color = Color(0xFF682300),
                                        fontSize = 36.sp
                                    )
                                }
                            }
                            else{
                                SubcomposeAsyncImageContent()
                            }

                        }

                }
                //Chef title and name
                Column(
                    Modifier
                        .padding(start = 12.dp)
                        .fillMaxHeight()
                ){
                    Text(
                        authorName,
                        color = Color(0xFFd8af84),
                        fontSize = 16.sp,
                        fontWeight = FontWeight.Bold)
                    Text(
                        if (karma > 0) "Karma $karma" else "",
                        color = Color(0xFFd8af84),
                        fontSize = 16.sp,
                        fontWeight = FontWeight.Normal)
                }

                Box(
                    Modifier
                        .fillMaxSize()
                        .padding(0.dp)){

                    Surface(
                        Modifier
                            .padding(16.dp)
                            .align(Alignment.CenterEnd)
                            .wrapContentSize()
                            .clickable { if(likedByUser != 1) onLikeClick() },
                        color = Color.Transparent
                    ) {
                        Row(Modifier.wrapContentSize(),
                            horizontalArrangement = Arrangement.Center,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Icon(
                                if (likedByUser == 1) Icons.Filled.ThumbUp else Icons.Outlined.ThumbUp,
                                contentDescription = null,
                                modifier = Modifier.size(30.dp),
                                tint = Color(0xFFd8af84)
                            )

                            Spacer(Modifier.width(6.dp))

                            Text(
                                likes.toString(),
                                color = Color(0xFFd8af84),
                                fontSize = 16.sp,
                                fontWeight = FontWeight.Bold
                            )
                        }
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

            Column(
                Modifier
                    .wrapContentSize()
                    .padding(12.dp)
                    .clickable { expanded = !expanded }
            ) {
                Text(
                    text = if(!expandable || (expandable && expanded)) reviewText else reviewText.substring(0, 160) + "...",
                    color = Color(0xFFd8af84),
                )
            }

            if(expandable) {
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
                        .fillMaxWidth()
                        .height(20.dp)
                        .clickable { expanded = !expanded }
                ){
                    Box(
                        Modifier
                            .fillMaxSize(),
                        contentAlignment = Alignment.Center
                    ){
                        Icon(
                            if(!expanded) Icons.Outlined.ExpandMore else Icons.Outlined.ExpandLess,
                            contentDescription = null,
                            Modifier.size(32.dp),
                            tint = Color(0xFF682300)
                        )
                    }

                }
            }


            
        }


    }
}

//@Preview(showSystemUi = true)
//@Composable
//fun revwidprev(){
//    ReviewWidget()
//}