package com.example.watchlist

import android.app.*
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.os.AsyncTask
import android.os.Build
import android.os.Bundle
import android.view.WindowManager
import android.widget.*
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import com.example.watchlist.sampledata.UserListFragment
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
            window.setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);


            setContentView(R.layout.activity_create_newest_watch_list)

            val createWatchButton = this.findViewById<Button>(R.id.createNewestWatchListBtn)
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


            val selectedTime = Calendar.getInstance()
            var date = Format.format(selectedTime.time).toString()
            createDateBtn.setOnClickListener {

                val datePicker = DatePickerDialog(
                    this, DatePickerDialog.OnDateSetListener { view, year, month, dayOfMonth ->
                        selectedTime.set(Calendar.YEAR, year)
                        selectedTime.set(Calendar.MONTH, month)
                        selectedTime.set(Calendar.DAY_OF_MONTH, dayOfMonth)

                        date = Format.format(selectedTime.time).toString()

                        createDateBtn.text = date
                        Toast.makeText(this, "date:$date", Toast.LENGTH_SHORT).show()
                    },
                    selectedTime.get(Calendar.YEAR),
                    selectedTime.get(Calendar.MONTH),
                    selectedTime.get(Calendar.DAY_OF_MONTH)
                )
                datePicker.datePicker.minDate = System.currentTimeMillis()
                datePicker.show()
            }


            var time = timeFormat.format(selectedTime.time).toString()
            createTimeBtn.setOnClickListener{
                val timePicker = TimePickerDialog(
                    this, TimePickerDialog.OnTimeSetListener { view, hourOfDay, minute ->
                        selectedTime.set(Calendar.HOUR_OF_DAY, hourOfDay)
                        selectedTime.set(Calendar.MINUTE, minute)
                        selectedTime.set(Calendar.SECOND, 0)
                        time = timeFormat.format(selectedTime.time).toString()

                        createTimeBtn.text = time
                        Toast.makeText(
                            this,
                            "Time : ${timeFormat.format(selectedTime.time)}",
                            Toast.LENGTH_SHORT
                        ).show()
                    },
                    selectedTime.get(Calendar.HOUR_OF_DAY),
                    selectedTime.get(Calendar.MINUTE), false
                )
                timePicker.show()
            }

            createWatchButton.setOnClickListener{

                val watchTitle = this.findViewById<EditText>(R.id.titleEditText).editableText.toString().trim()
                val watchContent = this.findViewById<EditText>(R.id.contentEditText).editableText.toString().trim()
                val watchPlatform = this.findViewById<EditText>(R.id.platformEditText).editableText.toString().trim()
                val watchDate = "$date $time"

                    try {
                        val progressDialog = ProgressDialog(this)
                        progressDialog.setTitle(R.string.loading)
                        progressDialog.show()

                        val id = newestWatchListRepository.createWatchList(
                            watchTitle,
                            watchContent,
                            watchDate,
                            watchPlatform
                        )

                        newestWatchListRepository.uploadImgToStorage("adminImg/$id", imgToUpload).addOnSuccessListener {
                            val intent = Intent(this, WatchAdminViewActivity::class.java).apply {
                                progressDialog.dismiss()
                                putExtra("newestId", id)
                            }
                            startActivity(intent)
                            finish()
                        }
                    } catch (e: IllegalStateException){
                        Toast.makeText(this, getString(e.message!!.toInt()), Toast.LENGTH_SHORT).show()
                    }
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