package com.github.catomon.yukinotes.feature

import androidx.compose.animation.AnimatedContentTransitionScope
import androidx.compose.animation.AnimatedVisibility
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
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.github.catomon.yukinotes.createDatabase
import com.github.catomon.yukinotes.data.mappers.toNote
import com.github.catomon.yukinotes.data.repository.YukiRepositoryImpl
import com.github.catomon.yukinotes.domain.Note
import kotlinx.coroutines.launch
import org.jetbrains.compose.resources.painterResource
import org.jetbrains.compose.ui.tooling.preview.Preview
import yukinotes.composeapp.generated.resources.Res
import yukinotes.composeapp.generated.resources.yuki
import java.time.ZoneId
import java.time.ZonedDateTime
import kotlin.uuid.Uuid

@Composable
@Preview
fun YukiApp() {
    val appState = remember { AppState(YukiRepositoryImpl(createDatabase().noteDao())) }
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
                    NotesScreen(appState, navController)
                }

                composable(Routes.EDIT_NOTE) { backStackEntry ->
                    val noteId = backStackEntry.arguments?.getString(RouteArgs.NOTE_ID)
                        ?: throw IllegalStateException("${RouteArgs.NOTE_ID} argument is missing")
                    NoteCreationScreen(
                        appState,
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
fun NotesScreen(appState: AppState, navController: NavHostController) {
    val notes = appState.repository.getAll().collectAsState(emptyList())
    var selectedNoteIndex by remember { mutableIntStateOf(-1) }
    val coroutineScope = rememberCoroutineScope()

    Box(Modifier.background(color = Color.White).fillMaxSize().clickable(
        interactionSource = remember { MutableInteractionSource() },
        indication = null
    ) {
        selectedNoteIndex = -1
    }) {
        LazyColumn(modifier = Modifier.align(Alignment.TopStart)) {
            items(notes.value.size) {
                if (selectedNoteIndex == it) {
                    val note = notes.value[it]

                    Column(Modifier.fillMaxWidth()) {
                        Text(note.title, modifier = Modifier.clickable {
                            selectedNoteIndex = if (selectedNoteIndex != it) it else -1
                        }.fillMaxSize().background(Colors.yukiEyes).padding(start = 8.dp))

                        if (note.content.isNotEmpty())
                            Text(
                                note.content,
                                modifier = Modifier.fillMaxWidth().padding(start = 16.dp),
                                color = Color.Gray
                            )
                    }
                } else {
                    Text(notes.value[it].title, modifier = Modifier.clickable {
                        selectedNoteIndex = it
                    }.fillMaxSize().padding(start = 8.dp))
                }
            }
        }

        Row(
            Modifier.align(Alignment.BottomEnd).padding(8.dp),
            horizontalArrangement = Arrangement.spacedBy(8.dp),
            verticalAlignment = Alignment.Bottom
        ) {
            val note = notes.value.getOrNull(selectedNoteIndex)?.toNote()
            AnimatedVisibility(note != null) {
                RemoveNoteButton(appState) {
                    if (note != null) {
                        coroutineScope.launch {
                            appState.removeNote(note)
                            selectedNoteIndex = -1
                        }
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
fun RemoveNoteButton(appState: AppState, onClick: () -> Unit) {
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
fun NoteCreationScreen(appState: AppState, noteId: String? = null, navBack: () -> Unit) {
    val coroutineScope = rememberCoroutineScope()

    var note by remember { mutableStateOf<Note?>(null) }

    LaunchedEffect(null) {
        if (noteId != null) {
            appState.repository.getById(Uuid.parse(noteId))?.toNote()?.let {
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
                    coroutineScope.launch {
                        val curTime =
                            ZonedDateTime.now(ZoneId.systemDefault()).toInstant().toEpochMilli()
                        appState.addNote(
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
                }
            }) {
                Text("Save")
            }
        }
    }
}