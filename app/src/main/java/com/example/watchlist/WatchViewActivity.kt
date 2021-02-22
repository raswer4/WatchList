package com.example.watchlist

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.WindowManager
import android.widget.Button
import android.widget.TextView
import androidx.appcompat.app.AlertDialog

class WatchViewActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        supportActionBar?.hide()
        window.setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN)
        setContentView(R.layout.activity_watch_view)

        val id = intent.getIntExtra("id", 0)
        val watch = watchListRepository.getWatchListById(id)
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
                        watchListRepository.deleteWatchListById(id, this)
                        this.finish()
                    }.setNegativeButton(
                            "No"
                    ) { dialog, whichButton ->
                        // Do not delete it.
                    }.show()
        }
    }
}