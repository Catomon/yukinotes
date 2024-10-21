package com.github.catomon.yukinotes.data.database

import androidx.room.ConstructedBy
import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.github.catomon.yukinotes.data.database.YukiDatabaseConstructor
import com.github.catomon.yukinotes.data.dao.NoteDao
import com.github.catomon.yukinotes.data.model.Converters
import com.github.catomon.yukinotes.data.model.NoteEntity

@Database(entities = [NoteEntity::class], version = 2)
@TypeConverters(Converters::class)
@ConstructedBy(YukiDatabaseConstructor::class)
abstract class YukiDatabase : RoomDatabase() {

    abstract fun noteDao(): NoteDao
}