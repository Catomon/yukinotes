package com.github.catomon.yukinotes.feature

import com.github.catomon.yukinotes.data.model.NoteEntity
import com.github.catomon.yukinotes.data.repository.YukiRepository
import com.github.catomon.yukinotes.domain.Note

class AppState(
    val repository: YukiRepository
) {

    suspend fun addNote(note: Note) {
        repository.insert(note.toEntity())
    }

    suspend fun removeNote(note: Note) {
        repository.delete(note.toEntity())
    }

    private fun Note.toEntity(): NoteEntity =
        NoteEntity(id, title, content, createdAt, updatedAt, isPinned)
}