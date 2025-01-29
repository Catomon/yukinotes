package com.github.catomon.yukinotes.di

import com.github.catomon.yukinotes.createDatabase
import com.github.catomon.yukinotes.data.repository.YukiRepository
import com.github.catomon.yukinotes.data.repository.YukiRepositoryImpl
import com.github.catomon.yukinotes.data.repository.YukiTxtRepositoryImpl
import com.github.catomon.yukinotes.feature.YukiViewModel
import com.github.catomon.yukinotes.loadSettings
import org.koin.dsl.module

//FIXME
val storeNotesAsTxtFiles by lazy { loadSettings().storeAsTxtFiles }

val appModule = module {
    single<YukiRepository> { if (storeNotesAsTxtFiles) YukiTxtRepositoryImpl() else YukiRepositoryImpl(createDatabase().noteDao()) }
    factory { YukiViewModel(get()) }
}