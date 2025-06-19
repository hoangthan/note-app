package com.note.libraries.note.data.datasource.local.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "note")
internal data class NoteEntity(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val title: String,
    val content: String,
    val isCompleted: Boolean = false,
    val timestamp: Long = System.currentTimeMillis()
)
