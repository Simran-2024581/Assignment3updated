package com.example.assignment2brewbyte

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.assignment2brewbyte.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // MENU ITEM BUTTONS
        binding.btnAddCappuccino.setOnClickListener {
            Cart.addItem("Cappuccino", 4.50)
            Toast.makeText(this, "Added Cappuccino to cart", Toast.LENGTH_SHORT).show()
        }

        binding.btnAddTea.setOnClickListener {
            Cart.addItem("Tea", 3.00)
            Toast.makeText(this, "Added Tea to cart", Toast.LENGTH_SHORT).show()
        }

        binding.btnAddSandwich.setOnClickListener {
            Cart.addItem("Sandwich", 5.50)
            Toast.makeText(this, "Added Sandwich to cart", Toast.LENGTH_SHORT).show()
        }

        binding.btnAddPastry.setOnClickListener {
            Cart.addItem("Pastry", 3.99)
            Toast.makeText(this, "Added Pastry to cart", Toast.LENGTH_SHORT).show()
        }

        // NAVIGATION BUTTONS
        binding.btnCart.setOnClickListener {
            startActivity(Intent(this, CartActivity::class.java))
        }

        binding.btnOrderHistory.setOnClickListener {
            startActivity(Intent(this, OrderHistoryActivity::class.java))
        }

        binding.btnProfile.setOnClickListener {
            startActivity(Intent(this, ProfileActivity::class.java))
        }
    }
}
