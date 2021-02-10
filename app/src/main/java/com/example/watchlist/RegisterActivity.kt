package com.example.watchlist

import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
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
        setContentView(R.layout.activity_register)
        auth = Firebase.auth
        findViewById<Button>(R.id.register_button).setOnClickListener{
            val name = findViewById<EditText>(R.id.name)
            val email = findViewById<EditText>(R.id.register_username)
            val password = findViewById<EditText>(R.id.register_password)
            val repeatPassword = findViewById<EditText>(R.id.repeat_password)
            val errorsExists= validate(name,email, password, repeatPassword)
            if (!errorsExists){
                createAccount(name.editableText.toString(), email.editableText.toString(), password.editableText.toString())
            }
        }
    }
    private fun validate(name: EditText, email: EditText,password: EditText, repeatPassword: EditText):Boolean {
        var errorsExists = false
        if (name.editableText.toString().isEmpty()) {
            name.setError(getString(R.string.invalidName))
            errorsExists = true
        }
        if (!email.editableText.toString().contains("@")) {
          email.setError(getString(R.string.invalidEmail))
            errorsExists = true
        }
        if (password.editableText.toString().length<8) {
            password.setError(getString(R.string.shortPW))
            errorsExists = true
        }
        if ((password.editableText.toString()) != (repeatPassword.editableText.toString())) {
            repeatPassword.setError(getString(R.string.notEqualPW))
            errorsExists = true
        }
        return errorsExists
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
                        .addOnCompleteListener(this) { task ->
                            if (task.isSuccessful) {
                                finish()
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
