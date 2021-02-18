package com.example.watchlist

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Toast

class UpdateWatchListActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_update_watch_list)

        val updateButton = this.findViewById<Button>(R.id.updateButton)
        val id = intent.getIntExtra("id",0)


        updateButton.setOnClickListener(){
            val updateTitle = this.findViewById<EditText>(R.id.updateTitle).editableText.toString().trim()
            val updateContent = this.findViewById<EditText>(R.id.updateContent).editableText.toString().trim()
            val updatehDate = this.findViewById<EditText>(R.id.updateDate).editableText.toString().trim()

            val id = watchListRepository.updateWatchListById(id, updateTitle,updateContent,updatehDate,this)
            Toast.makeText(this, id.toString(), Toast.LENGTH_SHORT).show()


            finish()
        }
    }
}