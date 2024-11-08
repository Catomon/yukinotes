package com.github.catomon.yukinotes.feature

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.Button
import androidx.compose.material.Text
import androidx.compose.material.TextButton
import androidx.compose.material.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.github.catomon.yukinotes.data.mappers.toNote
import com.github.catomon.yukinotes.domain.Note
import java.time.ZoneId
import java.time.ZonedDateTime
import kotlin.uuid.Uuid

@Composable
fun NoteCreationScreen(yukiViewModel: YukiViewModel, noteId: String? = null, navBack: () -> Unit) {
    var newNote by remember { mutableStateOf<Note?>(null) }

    LaunchedEffect(null) {
        if (noteId != null) {
            yukiViewModel.getNoteById(Uuid.parse(noteId))?.toNote()?.let {
                newNote = it
            }
        }
    }

    var title by remember { mutableStateOf("") }
    var content by remember { mutableStateOf("") }

    LaunchedEffect(newNote) {
        title = newNote?.title ?: ""
        content = newNote?.content ?: ""
    }

    Column(
        Modifier.fillMaxSize().padding(start = 48.dp, end = 48.dp, top = 16.dp),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        TextField(
            value = title,
            onValueChange = {
                if (it.length <= 320)
                    title = it
            },
            label = { Text("Title") },
            modifier = Modifier.fillMaxWidth(),
            maxLines = 3
        )

        TextField(
            content,
            {
                content = it
            },
            label = { Text("Details") },
            modifier = Modifier.fillMaxWidth().weight(0.5f),
        )

        Row(horizontalArrangement = Arrangement.SpaceBetween, modifier = Modifier.fillMaxWidth()) {
            TextButton({
                navBack()
            }) {
                Text("Cancel")
            }

            Button({
                if (title.isNotEmpty()) {
                    val curTime =
                        ZonedDateTime.now(ZoneId.systemDefault()).toInstant().toEpochMilli()
                    yukiViewModel.addNote(
                        Note(
                            id = newNote?.id ?: Uuid.random(),
                            title = title,
                            content = content,
                            createdAt = newNote?.createdAt ?: curTime,
                            updatedAt = curTime,
                        )
                    )

                    navBack()
                }
            }) {
                Text("Save")
            }
        }
    }
}