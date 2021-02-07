package com.example.watchlist

import android.content.Intent
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.*

class LoginActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        supportActionBar?.hide()
        setContentView(R.layout.activity_login)

        val register_button = this.findViewById<Button>(R.id.register_button)
        val testButton = this.findViewById<Button>(R.id.testButton)
        val backgroundVideoPlayer = this.findViewById<VideoView>(R.id.videoView);

        val uri = Uri.parse("android.resource://"
                + packageName +"/"
                +R.raw.background);

        backgroundVideoPlayer.setVideoURI(uri);
        backgroundVideoPlayer.start();

        backgroundVideoPlayer.setOnCompletionListener{
            backgroundVideoPlayer.seekTo(0);
            backgroundVideoPlayer.start()
        }

        register_button.setOnClickListener(){
            val intent = Intent(this, RegisterActivity::class.java)
            startActivity(intent)
        }

        testButton.setOnClickListener(){
            val intent = Intent(this, CreateWatchListActivity::class.java)
            startActivity(intent)
        }



    }

}