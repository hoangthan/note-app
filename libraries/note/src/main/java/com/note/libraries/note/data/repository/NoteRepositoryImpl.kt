package com.note.libraries.note.data.repository

import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import androidx.paging.map
import com.note.libraries.note.data.datasource.local.NoteDao
import com.note.libraries.note.data.datasource.mapper.toNoteEntity
import com.note.libraries.note.data.datasource.mapper.toNoteModel
import com.note.libraries.note.domain.model.Note
import com.note.libraries.note.domain.repository.NoteRepository
import com.note.libraries.note.domain.usecase.GetNoteUseCase.SortOption
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject

internal class NoteRepositoryImpl @Inject constructor(
    private val noteDao: NoteDao
) : NoteRepository {
    override suspend fun createNote(note: Note) {
        noteDao.insertNote(note.toNoteEntity())
    }

    override suspend fun deleteNote(note: Note) {
        noteDao.deleteNote(note.toNoteEntity())
    }

    override suspend fun updateNote(note: Note) {
        noteDao.updateNote(note.toNoteEntity())
    }

    override fun getFilteredNotes(
        query: String,
        sortOption: SortOption
    ): Flow<PagingData<Note>> {
        val isAsc = sortOption == SortOption.OldestFirst
        val pager = Pager(
            config = PagingConfig(pageSize = 20),
            pagingSourceFactory = { noteDao.getNoteWithFilter(query, isAsc) }
        )

        return pager.flow.map { pagingData -> pagingData.map { it.toNoteModel() } }
    }
}
