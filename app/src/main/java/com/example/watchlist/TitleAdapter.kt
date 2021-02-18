package com.example.watchlist

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.firebase.ui.firestore.FirestoreRecyclerAdapter
import com.firebase.ui.firestore.FirestoreRecyclerOptions


class TitleAdapter(options: FirestoreRecyclerOptions<Watch>) :
    FirestoreRecyclerAdapter<Watch, TitleAdapter.TitleAdapterVH>(options) {



    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TitleAdapterVH {
        return TitleAdapterVH(LayoutInflater.from(parent.context).inflate(R.layout.titles_layout, parent, false))
    }

    override fun onBindViewHolder(holder: TitleAdapterVH, position: Int, model: Watch) {

        holder.movieTitle.text = model.title

    }

    class TitleAdapterVH(itemView: View) : RecyclerView.ViewHolder(itemView) {

        var movieTitle = itemView.findViewById<TextView>(R.id.movieTitle)
        var moviePoster = itemView.findViewById<ImageView>(R.id.titlePoster)
    }

}