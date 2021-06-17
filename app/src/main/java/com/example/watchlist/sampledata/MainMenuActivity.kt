package com.example.watchlist.sampledata

import android.content.Intent
import android.graphics.Color
import android.media.MediaPlayer
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Menu
import android.view.View
import android.view.WindowManager
import android.widget.Button
import androidx.core.graphics.toColor
import androidx.core.view.MotionEventCompat.getButtonState
import androidx.core.view.isGone
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import com.example.watchlist.CreateWatchListActivity.Companion.auth
import com.example.watchlist.R
import com.example.watchlist.databinding.ActivityMainMenuBinding
import com.example.watchlist.newestWatchListRepository
import com.example.watchlist.watchListRepository
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.firebase.auth.FirebaseAuth


class MainMenuActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main_menu)
        val bindning = ActivityMainMenuBinding.inflate(layoutInflater)
        window.setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);

        watchListRepository.getDataFromFirebase()
        newestWatchListRepository.getAllDataFromFirebase()
        setContentView(bindning.root)


        val newestFragment = NewestTitlesFragment()
        val userFragment = UserListFragment()
        val profileFragment = ProfileSettingFragment()
        val bottomNavigationView = findViewById<BottomNavigationView>(R.id.bottom_navigation)


        makeCurrentFragment(newestFragment)

        bottomNavigationView.setOnNavigationItemReselectedListener {
            when (it.itemId){
                R.id.navigation_home -> makeCurrentFragment(newestFragment)
                R.id.navigation_file_storage -> makeCurrentFragment(userFragment)
                R.id.navigation_profile -> makeCurrentFragment(profileFragment)

            }
            true
        }
        if(auth.currentUser?.isAnonymous == true){
            bottomNavigationView.menu.removeItem(R.id.navigation_file_storage);
        }
    }




        private fun makeCurrentFragment(fragment: Fragment) =
            supportFragmentManager.beginTransaction().apply {
                replace(R.id.frame_layout, fragment)
                commit()
        }

}