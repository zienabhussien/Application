package com.example.application

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.application.databinding.ActivityNoteBinding

class NoteActivity : AppCompatActivity() {
    lateinit var binding: ActivityNoteBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityNoteBinding.inflate(layoutInflater)
        setContentView(binding.root)
      var note = intent.extras?.getString("note_key").toString()
      var title = intent.extras?.getString("title_key").toString()
        binding.noteBodyTV.text = note
        binding.titleBodyTV.text = title

    }
}