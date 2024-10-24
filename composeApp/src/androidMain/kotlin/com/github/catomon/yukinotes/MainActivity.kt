package com.github.catomon.yukinotes

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.Preview
import com.github.catomon.yukinotes.di.appModule
import com.github.catomon.yukinotes.feature.YukiApp
import org.koin.core.context.GlobalContext.startKoin

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mainActivityContext = this

        startKoin {
            modules(appModule)
        }

        setContent {
            YukiApp()
        }
    }

    override fun onDestroy() {
        super.onDestroy()

        mainActivityContext = null
    }
}

@Preview
@Composable
fun AppAndroidPreview() {
    YukiApp()
}