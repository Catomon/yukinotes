package com.github.catomon.yukinotes.feature

object RouteArgs {
    const val NULL = "null"
    const val NOTE_ID = "noteId"
}

object Routes {
    const val NOTES = "notes"
    const val EDIT_NOTE = "editNote/{${RouteArgs.NOTE_ID}}"
    const val SETTINGS = "settings"

    fun createRoute(route: String, vararg args: String) : String {
        return if (args.isEmpty()) route else route.substringBefore("/") + "/" + args.joinToString("/")
    }
}