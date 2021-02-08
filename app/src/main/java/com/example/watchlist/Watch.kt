package com.example.watchlist

import android.net.Uri

data class Watch(
    var title :String,
    var content:String,
    var date:Int,
    val id : Int,
    var img:Uri
    ) {
    override fun toString() = title
}
