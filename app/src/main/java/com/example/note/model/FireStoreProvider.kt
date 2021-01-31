package com.example.note.model

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.QueryDocumentSnapshot

private const val NOTES_COLLECTION = "notes"

class FireStoreProvider : RemoteDataProvider {

    companion object {
        private val TAG = "${FireStoreProvider::class.java.simpleName} :"
    }

    private val db = FirebaseFirestore.getInstance()
    private val notesReference = db.collection(NOTES_COLLECTION)

    override fun subscribeToAllNotes(): LiveData<NoteResults> {
        val result = MutableLiveData<NoteResults>()

        notesReference.addSnapshotListener { value, error ->
            if (error != null) {
                result.value = NoteResults.Error(error)
            } else if (value != null) {
                val notes = mutableListOf<Note>()

                for (doc: QueryDocumentSnapshot in value) {
                    notes.add(doc.toObject(Note::class.java))
                }
                result.value = NoteResults.Success(notes)
            }
        }

        return result
    }

    override fun getNoteById(id: String): LiveData<NoteResults> {
        val result = MutableLiveData<NoteResults>()

        notesReference.document(id)
            .get()
            .addOnSuccessListener { snapshot ->
                result.value = NoteResults.Success(snapshot.toObject(Note::class.java))
            }.addOnFailureListener { exception ->
                result.value = NoteResults.Error(exception)
            }

        return result
    }

    override fun saveNote(note: Note): LiveData<NoteResults> {
        val result = MutableLiveData<NoteResults>()

        notesReference.document(note.id)
            .set(note)
            .addOnSuccessListener {
                Log.d(TAG, "Note $note is saved")
                result.value = NoteResults.Success(note)
            }.addOnFailureListener { exception ->
                Log.d(TAG, "Error saving note $note, message: ${exception.message}")
                result.value = NoteResults.Error(exception)
            }

        return result
    }
}