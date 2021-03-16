package com.example.watchlist.sampledata

import android.content.Intent
import android.graphics.Color
import android.media.MediaPlayer
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.view.WindowManager
import android.widget.Button
import androidx.core.graphics.toColor
import androidx.core.view.MotionEventCompat.getButtonState
import androidx.core.view.isGone
import androidx.core.view.isVisible
import com.example.watchlist.R
import com.example.watchlist.databinding.ActivityMainMenuBinding
import com.example.watchlist.newestWatchListRepository
import com.example.watchlist.watchListRepository
import com.google.firebase.auth.FirebaseAuth


class MainMenuActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val bindning = ActivityMainMenuBinding.inflate(layoutInflater)
        window.setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);

        watchListRepository.getDataFromFirebase()
        newestWatchListRepository.getAllDataFromFirebase()
        setContentView(bindning.root)
        val auth = FirebaseAuth.getInstance()

        if(auth.currentUser?.isAnonymous == true){
            findViewById<Button>(R.id.My_Watchlist_button).visibility = View.GONE
        }

        if(savedInstanceState == null) {
            supportFragmentManager
                    .beginTransaction()
                    .add(R.id.frame_layout, NewestTitlesFragment())
                    .commit()
                    findViewById<Button>(R.id.My_Watchlist_button).setTextColor(Color.parseColor("#FFFFFF"))
                    findViewById<Button>(R.id.Profile_setting_Button).setTextColor(Color.parseColor("#FFFFFF"))
                    findViewById<Button>(R.id.Newest_Titles_Button).setTextColor(Color.parseColor("#FF0000"))
        }

        bindning.NewestTitlesButton.setOnClickListener{
            supportFragmentManager
                    .beginTransaction()
                    .replace(R.id.frame_layout, NewestTitlesFragment())
                    .commit()
                    findViewById<Button>(R.id.My_Watchlist_button).setTextColor(Color.parseColor("#FFFFFF"))
                    findViewById<Button>(R.id.Profile_setting_Button).setTextColor(Color.parseColor("#FFFFFF"))
                    findViewById<Button>(R.id.Newest_Titles_Button).setTextColor(Color.parseColor("#FF0000"))
        }

        bindning.MyWatchlistButton.setOnClickListener{
            supportFragmentManager
                .beginTransaction()
                .replace(R.id.frame_layout, UserListFragment())
                .commit()
                findViewById<Button>(R.id.My_Watchlist_button).setTextColor(Color.parseColor("#FF0000"))
                findViewById<Button>(R.id.Profile_setting_Button).setTextColor(Color.parseColor("#FFFFFF"))
                findViewById<Button>(R.id.Newest_Titles_Button).setTextColor(Color.parseColor("#FFFFFF"))

        }
        bindning.ProfileSettingButton.setOnClickListener{
            supportFragmentManager
                .beginTransaction()
                .replace(R.id.frame_layout, ProfileSettingFragment())
                .commit()
                findViewById<Button>(R.id.My_Watchlist_button).setTextColor(Color.parseColor("#FFFFFF"))
                findViewById<Button>(R.id.Profile_setting_Button).setTextColor(Color.parseColor("#FF0000"))
                findViewById<Button>(R.id.Newest_Titles_Button).setTextColor(Color.parseColor("#FFFFFF"))
        }

    }
}