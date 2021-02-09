package com.example.watchlist

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import com.squareup.picasso.Picasso


class ProfileActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_profile)
        val name = findViewById<TextView>(R.id.name)
        val eMail = findViewById<TextView>(R.id.eMail)
        val profilePic = findViewById<ImageView>(R.id.profilePic)

        val signInAccount = Firebase.auth.currentUser
        if (signInAccount != null){
            Picasso.get().load(signInAccount.photoUrl).into(profilePic)
            name.setText(signInAccount.displayName)
            eMail.setText(signInAccount.email)
        }else{
            val intent = Intent(applicationContext, LoginActivity::class.java)
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
            startActivity(intent)
        }
        findViewById<Button>(R.id.logout).setOnClickListener{
            Firebase.auth.signOut()
            val intent = Intent(applicationContext, LoginActivity::class.java)
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
            startActivity(intent)
        }






    }

}