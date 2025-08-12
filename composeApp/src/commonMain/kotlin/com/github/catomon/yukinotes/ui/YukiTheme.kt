package com.github.catomon.yukinotes.ui

import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Typography
import androidx.compose.runtime.Composable
import androidx.compose.runtime.ReadOnlyComposable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontFamily
import com.github.catomon.yukinotes.loadSettings
import com.github.catomon.yukinotes.ui.util.darken
import org.jetbrains.compose.resources.Font
import yukinotes.composeapp.generated.resources.BadComic_Regular
import yukinotes.composeapp.generated.resources.Res

object Themes {
    val list = listOf(Pink(), Violet(), Blue(), YukiLight(), LuckyDark())

    fun forName(name: String) = list.find { it.name == name }

    fun forNameOrFirst(name: String) = list.find { it.name == name } ?: list.first()

    class Pink : IYukiTheme {
        override val name: String = "yuki"
        override val background = Color(0xffff98b3)
        override val surface =  Color(0xcdff98b3) //Color(0xffffc5d6).copy(0.4f)
        override val surfaceSecondary = Color(0xffc23a80)
        override val text = Color(0xFFFFFFFF)
        override val textSecondary = Color(0xFFFFE1EA)
        override val bars = Color(0xffe9678f).copy(0.9f)
        override val barsShadow = Color(0xff000000)
    }

    class YukiLight : IYukiTheme {
        override val name: String = "yuki_light"
        override val background = Color(0xfff1f1f1)
        override val surface =  Color(0xcdc2c2c2)
        override val surfaceSecondary = Color(0xffc23a80)
        override val text = Color(0xff000000)
        override val textSecondary = Color(0xff000000)
        override val bars = Color(0xfffafafa).copy(0.9f)
        override val barsShadow = Color(0xff000000)
    }

    class LuckyDark : IYukiTheme {
        override val name: String = "lucky_dark"
        override val background = Color(0xff000000)
        override val surface = Color(0xffc33c83)  //Color(0xffffc5d6).copy(0.4f)
        override val surfaceSecondary = Color(0xffc23a80)
        override val text = Color(0xFFFFFFFF)
        override val textSecondary = Color(0xFFFFE1EA)
        override val bars = Color(0xff000000)
        override val barsShadow = Color(0xff000000)
    }

    class Violet : IYukiTheme {
        override val name: String = "gami-kasa"
        override val background = Color(0xff6e4eaa)
        override val surface = Color(0xffc8abff).copy(0.4f)
        override val surfaceSecondary = Color(0xffc0a1ff).copy(0.8f)
        override val text = Color(0xFFFFFFFF)
        override val textSecondary = Color(0xFFDECCFF)
        override val bars = Color(0xff6232a9).copy(0.9f)
        override val barsShadow = Color(0xff48257e)
    }

    class Blue : IYukiTheme {
        override val name: String = "nata"
        override val background = Color(0xff3a55af)
        override val surface = Color(0xff6197de).copy(0.4f)
        override val surfaceSecondary = Color(0xff3671c1).copy(0.8f)
        override val text = Color(0xFFFFFFFF)
        override val textSecondary = Color(0xffd2e6ff)
        override val bars = Color(0xff0f2e93).copy(0.9f)
        override val barsShadow = Color(0xff0c2473)
    }

    interface IYukiTheme {
        val name: String
        val background: Color
        val surface: Color
        val surfaceSecondary: Color
        val text: Color
        val textSecondary: Color
        val bars: Color
        val barsShadow: Color
    }
}

@Composable
fun YukiTheme(content: @Composable () -> Unit) {
    val fontFamily = FontFamily(Font(Res.font.BadComic_Regular))
    val defaultTypography = MaterialTheme.typography
    val typography = remember(YukiTheme.colors) {
        Typography(
            displayLarge = defaultTypography.displayLarge.copy(fontFamily = fontFamily),
            displayMedium = defaultTypography.displayMedium.copy(fontFamily = fontFamily),
            displaySmall = defaultTypography.displaySmall.copy(fontFamily = fontFamily),

            headlineLarge = defaultTypography.headlineLarge.copy(fontFamily = fontFamily),
            headlineMedium = defaultTypography.headlineMedium.copy(fontFamily = fontFamily),
            headlineSmall = defaultTypography.headlineSmall.copy(fontFamily = fontFamily),

            titleLarge = defaultTypography.titleLarge.copy(fontFamily = fontFamily),
            titleMedium = defaultTypography.titleMedium.copy(fontFamily = fontFamily),
            titleSmall = defaultTypography.titleSmall.copy(fontFamily = fontFamily),

            bodyLarge = defaultTypography.bodyLarge.copy(fontFamily = fontFamily, fontSize = sizes.font, lineHeight = sizes.fontLineHeight, color = YukiTheme.colors.text),
            bodyMedium = defaultTypography.bodyMedium.copy(fontFamily = fontFamily, fontSize = sizes.font, lineHeight = sizes.fontLineHeight, color = YukiTheme.colors.text),
            bodySmall = defaultTypography.bodySmall.copy(fontFamily = fontFamily, fontSize = sizes.font, lineHeight = sizes.fontLineHeight, color = YukiTheme.colors.text),

            labelLarge = defaultTypography.labelLarge.copy(fontFamily = fontFamily),
            labelMedium = defaultTypography.labelMedium.copy(fontFamily = fontFamily),
            labelSmall = defaultTypography.labelSmall.copy(fontFamily = fontFamily)
        )
    }

    MaterialTheme(
        colorScheme = YukiTheme.YukiColorScheme(),
        typography = typography,
        content = content
    )
}

object YukiTheme {
    var colors by mutableStateOf(Themes.list.first())

    private val violetDark = Color(50, 23, 131)
    private val violet = Color(111, 79, 171)
    private val lightRed = Color(245, 83, 95)

    var noteBackground = Color(146, 110, 216)
    var noteTitle = Color.White
    var noteText = Color(222, 204, 255)
    var noteDetails = Color(222, 204, 255)
    var background = violet
    var bars = Color(255, 148, 255)
    var dividers = Color(255, 148, 255)
    var textOnBackground = background.copy(1f).darken(0.75f)

    init {
        colors =
            Themes.list.find { it.name == loadSettings().theme } ?: Themes.list.first()
        updateTheme()
    }

    fun updateTheme() {
        noteBackground = colors.surface
        noteTitle = colors.textSecondary
        noteText = colors.text
        noteDetails = colors.textSecondary
        background = colors.background
        bars = colors.bars
        dividers = colors.background
        textOnBackground = background.copy(1f).darken(0.6f)
    }

    @Composable
    @ReadOnlyComposable
    fun YukiColorScheme() = MaterialTheme.colorScheme.copy(
        primary = bars,
        primaryContainer = Color(0xFF3700B3),
        secondary = Color(0xFF03DAC6),
        secondaryContainer = Color(0xFF018786),
        background = colors.surface,
        surface = bars,
        error = lightRed,
        onPrimary = background,
        onSecondary = violetDark,
        onBackground = violetDark,
        onSurface = background,
        onError = Color.White,
    )
}