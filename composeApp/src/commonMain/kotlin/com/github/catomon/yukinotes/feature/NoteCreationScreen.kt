package com.github.catomon.yukinotes.feature

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material.Text
import androidx.compose.material.TextField
import androidx.compose.material.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.github.catomon.yukinotes.data.mappers.toNote
import com.github.catomon.yukinotes.domain.Note
import org.jetbrains.compose.resources.painterResource
import yukinotes.composeapp.generated.resources.Res
import yukinotes.composeapp.generated.resources.cancel
import yukinotes.composeapp.generated.resources.confirm
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

    var titleError by remember { mutableStateOf(false) }

    LaunchedEffect(newNote) {
        title = newNote?.title ?: ""
        content = newNote?.content ?: ""
    }

    Column(
        Modifier.fillMaxSize(),//.padding(start = 48.dp, end = 48.dp, top = 16.dp),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        TextField(
            value = title,
            onValueChange = {
                if (it.length <= 320)
                    title = it

                if (titleError)
                    titleError = false
            },
            label = { Text("Title") },
            modifier = Modifier.fillMaxWidth().padding(4.dp, 4.dp, 4.dp, 0.dp),
            maxLines = 3,
            colors = TextFieldDefaults.textFieldColors(textColor = Color.White),
            isError = titleError,
        )

        TextField(
            content,
            {
                content = it
            },
            label = { Text("Details") },
            modifier = Modifier.fillMaxWidth().weight(0.5f).padding(4.dp),
            colors = TextFieldDefaults.textFieldColors(textColor = Color.White)
        )

        BottomBar(
            onCancel = navBack,
            onConfirm = {
                if (title.isNotBlank()) {
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
                } else {
                    titleError = true
                }
            }
        )
    }
}

@Composable
fun BottomBar(onCancel: () -> Unit, onConfirm: () -> Unit, modifier: Modifier = Modifier) {
    Row(
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically,
        modifier = modifier.background(Colors.primary)
    ) {
        Image(
            painterResource(Res.drawable.cancel),
            "Cancel Create Note",
            Modifier.height(32.dp).width(64.dp).clickable(onClick = onCancel).weight(0.2f)
        )

        Image(
            painterResource(Res.drawable.confirm),
            "Confirm Create Note",
            Modifier.height(32.dp).width(64.dp).clickable(onClick = onConfirm).weight(0.2f)
        )
    }
}