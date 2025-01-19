package com.github.catomon.yukinotes

import com.github.catomon.yukinotes.data.database.YukiDatabase
import com.github.catomon.yukinotes.feature.Themes
import kotlinx.serialization.Serializable
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json
import java.io.File

expect val userFolderPath: String

expect fun createDatabase(): YukiDatabase

expect val userDataFolder: File

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

fun loadSettings() : UserSettings {
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
)