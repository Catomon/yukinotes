package com.github.catomon.yukinotes.feature

import androidx.compose.animation.AnimatedContentTransitionScope
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import org.jetbrains.compose.ui.tooling.preview.Preview
import org.koin.java.KoinJavaComponent.get

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
