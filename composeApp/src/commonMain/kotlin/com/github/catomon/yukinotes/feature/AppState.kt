package com.github.catomon.yukinotes.feature

import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import com.github.catomon.yukinotes.createDatabase
import com.github.catomon.yukinotes.data.model.NoteEntity
import com.github.catomon.yukinotes.domain.Note

class AppState {

    val database = createDatabase()

    var curScreen = mutableStateOf(Screens.NOTES)

    suspend fun addNote(note: Note) {
        database.noteDao().insert(note.toEntity())
    }

    suspend fun removeNote(note: Note) {
        database.noteDao().delete(note.toEntity())
    }

    private fun Note.toEntity() : NoteEntity = NoteEntity(id, title, content)
}

enum class Screens {
    NOTES,
    NEW_NOTE,
}