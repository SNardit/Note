package com.example.note.model

import androidx.lifecycle.LiveData

interface RemoteDataProvider {

    fun subscribeToAllNotes(): LiveData<NoteResults>

    fun getNoteById(id: String): LiveData<NoteResults>

    fun saveNote(note: Note): LiveData<NoteResults>

}