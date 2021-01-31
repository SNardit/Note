package com.example.note.ui

import com.example.note.model.Note

class NoteViewState(note: Note? = null, error: Throwable? = null) :
    BaseViewState<Note?>(note, error)