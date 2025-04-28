package com.github.catomon.yukinotes.ui

import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.github.catomon.yukinotes.UserSettings
import com.github.catomon.yukinotes.data.model.NoteEntity
import com.github.catomon.yukinotes.data.repository.YukiRepository
import com.github.catomon.yukinotes.domain.Note
import com.github.catomon.yukinotes.exportNotesAsTxt
import com.github.catomon.yukinotes.loadSettings
import com.github.catomon.yukinotes.saveSettings
import com.github.catomon.yukinotes.storeNotesAsTxtFiles
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.last
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import kotlin.uuid.Uuid

class YukiViewModel(
    private val repository: YukiRepository
) : ViewModel() {

    val notes = getAllNotes()

    private val _notesScreenState: MutableStateFlow<NotesScreenState> =
        MutableStateFlow(NotesScreenState())
    val notesScreenState = _notesScreenState.asStateFlow()

    val userSettings = mutableStateOf(loadSettings())

    init {
        viewModelScope.launch {
            notes.collect { notes ->
                _notesScreenState.update {
//                        it.copy(notes = Array<NoteEntity>(30) {
//                            NoteEntity(
//                                id = Uuid.random(),
//                                title = "asdasdasdddddddddddddddddddddddddd",
//                                content = "dassssssssssssssssssssssssssssssssssssasd",
//                                createdAt = 1,
//                                updatedAt = 2,
//                                isPinned = false
//                            )
//                        }.toList())

                    it.copy(notes = notes)
                }
            }
        }
    }

    fun updateUserSettings(newSettings: UserSettings) {
        userSettings.value = newSettings
        saveSettings(newSettings)
    }

    fun setStoreAsTxt(storeAsTxt: Boolean) {
        updateUserSettings(userSettings.value.copy(storeAsTxtFiles = storeAsTxt))

        if (storeAsTxt)
            viewModelScope.launch {
                exportNotesAsTxt(_notesScreenState.value.notes)
            }
    }

    fun alwaysShowDetails(alwaysShowDetails: Boolean = true) {
        updateUserSettings(userSettings.value.copy(alwaysShowDetails = alwaysShowDetails))
        _notesScreenState.value =
            _notesScreenState.value.copy(alwaysShowDetails = alwaysShowDetails)
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
            if (storeNotesAsTxtFiles) _notesScreenState.value =
                _notesScreenState.value.copy(notes = repository.getAll().last())
        }
    }

    fun removeNote(note: Note) {
        viewModelScope.launch {
            repository.delete(note.toEntity())
            if (storeNotesAsTxtFiles) _notesScreenState.value =
                _notesScreenState.value.copy(notes = repository.getAll().last())
        }
    }

    fun getAllNotes(): Flow<List<NoteEntity>> = repository.getAll()

    suspend fun getNoteById(uuid: Uuid) = repository.getById(uuid)

    private fun Note.toEntity(): NoteEntity =
        NoteEntity(id, title, content, createdAt, updatedAt, isPinned)
}