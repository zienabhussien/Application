package com.example.application

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.TextView

class NoteAdapter(context: Context,noteList : ArrayList<Note>)
    : ArrayAdapter<Note>(context,0,noteList){

    override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {

        val view =LayoutInflater.from(context).inflate(R.layout.note_layout,parent,false)

        val note = getItem(position)
        var titleTV = view.findViewById<TextView>(R.id.titleTV)
        var timeTV = view.findViewById<TextView>(R.id.timeTV)
          titleTV.text = note?.title.toString()
          timeTV.text = note?.timestamp.toString()

        return view
    }
}