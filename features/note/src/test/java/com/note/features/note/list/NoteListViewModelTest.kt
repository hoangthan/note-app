package com.note.features.note.list

import app.cash.turbine.test
import com.note.data.core.coroutine.DispatcherProvider
import com.note.features.note.list.model.NoteUi
import com.note.libraries.note.domain.model.Note
import com.note.libraries.note.domain.usecase.CreateNoteUseCase
import com.note.libraries.note.domain.usecase.CreateNoteUseCase.CreateNoteParam
import com.note.libraries.note.domain.usecase.DeleteNoteUseCase
import com.note.libraries.note.domain.usecase.GetNoteUseCase
import com.note.libraries.note.domain.usecase.GetNoteUseCase.SortOption
import com.note.libraries.note.domain.usecase.UpdateNoteUseCase
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.every
import io.mockk.mockk
import io.mockk.slot
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Test

@ExperimentalCoroutinesApi
class NoteListViewModelTest {

    private val testDispatcher = StandardTestDispatcher()
    private lateinit var getNoteUseCase: GetNoteUseCase
    private lateinit var createNoteUseCase: CreateNoteUseCase
    private lateinit var deleteNoteUseCase: DeleteNoteUseCase
    private lateinit var updateNoteUseCase: UpdateNoteUseCase
    private lateinit var dispatcherProvider: DispatcherProvider
    private lateinit var viewModel: NoteListViewModel

    @Before
    fun setup() {
        Dispatchers.setMain(testDispatcher)
        getNoteUseCase = mockk(relaxed = true)
        createNoteUseCase = mockk(relaxed = true)
        deleteNoteUseCase = mockk(relaxed = true)
        updateNoteUseCase = mockk(relaxed = true)
        dispatcherProvider = mockk {
            every { io } returns testDispatcher
        }
        viewModel = NoteListViewModel(
            getNoteUseCase,
            createNoteUseCase,
            deleteNoteUseCase,
            updateNoteUseCase,
            dispatcherProvider
        )
    }

    @After
    fun tearDown() {
        Dispatchers.resetMain()
    }

    @Test
    fun `when search query changes, filter is updated with correct query`() = runTest {
        // Given
        val query = "test query"

        // When
        viewModel.onEvent(NoteListEvent.SetSearchQuery(query))
        testDispatcher.scheduler.advanceUntilIdle()

        // Then - verify the state is updated correctly
        viewModel.state.test {
            val state = awaitItem()
            assertEquals(query, state.searchQuery)
        }
    }

    @Test
    fun `when sort option changes, filter is updated with correct sort option`() = runTest {
        // Given - default sort option is LatestFirst
        val expectedSortOption = SortOption.OldestFirst

        // When
        viewModel.onEvent(NoteListEvent.ToggleSortDirection)
        testDispatcher.scheduler.advanceUntilIdle()

        // Then - verify the state is updated correctly
        viewModel.state.test {
            val state = awaitItem()
            assertEquals(expectedSortOption, state.sortOption)
        }
    }

    @Test
    fun `when create note is called, createNoteUseCase is invoked with correct parameters`() =
        runTest {
            // Given
            val title = "Test Title"
            val content = "Test Content"
            val expectedParam = CreateNoteParam(title, content)
            coEvery { createNoteUseCase(expectedParam) } returns Unit

            // When
            viewModel.onEvent(NoteListEvent.CreateNote(title, content))
            testDispatcher.scheduler.advanceUntilIdle()

            // Then
            coVerify { createNoteUseCase(expectedParam) }
        }

    @Test
    fun `when delete note is called, deleteNoteUseCase is invoked with correct parameters`() =
        runTest {
            // Given
            val noteUi = NoteUi(
                id = 1,
                title = "Test Title",
                content = "Test Content",
                isCompleted = false,
                timestamp = 123456789L,
                createdAt = "01/01/2025"
            )
            val noteSlot = slot<Note>()
            coEvery { deleteNoteUseCase(capture(noteSlot)) } returns Unit

            // When
            viewModel.onEvent(NoteListEvent.DeleteNote(noteUi))
            testDispatcher.scheduler.advanceUntilIdle()

            // Then
            coVerify { deleteNoteUseCase(capture(noteSlot)) }
            assertEquals(noteUi.id, noteSlot.captured.id)
            assertEquals(noteUi.title, noteSlot.captured.title)
            assertEquals(noteUi.content, noteSlot.captured.content)
        }

    @Test
    fun `when toggle note completion is called, updateNoteUseCase is invoked with toggled completion`() =
        runTest {
            // Given
            val noteUi = NoteUi(
                id = 1,
                title = "Test Title",
                content = "Test Content",
                isCompleted = false,
                timestamp = 123456789L,
                createdAt = "01/01/2025"
            )
            val noteSlot = slot<Note>()
            coEvery { updateNoteUseCase(capture(noteSlot)) } returns Unit

            // When
            viewModel.onEvent(NoteListEvent.ToggleNoteCompletion(noteUi))
            testDispatcher.scheduler.advanceUntilIdle()

            // Then
            coVerify { updateNoteUseCase(capture(noteSlot)) }
            assertEquals(noteUi.id, noteSlot.captured.id)
            assertEquals(noteUi.title, noteSlot.captured.title)
            assertEquals(noteUi.content, noteSlot.captured.content)
            assertEquals(!noteUi.isCompleted, noteSlot.captured.isCompleted)
        }
}
