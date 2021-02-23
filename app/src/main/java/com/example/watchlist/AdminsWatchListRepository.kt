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

val newestWatchListRepository = NewestWatchListRepository()

class NewestWatchListRepository {

    private val watchLists = mutableListOf<Watch>()
    private var storageRef = Firebase.storage.reference


    fun addAdminsWatchList(title: String, content: String, date: String, img: Uri, context: Context): Long{
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
                watch.put("Img", "images/${currentUser.uid}/$id")
                database.collection("Admins").document("WatchList").collection("NewestTitles")
                    .document(id.toString()).set(watch).addOnFailureListener {
                        throw error(R.string.error)
                    }
                uploadImgToAdminsStorage(id, img)
                watchLists.add(
                    Watch(
                        id,
                        title,
                        content,
                        date,
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

    fun addtoAdminsWachlistRepository(title: String, content: String, date: String, img: String, id: Long){
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

    fun getAdminsWatchListById(id: Long) =
        watchLists.find {
            it.Id == id
        }

    fun deleteAdminWatchListById(id: Long) {
        val database = FirebaseFirestore.getInstance()
        val auth = FirebaseAuth.getInstance()
        val currentUser = auth.currentUser
        try {
            if (currentUser != null) {
                database.collection("Admins").document("WatchList").collection("NewestTitles")
                    .document(
                        id.toString()
                    ).delete().addOnFailureListener {
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
    fun updateAdminsWatchListById(
        id: Long,
        newTitle: String,
        newContent: String,
        newDate: String,
        img: Uri,
        context: Context
    ){
        val database = FirebaseFirestore.getInstance()
        val auth = FirebaseAuth.getInstance()
        val currentUser = auth.currentUser
        val watch = HashMap<String, Any>()
        try{
            if(currentUser != null){
                database.collection("Admins").document("WatchList").collection("NewestTitles").document(id.toString())
                    .update(
                        "Title", newTitle,
                        "Content", newContent,
                        "Date", newDate
                    ).addOnFailureListener{
                        throw error(R.string.error)
                    }
                    .addOnCompleteListener{
                        Toast.makeText(context, "Success", Toast.LENGTH_SHORT).show()
                    }
                uploadImgToAdminsStorage(id,img)
                getAdminsWatchListById(id)?.run{
                    Title = newTitle
                    Content = newContent
                    Date = newDate
                }
            }else{
                throw error(R.string.authError)
            }
        }catch (e: IllegalStateException){
            throw e
        }



    }

    fun getDataFromAdminsFirebase(context: Context){
        val database = FirebaseFirestore.getInstance()
        val auth = FirebaseAuth.getInstance()
        val currentUser = auth.currentUser

        database.collection("Admins").document("WatchList").collection("NewestTitles")
            .get()
            .addOnCompleteListener{
                Toast.makeText(context, "it worked", Toast.LENGTH_SHORT)
                for(document in it.result!!){
                    val title = document.data.getValue("Title") as String
                    val content = document.data.getValue("Content") as String
                    val date = document.data.getValue("Date") as String
                    val img = document.data.getValue("Img") as String
                    val id = document.data.getValue("Id") as Long

                    newestWatchListRepository.addtoAdminsWachlistRepository(title, content, date, img, id)

                }
            }.addOnSuccessListener {
                Toast.makeText(context, "it worked", Toast.LENGTH_SHORT).show()
            }.addOnFailureListener {
                Toast.makeText(context, "it did not work worked", Toast.LENGTH_SHORT).show()
            }

    }

    fun uploadImgToAdminsStorage(id: Long, imgUrl : Uri){
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