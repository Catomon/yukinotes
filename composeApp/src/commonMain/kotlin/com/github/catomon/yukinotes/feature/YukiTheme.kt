package com.github.catomon.yukinotes.feature

import androidx.compose.material.MaterialTheme
import androidx.compose.material.Typography
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontFamily
import org.jetbrains.compose.resources.Font
import yukinotes.composeapp.generated.resources.BadComic_Regular
import yukinotes.composeapp.generated.resources.Res

@Composable
fun YukiTheme(content: @Composable () -> Unit) {
    MaterialTheme(
        colors = Colors.yukiColors,
        typography = Typography(FontFamily(Font(Res.font.BadComic_Regular))),
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