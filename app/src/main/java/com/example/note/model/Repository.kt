package com.example.note.model

import com.example.note.model.providers.RemoteDataProvider

class Repository(private val remoteProvider: RemoteDataProvider) {

    suspend fun getNotes() = remoteProvider.subscribeToAllNotes()

    suspend fun saveNote(note: Note) = remoteProvider.saveNote(note)

    suspend fun getNoteById(id: String) = remoteProvider.getNoteById(id)

    suspend fun getCurrentUser() = remoteProvider.getCurrentUser()

    suspend fun deleteNote(noteId: String) = remoteProvider.deleteNote(noteId)

}