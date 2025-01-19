package com.github.catomon.yukinotes.feature

import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.github.catomon.yukinotes.Const
import org.jetbrains.compose.resources.DrawableResource
import org.jetbrains.compose.resources.painterResource

expect object YukiIcons {
    val appIcon: DrawableResource
    val menu: DrawableResource
    val createNote: DrawableResource
    val deleteNote: DrawableResource
    val editNote: DrawableResource
    val confirmDeleteNote: DrawableResource
    val confirm: DrawableResource
    val cancel: DrawableResource
}

@Composable
fun TopBar(menuButtonClicked: () -> Unit, modifier: Modifier = Modifier) {
    Row(
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically,
        modifier = modifier
    ) {
        Image(painterResource(YukiIcons.appIcon), "App Icon", Modifier.size(sizes.topBarSize))

        Text(Const.APP_NAME, color = Color.White, modifier = Modifier.padding(start = 8.dp))

        Spacer(Modifier.weight(2f))

        Row {
            PlatformActionButton()

            Image(
                painterResource(YukiIcons.menu),
                "App Menu",
                Modifier.size(sizes.topBarSize).clickable(onClick = menuButtonClicked)
            )
        }
    }
}

@Composable
expect fun PlatformActionButton()