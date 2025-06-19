package com.note.libraries.note.domain.model

data class Note(
    val id: Int = 0,
    val title: String,
    val content: String,
    val isCompleted: Boolean = false,
    val timestamp: Long = System.currentTimeMillis()
)
