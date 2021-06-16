package com.example.watchlist
import android.content.ContentValues
import android.net.Uri
import android.util.Log
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
    private val watchList = mutableListOf<Watch>()

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
                        DocumentChange.Type.ADDED -> addtoWatchlistRepository(
                            title,
                            content,
                            date,
                            img,
                            platform,
                            id
                        )
                        DocumentChange.Type.MODIFIED -> updateWatchListById(id,title,content,date,platform,img)
                        DocumentChange.Type.REMOVED -> deleteWatchListById(id)
                    }
                }
            }

        }
    }

/*
    fun createWatchList(title: String, content: String, date: String, platform: String): Long{
        val id = getHighestWatchId() + 1
        try {

            addWatchListFirebase(id,title,content,date,platform)

        }catch (e: IllegalAccessException){
            throw e
        }
        watchList.add(
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
*/
    fun createWatchList(title: String, content: String, date: String, img: String, platform: String): Long{
        val id = getHighestWatchId() + 1

        try {
            addtoWatchlistRepository(
                title,
                content,
                date,
                img,
                platform,
                id
            )
            addWatchListFirebase(id,title,content,date,img,platform)

        }catch (e: IllegalAccessException){
            throw e
        }




        return id
    }



    fun addtoWatchlistRepository(
        title: String,
        content: String,
        date: String,
        img: String,
        platform: String,
        id: Long
    ){
        watchList.add(
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

    fun clearWatchListRepository()=  watchList.clear()

    fun getAllWatchLists() = watchList

    fun getWatchListById(id: Long) =
        watchList.find {
            it.Id == id
        }

    fun deleteWatchListById(id: Long) {
        watchList.remove(
            watchList.find {
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
        newImgPath:String,
    ){
        getWatchListById(id)?.run {
            Title = newTitle
            Content = newContent
            Date = newDate
            Platform = newPlatform
            Img = newImgPath
        }

    }

    fun getHighestWatchId():Long{
        var id:Long = 1

        for(watch in watchList){
            if (id<watch.Id){
                id= watch.Id
            }
        }
        return id
    }
}