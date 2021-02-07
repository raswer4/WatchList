package com.example.watchlist

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.TextView
import com.google.firebase.database.*
import com.google.firebase.ktx.Firebase

class WatchViewActivity : AppCompatActivity() {
    private lateinit var referance: DatabaseReference
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        supportActionBar?.hide()
        setContentView(R.layout.activity_watch_view)

        var database = FirebaseDatabase.getInstance()
        referance = database.getReference("watchList")


    }

    private fun getData()
    {

        referance.addValueEventListener(object : ValueEventListener
        {
            override fun onCancelled(p0: DatabaseError){

            }
            override fun onDataChange(p0: DataSnapshot){

                handler(data)
            }
        })

    }
}