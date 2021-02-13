package com.example.watchlist.sampledata

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.watchlist.CreateWatchListActivity
import com.example.watchlist.LoginActivity
import com.example.watchlist.R
import com.example.watchlist.databinding.ActivityMainMenuBinding
import com.example.watchlist.databinding.FragmentUserListBinding

class UserListFragment : Fragment() {

    lateinit var binding: FragmentUserListBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val bindning = ActivityMainMenuBinding.inflate(layoutInflater)
    }
}