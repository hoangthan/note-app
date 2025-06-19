package com.note.libraries.note.data.datasource.mapper

import com.note.libraries.note.data.datasource.local.entity.NoteEntity
import com.note.libraries.note.domain.model.Note
import org.junit.Assert.assertEquals
import org.junit.Test

class NoteMapperTest {

    @Test
    fun `toNoteModel maps NoteEntity to Note correctly`() {
        // Given
        val noteEntity = NoteEntity(
            id = 1,
            title = "Test Title",
            content = "Test Content",
            isCompleted = true,
            timestamp = 1234567890L
        )

        // When
        val note = noteEntity.toNoteModel()

        // Then
        assertEquals(noteEntity.id, note.id)
        assertEquals(noteEntity.title, note.title)
        assertEquals(noteEntity.content, note.content)
        assertEquals(noteEntity.isCompleted, note.isCompleted)
        assertEquals(noteEntity.timestamp, note.timestamp)
    }

    @Test
    fun `toNoteEntity maps Note to NoteEntity correctly`() {
        // Given
        val note = Note(
            id = 1,
            title = "Test Title",
            content = "Test Content",
            isCompleted = true,
            timestamp = 1234567890L
        )

        // When
        val noteEntity = note.toNoteEntity()

        // Then
        assertEquals(note.id, noteEntity.id)
        assertEquals(note.title, noteEntity.title)
        assertEquals(note.content, noteEntity.content)
        assertEquals(note.isCompleted, noteEntity.isCompleted)
        assertEquals(note.timestamp, noteEntity.timestamp)
    }
}
