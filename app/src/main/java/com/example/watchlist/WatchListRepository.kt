package com.example.watchlist
import android.content.Context
import android.net.Uri

import android.util.Log
import android.widget.TextView
import android.widget.Toast
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.ktx.Firebase
import java.net.URI
import com.google.firebase.storage.ktx.storage
import java.util.*

val watchListRepository = WatchListRepository()

class WatchListRepository{

    private val watchLists = mutableListOf<Watch>()
    private var storageRef = Firebase.storage.reference

    fun addWatchList(title: String, content: String, date: String, img: Uri,context:Context): Int{
        val id = when {
            watchLists.count() == 0 -> 1
            else -> watchLists.last().id+1
        }
        Log.d("msg",img.toString())
        val database = FirebaseFirestore.getInstance()
        val auth = FirebaseAuth.getInstance()
        val currentUser = auth.currentUser
        val watch = HashMap<String, Any>()
        try {
            watch.put("Title", title)
            watch.put("Content", content)
            watch.put("Date", date)
            uploadImgToStorage(id,img)
            watchLists.add(Watch(
                id,
                title,
                content,
                date,
                img.toString()
            ))
            database.collection("users").document(currentUser!!.uid).collection("titles").document(title).set(watch)
                .addOnSuccessListener {
                    Toast.makeText(context, "Success", Toast.LENGTH_SHORT).show()
                }
                .addOnFailureListener{
                    throw error("firestoreError")
                    Toast.makeText(context, "Failure LOL", Toast.LENGTH_SHORT).show()
                }

        }catch (e :  IllegalStateException){
            throw e
        }

        return id
    }

    fun getAllWatchLists() = watchLists

    fun getWatchListById(id: Int) =
        watchLists.find {
            it.id == id
        }

    fun deleteWatchListById(id: Int) =
        watchLists.remove(
            watchLists.find {
                it.id == id
            }
        )

    fun updateWatchListById(id: Int, newTitle: String, newContent: String, newDate: String){

        getWatchListById(id)?.run{
            title = newTitle
            content = newContent
            date = newDate
        }

    }

    fun getDataFromFirebase(context: Context){
        val database = FirebaseFirestore.getInstance()
            val auth = FirebaseAuth.getInstance()
            val currentUser = auth.currentUser

        database.collection("users").document(currentUser!!.uid).collection("titles")
            .get()
            .addOnCompleteListener{
                    Toast.makeText(context,"it worked",Toast.LENGTH_SHORT)
                    for(document in it.result!!){
                        val title = document.data.getValue("Title") as String
                        val content = document.data.getValue("Content") as String
                        val date = document.data.getValue("Date") as String
                        watchListRepository.addWatchList(title,content,date, Uri.EMPTY,context)

                }
            }.addOnSuccessListener {
                Toast.makeText(context,"it worked",Toast.LENGTH_SHORT).show()
                }.addOnFailureListener {
                    Toast.makeText(context,"it did not work worked",Toast.LENGTH_SHORT).show()
                }

    }

    fun uploadImgToStorage(id: Int ,imgUrl : Uri){
        val user = Firebase.auth.currentUser
        if(user != null) {
            val imgPath = storageRef.child("images/${user.uid}/$id")
            imgPath.putFile(imgUrl).addOnFailureListener{
                throw error("error")
            }
        }else{
            throw error("authError")
        }
    }



}