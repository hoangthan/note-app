package com.note.libraries.note.domain.usecase

import androidx.paging.PagingData
import com.note.libraries.note.domain.model.Note
import com.note.libraries.note.domain.repository.NoteRepository
import com.note.libraries.note.domain.usecase.GetNoteUseCase.GetNoteFilter
import com.note.libraries.note.domain.usecase.GetNoteUseCase.SortOption
import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.flowOf
import org.junit.Before
import org.junit.Test

@ExperimentalCoroutinesApi
class GetNoteUseCaseImplTest {

    private lateinit var repository: NoteRepository
    private lateinit var useCase: GetNoteUseCaseImpl

    @Before
    fun setup() {
        repository = mockk()
        useCase = GetNoteUseCaseImpl(repository)
    }

    @Test
    fun `invoke should call repository getFilteredNotes with correct parameters`() {
        // Given
        val filter = GetNoteFilter(SortOption.LatestFirst, "test")
        val pagingData = PagingData.from(listOf<Note>())
        every {
            repository.getFilteredNotes(
                filter.keyword,
                filter.sort
            )
        } returns flowOf(pagingData)

        // When
        val result = useCase(filter)

        // Then
        verify { repository.getFilteredNotes(filter.keyword, filter.sort) }
    }
}
