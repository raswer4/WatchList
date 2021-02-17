package com.example.watchlist

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.WindowManager
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import com.example.watchlist.sampledata.UserListFragment
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.example.watchlist.sampledata.MainMenuActivity as MainMenuActivity1

class CreateWatchListActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        supportActionBar?.hide()
        window.setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);

        setContentView(R.layout.activity_create_watch_list)
        val createWatchButton = this.findViewById<Button>(R.id.createWatchList)

        createWatchButton.setOnClickListener(){
            val watchTitle = this.findViewById<EditText>(R.id.createTitle).editableText.toString().trim()
            val watchContent = this.findViewById<EditText>(R.id.createContent).editableText.toString().trim()
            val watchDate = this.findViewById<EditText>(R.id.createDate).editableText.toString().trim()

            val id = watchListRepository.addWatchList(watchTitle,watchContent,watchDate,"hello",this)
            Toast.makeText(this, id.toString(), Toast.LENGTH_SHORT).show()

            val intent = Intent(this, WatchViewActivity::class.java).apply {
                putExtra("id", id)
            }
            startActivity(intent)
            finish()


        }
    }
}
