package com.example.watchlist

import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.*

class LoginActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        supportActionBar?.hide()
        setContentView(R.layout.activity_login)

        val backgroundVideoPlayer = this.findViewById<VideoView>(R.id.videoView);

        val uri = Uri.parse("android.resource://"
                + packageName +"/"
                +R.raw.lighting);
        backgroundVideoPlayer.setVideoURI(uri);
        backgroundVideoPlayer.start();
        backgroundVideoPlayer
    }

}