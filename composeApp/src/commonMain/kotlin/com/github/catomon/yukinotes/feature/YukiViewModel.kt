package com.github.catomon.yukinotes.feature

import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.github.catomon.yukinotes.UserSettings
import com.github.catomon.yukinotes.data.model.NoteEntity
import com.github.catomon.yukinotes.data.repository.YukiRepository
import com.github.catomon.yukinotes.domain.Note
import com.github.catomon.yukinotes.loadSettings
import com.github.catomon.yukinotes.saveSettings
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import kotlin.uuid.Uuid

class YukiViewModel(
    private val repository: YukiRepository
) : ViewModel() {

    private val _notesScreenState: MutableStateFlow<NotesScreenState> = MutableStateFlow(NotesScreenState(getAllNotes(), null))
    val notesScreenState = _notesScreenState.asStateFlow()

    val userSettings = mutableStateOf(loadSettings())

    fun updateUserSettings(newSettings: UserSettings) {
        userSettings.value = newSettings
        saveSettings(newSettings)
    }

    fun alwaysShowDetails(alwaysShowDetails: Boolean = true) {
        updateUserSettings(userSettings.value.copy(alwaysShowDetails = alwaysShowDetails))
        _notesScreenState.value = _notesScreenState.value.copy(alwaysShowDetails = alwaysShowDetails)
    }

    fun selectNote(noteId: Uuid?) {
        _notesScreenState.value = _notesScreenState.value.copy(selectedNoteId = noteId)
    }

    fun addNote(note: Note) {
        viewModelScope.launch {
            repository.insert(note.toEntity())
        }
    }

    fun removeNote(noteId: Uuid) {
        viewModelScope.launch {
            repository.delete(noteId)
        }
    }

    fun removeNote(note: Note) {
        viewModelScope.launch {
            repository.delete(note.toEntity())
        }
    }

    fun getAllNotes() : Flow<List<NoteEntity>> = repository.getAll()

    suspend fun getNoteById(uuid: Uuid) = repository.getById(uuid)

    private fun Note.toEntity(): NoteEntity =
        NoteEntity(id, title, content, createdAt, updatedAt, isPinned)
}