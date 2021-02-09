package com.example.watchlist

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.Toast
import com.google.firebase.firestore.FirebaseFirestore

class CreateWatchListActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        supportActionBar?.hide()
        setContentView(R.layout.activity_create_watch_list)



        val createWatchButton = this.findViewById<Button>(R.id.createWatchList)

        createWatchButton.setOnClickListener(){
            val watchTitle = this.findViewById<Button>(R.id.createTitle).editableText.toString()
            val watchContent = this.findViewById<Button>(R.id.createContent).editableText.toString()
            val watchDate = this.findViewById<Button>(R.id.createDate).editableText.toString().toInt()

            sendData(watchTitle, watchContent, watchDate)
        }
    }
    private fun sendData( title: String, content: String, date: Int){
        val database = FirebaseFirestore.getInstance()
        val watchlist: MutableMap<String, Any> = HashMap()
        watchlist["title"] = title
        watchlist["content"] = content
        watchlist["date"] = date


        database.collection("user").document("watchlist").set(watchlist)
                .addOnSuccessListener { Toast.makeText(this, "WatchList Added Successfully", Toast.LENGTH_SHORT).show()
                }
                .addOnFailureListener{ Toast.makeText(this, "Failed To Add WatchList", Toast.LENGTH_SHORT).show()

                }

    }
}
