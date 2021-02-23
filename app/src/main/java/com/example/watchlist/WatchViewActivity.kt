package com.example.watchlist

import android.content.Intent
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.WindowManager
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
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
        val imgView = findViewById<ImageView>(R.id.movieImage)
        val id = intent.getLongExtra("id", 0)
        val watch = watchListRepository.getWatchListById(id)




        if(watch != null){
            val imgReference = watch.Img
            val pathReference = storageRef.child(imgReference)
            pathReference.downloadUrl.addOnSuccessListener{
                Picasso.get().load(it).into(imgView)
                imgView.setImageURI(it)
            }.addOnFailureListener{
                Toast.makeText(this,getString(R.string.downloadError),Toast.LENGTH_SHORT).show()
            }
        }



        val deteleButton = findViewById<Button>(R.id.DeleteWatchList)
        val updateButton = findViewById<Button>(R.id.updateWatchList)

        this.findViewById<TextView>(R.id.viewTitle).apply {
           if( watch != null){
               text = watch.Title
           }else{
               text = "nothing came"
           }
        }
        this.findViewById<TextView>(R.id.viewContent).apply {
            text = watch?.Content
        }
        this.findViewById<TextView>(R.id.viewDate).apply {
            text = watch?.Date
        }

        updateButton.setOnClickListener(){
            val intent = Intent(this, UpdateWatchListActivity::class.java).apply {
                putExtra("id", id)
            }
            startActivity(intent)
            finish();
        }


        deteleButton.setOnClickListener(){
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
