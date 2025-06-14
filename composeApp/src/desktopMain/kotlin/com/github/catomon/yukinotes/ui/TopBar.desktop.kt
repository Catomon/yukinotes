package com.github.catomon.yukinotes.ui

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Icon
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.github.catomon.yukinotes.LocalWindow
import org.jetbrains.compose.resources.painterResource
import yukinotes.composeapp.generated.resources.Res
import yukinotes.composeapp.generated.resources.minimize

@Composable
actual fun PlatformActionButton() {
    val window = LocalWindow.current
    Icon(
        painterResource(Res.drawable.minimize),
        "App Menu",
        Modifier.size(sizes.topBarSize).clickable(onClick = {
            window.isMinimized = true
        }),
        tint = YukiTheme.colors.surface
    )
}

