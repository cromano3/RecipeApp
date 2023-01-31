package com.example.bearrecipebookapp.ui.components

import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.TileMode
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@Composable
fun DetailsScreenButton(
    onClick: () -> Unit,
    borderStartColor: Color,
    borderEndColor: Color,
    textColor: Color,
    backgroundColor: Color,
    buttonText: String,
    ){

    Surface(
        modifier = Modifier
            .wrapContentSize()
            .border(
                width = 2.dp,
                brush = (Brush.horizontalGradient(
                    colors = listOf(borderStartColor, borderEndColor),
                    startX = -20f,
                    tileMode = TileMode.Mirror
                )),
                shape = CircleShape
            )
            .clickable(
                onClick = onClick,
            ),
        shape = RoundedCornerShape(25.dp),
        color = backgroundColor,
        elevation = 4.dp,

        ) {
        Row(
            horizontalArrangement = Arrangement.Start,
            modifier = Modifier.wrapContentSize().padding(start = 2.dp, end = 2.dp, top = 4.dp, bottom = 4.dp)
        )
        {

            Text(
                text = buttonText,
                modifier = Modifier
                    .align(Alignment.CenterVertically)
                    .padding(start = 8.dp, end = 8.dp, top = 4.dp, bottom = 4.dp),
                color = textColor,
                fontSize = 18.sp,
                textAlign = TextAlign.Center,
                fontWeight = FontWeight.Bold

            )
        }
    }

}