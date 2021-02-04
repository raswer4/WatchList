package com.example.watchlist

import androidx.appcompat.app.AppCompatActivity
import java.util.*


class ToDoRepository{

    private val watchLists = mutableListOf<WatchList>()

    fun addWatchList(title: String, content: String, date: Int): Int{
        val id = when {
            watchLists.count() == 0 -> 1
            else -> watchLists.last().id+1
        }
        watchLists.add(WatchList(
                id,
                title,
                content,
                date

        ))
        return id
    }

    fun getAllWatchLists() = watchLists

    fun getWatchListById(id: Int) =
            watchLists.find {
                it.id == id
            }

    fun deleteWatchListById(id: Int) =
            watchLists.remove(
                    watchLists.find {
                        it.id == id
                    }
            )

    fun updateWatchListById(id: Int, newTitle: String, newContent: String, newDate: Int){

        getWatchListById(id)?.run{
            title = newTitle
            content = newContent
            date = newDate
        }

    }

}