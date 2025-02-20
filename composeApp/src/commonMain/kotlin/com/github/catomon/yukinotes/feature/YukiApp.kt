package com.github.catomon.yukinotes.feature

import androidx.compose.animation.AnimatedContentTransitionScope
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import org.jetbrains.compose.ui.tooling.preview.Preview
import org.koin.java.KoinJavaComponent.get

@Composable
@Preview
fun YukiApp(modifier: Modifier = Modifier) {
    val yukiViewModel: YukiViewModel = get(YukiViewModel::class.java)
    val navController: NavHostController = rememberNavController()
    val settings by yukiViewModel.userSettings
    var topBarColor by remember { mutableStateOf(Colors.bars) }
    LaunchedEffect(settings.theme) {
        Colors.currentYukiTheme = Themes.forNameOrFirst(settings.theme)
        Colors.updateTheme()
        topBarColor = Colors.bars
    }

    YukiTheme {
        Column(modifier) {
            TopBar(menuButtonClicked = {
                val currentRoute = navController.currentBackStackEntry?.destination?.route
                if (currentRoute == Routes.SETTINGS) {
                    navController.popBackStack()
                } else {
                    navController.navigate(Routes.SETTINGS)
                }
            },
                Modifier.background(topBarColor))

            NavHost(
                navController,
                startDestination = Routes.NOTES,
                modifier = Modifier.fillMaxSize().background(color = Colors.background),
                enterTransition = { slideIntoContainer(AnimatedContentTransitionScope.SlideDirection.Left) },
                exitTransition = { slideOutOfContainer(AnimatedContentTransitionScope.SlideDirection.Right) },
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
}
