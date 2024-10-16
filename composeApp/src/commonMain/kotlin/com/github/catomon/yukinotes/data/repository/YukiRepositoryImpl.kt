package com.github.catomon.yukinotes.data.repository

import com.github.catomon.yukinotes.data.dao.NoteDao
import com.github.catomon.yukinotes.data.model.NoteEntity
import kotlin.uuid.ExperimentalUuidApi
import kotlin.uuid.Uuid

class YukiRepositoryImpl(private val noteDao: NoteDao) : YukiRepository {

    @OptIn(ExperimentalUuidApi::class)
    override suspend fun insert(uid: Uuid, title: String, content: String) {
        noteDao.insert(NoteEntity(uid, title, content))
    }

    @OptIn(ExperimentalUuidApi::class)
    override suspend fun update(uid: Uuid, title: String, content: String) {
        noteDao.update(NoteEntity(uid, title, content))
    }

    override suspend fun delete(note: NoteEntity) {
        noteDao.delete(note)
    }
}
