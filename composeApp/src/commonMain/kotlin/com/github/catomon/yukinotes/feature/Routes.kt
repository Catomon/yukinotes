package com.github.catomon.yukinotes.feature

object RouteArgs {
    const val NULL = "null"
    const val NOTE_ID = "noteId"
}

object Routes {
    const val NOTES = "notes"
    const val EDIT_NOTE = "editNote/{${RouteArgs.NOTE_ID}}"

    fun createRoute(route: String, vararg args: String) : String {
        return route.substringBefore("/") + "/" + args.joinToString("/")
    }
}