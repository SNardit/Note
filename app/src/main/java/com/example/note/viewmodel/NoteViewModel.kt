package com.example.note.viewmodel

import androidx.lifecycle.Observer
import com.example.note.model.Note
import com.example.note.model.NoteResults
import com.example.note.model.Repository
import com.example.note.ui.BaseViewModel
import com.example.note.ui.NoteViewState

class NoteViewModel(val repository: Repository = Repository) :
    BaseViewModel<Note?, NoteViewState>() {

    private var pendingNote: Note? = null

    fun saveChanges(note: Note) {
        pendingNote = note
    }

    override fun onCleared() {
        if (pendingNote != null) {
            repository.saveNote(pendingNote!!)
        }
    }

    fun loadNote(noteId: String) {
        repository.getNoteById(noteId).observeForever(object : Observer<NoteResults> {
            override fun onChanged(t: NoteResults?) {
                if (t == null) return

                when (t) {
                    is NoteResults.Success<*> -> {
                        viewStateLiveData.value = NoteViewState(note = t.data as? Note)

                    }
                    is NoteResults.Error -> {
                        viewStateLiveData.value = NoteViewState(error = t.error)
                    }
                }
            }

        })
    }
}