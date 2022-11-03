package com.example.bearrecipebookapp.ui

import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CheckBox
import androidx.compose.material.icons.outlined.CheckBoxOutlineBlank
import androidx.compose.material.icons.outlined.Close
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Shadow
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.bearrecipebookapp.R
import com.example.bearrecipebookapp.data.IngredientEntity
import com.example.bearrecipebookapp.data.InstructionEntity
import com.example.bearrecipebookapp.data.RecipeEntity
import com.example.bearrecipebookapp.datamodel.RecipeWithIngredients
import com.example.bearrecipebookapp.datamodel.RecipeWithIngredientsAndInstructions
import com.example.bearrecipebookapp.datamodel.RecipeWithInstructions
import com.example.bearrecipebookapp.ui.theme.BearRecipeBookAppTheme

@Composable
fun ShoppingListScreen(
    selectedIngredients: List<IngredientEntity>,
    //selectedRecipes: List<RecipeWithIngredients>,
    selectedRecipes: List<RecipeWithIngredientsAndInstructions>,
    onClickIngredientSelected: (IngredientEntity) -> Unit,
    onClickIngredientDeselected: (IngredientEntity) -> Unit,
    //onDetailsClick: (RecipeWithIngredients) -> Unit
    onDetailsClick: (RecipeWithIngredientsAndInstructions) -> Unit
) {
    Surface(
        modifier = Modifier.fillMaxSize(),
        color = Color(0xFFd8af84)//Color(0xFFb15f33), //Color(0xFFd8af84)


    ) {
        Row(
            Modifier
                .fillMaxSize()
//                .verticalScroll(rememberScrollState())
        )
            {
                Column(modifier = Modifier
                    .fillMaxHeight()
                    .weight(0.60f)
                    .padding(bottom = 8.dp)
                    .verticalScroll(rememberScrollState()),

                    ) {

                    selectedIngredients.forEach{
                        ShoppingListItemWithButton(
                            ingredientEntity = it,
                            onClickIngredientSelected = {onClickIngredientSelected(it)} ,
                            onClickIngredientDeselected = {onClickIngredientDeselected(it)},

                        )
                    }
                }
                Column(modifier = Modifier
                    .fillMaxHeight()
                    .weight(0.40f)
                    .verticalScroll(rememberScrollState()),

                    ) {

                    selectedRecipes.forEach{
                        RecipeIconWithButton(
                            recipeWithIngredients = RecipeWithIngredients(recipeEntity = it.recipeEntity, ingredientsList = it.ingredientsList),
                                    //it,
                            onDetailsClick = { onDetailsClick(it) }
                        )
                    }
                }
            }

    }
}

@Composable
fun RecipeIconWithButton(
    recipeWithIngredients: RecipeWithIngredients,
    onDetailsClick: () -> Unit
){


    var image = R.drawable.bagel

    image = when(recipeWithIngredients.recipeEntity.recipeName){
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

    val gradientWidth = with(LocalDensity.current) { 100.dp.toPx() }

    Column(Modifier.fillMaxWidth(),
        horizontalAlignment = Alignment.CenterHorizontally)
    {
        Surface(
            modifier = Modifier
                .size(120.dp)
                .padding(top = 8.dp, bottom = 4.dp)
                .align(Alignment.CenterHorizontally)
                .clickable { /* Highlight ingredients that match this recipe on this screen*/ },
            shape = RoundedCornerShape(15.dp),
            elevation = 6.dp,

        ){
            Image(
                modifier = Modifier
                    .fillMaxSize(),
                contentScale = ContentScale.Crop,
                /*

                add correct image

                */

                //painter = painterResource(recipeEntity.image),

                painter = painterResource(image),

                contentDescription = null
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
                .wrapContentSize()
                .clickable { onDetailsClick() },
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
    ingredientEntity: IngredientEntity,
    onClickIngredientSelected: () -> Unit,
    onClickIngredientDeselected: () -> Unit
){

    val selected: Boolean

    val gradientWidth = with(LocalDensity.current) { 200.dp.toPx() }

    val myIcon: ImageVector
    val checkBoxBackgroundColor: Color
    val decoration : TextDecoration
    val alphaLevel : Float


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


    Surface(
        modifier = Modifier
            .padding(start = 8.dp, top = 8.dp)
            .width(240.dp)
            .height(36.dp)
            .alpha(alphaLevel)
//            .background(
//                brush = Brush.horizontalGradient(
//                    colors = listOf(Color(0xFF682300), Color(0xFFb15f33)),
//                    endX = gradientWidth,
//                    tileMode = TileMode.Mirror
//                ),
//                shape = RoundedCornerShape((14.dp))
//            )
            .clickable(
                enabled = !selected,
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
                    onClick = onClickIngredientDeselected //{ selected = !selected }
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



@Preview(showBackground = true, showSystemUi = true)
@Composable
fun DefaultPreview() {
    var myRecipe: RecipeEntity = RecipeEntity(recipeName = "Cauliflower Walnut Tacos", onMenu = 0,1, timeToMake = 60, rating = 98)

    var ing1: IngredientEntity = IngredientEntity(ingredientName = "Ingredient 1", quantityOwned = 0, quantityNeeded = 0)
    var ing2: IngredientEntity = IngredientEntity(ingredientName = "Ingredient 2", quantityOwned = 0, quantityNeeded = 0)
    var ing3: IngredientEntity = IngredientEntity(ingredientName = "Ingredient 3", quantityOwned = 0, quantityNeeded = 0)

    var ins1: InstructionEntity = InstructionEntity(instructionID = 1, recipeID = "Bagels", instruction = "Munch it.")
    var ins2: InstructionEntity = InstructionEntity(instructionID = 1, recipeID = "Bagels", instruction = "Munch it!.")
    var ins3: InstructionEntity = InstructionEntity(instructionID = 1, recipeID = "Bagels", instruction = "Munch it!!.")

    var recList = listOf(
        RecipeWithIngredients(myRecipe,listOf(ing1, ing2, ing3)),
        RecipeWithIngredients(myRecipe,listOf(ing1, ing2, ing3)))

    var recInsList = listOf(
        RecipeWithInstructions(myRecipe, listOf(ins1, ins2, ins3)),
        RecipeWithInstructions(myRecipe, listOf(ins1, ins2, ins3)),
    )

    var recipeAll = RecipeWithIngredientsAndInstructions(myRecipe,listOf(ing1, ing2), listOf(ins1, ins2))

    BearRecipeBookAppTheme {
        ShoppingListScreen(
            listOf(ing1, ing2, ing3),
            listOf(recipeAll),
            {},
            {},
            {}
        )
    }
}
