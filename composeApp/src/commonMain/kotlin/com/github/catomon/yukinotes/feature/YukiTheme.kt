package com.github.catomon.yukinotes.feature

import androidx.compose.material.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color

@Composable
fun YukiTheme(content: @Composable () -> Unit) {
    MaterialTheme(
        colors = Colors.yukiColors,
        content = content
    )
}

object Colors {
    val yukiHair = Color(255, 117, 236)
    val yukiEyes = Color(134, 105, 189)
    val yukiEyesDark = Color(50, 23, 131)
    val yukiRed = Color(200, 33, 20)
    val lightGrey = Color(230, 230, 230)

    val yukiColors = androidx.compose.material.Colors(
        primary = yukiHair,
        primaryVariant = Color(0xFF3700B3),
        secondary = Color(0xFF03DAC6),
        secondaryVariant = Color(0xFF018786),
        background = Color.White,
        surface = Color.White,
        error = yukiRed,
        onPrimary = Color.White,
        onSecondary = yukiEyesDark,
        onBackground = yukiEyesDark,
        onSurface = yukiEyesDark,
        onError = Color.White,
        true
    )
}