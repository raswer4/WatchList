package com.example.watchlist.sampledata


import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import com.example.watchlist.CreateWatchListActivity
import com.example.watchlist.LoginActivity
import com.example.watchlist.R

import com.example.watchlist.databinding.ActivityMainMenuBinding
import com.example.watchlist.databinding.FragmentUserListBinding

@Suppress("DEPRECATION")
class UserListFragment : Fragment() {

    lateinit var binding: FragmentUserListBinding
    lateinit var button: Button
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    )= FragmentUserListBinding.inflate(inflater, container,false).run {
        binding = this
        root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

            binding.addToUserList.setOnClickListener {
                val i = Intent(activity,CreateWatchListActivity::class.java)
                activity?.startActivity(i)

            }


    }

}