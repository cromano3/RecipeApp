package com.christopherromano.culinarycompanion.ui.components

import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ThumbDown
import androidx.compose.material.icons.filled.ThumbUp
import androidx.compose.material.icons.outlined.ThumbDown
import androidx.compose.material.icons.outlined.ThumbUp
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.TileMode
import androidx.compose.ui.text.AnnotatedString
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
fun AnnotatedStringAlert(
    text: AnnotatedString,
    confirmButtonText: String,
    cancelButtonText: String,
    onConfirmClick: () -> Unit,
    onCancelClick: () -> Unit,
    onDismiss: () -> Unit,
){
    AlertDialog(
        onDismissRequest = onDismiss,
        text = { AnnotatedStringText(text) },
        buttons = { AlertButtonsRow(confirmButtonText, cancelButtonText, onConfirmClick, onCancelClick) },
    )

}

@Composable
fun OneButtonAlert(
    text: String,
    confirmButtonText: String,
    onConfirmClick: () -> Unit,
    onDismiss: () -> Unit
){
    AlertDialog(
        onDismissRequest = onDismiss,
        text = { AlertText(text = text)},
        buttons =
        {
            Row(
                Modifier
                    .fillMaxWidth()
                    .padding(bottom = 8.dp),
                horizontalArrangement = Arrangement.Center
            ) {
                ConfirmAlertButton(buttonText = confirmButtonText, onButtonClick = onConfirmClick)
            }
        }
    )
}


@Composable
fun AlertText(text: String){
    Row(
        Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.Center
    ){
        Text(text = text,
            modifier = Modifier.padding(bottom = 16.dp),
            color = Color(0xFF682300),
            fontSize = 18.sp,
            textAlign = TextAlign.Center
        )
    }
}


@Composable
fun ScrollableAlertText(text: String){
    Box(
        Modifier
            .fillMaxWidth()
            .fillMaxHeight()
            .verticalScroll(rememberScrollState()),
//        horizontalArrangement = Arrangement.Center
    ){
        Text(text = text,
            modifier = Modifier.padding(bottom = 16.dp),
            color = Color(0xFF682300),
            fontSize = 18.sp,
            textAlign = TextAlign.Center
        )
    }
}


@Composable
fun AnnotatedStringText(text: AnnotatedString){
    Row(
        Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.Center
    ){
        Text(text = text,
            modifier = Modifier.padding(bottom = 16.dp),
            color = Color(0xFF682300),
            fontSize = 18.sp,
            textAlign = TextAlign.Center
        )
    }
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
            .padding(start = 8.dp, end = 8.dp, bottom = 8.dp)
            .fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceEvenly
    ) {
        CancelAlertButton(
            buttonText = cancelButtonText,
            onButtonClick = onCancelClick,
        )
        ConfirmAlertButton(
            buttonText = confirmButtonText,
            onButtonClick = onConfirmClick,
        )
    }
}

@Composable
fun CancelAlertButton(
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
                colors = listOf(Color(0xFFb15f33), Color(0xFF682300)),
                tileMode = TileMode.Mirror
            )),
        ),
        colors = ButtonDefaults.buttonColors(backgroundColor = Color(0xFFd8af84), contentColor = Color(0xFF682300))
    ) {
        Text(buttonText)
    }
}

@Composable
fun ConfirmAlertButton(
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
                startX = -30f,
                colors = listOf(Color(0xFFb15f33), Color(0xFFb15f33)),
                tileMode = TileMode.Mirror
            )),
        ),
        colors = ButtonDefaults.buttonColors(backgroundColor = Color(0xFF682300), contentColor = Color(0xFFd8af84))
    ) {
        Text(buttonText)
    }
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
    text: String,
    isThumbDownSelected: Boolean,
    isThumbUpSelected: Boolean,
){


    AlertDialog(
        onDismissRequest = onDismiss,
        text =
        {
            Column{
                AlertText(text)
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
                            Color(0xFF682300),
                            Color(0xFF682300)
                        ),
                        tileMode = TileMode.Mirror
                    )),
                    shape = CircleShape
                )
                .size(74.dp)
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


