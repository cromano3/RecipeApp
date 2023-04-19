package com.christopherromano.culinarycompanion

import android.annotation.SuppressLint
import android.app.Activity
import android.app.Application
import android.content.Intent
import android.content.pm.ActivityInfo
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.compose.setContent
import androidx.activity.result.ActivityResult
import androidx.activity.result.IntentSenderRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.animation.AnimatedContentScope
import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.CutCornerShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.*
import androidx.compose.runtime.*
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.TileMode
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.text.PlatformTextStyle
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.text.style.LineHeightStyle
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.em
import androidx.compose.ui.unit.sp
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewmodel.compose.LocalViewModelStoreOwner
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.NavHostController
import androidx.navigation.compose.currentBackStackEntryAsState
import com.christopherromano.culinarycompanion.data.repository.FirebaseRepository
import com.christopherromano.culinarycompanion.datamodel.RecipeWithIngredientsAndInstructions
import com.christopherromano.culinarycompanion.ui.*
import com.christopherromano.culinarycompanion.ui.components.*
import com.christopherromano.culinarycompanion.ui.theme.Cabin
import com.christopherromano.culinarycompanion.ui.theme.CulinaryCompanionTheme
import com.christopherromano.culinarycompanion.viewmodel.AppViewModel
import com.christopherromano.culinarycompanion.viewmodel.TopBarViewModel
import com.google.accompanist.navigation.animation.AnimatedNavHost
import com.google.accompanist.navigation.animation.composable
import com.google.accompanist.navigation.animation.rememberAnimatedNavController
import com.google.android.gms.auth.api.identity.BeginSignInResult
import com.google.android.gms.common.api.ApiException
import com.google.firebase.auth.GoogleAuthProvider
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext


class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            CulinaryCompanionTheme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colors.background
                ){
                        CulinaryCompanion()

                }
            }
        }
    }
}

@OptIn(ExperimentalAnimationApi::class)
@SuppressLint("UnusedMaterialScaffoldPaddingParameter")
@Composable
fun CulinaryCompanion(
){
    val owner = LocalViewModelStoreOwner.current

    owner?.let { viewModelStoreOwner ->
        val appViewModel: AppViewModel = viewModel(
            viewModelStoreOwner,
            "AppViewModel",
            AppViewModelFactory(
                LocalContext.current.applicationContext
                        as Application,
            )
        )

        val context = LocalContext.current

        (context as? Activity)?.requestedOrientation = ActivityInfo.SCREEN_ORIENTATION_PORTRAIT

        val appUiState by appViewModel.appUiState.collectAsState()

        val showSplashScreen = rememberSaveable { mutableStateOf(true) }

        val launcher =
            rememberLauncherForActivityResult(ActivityResultContracts.StartIntentSenderForResult()) { result ->
                println("result code is ${ActivityResult.resultCodeToString(result.resultCode)}")
                try {
                    println("big try")
                    val credentials =
                        appViewModel.oneTapClient.getSignInCredentialFromIntent(result.data)
                    val googleIdToken = credentials.googleIdToken
                    val googleCredentials = GoogleAuthProvider.getCredential(googleIdToken, null)
                    appViewModel.firebaseSignInWithGoogle(googleCredentials)
                } catch (it: ApiException) {
                    println("big fail with error $it")
                    appViewModel.endSplashOnFail()
                }
            }

        fun tryFirebaseSignIn(signInResult: BeginSignInResult) {
            println("try firebase sign in")
            val intent =
                IntentSenderRequest.Builder(signInResult.pendingIntent.intentSender).build()
            launcher.launch(intent)
        }


        if (appUiState.googleSignInState == "Success") {
            println("before LE")

            LaunchedEffect(Unit) {
                println("launched")
                appUiState.googleSignInResult?.let { tryFirebaseSignIn(it) }
            }

        }

        if (showSplashScreen.value) {
            SplashScreen(
                showLoading = appUiState.showLoading,
                showSignInButtons = appUiState.showSignInButtons,
                endSplash = appUiState.endSplash,
                isConsentBoxChecked = appUiState.consentBoxChecked,
                consentBoxClicked = { appViewModel.toggleConsentBoxCheck() },
                continueWithoutSignIn = {
                    appViewModel.dontSignIn()
                                        },
                trySignInWithGoogle = {appViewModel.signIn()}
            ) {
                showSplashScreen.value = false
            }
        }
        else {


        val navController: NavHostController = rememberAnimatedNavController()
        val currentBackStackEntry by navController.currentBackStackEntryAsState()

        val currentScreen = currentBackStackEntry?.destination?.route ?: "RecipeScreen"

        val coroutineScope = rememberCoroutineScope()
        val scaffoldState = rememberScaffoldState()


        Scaffold(
            scaffoldState = scaffoldState,
            topBar = {
                CulinaryCompanionTopBar(
                    detailsScreenData = appUiState.detailsScreenTarget,
                    currentScreen = currentScreen,
                    onHomeClick = {
                        if (currentScreen == "ProfileScreen") {
                            navController.popBackStack()
                        }
                        if (currentScreen == "SettingsScreen") {
                            navController.popBackStack()
                            navController.popBackStack()
                        }

                        if (currentScreen == "WeeklyMenuScreen") {
                            navController.navigate("RecipeScreen") {
                                popUpTo(navController.graph.findStartDestination().id) {
                                    saveState = false
                                }
                                launchSingleTop = true
                                restoreState = true

                            }
                        } else {
                            navController.navigate("RecipeScreen") {
                                popUpTo(navController.graph.findStartDestination().id) {
                                    saveState = true
                                }
                                launchSingleTop = true
                                restoreState = true

                            }
                        }

                    },
                    onBackClick = { navController.popBackStack() },
                    onProfileClick = { navController.navigate("ProfileScreen") },
                    onSearchClick = { navController.navigate("SearchScreen") },
                    onFavoriteClick =
                    {
                        favoriteSnackBar(it.recipeEntity, scaffoldState, coroutineScope)
                        appViewModel.toggleDetailsScreenUiFavorite()
                    },
                )
            },
            bottomBar = { CulinaryCompanionBottomBar(navController = navController) },

            snackbarHost = {
                SnackbarHost(it) { data ->
                    Snackbar(
                        modifier = Modifier.padding(8.dp),
                        shape = CutCornerShape(0.dp),
                        backgroundColor = Color(0xFF000000),
                    )
                    {
                        Text(
                            text = data.message,
                            color = Color(0xFFFFFFFF)
                        )
                    }
                }


            }
        ) {
            AnimatedNavHost(
                navController = navController,
                startDestination = "RecipeScreen",
            ) {

                /** Profile Screen */
                composable(
                    route = "ProfileScreen",
                    enterTransition = { fadeIn(animationSpec = tween(300)) },
                    exitTransition = { fadeOut(animationSpec = tween(300)) },
                ) {
                    ProfileScreen(
                        onDetailsClick =
                        {
                            coroutineScope.launch(Dispatchers.Main) {
                                withContext(Dispatchers.IO) { appViewModel.setupDetailsScreen(it) }
                                navController.navigate("DetailsScreen")
                            }
                        },
                        updateLikes = { appViewModel.updateLikes(it) },
                        updateDislikes = { appViewModel.updateDislikes(it) },
                        deleteReview = { commentID, recipeName ->
                            appViewModel.deleteReview(commentID, recipeName)
                        },
                        onRemoveClick = {
                            favoriteSnackBar(
                                it.recipeEntity,
                                scaffoldState,
                                coroutineScope
                            )
                        },
                        onSettingsClick = { navController.navigate("SettingsScreen") },
                    )
                }

                /**Comment Screen*/
                composable(
                    route = "CommentScreen",
                    enterTransition = { fadeIn(animationSpec = tween(300)) },
                    exitTransition = { fadeOut(animationSpec = tween(300)) },
                ) {
                    CommentScreen(
                        commentScreenData = appUiState.reviewScreenTarget,
                        onCancelClick =
                        {
                            appViewModel.markAsReviewed()
                            appViewModel.markReviewAsSynced()
                            navController.popBackStack()
                        },
                        onConfirmClick =
                        { recipeName, reviewText ->
                            appViewModel.markAsReviewed()
                            appViewModel.storeReview(recipeName, reviewText)
                            coroutineScope.launch(Dispatchers.Main) {
                                if (navController.previousBackStackEntry?.destination?.route == "DetailsScreen") {
                                    navController.popBackStack()
                                    navController.popBackStack()
                                    navController.navigate("DetailsScreen")
                                } else {
                                    navController.popBackStack()
                                }
                            }
                        },
                    )
                }

                /** Settings Screen */
                composable(
                    route = "SettingsScreen",
                    enterTransition = { fadeIn(animationSpec = tween(300)) },
                    exitTransition = { fadeOut(animationSpec = tween(300)) }
                ) {
                    SettingsScreen(
                        confirmSignInWithGoogle = { appViewModel.signInWithGoogle() },
                        navigateToLicensesScreen = { navController.navigate("LicensesScreen") }
                    )
                }

                /** Settings Screen */
                composable(
                    route = "LicensesScreen",
                    enterTransition = { fadeIn(animationSpec = tween(300)) },
                    exitTransition = { fadeOut(animationSpec = tween(300)) }
                ) {
                    LicensesScreen()
                }

                /** Home Screen */
                composable(route = "RecipeScreen",
                    enterTransition = {
                        when (initialState.destination.route) {
                            "WeeklyMenuScreen" -> slideIntoContainer(
                                AnimatedContentScope.SlideDirection.Right,
                                animationSpec = tween(300)
                            )
                            "ShoppingScreen" -> slideIntoContainer(
                                AnimatedContentScope.SlideDirection.Right,
                                animationSpec = tween(300)
                            )
                            "SearchScreen" -> slideIntoContainer(
                                AnimatedContentScope.SlideDirection.Up,
                                animationSpec = tween(300)
                            )
                            else -> fadeIn(animationSpec = tween(300))
                        }
                    },
                    exitTransition = {
                        when (targetState.destination.route) {
                            "WeeklyMenuScreen" -> slideOutOfContainer(
                                AnimatedContentScope.SlideDirection.Left,
                                animationSpec = tween(300)
                            )
                            "ShoppingScreen" -> slideOutOfContainer(
                                AnimatedContentScope.SlideDirection.Left,
                                animationSpec = tween(300)
                            )
                            "SearchScreen" -> slideOutOfContainer(
                                AnimatedContentScope.SlideDirection.Down,
                                animationSpec = tween(300)
                            )
                            else -> fadeOut(animationSpec = tween(300))
                        }
                    }
                ) {

                    //Recipe Book Main Screen
                    HomeScreen(
                        onDetailsClick = {
                            coroutineScope.launch(Dispatchers.Main) {
                                withContext(Dispatchers.IO) { appViewModel.setupDetailsScreen(it) }
                                navController.navigate("DetailsScreen")
                            }
                        },
                        onFavoriteClick = {
                            favoriteSnackBar(
                                it.recipeEntity,
                                scaffoldState,
                                coroutineScope
                            )
                        },
                        onMenuClick = {
                            addMenuSnackBar(
                                it.recipeEntity,
                                scaffoldState,
                                coroutineScope
                            )
                        },
                        onMenuRemovedClick = {
                            removeMenuSnackBar(
                                it.recipeEntity,
                                scaffoldState,
                                coroutineScope
                            )
                        },
                        onCreateRecipeClick = { navController.navigate("AddRecipeScreen") }
                    )
                }

                /** Weekly Menu Screen */
                composable(
                    route = "WeeklyMenuScreen",
                    enterTransition = {
                        when (initialState.destination.route) {
                            "RecipeScreen" -> slideIntoContainer(
                                AnimatedContentScope.SlideDirection.Left,
                                animationSpec = tween(300)
                            )
                            "ShoppingScreen" -> slideIntoContainer(
                                AnimatedContentScope.SlideDirection.Right,
                                animationSpec = tween(300)
                            )
                            else -> fadeIn(animationSpec = tween(300))
                        }
                    },
                    exitTransition = {
                        when (targetState.destination.route) {
                            "RecipeScreen" -> slideOutOfContainer(
                                AnimatedContentScope.SlideDirection.Right,
                                animationSpec = tween(300)
                            )
                            "ShoppingScreen" -> slideOutOfContainer(
                                AnimatedContentScope.SlideDirection.Left,
                                animationSpec = tween(300)
                            )
                            else -> fadeOut(animationSpec = tween(300))
                        }
                    },
                ) {
                    //Weekly menu screen
                    MenuScreen(
                        userIsOnlineStatus = appUiState.userIsOnlineStatus,
                        onDetailsClick = {
                            coroutineScope.launch(Dispatchers.Main) {
                                withContext(Dispatchers.IO) { appViewModel.setupDetailsScreen(it) }
                                navController.navigate("DetailsScreen")
                            }
                        },
                        onFavoriteClick = {
                            favoriteSnackBar(
                                it.recipeEntity,
                                scaffoldState,
                                coroutineScope
                            )
                        },
                        onAddedToFavoriteFromAlertClick = {
                            addFavoriteSnackBar(
                                it,
                                scaffoldState,
                                coroutineScope
                            )
                        },
                        onRemoveClick = {
                            removeFavoriteSnackBar(
                                it.recipeEntity,
                                scaffoldState,
                                coroutineScope
                            )
                        },
                        onCompleteClick = {
                            completedCookingSnackBar(
                                it.recipeEntity,
                                scaffoldState,
                                coroutineScope
                            )
                        },
                        onSystemBackClick = {
                            navController.navigate("RecipeScreen") {
                                popUpTo(navController.graph.findStartDestination().id)
                                restoreState = true
                            }
                        },
                        onConfirmWriteReviewClick = {
                            coroutineScope.launch(Dispatchers.Main) {
                                withContext(Dispatchers.IO) { appViewModel.setupReviewScreen(it) }
                                navController.navigate("CommentScreen")
                            }
                        },
                        onAddRecipeClick = {
                            navController.navigate("RecipeScreen") {
                                popUpTo(navController.graph.findStartDestination().id) {
                                    saveState = false
                                }
                                launchSingleTop = true
                                restoreState = true
                            }

                            val backStackEntry = navController.currentBackStackEntry
                            val currentRoute = backStackEntry?.destination?.route

                            if (currentRoute == "SearchScreen") {
                                navController.popBackStack()
                            }

                        }
                    )
                }

                /** Shopping List Screen */
                composable(
                    route = "ShoppingScreen",
                    enterTransition = {
                        when (initialState.destination.route) {
                            "RecipeScreen" -> slideIntoContainer(
                                AnimatedContentScope.SlideDirection.Left,
                                animationSpec = tween(300)
                            )
                            "WeeklyMenuScreen" -> slideIntoContainer(
                                AnimatedContentScope.SlideDirection.Left,
                                animationSpec = tween(300)
                            )
                            else -> fadeIn(animationSpec = tween(300))
                        }
                    },
                    exitTransition = {
                        when (targetState.destination.route) {
                            "RecipeScreen" -> slideOutOfContainer(
                                AnimatedContentScope.SlideDirection.Right,
                                animationSpec = tween(300)
                            )
                            "WeeklyMenuScreen" -> slideOutOfContainer(
                                AnimatedContentScope.SlideDirection.Right,
                                animationSpec = tween(300)
                            )
                            else -> fadeOut(animationSpec = tween(300))
                        }
                    }
                ) {
                    ShoppingListScreen(
                        onDetailsClick = {
                            coroutineScope.launch(Dispatchers.Main) {
                                withContext(Dispatchers.IO) { appViewModel.setupDetailsScreen(it) }
                                navController.navigate("DetailsScreen")
                            }
                        },
                        onSystemBackClick = {
                            navController.navigate("RecipeScreen") {
                                popUpTo(navController.graph.findStartDestination().id)
                                restoreState = true
                            }
                        },
                        onAddRecipeClick = {
                            navController.navigate("RecipeScreen") {

                                popUpTo(navController.graph.findStartDestination().id) {
                                    saveState = true
                                }
                                launchSingleTop = true
                                restoreState = true

                            }

                            val backStackEntry = navController.currentBackStackEntry
                            val currentRoute = backStackEntry?.destination?.route

                            if (currentRoute == "SearchScreen") {
                                navController.popBackStack()
                            }
                        }
                    )
                }

                /** Details Screen */
                composable(
                    route = "DetailsScreen",
                    enterTransition = { fadeIn(animationSpec = tween(300)) },
                    exitTransition = { fadeOut(animationSpec = tween(300)) },
                ) {
                    NewDetailsScreen(
                        recipeData = appUiState.detailsScreenTarget,
                        storeRating = { appViewModel.storeRating(it) },
                        markAsRated = { appViewModel.markAsRated() },
                        markAsReviewed = { appViewModel.markAsReviewed() },
                        addedToFavoriteUiUpdate = { appViewModel.addedToFavoriteUiUpdate() },
                        updateLikes = { appViewModel.updateLikes(it) },
                        updateDislikes = { appViewModel.updateDislikes(it) },
                        onMenuAddClick = {
                            appViewModel.updateDetailsScreenUiOnMenuStatus()
                            addMenuSnackBar(it.recipeEntity, scaffoldState, coroutineScope)
                        },
                        onMenuRemoveClick = {
                            appViewModel.updateDetailsScreenUiOnMenuStatus()
                            removeMenuSnackBar(it.recipeEntity, scaffoldState, coroutineScope)
                        },
                        onFinishedCookingClick = {
                            appViewModel.updateDetailsScreenUiOnMenuStatus()
                            completedCookingSnackBar(it.recipeEntity, scaffoldState, coroutineScope)
                        },
                        onIMadeThisClick = {
                            completedCookingSnackBar(
                                it.recipeEntity,
                                scaffoldState,
                                coroutineScope
                            )
                        },
                        showAddedToFavoritesSnackBarMessage = {
                            addFavoriteSnackBar(
                                it,
                                scaffoldState,
                                coroutineScope
                            )
                        },
                        navigateToCommentScreen = {
                            coroutineScope.launch(Dispatchers.Main) {
                                withContext(Dispatchers.IO) { appViewModel.setupReviewScreen(it) }
                                navController.navigate("CommentScreen")
                            }
                        },
                        submitReport = {
                            reportSubmittedSnackBar(scaffoldState, coroutineScope)
                            appViewModel.submitReport(it)
                        }
                    )
                }

                /** Search Screen */
                composable(
                    route = "SearchScreen",
                    enterTransition = {
                        when (initialState.destination.route) {
                            "RecipeScreen" -> slideIntoContainer(
                                AnimatedContentScope.SlideDirection.Down,
                                animationSpec = tween(300)
                            )
                            else -> fadeIn(animationSpec = tween(300))
                        }
                    },
                    exitTransition = {
                        when (targetState.destination.route) {
                            "RecipeScreen" -> slideOutOfContainer(
                                AnimatedContentScope.SlideDirection.Up,
                                animationSpec = tween(300)
                            )
                            else -> fadeOut(animationSpec = tween(300))
                        }
                    },
                ) {
                    SearchScreen(
                        onGoBackClick = { navController.popBackStack() },
                        onDetailsClick = {
                            coroutineScope.launch(Dispatchers.Main) {
                                withContext(Dispatchers.IO) { appViewModel.setupDetailsScreen(it) }
                                navController.navigate("DetailsScreen")
                            }
                        },
                        onFavoriteClick = { favoriteSnackBar(it, scaffoldState, coroutineScope) },
                        onMenuClick = { menuSnackBar(it, scaffoldState, coroutineScope) },
                    )
                }

            }

        }
    }
    }
}


@Composable
fun CulinaryCompanionBottomBar(
    navController: NavHostController,
)
{

    val backStackEntry by navController.currentBackStackEntryAsState()
    val currentRoute = backStackEntry?.destination?.route

    if(currentRoute != "CommentScreen" && currentRoute != "LicensesScreen") {
        BottomNavigation(
            backgroundColor = Color(0xFF682300),
            contentColor = Color(0xFFd8af84)
        ) {

            val routes = listOf("RecipeScreen", "WeeklyMenuScreen", "ShoppingScreen")

            var selected: Boolean
            var alpha: Float


            routes.forEach {
                if (it == currentRoute) {
                    selected = true
                    alpha = 1f
                } else {
                    selected = false
                    alpha = 0.5f
                }
                BottomNavigationItem(
                    selected = selected,
                    onClick = {
                        if (currentRoute == "SearchScreen" && it == "RecipeScreen") {
                            navController.popBackStack()
                        }
                        else if (currentRoute == "AddRecipeScreen" && it == "RecipeScreen") {
                            navController.popBackStack()
                        }
                        else if (currentRoute == "DetailsScreen" && navController.previousBackStackEntry?.destination?.route == "ProfileScreen") {
                            navController.popBackStack()
                            navController.popBackStack()
                        }
                        else if(currentRoute == "DetailsScreen"){
                            navController.popBackStack()
                        }
                        else if (currentRoute == "ProfileScreen") {
                            navController.popBackStack()
                        }
                        else if (currentRoute == "SettingsScreen") {
                            navController.popBackStack()
                            navController.popBackStack()
                        }

                        if(currentRoute != it){
                            if(currentRoute == "WeeklyMenuScreen"){
                                navController.navigate(it) {

                                    popUpTo(navController.graph.findStartDestination().id) {
                                        saveState = false
                                    }
                                    launchSingleTop = true
                                    restoreState = true

                                }
                            }
                            else{
                                navController.navigate(it) {

                                    popUpTo(navController.graph.findStartDestination().id) {
                                        saveState = true
                                    }
                                    launchSingleTop = true
                                    restoreState = true

                                }
                            }
                        }

                    },
                    icon = {
                        when (it) {
                            "RecipeScreen" -> Icon(
                                Icons.Outlined.MenuBook,
                                contentDescription = null
                            )
                            "WeeklyMenuScreen" -> Icon(
                                Icons.Outlined.Restaurant,
                                contentDescription = null
                            )
                            "ShoppingScreen" -> Icon(
                                Icons.Outlined.ShoppingCart,
                                contentDescription = null
                            )
                        }
                    },
                    modifier = Modifier
                        .alpha(alpha)
                        .background(
                            brush = Brush.horizontalGradient(
                                listOf(
                                    Color(0xFFFFFFFF),
                                    Color(0xFFFFFFFF)
                                )
                            ), alpha = 0.1f
                        )

                )
            }

        }
    }
}

@OptIn(ExperimentalMaterialApi::class)
@Composable
fun CulinaryCompanionTopBar(
    detailsScreenData: RecipeWithIngredientsAndInstructions,
    currentScreen: String,
    onHomeClick: () -> Unit,
    onBackClick: () -> Unit,
    onProfileClick: () -> Unit,
    onSearchClick: () -> Unit,
    onFavoriteClick: (RecipeWithIngredientsAndInstructions) -> Unit,
    ) {

    val owner = LocalViewModelStoreOwner.current

    owner?.let { viewModelStoreOwner ->
        val topBarViewModel: TopBarViewModel = viewModel(
            viewModelStoreOwner,
            "TopBarViewModel",
            TopBarViewModelFactory(
                LocalContext.current.applicationContext
                        as Application,
            )
        )

        val uiState by topBarViewModel.uiState.collectAsState()

        val showResults by topBarViewModel.showResults.observeAsState()

        var title = ""
        var show = false
        var showShare = false
        var showSearchButton = false
        var showTitle = false
        var showSearchField = false
        var showIcon = false
        var showIcon2 = false
        var icon = Icons.Outlined.Home
        var icon2 = Icons.Outlined.Home
        var clickEffectLeft = onHomeClick
        var clickEffectRight = onHomeClick
        var textModifier = Modifier.wrapContentWidth()

        val focusManager = LocalFocusManager.current
        val focusRequester = remember { FocusRequester() }


        if(showResults == 0 && currentScreen == "SearchScreen") {
            LaunchedEffect(Unit) {
                focusRequester.requestFocus()
            }
        }

        if(showResults == 1 && currentScreen == "SearchScreen"){
            topBarViewModel.updateSearchText()
        }

        when (currentScreen) {
            "RecipeScreen" -> {
                show = true
                textModifier = Modifier.wrapContentWidth()
                icon = Icons.Outlined.Home
                icon2 = Icons.Outlined.Person
                clickEffectLeft = onHomeClick
                clickEffectRight = onProfileClick
                showSearchButton = true
                showTitle = false
                showSearchField = false
                showShare = false
                showIcon = false
                showIcon2 = true
            }
            "WeeklyMenuScreen" -> {
                show = true
                textModifier = Modifier.wrapContentWidth()
                title = "My Menu"
                icon = Icons.Outlined.Home
                icon2 = Icons.Outlined.Person
                clickEffectLeft = onHomeClick
                clickEffectRight = onProfileClick
                showTitle = true
                showSearchField = false
                showSearchButton = false
                showShare = false
                showIcon = true
                showIcon2 = true
            }
            "ShoppingScreen" -> {
                show = true
                textModifier = Modifier.wrapContentWidth()
                title = "Shopping list"
                icon = Icons.Outlined.Home
                icon2 = Icons.Outlined.Person
                clickEffectLeft = onHomeClick
                clickEffectRight = onProfileClick
                showTitle = true
                showSearchField = false
                showSearchButton = false
                showShare = false
                showIcon = true
                showIcon2 = true
            }
            "ProfileScreen" -> {
                show = true
                textModifier = Modifier.wrapContentWidth()
                title = "My Profile"
                icon = Icons.Outlined.ArrowBack
                icon2 = Icons.Outlined.Home
                clickEffectLeft = onBackClick
                clickEffectRight = onHomeClick
                showTitle = true
                showSearchField = false
                showSearchButton = false
                showShare = false
                showIcon = true
                showIcon2 = true
            }
            "SettingsScreen" -> {
                show = true
                textModifier = Modifier.wrapContentWidth()
                title = "Settings"
                icon = Icons.Outlined.ArrowBack
                icon2 = Icons.Outlined.Home
                clickEffectLeft = onBackClick
                clickEffectRight = onHomeClick
                showTitle = true
                showSearchField = false
                showSearchButton = false
                showShare = false
                showIcon = true
                showIcon2 = true
            }
            "SearchScreen" -> {
                show = true
                textModifier = Modifier.width(0.dp)
                title = ""
                icon = Icons.Outlined.ArrowBack
                icon2 = Icons.Outlined.Person
                clickEffectLeft = onBackClick
                clickEffectRight = {}
                showTitle = false
                showSearchField = true
                showSearchButton = false
                showShare = false
                showIcon = true
                showIcon2 = false

            }
            "AddRecipeScreen" -> {
                show = true
                textModifier = Modifier.wrapContentWidth()
                title = "Add Custom Recipe"
                icon = Icons.Outlined.ArrowBack
                icon2 = Icons.Outlined.Person
                clickEffectLeft = onBackClick
                clickEffectRight = {}
                showTitle = true
                showSearchField = false
                showSearchButton = false
                showShare = false
                showIcon = true
                showIcon2 = false

            }
            "CommentScreen" -> {
                show = true
                textModifier = Modifier.wrapContentWidth()
                title = "Leave a Tip/Comment"
                icon = Icons.Outlined.ArrowBack
                icon2 = Icons.Outlined.Person
                clickEffectLeft = {}
                clickEffectRight = {}
                showTitle = true
                showSearchField = false
                showSearchButton = false
                showShare = false
                showIcon = false
                showIcon2 = false

            }
            "LicensesScreen" -> {
                show = true
                textModifier = Modifier.wrapContentWidth()
                title = "Licenses Information"
                icon = Icons.Outlined.ArrowBack
                icon2 = Icons.Outlined.Person
                clickEffectLeft = onBackClick
                clickEffectRight = {}
                showTitle = true
                showSearchField = false
                showSearchButton = false
                showShare = false
                showIcon = true
                showIcon2 = false

            }
            "DetailsScreen" -> {
                show = true

                title = detailsScreenData.recipeEntity.recipeName
                textModifier = Modifier.width(200.dp)
                icon = Icons.Outlined.ArrowBack

                clickEffectLeft = onBackClick
                clickEffectRight = {
                    topBarViewModel.toggleFavorite(detailsScreenData)
                    onFavoriteClick(detailsScreenData)
                }
                showTitle = true
                showSearchField = false
                showSearchButton = false
                showShare = true

                showIcon = true

                showIcon2 = true

                println(detailsScreenData.recipeEntity.isFavorite)
                icon2 = if (detailsScreenData.recipeEntity.isFavorite == 0) {
                    Icons.Outlined.FavoriteBorder

                } else {
                    Icons.Outlined.Favorite
                }
            }
        }

            if (show) {
            Surface(
                modifier = Modifier
                    .fillMaxWidth()
                    .wrapContentHeight(),
                color = Color.Transparent){
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(64.dp)
                        .background(Color(0xFF682300)),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.Start
                ) {
                    Spacer(Modifier.size(8.dp))

                    //shows clickable only search button on main screen to go to search screen
                    if (showSearchButton) {
                        Surface(
                            Modifier
                                .height(44.dp)
                                .width(300.dp)
                                .clickable(onClick = onSearchClick)
                                .padding(start = 8.dp, top = 0.dp, bottom = 0.dp)
                                .border(
                                    width = 2.dp,
                                    brush = (Brush.horizontalGradient(
                                        colors = listOf(Color(0xFFd8af84), Color(0xFFb15f33)),
                                        tileMode = TileMode.Mirror
                                    )),
                                    shape = RoundedCornerShape(25.dp)
                                ),
                            shape = RoundedCornerShape(25.dp)
                        ) {
                            TextField(
                                value = "",
                                onValueChange = {},
                                modifier = Modifier,
                                enabled = false,
                                readOnly = true,
                                leadingIcon = {
                                    Icon(
                                        Icons.Outlined.Search,
                                        contentDescription = null,
                                        tint = Color(0xFF000000)
                                    )
                                },
                                shape = RoundedCornerShape(25.dp),
                                colors = TextFieldDefaults.textFieldColors(
                                    backgroundColor = Color(
                                        0xFFd8af84
                                    ), textColor = Color(0xFF000000)
                                )
                            )
                        }
                    }
                    else if(showIcon){
                        IconButton(
                            onClick = {
                                clickEffectLeft()
                                      },
                            modifier =
                            Modifier
                                .size(48.dp)
                                .align(Alignment.CenterVertically)
                                .background(color = Color.Transparent)
                                .padding(start = 8.dp)
                                .border(
                                    width = 2.dp,
                                    brush = (Brush.horizontalGradient(
                                        colors = listOf(Color(0xFFd8af84), Color(0xFFb15f33)),
                                        tileMode = TileMode.Mirror,
                                    )),
                                    shape = CircleShape
                                ),
                        ) {
                            Icon(
                                imageVector = icon,
                                contentDescription = null,
                                modifier = Modifier
                                    .size(24.dp),
                                tint = Color(0xFFd8af84)
                            )
                        }
                    }
                    else{
                        Spacer(Modifier.width(48.dp))

                    }

                    Spacer(Modifier.weight(1f))

                    if(showTitle){
                        Text(
                            text = title,
                            modifier = textModifier,
                            color = Color(0xFFd8af84),
                            textAlign = TextAlign.Center,
                            fontSize = 22.sp,
                            fontFamily = Cabin,
                            fontWeight = FontWeight.Bold,
                            lineHeight = 1.0.em,
                            style = MaterialTheme.typography.h4.merge(
                                TextStyle(
                                    platformStyle = PlatformTextStyle(
                                        includeFontPadding = false
                                    ),
                                    lineHeightStyle = LineHeightStyle(
                                        alignment = LineHeightStyle.Alignment.Top,
                                        trim = LineHeightStyle.Trim.FirstLineTop
                                    )
                                )
                            ),
                        )
                    }


                    Spacer(Modifier.weight(1f))

                    if (showShare) {



                        val context = LocalContext.current


                        IconButton(
                            onClick = {

                                var myString = detailsScreenData.recipeEntity.recipeName + '\n' + '\n'

                                for(x in detailsScreenData.ingredientsList.indices){
                                    myString += detailsScreenData.ingredientsList[x].ingredientName + '\n'
                                }

                                myString += '\n'

                                for(x in detailsScreenData.instructionsList.indices){
                                    myString += detailsScreenData.instructionsList[x].instruction + '\n'
                                }

                                val sendIntent: Intent = Intent().apply {
                                    action = Intent.ACTION_SEND
                                    putExtra(Intent.EXTRA_TEXT, myString)
                                    type = "text/plain"
                                }

                                val shareIntent = Intent.createChooser(sendIntent, null)

                                context.startActivity(shareIntent)

                            },
                            modifier = Modifier
                                .size(48.dp)
                                .align(Alignment.CenterVertically)
                                .background(color = Color.Transparent)
                                .padding(start = 8.dp)
                                .border(
                                    width = 2.dp,
                                    brush = (Brush.horizontalGradient(
                                        colors = listOf(Color(0xFFd8af84), Color(0xFFb15f33)),
                                        tileMode = TileMode.Mirror,
                                    )),
                                    shape = CircleShape
                                ),
                        ) {
                            Icon(
                                imageVector = Icons.Outlined.Share,
                                contentDescription = null,
                                modifier = Modifier
                                    .size(24.dp),
                                tint = Color(0xFFd8af84)
                            )
                        }
                        Spacer(Modifier.size(16.dp))
                    }



                    val gradientWidthButton = with(LocalDensity.current) { 48.dp.toPx() }


                    if(currentScreen == "AddRecipeScreen" || currentScreen == "CommentScreen"){
                        Spacer(Modifier.width(48.dp))
                    }

                    if(showIcon2) {
                        FloatingActionButton(
                            onClick = {
                                clickEffectRight()
                            },
                            elevation = FloatingActionButtonDefaults.elevation(8.dp),
                            modifier = Modifier
                                .padding(end = 8.dp)
                                .border(
                                    width = 2.dp,
                                    brush = (Brush.horizontalGradient(
                                        colors = listOf(Color(0xFFd8af84), Color(0xFFb15f33)),
                                        endX = gradientWidthButton,
                                        tileMode = TileMode.Mirror,
                                    )),
                                    shape = CircleShape
                                )
                                .size(48.dp)
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
                                icon2,
                                tint = Color(0xFFd8af84),
                                modifier = Modifier.size(24.dp),
                                contentDescription = null
                            )
                        }
                    }
                    if(showSearchField){

                        val interactionSource = remember { MutableInteractionSource() }

                        BasicTextField(
                            value =  uiState.currentInput,
                            onValueChange =
                            { topBarViewModel.updatePreview( it, it.text) },
                            modifier = Modifier
                                .focusRequester(focusRequester)
                                .background(
                                    color = Color(0xFFd8af84),
                                    shape = RoundedCornerShape(25.dp)
                                )
                                .height(44.dp)
                                .width(300.dp)
                                .border(
                                    width = 2.dp,
                                    brush = (Brush.horizontalGradient(
                                        colors = listOf(Color(0xFFd8af84), Color(0xFFb15f33)),
                                        tileMode = TileMode.Mirror
                                    )),
                                    shape = RoundedCornerShape(25.dp)
                                ),
                            keyboardOptions = KeyboardOptions(imeAction = ImeAction.Search),
                            keyboardActions = KeyboardActions(
                                onSearch = {
                                    topBarViewModel.liveSearchForClick()
                                    focusManager.clearFocus()
                                }),
                            singleLine = true,
                        ){
                            TextFieldDefaults.TextFieldDecorationBox(
                                value = uiState.currentInput.text,
                                innerTextField = it,
                                enabled = true,
                                singleLine = true,
                                visualTransformation = VisualTransformation.None,
                                interactionSource = interactionSource,
                                leadingIcon = { Icon(Icons.Outlined.Search, contentDescription = null, tint = Color(0xFF000000)) },
                                trailingIcon =
                                {
                                    if(uiState.currentInput.text.isNotEmpty()){
                                        Surface(
                                            color = Color.Transparent,
                                            modifier = Modifier.clickable {  topBarViewModel.updatePreview( TextFieldValue(""), "") }
                                        )
                                        {
                                            Icon(
                                                Icons.Outlined.Close,
                                                contentDescription = null,
                                                tint = Color(0xFF000000)
                                            )
                                        }
                                    }
                                },

                                contentPadding = TextFieldDefaults.textFieldWithoutLabelPadding(
                                    top = 0.dp, bottom = 0.dp
                                )
                            )
                        }

                        Spacer(Modifier.weight(1f))
                    }
                    Spacer(Modifier.size(8.dp))
                }
            }

//            }
        }
    }
}

class TopBarViewModelFactory(
    val application: Application,
) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {

        return TopBarViewModel(
            application,
        ) as T
    }
}

class AppViewModelFactory(
    val application: Application,
) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {

        return AppViewModel(
            application,
            FirebaseRepository(application, Firebase.firestore, Firebase.auth),
        ) as T
    }
}
