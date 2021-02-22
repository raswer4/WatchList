package com.example.watchlist

import android.app.DatePickerDialog
import android.app.TimePickerDialog
import android.content.Intent
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.WindowManager
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.annotation.RequiresApi
import java.lang.Exception
import java.text.SimpleDateFormat
import java.time.format.DateTimeFormatter
import java.util.*
import kotlin.reflect.typeOf

class CreateWatchListActivity : AppCompatActivity() {


   companion object {
      private var Format = SimpleDateFormat("dd MMM, YYYY", Locale.US)
      private var timeFormat = SimpleDateFormat("hh:mm a", Locale.US)

    }

    @RequiresApi(Build.VERSION_CODES.N)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        supportActionBar?.hide()
        window.setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);

        setContentView(R.layout.activity_create_watch_list)
        val createWatchButton = this.findViewById<Button>(R.id.createWatchListBtn)
        val createDateBtn = this.findViewById<Button>(R.id.createDateBtn)
        val createTimeBtn = this.findViewById<Button>(R.id.createTimeBtn)


        createWatchButton.setOnClickListener(){
            val watchTitle = this.findViewById<EditText>(R.id.titleEditText).editableText.toString().trim()
            val watchContent = this.findViewById<EditText>(R.id.contentEditText).editableText.toString().trim()
            val watchDate = this.findViewById<Button>(R.id.createDateBtn).editableText.toString().trim()

            val id = watchListRepository.addWatchList(watchTitle,watchContent,watchDate,"hello",this)
            Toast.makeText(this, id.toString(), Toast.LENGTH_SHORT).show()

            val intent = Intent(this, WatchViewActivity::class.java).apply {
                putExtra("id", id)
            }
            startActivity(intent)
            finish()
        }

        createDateBtn.setOnClickListener {
            val calender = Calendar.getInstance()
            val selectedDate = Calendar.getInstance()
            var date = Format.format(selectedDate.time).toString()
            val datePicker = DatePickerDialog(
                this,
                DatePickerDialog.OnDateSetListener { view, year, month, dayOfMonth ->
                    selectedDate.set(Calendar.YEAR, year)
                    selectedDate.set(Calendar.MONTH, month)
                    selectedDate.set(Calendar.DAY_OF_MONTH, dayOfMonth)
                    date = Format.format(selectedDate.time).toString()

                    createDateBtn.text = date
                    Toast.makeText(this, "date:$date", Toast.LENGTH_SHORT).show()
                },
                calender.get(Calendar.YEAR),
                calender.get(Calendar.MONTH),
                calender.get(Calendar.DAY_OF_MONTH)
            )
            datePicker.show()

             Log.d("msg", date)

        }
        createTimeBtn.setOnClickListener{
            val klock = Calendar.getInstance()
            val selectedTime = Calendar.getInstance()
            var Time = timeFormat.format(selectedTime.time).toString()

            val timePicker = TimePickerDialog(this, TimePickerDialog.OnTimeSetListener { view, hourOfDay, minute ->
                selectedTime.set(Calendar.HOUR_OF_DAY,hourOfDay)
                selectedTime.set(Calendar.MINUTE,minute)

                createTimeBtn.text = Time
                Toast.makeText(this,"Time : ${timeFormat.format(klock.time)}",Toast.LENGTH_SHORT ).show()
            },
                klock.get(Calendar.HOUR_OF_DAY),klock.get(Calendar.MINUTE),false
            )
            timePicker.show()
        }
    }
}

