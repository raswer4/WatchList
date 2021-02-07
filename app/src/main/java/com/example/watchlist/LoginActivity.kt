package com.example.watchlist

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Button
import android.widget.Toast
import android.widget.VideoView
import androidx.appcompat.app.AppCompatActivity
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.common.SignInButton
import com.google.android.gms.common.api.ApiException
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.auth.GoogleAuthProvider
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase




class LoginActivity : AppCompatActivity() {
    private lateinit var auth: FirebaseAuth
    private lateinit var googleSignInClient: GoogleSignInClient
    private var RC_SIGN_IN: Int=0

    // ...
    // Initialize Firebase Auth
    override fun onStart() {
        super.onStart()
        val user =  auth.currentUser
        if(user!=null){
            val intent = Intent(this,MainMenu::class.java)
            startActivity(intent)
        }
    }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        supportActionBar?.hide()
        setContentView(R.layout.activity_login)
        auth = FirebaseAuth.getInstance()
        val register_button = this.findViewById<Button>(R.id.register_button)
        val backgroundVideoPlayer = this.findViewById<VideoView>(R.id.videoView);
        val uri = Uri.parse("android.resource://"
                + packageName +"/"
                +R.raw.background);

        backgroundVideoPlayer.setVideoURI(uri);
        backgroundVideoPlayer.start();

        backgroundVideoPlayer.setOnCompletionListener{
            backgroundVideoPlayer.seekTo(0);
            backgroundVideoPlayer.start()
        }

        register_button.setOnClickListener(){
            val intent = Intent(this, RegisterActivity::class.java)
            startActivity(intent)
        }
        createRequest()
        findViewById<SignInButton>(R.id.sign_in_button).setOnClickListener {
            signIn()
        }
    }
    private fun createRequest(){
        val gso = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN).requestIdToken(getString(R.string.default_web_client_id)).requestEmail().build()
        googleSignInClient = GoogleSignIn.getClient(this, gso)
    }

    private fun signIn() {
        val signInIntent = googleSignInClient.signInIntent
        startActivityForResult(signInIntent, RC_SIGN_IN)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        // Result returned from launching the Intent from GoogleSignInApi.getSignInIntent(...);
        if (requestCode == RC_SIGN_IN) {
            val task = GoogleSignIn.getSignedInAccountFromIntent(data)
            //try {
                // Google Sign In was successful, authenticate with Firebase
                val account = task.getResult(ApiException::class.java)!!
                firebaseAuthWithGoogle(account.idToken!!)
            //}
            /*catch (e: ApiException) {
                Toast.makeText(this,getString(R.string.faildLogin),Toast.LENGTH_SHORT).show()
            }*/
        }
    }

    private fun firebaseAuthWithGoogle(idToken: String) {
        val credential = GoogleAuthProvider.getCredential(idToken, null)
        auth.signInWithCredential(credential).addOnCompleteListener(this) { task ->
            if (task.isSuccessful) {
                // Sign in success, update UI with the signed-in user's information
                val user = auth.currentUser
                val intent = Intent(this,MainMenu::class.java)
                startActivity(intent)
            } else {
                Toast.makeText(this,getString(R.string.faildLogin),Toast.LENGTH_SHORT).show()
            }

        }
    }
}