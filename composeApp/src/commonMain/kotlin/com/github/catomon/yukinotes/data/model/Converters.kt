package com.github.catomon.yukinotes.data.model

import androidx.room.TypeConverter
import kotlin.uuid.ExperimentalUuidApi
import kotlin.uuid.Uuid

class Converters {
    @OptIn(ExperimentalUuidApi::class)
    @TypeConverter
    fun fromUuid(uuid: Uuid): ByteArray {
        return uuid.toByteArray()
    }

    @OptIn(ExperimentalUuidApi::class)
    @TypeConverter
    fun toUuid(bytes: ByteArray): Uuid {
        return Uuid.fromByteArray(bytes)
    }
}