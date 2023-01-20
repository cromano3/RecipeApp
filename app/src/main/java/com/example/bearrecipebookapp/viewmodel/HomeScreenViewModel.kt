package com.example.bearrecipebookapp.viewmodel

import android.app.Application
import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import com.example.bearrecipebookapp.data.RecipeAppDatabase
import com.example.bearrecipebookapp.data.entity.FilterEntity
import com.example.bearrecipebookapp.data.entity.RecipeEntity
import com.example.bearrecipebookapp.data.repository.HomeScreenRepository
import com.example.bearrecipebookapp.datamodel.HomeScreenDataModel
import com.example.bearrecipebookapp.datamodel.RecipeWithIngredients
import com.example.bearrecipebookapp.datamodel.UiAlertStateHomeScreenDataModel
import com.example.bearrecipebookapp.datamodel.UiFiltersStateDataModel
import kotlinx.coroutines.*
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.update

class HomeScreenViewModel(application: Application): ViewModel() {

    private val repository: HomeScreenRepository
    private val coroutineScope = CoroutineScope(Dispatchers.Main)

    var homeScreenData: LiveData<List<HomeScreenDataModel>>

    var referenceList: LiveData<List<HomeScreenDataModel>>



    //GOOD!!!
    var filtersList: LiveData<List<FilterEntity>>
    //GOOD!!

    var shownRecipeList: LiveData<List<RecipeWithIngredients>>




    var unfilteredList: LiveData<List<RecipeWithIngredients>>

    var filteredList1: LiveData<List<RecipeWithIngredients>>
    var filteredList2: LiveData<List<RecipeWithIngredients>>

//    val uiState = MutableStateFlow(HomeScreenUiStateDataModel())

    val uiFiltersState = MutableStateFlow(UiFiltersStateDataModel())
    val uiAlertState = MutableStateFlow(UiAlertStateHomeScreenDataModel())

    private var filterCount: Int
    private var myFiltersList: MutableList<String>

    private var isSecondFiltered: Boolean


    init {
        val appDb = RecipeAppDatabase.getInstance(application)
        val homeScreenDao = appDb.HomeScreenDao()
        repository = HomeScreenRepository(homeScreenDao)

//        cleanUpSort()
//        cleanUpFilters()
//        newGetData()

        filterCount = 0
        isSecondFiltered = false
        myFiltersList = mutableListOf()

        homeScreenData = repository.homeScreenData
        filtersList = repository.filtersList
        referenceList = repository.referenceList

        unfilteredList = repository.unfilteredList
        filteredList1 = repository.filteredList1
        filteredList2 = repository.filteredList2

//        newGetData()

        //

        coroutineScope.launch(Dispatchers.IO) {
            async(Dispatchers.IO) { repository.cleanRecipes() }
            async(Dispatchers.IO) { repository.cleanFilters() }
        }



        shownRecipeList = repository.shownRecipeList
    }

    fun cancelScroll(){
        uiFiltersState.update { currentState ->
            currentState.copy(
                triggerScroll = false
            )
        }
    }


    fun triggerAlert(recipe: RecipeWithIngredients){
        uiAlertState.update { currentState ->
            currentState.copy(
                showAlert = true,
                recipe = recipe
            )
        }
    }

    fun cancelAlert(){
        uiAlertState.update { currentState ->
            currentState.copy(
                showAlert = false,
                recipe = RecipeWithIngredients(RecipeEntity(), listOf())
            )
        }
    }


    fun filterBy(filter: FilterEntity){
        coroutineScope.launch(Dispatchers.Main) {

        uiFiltersState.update { currentState ->
            currentState.copy(isWorking = true)
        }
            if (filter.isActiveFilter == 0 ||
                filter.isActiveFilter == 1
            ) {
                uiFiltersState.update { currentState ->
                    currentState.copy(
                        showAllRecipes = false,
                    )
                }

                //allows fade out animation
                delay(300)

                uiFiltersState.update { currentState ->
                    currentState.copy(
                        isFiltered = true,
                    )
                }


                withContext(Dispatchers.IO) { repository.removeOtherFilters(filter.filterName) }
                withContext(Dispatchers.IO) { repository.filterBy(filter.filterName)  }


                var match = false

                for (x in 0 until (referenceList.value?.size ?: 0)) {
                    for (y in 0 until (referenceList.value?.get(x)?.filtersList?.size ?: 0)) {
                        if ((referenceList.value?.get(x)?.filtersList?.get(y)?.filterName
                                ?: 0) == filter.filterName
                        ) {
                            match = true
                            break
                        }
                    }
                    if (!match) {
                        referenceList.value?.get(x)?.recipeEntity?.recipeName?.let {
                            withContext(Dispatchers.IO) { repository.setRecipeToNotShown(it) }
                        }
                    } else {
                        referenceList.value?.get(x)?.recipeEntity?.recipeName?.let {
                            withContext(Dispatchers.IO) { repository.setRecipeToShown(it) }
                        }
                    }
                    match = false
                }

                uiFiltersState.update { currentState ->
                    currentState.copy(
                        showAllRecipes = true,
                        triggerScroll = true
                    )
                }

            } else if (filter.isActiveFilter == 2) {

                uiFiltersState.update { currentState ->
                    currentState.copy(showAllRecipes = false)
                }
                //this delay allows the fade out animation to take place/finish before getting cut
                //off. It could/should be reduced to the duration of the animation itself.
                delay(300)

                uiFiltersState.update { currentState ->
                    currentState.copy(
                        isFiltered = false,
                    )
                }

//                coroutineScope.launch(Dispatchers.IO) {
//                println("zero")
                    withContext(Dispatchers.IO) { repository.cleanRecipes() }
//                println("first")
                    withContext(Dispatchers.IO) { repository.cleanFilters() }
//                println("second")
//                }

                uiFiltersState.update { currentState ->
                    currentState.copy(
                        showAllRecipes = true,
                    )
                }
            }

        uiFiltersState.update { currentState ->
            currentState.copy(
                isWorking = false,
                )
        }

        }

    }




    fun toggleFavorite(recipe: RecipeWithIngredients){
        if(recipe.recipeEntity.isFavorite == 0){
            repository.updateFavorite(recipe.recipeEntity.recipeName, 1)
        }
        else if(recipe.recipeEntity.isFavorite == 1){
            repository.updateFavorite(recipe.recipeEntity.recipeName, 0)
        }
    }


    fun toggleMenu(recipe: RecipeWithIngredients){

        repository.cleanIngredients()
        repository.cleanShoppingFilters()

        if(recipe.recipeEntity.onMenu == 0){
            for(x in 0 until recipe.ingredientsList.size){
                repository.updateQuantityNeeded(recipe.ingredientsList[x].ingredientName, recipe.ingredientsList[x].quantityNeeded + 1)
            }
            repository.updateMenu(recipe.recipeEntity.recipeName, 1)
        }
        else if(recipe.recipeEntity.onMenu == 1){
            for(x in 0 until recipe.ingredientsList.size){
                repository.updateQuantityNeeded(recipe.ingredientsList[x].ingredientName, recipe.ingredientsList[x].quantityNeeded - 1)
                repository.setIngredientQuantityOwned(recipe.ingredientsList[x], 0)
            }
            repository.updateMenu(recipe.recipeEntity.recipeName, 0)
        }
    }

    fun setDetailsScreenTarget(recipeName: String){
        repository.setDetailsScreenTarget(recipeName)
    }
}
