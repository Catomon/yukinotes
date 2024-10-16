package com.github.catomon.yukinotes

import androidx.room.RoomDatabaseConstructor
import com.github.catomon.yukinotes.data.database.YukiDatabase

expect val userFolderPath: String

expect fun createDatabase(): YukiDatabase