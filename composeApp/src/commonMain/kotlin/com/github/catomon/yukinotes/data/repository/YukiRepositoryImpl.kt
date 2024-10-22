package com.github.catomon.yukinotes.data.repository

import com.github.catomon.yukinotes.data.dao.NoteDao
import com.github.catomon.yukinotes.data.model.NoteEntity
import kotlinx.coroutines.flow.Flow
import kotlin.uuid.Uuid

class YukiRepositoryImpl(private val noteDao: NoteDao) : YukiRepository {

    override suspend fun insert(note: NoteEntity) {
        noteDao.insert(note)
    }

    override suspend fun update(note: NoteEntity) {
        noteDao.update(note)
    }

    override suspend fun delete(note: NoteEntity) {
        noteDao.delete(note)
    }

    override fun getAll(): Flow<List<NoteEntity>> {
        return noteDao.getAllNotes()
    }

    override suspend fun getById(id: Uuid): NoteEntity? {
        return noteDao.getNoteById(id)
    }
}
