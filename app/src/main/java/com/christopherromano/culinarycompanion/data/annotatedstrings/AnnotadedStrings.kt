package com.christopherromano.culinarycompanion.data.annotatedstrings

import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.withStyle


fun confirmSubmitReportAnoString(): AnnotatedString{
    return buildAnnotatedString {
        append("Report this comment for being inappropriate or off topic? ")
        withStyle(style = SpanStyle(fontWeight = FontWeight.Bold)) {
            append("Please note: submitting false reports may result in your account being banned")
        }
        append(".")
    }
}

fun confirmRemoveMenuAnoString(recipeName: String): AnnotatedString{
     return buildAnnotatedString {
        append("Are you sure you want to remove ")
        append(recipeName)
        append(" from the ")
        withStyle(style = SpanStyle(fontWeight = FontWeight.Bold)) {
            append("Menu")
        }
        append("? (This will also remove its ingredients from the ")
        withStyle(style = SpanStyle(fontWeight = FontWeight.Bold)) {
            append("Shopping List")
        }
        append(".")
    }
}

fun confirmCompletedCookingAnoString(recipeName: String): AnnotatedString{
    return buildAnnotatedString {
        append("Great job! Confirm that you have finished cooking ")
        append(recipeName)
        append(" and ")
        withStyle(style = SpanStyle(fontWeight = FontWeight.Bold)) {
            append("remove it")
        }
        append(" from your ")
        withStyle(style = SpanStyle(fontWeight = FontWeight.Bold)) {
            append("Menu")
        }
        append(" and its ingredients from your ")
        withStyle(style = SpanStyle(fontWeight = FontWeight.Bold)) {
            append("Shopping List")
        }
        append("?")
    }
}

fun confirmIMadeThisAnoString(recipeName: String): AnnotatedString{
    return buildAnnotatedString {
        append("Great job! Add ")
        append(recipeName)
        append(" to the ")
        withStyle(style = SpanStyle(fontWeight = FontWeight.Bold)) {
            append("Cooked Recipes")
        }
        append(" list?")
    }

}

fun addRecipeOrCustomItemAnoString(): AnnotatedString{
    return buildAnnotatedString {
        append("Add a ")
        withStyle(style = SpanStyle(fontWeight = FontWeight.Bold)){
            append("Recipe")
        }
        append(" or a ")
        withStyle(style = SpanStyle(fontWeight = FontWeight.Bold)){
            append("Custom Item")
        }
        append(" to the shopping list?")
    }

}

fun tutorialTextAnoString(): AnnotatedString{
    return buildAnnotatedString {
        append("to ")
        withStyle(style = SpanStyle(fontWeight = FontWeight.Bold)){
            append("add a Recipe")
        }
        append(" to your ")
        withStyle(style = SpanStyle(fontWeight = FontWeight.Bold)){
            append("Menu")
        }
        append(" and its ")
        withStyle(style = SpanStyle(fontWeight = FontWeight.Bold)){
            append("ingredients")
        }
        append(" to your ")
        withStyle(style = SpanStyle(fontWeight = FontWeight.Bold)){
            append("Shopping List.")
        }
    }
}