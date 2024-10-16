package com.github.catomon.yukinotes

import androidx.room.Room
import androidx.room.RoomDatabaseConstructor
import androidx.sqlite.driver.bundled.BundledSQLiteDriver
import com.github.catomon.yukinotes.data.database.YukiDatabase

actual val userFolderPath: String = System.getProperty("user.home") + "/Documents/YukiNotes/"

actual fun createDatabase(): YukiDatabase {
    return Room.databaseBuilder<YukiDatabase>(
        userFolderPath + "yuki_database.db"
    )     .setDriver(BundledSQLiteDriver())
        .build()
}