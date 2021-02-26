package com.example.watchlist

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.WindowManager
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.AlertDialog
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

        val movieImage = findViewById<ImageView>(R.id.moviePoster)
        val deleteButton = findViewById<Button>(R.id.DeleteWatchList)
        val updateButton = findViewById<Button>(R.id.updateWatchList)

        this.findViewById<TextView>(R.id.titleTextView).apply {
           if( watch != null){
               text = watch.Title
           }else{
               text = "nothing came"
           }
        }
        this.findViewById<TextView>(R.id.contentTextView).apply {
            text = watch?.Content
        }
        this.findViewById<TextView>(R.id.dateTextView).apply {
            text = watch?.Date
        }
        this.findViewById<TextView>(R.id.platformTextView).apply {
            text = watch?.Platform
        }
        this.findViewById<ImageView>(R.id.moviePoster).apply {
            val imgReference = watch?.Img
            val pathReference = storageRef.child(imgReference.toString())
            pathReference.downloadUrl.addOnSuccessListener{
                Picasso.get().load(it).into(movieImage)
            }
        }
        updateButton.setOnClickListener(){
            val intent = Intent(this, UpdateWatchListActivity::class.java).apply {
                putExtra("id", id)
            }
            startActivity(intent)
            finish();
        }


        deleteButton.setOnClickListener(){
            AlertDialog.Builder(this)
                    .setTitle("Delete"+title)
                    .setMessage("Do you really want to delete it?")
                    .setPositiveButton(
                            "Yes"
                    ) { dialog, whichButton ->
                        watchListRepository.deleteWatchListById(id)
                        this.finish()
                    }.setNegativeButton(
                            "No"
                    ) { dialog, whichButton ->
                        // Do not delete it.
                    }.show()
        }
    }
}
