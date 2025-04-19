package com.github.catomon.yukinotes.ui

import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.animateContentSize
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.requiredHeightIn
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.lazy.staggeredgrid.LazyVerticalStaggeredGrid
import androidx.compose.foundation.lazy.staggeredgrid.StaggeredGridCells
import androidx.compose.foundation.lazy.staggeredgrid.rememberLazyStaggeredGridState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Divider
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.geometry.CornerRadius
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import com.github.catomon.yukinotes.data.model.NoteEntity
import com.github.catomon.yukinotes.epochMillisToSimpleDate
import com.github.catomon.yukinotes.loadSettings
import kotlinx.coroutines.launch
import org.jetbrains.compose.resources.painterResource
import kotlin.uuid.Uuid

data class NotesScreenState(
    val notes: List<NoteEntity> = emptyList(),
    val selectedNoteId: Uuid? = null,
    var alwaysShowDetails: Boolean = loadSettings().alwaysShowDetails,
    var confirmDelete: Boolean = false
)

@Composable
fun NotesScreen(viewModel: YukiViewModel, navController: NavHostController) {
    val state by viewModel.notesScreenState.collectAsState()

    var showConfirmDeleteNote by remember { mutableStateOf(false) }

    LaunchedEffect(state.selectedNoteId) {
        showConfirmDeleteNote = false
    }

    Box(Modifier.background(color = Colors.background).fillMaxSize().clickable(
        interactionSource = remember { MutableInteractionSource() }, indication = null
    ) {
        viewModel.selectNote(null)
    }) {
        if (true)
            NotesStaggeredGrid(
                state,
                onNoteSelected = { noteId ->
                    viewModel.selectNote(if (state.selectedNoteId != noteId) noteId else null)
                },
                modifier = Modifier.align(Alignment.TopStart)
                    .padding(horizontal = sizes.notesListPadding)
            )
        else
            NotesList(
                state,
                onNoteSelected = { noteId ->
                    viewModel.selectNote(if (state.selectedNoteId != noteId) noteId else null)
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
                    viewModel.removeNote(selectedNoteId)
                }

                viewModel.selectNote(null)
            },
            cancelRemove = {
                showConfirmDeleteNote = false
            },
            createNote = {
                navController.navigate(
                    Routes.createRoute(Routes.EDIT_NOTE, RouteArgs.NULL)
                )

                viewModel.selectNote(null)
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
fun NotesStaggeredGrid(
    state: NotesScreenState, onNoteSelected: (Uuid) -> Unit, modifier: Modifier = Modifier
) {
    val notes = state.notes
    val selectedNoteId = state.selectedNoteId
    val gridState = rememberLazyStaggeredGridState()
    val coroutineScope = rememberCoroutineScope()

    LazyVerticalStaggeredGrid(
        state = gridState,
        columns = StaggeredGridCells.Adaptive(sizes.noteItemWidth),
        verticalItemSpacing = 2.dp,
        horizontalArrangement = Arrangement.spacedBy(2.dp),
        contentPadding = PaddingValues(
            start = 0.dp,
            top = 0.dp,
            end = 0.dp,
            bottom = 32.dp
        ),
        modifier = modifier
    ) {
        items(notes.size) { index ->
            val note = notes[index]

            NoteItem(
                { uuid ->
                    if (selectedNoteId == note.id) coroutineScope.launch {
                        gridState.animateScrollToItem(index)
                    }
                    onNoteSelected(uuid)
                }, note, selectedNoteId, state
            )
        }
    }
}

@Composable
fun NotesList(
    state: NotesScreenState, onNoteSelected: (Uuid) -> Unit, modifier: Modifier = Modifier
) {
    val notes = state.notes
    val selectedNoteId = state.selectedNoteId
    val listState = rememberLazyListState()
    val coroutineScope = rememberCoroutineScope()

    LazyColumn(state = listState, modifier = modifier) {
        item {
            Spacer(Modifier.size(sizes.notesListPadding))
        }

        items(notes.size) { index ->
            val note = notes[index]

            NoteItem(
                { uuid ->
                    if (selectedNoteId == note.id) coroutineScope.launch {
                        listState.animateScrollToItem(index)
                    }
                    onNoteSelected(uuid)
                }, note, selectedNoteId, state
            )
        }

        item {
            Spacer(Modifier.size(64.dp))
        }
    }
}

@Composable
fun NoteItem(
    onNoteSelected: (Uuid) -> Unit, note: NoteEntity, selectedNoteId: Uuid?, state: NotesScreenState
) {
    var isContentOverflow by remember { mutableStateOf(false) }
    val showDetails =
        (selectedNoteId == note.id || state.alwaysShowDetails) && note.content.isNotEmpty() && note.content.isNotBlank()
    val isSelected = selectedNoteId == note.id

    Box(contentAlignment = Alignment.Center) {
        Column(
            Modifier.fillMaxWidth().clickable(indication = null,
                interactionSource = remember { MutableInteractionSource() },
                onClick = {
                    onNoteSelected(note.id)
                }).padding(sizes.noteItemPadding).background(
                color = Colors.noteBackground, shape = RoundedCornerShape(4.dp)
            ).let {
                return@let if (isSelected) {
                    it.border(2.dp, color = Color.White, shape = RoundedCornerShape(4.dp))
                } else it
            }.padding(start = 8.dp).animateContentSize()
        ) {
            Text(
                note.title,
                modifier = Modifier.fillMaxSize().padding(vertical = 6.dp),
                maxLines = 3,
                fontSize = sizes.fontHeadline,
                color = Colors.noteTextHeadline
            )

            if (showDetails) {
                Divider(
                    thickness = sizes.dividerThickness,
                    modifier = Modifier.fillMaxWidth().padding(end = 8.dp),
                    color = Colors.dividers
                )

                Text(note.content, modifier = Modifier.fillMaxWidth().padding(bottom = 6.dp).let {
                    if (isSelected) {
                        it
                    } else {
                        it.requiredHeightIn(0.dp, 150.dp)
                    }
                }, color = Colors.noteText,
//                    maxLines = 6,
                    fontSize = sizes.font, overflow = TextOverflow.Ellipsis, onTextLayout = {
                        isContentOverflow = it.hasVisualOverflow
                    })
            }

            if (isSelected) {
                Box(contentAlignment = Alignment.Center,
                    modifier = Modifier.padding(end = 8.dp, bottom = 8.dp).align(Alignment.End)
                        .drawBehind {
                            drawRoundRect(
                                color = Colors.noteBackground,
                                topLeft = Offset(0f, 0f + size.height / 3f),
                                size = Size(size.width, size.height / 1.5f),
                                cornerRadius = CornerRadius(4f, 4f)
                            )
                        }) {
                    Text(
                        remember(note.updatedAt) {
                            epochMillisToSimpleDate(note.updatedAt)
                        },
                        color = Colors.noteTextSmall,
                        maxLines = 1,
                        fontSize = sizes.fontSmall,
                    )
                }
            }
        }

        if (!isSelected && ((showDetails && isContentOverflow) || (!state.alwaysShowDetails && note.content.isNotBlank()))) {
            Box(contentAlignment = Alignment.Center,
                modifier = Modifier.padding(end = 8.dp, bottom = 8.dp).align(Alignment.BottomEnd)
                    .drawBehind {
                        drawRoundRect(
                            color = Colors.noteBackground,
                            topLeft = Offset(0f, 0f + size.height / 3f),
                            size = Size(size.width, size.height / 1.5f),
                            cornerRadius = CornerRadius(4f, 4f)
                        )
                    }) {

                Text(
                    "...",
                    color = Colors.noteText,
                    modifier = Modifier.padding(end = 8.dp),
                )
            }
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

    val buttonSize = sizes.bottomBarSize
    AnimatedContent(noteSelected, modifier = modifier.background(Colors.bars)) {
        if (it) {
            AnimatedContent(isShowConfirmDelete && noteSelected) { showConfirm ->
                if (showConfirm) {
                    Row(
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically,
                        modifier = modifier.fillMaxSize()
                    ) {
                        CancelDeleteButton(
                            cancelRemove,
                            Modifier.height(buttonSize).weight(0.33f)
                        )
                        TrashcanImage(Modifier.height(buttonSize).weight(0.33f))
                        ConfirmDeleteButton(
                            removeNote,
                            Modifier.height(buttonSize).weight(0.33f)
                        )
                    }
                } else {
                    Row(
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically,
                        modifier = modifier.fillMaxSize()
                    ) {
                        DeleteButton(
                            showRemoveConfirm,
                            Modifier.height(buttonSize).weight(0.33f)
                        )
                        CreateButton(createNote, Modifier.height(buttonSize).weight(0.33f))
                        EditButton(editNote, Modifier.height(buttonSize).weight(0.33f))
                    }
                }
            }
        } else {
            Row(
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically,
                modifier = modifier.fillMaxSize()
            ) {
                CreateButton(createNote, Modifier.height(buttonSize).weight(0.33f))
            }
        }
    }
}

@Composable
private fun EditButton(
    editNote: () -> Unit,
    modifier: Modifier = Modifier
) {
    Image(
        painterResource(YukiIcons.editNote),
        contentDescription = "Edit Note",
        modifier = modifier
            .clip(RoundedCornerShape(10.dp))
            .clickable(onClick = editNote)
    )
}

@Composable
private fun CreateButton(
    createNote: () -> Unit,
    modifier: Modifier = Modifier
) {
    Image(
        painterResource(YukiIcons.createNote),
        contentDescription = "Create Note",
        modifier = modifier
            .clip(RoundedCornerShape(10.dp))
            .clickable(onClick = createNote)
    )
}

@Composable
private fun DeleteButton(
    showRemoveConfirm: () -> Unit,
    modifier: Modifier = Modifier
) {
    Image(
        painterResource(YukiIcons.deleteNote),
        contentDescription = "Delete Note",
        modifier = modifier
            .clip(RoundedCornerShape(10.dp))
            .clickable(onClick = showRemoveConfirm)
    )
}

@Composable
private fun ConfirmDeleteButton(
    removeNote: () -> Unit,
    modifier: Modifier = Modifier
) {
    Image(
        painterResource(YukiIcons.confirm),
        contentDescription = "Confirm Delete Note",
        modifier = modifier
            .clip(RoundedCornerShape(10.dp))
            .clickable(onClick = removeNote)
    )
}

@Composable
private fun TrashcanImage(
    modifier: Modifier = Modifier
) {
    Image(
        painterResource(YukiIcons.confirmDeleteNote),
        contentDescription = "Trashcan",
        modifier = modifier.clip(RoundedCornerShape(10.dp))
    )
}

@Composable
private fun CancelDeleteButton(
    cancelRemove: () -> Unit,
    modifier: Modifier = Modifier
) {
    Image(
        painterResource(YukiIcons.cancel),
        contentDescription = "Cancel Delete Note",
        modifier = modifier
            .clip(RoundedCornerShape(10.dp))
            .clickable(onClick = cancelRemove)
    )
}