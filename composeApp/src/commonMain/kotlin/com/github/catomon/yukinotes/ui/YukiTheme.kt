package com.github.catomon.yukinotes.ui

import androidx.compose.material.MaterialTheme
import androidx.compose.material.Typography
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.unit.sp
import com.github.catomon.yukinotes.loadSettings
import org.jetbrains.compose.resources.Font
import yukinotes.composeapp.generated.resources.BadComic_Regular
import yukinotes.composeapp.generated.resources.Res

object Themes {
    val list = listOf(Pink(), Violet(), Blue())

    fun forName(name: String) = list.find { it.name == name }

    fun forNameOrFirst(name: String) = list.find { it.name == name } ?: list.first()

    class Pink : IYukiTheme {
        override val name: String = "yuki"
        override val background = Color(0xffee6b98)
        override val surface = Color(0xfffd8ead)
        override val surfaceSecondary = Color(0xfff87fa1)
        override val font = Color(0xFFFFFFFF)
        override val fontSecondary = Color(0xffffeef2)
        override val bars = Color(0xffee588c)
    }

    class Violet : IYukiTheme {
        override val name: String = "gami-kasa"
        override val background = Color(0xff6e4eaa)
        override val surface = Color(0xff916dd6)
        override val surfaceSecondary = Color(0xff8563cc)
        override val font = Color(0xFFFFFFFF)
        override val fontSecondary = Color(0xFFDECCFF)
        override val bars = Color(0xFF66419F)
    }

    class Blue : IYukiTheme {
        override val name: String = "nata"
        override val background = Color(0xff3a55af)
        override val surface = Color(0xff6197de)
        override val surfaceSecondary = Color(0xff3671c1)
        override val font = Color(0xFFFFFFFF)
        override val fontSecondary = Color(0xffd2e6ff)
        override val bars = Color(0xff0f2e93)
    }

    interface IYukiTheme {
        val name: String
        val background: Color
        val surface: Color
        val surfaceSecondary: Color
        val font: Color
        val fontSecondary: Color
        val bars: Color
    }
}

@Composable
fun YukiTheme(content: @Composable () -> Unit) {
    MaterialTheme(
        colors = Colors.yukiColors,
        typography = Typography(FontFamily(Font(Res.font.BadComic_Regular)), body1 = TextStyle(fontSize = 12.sp)),
        content = content
    )
}

object Colors {
    var currentYukiTheme = Themes.list.first()

    private val violetDark = Color(50, 23, 131)
    private val violet = Color(111, 79, 171)
    private val lightRed = Color(245, 83, 95)

    var noteBackground = Color(146, 110, 216)
    var noteTextHeadline = Color.White
    var noteText = Color(222, 204, 255)
    var noteTextSmall = Color(222, 204, 255)
    var background = violet
    var bars = Color(255, 148, 255)
    var dividers = Color(255, 148, 255)

    init {
        currentYukiTheme =
            Themes.list.find { it.name == loadSettings().theme } ?: Themes.list.first()
        updateTheme()
    }

    fun updateTheme() {
        noteBackground = currentYukiTheme.surface
        noteTextHeadline = currentYukiTheme.font
        noteText = currentYukiTheme.fontSecondary
        noteTextSmall = currentYukiTheme.fontSecondary
        background = currentYukiTheme.background
        bars = currentYukiTheme.bars
        dividers = currentYukiTheme.surfaceSecondary
    }

    val yukiColors = androidx.compose.material.Colors(
        primary = bars,
        primaryVariant = Color(0xFF3700B3),
        secondary = Color(0xFF03DAC6),
        secondaryVariant = Color(0xFF018786),
        background = Color.White,
        surface = Color.White,
        error = lightRed,
        onPrimary = Color.White,
        onSecondary = violetDark,
        onBackground = violetDark,
        onSurface = violetDark,
        onError = Color.White,
        true
    )
}