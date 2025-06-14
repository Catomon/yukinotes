package com.github.catomon.yukinotes.ui

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.github.catomon.yukinotes.data.mappers.toNote
import com.github.catomon.yukinotes.domain.Note
import com.github.catomon.yukinotes.epochMillisToSimpleDate
import org.jetbrains.compose.resources.painterResource
import yukinotes.composeapp.generated.resources.Res
import yukinotes.composeapp.generated.resources.cancel
import yukinotes.composeapp.generated.resources.confirm
import java.time.ZoneId
import java.time.ZonedDateTime
import kotlin.uuid.Uuid

@Composable
fun NoteEditScreen(yukiViewModel: YukiViewModel, noteId: String? = null, navBack: () -> Unit) {
    var editedNote by remember { mutableStateOf<Note?>(null) }

    LaunchedEffect(null) {
        if (noteId != null) {
            yukiViewModel.getNoteById(Uuid.parse(noteId))?.toNote()?.let {
                editedNote = it
            }
        }
    }

    var title by remember { mutableStateOf("") }
    var content by remember { mutableStateOf("") }

    var titleError by remember { mutableStateOf(false) }

    LaunchedEffect(editedNote) {
        title = editedNote?.title ?: ""
        content = editedNote?.content ?: ""
    }

    Column(
        Modifier.fillMaxSize(),//.padding(start = 48.dp, end = 48.dp, top = 16.dp),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        TextField(
            value = title,
            onValueChange = {
                if (it.length <= 255)
                    title = it

                if (titleError)
                    titleError = false
            },
            label = { Text("Title:") },
            modifier = Modifier.fillMaxWidth().padding(
                start = 4.dp,
                top = 4.dp,
                end = 4.dp,
                bottom = 0.dp
            ),
            maxLines = 1,
            colors = TextFieldDefaults.colors(
                unfocusedTextColor = Color.White,
                focusedTextColor = Color.White,
                cursorColor = Color.White,
                unfocusedContainerColor = YukiTheme.bars,
                focusedContainerColor = YukiTheme.bars,
                errorContainerColor = YukiTheme.bars,
                unfocusedLabelColor = YukiTheme.colors.surface,
                focusedLabelColor = YukiTheme.colors.surface,
                unfocusedIndicatorColor = YukiTheme.bars,
            ),
            isError = titleError,
        )

        TextField(
            content,
            {
                content = it
            },
            label = { Text("Details:") },
            modifier = Modifier.fillMaxWidth().weight(0.99f).padding(vertical = 4.dp),
            colors = TextFieldDefaults.colors(
                unfocusedTextColor = Color.White,
                focusedTextColor = Color.White,
                cursorColor = Color.White,
                unfocusedContainerColor = YukiTheme.bars,
                focusedContainerColor = YukiTheme.bars,
                errorContainerColor = YukiTheme.bars,
                unfocusedLabelColor = YukiTheme.colors.surface,
                focusedLabelColor = YukiTheme.colors.surface,
                unfocusedIndicatorColor = YukiTheme.bars,
            )
        )

        editedNote?.updatedAt?.let { millis ->
            if (millis > 0)
                Text("Edited: ${epochMillisToSimpleDate(millis)}", color = YukiTheme.colors.surface)
        }

        BottomBar(
            onCancel = navBack,
            onConfirm = {
                if (title.isNotBlank()) {
                    val curTime =
                        ZonedDateTime.now(ZoneId.systemDefault()).toInstant().toEpochMilli()
                    yukiViewModel.addNote(
                        Note(
                            id = editedNote?.id ?: Uuid.random(),
                            title = title,
                            content = content,
                            createdAt = editedNote?.createdAt ?: curTime,
                            updatedAt = curTime,
                        )
                    )
                    navBack()
                } else {
                    titleError = true
                }
            },
            modifier = Modifier.height(sizes.bottomBarSize)
        )
    }
}

@Composable
fun NoteEditPane(yukiViewModel: YukiViewModel, noteId: String? = null) {
    var note by remember { mutableStateOf<Note?>(null) }
    var isChanged by remember { mutableStateOf(false) }
    var isLoading by remember { mutableStateOf(noteId != null) }

    LaunchedEffect(Unit) {
        if (noteId != null) {
            yukiViewModel.getNoteById(Uuid.parse(noteId))?.toNote()?.let {
                note = it
            }

            if (note == null)
                isLoading = false
        }
    }

    var title by remember { mutableStateOf("") }
    var content by remember { mutableStateOf("") }

    var titleError by remember { mutableStateOf(false) }

    LaunchedEffect(note, isChanged) {
        if (!isChanged) {
            title = note?.title ?: ""
            content = note?.content ?: ""

            if (note != null)
                isLoading = false
        }
    }

    AnimatedVisibility(!isLoading, enter = fadeIn(), exit = fadeOut()) {
        Column(
            Modifier.fillMaxSize(),//.padding(start = 48.dp, end = 48.dp, top = 16.dp),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally,
        ) {
            TextField(
                value = title,
                onValueChange = {
                    if (it.length <= 255)
                        title = it

                    if (titleError)
                        titleError = false

                    isChanged = true
                },
                label = { Text("Title:", color = YukiTheme.textOnBackground) },
                modifier = Modifier.fillMaxWidth().padding(
                    start = 4.dp,
                    top = 4.dp,
                    end = 4.dp,
                    bottom = 0.dp
                ),
                maxLines = 1,
                colors = TextFieldDefaults.colors(
                    unfocusedTextColor = Color.White,
                    focusedTextColor = Color.White,
                    cursorColor = Color.White,
                    unfocusedContainerColor = Color.Transparent,
                    focusedContainerColor = Color.Transparent,
                    errorContainerColor = Color.Transparent,
                    unfocusedLabelColor = YukiTheme.colors.surface,
                    focusedLabelColor = YukiTheme.colors.surface,
                    unfocusedIndicatorColor = Color.Transparent,
                    focusedIndicatorColor = Color.Transparent
                ),
                isError = titleError,
            )

            TextField(
                content,
                {
                    content = it
                    isChanged = true
                },
                label = { Text("Details:", color = YukiTheme.textOnBackground) },
                modifier = Modifier.fillMaxWidth().weight(0.99f).padding(vertical = 4.dp),
                colors = TextFieldDefaults.colors(
                    unfocusedTextColor = Color.White,
                    focusedTextColor = Color.White,
                    cursorColor = Color.White,
                    unfocusedContainerColor = Color.Transparent,
                    focusedContainerColor = Color.Transparent,
                    errorContainerColor = Color.Transparent,
                    unfocusedLabelColor = YukiTheme.colors.surface,
                    focusedLabelColor = YukiTheme.colors.surface,
                    unfocusedIndicatorColor = Color.Transparent,
                    focusedIndicatorColor = Color.Transparent
                )
            )

            note?.updatedAt?.let { millis ->
                if (millis > 0)
                    Text("Edited: ${epochMillisToSimpleDate(millis)}", color = YukiTheme.textOnBackground)
            }

            if (isChanged)
                Row(
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier.height(sizes.bottomBarSize)
                ) {
                    Icon(
                        painterResource(Res.drawable.cancel),
                        "Undo changes",
                        Modifier.fillMaxHeight().width(64.dp).clip(RoundedCornerShape(8.dp))
                            .clickable(onClick = {
                                isChanged = false
                            }).weight(0.2f),
                        tint = YukiTheme.textOnBackground
                    )

                    Icon(
                        painterResource(Res.drawable.confirm),
                        "Confirm",
                        Modifier.fillMaxHeight().width(64.dp).clip(RoundedCornerShape(8.dp))
                            .clickable(onClick = {
                                if (title.isNotBlank()) {
                                    val curTime =
                                        ZonedDateTime.now(ZoneId.systemDefault()).toInstant().toEpochMilli()
                                    yukiViewModel.addNote(
                                        Note(
                                            id = note?.id ?: Uuid.random(),
                                            title = title,
                                            content = content,
                                            createdAt = note?.createdAt ?: curTime,
                                            updatedAt = curTime,
                                        )
                                    )

                                    isChanged = false
                                } else {
                                    titleError = true
                                }
                            }).weight(0.2f),
                        tint = YukiTheme.textOnBackground
                    )
                }
        }
    }
}

@Composable
fun BottomBar(onCancel: () -> Unit, onConfirm: () -> Unit, modifier: Modifier = Modifier) {
    Row(
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically,
        modifier = modifier.background(YukiTheme.bars)
    ) {
        Icon(
            painterResource(Res.drawable.cancel),
            "Cancel",
            Modifier.fillMaxHeight().width(64.dp).clip(RoundedCornerShape(8.dp))
                .clickable(onClick = onCancel).weight(0.2f),
            tint = YukiTheme.colors.surface
        )

        Icon(
            painterResource(Res.drawable.confirm),
            "Confirm",
            Modifier.fillMaxHeight().width(64.dp).clip(RoundedCornerShape(8.dp))
                .clickable(onClick = onConfirm).weight(0.2f),
            tint = YukiTheme.colors.surface
        )
    }
}