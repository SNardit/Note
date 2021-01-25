package com.example.note.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.note.model.Note
import com.example.note.model.Repository
import com.example.note.ui.MainViewState

class MainViewModel : ViewModel() {

    val viewStateLiveData: MutableLiveData<MainViewState> = MutableLiveData()

    init {
        viewStateLiveData.value = MainViewState(Repository.notes)
    }

    fun viewState(): LiveData<MainViewState> = viewStateLiveData

    fun addNoteToRep() {
        Repository.addNote(Note(" ", " "))
    }

    fun updateNotes() {
        viewStateLiveData.value = MainViewState(Repository.notes)
    }
}