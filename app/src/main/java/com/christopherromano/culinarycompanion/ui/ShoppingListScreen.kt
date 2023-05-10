package com.christopherromano.culinarycompanion.ui

import android.app.Application
import androidx.activity.compose.BackHandler
import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.defaultMinSize
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.AlertDialog
import androidx.compose.material.Button
import androidx.compose.material.ButtonDefaults
import androidx.compose.material.FloatingActionButton
import androidx.compose.material.FloatingActionButtonDefaults
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.material.TextField
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CheckBox
import androidx.compose.material.icons.outlined.Add
import androidx.compose.material.icons.outlined.CheckBoxOutlineBlank
import androidx.compose.material.icons.outlined.Close
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.graphics.Shadow
import androidx.compose.ui.graphics.TileMode
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewmodel.compose.LocalViewModelStoreOwner
import androidx.lifecycle.viewmodel.compose.viewModel
import coil.compose.AsyncImage
import com.christopherromano.culinarycompanion.R
import com.christopherromano.culinarycompanion.data.annotatedstrings.addRecipeOrCustomItemAnoString
import com.christopherromano.culinarycompanion.data.entity.ShoppingListCustomItemsEntity
import com.christopherromano.culinarycompanion.datamodel.IngredientsWithQuantities
import com.christopherromano.culinarycompanion.datamodel.RecipeWithIngredients
import com.christopherromano.culinarycompanion.ui.components.BasicAlert
import com.christopherromano.culinarycompanion.ui.components.CancelAlertButton
import com.christopherromano.culinarycompanion.ui.components.ConfirmAlertButton
import com.christopherromano.culinarycompanion.ui.theme.Cabin
import com.christopherromano.culinarycompanion.viewmodel.ShoppingListScreenViewModel
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

//@OptIn(ExperimentalFoundationApi::class)
@Composable
fun ShoppingListScreen(
    isCompact: Boolean,
    onDetailsClick: (String) -> Unit,
    onSystemBackClick: () -> Unit,
    onAddRecipeClick: () -> Unit,
) {
    val owner = LocalViewModelStoreOwner.current

    owner?.let { viewModelStoreOwner ->
        val shoppingListScreenViewModel: ShoppingListScreenViewModel = viewModel(
            viewModelStoreOwner,
            "ShoppingListScreenViewModel",
            ShoppingListScreenViewModelFactory(
                LocalContext.current.applicationContext
                        as Application,
            )
        )

        val shoppingListScreenData by shoppingListScreenViewModel.shoppingListScreenData.observeAsState(listOf())
        val selectedIngredients by shoppingListScreenViewModel.selectedIngredients.observeAsState(listOf())
        val selectedIngredients2 by shoppingListScreenViewModel.selectedIngredients2.observeAsState(listOf())

        var filterWasClicked by remember { mutableStateOf(false) }

        val uiState by shoppingListScreenViewModel.shoppingScreenUiState.collectAsState()
        val uiAlertState by shoppingListScreenViewModel.uiAlertState.collectAsState()

        val coroutineScope = rememberCoroutineScope()

        val listState = rememberLazyListState()
        val listState2 = rememberLazyListState()

        val scrollState = rememberScrollState()

        val itemHeight = with(LocalDensity.current) { if(isCompact) 190.dp.toPx() else 270.dp.toPx() }.toInt()



        var scrollDown by remember { mutableStateOf(false) }

        val focusRequester = remember { FocusRequester() }

        BackHandler {
            onSystemBackClick()
        }

//        if(filterWasClicked) {
//            coroutineScope.launch {
//
//                delay(420)
////                listState.animateScrollBy(value= -5000f, animationSpec = tween(durationMillis = 1000))
//                listState.animateScrollToItem(0)
//                listState2.animateScrollToItem(0)
//                filterWasClicked = false
//            }
//        }

//        if(uiState.triggerScroll) {
//            coroutineScope.launch {
//                listState.animateScrollToItem(0)
//                shoppingListScreenViewModel.stopScroll()
//            }
//        }

        LaunchedEffect(selectedIngredients2){
//            scrollState.scrollTo(0)
            if(filterWasClicked) {
                filterWasClicked = false
                listState.scrollToItem(0)
            }
        }

        if(scrollDown){
            coroutineScope.launch {
                scrollDown = false
//                scrollState.scrollTo(scrollState.maxValue)
                listState.animateScrollToItem(selectedIngredients2.size + uiState.customItems.size)
            }
        }


        Surface(
            modifier = Modifier
                .fillMaxSize()
                .padding(bottom = 56.dp),
            color = Color(0xFFd8af84)
        ) {

            Row(
                Modifier
                    .fillMaxSize()
            )
            {
                LazyColumn(
                    state = listState,
                    modifier = Modifier
                        .weight(0.60f),
//                        .verticalScroll(scrollState, enabled = !uiState.isWorking),
                    horizontalAlignment = Alignment.Start,

//                    userScrollEnabled = !uiState.isWorking,

                    ) {
                    items(selectedIngredients2, key = { it.ingredientName }) {
//                    selectedIngredients2.forEach {it ->
                        ShoppingListItemWithButton(
                            isCompact = isCompact,
                            ingredientEntity = it,
                            isWorking = uiState.isWorking,
                            onClickIngredientSelected = { shoppingListScreenViewModel.ingredientSelected(it.ingredientName) },
                            onClickIngredientDeselected = { shoppingListScreenViewModel.ingredientDeselected(it.ingredientName) },
                        )
                    }


                    if(uiState.customItems.isNotEmpty()) {
                        item {
//                    androidx.compose.animation.AnimatedVisibility(
//                        visible = (uiState.customItems.isNotEmpty() && !uiState.isWorking),
//                        enter = fadeIn(TweenSpec(20)),
//                        exit = fadeOut(TweenSpec(20)),
//                    ) {
                            Text(
                                text = "Custom Items",
                                modifier = Modifier.padding(
                                    top = 8.dp,
                                    start = 8.dp,
                                    bottom = 2.dp
                                ),
                                fontSize = 18.sp,
                                fontFamily = Cabin,
                                fontWeight = FontWeight.Bold,
                                color = Color(0xFF682300)
                            )
                        }
                    }




                    if(uiState.customItems.isNotEmpty()){
//                    androidx.compose.animation.AnimatedVisibility(
//                        visible = (uiState.customItems.isNotEmpty() && !uiState.isWorking),
//                        enter = fadeIn(TweenSpec(20)),
//                        exit = fadeOut(TweenSpec(20)),
//                    ) {
                        item {
                            Spacer(
                                Modifier
                                    .height(2.dp)
                                    .fillMaxWidth()
                                    .padding(end = 8.dp)
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
                        }
                    }


                    if(uiState.customItems.isNotEmpty()){
                        items(uiState.customItems, key = {it.item}) {
//                        uiState.customItems.forEach {it ->
//                        androidx.compose.animation.AnimatedVisibility(
//                            visible = (uiState.customItems.isNotEmpty() && !uiState.isWorking),
//                            enter = fadeIn(TweenSpec(20)),
//                            exit = fadeOut(TweenSpec(20)),
//                        ) {
                            CustomShoppingListItem(
//                                modifier = Modifier.animateItemPlacement(animationSpec = (TweenSpec(20, delay = 0))),
                                isCompact = isCompact,
                                shoppingListCustomItemEntity = it,
                                isWorking = uiState.isWorking,
                                onClickItemSelected = { shoppingListScreenViewModel.customItemSelected(it) },
                                onClickItemDeselected = { shoppingListScreenViewModel.customItemDeselected(it) },
                                onClickDeleteCustomItem = { shoppingListScreenViewModel.deleteCustomItem(it) }
                            )
                        }
                    }


                    if(uiState.customItems.isNotEmpty()){
                        item {

//                        androidx.compose.animation.AnimatedVisibility(
//                            visible = (uiState.customItems.isNotEmpty() && !uiState.isWorking),
//                            enter = fadeIn(TweenSpec(20)),
//                            exit = fadeOut(TweenSpec(20)),
//                        ) {
                            ClearCustomItemButton(
                                modifier = Modifier,
                                isCompact = isCompact,
                                isWorking = uiState.isWorking,
                                onClickClearAll = { shoppingListScreenViewModel.triggerClearAllCustomItemsAlert() }
                            )
                        }
                    }



                    if(uiState.customItems.isNotEmpty() || selectedIngredients2.isNotEmpty()){
                        item {
//                        androidx.compose.animation.AnimatedVisibility(
//                            visible = (
//                                (uiState.customItems.isNotEmpty() && !uiState.isWorking ) ||
//                                (selectedIngredients2.isNotEmpty() && !uiState.isWorking )
//                            ),
//                            enter = fadeIn(TweenSpec(20)),
//                            exit = fadeOut(TweenSpec(20)),
//                        ) {
                            AddCustomItemButton(
                                modifier = Modifier,
                                isCompact = isCompact,
                                isWorking = uiState.isWorking,
                                onClickAddItem = { shoppingListScreenViewModel.triggerAddCustomItemAlert() }
                            )
                        }
                    }



                    item{ Spacer(
                        Modifier
                            .size(20.dp)
                            .fillMaxWidth()) }
                }
                LazyColumn(
                    modifier = Modifier
                        .weight(0.40f),
                    state = listState2,
                    horizontalAlignment = Alignment.CenterHorizontally,
                    userScrollEnabled = !uiState.isWorking,
                        ) {

                    items(shoppingListScreenData, key = { it.recipeEntity.recipeName }) {
                        RecipeIconWithButton(
                            isCompact = isCompact,
                            recipeWithIngredients = it,
//                            filterWasClicked = filterWasClicked,
                            isWorking = uiState.isWorking,
                            isClickable = shoppingListScreenData.size != 1,
                            onFilterClick = {
                                coroutineScope.launch {

                                    filterWasClicked = true

                                    shoppingListScreenViewModel.filterBy(it)

                                    val lastItemInfo = listState2.layoutInfo.visibleItemsInfo.lastOrNull()
                                    val firstItemInfo = listState2.layoutInfo.visibleItemsInfo.firstOrNull()

                                    if(lastItemInfo != null && lastItemInfo.index == shoppingListScreenData.indexOf(it)){
                                        if(lastItemInfo.offset + lastItemInfo.size > listState2.layoutInfo.viewportEndOffset){
                                            val viewportHeight = listState2.layoutInfo.viewportEndOffset
                                            val itemsThatFit = viewportHeight / itemHeight
                                            val partialItemHeight = viewportHeight % itemHeight
                                            val scrollToIndex = maxOf(0, (lastItemInfo.index + 1) - (itemsThatFit  + (if (partialItemHeight > 0) 1 else 0)))
                                            listState2.animateScrollToItem(scrollToIndex, if (partialItemHeight > 0) itemHeight - partialItemHeight else 0)
                                        }
                                    }
                                    else if(firstItemInfo != null && firstItemInfo.index == shoppingListScreenData.indexOf(it)){
                                        listState2.animateScrollToItem(firstItemInfo.index)
                                    }

                                }
                            },
                            onClearFilterClick = {
                                coroutineScope.launch {
                                    filterWasClicked = true

                                    shoppingListScreenViewModel.clearFilter()
                                }
                            },

                            onDetailsClick = { onDetailsClick(it.recipeEntity.recipeName) }
                        )
                    }

                }
            }
            if (selectedIngredients2.isEmpty() && uiState.customItems.isEmpty()) {
                Box(Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                    Image(
                        painter = painterResource(id = R.drawable.shoppingscreenempty),
                        contentDescription = null,
                        alignment = Alignment.Center,
                        alpha = .5f,
                        colorFilter = ColorFilter.tint(Color(0xFF682300))
                    )
                }
                Box(Modifier.fillMaxSize(), contentAlignment =  Alignment.BottomEnd) {
                    FloatingActionButton(
                        onClick = { shoppingListScreenViewModel.triggerAddRecipeOrCustomItemAlert() },
                        elevation = FloatingActionButtonDefaults.elevation(8.dp),
                        modifier = Modifier
                            .padding(bottom = 70.dp, end = 24.dp)
                            .border(
                                width = 2.dp,
                                brush = (Brush.horizontalGradient(
                                    colors = listOf(
                                        Color(0xFFd8af84),
                                        Color(0xFFb15f33),

                                        ),
                                    tileMode = TileMode.Mirror
                                )),
                                shape = CircleShape
                            )
                            .align(Alignment.BottomEnd)
                            .size(56.dp)
                            .background(color = Color.Transparent),
                        shape = CircleShape,
                        backgroundColor = Color(0xFF682300)
                    ) {
                        Icon(
                            imageVector = Icons.Outlined.Add,
                            tint = Color(0xFFd8af84),
                            modifier = Modifier.size(28.dp),
                            contentDescription = "Tap to add a recipe to your " +
                                    "shopping list, or add a custom item to your shopping list. " +
                                    "Your shopping list is currently empty."
                        )
                    }
                }
            }

            Box(Modifier.fillMaxSize()){
                //add recipe or custom item
                if(uiAlertState.showAddRecipeOrCustomItemAlert){
                    AlertDialog(
                        onDismissRequest = {shoppingListScreenViewModel.cancelAddRecipeOrCustomItemAlert()},
                        text = {
                            Text(
                                text = addRecipeOrCustomItemAnoString(),
                                color = Color(0xFF682300),
                                fontSize = 16.sp,
                                textAlign = TextAlign.Center
                            )
                        },
                        buttons = {
                            Column(
                                modifier = Modifier
                                    .padding(all = 8.dp)
                                    .fillMaxWidth(),
                            ) {
                                Button(
                                    modifier = Modifier.fillMaxWidth(),
                                    onClick = {
                                        shoppingListScreenViewModel.cancelAddRecipeOrCustomItemAlert()
                                        shoppingListScreenViewModel.addTutorialAlert()
                                        onAddRecipeClick()
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
                                    colors = ButtonDefaults.buttonColors(backgroundColor = Color(0xFF682300), contentColor = Color(0xFFd8af84))
                                ) {
                                    Text("Add Recipe")
                                }

                                Button(
                                    modifier = Modifier.fillMaxWidth(),
                                    onClick = {
                                        shoppingListScreenViewModel.cancelAddRecipeOrCustomItemAlert()
                                        shoppingListScreenViewModel.triggerAddCustomItemAlert()
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
                                    colors = ButtonDefaults.buttonColors(backgroundColor = Color(0xFF682300), contentColor = Color(0xFFd8af84))
                                ) {
                                    Text("Add Custom Item")
                                }
                            }
                        }
                    )
                }
                else if(uiAlertState.showAddCustomItemAlert){

                    LaunchedEffect(Unit) {
                        delay(200)
                        focusRequester.requestFocus()
                    }

                    AlertDialog(
                        onDismissRequest = {
                            shoppingListScreenViewModel.cancelAddCustomItemAlert()
                        },
                        title = {
                            Text(text = "Enter item: ", color = Color(0xFF682300))
                        },
                        text = {
                            Column {
                                TextField(
                                    value = uiAlertState.inputText,
                                    onValueChange = { shoppingListScreenViewModel.updateInputText(it) },
                                    modifier = Modifier
                                        .focusRequester(focusRequester)
                                        .fillMaxWidth(),
                                    singleLine = true,
                                )
                                Text(
                                    text = "${uiAlertState.inputText.text.length}/20",
                                    color = if(uiAlertState.inputText.text.length > 20) Color.Red else Color.Black
                                )
                            }
                        },
                        buttons = {
                            Row(
                                modifier = Modifier
                                    .padding(all = 8.dp)
                                    .fillMaxWidth(),
                                horizontalArrangement = Arrangement.SpaceEvenly
                            ) {

                                CancelAlertButton(buttonText = "Cancel") {
                                    shoppingListScreenViewModel.cancelAddCustomItemAlert()
                                }
                                ConfirmAlertButton(buttonText = "Confirm") {
                                    coroutineScope.launch {
                                        shoppingListScreenViewModel.addCustomItem()
                                        scrollDown = true
                                    }

                                }
                            }
                        }
                    )
                }
                else if(uiAlertState.showClearAllCustomItemsAlert){
                    BasicAlert(
                        text = "Clear all custom items?",
                        confirmButtonText = "Yes",
                        cancelButtonText = "Cancel",
                        onConfirmClick = { shoppingListScreenViewModel.clearAllCustomItems() },
                        onCancelClick = { shoppingListScreenViewModel.cancelClearAllCustomItemsAlert() },
                        onDismiss = { shoppingListScreenViewModel.cancelClearAllCustomItemsAlert() })

                }
            }
        }

    }
}


@Composable
fun RecipeIconWithButton(
//    modifier: Modifier,
    isCompact: Boolean,
    recipeWithIngredients: RecipeWithIngredients,
    isWorking: Boolean,
//    filterWasClicked: Boolean,
    isClickable: Boolean,
    onFilterClick: () -> Unit,
    onClearFilterClick: () -> Unit,
    onDetailsClick: () -> Unit
){

    val image = when(recipeWithIngredients.recipeEntity.recipeName){
        "Bagels" -> R.drawable.bagels
        "Garlic Knots" -> R.drawable.garlic_knots
        "Cauliflower Walnut Tacos" -> R.drawable.cauliflower_tacos
        "Lentil Sweet Potato Curry" -> R.drawable.lentil_curry
        "Thai Style Peanut Soup" -> R.drawable.thai_soup
        "Yummy Rice with Marinated Tofu" -> R.drawable.yummy_rice
        "Corn Chowder" -> R.drawable.corn_chowder
        "Vegan Eggplant Parmesan" -> R.drawable.eggplant
//        "Mexican Style Rice" -> R.drawable.bagel
        "Wild Rice Salad" -> R.drawable.wild_rice
        "Rice Soup" -> R.drawable.rice_soup
        "Miso Soup" -> R.drawable.miso_soup
        "Sweet Potato Tortilla" -> R.drawable.sweet_potato_tortilla
        "Tumbet" -> R.drawable.tumbet
        "Chinese Eggplant" -> R.drawable.chinese_eggplant
        "Coca De Prebes" -> R.drawable.coca
        "Banana Walnut Pancakes" -> R.drawable.pancakes
        "Huevos Rotos" -> R.drawable.huevos
        else -> R.drawable.bagel
    }

    val alphaAnim: Float by animateFloatAsState(
        targetValue =
        if (
            recipeWithIngredients.recipeEntity.isShoppingFilter == 1 ||
            recipeWithIngredients.recipeEntity.isShoppingFilter == 2
        )
            1f
        else
            0.30f,
        animationSpec = tween(durationMillis = 150, delayMillis = 0, easing = LinearEasing,)
    )

    val (borderColors, mainColor, contentColor) = if (recipeWithIngredients.recipeEntity.isShoppingFilter == 2) {
        Triple(listOf(Color(0xFF682300), Color(0xFF682300)), Color(0xFFd8af84), Color(0xFF682300))
    } else {
        Triple(listOf(Color(0xFFb15f33), Color(0xFFb15f33)), Color(0xFF682300), Color(0xFFd8af84))
    }

    Column(
        Modifier
            .wrapContentSize()
            .alpha(alphaAnim),
        horizontalAlignment = Alignment.CenterHorizontally)
    {
        Surface(
            modifier = Modifier
                .padding(top = 8.dp, bottom = 4.dp, start = 8.dp, end = 8.dp)
                .size(if (isCompact) 120.dp else 200.dp)
                .align(Alignment.CenterHorizontally)
                .border(
                    width = if (isCompact) 2.dp else 4.dp,
                    brush = (Brush.horizontalGradient(
                        startX = -10f,
                        colors = listOf(Color(0xFFb15f33), Color(0xFFb15f33)),
                        tileMode = TileMode.Mirror
                    )),
                    shape = RoundedCornerShape(15.dp)
                )
                .clickable(
                    enabled = !isWorking && recipeWithIngredients.recipeEntity.isShoppingFilter != 0,
                    onClick = onDetailsClick
                ),
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
                modifier = Modifier.fillMaxSize(),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center
            ){
                Text(
                    text = recipeWithIngredients.recipeEntity.recipeName,
                    modifier = Modifier.padding(start = 2.dp, end = 2.dp),
                    fontSize = if(isCompact) 18.sp else 22.sp,
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

        if(isClickable) {
            Spacer(Modifier.size(2.dp))


            Surface(
                modifier = Modifier
                    .wrapContentWidth()
                    .height(36.dp)
                    .border(
                        width = 2.dp,
                        brush = (Brush.horizontalGradient(
                            startX = -10f,
                            colors = borderColors,
                            tileMode = TileMode.Mirror
                        )),
                        shape = RoundedCornerShape(25.dp)
                    )
                    .clickable(enabled = !isWorking && recipeWithIngredients.recipeEntity.isShoppingFilter != 0)
                    {
                        if (recipeWithIngredients.recipeEntity.isShoppingFilter == 2) {
                            onClearFilterClick()
                        } else {
                            onFilterClick()
                        }

                    },
                shape = RoundedCornerShape(25.dp),
                color = mainColor,
                elevation = 6.dp,
                contentColor = contentColor,
            ) {
                Text(
                    text = if (recipeWithIngredients.recipeEntity.isShoppingFilter == 2) "Clear Filter" else "Filter",
                    modifier = Modifier
                        .padding(start = 8.dp, end = 8.dp, top = 8.dp, bottom = 8.dp)
                        .background(color = Color.Transparent),
                    color = contentColor,
                    fontSize = 16.sp,
                    textAlign = TextAlign.Center,
                )
            }

            Spacer(
                Modifier
                    .size(20.dp)
                    .fillMaxWidth()
            )
        }
    }

}

@Composable
fun ShoppingListItemWithButton(
//    modifier: Modifier,
    isCompact: Boolean,
    ingredientEntity: IngredientsWithQuantities,
    isWorking: Boolean,
    onClickIngredientSelected: () -> Unit,
    onClickIngredientDeselected: () -> Unit
){

    val selected: Boolean

    val myIcon: ImageVector
    val checkBoxBackgroundColor: Color
    var alphaLevel : Float


    if(ingredientEntity.quantityOwned == ingredientEntity.quantityNeeded
        && ingredientEntity.quantityNeeded > 0){

        selected = true
        myIcon = Icons.Filled.CheckBox
        checkBoxBackgroundColor = Color(0xFFd8af84)
        alphaLevel = 0.55f


    }
    else{

        selected = false
        myIcon = Icons.Outlined.CheckBoxOutlineBlank
        checkBoxBackgroundColor = Color(0xFFd8af84)
        alphaLevel = 1f

    }

    if (ingredientEntity.isShown == 0) alphaLevel = 0.30f


    val alphaAnim: Float by animateFloatAsState(
        targetValue = alphaLevel,
        animationSpec = tween(
            durationMillis = 150,
            delayMillis = 0,
            easing = LinearEasing,
        )
    )


    Surface(
        modifier = Modifier
            .padding(start = 8.dp, top = 8.dp, end = 8.dp)
            .width(if (isCompact) 240.dp else 300.dp)
            .defaultMinSize(minHeight = 36.dp)
//            .wrapContentHeight()
            .alpha(alphaAnim)
            .border(
                width = 2.dp,
                brush = (Brush.horizontalGradient(
                    startX = -10f,
                    colors = listOf(Color(0xFF682300), Color(0xFF682300)),
                    tileMode = TileMode.Mirror
                )),
                shape = RoundedCornerShape(25.dp)
            )
            .clickable(
                enabled = !isWorking && (ingredientEntity.isShown == 1),
                onClick = if (selected) onClickIngredientDeselected else onClickIngredientSelected,
            ),
        shape = RoundedCornerShape(25.dp),
        color = Color(0xFF81340A),
        elevation = 4.dp,
        contentColor = Color(0xFFd8af84),
    ){
        Row(
            Modifier
                .fillMaxSize()
                .padding(top = 1.dp, bottom = 1.dp),
            horizontalArrangement = Arrangement.Start
        )
        {
            Icon(
                imageVector = myIcon,
                tint = checkBoxBackgroundColor,
                contentDescription = null,
                modifier = Modifier
                    .padding(start = 6.dp, top = 2.dp, end = 2.dp, bottom = 2.dp)
                    .size(28.dp)
                    .align(Alignment.CenterVertically)
                    .alpha(alphaAnim)
            )
            var myText = ingredientEntity.ingredientName

            if(ingredientEntity.quantity.isNotBlank()) {
                myText += " ("
                myText += ingredientEntity.quantity.replace("/", "\u002F")
                myText += "\u00A0"
                myText += ingredientEntity.unit
                myText += ")"
            }

            Text(
                modifier = Modifier
                    .padding(start = 2.dp, bottom = 2.dp, end = 2.dp)
                    .align(Alignment.CenterVertically)
                    .alpha(alphaAnim),
                text = myText,
                fontSize = 16.sp,
//                fontWeight = FontWeight.Bold
            )
            /*
                Show (2) or more if the number needed is more than 1
                AND there is no quantity/unit associated with the item
             */
            if(ingredientEntity.quantityNeeded > 1 && ingredientEntity.quantity.isBlank()) {
                Text(
                    modifier = Modifier
                        .align(Alignment.CenterVertically)
                        .alpha(alphaAnim),
                    text = " (${ingredientEntity.quantityNeeded})",
                    fontSize = 16.sp,
//                    fontWeight = FontWeight.Bold
                )
            }
        }
    }
}

@Composable
fun CustomShoppingListItem(
//    modifier: Modifier,
    isCompact: Boolean,
    shoppingListCustomItemEntity: ShoppingListCustomItemsEntity,
    isWorking: Boolean,
    onClickItemSelected: () -> Unit,
    onClickItemDeselected: () -> Unit,
    onClickDeleteCustomItem: () -> Unit,
){

    val selected: Boolean


    val myIcon: ImageVector
    val checkBoxBackgroundColor: Color
    var alphaLevel : Float


    if(shoppingListCustomItemEntity.selected == 1){

        selected = true
        myIcon = Icons.Filled.CheckBox
        checkBoxBackgroundColor = Color(0xFFd8af84)
        alphaLevel = 0.55f


    }
    else{

        selected = false
        myIcon = Icons.Outlined.CheckBoxOutlineBlank
        checkBoxBackgroundColor = Color(0xFFd8af84)
        alphaLevel = 1f

    }


    val alphaAnim: Float by animateFloatAsState(
        targetValue = alphaLevel,
        animationSpec = tween(
            durationMillis = 150,
            delayMillis = 0,
            easing = LinearEasing,
        )
    )


    Surface(
        modifier = Modifier
            .padding(start = 8.dp, top = 8.dp, end = 8.dp)
            .width(if (isCompact) 240.dp else 300.dp)
            .height(36.dp)
            .alpha(alphaAnim)
            .border(
                width = 2.dp,
                brush = (Brush.horizontalGradient(
                    startX = -10f,
                    colors = listOf(Color(0xFF682300), Color(0xFF682300)),
                    tileMode = TileMode.Mirror
                )),
                shape = RoundedCornerShape(25.dp)
            )
            .clickable(
                enabled = (!isWorking),
                onClick = if (selected) onClickItemDeselected else onClickItemSelected,
            ),
        shape = RoundedCornerShape(25.dp),
        color = Color(0xFF81340A),
        elevation = 4.dp,
        contentColor = Color(0xFFd8af84),
    ){

        /*if selected then show X */

        if (selected){
            Box{
                IconButton(
                    modifier = Modifier
                        .align(Alignment.CenterEnd)
                        .size(36.dp),
                    onClick = onClickDeleteCustomItem,
                    enabled = !isWorking
                ){
                    Icon(
                        modifier = Modifier,
                        imageVector = Icons.Outlined.Close,
                        tint = Color(0xFFFFFFFF),
                        contentDescription = "Delete this item from the shopping list."
                    )
                }
            }
        }
        Row(
            Modifier.fillMaxSize(),
            horizontalArrangement = Arrangement.Start
        )
        {
            Icon(
                imageVector = myIcon,
                tint = checkBoxBackgroundColor,
                contentDescription = null,
                modifier = Modifier
                    .padding(start = 6.dp, top = 2.dp, end = 2.dp, bottom = 2.dp)
                    .size(28.dp)
                    .align(Alignment.CenterVertically)
                    .alpha(alphaAnim)
            )
            Text(
                modifier = Modifier
                    .padding(start = 4.dp)
                    .align(Alignment.CenterVertically)
                    .alpha(alphaAnim),
                text = shoppingListCustomItemEntity.item,
                fontSize = 16.sp
            )
        }
    }
}

@Composable
fun AddCustomItemButton(
    modifier: Modifier,
    isCompact: Boolean,
    isWorking: Boolean,
    onClickAddItem: () -> Unit,
){

    val alphaLevel : Float = 1f

    val alphaAnim: Float by animateFloatAsState(
        targetValue = alphaLevel,
        animationSpec = tween(
            durationMillis = 150,
            delayMillis = 0,
            easing = LinearEasing,
        )
    )


    Surface(
        modifier = modifier
            .padding(start = 8.dp, top = 8.dp, end = 8.dp)
            .width(if (isCompact) 240.dp else 300.dp)
            .height(36.dp)
            .alpha(alphaAnim)
            .border(
                width = 2.dp,
                brush = (Brush.horizontalGradient(
                    startX = -10f,
                    colors = listOf(Color(0xFFb15f33), Color(0xFFb15f33)),
                    tileMode = TileMode.Mirror
                )),
                shape = RoundedCornerShape(25.dp)
            )
            .clickable(
                enabled = !isWorking,
                onClick = onClickAddItem,
            ),
        shape = RoundedCornerShape(25.dp),
        color = Color(0xFF682300),
        elevation = 4.dp,
        contentColor = Color(0xFFd8af84),
    ){

        Row(
            Modifier.fillMaxSize(),
            horizontalArrangement = Arrangement.Center
        )
        {
            Text(
                modifier = Modifier
                    // .weight(1f)
                    .padding(start = 4.dp)
                    .align(Alignment.CenterVertically)
                    .alpha(alphaAnim),
                text = "Add Item",
                fontSize = 16.sp
            )
        }
    }
}

@Composable
fun ClearCustomItemButton(
    modifier: Modifier,
    isCompact: Boolean,
    isWorking: Boolean,
    onClickClearAll: () -> Unit,
){

    val alphaLevel : Float = 1f

    val alphaAnim: Float by animateFloatAsState(
        targetValue = alphaLevel,
        animationSpec = tween(
            durationMillis = 150,
            delayMillis = 0,
            easing = LinearEasing,
        )
    )


    Surface(
        modifier = modifier
            .padding(start = 8.dp, top = 8.dp, end = 8.dp)
            .width(if (isCompact) 240.dp else 300.dp)
            .height(36.dp)
            .alpha(alphaAnim)
            .border(
                width = 2.dp,
                brush = (Brush.horizontalGradient(
                    startX = -10f,
                    colors = listOf(Color(0xFF682300), Color(0xFF682300)),
                    tileMode = TileMode.Mirror
                )),
                shape = RoundedCornerShape(25.dp)
            )
            .clickable(
                enabled = !isWorking,
                onClick = onClickClearAll,
            ),
        shape = RoundedCornerShape(25.dp),
        color = Color(0xFFd8af84),
        elevation = 4.dp,
        contentColor = Color(0xFF682300),
    ){

        Row(
            Modifier.fillMaxSize(),
            horizontalArrangement = Arrangement.Center
        )
        {
            Text(
                modifier = Modifier
                    // .weight(1f)
                    .padding(start = 4.dp)
                    .align(Alignment.CenterVertically)
                    .alpha(alphaAnim),
                text = "Clear Custom Items",
                fontSize = 16.sp
            )
        }
    }
}


class ShoppingListScreenViewModelFactory(
    val application: Application,
) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return ShoppingListScreenViewModel(
            application,
        ) as T
    }

}
