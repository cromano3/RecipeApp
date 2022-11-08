package com.example.bearrecipebookapp.datamodel

import androidx.room.Embedded
import androidx.room.Junction
import androidx.room.Relation
import com.example.bearrecipebookapp.data.FilterEntity
import com.example.bearrecipebookapp.data.RecipeEntity
import com.example.bearrecipebookapp.data.RecipeFiltersJoinEntity

data class RecipeWithFilters(

    @Embedded
    val recipeEntity: RecipeEntity = RecipeEntity(),

    @Relation(
        parentColumn = "recipe_name",
        entityColumn = "filter_name",
        associateBy = Junction(RecipeFiltersJoinEntity::class)
    )

    val filtersList: List<FilterEntity> = listOf<FilterEntity>()

)

