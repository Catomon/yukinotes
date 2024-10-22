package com.github.catomon.yukinotes.feature

import androidx.compose.runtime.mutableIntStateOf
import com.github.catomon.yukinotes.createDatabase
import com.github.catomon.yukinotes.data.model.NoteEntity
import com.github.catomon.yukinotes.data.repository.YukiRepository
import com.github.catomon.yukinotes.data.repository.YukiRepositoryImpl
import com.github.catomon.yukinotes.domain.Note

class AppState {

    val database = createDatabase()
    val repository: YukiRepository = YukiRepositoryImpl(database.noteDao())

    val selectedNoteIndex = mutableIntStateOf(-1)

    suspend fun addNote(note: Note) {
        database.noteDao().insert(note.toEntity())
    }

    suspend fun removeNote(note: Note) {
        database.noteDao().delete(note.toEntity())
    }

    private fun Note.toEntity(): NoteEntity =
        NoteEntity(id, title, content, createdAt, updatedAt, isPinned)
}