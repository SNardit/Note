package com.example.note.ui

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.example.note.databinding.ActivityMainBinding
import com.example.note.model.Note
import com.example.note.viewmodel.MainViewModel

class MainActivity : AppCompatActivity() {

    lateinit var ui: ActivityMainBinding
    private lateinit var viewModel: MainViewModel
    lateinit var adapter: MainAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        ui = ActivityMainBinding.inflate(layoutInflater)
        setContentView(ui.root)

        setSupportActionBar(ui.toolbar)
        viewModel = ViewModelProvider(this).get(MainViewModel::class.java)
        adapter = MainAdapter(object : OnItemClickListener {
            override fun onItemClick(note: Note) {
                openNoteScreen(note)
            }
        })
        ui.mainRecycler.adapter = adapter

        viewModel.viewState().observe(this, Observer<MainViewState> { state ->
            state?.let { adapter.notes = state.notes as MutableList<Note> }
        })

        ui.fab.setOnClickListener { openNoteScreen() }
    }

    private fun openNoteScreen(note: Note? = null) {
        startActivity(NoteActivity.getStartIntent(this, note))
    }

}


