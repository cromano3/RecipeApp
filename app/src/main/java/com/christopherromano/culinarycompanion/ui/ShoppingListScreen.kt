package com.christopherromano.culinarycompanion.ui

import android.app.Application
import androidx.activity.compose.BackHandler
import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.TweenSpec
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CheckBox
import androidx.compose.material.icons.outlined.Add
import androidx.compose.material.icons.outlined.CheckBoxOutlineBlank
import androidx.compose.material.icons.outlined.Close
import androidx.compose.runtime.*
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.*
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
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

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun ShoppingListScreen(
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

        var scrollDown by remember { mutableStateOf(false) }

        val focusRequester = remember { FocusRequester() }

        BackHandler {
            onSystemBackClick()
        }

        if(filterWasClicked) {
            coroutineScope.launch {

                delay(420)
//                listState.animateScrollBy(value= -5000f, animationSpec = tween(durationMillis = 1000))
                listState.animateScrollToItem(0)
                listState2.animateScrollToItem(0)
                filterWasClicked = false
            }
        }

        if(scrollDown){
            coroutineScope.launch {
                scrollDown = false
                listState.animateScrollToItem(selectedIngredients2.size + uiState.customItems.size)
            }
        }


        Surface(
            modifier = Modifier
                .fillMaxSize()
                .padding(bottom = 48.dp),
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

                    userScrollEnabled = !filterWasClicked,

                    ) {
                    items(selectedIngredients2, key = { it.ingredientName }) {
                        ShoppingListItemWithButton(
                            modifier = Modifier.animateItemPlacement(animationSpec = (TweenSpec(400, delay = 0))),
                            ingredientEntity = it,
                            isWorking = uiState.isWorking,
                            onClickIngredientSelected = { shoppingListScreenViewModel.ingredientSelected(it.ingredientName) },
                            onClickIngredientDeselected = { shoppingListScreenViewModel.ingredientDeselected(it.ingredientName) },
                        )
                    }

                    item{
                        androidx.compose.animation.AnimatedVisibility(
                            visible = (uiState.customItems.isNotEmpty() && !filterWasClicked),
                            enter = fadeIn(TweenSpec(20)),
                            exit = fadeOut(TweenSpec(20)),
                        ) {
                            Text(text = "Custom Items",
                                modifier = Modifier.padding(top = 8.dp, start = 8.dp, bottom = 2.dp),
                                fontSize = 18.sp,
                                fontFamily = Cabin,
                                fontWeight = FontWeight.Bold,
                                color = Color(0xFF682300)
                            )
                        }
                    }
                    item{
                        androidx.compose.animation.AnimatedVisibility(
                            visible = (uiState.customItems.isNotEmpty() && !filterWasClicked),
                            enter = fadeIn(TweenSpec(20)),
                            exit = fadeOut(TweenSpec(20)),
                        ) {
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
                        }
                    }
                    items(uiState.customItems, key = {it.item}) {
                        androidx.compose.animation.AnimatedVisibility(
                            visible = (uiState.customItems.isNotEmpty() && !filterWasClicked),
                            enter = fadeIn(TweenSpec(20)),
                            exit = fadeOut(TweenSpec(20)),
                        ) {
                            CustomShoppingListItem(
                                modifier = Modifier.animateItemPlacement(animationSpec = (TweenSpec(20, delay = 0))),
                                shoppingListCustomItemEntity = it,
                                isWorking = uiState.isWorking,
                                onClickItemSelected = { shoppingListScreenViewModel.customItemSelected(it) },
                                onClickItemDeselected = { shoppingListScreenViewModel.customItemDeselected(it) },
                                onClickDeleteCustomItem = { shoppingListScreenViewModel.deleteCustomItem(it) }
                            )
                        }
                    }


                    item {
                        androidx.compose.animation.AnimatedVisibility(
                            visible = (uiState.customItems.isNotEmpty() && !filterWasClicked),
                            enter = fadeIn(TweenSpec(20)),
                            exit = fadeOut(TweenSpec(20)),
                        ) {
                            ClearCustomItemButton(
                                modifier = Modifier,
                                isWorking = uiState.isWorking,
                                onClickClearAll = { shoppingListScreenViewModel.triggerClearAllCustomItemsAlert() }
                            )
                        }
                    }


                    item {
                        androidx.compose.animation.AnimatedVisibility(
                            visible = (
                                (uiState.customItems.isNotEmpty() && !filterWasClicked ) ||
                                (selectedIngredients2.isNotEmpty() && !filterWasClicked )
                            ),
                            enter = fadeIn(TweenSpec(20)),
                            exit = fadeOut(TweenSpec(20)),
                        ) {
                            AddCustomItemButton(
                                modifier = Modifier,
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
                    userScrollEnabled = !filterWasClicked,
                        ) {

                    items(shoppingListScreenData, key = { it.recipeEntity.recipeName }) {
                        RecipeIconWithButton(
                            modifier = Modifier.animateItemPlacement(animationSpec = (TweenSpec(200, delay = 0))),
                            recipeWithIngredients = it,
                            filterWasClicked = filterWasClicked,
                            isWorking = uiState.isWorking,
                            isClickable = shoppingListScreenData.size != 1,
                            onFilterClick = {shoppingListScreenViewModel.filterBy(it); filterWasClicked = !filterWasClicked},
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
                            contentDescription = null
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
                                    modifier = Modifier.focusRequester(focusRequester),
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
                                    shoppingListScreenViewModel.addCustomItem()
                                    scrollDown = true
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
    modifier: Modifier,
    recipeWithIngredients: RecipeWithIngredients,
    isWorking: Boolean,
    filterWasClicked: Boolean,
    isClickable: Boolean,
    onFilterClick: () -> Unit,
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
        animationSpec = tween(
            durationMillis = 150,
            delayMillis = 0,
            easing = LinearEasing,
        )
    )

    Column(
        modifier
            .fillMaxWidth()
            .alpha(alphaAnim),
        horizontalAlignment = Alignment.CenterHorizontally)
    {
        Surface(
            modifier = Modifier
                .size(120.dp)
                .padding(top = 8.dp, bottom = 4.dp)
                .align(Alignment.CenterHorizontally)
                .border(
                    width = 2.dp,
                    brush = (Brush.horizontalGradient(
                        startX = -10f,
                        colors = listOf(Color(0xFFb15f33), Color(0xFFb15f33)),
                        tileMode = TileMode.Mirror
                    )),
                    shape = RoundedCornerShape(15.dp)
                )
                .clickable(
                    enabled = !isWorking && isClickable && !filterWasClicked,
                    onClick = onFilterClick
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
                .wrapContentSize()
                .border(
                    width = 2.dp,
                    brush = (Brush.horizontalGradient(
                        startX = -10f,
                        colors = listOf(Color(0xFFb15f33), Color(0xFFb15f33)),
                        tileMode = TileMode.Mirror
                    )),
                    shape = RoundedCornerShape(25.dp)
                )
                .clickable(enabled = !isWorking && !filterWasClicked) { onDetailsClick() },
            shape = RoundedCornerShape(25.dp),
            color = Color(0xFF682300),
            elevation = 6.dp,
            contentColor = Color(0xFFd8af84),
        ){
            Text(
                text = "Details",
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
                .size(20.dp)
                .fillMaxWidth())
    }

}

@Composable
fun ShoppingListItemWithButton(
    modifier: Modifier,
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
        modifier = modifier
            .padding(start = 8.dp, top = 8.dp)
            .width(240.dp)
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
                    .alpha(alphaLevel)
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
                    .padding(start = 2.dp, bottom = 2.dp)
                    .align(Alignment.CenterVertically)
                    .alpha(alphaLevel),
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
                        .alpha(alphaLevel),
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
    modifier: Modifier,
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
        modifier = modifier
            .padding(start = 8.dp, top = 8.dp)
            .width(240.dp)
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
                        contentDescription = null
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
                    .alpha(alphaLevel)
            )
            Text(
                modifier = Modifier
                    .padding(start = 4.dp)
                    .align(Alignment.CenterVertically)
                    .alpha(alphaLevel),
                text = shoppingListCustomItemEntity.item,
                fontSize = 16.sp
            )
        }
    }
}

@Composable
fun AddCustomItemButton(
    modifier: Modifier,
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
            .padding(start = 8.dp, top = 8.dp)
            .width(240.dp)
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
            .padding(start = 8.dp, top = 8.dp)
            .width(240.dp)
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
