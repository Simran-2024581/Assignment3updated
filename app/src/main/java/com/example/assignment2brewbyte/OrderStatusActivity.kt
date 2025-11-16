package com.example.assignment2brewbyte

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.example.assignment2brewbyte.databinding.ActivityOrderStatusBinding
import com.google.firebase.firestore.FirebaseFirestore

class OrderStatusActivity : AppCompatActivity() {

    private lateinit var binding: ActivityOrderStatusBinding
    private val db = FirebaseFirestore.getInstance()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityOrderStatusBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val orderId = intent.getStringExtra("ORDER_ID")
        if (orderId == null) {
            binding.txtOrderId.text = "Order ID: (none)"
            binding.txtStatus.text = "No Order ID found"
            return
        }

        // Show ID on screen
        binding.txtOrderId.text = "Order ID: $orderId"

        // Live updates from Firestore
        db.collection("orders").document(orderId)
            .addSnapshotListener { snapshot, _ ->
                if (snapshot != null && snapshot.exists()) {

                    val items = snapshot["items"] as? List<Map<String, Any>>
                    val payment = snapshot.getString("payment") ?: ""
                    val status = snapshot.getString("status") ?: ""
                    val total = snapshot.getDouble("total") ?: 0.0

                    val itemsText = items?.joinToString("\n") {
                        "${it["name"]} - $${it["price"]}"
                    } ?: ""

                    binding.txtItems.text = itemsText
                    binding.txtPayment.text = "Payment: $payment"
                    binding.txtTotal.text = "Total: $${"%.2f".format(total)}"
                    binding.txtStatus.text = "Status: $status"
                } else {
                    binding.txtStatus.text = "Order not found"
                }
            }

        binding.btnBackToMenu.setOnClickListener {
            startActivity(Intent(this, MainActivity::class.java))
            finish()
        }
    }
}
