package com.example.bearrecipebookapp.ui

import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ExpandLess
import androidx.compose.material.icons.outlined.AddBox
import androidx.compose.material.icons.outlined.DisabledByDefault
import androidx.compose.material.icons.outlined.ExpandMore
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.bearrecipebookapp.oldstuff.DataSource
import com.example.bearrecipebookapp.ui.theme.BearRecipeBookAppTheme

@Composable
fun PantryScreen (pantryList: List<List<String>>){


    var first = true
    var expanded by remember { mutableStateOf(false)}

    Column(
        modifier = Modifier
            .verticalScroll(rememberScrollState())
            .fillMaxWidth()
            .background(color = MaterialTheme.colors.surface),
    ){
        pantryList.forEach {

            PantryHelper(it)
           // PantryHelper(it,count = count, onPlusClick = { count += 1 }, onMinusClick = { count -= 1 })
        }
    }
}


//                if (first) {
//                    //draw title
//                    OutlinedTextField(
//                        modifier = Modifier.fillMaxWidth().clickable { expanded = !expanded },
//                        value = category,
//                        onValueChange = {},
//                        readOnly = true,
//                        textStyle = MaterialTheme.typography.h3,
//
//                        trailingIcon = {
//                            Icon(
//                                imageVector = Icons.Outlined.ExpandMore,
//                                contentDescription = "Localized Description"
//                            )
//                        }
//                    )
//                    first = false
//                } else if (expanded) {
//                    Text(
//                        text = category,
//                        style = MaterialTheme.typography.body1,
//                        color = MaterialTheme.colors.onSurface,
//                        fontSize = 16.sp
//                    )
//
//                }


@Composable
private fun PantryHelper(list: List<String>,
                         //onPlusClick:()->Unit, onMinusClick:()->Unit
) {

    var isFirst = true
    var isSecond = true
    var expanded by remember { mutableStateOf(false) }


    list.forEach{
        if(isFirst){
            Row(modifier = Modifier
                .background(color = Color(0xFFd8af84))
                .fillMaxWidth()
                .border(2.dp, Color(0xFF682300))
                .padding(8.dp),
                verticalAlignment = Alignment.CenterVertically
            ){

                Text(
                    modifier = Modifier
                        .weight(0.5f)
                        .clickable { expanded = !expanded },
                    text = it,
                    style = MaterialTheme.typography.h3,
                    color = Color(0xFF682300)
                )

                IconButton(onClick = { expanded = !expanded  }) {
                    Icon(
                        modifier = Modifier
                            .background(
                                color = Color(0xFF682300),
                                shape = MaterialTheme.shapes.small
                            ),
                        imageVector = if (expanded) Icons.Filled.ExpandLess else Icons.Outlined.ExpandMore,
                        tint = MaterialTheme.colors.surface,
                        contentDescription = null
                    )
                }
            }
            if(!expanded){
                Spacer(modifier = Modifier.height(8.dp))
            }
            isFirst = false
        }
        else{
            if(expanded){
                if(!isSecond){
                    Divider(startIndent = 0.dp, thickness = 2.dp, color =MaterialTheme.colors.onSurface)
                }
                else{
                    isSecond = false
                }
                ItemHelper(it)

            }
        }
    }
}

@Composable
private fun ItemHelper(it: String){

    var count by remember { mutableStateOf(0) }

    Row(
        verticalAlignment = Alignment.CenterVertically,
        modifier = Modifier.background(Color(0xFFd8af84))){
        Text(
            modifier = Modifier.padding(start = 8.dp),
            text = count.toString(),
            style = MaterialTheme.typography.body2,
            color = MaterialTheme.colors.primaryVariant,
        )
        Text(
            modifier = Modifier.padding(start = 8.dp),
            text = it,
            style = MaterialTheme.typography.body2,
            color = MaterialTheme.colors.primaryVariant,
        )
        Spacer(Modifier.weight(1f))
        IconButton(onClick = { count += 1 }) {
            Icon(
                modifier = Modifier
                    .size(24.dp)
                    .background(
                        color = MaterialTheme.colors.onSurface,
                        shape = MaterialTheme.shapes.small
                    ),
                imageVector = Icons.Outlined.AddBox,
                tint = MaterialTheme.colors.surface,
                contentDescription = null
            )
        }
        IconButton(onClick = { if(count == 0) { count = 0 } else{ count -= 1} }
        ) {
            Icon(
                modifier = Modifier
                    .size(24.dp)
                    .background(
                        color = MaterialTheme.colors.onSurface,
                        shape = MaterialTheme.shapes.small
                    ),
                imageVector = Icons.Outlined.DisabledByDefault,
                tint = MaterialTheme.colors.surface,
                contentDescription = null
            )
        }
    }
}


@Preview
@Composable
fun MyPreview(){

    BearRecipeBookAppTheme {
        Surface(
            modifier = Modifier.fillMaxSize(),
            color = MaterialTheme.colors.background
        ) {
            PantryScreen(DataSource().loadPantryList())
        }
    }

}