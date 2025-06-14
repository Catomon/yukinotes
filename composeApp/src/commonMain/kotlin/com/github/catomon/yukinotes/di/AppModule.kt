package com.github.catomon.yukinotes.di

import com.github.catomon.yukinotes.createDatabase
import com.github.catomon.yukinotes.domain.YukiRepository
import com.github.catomon.yukinotes.data.repository.YukiRepositoryImpl
import com.github.catomon.yukinotes.data.repository.YukiTxtRepositoryImpl
import com.github.catomon.yukinotes.ui.YukiViewModel
import com.github.catomon.yukinotes.storeNotesAsTxtFiles
import org.koin.dsl.module

val appModule = module {
    single<YukiRepository> {
        if (storeNotesAsTxtFiles) YukiTxtRepositoryImpl() else YukiRepositoryImpl(
            createDatabase().noteDao()
        )
    }

    single { YukiViewModel(get()) }
}