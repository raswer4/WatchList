package com.example.watchlist

import android.app.ProgressDialog
import android.net.Uri
import android.os.Bundle
import android.view.WindowManager
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import android.widget.VideoView
import androidx.appcompat.app.AppCompatActivity
import androidx.core.graphics.red
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.UserProfileChangeRequest
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase

class RegisterActivity : AppCompatActivity() {
    private lateinit var auth: FirebaseAuth
    override fun onStart() {
        super.onStart()
        val currentUser = auth.currentUser
        if(currentUser != null){
            finish()
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        supportActionBar?.hide()
        window.setFlags(
            WindowManager.LayoutParams.FLAG_FULLSCREEN,
            WindowManager.LayoutParams.FLAG_FULLSCREEN
        );
        setContentView(R.layout.activity_register)
        backgroundVideoPlayer()

        auth = Firebase.auth
        findViewById<Button>(R.id.register_button).setOnClickListener{
            val name = findViewById<EditText>(R.id.nameEditText)
            val email = findViewById<EditText>(R.id.emailEditText)
            val password = findViewById<EditText>(R.id.passwordEditText)
            val repeatPassword = findViewById<EditText>(R.id.rPasswordEditText)
            val errorsExist= validate(name, email, password, repeatPassword)

            if (!errorsExist){
                createAccount(
                    name.editableText.toString(),
                    email.editableText.toString(),
                    password.editableText.toString()
                )
            }
        }
    }

    private fun backgroundVideoPlayer(){
        val backgroundVideoPlayer = this.findViewById<VideoView>(R.id.videoPlayer)
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

    private fun validate(
        name: EditText,
        email: EditText,
        password: EditText,
        repeatPassword: EditText
    ): Boolean {
        var errorsExist = false
        if (name.editableText.toString().isEmpty()) {
            name.setError(getString(R.string.invalidName))
            errorsExist = true
        }
        if (!email.editableText.toString().contains("@")) {
            email.setError(getString(R.string.invalidEmail))
            errorsExist = true
        }
        if (password.editableText.toString().length<8) {
            password.setError(getString(R.string.shortPW))
            errorsExist = true
        }
        if (password.editableText.toString() != repeatPassword.editableText.toString()) {
           repeatPassword.setError(getString(R.string.notEqualPW))
            errorsExist = true
        }
        return errorsExist
    }


    private fun createAccount(name: String, email: String, passWord: String){
        val progressDialog = ProgressDialog(this)
        progressDialog.setTitle(getString(R.string.loading))
        progressDialog.show()
        auth.createUserWithEmailAndPassword(email, passWord)
            .addOnCompleteListener(this) { task ->
                progressDialog.dismiss()
                if (task.isSuccessful) {
                    val user = auth.currentUser
                    user?.sendEmailVerification()
                    val profileUpdate = UserProfileChangeRequest.Builder()
                        .setDisplayName(name)
                        .build()
                    user!!.updateProfile(profileUpdate)
                        .addOnCompleteListener(this) { task ->
                            if (task.isSuccessful) {
                                finish()
                                Toast.makeText(
                                    baseContext, getString(R.string.VerfiyEmail),
                                    Toast.LENGTH_LONG,
                                ).show()
                            } else {
                                Toast.makeText(
                                    baseContext, getString(R.string.RegesterFaild),
                                    Toast.LENGTH_SHORT
                                ).show()
                            }
                        }
                } else {
                    Toast.makeText(
                        baseContext, getString(R.string.RegesterFaild),
                        Toast.LENGTH_SHORT
                    ).show()
                }
            }
    }



}
