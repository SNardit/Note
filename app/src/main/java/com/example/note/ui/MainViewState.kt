package com.example.note.ui

import com.example.note.model.Note

class MainViewState(var notes: List<Note>? = null, error: Throwable? = null) :
    BaseViewState<List<Note>?>(notes, error)