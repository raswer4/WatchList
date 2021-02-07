package com.example.watchlist

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle

class MainMenu : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        supportActionBar?.hide()
        setContentView(R.layout.activity_main_menu)
    }


}