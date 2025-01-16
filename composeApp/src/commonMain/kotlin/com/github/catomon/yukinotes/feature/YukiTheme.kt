package com.github.catomon.yukinotes.feature

import androidx.compose.material.MaterialTheme
import androidx.compose.material.Typography
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontFamily
import com.github.catomon.yukinotes.loadSettings
import org.jetbrains.compose.resources.Font
import yukinotes.composeapp.generated.resources.BadComic_Regular
import yukinotes.composeapp.generated.resources.Res

object Themes {
    const val DARK = "dark"
    const val BRIGHT = "bright"
}

@Composable
fun YukiTheme(content: @Composable () -> Unit) {
    MaterialTheme(
        colors = Colors.yukiColors,
        typography = Typography(FontFamily(Font(Res.font.BadComic_Regular))),
        content = content
    )
}

object Colors {
    var currentYukiTheme = Themes.DARK

//    val yukiHairOld = Color(255, 117, 236)
//    val yukiRedOld = Color(200, 33, 20)

    var yukiHair = Color(255, 148, 255)
    var pink = Color(249, 154, 181)
    var primary = yukiHair
    var yukiEyes = Color(134, 105, 189)
    var yukiEyesDark = Color(50, 23, 131)
    var yukiEyesDarker = Color(111, 79, 171)
    var yukiRed = Color(245, 83, 95)
    var lightGrey = Color(230, 230, 230)

//    ral noteBackground = Color(163, 126, 234)
    var noteBackground = Color(146, 110, 216)
    var noteTextHeadline = Color.White
    var noteText = Color(222, 204, 255)
    var noteTextSmall = Color(222, 204, 255)

    var background = yukiEyesDarker

    init {
        currentYukiTheme = loadSettings().theme
        updateTheme()
    }

    fun updateTheme() {
        if (currentYukiTheme == Themes.DARK) {
            setDark()
        } else {
            setBright()
        }
    }

    private fun setDark() {
        noteBackground = Color(146, 110, 216)
        noteTextHeadline = Color.White
        noteText = Color(222, 204, 255)
        noteTextSmall = Color(222, 204, 255)
        primary = yukiHair
        background = yukiEyesDarker
    }

    private fun setBright() {
        noteBackground = Color.White
        noteText =  Color.DarkGray
        noteTextSmall = Color.Gray
        noteTextHeadline = Color.Black
        primary = pink
        background = yukiEyes
    }

    val yukiColors = androidx.compose.material.Colors(
        primary = primary,
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