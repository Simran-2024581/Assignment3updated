package com.example.assignment2brewbyte

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.example.assignment2brewbyte.databinding.ActivityProfileBinding

class ProfileActivity : AppCompatActivity() {

    private lateinit var binding: ActivityProfileBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityProfileBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // Back to previous screen
        binding.btnBack.setOnClickListener {
            finish()
        }

        // Open camera screen from profile
        binding.btnCameraProfile.setOnClickListener {
            startActivity(Intent(this, CameraActivity::class.java))
        }
    }
}
