package com.github.catomon.yukinotes.ui

import androidx.compose.animation.AnimatedContentTransitionScope
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.text.selection.LocalTextSelectionColors
import androidx.compose.foundation.text.selection.TextSelectionColors
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.key
import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import org.jetbrains.compose.ui.tooling.preview.Preview
import org.koin.java.KoinJavaComponent

@Composable
@Preview
fun YukiAppDesktopScreen(modifier: Modifier = Modifier.Companion) {
    val yukiViewModel: YukiViewModel = remember { KoinJavaComponent.get(YukiViewModel::class.java) }
    val navController: NavHostController = rememberNavController()
    val settings by yukiViewModel.userSettings
    var topBarColor by remember { mutableStateOf(Colors.bars) }
    LaunchedEffect(settings.theme) {
        Colors.currentYukiTheme = Themes.forNameOrFirst(settings.theme)
        Colors.updateTheme()
        topBarColor = Colors.bars
    }

    Column(modifier) {
        TopBar(
            openSettings = {
                val currentRoute = navController.currentBackStackEntry?.destination?.route
                if (currentRoute == Routes.SETTINGS) {
                    navController.popBackStack()
                } else {
                    navController.navigate(Routes.SETTINGS)
                }
            },
            Modifier.background(topBarColor)
        )

        NavHost(
            navController,
            startDestination = Routes.NOTES,
            modifier = Modifier.Companion.fillMaxSize().background(color = Colors.bars),
            enterTransition = { slideIntoContainer(AnimatedContentTransitionScope.SlideDirection.Left) },
            exitTransition = { slideOutOfContainer(AnimatedContentTransitionScope.SlideDirection.Right) },
        ) {
            composable(Routes.NOTES) {
                Row(Modifier.Companion.fillMaxSize()) {
                    NotesScreen(yukiViewModel, navController, Modifier.Companion.width(200.dp))

                    val notesScreenState by yukiViewModel.notesScreenState.collectAsState()
                    val noteId by remember(notesScreenState) { mutableStateOf(notesScreenState.selectedNoteId?.toString()) }

                    val customSelectionColors = TextSelectionColors(
                        handleColor = Color.Companion.Gray,
                        backgroundColor = Color.Companion.Black
                    )

                    key(noteId) {
                        CompositionLocalProvider(LocalTextSelectionColors provides customSelectionColors) {
                            NoteEditScreen(
                                yukiViewModel,
                                noteId,
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

            composable(Routes.SETTINGS) {
                SettingsScreen(yukiViewModel, navBack = {
                    navController.popBackStack(
                        Routes.NOTES,
                        inclusive = false
                    )
                })
            }
        }
    }
}