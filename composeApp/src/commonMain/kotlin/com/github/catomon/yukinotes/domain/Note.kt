package com.github.catomon.yukinotes.domain

import kotlin.uuid.ExperimentalUuidApi
import kotlin.uuid.Uuid

data class Note @OptIn(ExperimentalUuidApi::class) constructor(
    val id: Uuid = Uuid.random(),
    val title: String = "",
    val content: String = "",
    val createdAt: Long = 0,
    val updatedAt: Long = 0,
    val isPinned: Boolean = false
)