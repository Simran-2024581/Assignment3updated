package com.example.assignment2brewbyte

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.example.assignment2brewbyte.databinding.ActivityProfileBinding
import com.google.firebase.auth.FirebaseAuth

class ProfileActivity : AppCompatActivity() {

    private lateinit var binding: ActivityProfileBinding
    private val auth = FirebaseAuth.getInstance()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityProfileBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.btnBack.setOnClickListener { finish() }

        val user = auth.currentUser
        if (user != null) {
            binding.txtEmail.text = "Email: ${user.email}"
            binding.txtUserId.text = "User ID: ${user.uid}"
        } else {
            binding.txtEmail.text = "Email: Guest (not logged in)"
            binding.txtUserId.text = "User ID: -"
        }
    }
}
