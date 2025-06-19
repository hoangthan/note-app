package com.note.features.note.list

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.PagingData
import androidx.paging.cachedIn
import androidx.paging.map
import com.note.data.core.coroutine.DispatcherProvider
import com.note.features.note.list.model.NoteUi
import com.note.features.note.list.model.toNote
import com.note.features.note.list.model.toUi
import com.note.libraries.note.domain.usecase.CreateNoteUseCase
import com.note.libraries.note.domain.usecase.CreateNoteUseCase.CreateNoteParam
import com.note.libraries.note.domain.usecase.DeleteNoteUseCase
import com.note.libraries.note.domain.usecase.GetNoteUseCase
import com.note.libraries.note.domain.usecase.GetNoteUseCase.GetNoteFilter
import com.note.libraries.note.domain.usecase.GetNoteUseCase.SortOption
import com.note.libraries.note.domain.usecase.UpdateNoteUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.debounce
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
@OptIn(ExperimentalCoroutinesApi::class, FlowPreview::class)
class NoteListViewModel @Inject constructor(
    private val getAllNotesUseCase: GetNoteUseCase,
    private val createNoteUseCase: CreateNoteUseCase,
    private val deleteNoteUseCase: DeleteNoteUseCase,
    private val updateNoteUseCase: UpdateNoteUseCase,
    private val dispatcherProvider: DispatcherProvider
) : ViewModel() {

    private val _state = MutableStateFlow(NoteListState())
    val state: StateFlow<NoteListState> = _state.asStateFlow()

    val notesFlow: Flow<PagingData<NoteUi>> = _state
        .debounce(100L)
        .map { state -> GetNoteFilter(state.sortOption, state.searchQuery) }
        .flatMapLatest(getAllNotesUseCase::invoke)
        .map { pagingData -> pagingData.map { it.toUi() } }
        .distinctUntilChanged()
        .cachedIn(viewModelScope)

    fun onEvent(event: NoteListEvent) {
        when (event) {
            is NoteListEvent.SetSearchQuery -> setSearchQuery(event.query)
            is NoteListEvent.ToggleSortDirection -> toggleSortDirection()
            is NoteListEvent.CreateNote -> createNote(event.title, event.content)
            is NoteListEvent.DeleteNote -> deleteNote(event.note)
            is NoteListEvent.ToggleNoteCompletion -> toggleNoteCompletion(event.note)
        }
    }

    private fun setSearchQuery(query: String) {
        _state.update { it.copy(searchQuery = query) }
    }

    private fun toggleSortDirection() {
        val newSortOption = when (_state.value.sortOption) {
            SortOption.LatestFirst -> SortOption.OldestFirst
            SortOption.OldestFirst -> SortOption.LatestFirst
        }
        _state.update { it.copy(sortOption = newSortOption) }
    }

    private fun createNote(title: String, content: String) {
        viewModelScope.launch(dispatcherProvider.io) {
            val param = CreateNoteParam(title = title, content = content)
            createNoteUseCase(param)
        }
    }

    private fun deleteNote(note: NoteUi) {
        viewModelScope.launch(dispatcherProvider.io) {
            deleteNoteUseCase(note.toNote())
        }
    }

    private fun toggleNoteCompletion(note: NoteUi) {
        viewModelScope.launch(dispatcherProvider.io) {
            val updatedNote = note.copy(isCompleted = !note.isCompleted)
            updateNoteUseCase(updatedNote.toNote())
        }
    }
}

data class NoteListState(
    val isLoading: Boolean = false,
    val sortOption: SortOption = SortOption.LatestFirst,
    val searchQuery: String = ""
)

sealed interface NoteListEvent {
    object ToggleSortDirection : NoteListEvent
    data class DeleteNote(val note: NoteUi) : NoteListEvent
    data class SetSearchQuery(val query: String) : NoteListEvent
    data class ToggleNoteCompletion(val note: NoteUi) : NoteListEvent
    data class CreateNote(val title: String, val content: String) : NoteListEvent
}
