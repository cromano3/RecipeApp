package com.christopherromano.culinarycompanion.datamodel


import androidx.room.Embedded
import androidx.room.Junction
import androidx.room.Relation
import com.christopherromano.culinarycompanion.data.entity.FilterEntity
import com.christopherromano.culinarycompanion.data.entity.RecipeEntity
import com.christopherromano.culinarycompanion.data.entity.RecipeFiltersJoinEntity


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

