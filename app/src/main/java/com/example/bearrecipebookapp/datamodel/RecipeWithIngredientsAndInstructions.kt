package com.example.bearrecipebookapp.datamodel

import androidx.room.Embedded
import androidx.room.Junction
import androidx.room.Relation
import com.example.bearrecipebookapp.data.IngredientEntity
import com.example.bearrecipebookapp.data.InstructionEntity
import com.example.bearrecipebookapp.data.RecipeEntity
import com.example.bearrecipebookapp.data.RecipeIngredientJoinEntity

data class RecipeWithIngredientsAndInstructions (
    @Embedded
    val recipeEntity: RecipeEntity = RecipeEntity(),

    @Relation(
        parentColumn = "recipe_name",
        entityColumn = "ingredient_name",
        associateBy = Junction(RecipeIngredientJoinEntity::class)
    )

    val ingredientsList: List<IngredientEntity> = listOf<IngredientEntity>(),

    @Relation(
        parentColumn = "recipe_name",
        entityColumn = "recipe_id",
    )

    val instructionsList: List<InstructionEntity> = listOf<InstructionEntity>()




)