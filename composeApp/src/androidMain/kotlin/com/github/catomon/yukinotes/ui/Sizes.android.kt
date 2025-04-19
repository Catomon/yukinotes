package com.github.catomon.yukinotes.ui

import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

actual val sizes: Sizes = object : Sizes {
    override val topBarSize: Dp = 48.dp
    override val bottomBarSize: Dp = 48.dp
    override val font: TextUnit = 14.sp
    override val fontHeadline: TextUnit = 18.sp
    override val fontSmall: TextUnit = 12.sp
    override val notesListPadding: Dp = 3.dp
    override val noteItemPadding: Dp = 1.dp
    override val dividerThickness: Dp = 2.dp
    override val noteDatePadding: Dp = 1.dp
}