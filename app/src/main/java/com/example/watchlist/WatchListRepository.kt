package com.example.watchlist
import android.content.Context
import android.util.Log
import android.widget.TextView
import android.widget.Toast
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import java.util.*

val watchListRepository = WatchListRepository()

class WatchListRepository{

    private val watchLists = mutableListOf<Watch>()

    fun addWatchList(title: String, content: String, date: String, img: String,context:Context): Int{
        val id = when {
            watchLists.count() == 0 -> 1
            else -> watchLists.last().id+1
        }

        val database = FirebaseFirestore.getInstance()
        val auth = FirebaseAuth.getInstance()
        val currentUser = auth.currentUser
        val watch = HashMap<String, Any>()


        watch.put("Title", title)
        watch.put("Content", content)
        watch.put("Date", date)

        watchLists.add(Watch(
                id,
                title,
                content,
                date,
                img
        ))

        database.collection("users").document(currentUser!!.uid).collection("titles").document(title).set(watch)
                .addOnSuccessListener {
                    Toast.makeText(context, "Success", Toast.LENGTH_SHORT).show()
                }
                .addOnFailureListener{
                    Toast.makeText(context, "Failure LOL", Toast.LENGTH_SHORT).show()
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
                        watchListRepository.addWatchList(title,content,date,"",context)

                }
            }.addOnSuccessListener {
                Toast.makeText(context,"it worked",Toast.LENGTH_SHORT).show()
                }.addOnFailureListener {
                    Toast.makeText(context,"it did not work worked",Toast.LENGTH_SHORT).show()
                }

    }



}