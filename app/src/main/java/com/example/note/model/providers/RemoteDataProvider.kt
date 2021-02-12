package com.example.note.model.providers

import com.example.note.model.Note
import com.example.note.model.NoteResults
import com.example.note.model.User
import kotlinx.coroutines.channels.ReceiveChannel

interface RemoteDataProvider {

    suspend fun subscribeToAllNotes(): ReceiveChannel<NoteResults>

    suspend fun getNoteById(id: String): Note

    suspend fun saveNote(note: Note): Note

    suspend fun getCurrentUser(): User?

    suspend fun deleteNote(noteId: String): Note?

}