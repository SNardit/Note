package com.example.note.ui

import com.example.note.model.Note
import com.example.note.ui.BaseViewState

class MainViewState(notes: List<Note>? = null, error: Throwable? = null) :
    BaseViewState<List<Note>?>(notes, error)