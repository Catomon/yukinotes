package com.github.catomon.yukinotes.feature

import androidx.compose.runtime.Composable
import org.jetbrains.compose.resources.DrawableResource
import yukinotes.composeapp.generated.resources.Res
import yukinotes.composeapp.generated.resources.cancel48
import yukinotes.composeapp.generated.resources.confirm48
import yukinotes.composeapp.generated.resources.create_note48
import yukinotes.composeapp.generated.resources.delete_note48
import yukinotes.composeapp.generated.resources.edit_note48
import yukinotes.composeapp.generated.resources.menu48
import yukinotes.composeapp.generated.resources.trashcan48
import yukinotes.composeapp.generated.resources.yuki48

@Composable
actual fun PlatformActionButton() {
}

actual object AppIcons {
    actual val yuki: DrawableResource = Res.drawable.yuki48
    actual val menu: DrawableResource = Res.drawable.menu48
    actual val createNote: DrawableResource = Res.drawable.create_note48
    actual val deleteNote: DrawableResource = Res.drawable.delete_note48
    actual val editNote: DrawableResource = Res.drawable.edit_note48
    actual val confirmDeleteNote: DrawableResource = Res.drawable.trashcan48
    actual val confirm: DrawableResource = Res.drawable.confirm48
    actual val cancel: DrawableResource = Res.drawable.cancel48
}