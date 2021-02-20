package com.example.watchlist.sampledata

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.ListView
import android.widget.inline.InlineContentView
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.watchlist.R
import com.example.watchlist.TitleAdapter
import com.example.watchlist.Watch
import com.example.watchlist.databinding.FragmentNewestTitlesBinding
import com.example.watchlist.databinding.FragmentUserListBinding
import com.firebase.ui.firestore.FirestoreRecyclerOptions
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.CollectionReference
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.Query


class NewestTitlesFragment : Fragment() {

    lateinit var binding: FragmentNewestTitlesBinding

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?

    ) =FragmentNewestTitlesBinding.inflate(inflater, container, false).run {
        binding = this

        root
    }

}