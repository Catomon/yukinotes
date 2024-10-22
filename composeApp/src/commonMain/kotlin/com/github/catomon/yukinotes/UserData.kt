package com.github.catomon.yukinotes

import com.github.catomon.yukinotes.data.database.YukiDatabase

expect val userFolderPath: String

expect fun createDatabase(): YukiDatabase