package com.example.watchlist
import android.content.Context
import android.net.Uri
import android.util.Log
import android.widget.Toast
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.ktx.storage
import java.util.*

val watchListRepository = WatchListRepository()

class WatchListRepository{

    private val watchLists = mutableListOf<Watch>()
    private var storageRef = Firebase.storage.reference


    fun addWatchList(title: String, content: String, date: String, img: Uri, context: Context): Int{
        val id = when {
            watchLists.count() == 0 -> 1
            else -> watchLists.last().id+1
        }

        val database = FirebaseFirestore.getInstance()
        val auth = FirebaseAuth.getInstance()
        val currentUser = auth.currentUser
        val watch = HashMap<String, Any>()
        try {
            watch.put("Title", title)
            watch.put("Content", content)
            watch.put("Date", date)
            watch.put("Id", id.toString())

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
                    Toast.makeText(context, "Successfully Created A Watch List", Toast.LENGTH_SHORT).show()
                }
                .addOnFailureListener{

                    Toast.makeText(context, "Couln't Create A Watch List ", Toast.LENGTH_SHORT).show()
                    throw error("firestoreError")
                }

        }catch (e :  IllegalStateException){
            throw e
        }

        return id
    }

    fun addtoWachlistRepository(title: String, content: String, date: String, img: String, id: Int){
        watchLists.add(
            Watch(
                id,
                title,
                content,
                date,
                img
            )
        )
    }


    fun getAllWatchLists() = watchLists

    fun getWatchListById(id: Int) =
        watchLists.find {
            it.id == id
        }

    fun deleteWatchListById(id: Int) =
        watchLists.remove(
            watchLists.find {
                val database = FirebaseFirestore.getInstance()
                val auth = FirebaseAuth.getInstance()
                val currentUser = auth.currentUser

                database.collection("users").document(currentUser!!.uid).collection("titles")
                    .document(
                        id.toString()
                    )
                    .delete()
                    /*.addOnCompleteListener {
                        Toast.makeText(context, "Successfully Deleted The Watch List", Toast.LENGTH_SHORT).show()
                    }
                    .addOnFailureListener {
                         Toast.makeText(context, "Couln't Delete", Toast.LENGTH_SHORT).show()
                    }*/

                it.id == id
            }
        )

    fun updateWatchListById(
        id: Int,
        newTitle: String,
        newContent: String,
        newDate: String,
        context: Context
    ){
        val database = FirebaseFirestore.getInstance()
        val auth = FirebaseAuth.getInstance()
        val currentUser = auth.currentUser
        val watch = HashMap<String, Any>()

        getWatchListById(id)?.run{
            title = newTitle
            content = newContent
            date = newDate
        }

        database.collection("users").document(currentUser!!.uid).collection("titles").document(id.toString())
            .update(
                "Title", newTitle,
                "Content", newContent,
                "Date", newDate
            )
            .addOnCompleteListener{
                Toast.makeText(context, "Success", Toast.LENGTH_SHORT).show()
                }

        }

    fun getDataFromFirebase(context: Context){
        val database = FirebaseFirestore.getInstance()
            val auth = FirebaseAuth.getInstance()
            val currentUser = auth.currentUser

        database.collection("users").document(currentUser!!.uid).collection("titles")
            .get()
            .addOnCompleteListener{
                    Toast.makeText(context, "it worked", Toast.LENGTH_SHORT)
                    for(document in it.result!!){
                        val title = document.data.getValue("Title") as String
                        val content = document.data.getValue("Content") as String
                        val date = document.data.getValue("Date") as String
                        val id = document.data.getValue("Id") as String

                        Log.d("msg",id)
                        watchListRepository.addtoWachlistRepository(title, content, date, "", id.toInt())
                       // watchListRepository.addWatchList(title,content,date, Uri.EMPTY,context)

                }
            }.addOnSuccessListener {
                Toast.makeText(context, "it worked", Toast.LENGTH_SHORT).show()
                }.addOnFailureListener {
                    Toast.makeText(context, "it did not work worked", Toast.LENGTH_SHORT).show()
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