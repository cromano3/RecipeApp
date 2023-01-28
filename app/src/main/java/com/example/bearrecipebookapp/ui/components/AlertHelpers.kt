package com.example.bearrecipebookapp.ui.components

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Grade
import androidx.compose.material.icons.filled.ThumbDown
import androidx.compose.material.icons.filled.ThumbUp
import androidx.compose.material.icons.outlined.StarOutline
import androidx.compose.material.icons.outlined.ThumbDown
import androidx.compose.material.icons.outlined.ThumbUp
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.TileMode
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@Composable
fun BasicAlert(
    text: String,
    confirmButtonText: String,
    cancelButtonText: String,
    onConfirmClick: () -> Unit,
    onCancelClick: () -> Unit,
    onDismiss: () -> Unit,

){
    AlertDialog(
        onDismissRequest = onDismiss,
        text = { AlertText(text) },
        buttons = { AlertButtonsRow(confirmButtonText, cancelButtonText, onConfirmClick, onCancelClick) },
    )
}

@Composable
fun ThumbsRatingAlert(
    confirmButtonText: String,
    cancelButtonText: String,
    onConfirmClick: () -> Unit,
    onCancelClick: () -> Unit,
    onDismiss: () -> Unit,
    onThumbDownClick: () -> Unit,
    onThumbUpClick: () -> Unit,
    recipeName: String,
    isThumbDownSelected: Boolean,
    isThumbUpSelected: Boolean,
){


    AlertDialog(
        onDismissRequest = onDismiss,
        title = { AlertTitle("Great job!") },
        text =
        {
            Column{
                AlertText(recipeName)
                AlertThumbsRow(
                    isThumbDownSelected = isThumbDownSelected,
                    isThumbUpSelected = isThumbUpSelected,
                    onThumbDownClick = onThumbDownClick,
                    onThumbUpClick = onThumbUpClick
                )
            }
        },
        buttons = { AlertButtonsRow(confirmButtonText, cancelButtonText, onConfirmClick, onCancelClick) },
    )

}

@Composable
fun AlertThumbsRow(
    isThumbDownSelected: Boolean,
    isThumbUpSelected: Boolean,
    onThumbDownClick: () -> Unit,
    onThumbUpClick:() -> Unit,
){
    Row(Modifier.fillMaxWidth(),
    horizontalArrangement = Arrangement.Center){
        AlertThumbIcon(type = "Down", filled = isThumbDownSelected) { onThumbDownClick() }
        AlertThumbIcon(type = "Up", filled = isThumbUpSelected) { onThumbUpClick() }

    }
}

@Composable
fun AlertThumbIcon(
    type: String,
    filled: Boolean,
    onClick: () -> Unit,
){

    val icon = if(type == "Up"){
        if(filled) Icons.Filled.ThumbUp else Icons.Outlined.ThumbUp
    }
    else{
        if(filled) Icons.Filled.ThumbDown else Icons.Outlined.ThumbDown
    }

    Box() {
        Surface(
            modifier = Modifier
                .padding(start = 8.dp, end = 8.dp)
                .border(
                    width = 2.dp,
                    brush = (Brush.horizontalGradient(
                        colors = listOf(
                            Color(0xFFd8af84),
                            Color(0xFFb15f33)
                        ),
                        tileMode = TileMode.Mirror
                    )),
                    shape = CircleShape
                )
                .size(56.dp)
                .clickable { onClick() },
            shape = CircleShape,
            color = Color(0xFFd8af84)
        ) {
            Box(contentAlignment = Alignment.Center) {
                Icon(
                    icon,
                    tint = Color( 0xFF682300),
                    modifier = Modifier.size(48.dp),
                    // modifier = Modifier.background(color = Color(0xFFFFFFFF)),
                    contentDescription = null
                )
            }
        }
    }
}



@Composable
fun StarRatingAlert(
    starCount: Int,
    confirmButtonText: String,
    cancelButtonText: String,
    onConfirmClick: () -> Unit,
    onCancelClick: () -> Unit,
    onDismiss: () -> Unit,
    onStarClick: (Int) -> Unit,
    reviewText: String,
    onTextChange: (String) -> Unit
){
//    var starCount by remember { mutableStateOf(0) }


    AlertDialog(
        onDismissRequest = onDismiss,
        text =
        {
            Column{
//                AlertStarRating(starCount = starCount, onStarClick = { onStarClick(it) })
                AlertReviewBox(reviewText) { onTextChange(it) }
            }
        },
        buttons = { AlertButtonsRow(confirmButtonText, cancelButtonText, onConfirmClick, onCancelClick) },
    )

}

@Composable
fun AlertReviewBox(reviewText: String, onTextChange: (String) -> Unit){
    Column{
        Text(text = "Share a tip for other chefs.")
        TextField(
            value = reviewText,
            onValueChange = {onTextChange(it)},
        )
    }
}


@Composable
fun AlertStarRating(
    starCount: Int,
    onStarClick: (Int) -> Unit,
){
    Row(
        Modifier
            .fillMaxWidth()
            .padding(start = 8.dp, end = 8.dp),
        horizontalArrangement = Arrangement.SpaceEvenly
    ){
        for(x in 1..5){
            AlertStar(filled = starCount >= x) { onStarClick(x) }
        }
//        AlertStar(starCount > 0) { onStarClick(starUpdater(1)) }
//        AlertStar(starCount > 1) { onStarClick(starUpdater(2)) }
//        AlertStar(starCount > 2) { onStarClick(starUpdater(3)) }
    }
}




//private fun starUpdater(clickedStar: Int, starCount: Int): Int{
//
//    return when(clickedStar){
//        1 -> if(starCount == 1) 0 else 1
//        2 -> if(starCount == 2) 1 else 2
//        3 -> if(starCount == 3) 2 else 3
//        else -> 0
//    }
//}

@Composable
fun AlertStar(
    filled: Boolean,
    onClick: () -> Unit
) {
    Box() {
        Surface(
            modifier = Modifier
                .padding(start = 8.dp, end = 8.dp)
//                .border(
//                    width = 2.dp,
//                    brush = (Brush.horizontalGradient(
//                        colors = listOf(
//                            Color(0xFFd8af84),
//                            Color(0xFFb15f33)
//                        ),
//                        tileMode = TileMode.Mirror
//                    )),
//                    shape = CircleShape
//                )
                .size(36.dp)
                .clickable { onClick() },
            shape = CircleShape,
            color = Color(0xFFd8af84)
        ) {
            Box(contentAlignment = Alignment.Center) {
                Icon(
                    if (filled) Icons.Filled.Grade else Icons.Outlined.StarOutline,
                    tint = Color( 0xFF682300),
                    modifier = Modifier.size(36.dp),
                    // modifier = Modifier.background(color = Color(0xFFFFFFFF)),
                    contentDescription = null
                )
            }
        }
    }
}

@Composable
fun AlertTitle(title: String){
    Text(
        text = title,
        color = Color(0xFF682300))
}

@Composable
fun AlertText(text: String){
    Text(text = text,
        color = Color(0xFF682300),
        fontSize = 16.sp,
        textAlign = TextAlign.Center
    )
}

@Composable
fun AlertButtonsRow(
    confirmButtonText: String,
    cancelButtonText: String,
    onConfirmClick: () -> Unit,
    onCancelClick: () -> Unit,
){
    Row(
        modifier = Modifier
            .padding(all = 8.dp)
            .fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceEvenly
    ) {
        AlertButton(
            buttonText = cancelButtonText,
            onButtonClick = onCancelClick,
            )
        AlertButton(
            buttonText = confirmButtonText,
            onButtonClick = onConfirmClick,
        )
    }
}

@Composable
fun AlertButton(
    buttonText: String,
    onButtonClick: () -> Unit,
) {
    Button(
        modifier = Modifier.wrapContentSize(),
        onClick =  onButtonClick,
        elevation = ButtonDefaults.elevation(6.dp),
        shape = RoundedCornerShape(25.dp),
        border = BorderStroke(
            width = 2.dp,
            brush = (Brush.horizontalGradient(
                startX = -10f,
                colors = listOf(Color(0xFFd8af84), Color(0xFFb15f33)),
                tileMode = TileMode.Mirror
            )),
        ),
        colors = ButtonDefaults.buttonColors(backgroundColor = Color(0xFFd8af84), contentColor = Color(0xFF682300))
    ) {
        Text(buttonText)
    }
}