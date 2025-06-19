package com.note.libraries.note.domain.usecase

import com.note.libraries.note.domain.model.Note
import com.note.libraries.note.domain.repository.NoteRepository
import com.note.libraries.note.domain.usecase.CreateNoteUseCase.CreateNoteParam

import javax.inject.Inject

interface CreateNoteUseCase {
    data class CreateNoteParam(
        val title: String,
        val content: String,
    )

    suspend operator fun invoke(param: CreateNoteParam)
}

internal class CreateNoteUseCaseImpl @Inject constructor(
    private val repository: NoteRepository
) : CreateNoteUseCase {
    override suspend operator fun invoke(param: CreateNoteParam) {
        val note = Note(id = 0, title = param.title, content = param.content)
        repository.createNote(note)
    }
}
