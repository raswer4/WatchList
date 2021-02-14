package com.example.watchlist

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.WindowManager
import android.widget.ArrayAdapter
import android.widget.Button
import android.widget.GridView
import androidx.recyclerview.widget.RecyclerView

private const val POST_TYPE_DESC: Int = 0
private const val POST_TYPE_IMAGE: Int = 1


class PostListAdapter(var postListitem: List<Watch>): RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    class DescViewHolder(itemView: View): RecyclerView.ViewHolder(itemView){
        fun bind(watch: Watch) {


        }
    }
    class ImageViewHolder(itemView: View): RecyclerView.ViewHolder(itemView){
        fun bind(watch: Watch) {

        }
    }


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        if(viewType == POST_TYPE_DESC){
            val view = LayoutInflater.from(parent.context).inflate(R.layout.activity_watch_view, parent, false)
            return DescViewHolder(view)
        }else{
            val view = LayoutInflater.from(parent.context).inflate(R.layout.activity_watch_view, parent, false)
            return ImageViewHolder(view)
        }
    }

    override fun getItemViewType(position: Int): Int {
        return if (postListitem[position].post_type == 0L) {
            POST_TYPE_DESC
        } else {
            POST_TYPE_IMAGE
        }
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        if(getItemViewType(position)== POST_TYPE_DESC){
            (holder as DescViewHolder).bind(postListitem[position])
        }else {
            (holder as ImageViewHolder).bind(postListitem[position])
        }
    }

    override fun getItemCount(): Int {
        return postListitem.size
    }
}