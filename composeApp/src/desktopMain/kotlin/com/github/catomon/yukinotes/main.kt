package com.github.catomon.yukinotes

import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.window.WindowDraggableArea
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.compositionLocalOf
import androidx.compose.ui.Modifier
import androidx.compose.ui.awt.ComposeWindow
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.geometry.CornerRadius
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Window
import androidx.compose.ui.window.WindowScope
import androidx.compose.ui.window.WindowState
import androidx.compose.ui.window.application
import com.github.catomon.yukinotes.di.appModule
import com.github.catomon.yukinotes.ui.YukiApp
import com.github.catomon.yukinotes.ui.YukiAppDesktopScreen
import com.github.catomon.yukinotes.ui.YukiTheme
import com.github.catomon.yukinotes.ui.customShadow
import org.jetbrains.compose.resources.painterResource
import org.jetbrains.compose.ui.tooling.preview.Preview
import org.koin.core.context.GlobalContext.startKoin
import yukinotes.composeapp.generated.resources.Res
import yukinotes.composeapp.generated.resources.app_ico32

var isTransparent = false
fun main() = application {
    startKoin {
        modules(appModule)
    }

    try {
        System.setProperty("skiko.renderApi", "OPENGL")
        isTransparent = true
    } catch (e: Exception) {
        e.printStackTrace()
    }

    val windowState = WindowState(width = 600.dp, height = 500.dp) //400 / 710
    Window(
        onCloseRequest = ::exitApplication,
        title = "YukiNotes",
        undecorated = true,
        transparent = isTransparent,
        state = windowState,
        icon = painterResource(Res.drawable.app_ico32)
    ) {
        CompositionLocalProvider(LocalWindow provides this.window) {
            App()
        }
    }
}

val LocalWindow = compositionLocalOf<ComposeWindow> {
    error("No window")
}

@Composable
@Preview
fun WindowScope.App() {
    WindowDraggableArea {
        YukiTheme {
            if (isTransparent)
                YukiAppDesktopScreen(
                    Modifier.padding(8.dp)
                    .customShadow()
//                    .drawBehind {
//                        drawRoundRect(
//                            color = YukiTheme.colors.barsShadow,
//                            topLeft = Offset(0f, 2f),
//                            size = this.size,
//                            cornerRadius = CornerRadius(12f)
//                        )
//                        drawRoundRect(
//                            color = YukiTheme.colors.bars,
//                            topLeft = Offset(0f, -2f),
//                            size = this.size,
//                            cornerRadius = CornerRadius(12f)
//                        )
//                    }
                    .clip(RoundedCornerShape(12.dp))
                )
            else
                YukiApp()
        }
    }
}