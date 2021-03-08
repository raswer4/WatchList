package com.example.watchlist

import android.app.Activity
import android.app.DatePickerDialog
import android.app.TimePickerDialog
import android.content.Intent
import android.content.pm.PackageManager
import android.os.AsyncTask
import android.os.Build
import android.os.Bundle
import android.view.WindowManager
import android.widget.*
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import java.text.SimpleDateFormat
import java.util.*
import android.net.Uri as AndroidNetUri
import kotlin.Int as Int1

class CreateNewestWatchListActivity : AppCompatActivity() {

    companion object {
        private var Format = SimpleDateFormat("dd MMM, YYYY", Locale.US)
        private var timeFormat = SimpleDateFormat("hh:mm a", Locale.US)
        private val IMAGE_PICK_CODE = 1000
        private val PERMISSION_CODE = 1001
        private var imgToUpload = AndroidNetUri.parse("android.resource://your.package.here/drawable/image_name")

    }

        @RequiresApi(Build.VERSION_CODES.N)
        override fun onCreate(savedInstanceState: Bundle?) {
            super.onCreate(savedInstanceState)
            supportActionBar?.hide()
            window.setFlags(
                WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN
            );

            setContentView(R.layout.activity_create_watch_list)

            val createWatchButton = this.findViewById<Button>(R.id.createWatchListBtn)
            val getImgBtn = this.findViewById<Button>(R.id.getImg)
            val createDateBtn = this.findViewById<Button>(R.id.createDateBtn)
            val createTimeBtn = this.findViewById<Button>(R.id.createTimeBtn)


            getImgBtn.setOnClickListener {
                if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.M){
                    if(checkSelfPermission(android.Manifest.permission.READ_EXTERNAL_STORAGE) == PackageManager.PERMISSION_DENIED){
                        val permissions  = arrayOf(android.Manifest.permission.READ_EXTERNAL_STORAGE)
                        requestPermissions(permissions, PERMISSION_CODE)
                    }else{
                        pickImgFromGallary()
                    }
                }else{
                    pickImgFromGallary()
                }

            }


            val calender = Calendar.getInstance()
            val selectedDate = Calendar.getInstance()
            var date = Format.format(selectedDate.time).toString()
            createDateBtn.setOnClickListener {

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
            }


            val klock = Calendar.getInstance()
            val selectedTime = Calendar.getInstance()
            var time = timeFormat.format(selectedTime.time).toString()
            createTimeBtn.setOnClickListener{


                val timePicker = TimePickerDialog(
                    this, TimePickerDialog.OnTimeSetListener { view, hourOfDay, minute ->
                        selectedTime.set(Calendar.HOUR_OF_DAY, hourOfDay)
                        selectedTime.set(Calendar.MINUTE, minute)
                        var time = timeFormat.format(selectedTime.time).toString()

                        createTimeBtn.text = time
                        Toast.makeText(
                            this,
                            "Time : ${timeFormat.format(selectedTime.time)}",
                            Toast.LENGTH_SHORT
                        ).show()
                    },
                    klock.get(Calendar.HOUR_OF_DAY), klock.get(Calendar.MINUTE), false
                )
                timePicker.show()
            }


            createWatchButton.setOnClickListener{

                val watchTitle = this.findViewById<EditText>(R.id.titleEditText).editableText.toString().trim()
                val watchContent = this.findViewById<EditText>(R.id.contentEditText).editableText.toString().trim()
                val watchPlatform = this.findViewById<EditText>(R.id.platformEditText).editableText.toString().trim()
                val watchDate = date + " " + time

                try {

                    val id = newestWatchListRepository.addAdminsWatchList(
                        watchTitle,
                        watchContent,
                        watchDate,
                        imgToUpload,
                        watchPlatform,
                        this
                    )
                    Toast.makeText(this, id.toString(), Toast.LENGTH_SHORT).show()


                } catch (e: IllegalStateException){
                    Toast.makeText(this, getString(e.message!!.toInt()), Toast.LENGTH_SHORT).show()
                }

                val intent= Intent(this, WatchViewActivity::class.java)



            }

        }


        private fun pickImgFromGallary(){
            intent = Intent()
            intent.type= "image/*"
            intent.action= Intent.ACTION_GET_CONTENT

            startActivityForResult(intent, IMAGE_PICK_CODE)

        }

        override fun onActivityResult(requestCode: Int1, resultCode: Int1, data: Intent?) {
            super.onActivityResult(requestCode, resultCode, data)
            if(requestCode==IMAGE_PICK_CODE && resultCode == Activity.RESULT_OK){
                imgToUpload = data?.data
                val img = findViewById<ImageView>(R.id.watchImageView)
                img.setImageURI(data?.data)
            }
        }

        override fun onRequestPermissionsResult(
            requestCode: Int1,
            permissions: Array<out String>,
            grantResults: IntArray
        ) {
            super.onRequestPermissionsResult(requestCode, permissions, grantResults)
            when(requestCode){

                PERMISSION_CODE -> {
                    if (grantResults.size > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                        pickImgFromGallary()
                    } else {
                        Toast.makeText(
                            this,
                            getString(R.string.permissionDenied),
                            Toast.LENGTH_SHORT
                        )
                            .show()
                    }
                }
            }
        }
}