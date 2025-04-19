package com.github.catomon.yukinotes.ui

import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.TextUnit

expect val sizes: Sizes

interface Sizes {
    val font: TextUnit
    val fontHeadline: TextUnit
    val fontSmall: TextUnit

    val notesListPadding: Dp
    val noteItemPadding: Dp
    val noteDatePadding: Dp

    val dividerThickness: Dp

    val topBarSize: Dp
    val bottomBarSize: Dp

    val noteItemWidth: Dp
}