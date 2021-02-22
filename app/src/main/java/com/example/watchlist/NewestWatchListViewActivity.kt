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
        setContentView(R.layout.activity_newest_watch_list_view)

        val id = intent.getLongExtra("newestTitlesID", 0)
        val watch = watchListRepository.getWatchListById(id)

        this.findViewById<TextView>(R.id.newestTitleTextView).apply {
            text = watch?.Title
        }
        this.findViewById<TextView>(R.id.newestContentTextView).apply {
            text = watch?.Content
        }
        this.findViewById<TextView>(R.id.newestDateTextView).apply {
            text = watch?.Date
        }

    }
}