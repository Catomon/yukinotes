package com.github.catomon.yukinotes

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.Preview
import com.github.catomon.yukinotes.feature.YukiApp

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContent {
            YukiApp()
        }
    }
}

@Preview
@Composable
fun AppAndroidPreview() {
    YukiApp()
}