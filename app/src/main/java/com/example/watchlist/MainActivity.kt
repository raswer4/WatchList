package com.example.watchlist

import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.*

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        supportActionBar?.hide()
        setContentView(R.layout.activity_main)

        val backgroundVideoPlayer = this.findViewById<VideoView>(R.id.videoView);

        val uri = Uri.parse("android.resource://"
                + packageName +"/"
                +R.raw.lighting);
        backgroundVideoPlayer.setVideoURI(uri);
        backgroundVideoPlayer.start();
    }

}