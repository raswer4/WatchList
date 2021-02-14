package com.example.watchlist

import android.net.Uri

data class Watch(
    val id : Int = 0,
    var title :String = "",
    var content:String = "",
    var date:Int = 0,
    var img:String = "",
    val post_type: Long = 0
    )
