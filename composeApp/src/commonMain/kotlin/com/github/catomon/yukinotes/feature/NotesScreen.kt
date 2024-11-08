package com.github.catomon.yukinotes.feature

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.animateContentSize
import androidx.compose.animation.expandHorizontally
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.shrinkHorizontally
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Button
import androidx.compose.material.ButtonDefaults
import androidx.compose.material.Divider
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import com.github.catomon.yukinotes.data.model.NoteEntity
import kotlinx.coroutines.flow.Flow
import kotlin.uuid.Uuid

data class NotesScreenState(val notes: Flow<List<NoteEntity>>, val selectedNoteId: Uuid?, var alwaysShowDetails: Boolean = false)

@Composable
fun NotesScreen(yukiViewModel: YukiViewModel, navController: NavHostController) {
    val state by yukiViewModel.notesScreenState.collectAsState()

    Box(Modifier.background(color = Colors.yukiEyes).fillMaxSize().padding(1.dp).clickable(
        interactionSource = remember { MutableInteractionSource() },
        indication = null
    ) {
        yukiViewModel.selectNote(null)
    }) {
        NotesList(
            state,
            onNoteSelected = { noteId ->
                yukiViewModel.selectNote(if (state.selectedNoteId != noteId) noteId else null)
            },
            modifier = Modifier.align(Alignment.TopStart)
        )

        NoteActionButtons(
            noteSelected = state.selectedNoteId != null,
            removeNote = {
                state.selectedNoteId?.let { selectedNoteId ->
                    yukiViewModel.removeNote(selectedNoteId)
                }

                yukiViewModel.selectNote(null)
            },
            createNote = {
                navController.navigate(
                    Routes.createRoute(Routes.EDIT_NOTE, RouteArgs.NULL)
                )

                yukiViewModel.selectNote(null)
            },
            editNote = {
                state.selectedNoteId?.let { selectedNoteId ->
                    navController.navigate(
                        Routes.createRoute(Routes.EDIT_NOTE, selectedNoteId.toString())
                    )
                }
            },
            modifier = Modifier.align(Alignment.BottomEnd).height(64.dp)
        )
    }
}

@Composable
fun NotesList(
    state: NotesScreenState,
    onNoteSelected: (Uuid) -> Unit,
    modifier: Modifier = Modifier
) {
    val notes by state.notes.collectAsState(emptyList())
    val selectedNoteId = state.selectedNoteId

    LazyColumn(modifier = modifier) {
        items(notes.size) {
            val note = notes[it]

            Column(
                Modifier
                    .fillMaxWidth()
                    .clickable(indication = null, interactionSource = remember { MutableInteractionSource() }, onClick = {
                        onNoteSelected(note.id)
                    })
                    .padding(1.dp)
                    .background(
                        color = when (selectedNoteId) {
                            null, note.id -> Color.White
                            else -> Color.LightGray
                        }, shape = RoundedCornerShape(4.dp)
                    )
                    .padding(start = 8.dp)
                    .animateContentSize()
            ) {
                Text(note.title, modifier = Modifier.fillMaxSize().padding(vertical = 6.dp), maxLines = 3)

                val showDetails = (selectedNoteId == note.id || state.alwaysShowDetails) && note.content.isNotEmpty() && note.content.isNotBlank()
                if (showDetails) {
                    Divider(thickness = 2.dp, modifier = Modifier.padding(end = 8.dp))

                    Text(
                        note.content,
                        modifier = Modifier.fillMaxWidth().padding(bottom = 6.dp),
                        color = Color.DarkGray,
                        maxLines = 6,
                        fontSize = 12.sp
                    )
                }
            }
        }

        item {
            Spacer(Modifier.size(64.dp))
        }
    }
}

@Composable
fun NoteActionButtons(
    noteSelected: Boolean,
    removeNote: () -> Unit,
    createNote: () -> Unit,
    editNote: () -> Unit,
    modifier: Modifier = Modifier
) {
    Row(
        modifier.fillMaxWidth().padding(8.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.Bottom
    ) {
        AnimatedVisibility(
            noteSelected,
            enter = fadeIn() + expandHorizontally(expandFrom = Alignment.Start),
            exit = fadeOut() + shrinkHorizontally(shrinkTowards = Alignment.Start),
        ) {
            NoteActionButton(onClick = removeNote, buttonText = "delete")
        }

        NoteActionButton(Modifier.weight(1f).padding(horizontal = 8.dp), onClick = createNote, buttonText = "create new")

        AnimatedVisibility(noteSelected) {
            NoteActionButton(onClick = editNote, buttonText = "edit")
        }
    }
}

@Composable
fun NoteActionButton(
    modifier: Modifier = Modifier,
    onClick: () -> Unit,
    buttonText: String
) {
    Button(
        onClick = onClick,
        shape = CircleShape,
        colors = ButtonDefaults.buttonColors(backgroundColor = Colors.yukiHair),
        modifier = modifier
    ) {
        Text(buttonText)
    }
}