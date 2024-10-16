package com.github.catomon.yukinotes.data.mappers

import com.github.catomon.yukinotes.data.model.NoteEntity
import com.github.catomon.yukinotes.domain.Note

fun NoteEntity.toNote() = Note(id, title, content)