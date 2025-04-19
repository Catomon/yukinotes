package com.github.catomon.yukinotes

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import com.github.catomon.yukinotes.di.appModule
import com.github.catomon.yukinotes.ui.YukiApp
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
