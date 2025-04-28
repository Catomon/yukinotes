package com.github.catomon.yukinotes.ui

import org.jetbrains.compose.resources.DrawableResource
import yukinotes.composeapp.generated.resources.Res
import yukinotes.composeapp.generated.resources.cancel
import yukinotes.composeapp.generated.resources.confirm
import yukinotes.composeapp.generated.resources.create_note
import yukinotes.composeapp.generated.resources.delete_note
import yukinotes.composeapp.generated.resources.edit_note
import yukinotes.composeapp.generated.resources.menu
import yukinotes.composeapp.generated.resources.notepad_icon48
import yukinotes.composeapp.generated.resources.trashcan

actual object YukiIcons {
    actual val appIcon: DrawableResource = Res.drawable.notepad_icon48
    actual val menu: DrawableResource = Res.drawable.menu
    actual val createNote: DrawableResource = Res.drawable.create_note
    actual val deleteNote: DrawableResource = Res.drawable.delete_note
    actual val editNote: DrawableResource = Res.drawable.edit_note
    actual val confirmDeleteNote: DrawableResource = Res.drawable.trashcan
    actual val confirm: DrawableResource = Res.drawable.confirm
    actual val cancel: DrawableResource = Res.drawable.cancel
}