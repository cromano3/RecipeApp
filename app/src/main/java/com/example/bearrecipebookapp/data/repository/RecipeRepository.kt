package com.example.bearrecipebookapp.data.repository

import androidx.lifecycle.LiveData
import com.example.bearrecipebookapp.data.dao.RecipeDao
import com.example.bearrecipebookapp.data.entity.IngredientEntity
import com.example.bearrecipebookapp.datamodel.RecipeWithIngredientsAndInstructions
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

// Declares the DAO as a private property in the constructor. Pass in the DAO
// instead of the whole database, because you only need access to the DAO

class RecipeRepository (private val recipeDao: RecipeDao) {

    private val coroutineScope = CoroutineScope(Dispatchers.Main)

    //for recipe book screen view model
    //var allRecipes: LiveData<List<RecipeEntity>> = recipeDao.getAllRecipes()
    //for menu screen view model
    //var selectedRecipes:  LiveData<List<RecipeEntity>> = recipeDao.getSelectedRecipes()

    //var selectedRecipesWithIngredients: LiveData<List<RecipeWithIngredients>> = recipeDao.getSelectedRecipesWithIngredients()

    var selectedIngredients:  LiveData<List<IngredientEntity>> = recipeDao.getNeededIngredients()

    //all recipes with their ingredients
    //THIS IS MUTABLE BECAUSE LIVEDATA IS ABSTRACT!!!!
    //we don't actually change the data after receiving it but we cant use normal Live Data here
    //because when we try to create an instance of live data that is "empty"
    //we get a compile error because LiveData is abstract.
//    var recipesWithIngredients =  MutableLiveData<List<RecipeWithIngredients>>()

    //var recipesWithIngredients:  LiveData<List<RecipeWithIngredients>> = recipeDao.getRecipesWithIngredients()

   //var recipesWithInstructions:  LiveData<List<RecipeWithInstructions>> = recipeDao.getRecipesWithInstructions()

    var recipesWithIngredientsAndInstructions: LiveData<List<RecipeWithIngredientsAndInstructions>> = recipeDao.getRecipesWithIngredientsAndInstructions()
    var selectedRecipesWithIngredientsAndInstructions: LiveData<List<RecipeWithIngredientsAndInstructions>> = recipeDao.getSelectedRecipesWithIngredientsAndInstructions()



    //var detailsScreenTargetInstructions: LiveData<RecipeWithIngredients> = recipeDao.getDetailsScreenTarget()

    //var detailsScreenTargetGood: LiveData<RecipeWithIngredientsAndInstructions> = recipeDao.getDetailsScreenTargetWithIngredientsAndInstructions()



    //see dao for more details about why this didn't work and potential future solutions.
    //****************************//
   // var detailsWithInstructions: RecipeWithInstructions = recipeDao.getInstructions("Bagels")
//
//    fun getInstructions(recipeName:String){
//        coroutineScope.launch(Dispatchers.IO){
//            detailsWithInstructions = recipeDao.getInstructions(recipeName)
//        }
//    }





//    fun setDetailsScreenTarget(recipeName: String) {
//        coroutineScope.launch(Dispatchers.IO) {
//            recipeDao.setDetailsScreenTarget(recipeName)
//        }
//    }
//
//    fun removeAsDetailsScreenTarget(recipeName: String){
//        coroutineScope.launch(Dispatchers.IO) {
//            recipeDao.removeAsDetailsScreenTarget(recipeName)
//        }
//    }



    fun updateMenu(recipeName: String, onMenuStatus: Int) {
        coroutineScope.launch(Dispatchers.IO) {
            recipeDao.updateMenu(recipeName, onMenuStatus)
        }
    }

    fun updateQuantityNeeded(ingredientName: String, quantityNeeded: Int){
        coroutineScope.launch(Dispatchers.IO) {
            recipeDao.updateQuantityNeeded(ingredientName, quantityNeeded)
        }
    }

    fun setIngredientToOwned(ingredientEntity: IngredientEntity){
        coroutineScope.launch(Dispatchers.IO) {
            recipeDao.setIngredientToOwned(ingredientEntity.ingredientName)
        }
    }

    fun setIngredientToNotOwned(ingredientEntity: IngredientEntity){
        coroutineScope.launch(Dispatchers.IO) {
            recipeDao.setIngredientToNotOwned(ingredientEntity.ingredientName)
        }
    }

    fun setIngredientQuantityOwned(ingredientEntity: IngredientEntity, quantityOwned: Int){
        coroutineScope.launch(Dispatchers.IO) {
            recipeDao.setIngredientQuantityOwned(ingredientEntity.ingredientName, quantityOwned)
        }
    }

//    fun getRecipesWithIngredients(){
//        coroutineScope.launch(Dispatchers.Main) {
//            recipesWithIngredients.value = asyncGetAllRecipesWithIngredients().await()
//        }
//    }
//    private fun asyncGetAllRecipesWithIngredients(): Deferred<List<RecipeWithIngredients>> =
//        coroutineScope.async(Dispatchers.IO) {
//            return@async recipeDao.getRecipesWithIngredients()
//        }



//    fun getIngredientsUsingRecipeName(recipeName: String): List<IngredientEntity> {
//           return recipeDao.getIngredientsUsingRecipeName(recipeName)
//    }






    /////////////////////Look out below...////////////////////////////////////





//  return as map (Key, Value) key = recipe name , value = RecipeWithIngredients CUSTOM CLASS



 //   val allProducts: LiveData<List<Product>> = productDao.getAllProducts()

//    suspend fun loadNames(){
//        names = recipeDao.getAllRecipeNames()
//    }
//
//    fun getAllRecipeNames(): List<String>{
//        return names
//    }

//
//
////    @Query("SELECT ingredient FROM recipe_table WHERE recipe_name = :name ORDER BY ingredient ASC")
////    fun getThisRecipesIngredients(name: String): List<String>
//    fun getThisRecipesIngredients(name: String): List<String>{
//        return recipeDao.getThisRecipesIngredients(name)
//    }
//
//
////    @Query("SELECT instruction FROM recipe_table WHERE recipe_name = :name ORDER BY instruction ASC")
////    fun getThisRecipesInstructions(name: String): List<String>
//    fun getThisRecipesInstructions(name: String): List<String>{
//        return recipeDao.getThisRecipesInstructions(name)
//    }
//
////    @Query("SELECT on_menu FROM recipe_table WHERE recipe_name = :name")
////    fun getThisRecipesOnMenuState(name: String): Int
//    fun getThisRecipesOnMenuState(name: String): Int{
//        return recipeDao.getThisRecipesOnMenuState(name)
//    }
//
////    @Query("SELECT image FROM recipe_table WHERE recipe_name = :name ORDER BY image ASC")
////    fun getThisRecipesImagesRefs(name: String): List<Int>
//    fun getThisRecipesOnImagesRefs(name: String): List<Int>{
//        return recipeDao.getThisRecipesImagesRefs(name)
//    }
//
//    //

//
//    var allRecipes = recipeDao.getAllRecipes()
//
//    fun getAllRecipes(){
//        recipeDao.getAllRecipes()
//    }
//
////    @Query("SELECT * FROM recipe_table WHERE recipe_name = :name ORDER BY recipe_name ASC, ingredient ASC")
////    fun getRecipeByName(name: String): List<RecipeEntity>
////
//    fun getRecipeByName(name: String){
//        recipeDao.getRecipeByName(name)
//    }
//
////    @Query("SELECT * FROM recipe_table WHERE on_menu = 1 ORDER BY recipe_name ASC, ingredient ASC")
////    fun getMenuRecipes(name: String): List<RecipeEntity>
//
//    fun getMenuRecipes(){
//        recipeDao.getMenuRecipes()
//    }
////
////    @Query("UPDATE recipe_table SET on_menu = :onMenu WHERE recipe_name = :name")
////    fun updateOnMenu(name: String, onMenu: Int)
//
//    fun updateOnMenu(name: String, onMenu: Int){
//        recipeDao.updateOnMenu(name, onMenu)
//    }



//    companion object {
//
//        // For Singleton instantiation
//        @Volatile private var instance: RecipeRepository? = null
//
//        fun getInstance(recipeDao: RecipeDao) =
//            instance ?: synchronized(this) {
//                instance ?: RecipeRepository(recipeDao).also { instance = it }
//            }
//    }

    // Room executes all queries on a separate thread.
    // Observed Flow will notify the observer when the data has changed.
//    val allWords: Flow<List<Word>> = recipeDao.getAlphabetizedWords()

    // By default Room runs suspend queries off the main thread, therefore, we don't need to
    // implement anything else to ensure we're not doing long running database work
    // off the main thread.
//    @Suppress("RedundantSuspendModifier")
//    @WorkerThread
//    suspend fun insert(word: Word) {
//        recipeDao.insert(word)
//    }
}