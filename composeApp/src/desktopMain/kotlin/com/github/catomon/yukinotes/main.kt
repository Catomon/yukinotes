package com.github.catomon.yukinotes

import androidx.compose.foundation.window.WindowDraggableArea
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.compositionLocalOf
import androidx.compose.ui.awt.ComposeWindow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Window
import androidx.compose.ui.window.WindowScope
import androidx.compose.ui.window.WindowState
import androidx.compose.ui.window.application
import com.github.catomon.yukinotes.di.appModule
import com.github.catomon.yukinotes.feature.YukiApp
import org.jetbrains.compose.resources.painterResource
import org.jetbrains.compose.ui.tooling.preview.Preview
import org.koin.core.context.GlobalContext.startKoin
import yukinotes.composeapp.generated.resources.Res
import yukinotes.composeapp.generated.resources.yuki
import java.awt.Window

fun main() = application {
    startKoin {
        modules(appModule)
    }

    val windowState = WindowState(width = 400.dp, height = 710.dp)
    Window(
        onCloseRequest = ::exitApplication,
        title = "Yuki Notes",
        undecorated = true,
        state = windowState,
        icon = painterResource(Res.drawable.yuki)
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
        YukiApp()
    }
}