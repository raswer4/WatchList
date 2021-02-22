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
            val name = findViewById<EditText>(R.id.nameEditText)
            val email = findViewById<EditText>(R.id.emailEditText)
            val password = findViewById<EditText>(R.id.passwordEditText)
            val repeatPassword = findViewById<EditText>(R.id.rPasswordEditText)
            val errorsExist= validate(name,email, password, repeatPassword)

            if (!errorsExist){
                createAccount(name.editableText.toString(), email.editableText.toString(), password.editableText.toString())
            }
        }
    }

    private fun validate(name: EditText, email: EditText,password: EditText, repeatPassword: EditText): Boolean {
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
            password.setError( getString(R.string.shortPW))
            errorsExist = true
        }
        if (password.editableText.toString().equals(repeatPassword)==false) {
           repeatPassword.setError( getString(R.string.notEqualPW))
            errorsExist = true
        }
        return errorsExist
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
