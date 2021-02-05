package com.example.watchlist

data class Watch(
        val id: Int,
        var title: String,
        var content: String,
        var date: Int
){

    override fun toString() = title

}