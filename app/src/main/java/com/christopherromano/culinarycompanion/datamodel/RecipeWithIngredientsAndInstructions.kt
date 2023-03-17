package com.christopherromano.culinarycompanion.datamodel


import androidx.room.Embedded
import androidx.room.Junction
import androidx.room.Relation
import com.christopherromano.culinarycompanion.data.entity.IngredientEntity
import com.christopherromano.culinarycompanion.data.entity.InstructionEntity
import com.christopherromano.culinarycompanion.data.entity.RecipeEntity
import com.christopherromano.culinarycompanion.data.entity.RecipeIngredientJoinEntity


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

