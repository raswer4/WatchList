package com.example.watchlist

import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.UserProfileChangeRequest
import com.google.firebase.auth.ktx.auth
import com.google.firebase.auth.ktx.userProfileChangeRequest
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
        setContentView(R.layout.activity_register)
        auth = Firebase.auth
        findViewById<Button>(R.id.register_button).setOnClickListener{
            val name = findViewById<EditText>(R.id.name).editableText.toString()
            val email = findViewById<EditText>(R.id.emailEditText).editableText.toString()
            val password = findViewById<EditText>(R.id.passwordTextView).editableText.toString()
            val repeatPassword = findViewById<EditText>(R.id.rPasswordEditText).editableText.toString()
            val numberOfErrors= validate(name,email, password, repeatPassword)

            if (numberOfErrors>0){
            }else{
                createAccount(name, email, password)

            }
        }
    }
    private fun validate(name: String, email: String,password: String, repeatPassword: String):Int {
        var numberOfErrors = 0
        if (name.isEmpty()) {
            Toast.makeText(this, getString(R.string.invalidName), Toast.LENGTH_SHORT).show()
            numberOfErrors += 1
        }
        if (!email.contains("@")) {
           // Snackbar.make(findViewById(R.id.login_error_text), getString(R.string.invalidEmail), 400).show()
            numberOfErrors += 1
        }
        if (password.length<9) {
            Toast.makeText(this, getString(R.string.shortPW), Toast.LENGTH_SHORT).show()
            numberOfErrors += 1
        }
        if (password.equals(repeatPassword)==false) {
            Toast.makeText(this, getString(R.string.notEqualPW), Toast.LENGTH_SHORT).show()
            numberOfErrors += 1
        }
        return numberOfErrors
    }


    private fun createAccount(name: String, email: String, passWord: String){
        auth.createUserWithEmailAndPassword(email, passWord)
            .addOnCompleteListener(this) { task ->
                if (task.isSuccessful) {
                    val user = auth.currentUser
                    val profileUpdate = UserProfileChangeRequest.Builder()
                        .setDisplayName(name)
                        .build()
                    user!!.updateProfile(profileUpdate)
                        .addOnCompleteListener(this){ task ->
                            if(task.isSuccessful){
                                finish()
                            }else{
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
