package com.christopherromano.culinarycompanion.datamodel


import androidx.room.Embedded
import androidx.room.Relation
import com.christopherromano.culinarycompanion.data.entity.InstructionEntity
import com.christopherromano.culinarycompanion.data.entity.RecipeEntity


data class RecipeWithInstructions(
    @Embedded
    val recipeEntity: RecipeEntity,

    @Relation(
        parentColumn = "recipe_name",
        entityColumn = "recipe_id",
    )

    val instructionsList: List<InstructionEntity>
)
