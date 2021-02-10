package com.example.watchlist

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.WindowManager
import android.widget.Button

class MainMenu : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        supportActionBar?.hide()
        window.setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN)
        setContentView(R.layout.activity_main_menu)

        findViewById<Button>(R.id.profileButtom).setOnClickListener{
            startActivity(Intent(this,ProfileActivity::class.java))
        }

        findViewById<Button>(R.id.myWatchlist).setOnClickListener{
            startActivity(Intent(this,UserListActivity::class.java))
        }

    }


}