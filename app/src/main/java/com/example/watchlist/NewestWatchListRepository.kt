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

    private val adminWatchLists = mutableListOf<Watch>()
    private var storageRef = Firebase.storage.reference


    fun addAdminsWatchList(title: String, content: String, date: String, platform: String ,img: Uri, context: Context): Long{
        val id = when {
            adminWatchLists.count() == 0 -> 1
            else -> adminWatchLists.last().Id+1
        }

        val database = FirebaseFirestore.getInstance()
        val auth = FirebaseAuth.getInstance()
        val currentUser = auth.currentUser
        val adminWatch = HashMap<String, Any>()
        if (currentUser != null) {
            try {

                adminWatch.put("Title", title)
                adminWatch.put("Content", content)
                adminWatch.put("Date", date)
                adminWatch.put("Id", id)
                adminWatch.put("Platform", platform)
                adminWatch.put("Img", "images/${currentUser.uid}/$id")
                database.collection("Admins").document("WatchList").collection("NewestTitles")
                    .document(id.toString())
                    .set(adminWatch)
                    .addOnFailureListener {
                        throw error(R.string.error)
                    }
                adminWatchLists.add(
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

    private fun addtoAdminsWatchlistRepository(id: Long, title: String, content: String, date: String, platform: String, img: String){
        adminWatchLists.add(
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


    fun getAllAdminWatchLists() = adminWatchLists

    fun getAdminsWatchListById(id: Long) =
        adminWatchLists.find {
            it.Id == id
        }

    fun deleteAdminWatchListById(id: Long) {
        val database = FirebaseFirestore.getInstance()
        val auth = FirebaseAuth.getInstance()
        val currentUser = auth.currentUser
        try {
            if (currentUser != null) {
                database.collection("Admins").document("WatchList").collection("NewestTitles")
                    .document(id.toString())
                    .delete()
                    .addOnFailureListener {
                        throw error(R.string.error)
                    }
                val imgPath = storageRef.child("images/${currentUser.uid}/$id")
                imgPath.delete().addOnFailureListener{
                    throw error(R.string.error)
                }
                adminWatchLists.remove(
                    adminWatchLists.find {
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
        newPlatform: String,
        newImg: Uri,
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
                        "Date", newDate,
                        "Platform", newPlatform,
                        "Img", newImg
                    ).addOnFailureListener{
                        throw error(R.string.error)
                    }
                    .addOnCompleteListener{
                        Toast.makeText(context, "Success", Toast.LENGTH_SHORT).show()
                    }
                getAdminsWatchListById(id)?.run{
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
                    val platform = document.data.getValue("Platform") as String
                    val id = document.data.getValue("Id") as Long

                    newestWatchListRepository.addtoAdminsWatchlistRepository(id, title, content, date, img, platform)

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