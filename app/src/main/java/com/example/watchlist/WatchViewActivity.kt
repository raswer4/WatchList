package com.example.watchlist

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle

class WatchViewActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        supportActionBar?.hide()
        setContentView(R.layout.activity_watch_view)
    }
}