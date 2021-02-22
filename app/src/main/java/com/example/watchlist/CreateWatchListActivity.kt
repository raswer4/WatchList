package com.example.watchlist

import android.app.Activity
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.view.WindowManager
import android.widget.Button
import android.widget.EditText
import android.widget.ImageView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity


class CreateWatchListActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        supportActionBar?.hide()
        window.setFlags(
            WindowManager.LayoutParams.FLAG_FULLSCREEN,
            WindowManager.LayoutParams.FLAG_FULLSCREEN
        );

        setContentView(R.layout.activity_create_watch_list)
        val createWatchButton = this.findViewById<Button>(R.id.createWatchList)
        val getImgBtn = this.findViewById<Button>(R.id.getImg)

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
        createWatchButton.setOnClickListener(){
            addToWatchList()
        }


    }
    companion object{
        private val IMAGE_PICK_CODE = 1000
        private val PERMISSION_CODE = 1001
        private var imgToUpload = Uri.parse("android.resource://your.package.here/drawable/image_name")

    }

    private fun addToWatchList(){
        val watchTitle = this.findViewById<EditText>(R.id.titleEditText).editableText.toString().trim()
        val watchContent = this.findViewById<EditText>(R.id.contentEditText).editableText.toString().trim()
        val watchDate = this.findViewById<EditText>(R.id.dateEditText).editableText.toString().trim()

        val id = watchListRepository.addWatchList(
            watchTitle,
            watchContent,
            watchDate,
            imgToUpload,
            this
        )
        Toast.makeText(this, id.toString(), Toast.LENGTH_SHORT).show()

        val intent = Intent(this, WatchViewActivity::class.java).apply {
            putExtra("id", id)
        }
        startActivity(intent)
        finish()
    }

    private fun pickImgFromGallary(){
        intent = Intent()
        intent.type= "image/*"
        intent.action= Intent.ACTION_GET_CONTENT

        startActivityForResult(intent, IMAGE_PICK_CODE)

    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if(requestCode==IMAGE_PICK_CODE && resultCode == Activity.RESULT_OK){
            imgToUpload = data?.data
            val img = findViewById<ImageView>(R.id.watchImageView)
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
