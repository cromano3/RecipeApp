package com.example.bearrecipebookapp.data

import androidx.lifecycle.LiveData
import com.example.bearrecipebookapp.datamodel.HomeScreenDataModel
import com.example.bearrecipebookapp.datamodel.RecipeWithIngredients
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

class HomeScreenRepository(private val homeScreenDao: HomeScreenDao) {

    private val coroutineScope = CoroutineScope(Dispatchers.Main)

    var homeScreenData: LiveData<List<HomeScreenDataModel>> = homeScreenDao.getData()
    var filtersList: LiveData<List<FilterEntity>> = homeScreenDao.getFilters()
    var referenceList: LiveData<List<HomeScreenDataModel>> = homeScreenDao.getReferenceList()

    var unfilteredList: LiveData<List<RecipeWithIngredients>> = homeScreenDao.getUnfilteredList()
    var filteredList1: LiveData<List<RecipeWithIngredients>> = homeScreenDao.getFilteredList1()
    var filteredList2: LiveData<List<RecipeWithIngredients>> = homeScreenDao.getFilteredList2()



    var newRecipeList: LiveData<List<RecipeWithIngredients>> = homeScreenDao.getRecipeList()


    fun cleanShoppingFilters(){
        coroutineScope.launch(Dispatchers.IO) {
            homeScreenDao.cleanShoppingFilters()
        }
    }

    fun cleanIngredients(){
        coroutineScope.launch(Dispatchers.IO) {
            homeScreenDao.cleanIngredients()
        }
    }




    suspend fun removeOtherFilters(name: String){
//        coroutineScope.launch(Dispatchers.IO) {
//            println("ZeroA")
//            delay(5000)
//            println("FirstA")
            homeScreenDao.removeOtherFilters(name)
//            println("SecondA")
//            delay(5000)
//            println("ThirdA")
//        }
    }

    suspend fun filterBy(name: String){
//        coroutineScope.launch(Dispatchers.IO) {

//            println("ZeroB")
//            delay(5000)
//            println("FirstB")
            homeScreenDao.filterBy(name)
//            println("SecondB")
//            delay(5000)
//            println("ThirdB")
//        }
    }

    //

    fun setRecipeToNotShown(name: String){
        coroutineScope.launch(Dispatchers.IO) {
            homeScreenDao.setRecipeToNotShown(name)
        }
    }

    fun setRecipeToShown(name: String){
        coroutineScope.launch(Dispatchers.IO) {
            homeScreenDao.setRecipeToShown(name)
        }
    }

    //

    suspend fun cleanFilters(){
//        coroutineScope.launch(Dispatchers.IO) {
//        println("zeroB")
//        delay(5000)
//        println("firstB")
            homeScreenDao.cleanFilters()
//        }
    }

    suspend fun cleanRecipes(){
//        coroutineScope.launch(Dispatchers.IO) {
//        println("zeroA")
//        delay(5000)
//        println("firstA")
            homeScreenDao.cleanRecipes()

//        }
    }








    //////////////////

    suspend fun getUiData(): MutableList<FilterEntity>{
        return homeScreenDao.getUiData()
    }

    suspend fun newGetData(): MutableList<HomeScreenDataModel>{
           return homeScreenDao.newGetData()
    }


    fun setAllFiltersToShown(){
        coroutineScope.launch(Dispatchers.IO) {
            homeScreenDao.setAllFiltersToShown()
        }
    }

    ///
    fun setAllFiltersToOff(){
        coroutineScope.launch(Dispatchers.IO) {
            homeScreenDao.setAllFiltersToOff()
        }
    }

    fun setAllToShown(){
        coroutineScope.launch(Dispatchers.IO) {
            delay(150)
            homeScreenDao.setAllToShown()
        }
    }

    fun setIsShown(recipeName: String, isShown: Int){
        coroutineScope.launch(Dispatchers.IO) {
            homeScreenDao.setIsShown(recipeName, isShown)
        }
    }

    ///

    fun setAsUnfiltered(){
        coroutineScope.launch(Dispatchers.IO) {
            // UPDATE filter_table SET is_active_filter = 1 WHERE filter_name = Unfiltered
            homeScreenDao.setAsUnfiltered()
        }
    }
    fun setAsFiltered(){
        coroutineScope.launch(Dispatchers.IO) {
            //UPDATE filter_table SET is_active_filter = 0 WHERE filter_name = Unfiltered
            homeScreenDao.setAsFiltered()
        }
    }

    fun clearFilters(){
        coroutineScope.launch(Dispatchers.IO){
            // UPDATE filter_table SET is_active_filter = 0 WHERE filter_name != Unfiltered
            homeScreenDao.clearFilters()
        }
    }
    fun addFilter(filterName: String){
        coroutineScope.launch(Dispatchers.IO) {
            //UPDATE filter_table SET is_active_filter = 1 WHERE filter_name = 'filterName'
            homeScreenDao.addFilter(filterName)
        }
    }

    fun hideOtherFilters(filterName: String){
        coroutineScope.launch(Dispatchers.IO) {
            homeScreenDao.hideOtherFilters(filterName)
        }
    }

    fun removeFilter(filterName: String){
        coroutineScope.launch(Dispatchers.IO) {
            //UPDATE filter_table SET is_active_filter = 0 WHERE filter_name = 'filterName'
            homeScreenDao.removeFilter(filterName)
        }
    }






    ///

    fun updateFavorite(recipeName: String, isFavoriteStatus: Int) {
        coroutineScope.launch(Dispatchers.IO) {
            homeScreenDao.updateFavorite(recipeName, isFavoriteStatus)
        }
    }

    fun updateMenu(recipeName: String, onMenuStatus: Int) {
        coroutineScope.launch(Dispatchers.IO) {
            homeScreenDao.updateMenu(recipeName, onMenuStatus)
        }
    }

    fun updateQuantityNeeded(ingredientName: String, quantityNeeded: Int){
        coroutineScope.launch(Dispatchers.IO) {
            homeScreenDao.updateQuantityNeeded(ingredientName, quantityNeeded)
        }
    }

    fun setIngredientQuantityOwned(ingredientEntity: IngredientEntity, quantityOwned: Int){
        coroutineScope.launch(Dispatchers.IO) {
            homeScreenDao.setIngredientQuantityOwned(ingredientEntity.ingredientName, quantityOwned)
        }
    }

    fun setDetailsScreenTarget(recipeName: String){
        coroutineScope.launch(Dispatchers.IO) {
            homeScreenDao.setDetailsScreenTarget(recipeName)
        }
    }
}