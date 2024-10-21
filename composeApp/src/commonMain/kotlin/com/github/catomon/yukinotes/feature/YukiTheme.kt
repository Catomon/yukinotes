package com.github.catomon.yukinotes.feature

import androidx.compose.ui.graphics.Color

object YukiTheme {
    fun createYukiColors(
        primary: Color = Colors.yukiHair,
        primaryVariant: Color = Color(0xFF3700B3),
        secondary: Color = Color(0xFF03DAC6),
        secondaryVariant: Color = Color(0xFF018786),
        background: Color = Color.White,
        surface: Color = Color.White,
        error: Color = Colors.yukiRed,
        onPrimary: Color = Color.White,
        onSecondary: Color = Colors.yukiEyesDark,
        onBackground: Color = Colors.yukiEyesDark,
        onSurface: Color = Colors.yukiEyesDark,
        onError: Color = Color.White
    ): androidx.compose.material.Colors = androidx.compose.material.Colors(
        primary,
        primaryVariant,
        secondary,
        secondaryVariant,
        background,
        surface,
        error,
        onPrimary,
        onSecondary,
        onBackground,
        onSurface,
        onError,
        true
    )
}