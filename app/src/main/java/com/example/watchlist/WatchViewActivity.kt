package com.example.watchlist

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.WindowManager
import android.widget.TextView
import com.google.firebase.database.*
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.ktx.Firebase

class WatchViewActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        supportActionBar?.hide()
        window.setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN)
        setContentView(R.layout.activity_watch_view)
        readFireStoredData()
        var postList: List<Watch>
    }

        fun readFireStoredData(){
            val database = FirebaseFirestore.getInstance()
            val movieDescriptions = findViewById<TextView>(R.id.movieDescriptions)
            database.collection("users").document("watchList").collection("titles")
                .get()
                .addOnCompleteListener{
                    if(it.isSuccessful){
                        val result: StringBuffer = StringBuffer()

                        for(document in it.result!!){
                            result.append(document.data.getValue("Title")).append(" ")
                                .append(document.data.getValue("Content")).append("\n\n")
                                    .append(document.data.getValue("Date")).append("\n\n")
                            movieDescriptions.text = result
                        }
                    }
                }

    }
}
