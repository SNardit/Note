package com.example.note.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.note.model.Repository
import com.example.note.ui.MainViewState

class MainViewModel : ViewModel() {

    val viewStateLiveData: MutableLiveData<MainViewState> = MutableLiveData()

    init {
        Repository.getNotes().observeForever { notes ->
            viewStateLiveData.value = viewStateLiveData.value?.copy(notes = notes)
                ?: MainViewState(notes)
        }
    }

    fun viewState(): LiveData<MainViewState> = viewStateLiveData
}