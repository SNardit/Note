package com.example.note.model.providers

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.example.note.model.NoAuthException
import com.example.note.model.Note
import com.example.note.model.NoteResults
import com.example.note.model.User
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore

private const val NOTES_COLLECTION = "notes"
private const val USERS_COLLECTION = "users"
private val TAG = "${FireStoreProvider::class.java.simpleName} :"

class FireStoreProvider(
    private val firebaseAuth: FirebaseAuth,
    private val db: FirebaseFirestore
) : RemoteDataProvider {

    private val currentUser
        get() = firebaseAuth.currentUser

    override fun subscribeToAllNotes(): LiveData<NoteResults> =
        MutableLiveData<NoteResults>().apply {

            try {
                getUserNoteCollection().addSnapshotListener { snapshot, error ->
                    value = error?.let { e -> NoteResults.Error(e) }
                        ?: snapshot?.let { s ->
                            val notes = s.documents.map { document ->
                                document.toObject(Note::class.java)
                            }
                            NoteResults.Success(notes)
                        }
                }
            } catch (e: Throwable) {
                value = NoteResults.Error(e)
            }
        }

    override fun getNoteById(id: String): LiveData<NoteResults> =
        MutableLiveData<NoteResults>().apply {

            try {
                getUserNoteCollection().document(id)
                    .get()
                    .addOnSuccessListener { snapshot ->
                        value = NoteResults.Success(snapshot.toObject(Note::class.java))
                    }.addOnFailureListener { exception ->
                        throw exception
                    }
            } catch (e: Throwable) {
                value = NoteResults.Error(e)
            }
        }

    override fun saveNote(note: Note): LiveData<NoteResults> =
        MutableLiveData<NoteResults>().apply {

            try {
                getUserNoteCollection().document(note.id)
                    .set(note)
                    .addOnSuccessListener {
                        Log.d(TAG, "Note $note is saved")
                        value = NoteResults.Success(note)
                    }.addOnFailureListener { exception ->
                        Log.d(TAG, "Error saving note $note, message: ${exception.message}")
                        throw exception
                    }
            } catch (e: Throwable) {
                value = NoteResults.Error(e)
            }
        }

    override fun getCurrentUser(): LiveData<User?> =
        MutableLiveData<User?>().apply {
            value = currentUser?.let {
                User(
                    it.displayName ?: "",
                    it.email ?: ""

                )
            }
        }

    override fun deleteNote(noteId: String): LiveData<NoteResults> =
        MutableLiveData<NoteResults>().apply {

            try {
                getUserNoteCollection()
                    .document(noteId)
                    .delete()
                    .addOnSuccessListener {
                        Log.d(TAG, "Note $noteId is deleted")
                        value = NoteResults.Success(null)
                    }
                    .addOnFailureListener { exception ->
                        Log.d(TAG, "Error note delete $noteId, message: ${exception.message}")
                        throw exception
                    }

            } catch (e: Throwable) {
                value = NoteResults.Error(e)
            }
        }

    private fun getUserNoteCollection() = currentUser?.let { firebaseUser ->
        db.collection(USERS_COLLECTION)
            .document(firebaseUser.uid)
            .collection(NOTES_COLLECTION)
    } ?: throw NoAuthException()
}

