package com.github.catomon.yukinotes.feature

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.material.Switch
import androidx.compose.material.Text
import androidx.compose.material.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import org.koin.java.KoinJavaComponent.get

@Composable
fun SettingsScreen(yukiViewModel: YukiViewModel = get(YukiViewModel::class.java), navBack: () -> Unit) {
    val state by yukiViewModel.notesScreenState.collectAsState()

    Column(horizontalAlignment = Alignment.CenterHorizontally, verticalArrangement = Arrangement.Center) {
        Row {
            Text("Always show details")
            Switch(state.alwaysShowDetails, onCheckedChange = {
                yukiViewModel.changeSettings(it)
            })
        }

        TextButton({
            navBack()
        }) {
            Text("Close")
        }
    }
}