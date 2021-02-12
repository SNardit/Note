package com.example.note.model.providers

import android.util.Log
import com.example.note.model.NoAuthException
import com.example.note.model.Note
import com.example.note.model.NoteResults
import com.example.note.model.User
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ListenerRegistration
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.channels.ReceiveChannel
import kotlin.coroutines.resume
import kotlin.coroutines.resumeWithException
import kotlin.coroutines.suspendCoroutine

private const val NOTES_COLLECTION = "notes"
private const val USERS_COLLECTION = "users"
private val TAG = "${FireStoreProvider::class.java.simpleName} :"

class FireStoreProvider(
    private val firebaseAuth: FirebaseAuth,
    private val db: FirebaseFirestore
) : RemoteDataProvider {

    private val currentUser
        get() = firebaseAuth.currentUser

    override suspend fun subscribeToAllNotes(): ReceiveChannel<NoteResults> =
        Channel<NoteResults>(Channel.CONFLATED).apply {
            var registration: ListenerRegistration? = null
            try {
                registration = getUserNoteCollection().addSnapshotListener { snapshot, error ->
                    val value = error?.let { e ->
                        NoteResults.Error(e)
                    } ?: snapshot?.let { s ->
                        val notes = s.documents.map { document ->
                            document.toObject(Note::class.java)
                        }
                        NoteResults.Success(notes)
                    }

                    value?.let { offer(it) }
                }
            } catch (e: Throwable) {
                offer(NoteResults.Error(e))
            }

            invokeOnClose { registration?.remove() }
        }

    override suspend fun getNoteById(id: String): Note =

        suspendCoroutine { continuation ->

            try {
                getUserNoteCollection().document(id)
                    .get()
                    .addOnSuccessListener { snapshot ->
                        continuation.resume(snapshot.toObject(Note::class.java)!!)
                    }.addOnFailureListener { exception ->
                        continuation.resumeWithException(exception)
                    }
            } catch (e: Throwable) {
                continuation.resumeWithException(e)
            }
        }

    override suspend fun saveNote(note: Note): Note =

        suspendCoroutine { continuation ->
            try {
                getUserNoteCollection().document(note.id)
                    .set(note)
                    .addOnSuccessListener {
                        Log.d(TAG, "Note $note is saved")
                        continuation.resume(note)
                    }.addOnFailureListener { exception ->
                        Log.d(TAG, "Error saving note $note, message: ${exception.message}")
                        continuation.resumeWithException(exception)
                    }
            } catch (e: Throwable) {
                continuation.resumeWithException(e)
            }
        }

    override suspend fun getCurrentUser(): User? =
        suspendCoroutine { continuation ->
            currentUser?.let {
                continuation.resume(
                    User(
                        it.displayName ?: "",
                        it.email ?: ""
                    )
                )
            } ?: continuation.resume(null)
        }

    override suspend fun deleteNote(noteId: String): Note? =
        suspendCoroutine { continuation ->
            try {
                getUserNoteCollection()
                    .document(noteId)
                    .delete()
                    .addOnSuccessListener {
                        Log.d(TAG, "Note $noteId is deleted")
                        continuation.resume(null)
                    }
                    .addOnFailureListener { exception ->
                        Log.d(TAG, "Error note delete $noteId, message: ${exception.message}")
                        continuation.resumeWithException(exception)
                    }

            } catch (e: Throwable) {
                continuation.resumeWithException(e)
            }

        }

    private fun getUserNoteCollection() = currentUser?.let { firebaseUser ->
        db.collection(USERS_COLLECTION)
            .document(firebaseUser.uid)
            .collection(NOTES_COLLECTION)
    } ?: throw NoAuthException()
}

