package com.example.note.ui

import com.example.note.model.Note
import com.example.note.viewmodel.MainViewModel

class MainViewState(var notes: List<Note>) {


    fun addNewNote() {
        MainViewModel().addNoteToRep()
    }
}

