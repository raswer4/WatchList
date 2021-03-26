package com.example.watchlist.sampledata

import android.app.Activity
import android.content.Intent
import android.content.pm.ActivityInfo
import android.media.MediaPlayer
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.example.watchlist.LoginActivity
import com.example.watchlist.R
import com.example.watchlist.databinding.FragmentProfileSettingBinding
import com.example.watchlist.newestWatchListRepository
import com.example.watchlist.watchListRepository
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import com.squareup.picasso.Picasso


class ProfileSettingFragment() : Fragment() {

    lateinit var binding: FragmentProfileSettingBinding


    override fun onCreateView(

        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?


    ) = FragmentProfileSettingBinding.inflate(inflater, container, false).run {
        binding = this
        activity?.requestedOrientation = ActivityInfo.SCREEN_ORIENTATION_PORTRAIT;
        root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val signInAccount = Firebase.auth.currentUser
        if (signInAccount != null) {

            binding.run {
                name.setText(signInAccount.displayName)
                eMail.setText(signInAccount.email)
                Picasso.get().load(signInAccount.photoUrl).into(profilePic)
            }
            binding.logout.setOnClickListener {
                if(Firebase.auth.currentUser!!.isAnonymous){
                    Firebase.auth.currentUser!!.delete()
                }
                    watchListRepository.clearWatchListRepository()
                    Firebase.auth.signOut()
                    binding.name.text = " "
                    binding.eMail.text = " "
                    val intent = Intent(activity, LoginActivity::class.java)
                    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
                    intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
                    startActivity(intent)

            }
        }

        if(signInAccount!!.isAnonymous){
            binding.name.text = getString(R.string.Unknown)
            binding.eMail.text = getString(R.string.UnknownEmail)
            binding.profilePic.setBackgroundResource(R.drawable.unknown_user)
            binding.logout.text = getString(R.string.goBackToLogin)
        }

    }
}
