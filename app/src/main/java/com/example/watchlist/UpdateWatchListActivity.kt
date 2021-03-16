package com.example.watchlist

import android.app.*
import android.content.Context
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
import kotlinx.coroutines.selects.select
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
        window.setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
            WindowManager.LayoutParams.FLAG_FULLSCREEN);

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


        val selectedTime = Calendar.getInstance()
        var date = Format.format(selectedTime.time).toString()
        updateDateBtn.setOnClickListener {

            val datePicker = DatePickerDialog(this, DatePickerDialog.OnDateSetListener { view, year, month, dayOfMonth ->
                selectedTime.set(Calendar.YEAR, year)
                selectedTime.set(Calendar.MONTH, month)
                selectedTime.set(Calendar.DAY_OF_MONTH, dayOfMonth)

                date = Format.format(selectedTime.time).toString()

                updateDateBtn.text = date
            },
                selectedTime.get(Calendar.YEAR),
                selectedTime.get(Calendar.MONTH),
                selectedTime.get(Calendar.DAY_OF_MONTH)
            )
            datePicker.datePicker.minDate = System.currentTimeMillis()
            datePicker.show()
        }


        var time = timeFormat.format(selectedTime.time).toString()

        updateTimeBtn.setOnClickListener{
            val timePicker = TimePickerDialog(this, TimePickerDialog.OnTimeSetListener { view, hourOfDay, minute ->
                selectedTime.set(Calendar.HOUR_OF_DAY,hourOfDay)
                selectedTime.set(Calendar.MINUTE,minute)
                selectedTime.set(Calendar.SECOND, 0)
                time = timeFormat.format(selectedTime.time).toString()

                updateTimeBtn.text = time
            },
                selectedTime.get(Calendar.HOUR_OF_DAY),
                selectedTime.get(Calendar.MINUTE),false
            )
            timePicker.show()
        }


        updateButton.setOnClickListener() {

            val updateTitle = this.findViewById<EditText>(R.id.updateTitleTextEdit).editableText.toString().trim()
            val updateContent = this.findViewById<EditText>(R.id.updateContentTextEdit).editableText.toString().trim()
            val updatePlatform = this.findViewById<EditText>(R.id.updatePlatformEditText).editableText.toString().trim()
            val updateDate = "$date $time"

            try {
                watchListRepository.updateWatchListById(
                    id,
                    updateTitle,
                    updateContent,
                    updateDate,
                    updatePlatform,
                )
                watchListRepository.updateWatchListFirebase(
                    id,
                    updateTitle,
                    updateContent,
                    updateDate,
                    updatePlatform,
                    imageToUpload
                )
                updateAlarm(selectedTime, id)
                finish()
            }catch (e: IllegalStateException){
                Toast.makeText(this, getString(e.message!!.toInt()), Toast.LENGTH_SHORT).show()
            }
        }
    }

    @RequiresApi(Build.VERSION_CODES.KITKAT)
    fun updateAlarm(calendar: Calendar, id: Long) {
        val alarmManager = getSystemService(Context.ALARM_SERVICE) as AlarmManager
        val intent = Intent(this, AlarmReceiver::class.java)
        val pendingIntent = PendingIntent.getBroadcast(this, id.toInt(), intent, 0)
        alarmManager.cancel(pendingIntent)
        alarmManager.setExact(AlarmManager.RTC_WAKEUP, calendar.timeInMillis, pendingIntent)
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
