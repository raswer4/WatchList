package com.example.watchlist.sampledata

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.fragment.app.Fragment
import com.example.watchlist.R
import com.example.watchlist.databinding.ActivityMainMenuBinding
import com.example.watchlist.watchListRepository


class MainMenuActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val bindning = ActivityMainMenuBinding.inflate(layoutInflater)

        watchListRepository.getDataFromFirebase(this)
        setContentView(bindning.root)

        if(savedInstanceState == null) {
            supportFragmentManager
                    .beginTransaction()
                    .add(R.id.frame_layout, NewestTitlesFragment())
                    .commit()
        }
        bindning.NewestTitlesButton.setOnClickListener{
            supportFragmentManager
                    .beginTransaction()
                    .replace(R.id.frame_layout, NewestTitlesFragment())
                    .commit()
        }
        bindning.MyWatchlistButton.setOnClickListener{
            supportFragmentManager
                .beginTransaction()
                .replace(R.id.frame_layout, UserListFragment())
                .commit()
        }
        bindning.ProfileSettingButton.setOnClickListener{
            supportFragmentManager
                .beginTransaction()
                .replace(R.id.frame_layout, ProfileSettingFragment())
                .commit()
        }

    }
}