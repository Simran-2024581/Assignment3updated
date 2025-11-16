package com.example.assignment2brewbyte

import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.assignment2brewbyte.databinding.ActivityFirestoreBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FieldValue
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.Query

class FirestoreActivity : AppCompatActivity() {

    private lateinit var binding: ActivityFirestoreBinding
    private val db = FirebaseFirestore.getInstance()
    private val auth: FirebaseAuth = FirebaseAuth.getInstance()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityFirestoreBinding.inflate(layoutInflater)
        setContentView(binding.root)

        supportActionBar?.title = "Firestore Demo"
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        // ---- CREATE DATA ----
        binding.btnSave.setOnClickListener {
            val text = binding.editNote.text.toString().trim()
            if (text.isEmpty()) {
                Toast.makeText(this, "Enter some text", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            val userId = auth.currentUser?.uid ?: "anonymous"

            val note = hashMapOf(
                "userId" to userId,
                "text" to text,
                "timestamp" to FieldValue.serverTimestamp()
            )

            db.collection("notes")
                .add(note)
                .addOnSuccessListener {
                    Toast.makeText(this, "Saved!", Toast.LENGTH_SHORT).show()
                    binding.editNote.text?.clear()
                }
                .addOnFailureListener {
                    Toast.makeText(this, "Error: ${it.message}", Toast.LENGTH_SHORT).show()
                }
        }

        // ---- READ DATA ----
        binding.btnLoad.setOnClickListener {
            db.collection("notes")
                .orderBy("timestamp", Query.Direction.DESCENDING)
                .get()
                .addOnSuccessListener { result ->
                    val builder = StringBuilder()
                    for (doc in result) {
                        builder.append(doc.getString("text") ?: "")
                            .append("\n\n")
                    }
                    binding.txtNotes.text = builder.toString()
                }
                .addOnFailureListener {
                    Toast.makeText(this, "Error: ${it.message}", Toast.LENGTH_SHORT).show()
                }
        }
    }

    // Back arrow in the action bar
    override fun onSupportNavigateUp(): Boolean {
        onBackPressedDispatcher.onBackPressed()
        return true
    }
}
