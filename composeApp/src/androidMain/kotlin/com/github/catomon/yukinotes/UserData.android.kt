package com.github.catomon.yukinotes

import android.content.Context
import androidx.room.Room
import com.github.catomon.yukinotes.data.database.YukiDatabase

actual val userFolderPath: String = ""

var context2: Context? = null

actual fun createDatabase(): YukiDatabase {
    val room = Room
        .databaseBuilder<YukiDatabase>(context2!!, userFolderPath + "yuki_database.db")
        .fallbackToDestructiveMigration(false)
        .build()
    return room
}