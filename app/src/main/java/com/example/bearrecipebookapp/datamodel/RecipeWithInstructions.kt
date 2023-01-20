package com.example.bearrecipebookapp.datamodel

import androidx.room.Embedded
import androidx.room.Relation
import com.example.bearrecipebookapp.data.entity.InstructionEntity
import com.example.bearrecipebookapp.data.entity.RecipeEntity

data class RecipeWithInstructions(
    @Embedded
    val recipeEntity: RecipeEntity,

    @Relation(
        parentColumn = "recipe_name",
        entityColumn = "recipe_id",
    )

    val instructionsList: List<InstructionEntity>
)
