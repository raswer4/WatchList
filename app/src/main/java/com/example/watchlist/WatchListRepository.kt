package com.example.watchlist
import android.content.Context
import android.net.Uri
import android.widget.Toast
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.ktx.storage
import java.util.*

val watchListRepository = WatchListRepository()

class WatchListRepository {

    private val watchLists = mutableListOf<Watch>()
    private var storageRef = Firebase.storage.reference


    fun addWatchList(title: String, content: String, date: String, img: Uri, platform: String,context: Context): Long{
        val id = when {
            watchLists.count() == 0 -> 1
            else -> watchLists.last().Id+1
        }

        val database = FirebaseFirestore.getInstance()
        val auth = FirebaseAuth.getInstance()
        val currentUser = auth.currentUser
        val watch = HashMap<String, Any>()
        if (currentUser != null) {
            try {

                watch.put("Title", title)
                watch.put("Content", content)
                watch.put("Date", date)
                watch.put("Id", id)
                watch.put("Platform", platform)
                watch.put("Img", "images/${currentUser.uid}/$id")
                database.collection("Users").document(currentUser.uid).collection("Titles")
                    .document(id.toString())
                    .set(watch)
                    .addOnFailureListener {
                        throw error(R.string.error)
                    }
                uploadImgToStorage(id, img)
                watchLists.add(
                    Watch(
                        id,
                        title,
                        content,
                        date,
                        platform,
                        "images/${currentUser.uid}/$id"
                    )
                )


            } catch (e: IllegalStateException) {
                throw e
            }
        } else {
            throw error(R.string.authError)
        }

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


    fun getAllWatchLists() = watchLists

    fun getWatchListById(id: Long) =
        watchLists.find {
            it.Id == id
        }

    fun deleteWatchListById(id: Long) {
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
                val imgPath = storageRef.child("images/${currentUser.uid}/$id")
                imgPath.delete().addOnFailureListener{
                    throw error(R.string.error)
                }
                watchLists.remove(
                    watchLists.find {
                        it.Id == id
                    }
                )
            }else{
                throw error(R.string.authError)
            }
        }catch (e: IllegalStateException){
            throw e
        }

    }
    fun updateWatchListById(
        id: Long,
        newTitle: String,
        newContent: String,
        newDate: String,
        newPlatform: String,
        newImg: Uri,
        context: Context
    ){
        val database = FirebaseFirestore.getInstance()
        val auth = FirebaseAuth.getInstance()
        val currentUser = auth.currentUser
        try{
            if(currentUser != null){
                database.collection("Users").document(currentUser.uid).collection("titles").document(id.toString())
                    .update(
                        "Title", newTitle,
                        "Content", newContent,
                        "Date", newDate,
                        "Platform", newPlatform,
                        "Img", newImg
                    ).addOnFailureListener{
                        throw error(R.string.error)
                    }
                    .addOnCompleteListener{
                        Toast.makeText(context, "Success", Toast.LENGTH_SHORT).show()
                    }
                uploadImgToStorage(id,newImg)
                getWatchListById(id)?.run{
                    Title = newTitle
                    Content = newContent
                    Date = newDate
                    Platform = newPlatform
                }
            }else{
                throw error(R.string.authError)
            }
        }catch (e: IllegalStateException){
            throw e
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
                    val img = document.data.getValue("Img") as String
                    val platform = document.data.getValue("Platform") as String
                    val id = document.data.getValue("Id") as Long

                    watchListRepository.addtoWatchlistRepository(title, content, date, img, platform, id)

                }
            }.addOnSuccessListener {
                Toast.makeText(context, "it worked", Toast.LENGTH_SHORT).show()
            }.addOnFailureListener {
                Toast.makeText(context, "it did not work worked", Toast.LENGTH_SHORT).show()
            }

    }

    fun uploadImgToStorage(id: Long ,imgUrl : Uri){
        val currentUser = Firebase.auth.currentUser
        if(currentUser != null) {
            val imgPath = storageRef.child("images/${currentUser.uid}/$id")
            imgPath.putFile(imgUrl).addOnFailureListener{
                throw error(R.string.error)
            }
        }else{
            throw error(R.string.authError)
        }
    }
}