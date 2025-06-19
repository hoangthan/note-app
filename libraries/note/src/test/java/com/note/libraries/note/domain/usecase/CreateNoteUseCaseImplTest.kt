package com.note.libraries.note.domain.usecase

import com.note.libraries.note.domain.model.Note
import com.note.libraries.note.domain.repository.NoteRepository
import com.note.libraries.note.domain.usecase.CreateNoteUseCase.CreateNoteParam
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
class CreateNoteUseCaseImplTest {

    private lateinit var repository: NoteRepository
    private lateinit var useCase: CreateNoteUseCaseImpl

    @Before
    fun setup() {
        repository = mockk()
        useCase = CreateNoteUseCaseImpl(repository)
    }

    @Test
    fun `invoke should call repository createNote with correct note`() = runTest {
        // Given
        val title = "Test Note"
        val content = "Test Content"
        val param = CreateNoteParam(title = title, content = content)
        val noteSlot = slot<Note>()
        coEvery { repository.createNote(capture(noteSlot)) } returns Unit

        // When
        useCase(param)

        // Then
        coVerify { repository.createNote(any()) }
        val capturedNote = noteSlot.captured
        assertEquals(0, capturedNote.id) // ID should be 0 for new notes
        assertEquals(title, capturedNote.title)
        assertEquals(content, capturedNote.content)
    }
}
