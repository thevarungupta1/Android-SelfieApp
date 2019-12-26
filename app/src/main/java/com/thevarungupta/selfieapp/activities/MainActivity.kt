package com.thevarungupta.selfieapp.activities

import android.content.Intent
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Environment
import android.provider.MediaStore
import android.util.Log
import android.widget.Toast
import androidx.core.content.FileProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.thevarungupta.selfieapp.R
import com.thevarungupta.selfieapp.adapters.AdapterSelfie
import com.thevarungupta.selfieapp.models.Selfie
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.content_main.*
import java.io.File
import java.io.IOException
import java.text.SimpleDateFormat
import java.util.*
import kotlin.collections.ArrayList

class MainActivity : AppCompatActivity() {


    val REQUEST_IMAGE_CAPTURE: Int = 1

    var currentPhotoPath: String? = null

    var photoFile: File? = null

    lateinit var adapterSelfie: AdapterSelfie
    var selfieList: ArrayList<Selfie> = ArrayList()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        init()
    }

    private fun init() {
        setupToolbar()
        recycler_view.layoutManager = LinearLayoutManager(this)
        adapterSelfie = AdapterSelfie(this, selfieList)
        recycler_view.adapter = adapterSelfie

        fab.setOnClickListener {
            takePictureUsingIntent()
        }
    }

    private fun setupToolbar() {
        var toolbar = toolbar
        toolbar.title = "Home"
        setSupportActionBar(toolbar)
    }

    private fun takePictureUsingIntent() {
        Intent(MediaStore.ACTION_IMAGE_CAPTURE).also { takePictureIntent ->
            // Ensure that there's a camera activity to handle the intent
            takePictureIntent.resolveActivity(packageManager)?.also {
                // Create the File where the photo should go
                photoFile = try {
                    createImageFile()
                } catch (ex: IOException) {
                    // Error occurred while creating the File
                    null
                }
                // Continue only if the File was successfully created
                photoFile?.also {
                    val photoURI: Uri = FileProvider.getUriForFile(
                        this,
                        "com.thevarungupta.selfieapp.fileprovider",
                        it
                    )
                    takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, photoURI)
                    startActivityForResult(takePictureIntent, REQUEST_IMAGE_CAPTURE)
                }
            }
        }
    }

    private fun addToGallery() {
        val galleryIntent = Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE)
        val picUri = Uri.fromFile(photoFile)
        Log.d("varun", "" + picUri.toString())
        galleryIntent.data = picUri
        this.sendBroadcast(galleryIntent)
    }

    @Throws(IOException::class)
    private fun createImageFile(): File {
        // Create an image file name
        val timeStamp: String = SimpleDateFormat("yyyyMMdd_HHmmss").format(Date())
        val storageDir: File? = getExternalFilesDir(Environment.DIRECTORY_PICTURES)

        return File.createTempFile(
            "Selfie_${timeStamp}_", /* prefix */
            ".jpg", /* suffix */
            storageDir /* directory */
        ).apply {
            // Save a file: path for use with ACTION_VIEW intents
            currentPhotoPath = absolutePath
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        addToGallery()
        if (requestCode == REQUEST_IMAGE_CAPTURE && resultCode == RESULT_OK) {
            //val imageBitmap = data?.extras?.get("data") as Bitmap
            //val imageBitmap = BitmapFactory.decodeFile(photoFile?.absolutePath)
            //image_view.setImageBitmap(imageBitmap)
            var imageName = photoFile?.name
            var imagePath = photoFile?.absolutePath
            var selfie = Selfie(imageName, imagePath)
            selfieList.add(selfie)
            adapterSelfie.notifyDataSetChanged()
            addToGallery()
        } else {
            Toast.makeText(this, "something went wrong", Toast.LENGTH_SHORT).show()
        }
    }

}
