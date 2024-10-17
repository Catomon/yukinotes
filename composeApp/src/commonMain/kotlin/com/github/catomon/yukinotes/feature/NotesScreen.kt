package com.github.catomon.yukinotes.feature

import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.animateDpAsState
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
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.material.TextButton
import androidx.compose.material.TextField
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Delete
import androidx.compose.runtime.Composable
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
import com.github.catomon.yukinotes.data.mappers.toNote
import com.github.catomon.yukinotes.domain.Note
import kotlinx.coroutines.launch
import org.jetbrains.compose.resources.painterResource
import org.jetbrains.compose.ui.tooling.preview.Preview
import yukinotes.composeapp.generated.resources.Res
import yukinotes.composeapp.generated.resources.yuki

private fun createYukiColors(
    primary: Color = Colors.pink,
    primaryVariant: Color = Color(0xFF3700B3),
    secondary: Color = Color(0xFF03DAC6),
    secondaryVariant: Color = Color(0xFF018786),
    background: Color = Color.White,
    surface: Color = Color.White,
    error: Color = Color(0xFFB00020),
    onPrimary: Color = Color.White,
    onSecondary: Color = Color.Black,
    onBackground: Color = Color.Black,
    onSurface: Color = Color.Black,
    onError: Color = Color.White
): androidx.compose.material.Colors = androidx.compose.material.Colors(
    primary,
    primaryVariant,
    secondary,
    secondaryVariant,
    background,
    surface,
    error,
    onPrimary,
    onSecondary,
    onBackground,
    onSurface,
    onError,
    true
)

val yukiColors = createYukiColors()

@Composable
@Preview
fun YukiApp() {
    val appState = remember { AppState() }

    MaterialTheme(
        yukiColors
    ) {
        Column {
            TopBar()
            NotesScreen(appState)
        }
    }
}

//animated vis

@Composable
fun NotesScreen(appState: AppState) {
    val curScreen by appState.curScreen

    AnimatedContent(curScreen) {
        when (curScreen) {
            Screens.NOTES -> {
                NotesFrame(appState)
            }

            Screens.NEW_NOTE -> {
                NoteCreationFrame(appState)
            }
        }
    }
}

@Composable
fun NotesFrame(appState: AppState) {
    val notes = appState.database.noteDao().getAllNotes().collectAsState(emptyList())

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
                        }.fillMaxSize().background(Colors.lightBlue).padding(start = 8.dp))

                        if (note.content.isNotEmpty())
                            Text(note.content, modifier = Modifier.fillMaxWidth().padding(start = 16.dp), color = Color.Gray)
                    }
                } else {
                    Text(notes.value[it].title, modifier = Modifier.clickable {
                        selectedNoteIndex = it
                    }.fillMaxSize().padding(start = 8.dp))
                }
            }
        }

        Row(Modifier.align(Alignment.BottomEnd).padding(8.dp), horizontalArrangement = Arrangement.spacedBy(8.dp)) {
            AnimatedVisibility(selectedNoteIndex != -1) {
                Button(
                    onClick = {
                        val noteEntity = notes.value.getOrNull(selectedNoteIndex)
                        if (noteEntity != null) {
                            coroutineScope.launch {
                                appState.removeNote(noteEntity.toNote())
                                selectedNoteIndex = -1
                            }
                        }
                    },
                    shape = CircleShape,
                    colors = ButtonDefaults.buttonColors(backgroundColor = Colors.pink),
                    modifier = Modifier.size(48.dp)
                ) {
//                    Text("X", color = Color.White)
                    Icon(Icons.Default.Delete, "Remove Note", tint = Color.White)
                }
            }
            NoteCreateButton(appState)
        }
    }
}

@Composable
fun TopBar() {
    Row(
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically,
        modifier = Modifier.background(Colors.pink)
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
fun NoteCreateButton(appState: AppState) {
    Button(
        onClick = {
            appState.curScreen.value = Screens.NEW_NOTE
        },
        shape = CircleShape,
        colors = ButtonDefaults.buttonColors(backgroundColor = Colors.pink),
        modifier = Modifier.size(48.dp)
    ) {
        Icon(Icons.Default.Add, "Add Note", tint = Color.White)
       // Text("+", color = Color.White)
    }
}

@Composable
fun NoteCreationFrame(appState: AppState) {
    val coroutineScope = rememberCoroutineScope()

    var title by remember { mutableStateOf("") }
    var content by remember { mutableStateOf("") }

    Column(Modifier.fillMaxSize(), verticalArrangement = Arrangement.Center, horizontalAlignment = Alignment.CenterHorizontally) {
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

        Button({
            if (title.isNotEmpty()) {
                coroutineScope.launch {
                    appState.addNote(Note(title = title, content = content))
                    appState.curScreen.value = Screens.NOTES
                }
            }
        }) {
            Text("Save")
        }
    }
}