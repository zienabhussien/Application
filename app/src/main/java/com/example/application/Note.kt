package com.example.application

data class Note(var id: String,var title: String,
                var note: String,var timestamp: String){

constructor():this(id: String, title: String, note: String, timestamp: String){

}

}