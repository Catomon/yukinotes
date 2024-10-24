package com.github.catomon.yukinotes

import android.content.Context
import androidx.room.Room
import com.github.catomon.yukinotes.data.database.YukiDatabase

actual val userFolderPath: String = ""

var mainActivityContext: Context? = null

actual fun createDatabase(): YukiDatabase {
    val room = Room
        .databaseBuilder<YukiDatabase>(mainActivityContext!!, userFolderPath + "yuki_database.db")
        .fallbackToDestructiveMigration(false)
        .build()
    return room
}