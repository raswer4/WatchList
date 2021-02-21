package com.example.watchlist.sampledata


import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.watchlist.*
import com.example.watchlist.databinding.FragmentUserListBinding
import com.firebase.ui.firestore.FirestoreRecyclerAdapter
import com.firebase.ui.firestore.FirestoreRecyclerOptions
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.CollectionReference
import com.google.firebase.firestore.FirebaseFirestore


class UserListFragment : Fragment() {

    class WatchViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView)

    lateinit var binding: FragmentUserListBinding
    lateinit var button: Button

    override fun onCreateView(
            inflater: LayoutInflater,
            container: ViewGroup?,
            savedInstanceState: Bundle?
    )= FragmentUserListBinding.inflate(inflater, container, false).run {
        binding = this

        val auth = FirebaseAuth.getInstance()
        val currentUser = auth.currentUser
        val db: FirebaseFirestore = FirebaseFirestore.getInstance()
        val collectionReference: CollectionReference = db.collection("users").document(currentUser!!.uid).collection("titles")
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

                var movieTitle : TextView = holder.itemView.findViewById(R.id.movieTitle)
                var moviePoster : ImageView = holder.itemView.findViewById(R.id.movieImage)

                movieTitle.text = model.title
            }
        }
        userListRecyclerView.adapter = adapter
        userListRecyclerView.layoutManager = LinearLayoutManager(context)


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
