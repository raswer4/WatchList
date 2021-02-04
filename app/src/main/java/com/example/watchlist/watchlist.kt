package com.example.watchlist

data class WatchList(
        val id: Int,
        var title: String,
        var content: String,
        var date: Int
){

    override fun toString() = title

}