package com.christopherromano.culinarycompanion.ui.components


import androidx.compose.foundation.Image
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Icon
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ThumbDown
import androidx.compose.material.icons.filled.ThumbUp
import androidx.compose.material.icons.outlined.ExpandLess
import androidx.compose.material.icons.outlined.ExpandMore
import androidx.compose.material.icons.outlined.ThumbDown
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
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImagePainter
import coil.compose.SubcomposeAsyncImage
import coil.compose.SubcomposeAsyncImageContent
import coil.request.ImageRequest
import com.christopherromano.culinarycompanion.R


@Composable
fun ReviewWidget(
    modifier: Modifier,
    authorName: String,
    authorImageUrl: String,
    reviewText: String,
    karma: Int,
    likes: Int,
    likedByUser: Int,
    dislikedByUser: Int,
    onLikeClick: () -> Unit,
    onDislikeClick: () -> Unit,
){
    var expanded by remember { mutableStateOf(false) }
    var liked by remember { mutableStateOf(false) }
    var disliked by remember { mutableStateOf(false) }
//    val modifier = if(expanded) Modifier.wrapContentHeight() else Modifier.height(280.dp)


//    val reviewText = "This This This This This This This This This This This This This This This This This This This This This This This This This This This This This This This This This This This "
    val expandable = reviewText.length > 160
    val surfaceShape = if(expandable) RoundedCornerShape(10.dp, 10.dp, 25.dp, 25.dp) else RoundedCornerShape(10.dp)

    Surface(
        modifier.wrapContentHeight()
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
                                    Image(
                                        painterResource(R.drawable.app_symbol),
                                        contentDescription = null)

//                                    Text(
//                                        text = "T",
//                                        color = Color(0xFF682300),
//                                        fontSize = 36.sp
//                                    )
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
                        if (karma > 0) "Karma: $karma" else "",
                        color = Color(0xFFd8af84),
                        fontSize = 16.sp,
                        fontWeight = FontWeight.Normal)
                }

                Box(
                    Modifier
                        .fillMaxSize()
                        .padding(0.dp)){
                    Box(
                        Modifier
                            .padding(16.dp)
                            .align(Alignment.CenterEnd)
                            .wrapContentSize()
                    ){
                        Row(Modifier.wrapContentSize(),
                            horizontalArrangement = Arrangement.Center,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Surface(
                                Modifier
                                    .wrapContentSize()
                                    .clickable(enabled = (likedByUser != 1 && !liked && dislikedByUser != 1 && !disliked)) {
                                        liked = true; onLikeClick()
                                    },
                                color = Color.Transparent
                            ) {

                                Icon(
                                    if (likedByUser == 1 || liked) Icons.Filled.ThumbUp else Icons.Outlined.ThumbUp,
                                    contentDescription = null,
                                    modifier = Modifier.size(30.dp),
                                    tint = Color(0xFFd8af84)
                                )

                            }

                            Spacer(Modifier.width(8.dp))

                            Surface(
                                Modifier
                                    .wrapContentSize()
                                    .clickable(enabled = (likedByUser != 1 && !liked && dislikedByUser != 1 && !disliked)) {
                                        disliked = true; onDislikeClick()
                                    },
                                color = Color.Transparent
                            ) {

                                Icon(
                                    if (dislikedByUser == 1 || disliked) Icons.Filled.ThumbDown else Icons.Outlined.ThumbDown,
                                    contentDescription = null,
                                    modifier = Modifier.size(30.dp),
                                    tint = Color(0xFFd8af84)
                                )

                            }
                            Spacer(Modifier.width(8.dp))

                            Text(
                                likes.toString(),
                                modifier = Modifier.padding(bottom = 1.dp),
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

@Preview(showSystemUi = true)
@Composable
fun revwidprev(){
    ReviewWidget(
        modifier = Modifier,
        authorImageUrl = "",
        authorName = "Chirs",
        dislikedByUser = 0,
        likedByUser = 0,
        likes = 20,
        onLikeClick = {},
        karma = 5,
        reviewText = "Review Text",
        onDislikeClick = {}
    )
}