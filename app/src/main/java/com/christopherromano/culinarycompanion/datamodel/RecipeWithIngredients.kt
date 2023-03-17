package com.christopherromano.culinarycompanion.datamodel

import androidx.room.Embedded
import androidx.room.Junction
import androidx.room.Relation
import com.christopherromano.culinarycompanion.data.entity.IngredientEntity
import com.christopherromano.culinarycompanion.data.entity.RecipeEntity
import com.christopherromano.culinarycompanion.data.entity.RecipeIngredientJoinEntity



data class RecipeWithIngredients(

    @Embedded val recipeEntity: RecipeEntity,

    @Relation(
        parentColumn = "recipe_name",
        entityColumn = "ingredient_name",
        associateBy = Junction(RecipeIngredientJoinEntity::class)
    )

    val ingredientsList: List<IngredientEntity>

)



///// var recipe: String, var instructions: String, var isOnMenu: Boolean,
///// var ingredientsList: List<IngredientEntity>,

///// OR

///// var recipe: <RecipeEntity>, var ingredientsList: List<IngredientsEntity>

///// we must get room to return either of these two custom data structures in one Query!
    // TO DO!!!!!!!!!!!!!!!!!!!!
