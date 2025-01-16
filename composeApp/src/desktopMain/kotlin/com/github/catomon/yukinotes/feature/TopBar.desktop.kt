package com.github.catomon.yukinotes.feature

import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.size
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.github.catomon.yukinotes.LocalWindow
import org.jetbrains.compose.resources.DrawableResource
import org.jetbrains.compose.resources.painterResource
import yukinotes.composeapp.generated.resources.Res
import yukinotes.composeapp.generated.resources.cancel
import yukinotes.composeapp.generated.resources.confirm
import yukinotes.composeapp.generated.resources.create_note
import yukinotes.composeapp.generated.resources.delete_note
import yukinotes.composeapp.generated.resources.edit_note
import yukinotes.composeapp.generated.resources.menu
import yukinotes.composeapp.generated.resources.minimize
import yukinotes.composeapp.generated.resources.trashcan
import yukinotes.composeapp.generated.resources.yuki

@Composable
actual fun PlatformActionButton() {
    val window = LocalWindow.current
    Image(
        painterResource(Res.drawable.minimize),
        "App Menu",
        Modifier.size(sizes.topBarSize).clickable(onClick = {
            window.isMinimized = true
        })
    )
}

actual object AppIcons {
    actual val yuki: DrawableResource = Res.drawable.yuki
    actual val menu: DrawableResource = Res.drawable.menu
    actual val createNote: DrawableResource = Res.drawable.create_note
    actual val deleteNote: DrawableResource = Res.drawable.delete_note
    actual val editNote: DrawableResource = Res.drawable.edit_note
    actual val confirmDeleteNote: DrawableResource = Res.drawable.trashcan
    actual val confirm: DrawableResource = Res.drawable.confirm
    actual val cancel: DrawableResource = Res.drawable.cancel
}