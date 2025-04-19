package com.github.catomon.yukinotes.ui

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
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.requiredHeightIn
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.lazy.staggeredgrid.LazyVerticalStaggeredGrid
import androidx.compose.foundation.lazy.staggeredgrid.StaggeredGridCells
import androidx.compose.foundation.lazy.staggeredgrid.rememberLazyStaggeredGridState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Divider
import androidx.compose.material.Text
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
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.launch
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
        interactionSource = remember { MutableInteractionSource() }, indication = null
    ) {
        yukiViewModel.selectNote(null)
    }) {
        if (true)
            NotesStaggeredGrid(
                state,
                onNoteSelected = { noteId ->
                    yukiViewModel.selectNote(if (state.selectedNoteId != noteId) noteId else null)
                },
                modifier = Modifier.align(Alignment.TopStart)
                    .padding(horizontal = sizes.notesListPadding)
            )
        else
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
fun NotesStaggeredGrid(
    state: NotesScreenState, onNoteSelected: (Uuid) -> Unit, modifier: Modifier = Modifier
) {
    val notes by state.notes.collectAsState(emptyList())
    val selectedNoteId = state.selectedNoteId
    val gridState = rememberLazyStaggeredGridState()
    val coroutineScope = rememberCoroutineScope()

    LazyVerticalStaggeredGrid(
        state = gridState,
        columns = StaggeredGridCells.Adaptive(125.dp),
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
    val notes by state.notes.collectAsState(emptyList())
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

    Box() {
        Column(Modifier.fillMaxWidth().clickable(indication = null,
                interactionSource = remember { MutableInteractionSource() },
                onClick = {
                    onNoteSelected(note.id)
                }).padding(sizes.noteItemPadding).background(
                color = Colors.noteBackground, shape = RoundedCornerShape(4.dp)
            ).let {
                return@let if (selectedNoteId == note.id) {
                    it.border(2.dp, color = Color.White, shape = RoundedCornerShape(4.dp))
                } else it
            }.padding(start = 8.dp).animateContentSize()) {
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
                    if (selectedNoteId == note.id) {
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
        }

        Row(Modifier.align(Alignment.BottomEnd).padding(end = 8.dp, bottom = 8.dp).drawBehind {
            drawRoundRect(
                color = Colors.noteBackground,
                topLeft = Offset(0f, 0f + size.height / 3f),
                size = Size(size.width, size.height / 1.5f),
                cornerRadius = CornerRadius(4f, 4f)
            )
        }) {
            if ((showDetails && isContentOverflow) || (!state.alwaysShowDetails && note.content.isNotBlank())) {
                Text(
                    "...",
                    color = Colors.noteText,
                    modifier = Modifier.padding(end = 8.dp),
                )
            }

            if (selectedNoteId == note.id) {
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
                            "Trashcan",
                            modifier = Modifier.height(32.dp)
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