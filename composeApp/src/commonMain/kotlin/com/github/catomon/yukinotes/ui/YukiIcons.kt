package com.github.catomon.yukinotes.ui

import org.jetbrains.compose.resources.DrawableResource

expect object YukiIcons {
    val appIcon: DrawableResource
    val menu: DrawableResource
    val createNote: DrawableResource
    val deleteNote: DrawableResource
    val editNote: DrawableResource
    val confirmDeleteNote: DrawableResource
    val confirm: DrawableResource
    val cancel: DrawableResource
}