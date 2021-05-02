package com.example.application

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
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

        // Read from the database


    }

    override fun onStart() {
        super.onStart()
        mRef!!.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                mNoteList?.clear()
             for(n in dataSnapshot.children){
                 //Toast.makeText(applicationContext,n.toString(),Toast.LENGTH_LONG).show()
                 Log.e("Main", "onDataChange: ")

                 var note = dataSnapshot.getValue(Note::class.java)
                 mNoteList?.add(note!!)
             }
                val adapter = NoteAdapter(applicationContext, mNoteList!!)
                binding.listView.adapter = adapter
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