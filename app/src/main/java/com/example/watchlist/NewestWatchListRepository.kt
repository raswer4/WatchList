package com.example.watchlist
import android.content.ContentValues
import android.content.Context
import android.net.Uri
import android.util.Log
import android.widget.Toast
import com.example.watchlist.sampledata.UserListFragment
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.DocumentChange
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.ktx.storage


import java.util.*
val newestWatchListRepository= NewestWatchListRepository()

class NewestWatchListRepository : NewestWatchlistFirebase() {

    val auth = FirebaseAuth.getInstance()
    val currentUser = auth.currentUser
    private val newestTitles = mutableListOf<NewestWatch>()

    init {
        val db: FirebaseFirestore = FirebaseFirestore.getInstance()
        val collectionReference = db.collection("Admins").document("WatchList").collection("NewestTitles")
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
                        DocumentChange.Type.ADDED -> addtoAdminsWatchlistRepository(title,content, date, img, platform, id)
                        DocumentChange.Type.MODIFIED -> updateAdminsWatchListById(id,title,content,date,platform)
                        DocumentChange.Type.REMOVED -> deleteAdminWatchListById(id)
                    }
                }
            }
        }
    }

    fun createWatchList(title: String, content: String, date: String, platform: String,pathRef:String): Long{
        val id = getHighestNewestWatchId() + 1

        try {

            addWatchListFirebase(id,title,content,date,pathRef,platform)

        }catch (e: IllegalAccessException){
            throw e
        }
        newestTitles.add(
            NewestWatch(
                id,
                title,
                content,
                date,
                platform,
                pathRef
            )
        )

        return id
    }


    fun addAdminsWatchList(title: String, content: String, date: String, platform: String): Long{
        val id = getHighestNewestWatchId() + 1

        newestTitles.add(
            NewestWatch(
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

    private fun addtoAdminsWatchlistRepository(title: String, content: String, date: String,  img: String, platform: String, id: Long){
        newestTitles.add(
            NewestWatch(
                id,
                title,
                content,
                date,
                platform,
                img
            )
        )
    }


    fun getAllAdminWatchLists() = newestTitles

    fun getAdminsWatchListById(id: Long) =newestTitles.find {
            it.Id == id
    }


    fun deleteAdminWatchListById(id: Long) {
                newestTitles.remove(
                    newestTitles.find {
                        it.Id == id
                    }
                )
    }
    fun updateAdminsWatchListById(
        id: Long,
        newTitle: String,
        newContent: String,
        newDate: String,
        newPlatform: String,
    ){
            getAdminsWatchListById(id)?.run{
                Title = newTitle
                Content = newContent
                Date = newDate
                Platform = newPlatform
            }
    }

    fun getHighestNewestWatchId():Long{
        var id:Long = 1
        for(watch in newestTitles){
            if (id<watch.Id){
                id = watch.Id
            }
        }
        return id
    }


}


