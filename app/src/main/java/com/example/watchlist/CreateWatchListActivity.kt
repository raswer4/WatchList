package com.example.watchlist

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase

class CreateWatchListActivity : AppCompatActivity() {

    private lateinit var referance: DatabaseReference
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        supportActionBar?.hide()
        setContentView(R.layout.activity_create_watch_list)

        var database = FirebaseDatabase.getInstance()
        referance = database.getReference("watchList")

        val createWatchButton = this.findViewById<Button>(R.id.createWatchList)

        createWatchButton.setOnClickListener(){
            sendData()
        }
    }
    private fun sendData(){
        val title = this.findViewById<Button>(R.id.createTitle).editableText.toString()
        val content = this.findViewById<Button>(R.id.createContent).editableText.toString()
        val date = this.findViewById<Button>(R.id.createDate).editableText.toString().toInt()
        val watchId = watchListRepository.addWatchList(title , content, date,"upload.wikimedia.org/wikipedia/en/thumb/3/3b/SpongeBob_SquarePants_character.svg/1200px-SpongeBob_SquarePants_character.svg.png")
        var id = referance.push().key

        referance.child(id!!).setValue(watchListRepository.getWatchListById(watchId))
    }
}