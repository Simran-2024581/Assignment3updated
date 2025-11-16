package com.example.assignment2brewbyte

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.assignment2brewbyte.databinding.ActivityCheckoutBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore

class CheckoutActivity : AppCompatActivity() {

    private lateinit var binding: ActivityCheckoutBinding
    private val db = FirebaseFirestore.getInstance()
    private val auth = FirebaseAuth.getInstance()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityCheckoutBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // --------------------------
        // BACK BUTTON
        // --------------------------
        binding.btnBack.setOnClickListener {
            finish()
        }

        // --------------------------
        // SHOW CART ITEMS
        // --------------------------
        val itemsText = Cart.items.joinToString("\n") { item ->
            "${item.name} - $${item.price}"
        }

        binding.txtItems.text = itemsText
        binding.txtTotal.text = "Total: $${Cart.getTotal()}"

        // --------------------------
        // PLACE ORDER
        // --------------------------
        binding.btnPlaceOrder.setOnClickListener {
            placeOrder()
        }
    }

    private fun placeOrder() {

        if (Cart.items.isEmpty()) {
            Toast.makeText(this, "Your cart is empty!", Toast.LENGTH_SHORT).show()
            return
        }

        binding.btnPlaceOrder.isEnabled = false

        val userId = auth.currentUser?.uid ?: "guest"

        val selectedPayment = when {
            binding.rbCash.isChecked -> "Cash on Pickup"
            binding.rbCard.isChecked -> "Credit / Debit Card"
            else -> "Cash on Pickup"
        }

        // --------------------------
        // BUILD ORDER DATA
        // --------------------------
        val orderData = hashMapOf(
            "userId" to userId,
            "items" to Cart.items.map { mapOf("name" to it.name, "price" to it.price) },
            "total" to Cart.getTotal(),
            "payment" to selectedPayment,
            "status" to "Pending",
            "createdAt" to System.currentTimeMillis()
        )

        // --------------------------
        // SAVE TO FIRESTORE
        // --------------------------
        db.collection("orders")
            .add(orderData)
            .addOnSuccessListener { document ->
                val orderId = document.id

                Toast.makeText(this, "Order placed!", Toast.LENGTH_SHORT).show()

                // Clear cart
                Cart.clear()

                // Move to Order Status page
                val intent = Intent(this, OrderStatusActivity::class.java)
                intent.putExtra("ORDER_ID", orderId)
                startActivity(intent)
                finish()
            }
            .addOnFailureListener { e ->
                Toast.makeText(this, "Error placing order: ${e.message}", Toast.LENGTH_LONG).show()
                binding.btnPlaceOrder.isEnabled = true
            }
    }
}
