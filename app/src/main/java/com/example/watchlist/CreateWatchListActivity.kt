package com.example.watchlist

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.WindowManager
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import kotlin.collections.HashMap

class CreateWatchListActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        supportActionBar?.hide()
        window.setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);

        setContentView(R.layout.activity_create_watch_list)
        val createWatchButton = this.findViewById<Button>(R.id.createWatchList)

        createWatchButton.setOnClickListener(){
            val watchTitle = this.findViewById<EditText>(R.id.createTitle).editableText.toString().trim()
            val watchContent = this.findViewById<EditText>(R.id.createContent).editableText.toString().trim()
            val watchDate = this.findViewById<EditText>(R.id.createDate).editableText.toString().trim()


            val database = FirebaseFirestore.getInstance()
            val auth = FirebaseAuth.getInstance()
            val currentUser = auth.currentUser
            val watch = HashMap<String, Any>()

            
            watch.put("Title", watchTitle)
            watch.put("Content", watchContent)
            watch.put("Date", watchDate)

            database.collection("users").document("watchList").collection("titles").document(watchTitle).set(watch)
                .addOnSuccessListener {
                    Toast.makeText(this, "Success", Toast.LENGTH_SHORT).show()
                    val intent = Intent(this, WatchViewActivity::class.java)
                    startActivity(intent)

                }
                .addOnFailureListener{
                    Toast.makeText(this, "Failure LOL", Toast.LENGTH_SHORT).show()
                }

        }
    }
}
