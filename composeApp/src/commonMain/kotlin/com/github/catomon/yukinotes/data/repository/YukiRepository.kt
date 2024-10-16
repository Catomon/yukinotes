package com.github.catomon.yukinotes.data.repository

import com.github.catomon.yukinotes.data.model.NoteEntity
import kotlin.uuid.ExperimentalUuidApi
import kotlin.uuid.Uuid

interface YukiRepository {
    @OptIn(ExperimentalUuidApi::class)
    suspend fun insert(uid: Uuid, title: String, content: String)
    @OptIn(ExperimentalUuidApi::class)
    suspend fun update(uid: Uuid, title: String, content: String)
    suspend fun delete(note: NoteEntity)
}