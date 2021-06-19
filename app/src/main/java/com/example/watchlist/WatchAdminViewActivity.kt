package com.example.watchlist

import android.content.Intent
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.view.WindowManager
import android.widget.*
import androidx.appcompat.app.AlertDialog
import androidx.core.view.isGone
import androidx.core.view.isVisible
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.ktx.storage
import com.squareup.picasso.Picasso

class WatchAdminViewActivity : AppCompatActivity() {

    private var storageRef = Firebase.storage.reference
    private lateinit var imgUri: Uri

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        supportActionBar?.hide()
        window.setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_watch_admin_view)

        val id = intent.getLongExtra("newestId", 0)
        val newestWatch = newestWatchListRepository.getAdminsWatchListById(id)

        val auth = FirebaseAuth.getInstance()
        val currentUser = auth.currentUser
        val raswer = "5gQa5pb77yZ2wEVGEdPimfqC9Ii1"
        val ahmed = "nIGbMcoycoXMDvF6EuKFjnpZSiB3"
        val michael = "Dm5iWuHXvMMwrHbDmhu6ssjDXzm2"
        val currentUserId = currentUser?.uid
        val addToMyList = findViewById<Button>(R.id.addToMyListBtn)
        if(auth.currentUser?.isAnonymous == true){
            addToMyList.visibility = View.GONE
        }


        val deleteButton = findViewById<Button>(R.id.DeleteWatchList)
        val updateButton = findViewById<Button>(R.id.updateWatchList)
        val trailerButton = findViewById<Button>(R.id.trailerButton)



        if(currentUserId.toString() == raswer || currentUserId.toString() == ahmed || currentUserId.toString() == michael){
            updateButton.isVisible  = true
            deleteButton.isVisible = true
            addToMyList.isGone = true
        }


        addToMyList.setOnClickListener(){
            try {
                if(newestWatch!=null){
                    startActivity(Intent(this,CreateWatchListActivity::class.java).putExtra("id", id))
                    finish()
                }
            }catch (e: IllegalStateException){
                    Toast.makeText(this, getString(e.message!!.toInt()), Toast.LENGTH_SHORT).show()
            }
        }


        trailerButton.setOnClickListener(){
            if (newestWatch!!.Link != null) {
                val url = newestWatch?.Link.toString()
                val intent = Intent(Intent.ACTION_VIEW)
                intent.data = Uri.parse(url)
                startActivity(intent)
            }
        }

        updateButton.setOnClickListener(){
            val intent = Intent(this, UpdateAdminWatchListActivity::class.java).apply {
                putExtra("updateTitles", id)
            }
            startActivity(intent)
        }


        deleteButton.setOnClickListener(){
            AlertDialog.Builder(this)
                .setTitle("Delete "+newestWatch?.Title)
                .setMessage("Do you really want to delete it?")
                .setPositiveButton(
                    "Yes"
                ) { dialog, whichButton ->
                    newestWatchListRepository.deleteWatchListFirebase(id).addOnSuccessListener {
                        newestWatchListRepository.deleteAdminWatchListById(id)
                        this.finish()
                    }
                }.setNegativeButton(
                    "No"
                ) { dialog, whichButton ->
                }.show()
        }
    }


    override fun onStart() {
        super.onStart()
        val movieImage = findViewById<ImageView>(R.id.moviePoster)
        val id = intent.getLongExtra("newestId", 0)
        val newestWatch = newestWatchListRepository.getAdminsWatchListById(id)

        this.findViewById<TextView>(R.id.newestTitleTextView).apply {
            text = newestWatch?.Title
        }
        this.findViewById<TextView>(R.id.newestContentTextView).apply {
            text = newestWatch?.Content
        }
        this.findViewById<TextView>(R.id.newestDateTextView).apply {
            text = newestWatch?.Date
        }
        this.findViewById<TextView>(R.id.newestPlatformTextView).apply {
            text = newestWatch?.Platform
        }
        this.findViewById<ImageView>(R.id.moviePoster).apply {
            val imgReference = newestWatch?.Img
            val pathReference = storageRef.child(imgReference.toString())
            pathReference.downloadUrl.addOnSuccessListener{
                imgUri = it
                Picasso.get().load(it).into(movieImage)
            }
        }

    }

}