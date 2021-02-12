package com.example.note.viewmodel

import com.example.note.model.Note
import com.example.note.model.Repository
import com.example.note.ui.NoteViewState.Data
import kotlinx.coroutines.launch

class NoteViewModel(val repository: Repository) :
    BaseViewModel<Data>() {

    private val currentNote: Note?
        get() = getViewState().poll()?.note

    fun saveChanges(note: Note) {
        setData(Data(note = note))
    }

    fun loadNote(noteId: String) {
        launch {
            try {
                setData(Data(note = repository.getNoteById(noteId)))
            } catch (e: Throwable) {
                setError(e)
            }
        }
    }

    fun deleteNote() {
        launch {
            try {
                currentNote?.let { repository.deleteNote(it.id) }
                setData(Data(isDeleted = true))
            } catch (e: Throwable) {
                setError(e)
            }
        }
    }

    override fun onCleared() {
        launch {
            currentNote?.let { repository.saveNote(it) }
            super.onCleared()
        }
    }

}