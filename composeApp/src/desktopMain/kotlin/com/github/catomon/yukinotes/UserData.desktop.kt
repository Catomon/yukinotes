package com.github.catomon.yukinotes

import androidx.room.Room
import androidx.sqlite.driver.bundled.BundledSQLiteDriver
import com.github.catomon.yukinotes.data.database.YukiDatabase
import java.io.File

actual val userFolderPath: String = System.getProperty("user.home") + "/Documents/YukiNotes/"

actual fun createDatabase(): YukiDatabase {
    return Room.databaseBuilder<YukiDatabase>(
        userFolderPath + "yuki_database.db"
    )     .setDriver(BundledSQLiteDriver())
        .fallbackToDestructiveMigration(true)
        .build()
}

actual val userDataFolder: File = File(System.getProperty("user.home"), "AppData/Roaming/YukiNotes")