package com.github.catomon.yukinotes

import com.github.catomon.yukinotes.data.database.YukiDatabase
import com.github.catomon.yukinotes.data.model.NoteEntity
import com.github.catomon.yukinotes.feature.Themes
import kotlinx.serialization.Serializable
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json
import java.io.File
import java.io.IOException

expect val userFolderPath: String

expect fun createDatabase(): YukiDatabase

expect val userDataFolder: File

val storeNotesAsTxtFiles by lazy { loadSettings().storeAsTxtFiles }

suspend fun exportNotesAsTxt(notes: List<NoteEntity>) {
    val notesFolder = File("$userFolderPath/notes/")
    try {
        notesFolder.mkdirs()
    } catch (e: IOException) {
        e.printStackTrace()
    }

    for (note in notes) {
        val maxTitleLength = 255
        val truncatedTitle = if (note.title.length > maxTitleLength) {
            note.title.substring(0, maxTitleLength)
        } else {
            note.title
        }
        val fileName = "${truncatedTitle.replace(Regex("[\\\\/:*?\"<>|]"), "_")}.txt"
        val noteFile = File(notesFolder, fileName)
        if (noteFile.exists() && noteFile.lastModified() > note.updatedAt) continue
        val fileText = """
                        ${note.content}
                       """.trimIndent()
        try {
            noteFile.writeText(
                fileText
            )
            noteFile.setLastModified(note.updatedAt)
        } catch (e: IOException) {
            e.printStackTrace()
        }
    }
}

fun saveSettings(settings: UserSettings) {
    try {
        val settingsFolder = File(userDataFolder.path)
        if (!settingsFolder.exists())
            settingsFolder.mkdirs()

        val file = File(userDataFolder.path + "/settings.json")
        if (!file.exists()) {
            file.createNewFile()
        }
        file.writeText(Json.encodeToString(settings))

    } catch (e: Exception) {
        e.printStackTrace()
    }
}

fun loadSettings(): UserSettings {
    try {
        val settingsFile = File(userDataFolder.path + "/settings.json")
        if (settingsFile.exists())
            return Json.decodeFromString(settingsFile.readText())
    } catch (e: Exception) {
        e.printStackTrace()
    }
    return UserSettings()
}

@Serializable
data class UserSettings(
    val theme: String = Themes.list.first().name,
    val alwaysShowDetails: Boolean = true,
    val storeAsTxtFiles: Boolean = false,
)
