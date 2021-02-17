package com.example.watchlist.sampledata

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.example.watchlist.LoginActivity
import com.example.watchlist.R
import com.example.watchlist.databinding.FragmentProfileSettingBinding
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
                Firebase.auth.signOut()
                binding.name.text = " "
                binding.eMail.text = " "
                binding.profilePic.setImageResource(R.drawable.watchlist_logo)
                val intent = Intent(activity, LoginActivity::class.java)
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
                startActivity(intent)


            }
        }

    }
}
