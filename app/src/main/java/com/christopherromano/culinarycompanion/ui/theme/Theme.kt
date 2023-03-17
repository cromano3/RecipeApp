package com.christopherromano.culinarycompanion.ui.theme

import android.annotation.SuppressLint
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material.MaterialTheme
import androidx.compose.material.darkColors
import androidx.compose.material.lightColors
import androidx.compose.runtime.Composable

@SuppressLint("ConflictingOnColor")
private val DarkColorPalette = darkColors(

    background = dark_background,
    surface = dark_surface,
    secondary = dark_secondary,
    onSurface = dark_onSurface,
    primary = dark_primary,
    onPrimary = dark_onPrimary,
    primaryVariant = dark_primaryVariant,

    secondaryVariant = dark_secondaryVariant
)

@SuppressLint("ConflictingOnColor")
private val LightColorPalette = lightColors(
    background = dark_background,
    surface = dark_surface,
    secondary = dark_secondary,
    onSurface = dark_onSurface,
    primary = dark_primary,
    onPrimary = dark_onPrimary,
    primaryVariant = dark_primaryVariant,

    secondaryVariant = dark_secondaryVariant

    /* Other default colors to override


    onSecondary = Color.Black,
    onBackground = Color.Black,

    */
)

@Composable
fun CulinaryCompanionTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    content: @Composable () -> Unit
) {
    val colors = if (darkTheme) {
        DarkColorPalette
    } else {
        LightColorPalette
    }

    MaterialTheme(
        colors = colors,
        typography = Typography,
        shapes = Shapes,
        content = content
    )
}