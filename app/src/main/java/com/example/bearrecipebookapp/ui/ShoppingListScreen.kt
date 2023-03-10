package com.example.bearrecipebookapp.ui

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
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewmodel.compose.LocalViewModelStoreOwner
import androidx.lifecycle.viewmodel.compose.viewModel
import coil.compose.AsyncImage
import com.example.bearrecipebookapp.R
import com.example.bearrecipebookapp.data.annotatedstrings.addRecipeOrCustomItemAnoString
import com.example.bearrecipebookapp.data.entity.ShoppingListCustomItemsEntity
import com.example.bearrecipebookapp.datamodel.IngredientsWithQuantities
import com.example.bearrecipebookapp.datamodel.RecipeWithIngredients
import com.example.bearrecipebookapp.ui.components.BasicAlert
import com.example.bearrecipebookapp.ui.components.CancelAlertButton
import com.example.bearrecipebookapp.ui.components.ConfirmAlertButton
import com.example.bearrecipebookapp.ui.theme.Cabin
import com.example.bearrecipebookapp.viewmodel.ShoppingListScreenViewModel
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
//        val customIngredients by shoppingListScreenViewModel.customIngredients.observeAsState(listOf())

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
//            LaunchedEffect(Unit) {
            coroutineScope.launch {

                delay(420)
                listState.animateScrollToItem(0)
                listState2.animateScrollToItem(0)
                filterWasClicked = false
            }
//            filterWasClicked = false
        }

//        if(uiAlertState.newCustomItemAddedSuccessfully){
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
            color = Color(0xFFd8af84)//Color(0xFFb15f33), //Color(0xFFd8af84)
        ) {

            Row(
                Modifier
                    .fillMaxSize()
            )
            {
                LazyColumn(
                    state = listState,
                    modifier = Modifier
//                        .fillMaxHeight()
                        .weight(0.60f)
//                        .padding(bottom = 48.dp)
                    ,

                    userScrollEnabled = !filterWasClicked,
//                        .verticalScroll(rememberScrollState()),

                    ) {
//                    item(){
//                        Text(text = uiState.counter.toString())
//                    }

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
                                isFiltered = uiState.isFiltered,
                                onClickItemSelected = { shoppingListScreenViewModel.customItemSelected(it) },
                                onClickItemDeselected = { shoppingListScreenViewModel.customItemDeselected(it) },
                                onClickDeleteCustomItem = { shoppingListScreenViewModel.deleteCustomItem(it) }
                            )
                        }
                    }


                    item {
                        androidx.compose.animation.AnimatedVisibility(
                            visible = (uiState.customItems.isNotEmpty() && !filterWasClicked && !uiState.isFiltered),
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
                                (uiState.customItems.isNotEmpty() && !filterWasClicked && !uiState.isFiltered) ||
                                (selectedIngredients2.isNotEmpty() && !filterWasClicked  && !uiState.isFiltered)
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

//                    selectedIngredients.forEach {
//                        ShoppingListItemWithButton(
//                            modifier = Modifier.animateItemPlacement(animationSpec = (TweenSpec(150, delay = 0))),
//                            ingredientEntity = it,
//                            onClickIngredientSelected = { shoppingListScreenViewModel.ingredientSelected(it) },
//                            onClickIngredientDeselected = { shoppingListScreenViewModel.ingredientDeselected(it) },
//                            )
//                    }
                }
                LazyColumn(
                    modifier = Modifier
//                        .padding(bottom = 48.dp)
//                        .fillMaxHeight()
                        .weight(0.40f),
                    state = listState2,
                    userScrollEnabled = !filterWasClicked,
//                        .verticalScroll(rememberScrollState()),
                        ) {

                    items(shoppingListScreenData, key = { it.recipeEntity.recipeName }) {
                        RecipeIconWithButton(
                            modifier = Modifier.animateItemPlacement(animationSpec = (TweenSpec(200, delay = 0))),
                            recipeWithIngredients = it,
                            filterWasClicked = filterWasClicked,
                            isWorking = uiState.isWorking,
                            isClickable = shoppingListScreenData.size != 1,
                            onFilterClick = {shoppingListScreenViewModel.filterBy(it); filterWasClicked = !filterWasClicked},
                            onDetailsClick =
                            {
                                onDetailsClick(it.recipeEntity.recipeName)

//                                /** main to IO coroutine */
//                                coroutineScope.launch(Dispatchers.Main) {
//                                    withContext(Dispatchers.IO) {
//                                        shoppingListScreenViewModel.setDetailsScreenTarget(it.recipeEntity.recipeName)
//                                    }
//                                    onDetailsClick()
//                                }
                            }
                        )


                    }

//                    shoppingListScreenData.forEach {
//                        RecipeIconWithButton(
//                            recipeWithIngredients = RecipeWithIngredients(
//                                recipeEntity = it.recipeEntity,
//                                ingredientsList = it.ingredientsList
//                            ),
//                            onDetailsClick = { shoppingListScreenViewModel.setDetailsScreenTarget(it.recipeEntity.recipeName);
//                                onDetailsClick() }
//                        )
//                    }
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
                            //the background of the square for this button, it stays a square even tho
                            //we have shape = circle shape.  If this is not changed you see a solid
                            //square for the "background" of this button.
                            .background(color = Color.Transparent),
                        shape = CircleShape,
                        //this is the background color of the button after the "Shaping" is applied.
                        //it is different then the background attribute above.
                        backgroundColor = Color(0xFF682300)
                    ) {
                        Icon(
                            imageVector = Icons.Outlined.Add,
                            tint = Color(0xFFd8af84),
                            modifier = Modifier.size(28.dp),
                            // modifier = Modifier.background(color = Color(0xFFFFFFFF)),
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
//                                horizontalArrangement = Arrangement.SpaceEvenly
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

//    val gradientWidth = with(LocalDensity.current) { 100.dp.toPx() }

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
//            Image(
//                modifier = Modifier
//                    .fillMaxSize(),
//                contentScale = ContentScale.Crop,
//                painter = painterResource(image),
//                contentDescription = null
//            )
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

//    val gradientWidth = with(LocalDensity.current) { 200.dp.toPx() }

    val myIcon: ImageVector
    val checkBoxBackgroundColor: Color
    val decoration : TextDecoration
    var alphaLevel : Float


    if(ingredientEntity.quantityOwned == ingredientEntity.quantityNeeded
        && ingredientEntity.quantityNeeded > 0){

        selected = true
        myIcon = Icons.Filled.CheckBox
        checkBoxBackgroundColor = Color(0xFFd8af84)
        decoration = TextDecoration.LineThrough
        alphaLevel = 0.55f


    }
    else{

        selected = false
        myIcon = Icons.Outlined.CheckBoxOutlineBlank
        checkBoxBackgroundColor = Color(0xFFd8af84)
        decoration = TextDecoration.None
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
            .wrapContentHeight()
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
            ),// { selected = !selected },
        shape = RoundedCornerShape(25.dp),
        color = Color(0xFF81340A),
        elevation = 4.dp,
        //color = Color(0xFF682300),//Color(0xFFd8af84),
        contentColor = Color(0xFFd8af84),
    ){
        /*
            if selected then show X
         */
//        if (selected){
//            Box{
//                IconButton(
//                    modifier = Modifier
//                        .align(Alignment.CenterEnd)
//                        .size(36.dp),
//                    onClick = onClickIngredientDeselected, //{ selected = !selected }
//                    enabled = !isWorking
//                ){
//                    Icon(
//                        modifier = Modifier,
//                        imageVector = Icons.Outlined.Close,
//                        tint = Color(0xFFFFFFFF),
//                        contentDescription = null
//                    )
//                }
//            }
//        }
        Row(
            Modifier.fillMaxSize().padding(top = 1.dp, bottom = 1.dp),
            horizontalArrangement = Arrangement.Start
        )
        {
            Icon(
                imageVector = myIcon,
                tint = checkBoxBackgroundColor,

                //  .background(color = Color(0xFFFFFFFF)),
                contentDescription = null,
                modifier = Modifier
                    .padding(start = 6.dp, top = 2.dp, end = 2.dp, bottom = 2.dp)
                    .size(28.dp)
                    .align(Alignment.CenterVertically)
                    .alpha(alphaLevel)
                //.weight(1f)

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
                    // .weight(1f)
                    .padding(start = 2.dp, bottom = 2.dp)
                    .align(Alignment.CenterVertically)
                    .alpha(alphaLevel),
                text = myText,
                textDecoration = decoration,
                fontSize = 16.sp
            )
            /*
                Show (2) or more if the number needed is more than 1
                AND there is no quantity/unit associated with the item
             */
            if(ingredientEntity.quantityNeeded > 1 && ingredientEntity.quantity.isBlank()) {
                Text(
                    modifier = Modifier
                        //.weight(1f)
                        //.padding(start = 4.dp)
                        .align(Alignment.CenterVertically)
                        .alpha(alphaLevel),
                    text = " (${ingredientEntity.quantityNeeded})",
                    textDecoration = decoration,
                    fontSize = 16.sp
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
    isFiltered: Boolean,
    onClickItemSelected: () -> Unit,
    onClickItemDeselected: () -> Unit,
    onClickDeleteCustomItem: () -> Unit,
){

    val selected: Boolean

//    val gradientWidth = with(LocalDensity.current) { 200.dp.toPx() }

    val myIcon: ImageVector
    val checkBoxBackgroundColor: Color
    val decoration : TextDecoration
    var alphaLevel : Float


    if(shoppingListCustomItemEntity.selected == 1){

        selected = true
        myIcon = Icons.Filled.CheckBox
        checkBoxBackgroundColor = Color(0xFFd8af84)
        decoration = TextDecoration.LineThrough
        alphaLevel = 0.55f


    }
    else{

        selected = false
        myIcon = Icons.Outlined.CheckBoxOutlineBlank
        checkBoxBackgroundColor = Color(0xFFd8af84)
        decoration = TextDecoration.None
        alphaLevel = 1f

    }

    if (isFiltered) alphaLevel = 0.30f


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
//            .background(
//                brush = Brush.horizontalGradient(
//                    colors = listOf(Color(0xFF682300), Color(0xFFb15f33)),
//                    endX = gradientWidth,
//                    tileMode = TileMode.Mirror
//                ),
//                shape = RoundedCornerShape((14.dp))
//            )
            .clickable(
                enabled = (!isWorking && !isFiltered),
                onClick = if (selected) onClickItemDeselected else onClickItemSelected,
            ),
        shape = RoundedCornerShape(25.dp),
        color = Color(0xFF81340A),
        elevation = 4.dp,
        //color = Color(0xFF682300),//Color(0xFFd8af84),
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

                //  .background(color = Color(0xFFFFFFFF)),
                contentDescription = null,
                modifier = Modifier
                    .padding(start = 6.dp, top = 2.dp, end = 2.dp, bottom = 2.dp)
                    .size(28.dp)
                    .align(Alignment.CenterVertically)
                    .alpha(alphaLevel)
                //.weight(1f)

            )
            Text(
                modifier = Modifier
                    // .weight(1f)
                    .padding(start = 4.dp)
                    .align(Alignment.CenterVertically)
                    .alpha(alphaLevel),
                text = shoppingListCustomItemEntity.item,
                textDecoration = decoration,
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
