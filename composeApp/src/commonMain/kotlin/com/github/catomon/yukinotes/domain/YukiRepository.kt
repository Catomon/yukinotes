package com.github.catomon.yukinotes.domain

import com.github.catomon.yukinotes.data.model.NoteEntity
import kotlinx.coroutines.flow.Flow
import kotlin.uuid.Uuid

interface YukiRepository {
    suspend fun insert(note: NoteEntity)
    suspend fun update(note: NoteEntity)
    suspend fun delete(note: NoteEntity)
    suspend fun delete(noteId: Uuid)
    fun getAll(): Flow<List<NoteEntity>>
    suspend fun getById(id: Uuid): NoteEntity?
}