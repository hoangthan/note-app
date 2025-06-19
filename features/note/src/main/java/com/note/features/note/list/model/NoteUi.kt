package com.note.features.note.list.model

import com.note.data.core.utils.toFormattedDate
import com.note.libraries.note.domain.model.Note


data class NoteUi(
    val id: Int,
    val title: String,
    val content: String,
    val isCompleted: Boolean,
    val timestamp: Long,
    val createdAt: String
)

fun Note.toUi(): NoteUi {
    return NoteUi(
        id = id,
        title = title,
        content = content,
        isCompleted = isCompleted,
        timestamp = timestamp,
        createdAt = timestamp.toFormattedDate()
    )
}

fun NoteUi.toNote(): Note {
    return Note(
        id = id,
        title = title,
        content = content,
        isCompleted = isCompleted,
        timestamp = timestamp,
    )
}
