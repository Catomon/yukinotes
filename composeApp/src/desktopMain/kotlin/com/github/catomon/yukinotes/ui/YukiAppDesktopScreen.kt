package com.github.catomon.yukinotes.ui

import androidx.compose.animation.AnimatedContentTransitionScope
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.selection.LocalTextSelectionColors
import androidx.compose.foundation.text.selection.TextSelectionColors
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.key
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.github.catomon.yukinotes.LocalWindow
import org.jetbrains.compose.resources.painterResource
import org.jetbrains.compose.ui.tooling.preview.Preview
import org.koin.java.KoinJavaComponent
import yukinotes.composeapp.generated.resources.Res
import yukinotes.composeapp.generated.resources.background
import yukinotes.composeapp.generated.resources.minimize

@Composable
@Preview
fun YukiAppDesktopScreen(modifier: Modifier = Modifier.Companion) {
    val yukiViewModel: YukiViewModel = remember { KoinJavaComponent.get(YukiViewModel::class.java) }
    val navController: NavHostController = rememberNavController()
    val settings by yukiViewModel.userSettings
    var backgroundColor by remember { mutableStateOf(YukiTheme.background) }
    LaunchedEffect(settings) {
        YukiTheme.colors = Themes.forNameOrFirst(settings.theme)
        YukiTheme.updateTheme()
        backgroundColor = YukiTheme.background
    }

    Box(modifier) {
        Image(
            painterResource(Res.drawable.background),
            null,
            Modifier.fillMaxSize().clip(RoundedCornerShape(14.dp)),
            contentScale = ContentScale.Crop
        )

        Column(Modifier.fillMaxSize()) {
            NavHost(
                navController,
                startDestination = Routes.NOTES,
                modifier = Modifier.fillMaxSize().background(color = backgroundColor),
                enterTransition = { slideIntoContainer(AnimatedContentTransitionScope.SlideDirection.Left) },
                exitTransition = { slideOutOfContainer(AnimatedContentTransitionScope.SlideDirection.Right) },
            ) {
                composable(Routes.NOTES) {
                    Row(Modifier.fillMaxSize()) {
                        Column(
                            Modifier.width(200.dp).background(
                                color = YukiTheme.bars,
                                shape = RoundedCornerShape(topEnd = 12.dp, bottomEnd = 12.dp)
                            )
                        ) {
                            Row(Modifier.fillMaxWidth()) {
                                TopBar(
                                    openSettings = {
                                        val currentRoute = navController.currentBackStackEntry?.destination?.route
                                        if (currentRoute == Routes.SETTINGS) {
                                            navController.popBackStack()
                                        } else {
                                            navController.navigate(Routes.SETTINGS)
                                        }
                                    }
                                )
                            }

                            NotesList(yukiViewModel, navController, Modifier.fillMaxSize())
                        }


                        val notesScreenState by yukiViewModel.notesScreenState.collectAsState()
                        val noteId by remember(notesScreenState) { mutableStateOf(if (notesScreenState.selectedNotes.size == 1) notesScreenState.selectedNotes.firstOrNull()?.toString() else null) }

                        val customSelectionColors = TextSelectionColors(
                            handleColor = Color.Companion.White,
                            backgroundColor = Color.Companion.Black
                        )

                        key(noteId) {
                            CompositionLocalProvider(LocalTextSelectionColors provides customSelectionColors) {
                                NoteEditPane(
                                    yukiViewModel,
                                    noteId,
                                )
                            }
                        }
                    }
                }

                composable(Routes.SETTINGS) {
                    Column {
                        Row(
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically,
                            modifier = Modifier.height(sizes.topBarSize).fillMaxWidth()
                        ) {
                            Row(
                                verticalAlignment = Alignment.CenterVertically,
                                modifier = Modifier.clip(RoundedCornerShape(8.dp)).clickable {
                                    val currentRoute = navController.currentBackStackEntry?.destination?.route
                                    if (currentRoute == Routes.SETTINGS) {
                                        navController.popBackStack()
                                    } else {
                                        navController.navigate(Routes.SETTINGS)
                                    }
                                }) {
                                Icon(
                                    Icons.Default.ArrowBack,
                                    null,
                                    Modifier.size(sizes.topBarSize),
                                    tint = YukiTheme.colors.text
                                )

                                Text("Back")
                            }

                            Spacer(Modifier.weight(2f))

                            val window = LocalWindow.current
                            Icon(
                                painterResource(Res.drawable.minimize),
                                "App Menu",
                                Modifier.size(sizes.topBarSize).clickable(onClick = {
                                    window.isMinimized = true
                                }),
                                tint = YukiTheme.colors.text
                            )
                        }

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
}