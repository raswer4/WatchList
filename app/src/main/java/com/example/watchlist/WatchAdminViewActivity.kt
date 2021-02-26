package com.example.watchlist

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.WindowManager
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.AlertDialog
import androidx.core.view.isVisible
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.ktx.storage
import com.squareup.picasso.Picasso

class WatchAdminViewActivity : AppCompatActivity() {

    private var storageRef = Firebase.storage.reference

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        supportActionBar?.hide()
        window.setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN)
        setContentView(R.layout.activity_watch_admin_view)

        val id = intent.getLongExtra("newestId", 0)
        val newestWatch = newestWatchListRepository.getAdminsWatchListById(id)

        val auth = FirebaseAuth.getInstance()
        val currentUser = auth.currentUser
        val raswer = "13IYtYt9Eme67jx8TmeAF5C5Kt33"
        val ahmed = "nIGbMcoycoXMDvF6EuKFjnpZSiB3"
        val michael = "Dm5iWuHXvMMwrHbDmhu6ssjDXzm2"
        val currentUserId = currentUser?.uid


        val movieImage = findViewById<ImageView>(R.id.moviePoster)
        val deleteButton = findViewById<Button>(R.id.DeleteWatchList)
        val updateButton = findViewById<Button>(R.id.updateWatchList)


        this.findViewById<TextView>(R.id.titleTextView).apply {
            text = newestWatch?.Title
        }
        this.findViewById<TextView>(R.id.contentTextView).apply {
            text = newestWatch?.Content
        }
        this.findViewById<TextView>(R.id.dateTextView).apply {
            text = newestWatch?.Date
        }
        this.findViewById<TextView>(R.id.platformTextView).apply {
            text = newestWatch?.Platform
        }
        this.findViewById<ImageView>(R.id.moviePoster).apply {
            val imgReference = newestWatch?.Img
            val pathReference = storageRef.child(imgReference.toString())
            pathReference.downloadUrl.addOnSuccessListener{
                Picasso.get().load(it).into(movieImage)
            }
        }


        if(currentUserId.toString() == raswer || currentUserId.toString() == ahmed || currentUserId.toString() == michael){
            updateButton.isVisible  = true
            deleteButton.isVisible = true
        }


        updateButton.setOnClickListener(){
            val intent = Intent(this, UpdateAdminWatchListActivity::class.java).apply {
                putExtra("updateTitles", id)
            }
            startActivity(intent)
            finish()
        }


        deleteButton.setOnClickListener(){
            AlertDialog.Builder(this)
                .setTitle("Delete"+newestWatch?.Title)
                .setMessage("Do you really want to delete it?")
                .setPositiveButton(
                    "Yes"
                ) { dialog, whichButton ->
                    newestWatchListRepository.deleteAdminWatchListById(id)
                    this.finish()
                }.setNegativeButton(
                    "No"
                ) { dialog, whichButton ->
                    // Do not delete it.
                }.show()
        }
    }
}