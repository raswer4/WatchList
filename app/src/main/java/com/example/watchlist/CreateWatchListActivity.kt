package com.example.watchlist

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase

class CreateWatchListActivity : AppCompatActivity() {

    private lateinit var referance: DatabaseReference
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        supportActionBar?.hide()
        setContentView(R.layout.activity_create_watch_list)

        var database = FirebaseDatabase.getInstance()
        referance = database.getReference("watchList")

        val createWatchButton = this.findViewById<Button>(R.id.createWatchList)

        createWatchButton.setOnClickListener(){
            sendData()
        }
    }
    private fun sendData(){
        val title = this.findViewById<Button>(R.id.createTitle).editableText.toString()
        val content = this.findViewById<Button>(R.id.createContent).editableText.toString()
        val date = this.findViewById<Button>(R.id.createDate).editableText.toString().toInt()

        var model = Watch(title, content, date)
        var id = referance.push().key

        referance.child(id!!).setValue(model)
    }
}