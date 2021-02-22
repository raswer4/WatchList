package com.example.watchlist.sampledata

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.watchlist.NewestWatchListViewActivity
import com.example.watchlist.R
import com.example.watchlist.Watch
import com.example.watchlist.WatchViewActivity
import com.example.watchlist.databinding.FragmentNewestTitlesBinding
import com.firebase.ui.firestore.FirestoreRecyclerAdapter
import com.firebase.ui.firestore.FirestoreRecyclerOptions
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.CollectionReference
import com.google.firebase.firestore.DocumentReference
import com.google.firebase.firestore.FirebaseFirestore


class NewestTitlesFragment : Fragment() {

    class NewestWatchViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView)

    lateinit var binding: FragmentNewestTitlesBinding

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?

    ) =FragmentNewestTitlesBinding.inflate(inflater, container, false).run {
        binding = this

        val db: FirebaseFirestore = FirebaseFirestore.getInstance()
        val collectionReference: CollectionReference = db.collection("Admins").document("WatchList").collection("NewestList")
        val options : FirestoreRecyclerOptions<Watch> = FirestoreRecyclerOptions.Builder<Watch>().setQuery(collectionReference, Watch::class.java)
                .setLifecycleOwner(this@NewestTitlesFragment).build()

        val adapter = object: FirestoreRecyclerAdapter<Watch, UserListFragment.WatchViewHolder>(options){

            override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): UserListFragment.WatchViewHolder {
                val view : View = LayoutInflater.from(parent.context).inflate(
                        R.layout.titles_layout,
                        parent,
                        false
                )
                return UserListFragment.WatchViewHolder(view)
            }

            override fun onBindViewHolder(holder: UserListFragment.WatchViewHolder, position: Int, model: Watch) {

                var movieTitle : TextView = holder.itemView.findViewById(R.id.movieTitle)
                //var moviePoster : ImageView = holder.itemView.findViewById(R.id.movieImage)

                movieTitle.text = model.Title

                holder.itemView.setOnClickListener{
                    var intent = Intent(context, NewestWatchListViewActivity::class.java).apply {
                        putExtra("newTitlesID", model.Id)
                    }
                    startActivity(intent)
                }
            }
        }
        newestTitlesRecycleView.adapter = adapter
        newestTitlesRecycleView.layoutManager = LinearLayoutManager(context)
        adapter.startListening()
        root
    }
}