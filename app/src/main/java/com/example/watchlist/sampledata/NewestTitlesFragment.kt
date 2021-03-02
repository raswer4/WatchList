package com.example.watchlist.sampledata

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.core.view.isVisible
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.watchlist.*
import com.example.watchlist.databinding.FragmentNewestTitlesBinding
import com.firebase.ui.firestore.FirestoreRecyclerAdapter
import com.firebase.ui.firestore.FirestoreRecyclerOptions
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.CollectionReference
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.ktx.storage
import com.squareup.picasso.Picasso


class NewestTitlesFragment : Fragment() {

    class WatchViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView)

    lateinit var binding: FragmentNewestTitlesBinding
    private var storageRef = Firebase.storage.reference

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?

    ) =FragmentNewestTitlesBinding.inflate(inflater, container, false).run {
        binding = this

        val auth = FirebaseAuth.getInstance()
        val currentUser = auth.currentUser
        val raswer = "13IYtYt9Eme67jx8TmeAF5C5Kt33"
        val ahmed = "nIGbMcoycoXMDvF6EuKFjnpZSiB3"
        val michael = "Dm5iWuHXvMMwrHbDmhu6ssjDXzm2"
        val currentUserId = currentUser?.uid

        if(currentUserId.toString() == raswer || currentUserId.toString() == ahmed || currentUserId.toString() == michael){ addButtonContainer.isVisible = true}

        val db: FirebaseFirestore = FirebaseFirestore.getInstance()
        val collectionReference: CollectionReference = db.collection("Admins").document("WatchList").collection("NewestTitles")
        val options : FirestoreRecyclerOptions<Watch> = FirestoreRecyclerOptions.Builder<Watch>().setQuery(collectionReference, Watch::class.java)
                .setLifecycleOwner(this@NewestTitlesFragment).build()

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
                }.addOnFailureListener{
                    Toast.makeText(activity,getString(R.string.downloadError), Toast.LENGTH_SHORT).show()
                }

                movieTitle.text = model.Title
                moviePlatform.text = model.Platform
                movieDate.text = model.Date

                holder.itemView.setOnClickListener{
                    var intent = Intent(context, WatchAdminViewActivity::class.java).apply {
                        putExtra("newestId", model.Id)
                    }
                    startActivity(intent)
                }
            }
        }
        newestTitlesRecycleView.adapter = adapter
        newestTitlesRecycleView.layoutManager = LinearLayoutManager(context)
        adapter.startListening()

        addToNewestTitles.setOnClickListener{
            var intent = Intent(context, CreateNewestWatchListActivity::class.java).apply {
            }
            startActivity(intent)
        }
        root


    }
}