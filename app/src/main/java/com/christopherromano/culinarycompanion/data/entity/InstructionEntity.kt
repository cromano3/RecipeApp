package com.christopherromano.culinarycompanion.data.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "instructions_table")
class InstructionEntity(
    @PrimaryKey(autoGenerate = false)
    @ColumnInfo(name = "instruction_id")
    val instructionID: Int = 0,

    @ColumnInfo(name = "recipe_id")
    val recipeID: String = "",

    @ColumnInfo(name = "instruction")
    val instruction: String = "",


    )