package com.github.catomon.yukinotes.feature

import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.animateContentSize
import androidx.compose.animation.expandHorizontally
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.shrinkHorizontally
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
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
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Button
import androidx.compose.material.ButtonDefaults
import androidx.compose.material.Divider
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
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
import com.github.catomon.yukinotes.data.model.NoteEntity
import com.github.catomon.yukinotes.epochMillisToSimpleDate
import com.github.catomon.yukinotes.loadSettings
import kotlinx.coroutines.flow.Flow
import org.jetbrains.compose.resources.painterResource
import kotlin.uuid.Uuid

data class NotesScreenState(
    val notes: Flow<List<NoteEntity>>,
    val selectedNoteId: Uuid?,
    var alwaysShowDetails: Boolean = loadSettings().alwaysShowDetails,
    var confirmDelete: Boolean = false
)

@Composable
fun NotesScreen(yukiViewModel: YukiViewModel, navController: NavHostController) {
    val state by yukiViewModel.notesScreenState.collectAsState()

    var showConfirmDeleteNote by remember { mutableStateOf(false) }

    LaunchedEffect(state.selectedNoteId) {
        showConfirmDeleteNote = false
    }

    Box(Modifier.background(color = Colors.background).fillMaxSize().clickable(
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
                .padding(horizontal = sizes.notesListPadding)
        )

        BottomBar(
            noteSelected = state.selectedNoteId != null,
            isShowConfirmDelete = showConfirmDeleteNote,
            showRemoveConfirm = {
                showConfirmDeleteNote = true
            },
            removeNote = {
                state.selectedNoteId?.let { selectedNoteId ->
                    yukiViewModel.removeNote(selectedNoteId)
                }

                yukiViewModel.selectNote(null)
            },
            cancelRemove = {
                showConfirmDeleteNote = false
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
            modifier = Modifier.align(Alignment.BottomEnd).height(sizes.bottomBarSize)
                .fillMaxWidth(),
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
        item {
            Spacer(Modifier.size(sizes.notesListPadding))
        }

        items(notes.size) {
            val note = notes[it]

            NoteItem(onNoteSelected, note, selectedNoteId, state)
        }

        item {
            Spacer(Modifier.size(64.dp))
        }
    }
}

@Composable
fun NoteItem(
    onNoteSelected: (Uuid) -> Unit,
    note: NoteEntity,
    selectedNoteId: Uuid?,
    state: NotesScreenState
) {
    Column(
        Modifier
            .fillMaxWidth()
            .clickable(
                indication = null,
                interactionSource = remember { MutableInteractionSource() },
                onClick = {
                    onNoteSelected(note.id)
                })
            .padding(sizes.noteItemPadding)
            .background(
                color = Colors.noteBackground, shape = RoundedCornerShape(4.dp)
            )
            .let {
                return@let if (selectedNoteId == note.id) {
                    it.border(2.dp, color = Color.White, shape = RoundedCornerShape(4.dp))
                } else it
            }
            .padding(start = 8.dp)
            .animateContentSize()
    ) {
        Text(
            note.title,
            modifier = Modifier.fillMaxSize().padding(vertical = 6.dp),
            maxLines = 3,
            fontSize = sizes.fontHeadline,
            color = Colors.noteTextHeadline
        )

        val showDetails =
            (selectedNoteId == note.id || state.alwaysShowDetails) && note.content.isNotEmpty() && note.content.isNotBlank()
        if (showDetails) {
            Row(
                verticalAlignment = Alignment.CenterVertically
            ) {
                val formattedDate = remember(note.updatedAt) {
                    epochMillisToSimpleDate(note.updatedAt)
                }

                Divider(
                    thickness = sizes.dividerThickness,
                    modifier = Modifier.weight(0.5f),
                    color = Colors.dividers
                )

                Text(
                    formattedDate,
                    modifier = Modifier.height(0.dp).wrapContentSize(unbounded = true)
                        .padding(bottom = sizes.noteDatePadding),
                    color = Colors.noteTextSmall,
                    maxLines = 1,
                    fontSize = sizes.fontSmall,
                )

                Divider(
                    thickness = sizes.dividerThickness,
                    modifier = Modifier.padding(end = 8.dp).weight(0.04f),
                    color = Colors.dividers
                )
            }

            Text(
                note.content,
                modifier = Modifier.fillMaxWidth().padding(bottom = 6.dp),
                color = Colors.noteText,
                maxLines = 6,
                fontSize = sizes.font
            )
        }
    }
}

@Composable
fun BottomBar(
    noteSelected: Boolean,
    isShowConfirmDelete: Boolean,
    showRemoveConfirm: () -> Unit,
    removeNote: () -> Unit,
    cancelRemove: () -> Unit,
    createNote: () -> Unit,
    editNote: () -> Unit,
    modifier: Modifier = Modifier
) {
    Row(
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically,
        modifier = modifier.background(Colors.bars)
    ) {
        AnimatedVisibility(
            noteSelected,
            enter = fadeIn() + expandHorizontally(expandFrom = Alignment.Start),
            exit = fadeOut() + shrinkHorizontally(shrinkTowards = Alignment.Start),
        ) {
            AnimatedContent(isShowConfirmDelete && noteSelected) { showConfirmDeleteSelectedNote ->
                if (showConfirmDeleteSelectedNote) {
                    Row {
                        Image(
                            painterResource(YukiIcons.cancel),
                            "Cancel Delete Note",
                            Modifier.height(32.dp).width(64.dp).clickable(onClick = cancelRemove)
                                .weight(0.2f)
                        )

                        Image(
                            painterResource(YukiIcons.confirmDeleteNote),
                            "Trashcan", modifier = Modifier.height(32.dp)
                        )

                        Image(
                            painterResource(YukiIcons.confirm),
                            "Confirm Delete Note",
                            Modifier.height(32.dp).width(64.dp).clickable(onClick = {
                                removeNote()
                            }).weight(0.2f)
                        )
                    }
                } else {
                    Image(
                        painterResource(YukiIcons.deleteNote),
                        "Delete Note",
                        Modifier.height(32.dp).width(64.dp).clickable(onClick = showRemoveConfirm)
                            .weight(0.2f)
                    )
                }
            }
        }

        Image(
            painterResource(YukiIcons.createNote),
            "Create Note",
            Modifier.height(32.dp).clickable(onClick = createNote).weight(0.6f)
        )

        AnimatedVisibility(noteSelected) {
            Image(
                painterResource(YukiIcons.editNote),
                "Edit Note",
                Modifier.height(32.dp).width(64.dp).clickable(onClick = editNote).weight(0.2f)
            )
        }
    }
}