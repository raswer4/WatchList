package com.example.watchlist.sampledata

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.ViewGroup
import com.example.watchlist.R
import com.example.watchlist.databinding.FragmentNewestTitlesBinding
import com.example.watchlist.databinding.FragmentUserListBinding


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