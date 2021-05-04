package com.example.application

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.AdapterView
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import com.example.application.databinding.ActivityMainBinding
import com.google.firebase.database.*
import java.text.SimpleDateFormat
import java.util.*
import kotlin.collections.ArrayList

class MainActivity : AppCompatActivity() {
    lateinit var binding: ActivityMainBinding
    lateinit var mRef :DatabaseReference
    lateinit  var mNoteList : ArrayList<Note>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

       val database = FirebaseDatabase.getInstance()
         mRef = database.getReference("Notes")
        mNoteList = ArrayList()
        val adapter = NoteAdapter(this, mNoteList!!)
        binding.listView.adapter = adapter

        binding.ddNewNoteBtn.setOnClickListener {
             showDialog()
        }

       binding.listView.setOnItemClickListener { parent, view, position, id ->
           var note = mNoteList.get(position)
           var noteIntent = Intent(this,NoteActivity::class.java)
           noteIntent.putExtra("note_key",note.note)
           noteIntent.putExtra("title_key",note.title)
           startActivity(noteIntent)
       }

        binding.listView.onItemLongClickListener = AdapterView.OnItemLongClickListener{
                adapterView: AdapterView<*>, view1: View, i: Int, l: Long ->
            val alertBuilder = AlertDialog.Builder(this)
            val view = layoutInflater.inflate(R.layout.edit_note,null)
            var alertDialog = alertBuilder.create()
            alertDialog.setView(view)
            alertDialog.show()
            var noteEdit = view.findViewById<EditText>(R.id.noteEdit)
            var titleEdit = view.findViewById<EditText>(R.id.titleEdit)
            var updateBtn = view.findViewById<Button>(R.id.updateBtn)
            var deletteBtn = view.findViewById<Button>(R.id.deleteBtn)

            var myNote = mNoteList.get(i)
            noteEdit.setText(myNote.note)
            titleEdit.setText(myNote.title)

            false
        }

    }
    // Read from the database
    override fun onStart() {
        mNoteList?.clear()
        super.onStart()
        // Read from the database
        mRef.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {

                for( n in dataSnapshot.children){
                    var note = n.getValue(Note::class.java)
                    mNoteList.add(0,note!!)
                }
                var noteAdapter = NoteAdapter(applicationContext,mNoteList)
                binding.listView.adapter = noteAdapter
            }

            override fun onCancelled(error: DatabaseError) {
                // Failed to read value
            }
        })
    }

    private fun showDialog() {
        val alertBuilder = AlertDialog.Builder(this)
        val view = layoutInflater.inflate(R.layout.add_note,null)
        alertBuilder.setView(view)
        val alertDialog = alertBuilder.create()
        alertDialog.show()

        val titleEt = view.findViewById<EditText>(R.id.titleEt)
        val noteEt = view.findViewById<EditText>(R.id.noteEt)
        val addBtn = view.findViewById<Button>(R.id.addBtn)
        addBtn.setOnClickListener {
            val title = titleEt.text.toString()
            val note = noteEt.text.toString()

            if (title.isNotEmpty() && note.isNotEmpty()) {
                val id = mRef!!.push().key.toString()
                var myNote = Note(id,title,note,getCurrentDate())
                //mRef!!.child(id).setValue(title)
                mRef!!.child(id).setValue(myNote)
                alertDialog.dismiss()
            }else{
                Toast.makeText(this,"Empty!!",Toast.LENGTH_LONG).show()
            }
        }


    }
    fun getCurrentDate(): String{
        val calender = Calendar.getInstance()
        val mDFormat = SimpleDateFormat("EEE hh:mm a")
        val strDate = mDFormat.format(calender.time)
        return strDate
    }


}