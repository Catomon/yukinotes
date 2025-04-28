package com.github.catomon.yukinotes.ui

import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.github.catomon.yukinotes.Const
import org.jetbrains.compose.resources.painterResource

@Composable
fun TopBar(openSettings: () -> Unit, modifier: Modifier = Modifier) {
    Row(
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically,
        modifier = modifier
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier.clip(RoundedCornerShape(8.dp)).clickable { openSettings() }) {
            Icon(
                painterResource(YukiIcons.appIcon),
                "App Icon",
                Modifier.size(sizes.topBarSize),
                tint = Colors.currentYukiTheme.surface
            )

            Text(
                Const.APP_NAME,
                color = Colors.currentYukiTheme.surface,
                modifier = Modifier.padding(start = 3.dp),
                fontSize = sizes.fontHeadline
            )
        }

        Spacer(Modifier.weight(2f))

        Row {
            PlatformActionButton()
        }
    }
}

@Composable
expect fun PlatformActionButton()