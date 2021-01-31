package com.example.note.model

sealed class NoteResults {

    data class Success<out T>(val data: T) : NoteResults()

    data class Error(val error: Throwable) : NoteResults()

}