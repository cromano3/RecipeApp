package com.example.bearrecipebookapp.viewmodel

import android.app.Application
import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import com.example.bearrecipebookapp.data.FilterEntity
import com.example.bearrecipebookapp.data.HomeScreenRepository
import com.example.bearrecipebookapp.data.RecipeAppDatabase
import com.example.bearrecipebookapp.datamodel.HomeScreenDataModel
import com.example.bearrecipebookapp.datamodel.RecipeWithIngredients
import com.example.bearrecipebookapp.datamodel.uiFiltersStateDataModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class HomeScreenViewModel(application: Application): ViewModel() {

    private val repository: HomeScreenRepository
    private val coroutineScope = CoroutineScope(Dispatchers.Main)

    var homeScreenData: LiveData<List<HomeScreenDataModel>>

    var referenceList: LiveData<List<HomeScreenDataModel>>

    var filtersList: LiveData<List<FilterEntity>>

    var unfilteredList: LiveData<List<RecipeWithIngredients>>
    var filteredList1: LiveData<List<RecipeWithIngredients>>
    var filteredList2: LiveData<List<RecipeWithIngredients>>

//    val uiState = MutableStateFlow(HomeScreenUiStateDataModel())

    val uiFiltersState = MutableStateFlow(uiFiltersStateDataModel())

    private var filterCount: Int
    private var isFiltered: Boolean
    private var myFiltersList: MutableList<String>

    private var isSecondFiltered: Boolean


    init {
        val appDb = RecipeAppDatabase.getInstance(application)
        val homeScreenDao = appDb.HomeScreenDao()
        repository = HomeScreenRepository(homeScreenDao)

//        cleanUpSort()
        cleanUpFilters()
        newGetData()

        filterCount = 0
        isFiltered = false
        isSecondFiltered = false
        myFiltersList = mutableListOf()

        homeScreenData = repository.homeScreenData
        filtersList = repository.filtersList
        referenceList = repository.referenceList

        unfilteredList = repository.unfilteredList
        filteredList1 = repository.filteredList1
        filteredList2 = repository.filteredList2

        newGetData()

    }


    private fun newGetData() {
        coroutineScope.launch(Dispatchers.IO) {

            var data = withContext(Dispatchers.IO){repository.getUiData()}

            data = data.sortedWith(compareBy { it.filterName }) as MutableList<FilterEntity>

            uiFiltersState.update{
                it.copy(
                    filtersList = data
                )
            }
        }
    }


    private fun cleanUpFilters() {
        repository.setAllToShown()
        repository.setAllFiltersToOff()
        repository.setAllFiltersToShown()
//        newGetData()
    }



    fun applyFilter(filterName: String) {

//        if (filterCount == 0) {
//            filterCount = 1
//        } else if (filterCount == 1) {
//            filterCount = 2
//        } else if (filterCount == 2) {
//            filterCount = 1
//        }

//        repository.addFilter(filterName)
//        repository.hideOtherFilters(filterName)

//        var myList = uiFiltersState.value.filtersList


        var myName = ""
        var myNum = 0
        var myList: MutableList<FilterEntity> = mutableListOf<FilterEntity>()

        for(x in 0 until uiFiltersState.value.filtersList.size){
            myNum = if(uiFiltersState.value.filtersList[x].filterName == filterName){
                1
            } else{
                0
            }

            myName = uiFiltersState.value.filtersList[x].filterName
            myList.add(FilterEntity(filterName = myName, isActiveFilter = myNum, isShown = myNum))
        }

        myList = myList.sortedWith(compareByDescending{it.isShown}) as MutableList<FilterEntity>


        uiFiltersState.update { currentState ->
            currentState.copy(filtersList = myList)
        }

//        for(y in uiFiltersState.value.filtersList.indices){
//            if(uiFiltersState.value.filtersList[y].filterName == filterName){
//                uiFiltersState.value.filtersList[y].isShown = 1
//            }
//            else
//                uiFiltersState.value.filtersList[y].isShown = 0
//        }






        for (x in 0 until (referenceList.value?.size ?: 0)) {
            for (z in 0 until (referenceList.value?.get(x)?.filtersList?.size ?: 0)) {
                if (referenceList.value?.get(x)?.filtersList?.get(z)?.filterName == filterName) {

                    referenceList.value?.get(x)?.recipeEntity?.recipeName?.let {
                        repository.setIsShown(
                            it, 1
                        )
                    }

                    break

                }
            }
        }


//        var hitCounter = 0
//
//       // var tempList = mutableListOf<HomeScreenDataModel>()
//
//        for(x in 0 until (homeScreenData.value?.size ?: 0)){
//            for(y in 0 until myFiltersList.size){
//                for(z in 0 until (homeScreenData.value?.get(x)?.filtersList?.size ?: 0)){
//                    if(homeScreenData.value?.get(x)?.filtersList?.get(z)?.filterName == myFiltersList[y]){
//                        hitCounter++
//                        break
//                    }
//                }
//            }
//            if(hitCounter == myFiltersList.size && myFiltersList.size > 0){
//                //
////                uiState.homeScreenDataModelList[x].recipeEntity.isShown = 1
//
//                //set isDisplayed = True
//                homeScreenData.value?.get(x)?.recipeEntity?.recipeName?.let {
//                    repository.setIsShown(
//                        it, 1)
//                }
//            }
//            else{
//                //
////                uiState.homeScreenDataModelList[x].recipeEntity.isShown = 0
//
//                //set isDisplayed = False
//                homeScreenData.value?.get(x)?.recipeEntity?.recipeName?.let {
//                    repository.setIsShown(
//                        it, 0)
//                }
//            }
//            hitCounter = 0
//        }

//        uiState.update{
//            it.copy(homeScreenDataModelList = tempList)
//        }
        // uiState.homeScreenDataModelList = tempList
    }


    fun removeFilter(filterName: String) {

//        filterCount = 0

        cleanUpFilters()

        var myName = ""
        var isActive = 0
        var isShown = 1
        var myList: MutableList<FilterEntity> = mutableListOf<FilterEntity>()

        for(x in 0 until uiFiltersState.value.filtersList.size){
            myName = uiFiltersState.value.filtersList[x].filterName
            myList.add(FilterEntity(filterName = myName, isActiveFilter = isActive, isShown = isShown))
        }

//        myList = myList.sortedWith(compareByDescending{it.isShown}) as MutableList<FilterEntity>
//
//
//        uiFiltersState.update { currentState ->
//            currentState.copy(filtersList = myList)
//        }


        myList = myList.sortedWith(compareBy { it.filterName }) as MutableList<FilterEntity>

        uiFiltersState.update{
            it.copy(
                filtersList = myList
            )
        }


//        filterCount--
//
//        //should change to 'filtersList' (LiveData)?????????????????????????
//        myFiltersList.remove(filterName)
//        //??????????????????????????????????????????????????????????
//
//        repository.removeFilter(filterName)
//
//        isFiltered = myFiltersList.size != 0
//
//        var hitCounter = 0
//
//        if(!isFiltered) {
//            cleanUpFilters()
//        }
//        else{
//            for(x in 0 until (referenceList.value?.size ?: 0)){
//                for(y in 0 until myFiltersList.size){
//                    for(z in 0 until (referenceList.value?.get(x)?.filtersList?.size ?: 0)){
//                        if(referenceList.value?.get(x)?.filtersList?.get(z)?.filterName == myFiltersList[y]){
//                            hitCounter++
//                            break
//                        }
//                    }
//                }
//                if(hitCounter == myFiltersList.size && myFiltersList.size > 0){
//                    //set isDisplayed = True
//                    referenceList.value?.get(x)?.recipeEntity?.recipeName?.let {
//                        repository.setIsShown(
//                            it, 1)
//                    }
//                }
//                else{
//                    //set isDisplayed = False
//                    referenceList.value?.get(x)?.recipeEntity?.recipeName?.let {
//                        repository.setIsShown(
//                            it, 0)
//                    }
//                }
//                hitCounter = 0
//            }
//        }
}





//    private fun cleanUpSort() {
//        // UPDATE filter_table SET is_active_filter = 0 WHERE filter_name != Unfiltered
//        repository.clearFilters()
//        // UPDATE filter_table SET is_active_filter = 1 WHERE filter_name = Unfiltered
//        repository.setAsUnfiltered()
//        filterCount = 0
//    }

//    fun addFilter(filterName: String) {
//        //UPDATE filter_table SET is_active_filter = 1 WHERE filter_name = 'filterName'
//        repository.addFilter(filterName)
//        //UPDATE filter_table SET is_active_filter = 0 WHERE filter_name = Unfiltered
//        repository.setAsFiltered()
//        filterCount ++
//    }
//
//
//    fun removeFilter(filterName: String) {
//        //UPDATE filter_table SET is_active_filter = 0 WHERE filter_name = 'filterName'
//        repository.removeFilter(filterName)
//
//        if (filterCount == 1) {
//            //UPDATE filter_table SET is_active_filter = 1 WHERE filter_name = Unfiltered
//            repository.setAsUnfiltered()
//        }
//        filterCount--
//    }





    fun toggleFavorite(recipe: RecipeWithIngredients){
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
