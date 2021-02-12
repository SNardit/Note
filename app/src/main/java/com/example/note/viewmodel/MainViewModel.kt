package com.example.note.viewmodel

import androidx.annotation.VisibleForTesting
import com.example.note.model.Note
import com.example.note.model.NoteResults
import com.example.note.model.Repository
import kotlinx.coroutines.channels.consumeEach
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking

class MainViewModel(repository: Repository) :
    BaseViewModel<List<Note>?>() {

    private val notesChannel by lazy { runBlocking { repository.getNotes() } }

    init {
        launch {
            notesChannel.consumeEach { result ->
                when (result) {
                    is NoteResults.Success<*> -> setData(result.data as? List<Note>)
                    is NoteResults.Error -> setError(result.error)
                }
            }
        }
    }

    @VisibleForTesting(otherwise = VisibleForTesting.PROTECTED)
    public override fun onCleared() {
        notesChannel.cancel()
        super.onCleared()

    }
}