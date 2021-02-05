package com.example.note.model.providers

import androidx.lifecycle.LiveData
import com.example.note.model.Note
import com.example.note.model.NoteResults
import com.example.note.model.User

interface RemoteDataProvider {

    fun subscribeToAllNotes(): LiveData<NoteResults>

    fun getNoteById(id: String): LiveData<NoteResults>

    fun saveNote(note: Note): LiveData<NoteResults>

    fun getCurrentUser(): LiveData<User?>

    fun deleteNote(noteId: String): LiveData<NoteResults>

}