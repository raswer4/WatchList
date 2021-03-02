package com.example.watchlist.sampledata


import android.content.ContentValues.TAG
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.watchlist.*
import com.example.watchlist.databinding.FragmentUserListBinding
import com.firebase.ui.firestore.FirestoreRecyclerAdapter
import com.firebase.ui.firestore.FirestoreRecyclerOptions
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.CollectionReference
import com.google.firebase.firestore.DocumentChange
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.ktx.storage
import com.squareup.picasso.Picasso
import com.google.firebase.firestore.DocumentSnapshot
import com.google.firebase.firestore.remote.WatchChange


class UserListFragment : Fragment() {

    class WatchViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView)



    companion object{
        lateinit var binding: FragmentUserListBinding
        lateinit var button: Button
        private var storageRef = Firebase.storage.reference
        val auth = FirebaseAuth.getInstance()
        val currentUser = auth.currentUser
    }


    override fun onCreateView(
            inflater: LayoutInflater,
            container: ViewGroup?,
            savedInstanceState: Bundle?
    )= FragmentUserListBinding.inflate(inflater, container, false).run {
        binding = this

        val db: FirebaseFirestore = FirebaseFirestore.getInstance()
        val collectionReference: CollectionReference = db.collection("Users").document(currentUser!!.uid).collection("Titles")
        val options : FirestoreRecyclerOptions<Watch> = FirestoreRecyclerOptions.Builder<Watch>().setQuery(collectionReference, Watch::class.java)
                .setLifecycleOwner(this@UserListFragment).build()

        val adapter = object: FirestoreRecyclerAdapter<Watch, WatchViewHolder>(options){

            override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): WatchViewHolder {
                val view : View = LayoutInflater.from(parent.context).inflate(
                    R.layout.titles_layout,
                    parent,
                    false
                )
                return WatchViewHolder(view)
            }

            override fun onBindViewHolder(holder: WatchViewHolder, position: Int, model: Watch) {

                val movieTitle : TextView = holder.itemView.findViewById(R.id.movieTitle)
                val moviePlatform : TextView = holder.itemView.findViewById(R.id.moviePlatform)
                val moviePoster : ImageView = holder.itemView.findViewById(R.id.moviePoster)
                val movieDate : TextView = holder.itemView.findViewById(R.id.movieDate)


                val imgReference = model.Img
                val pathReference = storageRef.child(imgReference)
                pathReference.downloadUrl.addOnSuccessListener{
                    Picasso.get().load(it).into(moviePoster)
                    //moviePoster.setImageURI(it)
                }.addOnFailureListener{
                    Toast.makeText(activity,getString(R.string.downloadError), Toast.LENGTH_SHORT).show()
                }

                movieTitle.text = model.Title
                moviePlatform.text = model.Platform
                movieDate.text = model.Date

                holder.itemView.setOnClickListener{
                    var intent = Intent(context, WatchViewActivity::class.java).apply {
                        putExtra("id", model.Id)
                    }
                    startActivity(intent)
                }
            }

        }
        userListRecyclerView.adapter = adapter
        userListRecyclerView.layoutManager = LinearLayoutManager(context)
        adapter.startListening()
        root
    }



    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

            binding.addToUserList.setOnClickListener {
                val i = Intent(activity, CreateWatchListActivity::class.java)
                activity?.startActivity(i)
            }

    }

}
