package com.example.watchlist
import android.app.AlarmManager
import android.app.PendingIntent
import android.content.ContentValues
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Build
import android.util.Log
import androidx.annotation.RequiresApi
import androidx.core.content.ContextCompat.getSystemService
import com.google.firebase.auth.FirebaseAuth

import com.google.firebase.firestore.DocumentChange
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.ktx.storage
import java.util.*

val watchListRepository = WatchListRepository()


class WatchListRepository : WatchlistFirebase() {

    val auth = FirebaseAuth.getInstance()
    val currentUser = auth.currentUser
    private val watchLists = mutableListOf<Watch>()
    private var storageRef = Firebase.storage.reference

    init {
        val db: FirebaseFirestore = FirebaseFirestore.getInstance()
        val collectionReference = db.collection("Users").document(currentUser!!.uid).collection("Titles")

        collectionReference.addSnapshotListener { snapshots, e ->
            if (e != null) {
                Log.w(ContentValues.TAG, "listen:error", e)
                return@addSnapshotListener
            }
            if (snapshots != null && !snapshots.metadata.hasPendingWrites()){
                for (dc in snapshots.documentChanges) {
                    val title = dc.document.data.getValue("Title") as String
                    val content = dc.document.data.getValue("Content") as String
                    val date = dc.document.data.getValue("Date") as String
                    val img = dc.document.data.getValue("Img") as String
                    val platform = dc.document.data.getValue("Platform") as String
                    val id = dc.document.data.getValue("Id") as Long
                    when (dc.type) {
                        DocumentChange.Type.ADDED -> addtoWatchlistRepository(title,content, date, img, platform, id)
                        DocumentChange.Type.MODIFIED -> updateWatchListById(id,title,content,date,platform)
                        DocumentChange.Type.REMOVED -> deleteWatchListById(id)
                    }
                }
            }

        }
    }


    fun createWatchList(title: String, content: String, date: String, img: Uri, platform: String): Long{
        val id = when {
            watchLists.count() == 0 -> 1
            else -> watchLists.last().Id+1
        }
        try {

            addWatchListFirebase(id,title,content,date,platform)

        }catch (e: IllegalAccessException){
            throw e
        }
        watchLists.add(
            Watch(
                id,
                title,
                content,
                date,
                platform,
                "images/${currentUser!!.uid}/$id"
            )
        )

        return id
    }

    fun createWatchList(title: String, content: String, date: String, img: String, platform: String): Long{
        val id = when {
            watchLists.count() == 0 -> 1
            else -> watchLists.last().Id+1
        }
        try {

            addWatchListFirebase(id,title,content,date,img,platform)

        }catch (e: IllegalAccessException){
            throw e
        }
        watchLists.add(
            Watch(
                id,
                title,
                content,
                date,
                platform,
                "images/${currentUser!!.uid}/$id"
            )
        )

        return id
    }



    fun addtoWatchlistRepository(title: String, content: String, date: String, img: String, platform: String, id: Long){
        watchLists.add(
            Watch(
                id,
                title,
                content,
                date,
                platform,
                img
            )
        )
    }

    fun clearWatchListRepository()=  watchLists.clear()

    fun getAllWatchLists() = watchLists

    fun getWatchListById(id: Long) =
        watchLists.find {
            it.Id == id
        }

    fun deleteWatchListById(id: Long) {
        watchLists.remove(
            watchLists.find {
                it.Id == id
            }
        )
    }
    fun updateWatchListById(
        id: Long,
        newTitle: String,
        newContent: String,
        newDate: String,
        newPlatform: String,
    ){
        getWatchListById(id)?.run {
            Title = newTitle
            Content = newContent
            Date = newDate
            Platform = newPlatform
        }

    }




}