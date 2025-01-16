package com.github.catomon.yukinotes.feature

import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.RadioButton
import androidx.compose.material.RadioButtonDefaults
import androidx.compose.material.Switch
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import org.jetbrains.compose.resources.painterResource
import org.koin.java.KoinJavaComponent.get
import yukinotes.composeapp.generated.resources.Res
import yukinotes.composeapp.generated.resources.exit
import kotlin.system.exitProcess

@Composable
fun SettingsScreen(
    yukiViewModel: YukiViewModel = get(YukiViewModel::class.java),
    navBack: () -> Unit
) {
    val state by yukiViewModel.notesScreenState.collectAsState()
    val settings by remember { yukiViewModel.userSettings }
    val theme = settings.theme

    Box {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center,
            modifier = Modifier.align(Alignment.Center).fillMaxSize()
        ) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Text("Theme", color = Color.White)
                RadioButton(
                    theme == Themes.DARK,
                    colors = RadioButtonDefaults.colors(Color.Black, Color.Black),
                    onClick = {
                        yukiViewModel.updateUserSettings(settings.copy(theme = Themes.DARK))
                    })
                RadioButton(
                    theme == Themes.BRIGHT,
                    colors = RadioButtonDefaults.colors(Color.White, Color.White),
                    onClick = {
                        yukiViewModel.updateUserSettings(settings.copy(theme = Themes.BRIGHT))
                    })
            }

            SwitchSetting("Always show details", state.alwaysShowDetails, onCheckedChange = {
                yukiViewModel.alwaysShowDetails(it)
            })
        }

        Text(
            "ver 1.2",
            color = Color.White,
            modifier = Modifier.padding(start = 8.dp).align(Alignment.BottomStart)
        )

        Image(
            painterResource(Res.drawable.exit),
            "Exit App",
            Modifier.padding(8.dp).size(32.dp).clickable(onClick = { exitProcess(0) })
                .align(Alignment.BottomEnd)

        )
    }
}

@Composable
fun SwitchSetting(text: String, switchState: Boolean, onCheckedChange: (Boolean) -> Unit) {
    Row(verticalAlignment = Alignment.CenterVertically) {
        Text(text, color = Color.White)
        Switch(switchState, onCheckedChange = onCheckedChange)
    }
}