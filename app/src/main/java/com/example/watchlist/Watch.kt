package com.example.watchlist

import android.net.Uri

data class Watch(
    val id : Int,
    var title :String,
    var content:String,
    var date:Int,
    var img:String
    ) {
    override fun toString() = title
}
