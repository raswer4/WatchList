package com.example.watchlist

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.ArrayAdapter
import android.widget.GridView
import android.widget.ListView

class NewestTitlesActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        supportActionBar?.hide()
        setContentView(R.layout.activity_newest_titles)

        var gridView = this.findViewById<GridView>(R.id.gridView)
        gridView.adapter = ArrayAdapter<Watch>(
            this,
            android.R.layout.simple_list_item_1,
            android.R.id.text1,
            watchListRepository.getAllWatchLists()

        )
        /*gridView.setOnItemClickListener{ parent, view, position, id ->
            val item = gridView.getItemAtPosition(position) as ToDo
            val intent = Intent(this, ViewToDoActivity::class.java).apply {
                putExtra("id", item.id)
            }
            startActivity(intent)
        }*/

    }
    override fun onStart() {
        super.onStart()

        var gridView = this.findViewById<GridView>(R.id.gridView)

        gridView.adapter = ArrayAdapter<Watch>(
            this,
            android.R.layout.simple_list_item_1,
            android.R.id.text1,
            watchListRepository.getAllWatchLists()
        )
        (gridView.adapter as ArrayAdapter<Watch>).notifyDataSetChanged()
    }
}