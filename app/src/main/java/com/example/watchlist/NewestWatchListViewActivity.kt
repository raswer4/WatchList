package com.example.watchlist

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.WindowManager
import android.widget.TextView

class NewestWatchListViewActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        supportActionBar?.hide()
        window.setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN)
        setContentView(R.layout.activity_watch_view)

        val id = intent.getLongExtra("newTitlesID", 0)
        val watch = watchListRepository.getWatchListById(id)

        this.findViewById<TextView>(R.id.newestTitlesViewTitle).apply {
            if( watch != null){
                text = watch.Title
            }else{
                text = "nothing came"
            }
        }
        this.findViewById<TextView>(R.id.newestTitlesViewContent).apply {
            text = watch?.Content
        }
        this.findViewById<TextView>(R.id.newestTitlesViewDate).apply {
            text = watch?.Date
        }

    }
}