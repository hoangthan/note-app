package com.note.libraries.note.domain.usecase

import com.note.libraries.note.domain.model.Note
import com.note.libraries.note.domain.repository.NoteRepository
import javax.inject.Inject

interface UpdateNoteUseCase {
    suspend operator fun invoke(note: Note)
}

internal class UpdateNoteUseCaseImpl @Inject constructor(
    private val noteRepository: NoteRepository,
) : UpdateNoteUseCase {
    override suspend operator fun invoke(note: Note) = noteRepository.updateNote(note)
}
