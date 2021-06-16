package com.example.watchlist

import android.net.Uri
import android.util.Log

import com.google.android.gms.tasks.Task
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.QuerySnapshot
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.StorageTask
import com.google.firebase.storage.UploadTask
import com.google.firebase.storage.ktx.storage
import java.util.*


open class WatchlistFirebase {


    private var storageRef = Firebase.storage.reference

    fun addWatchListFirebase(id:Long,title: String, content: String, date: String, platform: String){

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
                watch["Img"] = "images/${currentUser.uid}/$id"
                database.collection("Users").document(currentUser.uid).collection("Titles")
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

    fun addWatchListFirebase(id:Long,title: String, content: String, date: String, img: String, platform: String): Task<Void> {

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
                watch["Img"] = img
               return database.collection("Users").document(currentUser.uid).collection("Titles")
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
        newImg: Uri,
    ): StorageTask<UploadTask.TaskSnapshot> {
        val database = FirebaseFirestore.getInstance()
        val auth = FirebaseAuth.getInstance()
        val currentUser = auth.currentUser
        val watch = HashMap<String, Any>()
        try {
            if (currentUser != null) {
                watch["Title"] = newTitle
                watch["Content"] = newContent
                watch["Date"] = newDate
                watch["Id"] = id
                watch["Platform"] = newPlatform
                watch["Img"] = "images/${currentUser.uid}/$id"
                return  uploadImgToStorage("images/${currentUser.uid}/$id", newImg).addOnSuccessListener {
                    database.collection("Users").document(currentUser.uid).collection("Titles")
                        .document(id.toString())
                        .set(watch)
                        .addOnFailureListener {
                            throw error(R.string.error)
                        }
                }
            } else {
                throw error(R.string.authError)
            }
        } catch (e: IllegalStateException) {
            throw e
        }
    }


    fun updateWatchListFirebase(
        id: Long,
        newTitle: String,
        newContent: String,
        newDate: String,
        newPlatform: String,
        newImg: String,
    ) {
        val database = FirebaseFirestore.getInstance()
        val auth = FirebaseAuth.getInstance()
        val currentUser = auth.currentUser
        val watch = HashMap<String, Any>()
        try {
            if (currentUser != null) {
                watch["Title"] = newTitle
                watch["Content"] = newContent
                watch["Date"] = newDate
                watch["Id"] = id
                watch["Platform"] = newPlatform
                watch["Img"] = newImg

                database.collection("Users").document(currentUser.uid).collection("Titles")
                    .document(id.toString())
                    .set(watch)
                    .addOnFailureListener {
                        throw error(R.string.error)
                    }
            } else {
                throw error(R.string.authError)
            }
        } catch (e: IllegalStateException) {
            throw e
        }
    }


    fun getDataFromFirebase(): Task<QuerySnapshot> {
        val database = FirebaseFirestore.getInstance()
        val auth = FirebaseAuth.getInstance()
        val currentUser = auth.currentUser
        val databaseRef = database.collection("Users").document(currentUser!!.uid).collection("Titles")

        return databaseRef.get().addOnCompleteListener {

            for (document in it.result!!) {
                val title = document.data.getValue("Title") as String
                val content = document.data.getValue("Content") as String
                val date = document.data.getValue("Date") as String
                val img = document.data.getValue("Img") as String
                val platform = document.data.getValue("Platform") as String
                val id = document.data.getValue("Id") as Long
                Log.d("msg",id.toString())
                watchListRepository.addtoWatchlistRepository(
                    title,
                    content,
                    date,
                    img,
                    platform,
                    id
                )

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


    fun deleteWatchListFirebase(imgRef: String, id:Long) {
        val database = FirebaseFirestore.getInstance()
        val auth = FirebaseAuth.getInstance()
        val currentUser = auth.currentUser
        try {
            if (currentUser != null) {
                database.collection("Users").document(currentUser.uid).collection("Titles")
                    .document(id.toString())
                    .delete()
                    .addOnFailureListener {
                        throw error(R.string.error)
                    }
                if(!imgRef.contains("adminImg/")){
                    val imgPath = storageRef.child(imgRef)
                    imgPath.delete().addOnFailureListener{
                        throw error(it)
                    }
                }
            }else{
                throw error(R.string.authError)
            }
        }catch (e: IllegalStateException){
            throw e
        }

    }
}