package com.example.bearrecipebookapp.ui

import android.app.Application
import androidx.compose.animation.*
import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.SnapSpec
import androidx.compose.animation.core.tween
import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Grade
import androidx.compose.material.icons.outlined.Favorite
import androidx.compose.material.icons.outlined.Reviews
import androidx.compose.runtime.*
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.CornerRadius
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.*
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewmodel.compose.LocalViewModelStoreOwner
import androidx.lifecycle.viewmodel.compose.viewModel
import coil.compose.AsyncImage
import com.example.bearrecipebookapp.R
import com.example.bearrecipebookapp.datamodel.*
import com.example.bearrecipebookapp.ui.components.RecipeCard
import com.example.bearrecipebookapp.ui.theme.BearRecipeBookAppTheme
import com.example.bearrecipebookapp.viewmodel.ProfileScreenViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

@OptIn(ExperimentalAnimationApi::class)
@Composable
fun ProfileScreen(
    uiState: ProfileScreenDataModel,
    uiAlertState: UiAlertStateProfileScreenDataModel,
    uiStarsState: ProfileScreenStarsDataModel,
    favoritesData: List<RecipeWithIngredientsAndInstructions>,
    cookedData: List<RecipeWithIngredients>,
    onRemoveClick: (RecipeWithIngredientsAndInstructions) -> Unit,
    onDetailsClick: () -> Unit,
) {

    val owner = LocalViewModelStoreOwner.current

    owner?.let { it ->
        val profileScreenViewModel: ProfileScreenViewModel = viewModel(
            it,
            "ProfileScreenViewModel",
            ProfileScreenViewModelFactory(LocalContext.current.applicationContext as Application)
        )


//        val uiState by profileScreenViewModel.uiState.collectAsState()
//        val uiAlertState by profileScreenViewModel.uiAlertState.collectAsState()
//        val uiStarsState by profileScreenViewModel.uiStarsState.collectAsState()
//
//        val favoritesData by profileScreenViewModel.favoritesData.observeAsState(listOf())
//        val cookedData by profileScreenViewModel.cookedData.observeAsState(listOf())

        val expToGive by profileScreenViewModel.expToGive.observeAsState()

        var startAnimation by remember { mutableStateOf(true) }

        val fadedColors = listOf(Color(0x80D8AF84), Color(0x80B15F33))
        val fullColors = listOf(Color(0xFFd8af84), Color(0xFFb15f33))

        val coroutineScope = rememberCoroutineScope()

        val animatedFirstValue = remember {Animatable(0f) }



//        val animatedFirstValue =
//            remember{
//                lazy{
//                    coroutineScope.launch(Dispatchers.Main){
//                        val result = async(Dispatchers.IO) { Animatable(profileScreenViewModel.getStart()) }
//                        result.await()
//                    }
//                }
//            }

//        val animatedFirstValue =
//            remember{
//                lazy{
//                    coroutineScope.launch{
//                        val result = async { Animatable(profileScreenViewModel.getStart()) }
//                        result.await()
//                    }
//
//                    Animatable(0f)
//                }
//            }

//        val animatedFirstValue1 by lazy {
//            runBlocking {
//                val value = database.getAnimatedFirstValue()
//                Animatable(value)
//            }
//        }

//        val myVariable by lazy {
//            val deferred = GlobalScope.async {
//                // code to retrieve value from database
//            }
//            deferred.await()
//        }

//        fun main() = runBlocking {
//            val deferred = async {
//                // Do some long-running task here
//                delay(1000)
//                "Task complete"
//            }
//            println("Before await")
//            val result = deferred.await()
//            println("After await: $result")
//        }



        val barWidth = 600f
        val barHeight = 50f

        val mySize = Size(animatedFirstValue.value * barWidth, barHeight-10f)

        if(startAnimation){
            println("start animation")
            startAnimation = false
            profileScreenViewModel.animationSetup()
        }

        if(uiState.doAnimation){
            coroutineScope.launch {
                println("do animation")
                profileScreenViewModel.stopDoAnimation()
//                delay(1000)
//                println("before")
                withContext(Dispatchers.Main) { animatedFirstValue.animateTo(uiState.animationTargetFirst, animationSpec = tween(500)) }
//                println("after")
                profileScreenViewModel.endAnimation()
            }
        }

        if(uiState.resetAnimation){
            coroutineScope.launch {
                profileScreenViewModel.stopReset()
                withContext(Dispatchers.Main) { animatedFirstValue.animateTo(0f, animationSpec = SnapSpec(0)) }
                profileScreenViewModel.startNextAnimation()
            }
        }




        Column(modifier = Modifier.fillMaxSize()) {
            Box(
                modifier = Modifier
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
                contentAlignment = Alignment.Center
            ) {
                Column(
                    modifier = Modifier.wrapContentSize(),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {

                    Spacer(
                        Modifier
                            .height(8.dp)
                            .fillMaxWidth())

                    Text(
                        text = uiState.title,
                        color = Color(0xFFd8af84),
                    )

                    Spacer(
                        Modifier
                            .height(8.dp)
                            .fillMaxWidth())

                    Surface(modifier = Modifier.size(150.dp), shape = RoundedCornerShape(50.dp)) {
                        //image box
                        Box(
                            modifier = Modifier
                                .size(150.dp)
                                .border(
                                    width = 2.dp,
                                    brush = (Brush.verticalGradient(
                                        colors = listOf(
                                            Color(0xFFFFFFFF),
                                            Color(0xFFb15f33),
                                            Color(0xFFb15f33),
                                            Color(0xFFb15f33)
                                        ),
                                        tileMode = TileMode.Mirror
                                    )),
                                    shape = RoundedCornerShape(50.dp)
                                )
                                .background(
                                    color =
//                                Color(0xFFd8af84)
                                    Color(0xFF682300)
                                ),
                            contentAlignment = Alignment.Center,
                        ) {
                            Image(

                                painter = painterResource(R.drawable.chef),
                                contentScale = ContentScale.Fit,
                                alignment = Alignment.Center,
                                contentDescription = null,
                                modifier = Modifier
                                    .height(90.dp)
                                    .width(120.dp),
                                colorFilter = ColorFilter.tint(Color(0xFFd8af84))
                                )

                            //star box upper
                            Box(
                                modifier = Modifier
                                    .width(150.dp)
                                    .requiredHeight(75.dp)
                                    .clip(RoundedCornerShape(50.dp, 50.dp, 0.dp, 0.dp))
                                    .align(Alignment.TopCenter),
                            ){
                                //star 1
                                Surface(
                                    elevation = 8.dp,
                                    modifier = Modifier
                                        .padding(top = 2.dp)
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
                                        .size(24.dp)
                                        .align(Alignment.TopCenter),
                                    shape = CircleShape,
                                    color = Color.Transparent
                                ) {
                                    Box(contentAlignment = Alignment.Center) {
                                        this@Column.AnimatedVisibility((uiStarsState.starList[0] == Icons.Filled.Grade), enter = scaleIn(tween(200))
                                        ){
                                    //                                        if(uiStarsState.starList[0] == Icons.Filled.Grade){
                                            Icon(
                                                uiStarsState.starList[0],
                                                tint = Color(0xFFd8af84),
                                                modifier = Modifier.size(20.dp),
                                                // modifier = Modifier.background(color = Color(0xFFFFFFFF)),
                                                contentDescription = null
                                            )
                                        }
                                    }
                                }
                                //star 2
                                Surface(
                                    elevation = 8.dp,
                                    modifier = Modifier
                                        .padding(end = 20.dp, top = 7.dp)
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
                                        .size(24.dp)
                                        .align(Alignment.TopEnd)
                                        ,
                                    shape = CircleShape,
                                    color = Color.Transparent
                                ) {
                                    Box(contentAlignment = Alignment.Center) {
                                        this@Column.AnimatedVisibility((uiStarsState.starList[1] == Icons.Filled.Grade), enter = scaleIn(tween(200))
                                        ){
//                                        if(uiStarsState.starList[1] == Icons.Filled.Grade){
                                            Icon(
                                                uiStarsState.starList[1],
                                                tint = Color(0xFFd8af84),
                                                modifier = Modifier.size(20.dp),
                                                // modifier = Modifier.background(color = Color(0xFFFFFFFF)),
                                                contentDescription = null
                                            )
                                        }
                                    }
                                }
                                //star 3
                                Surface(
                                    elevation = 8.dp,
                                    modifier = Modifier
                                        .padding(bottom = 8.dp, end = 2.dp)
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
                                        .size(24.dp)
                                        .align(Alignment.BottomEnd),
                                    shape = CircleShape,
                                    color = Color.Transparent
                                ) {
                                    Box(contentAlignment = Alignment.Center) {
                                        this@Column.AnimatedVisibility((uiStarsState.starList[2] == Icons.Filled.Grade), enter = scaleIn(tween(200))
                                        ){
//                                        if(uiStarsState.starList[2] == Icons.Filled.Grade) {
                                            Icon(
                                                uiStarsState.starList[2],
                                                tint = Color(0xFFd8af84),
                                                modifier = Modifier.size(20.dp),
                                                // modifier = Modifier.background(color = Color(0xFFFFFFFF)),
                                                contentDescription = null
                                            )
                                        }
                                    }
                                }
                                //star 9
                                Surface(
                                    elevation = 8.dp,
                                    modifier = Modifier
                                        .padding(bottom = 8.dp, start = 2.dp)
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
                                        .size(24.dp)
                                        .align(Alignment.BottomStart),
                                    shape = CircleShape,
                                    color = Color.Transparent
                                ) {
                                    Box(contentAlignment = Alignment.Center) {
                                        this@Column.AnimatedVisibility((uiStarsState.starList[8] == Icons.Filled.Grade), enter = scaleIn(tween(200))
                                        ){
//                                        if(uiStarsState.starList[8] == Icons.Filled.Grade) {
                                            Icon(
                                                uiStarsState.starList[8],
                                                tint = Color(0xFFd8af84),
                                                modifier = Modifier.size(20.dp),
                                                // modifier = Modifier.background(color = Color(0xFFFFFFFF)),
                                                contentDescription = null
                                            )
                                        }
                                    }
                                }
                                //star 10
                                Surface(
                                    elevation = 8.dp,
                                    modifier = Modifier
                                        .padding(start = 20.dp, top = 7.dp)
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
                                        .size(24.dp)
                                        .align(Alignment.TopStart),
                                    shape = CircleShape,
                                    color = Color.Transparent
                                ) {
                                    Box(contentAlignment = Alignment.Center) {
                                        this@Column.AnimatedVisibility((uiStarsState.starList[9] == Icons.Filled.Grade), enter = scaleIn(tween(200))
                                        ){
//                                        if(uiStarsState.starList[9] == Icons.Filled.Grade) {
                                            Icon(
                                                uiStarsState.starList[9],
                                                tint = Color(0xFFd8af84),
                                                modifier = Modifier.size(20.dp),
                                                // modifier = Modifier.background(color = Color(0xFFFFFFFF)),
                                                contentDescription = null
                                            )
                                        }
                                    }
                                }



                            }

                            //star box lower
                            Box(
                                modifier = Modifier
                                    .width(150.dp)
                                    .requiredHeight(75.dp)
                                    .clip(RoundedCornerShape(0.dp, 0.dp, 50.dp, 50.dp))
                                    .align(Alignment.BottomCenter),
                            ){
                                //star 4
                                Surface(
                                    elevation = 0.dp,
                                    modifier = Modifier
                                        .padding(top = 8.dp, end = 2.dp)
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
                                        .size(24.dp)
                                        .align(Alignment.TopEnd),
                                    shape = CircleShape,
                                    color = Color.Transparent
                                ) {
                                    Box(contentAlignment = Alignment.Center) {
                                        this@Column.AnimatedVisibility((uiStarsState.starList[3] == Icons.Filled.Grade), enter = scaleIn(tween(200))
                                        ){
//                                        if(uiStarsState.starList[3] == Icons.Filled.Grade) {
                                            Icon(
                                                uiStarsState.starList[3],
                                                tint = Color(0xFFd8af84),
                                                modifier = Modifier.size(20.dp),
                                                // modifier = Modifier.background(color = Color(0xFFFFFFFF)),
                                                contentDescription = null
                                            )
                                        }
                                    }
                                }
                                //star 5
                                Surface(
                                    elevation = 0.dp,
                                    modifier = Modifier
                                        .padding(end = 20.dp, bottom = 7.dp)
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
                                        .size(24.dp)
                                        .align(Alignment.BottomEnd)
                                    ,
                                    shape = CircleShape,
                                    color = Color.Transparent
                                ) {
                                    Box(contentAlignment = Alignment.Center) {
                                        this@Column.AnimatedVisibility((uiStarsState.starList[4] == Icons.Filled.Grade), enter = scaleIn(tween(200))
                                        ){
//                                        if(uiStarsState.starList[4] == Icons.Filled.Grade) {
                                            Icon(
                                                uiStarsState.starList[4],
                                                tint = Color(0xFFd8af84),
                                                modifier = Modifier.size(20.dp),
                                                // modifier = Modifier.background(color = Color(0xFFFFFFFF)),
                                                contentDescription = null
                                            )
                                        }
                                    }
                                }
                                //star 6
                                Surface(
                                    elevation = 0.dp,
                                    modifier = Modifier
                                        .padding(bottom = 2.dp)
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
                                        .size(24.dp)
                                        .align(Alignment.BottomCenter),
                                    shape = CircleShape,
                                    color = Color.Transparent
                                ) {
                                    Box(contentAlignment = Alignment.Center) {
                                        this@Column.AnimatedVisibility((uiStarsState.starList[5] == Icons.Filled.Grade), enter = scaleIn(tween(200))
                                        ){
//                                        if(uiStarsState.starList[5] == Icons.Filled.Grade) {
                                            Icon(
                                                uiStarsState.starList[5],
                                                tint = Color(0xFFd8af84),
                                                modifier = Modifier.size(20.dp),
                                                // modifier = Modifier.background(color = Color(0xFFFFFFFF)),
                                                contentDescription = null
                                            )
                                        }
                                    }
                                }
                                //star 7
                                Surface(
                                    elevation = 0.dp,
                                    modifier = Modifier
                                        .padding(start = 20.dp, bottom = 7.dp)
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
                                        .size(24.dp)
                                        .align(Alignment.BottomStart),
                                    shape = CircleShape,
                                    color = Color.Transparent
                                ) {
                                    Box(contentAlignment = Alignment.Center) {
                                        this@Column.AnimatedVisibility((uiStarsState.starList[6] == Icons.Filled.Grade), enter = scaleIn(tween(200))
                                        ){
//                                        if(uiStarsState.starList[6] == Icons.Filled.Grade) {
                                            Icon(
                                                uiStarsState.starList[6],
                                                tint = Color(0xFFd8af84),
                                                modifier = Modifier.size(20.dp),
                                                // modifier = Modifier.background(color = Color(0xFFFFFFFF)),
                                                contentDescription = null
                                            )
                                        }
                                    }
                                }
                                //star 8
                                Surface(
                                    elevation = 0.dp,
                                    modifier = Modifier
                                        .padding(top = 8.dp, start = 2.dp)
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
                                        .size(24.dp)
                                        .align(Alignment.TopStart),
                                    shape = CircleShape,
                                    color = Color.Transparent
                                ) {
                                    Box(contentAlignment = Alignment.Center) {
                                        this@Column.AnimatedVisibility((uiStarsState.starList[7] == Icons.Filled.Grade), enter = scaleIn(tween(200))
                                        ){
//                                        if (uiStarsState.starList[7] == Icons.Filled.Grade) {
                                            Icon(
                                                uiStarsState.starList[7],
                                                tint = Color(0xFFd8af84),
                                                modifier = Modifier.size(20.dp),
                                                // modifier = Modifier.background(color = Color(0xFFFFFFFF)),
                                                contentDescription = null
                                            )
                                        }
                                    }
                                }
                            }
                        }
                    }

                    Spacer(Modifier.height(4.dp))

                    Text(
                        text = "level ${uiState.level}",
                        color = Color(0xFFd8af84),
                    )

                    Spacer(Modifier.height(4.dp))

                    androidx.compose.foundation.Canvas(modifier = Modifier
                        .height(40.dp)
                        .fillMaxWidth()){
                        val canvasWidth = size.width

                        //outer boarder
                        drawRoundRect(
                            brush = (Brush.horizontalGradient(
                                colors = listOf(Color(0xFFd8af84), Color(0xFFb15f33)),
                                startX = canvasWidth / 2 - barWidth/2,
                                endX = canvasWidth - (canvasWidth / 2 - barWidth/2),
                            )
                                    ),
                            topLeft = Offset(canvasWidth / 2 - barWidth/2, 0f),
                            size = Size(barWidth, barHeight),
                            cornerRadius = CornerRadius(25f, 25f),
                            style = Stroke(10f)
                        )

                        //Inner progress bar
                        if(!uiState.resetAnimation) {
                            drawRoundRect(
                                brush = (Brush.horizontalGradient(
                                    colors = listOf(Color(0xFFd8af84), Color(0xFFb15f33)),
                                    startX = canvasWidth / 2 - barWidth / 2,
                                    endX = canvasWidth - (canvasWidth / 2 - barWidth / 2),
                                )
                                        ),
                                //starting position
                                topLeft = Offset(canvasWidth / 2 - barWidth / 2, 5f),
                                /**
                                Animate this for exp changes
                                 */
                                size = if(uiState.xpToGive == 0) Size(uiState.animationTargetFirst * barWidth, barHeight-10f) else mySize,
                                cornerRadius = CornerRadius(25f, 25f),
                            )
                        }
                    }
                }
            }
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(80.dp)
            ) {

                //Favorite button tab
                Surface(
                    color = Color.Transparent,
                    modifier = Modifier
                        .fillMaxSize()
                        .weight(1f)
                        .clickable(onClick = { profileScreenViewModel.setActiveTab("favorites") })
                        .background(
                            brush = Brush.horizontalGradient(
                                listOf(
                                    Color(0xFF682300),
                                    Color(0xFF682300)
                                )
                            ),
                            alpha = if (uiState.activeTab == "favorites") 0.75f else 1f
                        ),
                ) {
                    Column(
                        modifier = Modifier.fillMaxSize(),
                        horizontalAlignment = Alignment.CenterHorizontally,
                        verticalArrangement = Arrangement.SpaceEvenly
                    ) {
                        Surface(
                            elevation = 8.dp,
                            modifier = Modifier
                                .border(
                                    width = 2.dp,
                                    brush = (Brush.horizontalGradient(
                                        colors = if (uiState.activeTab == "favorites") fullColors else fadedColors,
                                        tileMode = TileMode.Mirror
                                    )),
                                    shape = CircleShape
                                )
                                .size(36.dp)
                                .alpha(if (uiState.activeTab == "favorites") 1f else 0.5f),
                            shape = CircleShape,
                            color = Color(0xFF682300)
                        ) {
                            Box(contentAlignment = Alignment.Center) {
                                Icon(
                                    Icons.Outlined.Favorite,
                                    tint = Color(0xFFd8af84),
                                    modifier = Modifier.size(20.dp),
                                    // modifier = Modifier.background(color = Color(0xFFFFFFFF)),
                                    contentDescription = null
                                )
                            }
                        }
                        Text(
                            "Favorites",
                            modifier = Modifier.alpha(if (uiState.activeTab == "favorites") 1f else 0.5f),
                            color = Color(0xFFd8af84)
                        )
                    }
                }
                //Cooked Button Tab
                Surface(
                    color = Color.Transparent,
                    modifier = Modifier
                        .fillMaxSize()
                        .weight(1f)
                        .clickable(onClick = { profileScreenViewModel.setActiveTab("cooked") })
                        .background(
                            brush = Brush.horizontalGradient(
                                listOf(
                                    Color(0xFF682300),
                                    Color(0xFF682300)
                                )
                            ), alpha = if (uiState.activeTab == "cooked") 0.75f else 1f
                        )
                ) {
                    Column(
                        modifier = Modifier.fillMaxSize(),
                        horizontalAlignment = Alignment.CenterHorizontally,
                        verticalArrangement = Arrangement.SpaceEvenly
                    ) {
                        Surface(
                            elevation = 8.dp,
                            color = Color(0xFF682300),
                            modifier = Modifier
                                .border(
                                    width = 2.dp,
                                    brush = (Brush.horizontalGradient(
                                        colors = if (uiState.activeTab == "cooked") fullColors else fadedColors,
                                        tileMode = TileMode.Mirror
                                    )),
                                    shape = CircleShape
                                )
                                .size(36.dp)
                                .alpha(if (uiState.activeTab == "cooked") 1f else 0.5f),
                            shape = CircleShape,

                            ) {
                            Box(contentAlignment = Alignment.Center) {
                                Image(
                                    painter = painterResource(id = R.drawable.tray),
                                    contentDescription = null,
                                    modifier = Modifier.size(24.dp),
                                    colorFilter = ColorFilter.tint(Color(0xFFd8af84)),
                                )
                            }
                        }
                        Text(
                            "Cooked",
                            color = Color(0xFFd8af84),
                            modifier = Modifier.alpha(if (uiState.activeTab == "cooked") 1f else 0.5f)
                        )
                    }
                }
                Surface(
                    color = Color.Transparent,
                    modifier = Modifier
                        .fillMaxSize()
                        .weight(1f)
                        .clickable(onClick = { profileScreenViewModel.setActiveTab("reviews") })
                        .background(
                            brush = Brush.horizontalGradient(
                                listOf(
                                    Color(0xFF682300),
                                    Color(0xFF682300)
                                )
                            ), alpha = if (uiState.activeTab == "reviews") 0.75f else 1f
                        ),
                ) {
                    Column(
                        modifier = Modifier.fillMaxSize(),
                        horizontalAlignment = Alignment.CenterHorizontally,
                        verticalArrangement = Arrangement.SpaceEvenly
                    ) {

                        //Review button
                        Surface(
                            elevation = 8.dp,
                            modifier = Modifier
                                .border(
                                    width = 2.dp,
                                    brush = (Brush.horizontalGradient(
                                        colors = if (uiState.activeTab == "reviews") fullColors else fadedColors,
                                        tileMode = TileMode.Mirror
                                    )),
                                    shape = CircleShape
                                )
                                .size(36.dp)
                                .alpha(if (uiState.activeTab == "reviews") 1f else 0.5f),
                            shape = CircleShape,
                            color = Color(0xFF682300)
                        ) {
                            Box(contentAlignment = Alignment.Center) {
                                Icon(
                                    Icons.Outlined.Reviews,
                                    tint = Color(0xFFd8af84),
                                    modifier = Modifier.size(20.dp),
                                    // modifier = Modifier.background(color = Color(0xFFFFFFFF)),
                                    contentDescription = null
                                )
                            }
                        }
                        Text(
                            "Reviews",
                            color = Color(0xFFd8af84),
                            modifier = Modifier.alpha(if (uiState.activeTab == "reviews") 1f else 0.5f)
                        )
                    }
                }
            }

            Surface(Modifier.fillMaxSize(), color = Color(0xFFd8af84)) {


            //Favorites List
            androidx.compose.animation.AnimatedVisibility(
                visible = uiState.activeTab == "favorites",
                enter = slideInHorizontally { -it },
                exit = slideOutHorizontally { -it },
            ) {
                Surface(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(bottom = 48.dp),
                    color = Color.Transparent

                ) {
                    LazyColumn {
                        items(favoritesData, key = { it.recipeEntity.recipeName }) {
                            RecipeCard(
                                modifier = Modifier,
                                recipeWithIngredientsAndInstructions = it,
                                currentScreen = "FavoritesTab",
                                onFavoriteClick =
                                {
                                    profileScreenViewModel.triggerRemoveFavoriteAlert(it)
                                },
                                onRemoveClick = {},
                                onCompleteClick = {},
                                onDetailsClick = {

                                    profileScreenViewModel.cancelAnimationStack()

                                    /** main to IO coroutine */
                                    coroutineScope.launch(Dispatchers.Main) {
                                        withContext(Dispatchers.IO) {
                                            profileScreenViewModel.setDetailsScreenTarget(it.recipeEntity.recipeName)

                                        }
                                        onDetailsClick()
                                    }

                                }
                            )

                        }

                        item {
                            Spacer(
                                Modifier
                                    .fillMaxWidth()
                                    .padding(16.dp)
                            )
                        }
                    }
                }
            }

            //Cooked list
            androidx.compose.animation.AnimatedVisibility(
                visible = uiState.activeTab == "cooked",
                enter = if(uiState.previousTab == "favorites") slideInHorizontally { it } else slideInHorizontally { -it },
                exit = if(uiState.activeTab == "favorites") slideOutHorizontally { it } else slideOutHorizontally { -it },
            ) {
                Surface(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(bottom = 48.dp),
                    color = Color.Transparent

                ) {

                    LazyVerticalGrid(
                        columns = GridCells.Fixed(2),
                        modifier = Modifier
                            .padding(top = 0.dp, start = 16.dp, end = 16.dp, bottom = 0.dp),
                        horizontalArrangement = Arrangement.spacedBy(0.dp)
                    ) {
                        items(cookedData.size, key = { it }) { index ->
                            RecipeIcon(
                                recipeWithIngredients = cookedData[index],
                                onDetailsClick = {
                                    profileScreenViewModel.cancelAnimationStack()

                                    /** main to IO coroutine */
                                    coroutineScope.launch(Dispatchers.Main) {
                                        withContext(Dispatchers.IO) {
                                            profileScreenViewModel.setDetailsScreenTarget(cookedData[index].recipeEntity.recipeName)
                                        }
                                        onDetailsClick()
                                    }
                                }
                            )
                        }

//                        item() {
//                            Spacer(
//                                Modifier
//                                    .fillMaxWidth()
//                                    .padding(16.dp)
//                            )
//                        }
                    }
                }
            }
            if (uiState.activeTab == "reviews")
                LazyColumn {

                }
            }

            //Remove Favorite Alert
            Box(Modifier.fillMaxSize()){
                if(uiAlertState.showRemoveFavoriteAlert){
                    AlertDialog(
                        onDismissRequest = {},
                        text = {
                            Text(text = "Are you sure you want to remove " + uiAlertState.recipe.recipeEntity.recipeName +
                                    " from your Favorites?",
                                color = Color(0xFF682300),
                                fontSize = 16.sp,
                                textAlign = TextAlign.Center
                            )
                        },
                        buttons = {
                            Row(
                                modifier = Modifier
                                    .padding(all = 8.dp)
                                    .fillMaxWidth(),
                                horizontalArrangement = Arrangement.SpaceEvenly
                            ) {

                                Button(
                                    modifier = Modifier.wrapContentSize(),
                                    onClick = {
                                        profileScreenViewModel.cancelRemoveAlert()
                                    },
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
                                    Text("Cancel")
                                }

                                Button(
                                    modifier = Modifier.wrapContentSize(),
                                    onClick = {
                                        onRemoveClick(uiAlertState.recipe)
                                        profileScreenViewModel.removeFavorite(uiAlertState.recipe)
                                        profileScreenViewModel.cancelRemoveAlert()
                                    },
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
                                    Text("Yes")
                                }
                            }
                        },
                    )
                }
            }
        }
    }
}


class ProfileScreenViewModelFactory(val application: Application) : ViewModelProvider.Factory
{
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return ProfileScreenViewModel(application) as T
    }
}


//@Composable
//fun Star(alignment: Alignment) {
//    Surface(
//        elevation = 8.dp,
//        modifier = Modifier
//            .border(
//                width = 2.dp,
//                brush = (Brush.horizontalGradient(
//                    colors = listOf(Color(0xFFd8af84), Color(0xFFb15f33)),
//                    tileMode = TileMode.Mirror
//                )),
//                shape = CircleShape
//            )
//            .size(36.dp)
//            .align(alignment),
//        shape = CircleShape,
//        color = Color(0xFF682300)
//    ) {
//        Box(contentAlignment = Alignment.Center) {
//            Icon(
//                Icons.Outlined.Grade,
//                tint = Color(0xFFd8af84),
//                modifier = Modifier.size(20.dp),
//                // modifier = Modifier.background(color = Color(0xFFFFFFFF)),
//                contentDescription = null
//            )
//        }
//    }
//}

@Composable
fun RecipeIcon(
    recipeWithIngredients: RecipeWithIngredients,
    onDetailsClick: () -> Unit
){


    val image: Int = when(recipeWithIngredients.recipeEntity.recipeName){
        "Bagels" -> R.drawable.bagel2
        "Garlic Knots" -> R.drawable.garlic2
        "Cauliflower Walnut Tacos" -> R.drawable.cauliflower
        "Lentil Sweet Potato Curry" -> R.drawable.curry
        "Thai Style Peanut Soup" -> R.drawable.thaisoup
        "Yummy Rice with Marinated Tofu" -> R.drawable.yummyrice
        "Corn Chowder" -> R.drawable.cornchowder
        "Vegan Eggplant Parmesan" -> R.drawable.eggplant
        "Mexican Style Rice" -> R.drawable.mexicanrice
        "Wild Rice Salad" -> R.drawable.wildrice
        "Rice Soup" -> R.drawable.ricesoup
        else -> R.drawable.bagel
    }

    Column(
        Modifier
            .fillMaxWidth(),
        horizontalAlignment = Alignment.CenterHorizontally)
    {
        Surface(
            modifier = Modifier
                .size(120.dp)
                .padding(top = 8.dp, bottom = 4.dp)
                .align(Alignment.CenterHorizontally)
                .clickable(onClick = onDetailsClick),
            shape = RoundedCornerShape(15.dp),
            elevation = 6.dp,

            ){
            AsyncImage(
                model = image,
                contentDescription = null,
                modifier = Modifier.fillMaxSize(),
                contentScale = ContentScale.Crop,
            )
            Column(
                modifier = Modifier
                    .fillMaxSize(),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center
            ){
                Text(
                    text = recipeWithIngredients.recipeEntity.recipeName,
                    fontSize = 20.sp,
                    color = Color(0xFFFFFFFF),
                    textAlign = TextAlign.Center,
                    style = MaterialTheme.typography.h4.copy(
                        shadow = Shadow(
                            color = Color.Black,
                            offset = Offset(4f, 4f),
                            blurRadius = 8f
                        )
                    )
                )
            }
        }

        Spacer(
            Modifier
                .size(2.dp)
                .fillMaxWidth())


        Surface(
            modifier = Modifier
                // .padding(top = 4.dp)
                .wrapContentSize(),
            //               .background(
//                    brush = Brush.horizontalGradient(
//                        colors = listOf(Color(0xFF682300), Color(0xFFb15f33)),
//                        endX = gradientWidth,
//                        tileMode = TileMode.Mirror
//                    ),
//                    shape = RoundedCornerShape((25.dp))
//                ),
            //   .clickable(enabled = !selected) { selected = !selected },
            shape = RoundedCornerShape(25.dp),
            color = Color(0xFF682300),
            elevation = 6.dp,
            //color = Color(0xFF682300),//Color(0xFFd8af84),
            contentColor = Color(0xFFd8af84),
        ){
            Text(
                text = "Times Cooked: ${recipeWithIngredients.recipeEntity.cookedCount}",
                modifier = Modifier
                    .padding(start = 8.dp, end = 8.dp, top = 8.dp, bottom = 8.dp)
                    .background(color = Color.Transparent),
                color = Color(0xFFd8af84),
                fontSize = 16.sp,
                textAlign = TextAlign.Center,

                )
        }

        Spacer(
            Modifier
                .size(16.dp)
                .fillMaxWidth())
    }

}

@Preview
@Composable
fun ProfilePreview(){
    BearRecipeBookAppTheme {
//        ProfileScreen()
    }
}