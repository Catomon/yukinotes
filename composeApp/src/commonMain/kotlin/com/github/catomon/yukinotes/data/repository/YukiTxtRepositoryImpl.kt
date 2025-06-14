package com.github.catomon.yukinotes.data.repository

import com.github.catomon.yukinotes.data.model.NoteEntity
import com.github.catomon.yukinotes.domain.YukiRepository
import com.github.catomon.yukinotes.userFolderPath
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import java.io.File
import java.io.IOException
import kotlin.uuid.Uuid

class YukiTxtRepositoryImpl() : YukiRepository {
    private val notesFolder: File = File("$userFolderPath/notes/").also { it.mkdirs() }

    private lateinit var notes: MutableMap<Uuid, NoteEntity>

    fun collectNotes(): MutableMap<Uuid, NoteEntity> {
        val map = mutableMapOf<Uuid, NoteEntity>()
        for (note in notesFolder.listFiles() ?: return map) {
            val uuid = Uuid.random()
            map[uuid] =
                NoteEntity(
                    uuid,
                    note.nameWithoutExtension,
                    note.readText(),
                    updatedAt = note.lastModified()
                )
        }

        return map
    }

    override suspend fun insert(note: NoteEntity) {
        notes[note.id] = note
        writeNoteTxt(note)
    }

    override suspend fun update(note: NoteEntity) {
        writeNoteTxt(note)
    }

    private fun writeNoteTxt(note: NoteEntity) {
        val maxTitleLength = 255
        val truncatedTitle = if (note.title.length > maxTitleLength) {
            note.title.substring(0, maxTitleLength)
        } else {
            note.title
        }
        val fileName = "${truncatedTitle.replace(Regex("[\\\\/:*?\"<>|]"), "_")}.txt"
        val noteFile = File(notesFolder, fileName)
        val fileText = """
                            ${note.content}
                           """.trimIndent()
        noteFile.writeText(
            fileText
        )
    }

    override suspend fun delete(note: NoteEntity) {
        try {
            val fileName = "${note.title.replace(Regex("[\\\\/:*?\"<>|]"), "_")}.txt"
            File(notesFolder.path + "/${fileName}").delete()
        } catch (e: IOException) {
            e.printStackTrace()
        }
    }

    override suspend fun delete(noteId: Uuid) {
        try {
            val fileName = "${notes[noteId]?.title?.replace(Regex("[\\\\/:*?\"<>|]"), "_")}.txt"
            File(notesFolder.path + "/${fileName}").delete()
        } catch (e: IOException) {
            e.printStackTrace()
        }
    }

    override fun getAll(): Flow<List<NoteEntity>> {
        return flow {
            notes = collectNotes()
            emit(notes.values.toList())
        }
    }

    override suspend fun getById(id: Uuid): NoteEntity? {
        return notes[id]
    }
}
