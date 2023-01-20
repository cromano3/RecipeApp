package com.example.bearrecipebookapp.datamodel

import androidx.room.Embedded
import androidx.room.Junction
import androidx.room.Relation
import com.example.bearrecipebookapp.data.entity.*

data class HomeScreenDataModel(

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
        entityColumn = "filter_name",
        associateBy = Junction(RecipeFiltersJoinEntity::class)
    )

    val filtersList: List<FilterEntity> = listOf<FilterEntity>()

)
