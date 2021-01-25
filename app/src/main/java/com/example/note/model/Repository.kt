package com.example.note.model

import com.example.note.ui.MainViewState
import com.example.note.viewmodel.MainViewModel

object Repository {
    var notes: MutableList<Note> = mutableListOf(
        Note(
            "1 заметка",
            "какой-то текст",
            0xffb2ff59.toInt()
        ),
        Note(
            "2 заметка",
            "какой-то текст",
            0xffb2ff59.toInt()
        ),
        Note(
            "3 заметка",
            "какой-то текст",
            0xffb2ff59.toInt()
        )
    )

    fun addNote(note: Note) {
        notes.add(note)
        MainViewModel().updateNotes()
    }

}