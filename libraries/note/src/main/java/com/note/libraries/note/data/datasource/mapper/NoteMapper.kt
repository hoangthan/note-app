package com.note.libraries.note.data.datasource.mapper

import com.note.libraries.note.data.datasource.local.entity.NoteEntity
import com.note.libraries.note.domain.model.Note

internal fun NoteEntity.toNoteModel(): Note {
    return Note(
        id = id,
        title = title,
        content = content,
        isCompleted = isCompleted,
        timestamp = timestamp
    )
}

internal fun Note.toNoteEntity(): NoteEntity {
    return NoteEntity(
        id = id,
        title = title,
        content = content,
        isCompleted = isCompleted,
        timestamp = timestamp
    )
}
