package com.example.assignment2brewbyte

import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.assignment2brewbyte.databinding.ActivityAdminDashboardBinding
import com.google.firebase.firestore.FirebaseFirestore

class AdminDashboardActivity : AppCompatActivity() {

    private lateinit var binding: ActivityAdminDashboardBinding
    private val db = FirebaseFirestore.getInstance()
    private var currentOrderId: String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityAdminDashboardBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.btnBack.setOnClickListener { finish() }

        binding.btnLoadOrder.setOnClickListener {
            val orderId = binding.edtOrderId.text.toString().trim()
            if (orderId.isEmpty()) {
                Toast.makeText(this, "Enter Order ID", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }
            loadOrder(orderId)
        }

        binding.btnPending.setOnClickListener { updateStatus("Pending") }
        binding.btnPreparing.setOnClickListener { updateStatus("Preparing") }
        binding.btnReady.setOnClickListener { updateStatus("Ready") }
    }

    private fun loadOrder(orderId: String) {
        db.collection("orders").document(orderId).get()
            .addOnSuccessListener { doc ->
                if (doc.exists()) {
                    currentOrderId = orderId
                    binding.txtLoadedId.text = "Order ID: $orderId"
                    binding.txtCurrentStatus.text = "Current: " + doc.getString("status")
                    Toast.makeText(this, "Order loaded", Toast.LENGTH_SHORT).show()
                } else {
                    Toast.makeText(this, "Order not found", Toast.LENGTH_SHORT).show()
                }
            }
            .addOnFailureListener {
                Toast.makeText(this, "Error loading order", Toast.LENGTH_SHORT).show()
            }
    }

    private fun updateStatus(newStatus: String) {
        val orderId = currentOrderId
        if (orderId.isNullOrEmpty()) {
            Toast.makeText(this, "Load an order first", Toast.LENGTH_SHORT).show()
            return
        }

        db.collection("orders").document(orderId)
            .update("status", newStatus)
            .addOnSuccessListener {
                binding.txtCurrentStatus.text = "Current: $newStatus"
                Toast.makeText(this, "Updated to $newStatus", Toast.LENGTH_SHORT).show()
            }
            .addOnFailureListener {
                Toast.makeText(this, "Failed to update", Toast.LENGTH_SHORT).show()
            }
    }
}
