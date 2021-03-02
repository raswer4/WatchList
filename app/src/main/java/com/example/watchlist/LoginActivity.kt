package com.example.watchlist

import android.app.ProgressDialog
import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.media.MediaPlayer
import android.net.Uri
import android.os.Bundle
import android.view.WindowManager
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import com.example.watchlist.sampledata.MainMenuActivity
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.common.SignInButton
import com.google.android.gms.common.api.ApiException
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.auth.GoogleAuthProvider


@Suppress("DEPRECATION")
class LoginActivity : AppCompatActivity() {
    companion object{
        private lateinit var auth: FirebaseAuth
        private lateinit var googleSignInClient: GoogleSignInClient
        private var mediaPlay: MediaPlayer? = null
        private var RC_SIGN_IN: Int=0



    }


    override fun onStart() {
        super.onStart()
        val user = auth.currentUser
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
                Toast.makeText(this, getString(R.string.faildLogin) + " bye", Toast.LENGTH_SHORT).show()
            }
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        supportActionBar?.hide()
        window.setFlags(
            WindowManager.LayoutParams.FLAG_FULLSCREEN,
            WindowManager.LayoutParams.FLAG_FULLSCREEN
        )
        setContentView(R.layout.activity_login)
        backgroundVideoPlayer()
        auth = FirebaseAuth.getInstance()
        var user = auth.currentUser
        createRequest()
        if(user != null){
            val intent = Intent(this, MainMenuActivity::class.java)
            startActivity(intent)
        }

         findViewById<Button>(R.id.register_button).setOnClickListener(){
            val intent = Intent(this, RegisterActivity::class.java)
            startActivity(intent)
             mediaPlay = MediaPlayer.create(this,R.raw.swosh)
             mediaPlay?.start()
         }



        findViewById<Button>(R.id.login_button).setOnClickListener{
            val email = findViewById<EditText>(R.id.login_email)
            val password = findViewById<EditText>(R.id.login_password)
            val errorsExists = validate(email, password)

            if(!errorsExists){

                loginWithPassWord(email.editableText.toString(), password.editableText.toString())
            }
        }

        findViewById<SignInButton>(R.id.sign_in_button).setOnClickListener {
            val progressDialog:ProgressDialog = ProgressDialog(this)
            val email = findViewById<EditText>(R.id.login_email)
            progressDialog.setTitle(getString(R.string.loading))
            progressDialog.show()
            signIn()
        }

        findViewById<Button>(R.id.anonymousLoginBtn).setOnClickListener(){
            anonymous()

        }
    }

    private fun backgroundVideoPlayer(){
        val backgroundVideoPlayer = this.findViewById<VideoView>(R.id.videoView)
        val uri = Uri.parse(
            "android.resource://"
                    + packageName + "/"
                    + R.raw.background
        )

        backgroundVideoPlayer.setVideoURI(uri);
        backgroundVideoPlayer.start();

        backgroundVideoPlayer.setOnCompletionListener{
            backgroundVideoPlayer.seekTo(0)
            backgroundVideoPlayer.start()
        }
    }

    private fun anonymous(){
        val progressDialog: ProgressDialog = ProgressDialog(this)
        progressDialog.setTitle(getString(R.string.loading))
        progressDialog.show()
        auth.signInAnonymously()
            .addOnCompleteListener(this) { task ->
                progressDialog.dismiss()

                if (task.isSuccessful) {
                    // Sign in success, update UI with the signed-in user's information
                    Toast.makeText(this, "Logged In Anonymously", Toast.LENGTH_SHORT).show()
                    val user = auth.currentUser
                    updateUI(user)
                    val intent = Intent(this, MainMenuActivity::class.java)
                    startActivity(intent)
                } else {
                    // If sign in fails, display a message to the user.
                    Toast.makeText(this, "Couldn't Logged In Anonymously", Toast.LENGTH_SHORT).show()
                    Toast.makeText(baseContext, "Authentication failed.",
                        Toast.LENGTH_SHORT).show()
                    updateUI(null)
                }

                // ...
            }

    }

    private fun updateUI(user: FirebaseUser?) {
        if(user==null){
            auth.signInAnonymously().addOnCompleteListener(this){ task ->
                if (task.isSuccessful) {
                    // Sign in success, update UI with the signed-in user's information
                    val user = auth.currentUser
                    updateUI(user)
                }else{
                    updateUI(null)

                }
            }
        }
    }


    private fun createRequest(){
        val gso = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN).requestIdToken(
            getString(
                R.string.default_web_client_id
            )
        ).requestEmail().build()
        googleSignInClient = GoogleSignIn.getClient(this, gso)
    }

    private fun signIn() {
        val signInIntent = googleSignInClient.signInIntent
        startActivityForResult(signInIntent, RC_SIGN_IN)
    }

    private fun firebaseAuthWithGoogle(idToken: String) {
        val progressDialog: ProgressDialog = ProgressDialog(this)
        progressDialog.setTitle(getString(R.string.loading))
        progressDialog.show()
        val credential = GoogleAuthProvider.getCredential(idToken, null)
        auth.signInWithCredential(credential).addOnCompleteListener(this) { task ->
            progressDialog.dismiss()
            if (task.isSuccessful) {
                val intent = Intent(this, MainMenuActivity::class.java)
                startActivity(intent)
            } else {
                Toast.makeText(this, getString(R.string.faildLogin), Toast.LENGTH_SHORT).show()
            }

        }
    }




    private fun loginWithPassWord(email: String, password: String){
        val progressDialog = ProgressDialog(this)
        progressDialog.setTitle(getString(R.string.loading))
        progressDialog.show()
        auth.signInWithEmailAndPassword(email, password)
            .addOnCompleteListener(this) { task ->
                progressDialog.dismiss()
                if (auth.currentUser?.isEmailVerified == true) {
                    mediaPlay = MediaPlayer.create(this,R.raw.swosh)
                    mediaPlay?.start()
                    val intent = Intent(this, MainMenuActivity::class.java)
                    startActivity(intent)
                }
                else {
                    Toast.makeText(
                        baseContext, getString(R.string.faildLogin),
                        Toast.LENGTH_SHORT
                    ).show()
                }
            }
    }

    private fun validate(email: EditText, password: EditText):Boolean{
        var errorsExists = false
        if (!email.editableText.toString().contains("@")) {
            email.setError(getString(R.string.invalidEmail))
            errorsExists = true
        }
        if (password.editableText.toString().length<8) {
            password.setError(getString(R.string.shortPW))
            errorsExists = true
        }
        return errorsExists
    }

}