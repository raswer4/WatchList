package com.example.watchlist

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Toast

class RegisterActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        supportActionBar?.hide()
        setContentView(R.layout.activity_register)

        findViewById<Button>(R.id.login_button).setOnClickListener{
            val name = findViewById<EditText>(R.id.name).editableText.toString()
            val email = findViewById<EditText>(R.id.eMail).editableText.toString()
            val password = findViewById<EditText>(R.id.register_password).editableText.toString()
            val repeatPassword = findViewById<EditText>(R.id.repeat_password).editableText.toString()
            val numberOfErrors= validate(name,email,password,repeatPassword)
            if (numberOfErrors<1){

            }
        }
    }
    private fun validate(name:String,email:String,password:String,repeatPassword:String):Int {
        var numberOfErrors = 0
        if (name.isEmpty()) {
            Toast.makeText(this, getString(R.string.invalidName), Toast.LENGTH_SHORT).show()
            numberOfErrors += 1
        }
        if (!email.contains("@")) {
            Toast.makeText(this, getString(R.string.invalidEmail), Toast.LENGTH_SHORT).show()
            numberOfErrors += 1
        }
        if (password.length<9) {
            Toast.makeText(this, getString(R.string.shortPW), Toast.LENGTH_SHORT).show()
            numberOfErrors += 1
        }
        if (password != repeatPassword) {
            Toast.makeText(this, getString(R.string.notEqualPW), Toast.LENGTH_SHORT).show()
            numberOfErrors += 1
        }
        return numberOfErrors
    }

}
