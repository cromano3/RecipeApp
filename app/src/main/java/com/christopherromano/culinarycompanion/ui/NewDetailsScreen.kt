package com.christopherromano.culinarycompanion.ui

import android.app.Application
import androidx.compose.animation.core.TweenSpec
import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Icon
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.AutoAwesome
import androidx.compose.material.icons.outlined.Star
import androidx.compose.material.icons.outlined.ThumbUp
import androidx.compose.material.icons.outlined.Timer
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.graphics.TileMode
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.ExperimentalTextApi
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
import com.christopherromano.culinarycompanion.R
import com.christopherromano.culinarycompanion.data.annotatedstrings.confirmCompletedCookingAnoString
import com.christopherromano.culinarycompanion.data.annotatedstrings.confirmIMadeThisAnoString
import com.christopherromano.culinarycompanion.data.annotatedstrings.confirmRemoveMenuAnoString
import com.christopherromano.culinarycompanion.data.entity.RecipeEntity
import com.christopherromano.culinarycompanion.data.repository.DetailsScreenFirebaseRepository
import com.christopherromano.culinarycompanion.datamodel.AuthorDataWithComment
import com.christopherromano.culinarycompanion.datamodel.RecipeWithIngredientsAndInstructions
import com.christopherromano.culinarycompanion.ui.components.*
import com.christopherromano.culinarycompanion.ui.theme.Cabin
import com.christopherromano.culinarycompanion.viewmodel.DetailsScreenViewModel
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

@OptIn(ExperimentalTextApi::class, ExperimentalFoundationApi::class)
@Composable
fun NewDetailsScreen(
    //recipeName: String,
    recipeData: RecipeWithIngredientsAndInstructions,
//    reviewsData: List<ReviewWithAuthorDataModel>,
//    appUiState: AppUiState,
//    localUserReview: String,
//    localUserNickName: String,
//    localUserImageIRL: String,
//    onGoBackClick: () -> Unit,
    confirmSignInWithGoogle: () -> Unit,
    onMenuAddClick: (RecipeWithIngredientsAndInstructions) -> Unit,
    onMenuRemoveClick: (RecipeWithIngredientsAndInstructions) -> Unit,
//    onFavoriteClick: (RecipeWithIngredientsAndInstructions) -> Unit,
//    onCompleteClick: (RecipeWithIngredientsAndInstructions) -> Unit,
    addedToFavoriteUiUpdate: () -> Unit,
    markAsRated: () -> Unit,
    storeRating: (Int) -> Unit,
    markAsReviewed: () -> Unit,
    updateLikes: (String) -> Unit,
    updateDislikes: (String) -> Unit,
    onIMadeThisClick: (RecipeWithIngredientsAndInstructions) -> Unit,
    onFinishedCookingClick: (RecipeWithIngredientsAndInstructions) -> Unit,
    showAddedToFavoritesSnackBarMessage: (RecipeEntity) -> Unit,
    navigateToCommentScreen: (String) -> Unit,
    submitReport: (AuthorDataWithComment) -> Unit,
    ) {

    val owner = LocalViewModelStoreOwner.current

    owner?.let {viewModelStoreOwner ->
        val detailsScreenViewModel: DetailsScreenViewModel = viewModel(
            viewModelStoreOwner,
            "DetailsScreenViewModel",
            DetailsScreenViewModelFactory(
                LocalContext.current.applicationContext as Application,
                recipeData.recipeEntity.recipeName,
            )
        )

//        val detailsScreenData by detailsScreenViewModel.detailsScreenData.observeAsState(RecipeWithIngredientsAndInstructions())

        val uiAlertState by detailsScreenViewModel.uiAlertState.collectAsState()

        val commentsList by detailsScreenViewModel.commentsList.collectAsState()
//        val uiState by detailsScreenViewModel.uiState.collectAsState()

        val authState by detailsScreenViewModel.authState.observeAsState()

//        val reviewsData by detailsScreenViewModel.reviewsData.observeAsState(listOf())
//        val firebaseCommentsLiveData by detailsScreenViewModel.firebaseCommentsLiveData.observeAsState(listOf())
        val globalRating by detailsScreenViewModel.globalRating.observeAsState()

        val ingredientsQuantities by detailsScreenViewModel.ingredientQuantitiesList.observeAsState()
//        val globalRatingFirebaseLiveData by detailsScreenViewModel.globalRatingFirebaseLiveData.observeAsState()

        val coroutineScope = CoroutineScope(Dispatchers.Main)

//        println(reviewsData.size.toString() + "size")
//        println("notEmpty" + reviewsData.isNotEmpty())
//        println("isEmpty" + reviewsData.isEmpty())




//    val gradientWidth = with(LocalDensity.current) { 200.dp.toPx() }

//        BackHandler { onGoBackClick() }


        /*
         * Get the image based on the recipe Name
         */
        val image: Int = when (recipeData.recipeEntity.recipeName) {
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
            else -> R.drawable.garlic2
        }



        /*
        format the "Time to make" from raw Int minutes to "X hr./hrs. Y mins."
        **/
        val formattedTime: String
        val remainder: Int
        val quotient: Int

        if (recipeData.recipeEntity.timeToMake <= 60) {
            formattedTime = recipeData.recipeEntity.timeToMake.toString() + " mins."
        } else {
            quotient = recipeData.recipeEntity.timeToMake / 60
            remainder = recipeData.recipeEntity.timeToMake % 60
            if (remainder == 0) {
                formattedTime = "$quotient hrs."
            } else {
                if (quotient == 1) {
                    formattedTime = "$quotient hr. $remainder mins."
                } else {
                    formattedTime = "$quotient hrs. $remainder mins."
                }
            }
        }

        val scrollState = rememberScrollState()

        Surface(
            modifier = Modifier
                .fillMaxSize(),
            color = Color(0xFFd8af84)


        ) {
//            BackHandler { onGoBackClick() }
            Column() {

                LazyColumn(
                    modifier = Modifier
                        .fillMaxSize(),
                    verticalArrangement = Arrangement.Top,
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {

                    item{
                        Spacer(
                            Modifier
                                .fillMaxWidth()
                                .border(1.dp, Color(0xFFd8af84))
                                .height(1.dp)
                        )
                    }

                    item{
                        AsyncImage(
                            model = image,
                            contentDescription = null,
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(300.dp)
                                .padding(bottom = 0.dp),
                            contentScale = ContentScale.Crop,
                        )
                    }

                    item{
                        Spacer(
                            Modifier
                                .fillMaxWidth()
                                .border(1.dp, Color(0xFFd8af84))
                                .height(1.dp)
                        )
                    }


//                    Column(
//                        modifier = Modifier
//                            // .weight(1f)
//                            .fillMaxSize(),
//                        verticalArrangement = Arrangement.Center,
//                        horizontalAlignment = Alignment.CenterHorizontally
//                    ) {

                    //Buttons Row beneath image
                    item{
                        Surface(
                            modifier =
                            Modifier
                                .padding(start = 0.dp, end = 0.dp, bottom = 16.dp)
                                .height(60.dp)
                                .fillMaxWidth()
                                .background(
                                    brush = Brush.horizontalGradient(
                                        colors = listOf(Color(0xFFb15f33), Color(0xFF682300)),
                                        tileMode = TileMode.Mirror
                                    ),
                                    shape = RoundedCornerShape(0.dp, 0.dp, 0.dp, 0.dp)
                                ),
                            shape = RoundedCornerShape(0.dp, 0.dp, 0.dp, 0.dp),

                            color = Color.Transparent,

                            ) {

                            val selected: Boolean
//                            val alphaLevel: Float
                            val menuText: String
                            val finishedText: String

                            val removeTextColor = Color(0xFF682300)
                            val removeBackgroundColor = Color(0xFFd8af84)
                            val removeBorderStartColor = Color(0xFFb15f33)
                            val removeBorderEndColor = Color(0xFF682300)

                            val textColor = Color(0xFFd8af84)
                            val backgroundColor = Color(0xFF682300)
                            val borderStartColor = Color(0xFFd8af84)
                            val borderEndColor = Color(0xFFb15f33)

                            if (recipeData.recipeEntity.onMenu == 1) {
                                selected = true
//                            alphaLevel = 1f
                                menuText = "Remove from Menu"
                                finishedText = "Finished Cooking!"
                            } else {
                                selected = false
//                            alphaLevel = 1f
                                menuText = "Add to Menu"
                                finishedText = "I Made This!"
                            }

//                        val alphaAnim: Float by animateFloatAsState(
//                            targetValue = alphaLevel,
//                            animationSpec = tween(
//                                durationMillis = 150,
//                                delayMillis = 0,
//                                easing = LinearEasing,
//                            )
//                        )

                            Row(
                                Modifier.fillMaxSize(),
                                horizontalArrangement = Arrangement.SpaceEvenly,
                                verticalAlignment = Alignment.CenterVertically,
                            ) {

                                //Add or Remove Menu button
                                DetailsScreenButton(
                                    onClick =
                                    {
                                        if (selected) {
                                            detailsScreenViewModel.triggerRemoveAlert(recipeData)
                                        } else {
                                            detailsScreenViewModel.addToMenu(recipeData)
                                            onMenuAddClick(recipeData)
                                        }
                                    },
                                    borderStartColor = if (selected) removeBorderStartColor else borderStartColor,
                                    borderEndColor = if (selected) removeBorderEndColor else borderEndColor,
                                    textColor = if (selected) removeTextColor else textColor,
                                    backgroundColor = if (selected) removeBackgroundColor else backgroundColor,
                                    buttonText = menuText
                                )

                                //Finished cooking button
                                DetailsScreenButton(
                                    onClick =
                                    {
                                        if (selected) {
                                            detailsScreenViewModel.triggerCompletedAlert(recipeData)
                                        } else {
                                            detailsScreenViewModel.triggerCompletedAlert(recipeData)
                                        }
                                    },
                                    borderStartColor = borderStartColor,
                                    borderEndColor = borderEndColor,
                                    textColor = textColor,
                                    backgroundColor = backgroundColor,
                                    buttonText = finishedText
                                )

                            }
                        }
                    }

                    //Info box
                    item{
                        Surface(
                            modifier = Modifier
                                .padding(bottom = 16.dp)
                                .wrapContentSize(),
                            shape = RoundedCornerShape(10.dp),
                            color = Color(0xFF682300),
                            //color = Color(0xFF682300),//Color(0xFFd8af84),
                            contentColor = Color(0xFFd8af84),
                            elevation = 6.dp
                        ) {
                            //Info Box
                            Column(
                                Modifier.padding(start = 4.dp)
                            ) {

                                //Time
                                Row(verticalAlignment = Alignment.CenterVertically) {
                                    Icon(
                                        Icons.Outlined.Timer,
                                        tint = Color(0xFFd8af84),
                                        contentDescription = null
                                    )
                                    Text(
                                        text = "Time: $formattedTime",
                                        modifier = Modifier
                                            .padding(
                                                start = 8.dp,
                                                end = 8.dp,
                                                top = 8.dp,
                                                bottom = 8.dp
                                            )
                                            .background(color = Color.Transparent),
                                        // .weight(1f),
                                        color = Color(0xFFd8af84),
                                        fontSize = 18.sp,
                                        textAlign = TextAlign.Center,
                                        fontWeight = FontWeight.Bold

                                    )
                                }
                                //Difficulty
                                Row(
                                    verticalAlignment = Alignment.CenterVertically,
                                    modifier = Modifier.padding(end = 4.dp)
                                ) {
                                    Icon(
                                        Icons.Outlined.AutoAwesome,
                                        tint = Color(0xFFd8af84),
                                        contentDescription = null
                                    )
                                    Text(
                                        text = "Difficulty: ",
                                        modifier = Modifier
                                            .padding(
                                                start = 8.dp,
                                                end = 0.dp,
                                                top = 8.dp,
                                                bottom = 8.dp
                                            )
                                            .background(color = Color.Transparent),
                                        // .weight(1f),
                                        color = Color(0xFFd8af84),
                                        fontSize = 18.sp,
                                        textAlign = TextAlign.Center,
                                        fontWeight = FontWeight.Bold

                                    )
                                    for (x in 0 until recipeData.recipeEntity.difficulty) {
                                        Icon(
                                            Icons.Outlined.Star,
                                            tint = Color(0xFFd8af84),
                                            contentDescription = null
                                        )
                                    }
                                }
                                //Rating
                                Row(verticalAlignment = Alignment.CenterVertically) {
                                    Icon(
                                        Icons.Outlined.ThumbUp,
                                        tint = Color(0xFFd8af84),
                                        contentDescription = null
                                    )
                                    Text(
                                        text = "Rating: $globalRating",
                                        modifier = Modifier
                                            .padding(
                                                start = 8.dp,
                                                end = 8.dp,
                                                top = 8.dp,
                                                bottom = 8.dp
                                            )
                                            .background(color = Color.Transparent),
                                        // .weight(1f),
                                        color = Color(0xFFd8af84),
                                        fontSize = 18.sp,
                                        textAlign = TextAlign.Center,
                                        fontWeight = FontWeight.Bold

                                    )
                                }
                            }


                        }
                    }

                        //Ingredients List //
                        item {
                            Surface(
                                modifier = Modifier
                                    .wrapContentHeight()
                                    .fillMaxWidth()
                                    .padding(start = 16.dp, end = 16.dp, bottom = 16.dp),
                                //    .weight(1f),
                                shape = RoundedCornerShape(10.dp),
                                color = Color(0xFF682300),

                                ) {
                                Column(
                                    Modifier.padding(
                                        top = 8.dp,
                                        bottom = 8.dp,
                                        start = 16.dp,
                                        end = 16.dp
                                    )
                                ) {
                                    Text(
                                        modifier = Modifier
                                            .padding(bottom = 8.dp)
                                            .align(Alignment.CenterHorizontally),
                                        text = "Ingredients List:",
                                        textDecoration = TextDecoration.Underline,
                                        color = Color(0xFFd8af84),
                                        fontSize = 18.sp
                                    )


                                    for (x in 0 until (ingredientsQuantities?.size ?: 0)) {

                                        var myText = "- " + ingredientsQuantities?.get(x)?.ingredientName

                                        if(!ingredientsQuantities?.get(x)?.quantity.isNullOrBlank()) {
                                            myText += " ("
                                            myText += ingredientsQuantities?.get(x)?.quantity
                                            myText += " "
                                            myText += ingredientsQuantities?.get(x)?.unit
                                            myText += ")"
                                        }

//                                        if(ingredientsQuantities?.size != null){
//                                            println("NOT NULL")
//                                            println(ingredientsQuantities?.size)
//                                        }
//
//                                        for(y in 0 until (ingredientsQuantities?.size ?: 0)){
//                                            println("LLOPER")
//                                            if(ingredientsQuantities?.get(y)?.ingredientName == recipeData.ingredientsList[x].ingredientName){
//                                                if(!ingredientsQuantities?.get(y)?.quantity.isNullOrBlank()) {
//                                                    println("PASSED LOP")
//                                                    myText += " ("
//                                                    myText += ingredientsQuantities?.get(y)?.quantity
//                                                    myText += " "
//                                                    myText += ingredientsQuantities?.get(y)?.unit
//                                                    myText += ")"
//                                                }
//                                                break
//                                            }
//                                        }

                                        Text(
                                            //modifier = Modifier.padding(start = 8.dp, end = 8.dp),
                                            text = myText,
                                            color = Color(0xFFd8af84),
                                            fontSize = 18.sp

                                        )
                                    }
                                }
                            }
                        }




                    //Instructions List

                    //for (x in 0 until recipeData.instructionsList.size) {
                    items(recipeData.instructionsList, key = { it.instruction })
                    {
                        Surface(
                            modifier = Modifier
                                .wrapContentHeight()
                                .fillMaxWidth()
                                .padding(16.dp)
                                .clickable {}
                                .align(Alignment.CenterHorizontally),

                            shape = RoundedCornerShape(10.dp),
                            elevation = 8.dp,
                            color = Color(0xFF682300)
                        ) {
                            Text(
                                modifier = Modifier.padding(8.dp),
                                color = Color(0xFFd8af84),
                                text = it.instruction,
                                textAlign = TextAlign.Center
                            )
                        }
                    }

//                    }

//                    for(x in 0 until recipeData.reviewsList.size){
//                        ReviewWidget(recipeData.reviewsList[x].reviewText)
//                    }

                    if (commentsList.isNotEmpty()
//                        || authState == 0
                    ) {
                        item {
                            Text(
                                text = "Comments and Tips",
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

                        item {
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

//                    if (authState == 0) {
//                        item{
//                            Box(
//                                Modifier
//                                    .fillMaxWidth()
//                                    .padding(8.dp),
//                                contentAlignment = Alignment.Center
//                            )
//                            {
//                                Button(
//                                    modifier = Modifier
//                                        .width(250.dp)
//                                        .padding(start = 8.dp, end = 8.dp),
//                                    onClick = confirmSignInWithGoogle,
//                                    elevation = ButtonDefaults.elevation(6.dp),
//                                    shape = RoundedCornerShape(10.dp),
//                                    border = BorderStroke(
//                                        width = 2.dp,
//                                        brush = (Brush.horizontalGradient(
//                                            startX = -30f,
//                                            colors = listOf(Color(0xFFb15f33), Color(0xFFb15f33)),
//                                            tileMode = TileMode.Mirror
//                                        )),
//                                    ),
//                                    colors = ButtonDefaults.buttonColors(
//                                        backgroundColor = Color(0xFF682300),
//                                        contentColor = Color(0xFFd8af84)
//                                    )
//                                ) {
//                                    Row(
//                                        modifier = Modifier
//                                            .width(250.dp)
//                                            .height(25.dp),
//                                        horizontalArrangement = Arrangement.SpaceEvenly,
//                                        verticalAlignment = Alignment.CenterVertically
//                                    ) {
//                                        Image(
//                                            painter = painterResource(id = R.drawable.ic_google_logo),
//                                            contentDescription = null
//                                        )
//                                        Text("Sign in with Google")
//                                    }
//                                }
//                            }
//                        }
//                    }

//                    if(localUserReview.isNotEmpty()){
//                        item{
//                            ReviewWidget(
//                                authorName = localUserNickName,
//                                authorImageUrl = localUserImageIRL,
//                                reviewText = localUserReview,
//                                likes = 10,
//                                onLikeClick = {},
//
//                            )
//                        }
//                    }

                    if(commentsList.isNotEmpty()) {
                        items(if(commentsList.size <= 4) commentsList.take(3) else commentsList, key = { it.comment.commentID })
                        {
                            ReviewWidget(
                                modifier = Modifier.animateItemPlacement(animationSpec = (TweenSpec(400, delay = 0))),
                                isProfileScreen = false,
                                authorName = it.authorData.userName,
                                authorImageUrl = it.authorData.userPhotoURL,
                                reviewText = it.comment.commentText,
                                karma = it.authorData.karma,
//                                likes = if(it.comment.likedByMe == 1 && it.comment.myLikeWasSynced == 0) it.comment.likes + 1 else it.comment.likes,
                                likes = it.comment.likes,
                                likedByUser = it.comment.likedByMe,
                                dislikedByUser = it.comment.dislikedByMe,
                                onLikeClick = {
                                    println("click")
//                                    detailsScreenViewModel.setLiked(it.commentsEntity.commentID)
                                    updateLikes(it.comment.commentID)
                                },
                                onDislikeClick = {
                                    updateDislikes(it.comment.commentID)
                                },
                                onReportClick = {
                                    detailsScreenViewModel.triggerReportAlert(it)
                                }
                            )
                        }
                        if(commentsList.size == 4){
                            item() {
                                ConfirmAlertButton(buttonText = "Show All Comments") {
                                    detailsScreenViewModel.changeLimit()
//                                    detailsScreenViewModel.setCommentsLimit(50)

                                }
                            }
                        }
                    }

                    item {
                        Spacer(
                            Modifier
                                .size(64.dp)
                                .fillMaxWidth()
                        )
                    }

                }
            }

            //Alerts
            Box(Modifier.fillMaxSize()){

                //Report Alert
                if(uiAlertState.showReportAlert){
                    BasicAlert(
                        text = "Report this comment for being inappropriate or off topic? Please note: submitting false reports may result in your account being banned.",
                        confirmButtonText = "Submit report",
                        cancelButtonText = "Cancel",
                        onConfirmClick = {
                            submitReport(uiAlertState.reportedComment)
                             detailsScreenViewModel.cancelReportAlert()
                                         },
                        onCancelClick = { detailsScreenViewModel.cancelReportAlert() },
                        onDismiss = {
                            detailsScreenViewModel.cancelReportAlert()
                        }
                    )
                }

                //Remove Alert
                if(uiAlertState.showRemoveAlert){
                    AnnotatedStringAlert(
                        text = confirmRemoveMenuAnoString(uiAlertState.recipe.recipeEntity.recipeName),
                        confirmButtonText = "Yes",
                        cancelButtonText = "Cancel",
                        onConfirmClick =
                        {
                            detailsScreenViewModel.removeFromMenu(uiAlertState.recipe)
                            detailsScreenViewModel.cancelRemoveAlert()
                            onMenuRemoveClick(recipeData)
                        },
                        onCancelClick = { detailsScreenViewModel.cancelRemoveAlert() },
                        onDismiss = {}
                    )
                }

                //Completed Alert
                if(uiAlertState.showCompletedAlert){
                    val finishedText: AnnotatedString = if(recipeData.recipeEntity.onMenu == 0) {
                        confirmIMadeThisAnoString(uiAlertState.recipe.recipeEntity.recipeName)
                    }
                    else{
                        confirmCompletedCookingAnoString(uiAlertState.recipe.recipeEntity.recipeName)
                    }

                    AnnotatedStringAlert(
                        text = finishedText,
                        confirmButtonText = "Yes",
                        cancelButtonText = "Cancel",
                        onConfirmClick =
                        {
                            detailsScreenViewModel.addCooked(recipeData)
                            detailsScreenViewModel.addExp(uiAlertState.recipe)
                            if(recipeData.recipeEntity.onMenu == 1) {
                                detailsScreenViewModel.removeFromMenu(uiAlertState.recipe)
                                onFinishedCookingClick(recipeData)
                            }
                            else {
                                onIMadeThisClick(recipeData)
                            }
                            detailsScreenViewModel.confirmCompletedAlert(recipeData.recipeEntity)
                         },

                        onCancelClick = { detailsScreenViewModel.cancelCompletedAlert() },
                        onDismiss = {}
                    )

                }
                
                if(uiAlertState.showRatingAlert){
                    ThumbsRatingAlert(
                        confirmButtonText = "Confirm",
                        cancelButtonText = "Cancel",
                        onConfirmClick =
                        {
                            if(uiAlertState.isThumbUpSelected || uiAlertState.isThumbDownSelected){
                                markAsRated()
                                storeRating(if(uiAlertState.isThumbUpSelected) 1 else 0)
                            }
                            coroutineScope.launch {
                                detailsScreenViewModel.confirmRating(recipeData.recipeEntity)
                            }
                        },
                        onCancelClick = { detailsScreenViewModel.cancelRatingAlert() },
                        onDismiss = { detailsScreenViewModel.cancelRatingAlert() },
                        onThumbDownClick = { detailsScreenViewModel.thumbDownClicked() },
                        onThumbUpClick = { detailsScreenViewModel.thumbUpClicked() },
                        text = "Did you enjoy ${recipeData.recipeEntity.recipeName}?",
                        isThumbDownSelected = uiAlertState.isThumbDownSelected,
                        isThumbUpSelected = uiAlertState.isThumbUpSelected,
                    )

                }

                if(uiAlertState.showFavoriteAlert){
                    BasicAlert(
                        text = "Add ${recipeData.recipeEntity.recipeName} to your Favorites?",
                        confirmButtonText = "Yes",
                        cancelButtonText = "No",
                        onConfirmClick =
                        {
                            addedToFavoriteUiUpdate()
                            detailsScreenViewModel.addToFavorite(recipeData.recipeEntity)
                            showAddedToFavoritesSnackBarMessage(recipeData.recipeEntity)
                        },
                        onCancelClick = { detailsScreenViewModel.doNotAddToFavorite(recipeData.recipeEntity) },
                        onDismiss = { detailsScreenViewModel.cancelFavoriteAlert() }
                    )
                        


                }
                if(uiAlertState.showLeaveReviewAlert){
                    BasicAlert(
                        text = "Would you like to share a tip about this recipe for other chefs?",
                        confirmButtonText = "Yes",
                        cancelButtonText = "No",
                        onConfirmClick =
                        {
                            markAsReviewed()
                            detailsScreenViewModel.confirmShowWriteReviewAlert()
                            navigateToCommentScreen(recipeData.recipeEntity.recipeName)

//                            /** Will be main thread query to ensure data is ready when user gets to Comment Screen */
//
//                            coroutineScope.launch(Dispatchers.Main){
//                                withContext(Dispatchers.IO) {
//                                    detailsScreenViewModel.confirmShowWriteReviewAlert(
//                                        recipeData.recipeEntity
//                                    )
//                                }
//                                navigateToCommentScreen()
//                            }

                        },
                        onCancelClick =
                        {
                            markAsReviewed()
                            detailsScreenViewModel.doNotWriteReview(recipeData.recipeEntity)
                        },
                        onDismiss = { detailsScreenViewModel.cancelShowWriteReviewAlert() }
                    )
                }

            }
        }
    }
}

//@Preview
//@Composable
//fun MyPreview234(){
//    BearRecipeBookAppTheme {
//
//        val myRecipe = RecipeEntity(
//            recipeName = "Cauliflower Walnut Tacos",
//            onMenu = 0,
//            1,
//            rating = 98,
//            timeToMake = 90,
//            difficulty = 4
//        )
//
//        val ing1 = IngredientEntity(ingredientName = "Ingredient 1", quantityOwned = 0, quantityNeeded = 0)
//        val ing2 = IngredientEntity(ingredientName = "Ingredient 2", quantityOwned = 0, quantityNeeded = 0)
//        val ing3 = IngredientEntity(ingredientName = "Ingredient 3", quantityOwned = 0, quantityNeeded = 0)
//
//        val ins1 = InstructionEntity(instructionID = 1, recipeID = "Bagels", instruction = "Munch it.")
//        val ins2 = InstructionEntity(instructionID = 1, recipeID = "Bagels", instruction = "Munch it!.")
//        val ins3 = InstructionEntity(instructionID = 1, recipeID = "Bagels", instruction = "Munch it!!.")
//
//        var recList = listOf(
//            RecipeWithIngredients(myRecipe,listOf(ing1, ing2, ing3)),
//            RecipeWithIngredients(myRecipe,listOf(ing1, ing2, ing3)))
//
//        var recInsList = listOf(
//            RecipeWithInstructions(myRecipe, listOf(ins1, ins2, ins3)),
//            RecipeWithInstructions(myRecipe, listOf(ins1, ins2, ins3)),
//        )
//
//        val recipeAll = RecipeWithIngredientsAndInstructions(myRecipe,listOf(ing1, ing2,ing1, ing2, ing1, ing2, ing1 ,ing2), listOf(ins1, ins2))
//
//        NewDetailsScreen(
//            //recList,
//            // recInsList,
//            {},
//            // recList[0],
//            {},
//            {},
//            {},
//            {}
//            )
//    }
//}

class DetailsScreenViewModelFactory(
    val application: Application,
    val recipeName: String,
    ) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {

        return DetailsScreenViewModel(
            application,
            recipeName,
            DetailsScreenFirebaseRepository(application, Firebase.firestore, Firebase.auth)
        ) as T
    }
}