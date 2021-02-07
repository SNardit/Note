package com.example.note.viewmodel

import androidx.annotation.VisibleForTesting
import androidx.lifecycle.Observer
import com.example.note.model.Note
import com.example.note.model.NoteResults
import com.example.note.model.Repository
import com.example.note.ui.MainViewState

class MainViewModel(repository: Repository) :
    BaseViewModel<List<Note>?, MainViewState>() {

    private val notesObserver = object : Observer<NoteResults> {
        override fun onChanged(result: NoteResults?) {
            if (result == null) return

            when (result) {
                is NoteResults.Success<*> ->
                    viewStateLiveData.value = MainViewState(notes = result.data as? List<Note>)
                is NoteResults.Error ->
                    viewStateLiveData.value = MainViewState(error = result.error)

            }
        }
    }

    private val repositoryNotes = repository.getNotes()

    init {
        viewStateLiveData.value = MainViewState()
        repositoryNotes.observeForever(notesObserver)
    }

    @VisibleForTesting(otherwise = VisibleForTesting.PROTECTED)
    public override fun onCleared() {
        repositoryNotes.removeObserver(notesObserver)
    }
}