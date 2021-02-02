package com.example.note.model

import com.example.note.model.providers.FireStoreProvider
import com.example.note.model.providers.RemoteDataProvider

object Repository {

    private val remoteDataProvider: RemoteDataProvider = FireStoreProvider()

    fun getNotes() = remoteDataProvider.subscribeToAllNotes()

    fun saveNote(note: Note) = remoteDataProvider.saveNote(note)

    fun getNoteById(id: String) = remoteDataProvider.getNoteById(id)

    fun getCurrentUser() = remoteDataProvider.getCurrentUser()


}