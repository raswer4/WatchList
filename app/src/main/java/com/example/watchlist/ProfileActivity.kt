package com.example.watchlist

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.firebase.auth.FirebaseAuth



class ProfileActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_profile)
        val name = findViewById<TextView>(R.id.name)
        val eMail = findViewById<TextView>(R.id.eMail)

        val signInAccount = GoogleSignIn.getLastSignedInAccount(this)
        if (signInAccount != null){
            name.setText(signInAccount.displayName)
            eMail.setText(signInAccount.email)
        }
        findViewById<Button>(R.id.logout).setOnClickListener{
            FirebaseAuth.getInstance().signOut()
            val intent = Intent(applicationContext, LoginActivity::class.java)
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
            startActivity(intent)
        }




    }
}