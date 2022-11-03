package com.example.bearrecipebookapp.viewmodel

import android.app.Application
import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import com.example.bearrecipebookapp.data.IngredientEntity
import com.example.bearrecipebookapp.data.RecipeAppDatabase
import com.example.bearrecipebookapp.data.RecipeRepository
import com.example.bearrecipebookapp.datamodel.RecipeWithIngredients
import com.example.bearrecipebookapp.datamodel.RecipeWithIngredientsAndInstructions
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.update


class RecipeBookScreenViewModel(application: Application): ViewModel() {

    //get all recipe names
    //add them to a New Recipe Model list
    //for each newRecipeModel search by newRecipeModel.name
    //search for a list of ingredients (Strings) that have this name
    //now we have a list of ingredients as string.
    //add the list to the newRecipeModel as newRecipeModel.ingredients<String>

    private val repository: RecipeRepository

    //var allRecipes: LiveData<List<RecipeEntity>>
   // var selectedRecipes: LiveData<List<RecipeEntity>>
    //var selectedRecipesWithIngredients: LiveData<List<RecipeWithIngredients>>

    var selectedIngredients: LiveData<List<IngredientEntity>>

    //var recipesWithIngredients:  LiveData<List<RecipeWithIngredients>>
    //var recipesWithInstructions: LiveData<List<RecipeWithInstructions>>
    var recipesWithIngredientsAndInstructions: LiveData<List<RecipeWithIngredientsAndInstructions>>
    var selectedRecipesWithIngredientsAndInstructions: LiveData<List<RecipeWithIngredientsAndInstructions>>



    //var detailsScreenTarget: LiveData<RecipeWithIngredients>

    val detailsScreenUiState = MutableStateFlow(RecipeWithIngredientsAndInstructions())

    //var detailsScreenTargetGood: LiveData<RecipeWithIngredientsAndInstructions>





    init {
        val appDb = RecipeAppDatabase.getInstance(application)
        val recipeDao = appDb.RecipeDao()
        repository = RecipeRepository(recipeDao)

        //allRecipes = repository.allRecipes
       // selectedRecipes = repository.selectedRecipes
        //selectedRecipesWithIngredients = repository.selectedRecipesWithIngredients

        selectedIngredients = repository.selectedIngredients

        //recipesWithIngredients = repository.recipesWithIngredients
        //recipesWithInstructions = repository.recipesWithInstructions
        recipesWithIngredientsAndInstructions = repository.recipesWithIngredientsAndInstructions
        selectedRecipesWithIngredientsAndInstructions = repository.selectedRecipesWithIngredientsAndInstructions


        //detailsScreenTarget = repository.detailsScreenTarget


        //detailsScreenTargetGood = repository.detailsScreenTargetGood

    }


    fun newSetDetailsScreenTarget(recipeWithIngredientsAndInstructions: RecipeWithIngredientsAndInstructions){

       // getInstructions(recipeWithIngredients.recipeEntity.recipeName)

        detailsScreenUiState.update{currentState->
                currentState.copy(recipeEntity = recipeWithIngredientsAndInstructions.recipeEntity,
                    ingredientsList = recipeWithIngredientsAndInstructions.ingredientsList,
                    instructionsList = recipeWithIngredientsAndInstructions.instructionsList
                    //instructionsList = repository.detailsWithInstructions.instructionsList,
                    //instructionsList = detailsScreenUiState.value.instructionsList

                )
        }
    }

    fun setDetailsScreenTargetHeart(recipe: RecipeWithIngredientsAndInstructions){
        var newRecipe = recipe

        if(recipe.recipeEntity.onMenu == 1)
            newRecipe.recipeEntity.onMenu = 0
        else if (recipe.recipeEntity.onMenu == 0)
            newRecipe.recipeEntity.onMenu = 1

        detailsScreenUiState.update{currentState->
            currentState.copy(recipeEntity = newRecipe.recipeEntity,
                ingredientsList = recipe.ingredientsList,
                instructionsList = recipe.instructionsList
                //instructionsList = repository.detailsWithInstructions.instructionsList,
                //instructionsList = detailsScreenUiState.value.instructionsList

            )
        }

    }

//    fun updateOnMenuHeartUi(){
//        detailsScreenUiState.update { currentState ->
//            currentState.copy(recipeEntity = detailsScreenUiState.value.recipeEntity)
//        }
//    }

//    fun getInstructions(recipeName: String){
//         repository.getInstructions(recipeName)
//    }

//    fun setDetailsScreenTarget(recipeWithIngredients: RecipeWithIngredients){
//        repository.setDetailsScreenTarget(recipeWithIngredients.recipeEntity.recipeName)
//    }
//
//    fun removeAsDetailsScreenTarget(recipeWithIngredients: RecipeWithIngredients){
//        repository.removeAsDetailsScreenTarget(recipeWithIngredients.recipeEntity.recipeName)
//    }



    //////


//////
    fun updateMenuWithInstructions(recipe: RecipeWithIngredientsAndInstructions) {
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
//////
    fun updateMenu(recipe: RecipeWithIngredients) {
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

//    fun addToMenu(recipe: RecipeWithIngredients) {
//        if(recipe.recipeEntity.onMenu == 0){
//            for(x in 0 until recipe.ingredientsList.size){
//                repository.updateQuantityNeeded(recipe.ingredientsList[x].ingredientName, recipe.ingredientsList[x].quantityNeeded + 1)
//            }
//        }
//        repository.updateMenu(recipe.recipeEntity.recipeName, 1)
//    }

    fun removeFromMenu(recipe: RecipeWithIngredients) {
        if(recipe.recipeEntity.onMenu == 1){
            for(x in 0 until recipe.ingredientsList.size){
                repository.updateQuantityNeeded(recipe.ingredientsList[x].ingredientName, recipe.ingredientsList[x].quantityNeeded - 1)
               // if(recipe.ingredientsList[x].quantityOwned > 0)
                   // && recipe.ingredientsList[x].quantityOwned == recipe.ingredientsList[x].quantityNeeded)
              //  {
                repository.setIngredientQuantityOwned(recipe.ingredientsList[x], 0)
                        //recipe.ingredientsList[x].quantityOwned - 1)
               // }
            }
        }
        repository.updateMenu(recipe.recipeEntity.recipeName, 0)
    }
    fun newRemoveFromMenu(recipe: RecipeWithIngredientsAndInstructions) {
        if(recipe.recipeEntity.onMenu == 1){
            for(x in 0 until recipe.ingredientsList.size){
                repository.updateQuantityNeeded(recipe.ingredientsList[x].ingredientName, recipe.ingredientsList[x].quantityNeeded - 1)
                // if(recipe.ingredientsList[x].quantityOwned > 0)
                // && recipe.ingredientsList[x].quantityOwned == recipe.ingredientsList[x].quantityNeeded)
                //  {
                repository.setIngredientQuantityOwned(recipe.ingredientsList[x], 0)
                //recipe.ingredientsList[x].quantityOwned - 1)
                // }

            }
        }
        repository.updateMenu(recipe.recipeEntity.recipeName, 0)
    }




    fun ingredientSelected(ingredientEntity: IngredientEntity){
        repository.setIngredientToOwned(ingredientEntity)
    }

    fun ingredientDeselected(ingredientEntity: IngredientEntity){
        repository.setIngredientToNotOwned(ingredientEntity)
    }





//    fun getRecipesWithIngredients(){
//        repository.getRecipesWithIngredients()
//        recipesWithIngredients = repository.recipesWithIngredients
//    }

//    fun getIngredients(recipeName: String): List<IngredientEntity>{
//        return repository.getIngredientsUsingRecipeName(recipeName)
//    }



///////////////////Look out below.../////////////////////////////////////////////////////



    //    val uiState = MutableStateFlow(
//        RecipeBookScreenUiState(
//            //recipeList = newRecipeModelList
//        )
//    )

    //** loads DB when this is constructed, when App Main is called! **//
//    val uiState = MutableStateFlow(
//        RecipeBookScreenUiState(
//            recipeList = DataSource().loadRecipeList())
//    )


        /*
        We could check this here but it is unncessary as the database could be told to change the value
        and it would be fine.  Would be like saying "change a 1 to a 1".  But checking when the button
        is clicked in the UI would allow us to tell the user if the item is already on the list or not.
         */
//        if(!uiState.value.selectedRecipesList.contains(recipe)){
//         //UPDATE THE DATABASE!
            //
            //

//            uiState.update {currentState ->
//                currentState.copy(
//                    numberOfRecipesOnMenu = uiState.value.numberOfRecipesOnMenu.plus(1),
//                    selectedRecipesList = uiState.value.selectedRecipesList.plus(recipe)
//                )
//            }
//

//
//    fun addSelected(recipe: NewRecipeModel){
//
//        if(!uiState.value.selectedRecipesList.contains(recipe)){
//
//            //UPDATE THE DATABASE!
//            //
//            //
//            uiState.update {currentState ->
//                currentState.copy(
//                    numberOfRecipesOnMenu = uiState.value.numberOfRecipesOnMenu.plus(1),
//                    selectedRecipesList = uiState.value.selectedRecipesList.plus(recipe)
//                )
//            }
//        }
//    }


//    fun addOrRemoveSelected(recipeName: String){
//        var myRecipe =  NewRecipeModel(
//            recipeName = "Error",
//            ingredientsList = listOf("Error", "Error"),
//            instructions = listOf("Error", "Error"),
//            imageRes = listOf(R.drawable.garlic, R.drawable.garlic),
//            isOnMenu = false
//        )
//
//        //find the recipe based on its recipeName string Int tag from the list of all recipes
//        //for now we use starting list because the user cannot add new recipes to the recipe
//        //book yet, maybe later in the FUTURE
//        for(x in 0 until uiState.value.recipeList.size){
//            if(recipeName == uiState.value.recipeList[x].recipeName){
//                myRecipe = uiState.value.recipeList[x]
//            }
//        }
//        //if the recipe is already on the menu then remove it
//        if(uiState.value.selectedRecipesList.contains(myRecipe)){
//            uiState.update {currentState ->
//                currentState.copy(
//                    numberOfRecipesOnMenu = uiState.value.numberOfRecipesOnMenu.minus(1),
//                    selectedRecipesList = uiState.value.selectedRecipesList.minus(myRecipe)
//                )
//            }
//        }
//        //else the recipe is "new" (not already on the menu and not previously selected)
//        //then add it to the list of selected recipes
//        else{
//            uiState.update {currentState ->
//                currentState.copy(
//                    numberOfRecipesOnMenu = uiState.value.numberOfRecipesOnMenu.plus(1),
//                    selectedRecipesList = uiState.value.selectedRecipesList.plus(myRecipe)
//                )
//            }
//        }
//    }
}

