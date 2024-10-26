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
import androidx.compose.material.Icon
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Edit
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import com.github.catomon.yukinotes.data.mappers.toNote
import kotlin.uuid.Uuid

@Composable
fun NotesScreen(yukiViewModel: YukiViewModel, navController: NavHostController) {
    val notes = yukiViewModel.getAllNotes().collectAsState(emptyList())
    var selectedNoteId by remember { mutableStateOf<Uuid?>(null) }

    Box(Modifier.background(color = Colors.yukiEyes).fillMaxSize().padding(1.dp).clickable(
        interactionSource = remember { MutableInteractionSource() },
        indication = null
    ) {
        selectedNoteId = null
    }) {
        LazyColumn(modifier = Modifier.align(Alignment.TopStart)) {
            items(notes.value.size) {
                val note = notes.value[it]

                Column(
                    Modifier
                        .fillMaxWidth()
                        .clickable {
                            selectedNoteId = if (selectedNoteId != note.id) note.id else null
                        }
                        .padding(1.dp)
                        .background(color = when (selectedNoteId) {
                            null, note.id -> Color.White
                            else -> Color.Gray
                        }, shape = RoundedCornerShape(4.dp))
                        .padding(start = 8.dp)
                        .animateContentSize()
                ) {
                    Text(note.title, modifier = Modifier.fillMaxSize(), maxLines = 3)

                    if (selectedNoteId == note.id && note.content.isNotEmpty() && note.content.isNotBlank()) {
                        Divider(thickness = 2.dp, modifier = Modifier.padding(end = 8.dp))

                        Text(
                            note.content,
                            modifier = Modifier.fillMaxWidth(),
                            color = Color.DarkGray,
                            maxLines = 6
                        )
                    }
                }
            }

            item {
                Spacer(Modifier.size(64.dp))
            }
        }

        Row(
            Modifier.align(Alignment.BottomEnd).fillMaxWidth().padding(8.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.Bottom
        ) {
            val note = notes.value.find { it.id == selectedNoteId }?.toNote()

            AnimatedVisibility(
                note != null,
                enter = fadeIn() + expandHorizontally(expandFrom = Alignment.Start),
                exit = fadeOut() + shrinkHorizontally(shrinkTowards = Alignment.Start),
            ) {
                RemoveNoteButton {
                    if (note != null) {
                        yukiViewModel.removeNote(note)
                    }
                }
            }

            NoteCreateButton(Modifier.weight(1f).padding(horizontal = 8.dp)) {
                navController.navigate(
                    Routes.createRoute(Routes.EDIT_NOTE, RouteArgs.NULL)
                )
            }

            AnimatedVisibility(note != null) {
                EditNoteButton {
                    if (note != null) {
                        navController.navigate(
                            Routes.createRoute(Routes.EDIT_NOTE, note.id.toString())
                        )
                    }
                }
            }
        }
    }
}

@Composable
fun NoteCreateButton(modifier: Modifier = Modifier, onClick: () -> Unit) {
    Button(
        onClick = onClick,
        shape = CircleShape,
        colors = ButtonDefaults.buttonColors(backgroundColor = Colors.yukiHair),
        modifier = modifier.size(32.dp)
    ) {
        Text("create new")
//        Icon(Icons.Default.Add, "Add Note", tint = Color.White)
    }
}

@Composable
fun EditNoteButton(onClick: () -> Unit) {
    Button(
        onClick = onClick,
        shape = CircleShape,
        colors = ButtonDefaults.buttonColors(backgroundColor = Colors.yukiHair),
        modifier = Modifier.height(32.dp)
    ) {
        Text("edit")
//        Icon(Icons.Default.Edit, "Edit Note", tint = Color.White)
    }
}

@Composable
fun RemoveNoteButton(onClick: () -> Unit) {
    Button(
        onClick = onClick,
        shape = CircleShape,
        colors = ButtonDefaults.buttonColors(backgroundColor = Colors.yukiHair),
        modifier = Modifier.height(32.dp)
    ) {
        Text("delete")
//        Icon(Icons.Default.Delete, "Remove Note", tint = Color.White)
    }
}