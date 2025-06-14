package com.github.catomon.yukinotes.ui.util

import androidx.compose.ui.graphics.Color

fun Color.darken(factor: Float): Color {
    val color = this
    return Color(
        red = color.red * factor,
        green = color.green * factor,
        blue = color.blue * factor,
        alpha = color.alpha
    )
}