package com.example.assignment2brewbyte

import android.content.Intent
import android.os.Bundle
import android.view.MenuItem
import androidx.appcompat.app.AppCompatActivity
import com.example.assignment2brewbyte.databinding.ActivityOrderHistoryBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore

class OrderHistoryActivity : AppCompatActivity() {

    private lateinit var binding: ActivityOrderHistoryBinding
    private val db = FirebaseFirestore.getInstance()
    private val auth = FirebaseAuth.getInstance()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityOrderHistoryBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.btnBack.setOnClickListener { finish() }

        val user = auth.currentUser
        if (user == null) {
            binding.txtHistory.text = "Please log in to see your orders."
            return
        }

        binding.txtHistory.text = "Loading your orders..."

        // SIMPLE QUERY – NO INDEX NEEDED
        db.collection("orders")
            .whereEqualTo("userId", user.uid)
            .get()
            .addOnSuccessListener { snapshot ->
                if (snapshot == null || snapshot.isEmpty) {
                    binding.txtHistory.text = "You have no orders yet."
                    return@addOnSuccessListener
                }

                val builder = StringBuilder()

                for (doc in snapshot.documents) {
                    val orderId = doc.id
                    val items = doc.get("items") as? List<Map<String, Any?>> ?: emptyList()
                    val total = doc.getDouble("total") ?: 0.0
                    val payment = doc.getString("payment") ?: "Unknown"
                    val status = doc.getString("status") ?: "Pending"

                    builder.append("Order ID: $orderId\n")

                    builder.append("Items:\n")
                    for (item in items) {
                        val name = item["name"] as? String ?: "Item"
                        val price = (item["price"] as? Number)?.toDouble() ?: 0.0
                        builder.append("• $name - $${"%.2f".format(price)}\n")
                    }

                    builder.append("Total: $${"%.2f".format(total)}\n")
                    builder.append("Payment: $payment\n")
                    builder.append("Status: $status\n")
                    builder.append("------------------------\n\n")
                }

                binding.txtHistory.text = builder.toString()
            }
            .addOnFailureListener { e ->
                binding.txtHistory.text = "Error loading orders: ${e.message}"
            }

        // -------- BOTTOM NAV BAR --------
        // highlight Orders tab
        binding.bottomNav.selectedItemId = R.id.nav_orders

        binding.bottomNav.setOnItemSelectedListener { item: MenuItem ->
            when (item.itemId) {

                R.id.nav_home -> {
                    startActivity(Intent(this, MainActivity::class.java))
                    true
                }

                R.id.nav_orders -> {
                    // Already here
                    true
                }

                R.id.nav_profile -> {
                    startActivity(Intent(this, ProfileActivity::class.java))
                    true
                }

                else -> false
            }
        }
    }
}
