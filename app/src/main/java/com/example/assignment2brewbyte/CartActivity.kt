package com.example.assignment2brewbyte

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.assignment2brewbyte.databinding.ActivityCartBinding

class CartActivity : AppCompatActivity() {

    private lateinit var binding: ActivityCartBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityCartBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // ---- BACK BUTTON ----
        binding.btnBack.setOnClickListener {
            finish()
        }

        // ---- SHOW CART ITEMS ----
        updateCartUI()

        // ---- CLEAR CART ----
        binding.btnClearCart.setOnClickListener {
            Cart.clear()
            updateCartUI()
            Toast.makeText(this, "Cart cleared", Toast.LENGTH_SHORT).show()
        }

        // ---- GO TO CHECKOUT ----
        binding.btnCheckout.setOnClickListener {
            if (Cart.items.isEmpty()) {
                Toast.makeText(this, "Your cart is empty", Toast.LENGTH_SHORT).show()
            } else {
                startActivity(Intent(this, CheckoutActivity::class.java))
            }
        }
    }

    // ---- REFRESH CART SCREEN ----
    private fun updateCartUI() {

        if (Cart.items.isEmpty()) {
            binding.txtCartItems.text = "Your cart is empty"
            binding.txtTotal.text = "Total: $0.00"
            return
        }

        val itemText = Cart.items.joinToString("\n") { item ->
            "${item.name} - $${item.price}"
        }

        binding.txtCartItems.text = itemText
        binding.txtTotal.text = "Total: $${Cart.getTotal()}"
    }
}
