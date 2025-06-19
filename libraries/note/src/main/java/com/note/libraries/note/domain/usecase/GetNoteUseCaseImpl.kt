package com.note.libraries.note.domain.usecase

import androidx.paging.PagingData
import com.note.libraries.note.domain.model.Note
import com.note.libraries.note.domain.repository.NoteRepository
import com.note.libraries.note.domain.usecase.GetNoteUseCase.GetNoteFilter
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

interface GetNoteUseCase {
    data class GetNoteFilter(
        val sort: SortOption,
        val keyword: String,
    )

    sealed interface SortOption {
        object LatestFirst : SortOption
        object OldestFirst : SortOption
    }

    operator fun invoke(filter: GetNoteFilter): Flow<PagingData<Note>>
}

internal class GetNoteUseCaseImpl @Inject constructor(
    private val noteRepository: NoteRepository
) : GetNoteUseCase {

    override operator fun invoke(filter: GetNoteFilter): Flow<PagingData<Note>> {
        return noteRepository.getFilteredNotes(
            query = filter.keyword,
            sortOption = filter.sort,
        )
    }
}
