package com.note.libraries.note.data.repository

import com.note.libraries.note.data.datasource.local.NoteDao
import com.note.libraries.note.data.datasource.local.entity.NoteEntity
import com.note.libraries.note.domain.model.Note
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.mockk
import io.mockk.slot
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Test

@ExperimentalCoroutinesApi
class NoteRepositoryImplTest {

    private lateinit var noteDao: NoteDao
    private lateinit var repository: NoteRepositoryImpl

    @Before
    fun setup() {
        noteDao = mockk(relaxed = true)
        repository = NoteRepositoryImpl(noteDao)
    }

    @Test
    fun `createNote should call dao insertNote with correct entity`() = runTest {
        // Given
        val note = Note(id = 1, title = "Test Note", content = "Test Content")
        val noteEntitySlot = slot<NoteEntity>()
        coEvery { noteDao.insertNote(capture(noteEntitySlot)) } returns Unit

        // When
        repository.createNote(note)

        // Then
        coVerify { noteDao.insertNote(capture(noteEntitySlot)) }
        assertEquals(note.id, noteEntitySlot.captured.id)
        assertEquals(note.title, noteEntitySlot.captured.title)
        assertEquals(note.content, noteEntitySlot.captured.content)
    }

    @Test
    fun `deleteNote should call dao deleteNote with correct entity`() = runTest {
        // Given
        val note = Note(id = 1, title = "Test Note", content = "Test Content")
        val noteEntitySlot = slot<NoteEntity>()
        coEvery { noteDao.deleteNote(capture(noteEntitySlot)) } returns Unit

        // When
        repository.deleteNote(note)

        // Then
        coVerify { noteDao.deleteNote(capture(noteEntitySlot)) }
        assertEquals(note.id, noteEntitySlot.captured.id)
        assertEquals(note.title, noteEntitySlot.captured.title)
        assertEquals(note.content, noteEntitySlot.captured.content)
    }

    @Test
    fun `updateNote should call dao updateNote with correct entity`() = runTest {
        // Given
        val note = Note(id = 1, title = "Test Note", content = "Test Content")
        val noteEntitySlot = slot<NoteEntity>()
        coEvery { noteDao.updateNote(capture(noteEntitySlot)) } returns Unit

        // When
        repository.updateNote(note)

        // Then
        coVerify { noteDao.updateNote(capture(noteEntitySlot)) }
        assertEquals(note.id, noteEntitySlot.captured.id)
        assertEquals(note.title, noteEntitySlot.captured.title)
        assertEquals(note.content, noteEntitySlot.captured.content)
    }
}
