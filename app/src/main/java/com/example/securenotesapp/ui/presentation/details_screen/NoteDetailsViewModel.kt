package com.example.securenotesapp.ui.presentation.details_screen

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.securenotesapp.domain.NotesRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

data class NoteDetailsState(
    val title: String = "",
    val content: String = "",
    val createdAt: Long = System.currentTimeMillis(),
    val isLoading: Boolean = true
)

class NoteDetailsViewModel(
    private val noteId: Long,
    private val repository: NotesRepository
) : ViewModel() {

    private val _state = MutableStateFlow(NoteDetailsState())
    val state: StateFlow<NoteDetailsState> = _state.asStateFlow()
    val isNewNote: Boolean get() = noteId == -1L

    init {
        if (noteId == -1L) {
            _state.value = _state.value.copy(isLoading = false)
        } else {
            viewModelScope.launch {
                val note = repository.getNote(noteId)
                if (note != null) {
                    _state.value = NoteDetailsState(
                        title = note.title,
                        content = note.content,
                        createdAt = note.createdAt,
                        isLoading = false
                    )
                } else {
                    _state.value = _state.value.copy(isLoading = false)
                }
            }
        }
    }

    fun onTitleChange(value: String) {
        _state.value = _state.value.copy(title = value)
    }

    fun onContentChange(value: String) {
        _state.value = _state.value.copy(content = value)
    }

    fun save(onDone: () -> Unit) {
        viewModelScope.launch {
            val s = _state.value
            if (noteId == -1L) {
                repository.createNote(s.title, s.content)
            } else {
                repository.updateNote(noteId, s.title, s.content)
            }
            onDone()
        }
    }

    fun delete(onDone: () -> Unit) {
        if (isNewNote) return
        viewModelScope.launch {
            repository.deleteNote(noteId)
            onDone()
        }
    }

}