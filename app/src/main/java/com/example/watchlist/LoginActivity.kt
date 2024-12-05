package com.example.watchlist

import android.content.Intent
import android.net.Uri
import android.os.Bundle

import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import android.widget.VideoView
import androidx.appcompat.app.AppCompatActivity
import com.example.watchlist.sampledata.MainMenuActivity
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.common.SignInButton
import com.google.android.gms.common.api.ApiException
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.GoogleAuthProvider





class LoginActivity : AppCompatActivity() {
    private lateinit var auth: FirebaseAuth
    private lateinit var googleSignInClient: GoogleSignInClient
    private var RC_SIGN_IN: Int=0

    override fun onStart() {
        super.onStart()
        val user = auth.currentUser
        if(user!=null){
            val intent = Intent(this, MainMenuActivity::class.java)
            startActivity(intent)
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        // Result returned from launching the Intent from GoogleSignInApi.getSignInIntent(...);
        if (requestCode == RC_SIGN_IN) {
            val task = GoogleSignIn.getSignedInAccountFromIntent(data)
            try {
                // Google Sign In was successful, authenticate with Firebase
                val account = task.getResult(ApiException::class.java)!!
                firebaseAuthWithGoogle(account.idToken!!)
            }
            catch (e: ApiException) {
                Toast.makeText(this,getString(R.string.faildLogin),Toast.LENGTH_SHORT).show()
            }
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        supportActionBar?.hide()
        setContentView(R.layout.activity_login)
        backgroundVideoPlayer()
        createRequest()
        auth = FirebaseAuth.getInstance()
         findViewById<Button>(R.id.register_button) .setOnClickListener(){
            val intent = Intent(this, RegisterActivity::class.java)
            startActivity(intent)
        }

        findViewById<Button>(R.id.login_button).setOnClickListener{
            val email = findViewById<EditText>(R.id.login_username).editableText.toString()
            val password = findViewById<EditText>(R.id.login_password).editableText.toString()
            loginWithPassWord(email,password)

        }

        findViewById<SignInButton>(R.id.sign_in_button).setOnClickListener {
            signIn()
        }
    }

    private fun backgroundVideoPlayer(){
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
    }


    private fun createRequest(){
        val gso = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN).requestIdToken(getString(R.string.default_web_client_id)).requestEmail().build()
        googleSignInClient = GoogleSignIn.getClient(this, gso)
    }

    private fun signIn() {
        val signInIntent = googleSignInClient.signInIntent
        startActivityForResult(signInIntent, RC_SIGN_IN)
    }

    private fun firebaseAuthWithGoogle(idToken: String) {
        val credential = GoogleAuthProvider.getCredential(idToken, null)
        auth.signInWithCredential(credential).addOnCompleteListener(this) { task ->
            if (task.isSuccessful) {
                // Sign in success, update UI with the signed-in user's information

                val intent = Intent(this, MainMenuActivity::class.java)

                startActivity(intent)
            } else {
                Toast.makeText(this,getString(R.string.faildLogin),Toast.LENGTH_SHORT).show()
            }

        }
    }

    private fun loginWithPassWord(email:String,password:String){
        auth.signInWithEmailAndPassword(email, password)
            .addOnCompleteListener(this) { task ->
                if (task.isSuccessful) {

                    val intent = Intent(this,  MainMenuActivity::class.java)

                    startActivity(intent)
                } else {
                    Toast.makeText(baseContext, getString(R.string.faildLogin),
                        Toast.LENGTH_SHORT).show()
                }
            }
    }
}