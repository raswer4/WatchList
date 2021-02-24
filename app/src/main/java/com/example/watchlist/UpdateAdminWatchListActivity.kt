package com.example.watchlist

import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.WindowManager
import android.widget.Button
import android.widget.EditText
import android.widget.Toast

class UpdateAdminWatchListActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_update_admin_watch_list)
        supportActionBar?.hide()
        window.setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN)

        val updateButton = this.findViewById<Button>(R.id.updateButton)
        val id = intent.getLongExtra("updateNewestTitlesID",0)


        updateButton.setOnClickListener(){
            val updateTitle = this.findViewById<EditText>(R.id.updateAdminTitleEditText).editableText.toString().trim()
            val updateContent = this.findViewById<EditText>(R.id.updateAdminContentEditText).editableText.toString().trim()
            val updateDate = this.findViewById<EditText>(R.id.updateAdminDateEditText).editableText.toString().trim()
            val updatePlatform = this.findViewById<EditText>(R.id.updateAdminPlatformEditText).editableText.toString().trim()

            val id = newestWatchListRepository.updateAdminsWatchListById(id, updateTitle,updateContent,updateDate, updatePlatform,
                Uri.EMPTY,this)
            Toast.makeText(this, id.toString(), Toast.LENGTH_SHORT).show()

            finish()
        }
    }
}
