package com.christopherromano.culinarycompanion.data.repository

import androidx.lifecycle.LiveData
import com.christopherromano.culinarycompanion.data.dao.HomeScreenDao
import com.christopherromano.culinarycompanion.data.entity.FilterEntity
import com.christopherromano.culinarycompanion.data.entity.IngredientEntity
import com.christopherromano.culinarycompanion.datamodel.HomeScreenDataModel
import com.christopherromano.culinarycompanion.datamodel.RecipeWithIngredients
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class HomeScreenRepository(private val homeScreenDao: HomeScreenDao) {

    private val coroutineScope = CoroutineScope(Dispatchers.Main)

    var homeScreenData: LiveData<List<HomeScreenDataModel>> = homeScreenDao.getData()
    var filtersList: LiveData<List<FilterEntity>> = homeScreenDao.getFilters()
    var referenceList: LiveData<List<HomeScreenDataModel>> = homeScreenDao.getReferenceList()

    var showTutorial: LiveData<String> = homeScreenDao.getShowTutorial()

    var unfilteredList: LiveData<List<RecipeWithIngredients>> = homeScreenDao.getUnfilteredList()
    var filteredList1: LiveData<List<RecipeWithIngredients>> = homeScreenDao.getFilteredList1()
    var filteredList2: LiveData<List<RecipeWithIngredients>> = homeScreenDao.getFilteredList2()



    var shownRecipeList: LiveData<List<RecipeWithIngredients>> = homeScreenDao.getRecipeList()


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

    fun clearTutorialAlert(){
        coroutineScope.launch(Dispatchers.IO) {
            homeScreenDao.clearTutorialAlert()
        }
    }




    fun removeOtherFilters(name: String){
        homeScreenDao.removeOtherFilters(name)
    }

    fun filterBy(name: String){
        homeScreenDao.filterBy(name)
    }

    //

    fun setRecipeToNotShown(name: String){
        homeScreenDao.setRecipeToNotShown(name)
    }

    fun setRecipeToShown(name: String){
        homeScreenDao.setRecipeToShown(name)
    }

    fun updateIngredients(recipesNotToBeShown: List<String>, recipesToBeShown: List<String>){
        homeScreenDao.updateRecipes(recipesNotToBeShown, recipesToBeShown)
    }

    //

    fun cleanFilters(){
        homeScreenDao.cleanFilters()
    }

    fun cleanRecipes(){
        homeScreenDao.cleanRecipes()
    }



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


}