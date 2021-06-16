package com.example.watchlist

import android.net.Uri
import com.google.android.gms.tasks.Task
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.QuerySnapshot
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.StorageTask
import com.google.firebase.storage.UploadTask
import com.google.firebase.storage.ktx.storage
import java.util.HashMap

open class NewestWatchlistFirebase {

        private var storageRef = Firebase.storage.reference
        fun addWatchListFirebase(id:Long, title: String, content: String, date: String, platform: String, link: String){

            val database = FirebaseFirestore.getInstance()
            val auth = FirebaseAuth.getInstance()
            val currentUser = auth.currentUser
            val adminWatch = HashMap<String, Any>()
            if (currentUser != null) {
                try {

                    adminWatch["Title"] = title
                    adminWatch["Content"] = content
                    adminWatch["Date"] = date
                    adminWatch["Id"] = id
                    adminWatch["Platform"] = platform
                    adminWatch["Link"] = link
                    adminWatch["Img"] = "adminImg/$id"
                    database.collection("Admins").document("WatchList").collection("NewestTitles")
                        .document(id.toString())
                        .set(adminWatch)
                        .addOnFailureListener {
                            throw error(R.string.error)
                        }

                } catch (e: IllegalStateException) {
                    throw e
                }
            } else {
                throw error(R.string.authError)
            }
        }

        fun addWatchListFirebase(id:Long,title: String, content: String, date: String, img: String, platform: String, link: String){

            val database = FirebaseFirestore.getInstance()
            val auth = FirebaseAuth.getInstance()
            val currentUser = auth.currentUser
            val watch = HashMap<String, Any>()
            if (currentUser != null) {
                try {
                    watch["Title"] = title
                    watch["Content"] = content
                    watch["Date"] = date
                    watch["Id"] = id
                    watch["Platform"] = platform
                    watch["Link"] = link
                    watch["Img"] = img
                    database.collection("Admins").document("WatchList").collection("NewestTitles")
                        .document(id.toString())
                        .set(watch)
                        .addOnFailureListener {
                            throw error(R.string.error)
                        }

                } catch (e: IllegalStateException) {
                    throw e
                }
            } else {
                throw error(R.string.authError)
            }
        }


        fun updateWatchListFirebase(
            id: Long,
            newTitle: String,
            newContent: String,
            newDate: String,
            newPlatform: String,
            newLink: String
        ): Task<Void> {
            val database = FirebaseFirestore.getInstance()
            val auth = FirebaseAuth.getInstance()
            val currentUser = auth.currentUser
            val newestWatch = HashMap<String, Any>()
            try{
                if(currentUser != null){
                    newestWatch["Title"] = newTitle
                    newestWatch["Content"] = newContent
                    newestWatch["Date"] = newDate
                    newestWatch["Id"] = id
                    newestWatch["Platform"] = newPlatform
                    newestWatch["Link"] = newLink
                    newestWatch["Img"] = "adminImg/$id"
                    return database.collection("Admins").document("WatchList").collection("NewestTitles").document(id.toString())
                        .set(newestWatch)
                        .addOnFailureListener{
                            throw error(R.string.error)
                        }
                }else{
                    throw error(R.string.authError)
                }
            }catch (e: IllegalStateException){
                throw e
            }
        }



        fun getAllDataFromFirebase(): Task<QuerySnapshot> {
            val database = FirebaseFirestore.getInstance()
            return database.collection("Admins").document("WatchList").collection("NewestTitles")
                .get()
                    .addOnCompleteListener{
                        for(document in it.result!!){
                            val title = document.data.getValue("Title") as String
                            val content = document.data.getValue("Content") as String
                            val date = document.data.getValue("Date") as String
                            val img = document.data.getValue("Img") as String
                            val platform = document.data.getValue("Platform") as String
                            val id = document.data.getValue("Id") as Long
                            watchListRepository.addtoWatchlistRepository(title, content, date, img, platform,id)
                        }
                    }
        }

        fun uploadImgToStorage(pathReference: String ,imgUri : Uri): StorageTask<UploadTask.TaskSnapshot> {
            val currentUser = Firebase.auth.currentUser
            try {
                if(currentUser != null) {
                    val imgPath = storageRef.child(pathReference)
                    return imgPath.putFile(imgUri)
                }else{
                    throw error(R.string.authError)
                }
            }catch (e : IllegalAccessException){
                throw e
            }

        }


        fun deleteWatchListFirebase(id: Long): Task<Void> {
            val database = FirebaseFirestore.getInstance()
            val auth = FirebaseAuth.getInstance()
            val currentUser = auth.currentUser
            try {
                if (currentUser != null) {
                     return database.collection("Admins").document("WatchList").collection("NewestTitles")
                        .document(id.toString())
                        .delete()
                        .addOnFailureListener {
                            throw error(R.string.error)
                        }
                }else{
                    throw error(R.string.authError)
                }
            }catch (e: IllegalStateException){
                throw e
            }
        }
}