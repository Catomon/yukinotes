package com.github.catomon.yukinotes.feature

import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

actual val sizes: Sizes = object : Sizes {
    override val font: TextUnit = 12.sp
    override val fontHeadline: TextUnit = 16.sp
    override val fontSmall: TextUnit = 10.sp
    override val notesListPadding: Dp = 1.dp
    override val noteItemPadding: Dp = 1.dp
    override val dividerThickness: Dp = 2.dp
    override val topBarSize: Dp = 32.dp
    override val bottomBarSize: Dp = 32.dp
    override val noteDatePadding: Dp = 6.dp
}