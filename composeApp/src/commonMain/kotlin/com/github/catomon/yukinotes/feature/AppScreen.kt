package com.github.catomon.yukinotes.feature

import androidx.compose.animation.AnimatedContentTransitionScope
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.animateContentSize
import androidx.compose.foundation.Image
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
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Button
import androidx.compose.material.ButtonDefaults
import androidx.compose.material.Icon
import androidx.compose.material.Text
import androidx.compose.material.TextButton
import androidx.compose.material.TextField
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Edit
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
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.github.catomon.yukinotes.data.mappers.toNote
import com.github.catomon.yukinotes.domain.Note
import org.jetbrains.compose.resources.painterResource
import org.jetbrains.compose.ui.tooling.preview.Preview
import org.koin.java.KoinJavaComponent.get
import yukinotes.composeapp.generated.resources.Res
import yukinotes.composeapp.generated.resources.yuki
import java.time.ZoneId
import java.time.ZonedDateTime
import kotlin.uuid.Uuid

@Composable
@Preview
fun YukiApp() {
    val yukiViewModel: YukiViewModel = get(YukiViewModel::class.java)
    val navController: NavHostController = rememberNavController()

    YukiTheme {
        Column {
            TopBar()

            NavHost(
                navController,
                startDestination = Routes.NOTES,
                modifier = Modifier.fillMaxSize(),
                enterTransition = { slideIntoContainer(AnimatedContentTransitionScope.SlideDirection.Left) },
                exitTransition = { slideOutOfContainer(AnimatedContentTransitionScope.SlideDirection.Right) }
            ) {
                composable(Routes.NOTES) {
                    NotesScreen(yukiViewModel, navController)
                }

                composable(Routes.EDIT_NOTE) { backStackEntry ->
                    val noteId = backStackEntry.arguments?.getString(RouteArgs.NOTE_ID)
                        ?: throw IllegalStateException("${RouteArgs.NOTE_ID} argument is missing")
                    NoteCreationScreen(
                        yukiViewModel,
                        if (noteId == "null") null else noteId,
                        navBack = {
                            navController.popBackStack(
                                Routes.NOTES,
                                inclusive = false
                            )
                        })
                }
            }
        }
    }
}

@Composable
fun NotesScreen(yukiViewModel: YukiViewModel, navController: NavHostController) {
    val notes = yukiViewModel.getAllNotes().collectAsState(emptyList())
    var selectedNoteId by remember { mutableStateOf<Uuid?>(null) }

    Box(Modifier.background(color = Colors.yukiEyes).fillMaxSize().clickable(
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
                        .background(color = Color.White, shape = RoundedCornerShape(4.dp))
                        .padding(start = 8.dp)
                        .animateContentSize()
                ) {
                    Text(note.title, modifier = Modifier.fillMaxSize())

                    if (selectedNoteId == note.id && note.content.isNotEmpty() && note.content.isNotBlank())
                        Text(
                            note.content,
                            modifier = Modifier.fillMaxWidth().padding(start = 16.dp),
                            color = Color.DarkGray
                        )
                }
            }
        }

        Row(
            Modifier.align(Alignment.BottomEnd).padding(8.dp),
            horizontalArrangement = Arrangement.spacedBy(8.dp),
            verticalAlignment = Alignment.Bottom
        ) {
            val note = notes.value.find { it.id == selectedNoteId }?.toNote()
            AnimatedVisibility(note != null) {
                RemoveNoteButton {
                    if (note != null) {
                        yukiViewModel.removeNote(note)
                    }
                }
            }
            Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                AnimatedVisibility(note != null) {
                    EditNoteButton {
                        if (note != null) {
                            navController.navigate(
                                Routes.createRoute(Routes.EDIT_NOTE, note.id.toString())
                            )
                        }
                    }
                }
                NoteCreateButton {
                    navController.navigate(
                        Routes.createRoute(Routes.EDIT_NOTE, RouteArgs.NULL)
                    )
                }
            }
        }
    }
}

@Composable
fun EditNoteButton(onClick: () -> Unit) {
    Button(
        onClick = onClick,
        shape = CircleShape,
        colors = ButtonDefaults.buttonColors(backgroundColor = Colors.yukiHair),
        modifier = Modifier.size(48.dp)
    ) {
        Icon(Icons.Default.Edit, "Edit Note", tint = Color.White)
    }
}

@Composable
fun RemoveNoteButton(onClick: () -> Unit) {
    Button(
        onClick = onClick,
        shape = CircleShape,
        colors = ButtonDefaults.buttonColors(backgroundColor = Colors.yukiHair),
        modifier = Modifier.size(48.dp)
    ) {
        Icon(Icons.Default.Delete, "Remove Note", tint = Color.White)
    }
}

@Composable
fun TopBar() {
    Row(
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically,
        modifier = Modifier.background(Colors.yukiHair)
    ) {
        Image(painterResource(Res.drawable.yuki), "App Icon", Modifier.size(32.dp))

        Text("YukiNotes 0.1", color = Color.White, modifier = Modifier.padding(start = 8.dp))

        Spacer(Modifier.weight(2f))

        TextButton(onClick = {

        }, Modifier.size(32.dp)) {
            Text("|||", color = Color.White)
        }
    }
}

@Composable
fun NoteCreateButton(onClick: () -> Unit) {
    Button(
        onClick = onClick,
        shape = CircleShape,
        colors = ButtonDefaults.buttonColors(backgroundColor = Colors.yukiHair),
        modifier = Modifier.size(48.dp)
    ) {
        Icon(Icons.Default.Add, "Add Note", tint = Color.White)
    }
}

@Composable
fun NoteCreationScreen(yukiViewModel: YukiViewModel, noteId: String? = null, navBack: () -> Unit) {
    var note by remember { mutableStateOf<Note?>(null) }

    LaunchedEffect(null) {
        if (noteId != null) {
            yukiViewModel.getNoteById(Uuid.parse(noteId))?.toNote()?.let {
                note = it
            }
        }
    }

    var title by remember { mutableStateOf("") }
    var content by remember { mutableStateOf("") }

    LaunchedEffect(note) {
        title = note?.title ?: ""
        content = note?.content ?: ""
    }

    Column(
        Modifier.fillMaxSize(),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        TextField(
            title,
            {
                title = it
            },
            label = { Text("Title") }
        )

        TextField(
            content,
            {
                content = it
            },
            label = { Text("Details") }
        )

        Row {
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
                            id = note?.id ?: Uuid.random(),
                            title = title,
                            content = content,
                            createdAt = note?.createdAt ?: curTime,
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