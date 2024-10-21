package com.github.catomon.yukinotes.data.model

import androidx.room.Entity
import androidx.room.PrimaryKey
import kotlin.uuid.ExperimentalUuidApi
import kotlin.uuid.Uuid

@Entity(tableName = "notes")
data class NoteEntity @OptIn(ExperimentalUuidApi::class) constructor(
    @PrimaryKey(autoGenerate = false) val id: Uuid,
    val title: String,
    val content: String,
    val createdAt: Long = 0,
    val updatedAt: Long = 0,
    val isPinned: Boolean = false
)