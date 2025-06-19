package com.note.features.note.list.model

import com.note.data.core.utils.toFormattedDate
import com.note.libraries.note.domain.model.Note
import org.junit.Assert.assertEquals
import org.junit.Test

class NoteUiMapperTest {

    @Test
    fun `toUi maps Note to NoteUi correctly`() {
        // Given
        val note = Note(
            id = 1,
            title = "Test Title",
            content = "Test Content",
            isCompleted = true,
            timestamp = 1234567890L
        )

        // When
        val noteUi = note.toUi()

        // Then
        assertEquals(note.id, noteUi.id)
        assertEquals(note.title, noteUi.title)
        assertEquals(note.content, noteUi.content)
        assertEquals(note.isCompleted, noteUi.isCompleted)
        assertEquals(note.timestamp, noteUi.timestamp)
        assertEquals(note.timestamp.toFormattedDate(), noteUi.createdAt)
    }

    @Test
    fun `toNote maps NoteUi to Note correctly`() {
        // Given
        val noteUi = NoteUi(
            id = 1,
            title = "Test Title",
            content = "Test Content",
            isCompleted = true,
            timestamp = 1234567890L,
            createdAt = "Some formatted date"
        )

        // When
        val note = noteUi.toNote()

        // Then
        assertEquals(noteUi.id, note.id)
        assertEquals(noteUi.title, note.title)
        assertEquals(noteUi.content, note.content)
        assertEquals(noteUi.isCompleted, note.isCompleted)
        assertEquals(noteUi.timestamp, note.timestamp)
    }
}
