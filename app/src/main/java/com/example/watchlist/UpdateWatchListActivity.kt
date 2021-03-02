package com.example.watchlist

import android.app.Activity
import android.app.DatePickerDialog
import android.app.TimePickerDialog
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.WindowManager
import android.widget.Button
import android.widget.EditText
import android.widget.ImageView
import android.widget.Toast
import androidx.annotation.RequiresApi
import java.text.SimpleDateFormat
import java.util.*

class UpdateWatchListActivity : AppCompatActivity() {

    companion object {
        internal var Format = SimpleDateFormat("dd MMM, YYYY", Locale.US)
        internal var timeFormat = SimpleDateFormat("hh:mm a", Locale.US)
        internal val IMAGE_PICK_CODE = 1000
        internal val PERMISSION_CODE = 1001
        private var imageToUpload = Uri.parse("android.resource://your.package.here/drawable/image_name")

    }
    @RequiresApi(Build.VERSION_CODES.N)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        supportActionBar?.hide()
        window.setFlags(
            WindowManager.LayoutParams.FLAG_FULLSCREEN,
            WindowManager.LayoutParams.FLAG_FULLSCREEN
        )
        setContentView(R.layout.activity_update_watch_list)

        val getImgBtn = this.findViewById<Button>(R.id.updateImageButton)
        val updateDateBtn = this.findViewById<Button>(R.id.updateDateBtn)
        val updateTimeBtn = this.findViewById<Button>(R.id.updateTimeBtn)
        val updateButton = this.findViewById<Button>(R.id.updateButton)
        val id = intent.getLongExtra("update", 0)


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
        updateDateBtn.setOnClickListener {
            val datePicker = DatePickerDialog(
                this,
                DatePickerDialog.OnDateSetListener { view, year, month, dayOfMonth ->
                    selectedDate.set(Calendar.YEAR, year)
                    selectedDate.set(Calendar.MONTH, month)
                    selectedDate.set(Calendar.DAY_OF_MONTH, dayOfMonth)
                    date = Format.format(selectedDate.time).toString()

                    updateDateBtn.text = date
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
        updateTimeBtn.setOnClickListener{

            val timePicker = TimePickerDialog(this, TimePickerDialog.OnTimeSetListener { view, hourOfDay, minute ->
                selectedTime.set(Calendar.HOUR_OF_DAY,hourOfDay)
                selectedTime.set(Calendar.MINUTE,minute)
                var time = timeFormat.format(selectedTime.time).toString()

                updateTimeBtn.text = time
                Toast.makeText(this,"Time : ${timeFormat.format(selectedTime.time)}",Toast.LENGTH_SHORT ).show()
            },
                klock.get(Calendar.HOUR_OF_DAY),klock.get(Calendar.MINUTE),false
            )
            timePicker.show()
        }


        updateButton.setOnClickListener() {

            val updateTitle = this.findViewById<EditText>(R.id.updateTitleTextEdit).editableText.toString().trim()
            val updateContent = this.findViewById<EditText>(R.id.updateContentTextEdit).editableText.toString().trim()
            val updatePlatform = this.findViewById<EditText>(R.id.updatePlatformEditText).editableText.toString().trim()
            val updateDate = date

            try {
                watchListRepository.updateWatchListById(
                    id,
                    updateTitle,
                    updateContent,
                    updateDate,
                    updatePlatform,
                    imageToUpload
                )
                finish()
            }catch (e: IllegalStateException){
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

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if(requestCode== IMAGE_PICK_CODE && resultCode == Activity.RESULT_OK){
            imageToUpload = data?.data
            val img = findViewById<ImageView>(R.id.updateImage)
            img.setImageURI(data?.data)
        }
    }


    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        when(requestCode){

            PERMISSION_CODE -> {
                if (grantResults.size > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    pickImgFromGallary()
                } else {
                    Toast.makeText(this, getString(R.string.permissionDenied), Toast.LENGTH_SHORT)
                        .show()
                }
            }
        }
    }
}
