package com.github.catomon.yukinotes.feature

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.github.catomon.yukinotes.data.model.NoteEntity
import com.github.catomon.yukinotes.data.repository.YukiRepository
import com.github.catomon.yukinotes.domain.Note
import kotlinx.coroutines.launch

class YukiViewModel(
    val repository: YukiRepository
) : ViewModel() {

    fun addNote(note: Note) {
        viewModelScope.launch {
            repository.insert(note.toEntity())
        }
    }

    fun removeNote(note: Note) {
        viewModelScope.launch {
            repository.delete(note.toEntity())
        }
    }

    private fun Note.toEntity(): NoteEntity =
        NoteEntity(id, title, content, createdAt, updatedAt, isPinned)
}