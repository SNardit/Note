package com.example.note.viewmodel

import androidx.lifecycle.Observer
import com.example.note.model.Note
import com.example.note.model.NoteResults
import com.example.note.model.Repository
import com.example.note.ui.MainViewState

class MainViewModel(repository: Repository = Repository) :
    BaseViewModel<List<Note>?, MainViewState>() {

    private val notesObserver = object : Observer<NoteResults> {
        override fun onChanged(t: NoteResults?) {
            if (t == null) return

            when (t) {
                is NoteResults.Success<*> -> {
                    viewStateLiveData.value = MainViewState(notes = t.data as? List<Note>)

                }
                is NoteResults.Error -> {
                    viewStateLiveData.value = MainViewState(error = t.error)
                }
            }
        }
    }

    private val repositoryNotes = repository.getNotes()

    init {
        viewStateLiveData.value = MainViewState()
        repositoryNotes.observeForever(notesObserver)
    }

    override fun onCleared() {
        repositoryNotes.removeObserver(notesObserver)
    }
}