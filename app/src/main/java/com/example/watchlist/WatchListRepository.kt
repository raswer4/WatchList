package com.example.watchlist
import android.content.Context
import android.util.Log
import android.widget.Toast
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import java.util.*


val watchListRepository = WatchListRepository()

class WatchListRepository{

    private val watchLists = mutableListOf<Watch>()


    fun addWatchList(title: String, content: String, date: String, img: String, context: Context): Int{
        val id = when {
            watchLists.count() == 0 -> 1
            else -> watchLists.last().Id+1
        }

        val database = FirebaseFirestore.getInstance()
        val auth = FirebaseAuth.getInstance()
        val currentUser = auth.currentUser
        val watch = HashMap<String, Any>()


        watch.put("Title", title)
        watch.put("Content", content)
        watch.put("Date", date)
        watch.put("Id", id)

        watchLists.add(
            Watch(
                id,
                title,
                content,
                date,
                img
            )
        )

        database.collection("Users").document(currentUser!!.uid).collection("Titles").document(id.toString()).set(
            watch
        )
                .addOnSuccessListener {
                    Toast.makeText(context, "Successfully Created A Watch List", Toast.LENGTH_SHORT).show()
                }
                .addOnFailureListener{
                    Toast.makeText(context, "Couln't Create A Watch List ", Toast.LENGTH_SHORT).show()
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
            it.Id == id
        }

    fun deleteWatchListById(id: Int, context: Context) =
        watchLists.remove(
            watchLists.find {
                val database = FirebaseFirestore.getInstance()
                val auth = FirebaseAuth.getInstance()
                val currentUser = auth.currentUser

                database.collection("Users").document(currentUser!!.uid).collection("Titles")
                    .document(
                        id.toString()
                    )
                    .delete()
                    .addOnCompleteListener {
                        Toast.makeText(context, "Successfully Deleted The Watch List", Toast.LENGTH_SHORT).show()
                    }
                    .addOnFailureListener {
                         Toast.makeText(context, "Couln't Delete", Toast.LENGTH_SHORT).show()
                    }

                it.Id == id
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
            Title = newTitle
            Content = newContent
            Date = newDate
        }

        database.collection("Users").document(currentUser!!.uid).collection("Titles").document(id.toString())
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

        database.collection("Users").document(currentUser!!.uid).collection("Titles")
            .get()
            .addOnCompleteListener{
                    Toast.makeText(context, "it worked", Toast.LENGTH_SHORT)
                    for(document in it.result!!){
                        val title = document.data.getValue("Title") as String
                        val content = document.data.getValue("Content") as String
                        val date = document.data.getValue("Date") as String
                        val id = document.data.getValue("Id") as Int

                        watchListRepository.addtoWachlistRepository(title, content, date, "", id)

                }
            }.addOnSuccessListener {
                Toast.makeText(context, "it worked", Toast.LENGTH_SHORT).show()
                }.addOnFailureListener {
                    Toast.makeText(context, "it did not work worked", Toast.LENGTH_SHORT).show()
                }

    }

}

