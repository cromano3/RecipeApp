package com.example.bearrecipebookapp.ui

import android.app.Application
import androidx.activity.compose.BackHandler
import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.TweenSpec
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
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
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.*
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
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
import com.example.bearrecipebookapp.data.IngredientEntity
import com.example.bearrecipebookapp.datamodel.RecipeWithIngredients
import com.example.bearrecipebookapp.viewmodel.ShoppingListScreenViewModel
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun ShoppingListScreen(
    onDetailsClick: () -> Unit,
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

        var filterWasClicked by remember { mutableStateOf(false) }

        val uiState by shoppingListScreenViewModel.shoppingScreenUiState.collectAsState()
        val uiAlertState by shoppingListScreenViewModel.uiAlertState.collectAsState()

        val coroutineScope = rememberCoroutineScope()

        val listState = rememberLazyListState()
        val listState2 = rememberLazyListState()

        BackHandler {
            onSystemBackClick()
        }

        if(filterWasClicked) {
//            LaunchedEffect(Unit) {
            coroutineScope.launch {

                delay(200)
                listState.animateScrollToItem(0)
                listState2.animateScrollToItem(0)
                filterWasClicked = false
            }
//            filterWasClicked = false
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

                    userScrollEnabled = true,
//                        .verticalScroll(rememberScrollState()),

                    ) {
//                    item(){
//                        Text(text = uiState.counter.toString())
//                    }

                    items(selectedIngredients, key = { it.ingredientName }) {
                        ShoppingListItemWithButton(
                            modifier = Modifier.animateItemPlacement(animationSpec = (TweenSpec(150, delay = 0))),
                            ingredientEntity = it,
                            isWorking = uiState.isWorking,
                            onClickIngredientSelected = { shoppingListScreenViewModel.ingredientSelected(it) },
                            onClickIngredientDeselected = { shoppingListScreenViewModel.ingredientDeselected(it) },
                        )
                    }
                    item{
                        Spacer(
                            Modifier
                                .size(20.dp)
                                .fillMaxWidth())
                    }

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
//                        .verticalScroll(rememberScrollState()),
                        ) {

                    items(shoppingListScreenData, key = { it.recipeEntity.recipeName }) {
                        RecipeIconWithButton(
                            modifier = Modifier.animateItemPlacement(animationSpec = (TweenSpec(150, delay = 0))),
                            recipeWithIngredients = it,
                            isWorking = uiState.isWorking,
                            onFilterClick = {shoppingListScreenViewModel.filterBy(it); filterWasClicked = !filterWasClicked},
                            onDetailsClick = {
                                shoppingListScreenViewModel.setDetailsScreenTarget(it.recipeEntity.recipeName)
                                onDetailsClick() }
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
            if (selectedIngredients.isEmpty()) {
                Box(Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                    Image(

                        painter = painterResource(id = R.drawable.shoppingscreenempty),
                        contentDescription = null,
                        alignment = Alignment.Center,
                        alpha = .5f,
                        colorFilter = ColorFilter.tint(Color(0xFF682300))
                    )
                }
                Box(Modifier.fillMaxSize(), contentAlignment =  Alignment.BottomEnd){
                    FloatingActionButton(
                        onClick = { shoppingListScreenViewModel.triggerAddRecipeOrCustomItemAlert() },
                        elevation = FloatingActionButtonDefaults.elevation(8.dp),
                        modifier = Modifier
                            .padding(bottom = 40.dp, end = 24.dp)
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
                Box(Modifier.fillMaxSize()){
                    //add recipe or custom item
                    if(uiAlertState.showAddRecipeOrCustomItemAlert){
                        AlertDialog(
                            onDismissRequest = {shoppingListScreenViewModel.cancelAddRecipeOrCustomItemAlert()},
                            text = {
                                Text(text = "Add a recipe, or a custom item to the shopping list?" )
                            },
                            confirmButton = {
                                TextButton(
                                    onClick = {
                                        shoppingListScreenViewModel.cancelAddRecipeOrCustomItemAlert()
                                        shoppingListScreenViewModel.triggerAddCustomItemAlert()

                                    },
                                    modifier = Modifier.fillMaxWidth(),
                                    border = BorderStroke(2.dp, Color.Black)

                                ) {
                                    Text("Add Custom Item")
                                }
                            },
                            dismissButton = {
                                TextButton(
                                    onClick = {
                                        shoppingListScreenViewModel.cancelAddRecipeOrCustomItemAlert()
                                        onAddRecipeClick()
                                    },
                                    modifier = Modifier.fillMaxWidth(),
                                    border = BorderStroke(2.dp, Color.Black),
                                ) {
                                    Text("Add Recipe")
                                }
                            }
                        )
                    }
                    else if(uiAlertState.showAddCustomItemAlert){
                        AlertDialog(
                            onDismissRequest = {shoppingListScreenViewModel.cancelAddCustomItemAlert()},
                            text = {
                                Text(text = "Add custom item:" )
                            },
                            confirmButton = {
                                TextButton(
                                    onClick = {

                                        /** add the item */

                                    }
                                ) {
                                    Text("Add Item")
                                }
                            },
                            dismissButton = {
                                TextButton(
                                    onClick = {
                                        shoppingListScreenViewModel.cancelAddCustomItemAlert()
                                    }
                                ) {
                                    Text("Cancel")
                                }
                            }
                        )
                    }
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
                .clickable(enabled = !isWorking, onClick = onFilterClick),
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
                .clickable(enabled = !isWorking) { onDetailsClick() },
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
    ingredientEntity: IngredientEntity,
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
            .height(36.dp)
            .alpha(alphaAnim)
//            .background(
//                brush = Brush.horizontalGradient(
//                    colors = listOf(Color(0xFF682300), Color(0xFFb15f33)),
//                    endX = gradientWidth,
//                    tileMode = TileMode.Mirror
//                ),
//                shape = RoundedCornerShape((14.dp))
//            )
            .clickable(
                enabled = !selected && !isWorking,
                onClick = onClickIngredientSelected,
            ),// { selected = !selected },
        shape = RoundedCornerShape(25.dp),
        color = Color(0xFF682300),
        elevation = 4.dp,
        //color = Color(0xFF682300),//Color(0xFFd8af84),
        contentColor = Color(0xFFd8af84),
    ){
        /*
            if selected then show X
         */
        if (selected){
            Box{
                IconButton(
                    modifier = Modifier
                        .align(Alignment.CenterEnd)
                        .size(36.dp),
                    onClick = onClickIngredientDeselected, //{ selected = !selected }
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
                text = ingredientEntity.ingredientName,
                textDecoration = decoration,
                fontSize = 16.sp
            )
            /*
                Show (2) or more if the number needed is more than 1
             */
            if(ingredientEntity.quantityNeeded > 1) {
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



//@Preview(showBackground = true, showSystemUi = true)
//@Composable
//fun DefaultPreview() {
//    var myRecipe: RecipeEntity = RecipeEntity(recipeName = "Cauliflower Walnut Tacos", onMenu = 0,1, timeToMake = 60, rating = 98)
//
//    var ing1: IngredientEntity = IngredientEntity(ingredientName = "Ingredient 1", quantityOwned = 0, quantityNeeded = 0)
//    var ing2: IngredientEntity = IngredientEntity(ingredientName = "Ingredient 2", quantityOwned = 0, quantityNeeded = 0)
//    var ing3: IngredientEntity = IngredientEntity(ingredientName = "Ingredient 3", quantityOwned = 0, quantityNeeded = 0)
//
//    var ins1: InstructionEntity = InstructionEntity(instructionID = 1, recipeID = "Bagels", instruction = "Munch it.")
//    var ins2: InstructionEntity = InstructionEntity(instructionID = 1, recipeID = "Bagels", instruction = "Munch it!.")
//    var ins3: InstructionEntity = InstructionEntity(instructionID = 1, recipeID = "Bagels", instruction = "Munch it!!.")
//
//    var recList = listOf(
//        RecipeWithIngredients(myRecipe,listOf(ing1, ing2, ing3)),
//        RecipeWithIngredients(myRecipe,listOf(ing1, ing2, ing3)))
//
//    var recInsList = listOf(
//        RecipeWithInstructions(myRecipe, listOf(ins1, ins2, ins3)),
//        RecipeWithInstructions(myRecipe, listOf(ins1, ins2, ins3)),
//    )
//
//    var recipeAll = RecipeWithIngredientsAndInstructions(myRecipe,listOf(ing1, ing2), listOf(ins1, ins2))
//
//    BearRecipeBookAppTheme {
//        ShoppingListScreen(
//            listOf(ing1, ing2, ing3),
//            listOf(recipeAll),
//            {},
//            {},
//            {}
//        )
//    }
//}

class ShoppingListScreenViewModelFactory(
    val application: Application,
) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return ShoppingListScreenViewModel(
            application,
        ) as T
    }

}
