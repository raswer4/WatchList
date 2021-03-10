package com.example.watchlist

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.view.WindowManager
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.ktx.storage
import com.squareup.picasso.Picasso

class WatchViewActivity : AppCompatActivity() {

    private var storageRef = Firebase.storage.reference


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        supportActionBar?.hide()
        window.setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN)
        setContentView(R.layout.activity_watch_view)


        val id = intent.getLongExtra("id", 0)
        val watch = watchListRepository.getWatchListById(id)
        val imgReference = watch?.Img
        val movieImage = findViewById<ImageView>(R.id.moviePoster)
        val deleteButton = findViewById<Button>(R.id.DeleteWatchList)
        val updateButton = findViewById<Button>(R.id.updateWatchList)
        Log.d("msg",watch!!.Img)

        this.findViewById<TextView>(R.id.titleTextView).apply {
            text = watch.Title
        }
        this.findViewById<TextView>(R.id.contentTextView).apply {
            text = watch.Content
        }
        this.findViewById<TextView>(R.id.dateTextView).apply {
            text = watch.Date
        }
        this.findViewById<TextView>(R.id.platformTextView).apply {
            text = watch.Platform
        }
        this.findViewById<ImageView>(R.id.moviePoster).apply {

            val pathReference = storageRef.child(imgReference!!)
            pathReference.downloadUrl.addOnSuccessListener{
                Picasso.get().load(it).into(movieImage)
            }
        }
        updateButton.setOnClickListener(){
            val intent = Intent(this, UpdateWatchListActivity::class.java).apply {
                putExtra("update", id)
            }
            startActivity(intent)
            finish();
        }


        deleteButton.setOnClickListener(){
            try{
                AlertDialog.Builder(this)
                    .setTitle("Delete " + watch?.Title)
                    .setMessage("Do you really want to delete it?")
                    .setPositiveButton(
                        "Yes"
                    ) { dialog, whichButton ->
                        watchListRepository.deleteWatchListById(id)
                        watchListRepository.deleteWatchListFirebase(imgReference!!,id)
                        this.finish()
                    }.setNegativeButton(
                        "No"
                    ) { dialog, whichButton ->
                        // Do not delete it.
                    }.show()
            }catch (e: IllegalStateException ){
                Toast.makeText( this,getString(e.message!!.toInt()),Toast.LENGTH_SHORT)
            }

        }
    }
}
