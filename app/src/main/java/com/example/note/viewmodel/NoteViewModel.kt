package com.example.note.viewmodel

import com.example.note.model.Note
import com.example.note.model.NoteResults
import com.example.note.model.Repository
import com.example.note.ui.NoteViewState
import com.example.note.ui.NoteViewState.*

class NoteViewModel(val repository: Repository) :
    BaseViewModel<Data, NoteViewState>() {

    private val currentNote: Note?
        get() = viewStateLiveData.value?.data?.note

    fun saveChanges(note: Note) {
        viewStateLiveData.value = NoteViewState(Data(note = note))
    }

    override fun onCleared() {
        currentNote?.let { repository.saveNote(it) }
    }

    fun loadNote(noteId: String) {
        repository.getNoteById(noteId).observeForever { t ->
            t?.let { noteResult ->
                viewStateLiveData.value = when (noteResult) {
                    is NoteResults.Success<*> ->
                        NoteViewState(Data(note = noteResult.data as? Note))

                    is NoteResults.Error ->
                        NoteViewState(error = noteResult.error)
                }
            }
        }
    }

    fun deleteNote() {
        currentNote?.let { note ->
            repository.deleteNote(note.id).observeForever { result ->
                result?.let { noteResult ->
                    viewStateLiveData.value = when (noteResult) {
                        is NoteResults.Success<*> ->
                            NoteViewState(Data(isDeleted = true))

                        is NoteResults.Error ->
                            NoteViewState(error = noteResult.error)
                    }
                }
            }
        }
    }
}