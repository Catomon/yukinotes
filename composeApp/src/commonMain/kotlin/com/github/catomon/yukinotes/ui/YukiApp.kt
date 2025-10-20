package com.github.catomon.yukinotes.ui

import androidx.compose.animation.AnimatedContentTransitionScope
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.text.selection.LocalTextSelectionColors
import androidx.compose.foundation.text.selection.TextSelectionColors
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import org.jetbrains.compose.ui.tooling.preview.Preview
import org.koin.java.KoinJavaComponent.get

@Composable
@Preview
fun YukiApp(modifier: Modifier = Modifier) {
    val yukiViewModel: YukiViewModel = remember { get(YukiViewModel::class.java) }
    val navController: NavHostController = rememberNavController()
    val settings by yukiViewModel.userSettings
    var topBarColor by remember { mutableStateOf(YukiTheme.bars) }
    LaunchedEffect(settings.theme) {
        YukiTheme.colors = Themes.forNameOrFirst(settings.theme)
        YukiTheme.updateTheme()
        topBarColor = YukiTheme.bars
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
            modifier = Modifier.fillMaxSize().background(color = YukiTheme.bars),
            enterTransition = { slideIntoContainer(AnimatedContentTransitionScope.SlideDirection.Left) },
            exitTransition = { slideOutOfContainer(AnimatedContentTransitionScope.SlideDirection.Right) },
        ) {
            composable(Routes.NOTES) {
                NotesScreen(yukiViewModel, navController)
            }

            composable(Routes.EDIT_NOTE) { backStackEntry ->
                val noteId = backStackEntry.arguments?.getString(RouteArgs.NOTE_ID)
                    ?: throw IllegalStateException("${RouteArgs.NOTE_ID} argument is missing")

                val customSelectionColors = TextSelectionColors(
                    handleColor = Color.White,
                    backgroundColor = Color.Black
                )

                CompositionLocalProvider(LocalTextSelectionColors provides customSelectionColors) {
                    NoteEditScreen(
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

