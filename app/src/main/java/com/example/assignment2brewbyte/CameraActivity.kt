package com.example.assignment2brewbyte

import android.Manifest
import android.app.Activity
import android.content.ContentValues
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.provider.MediaStore
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.example.assignment2brewbyte.databinding.ActivityCameraBinding

class CameraActivity : AppCompatActivity() {

    private lateinit var binding: ActivityCameraBinding

    private val CAMERA_PERMISSION_CODE = 301

    // will hold the uri where the camera saves the photo
    private var pendingPhotoUri: Uri? = null

    // Activity Result launcher for camera
    private val cameraLauncher =
        registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
            if (result.resultCode == Activity.RESULT_OK) {

                // We told the camera to save into pendingPhotoUri
                val uri = pendingPhotoUri
                if (uri != null) {
                    // show the photo in the ImageView
                    binding.imgPreview.setImageURI(uri)
                    Toast.makeText(this, "Selfie saved to gallery 🎉", Toast.LENGTH_SHORT).show()
                } else {
                    Toast.makeText(this, "Photo saved, but URI is missing", Toast.LENGTH_SHORT).show()
                }

            } else {
                Toast.makeText(this, "Camera cancelled", Toast.LENGTH_SHORT).show()
            }
        }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityCameraBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // Back button
        binding.btnBack.setOnClickListener { finish() }

        // Take selfie button
        binding.btnTakePhoto.setOnClickListener {
            checkCameraPermissionAndOpen()
        }
    }

    private fun checkCameraPermissionAndOpen() {
        val permission = Manifest.permission.CAMERA

        if (ContextCompat.checkSelfPermission(this, permission)
            != PackageManager.PERMISSION_GRANTED
        ) {
            ActivityCompat.requestPermissions(
                this,
                arrayOf(permission),
                CAMERA_PERMISSION_CODE
            )
        } else {
            openCamera()
        }
    }

    private fun openCamera() {
        // 1) Create an empty entry in MediaStore where the camera will write the file
        val filename = "BrewByte_${System.currentTimeMillis()}.jpg"

        val values = ContentValues().apply {
            put(MediaStore.Images.Media.DISPLAY_NAME, filename)
            put(MediaStore.Images.Media.MIME_TYPE, "image/jpeg")
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
                put(MediaStore.Images.Media.RELATIVE_PATH, "Pictures/BrewByteSelfies")
            }
        }

        val resolver = contentResolver
        val uri = resolver.insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, values)

        if (uri == null) {
            Toast.makeText(this, "Could not create image file", Toast.LENGTH_SHORT).show()
            return
        }

        // Remember this uri so we can show it later
        pendingPhotoUri = uri

        // 2) Launch camera app and tell it where to save
        val intent = Intent(MediaStore.ACTION_IMAGE_CAPTURE).apply {
            putExtra(MediaStore.EXTRA_OUTPUT, uri)

            // hint to use front camera (not guaranteed but common)
            putExtra("android.intent.extras.CAMERA_FACING", 1)
            putExtra("android.intent.extras.LENS_FACING_FRONT", 1)
            putExtra("android.intent.extra.USE_FRONT_CAMERA", true)
        }

        cameraLauncher.launch(intent)
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)

        if (requestCode == CAMERA_PERMISSION_CODE) {
            if (grantResults.isNotEmpty() &&
                grantResults[0] == PackageManager.PERMISSION_GRANTED
            ) {
                openCamera()
            } else {
                Toast.makeText(
                    this,
                    "Camera permission is required to take selfies",
                    Toast.LENGTH_SHORT
                ).show()
            }
        }
    }
}
