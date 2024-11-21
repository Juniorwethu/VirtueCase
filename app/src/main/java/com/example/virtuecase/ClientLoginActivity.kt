package com.example.virtuecase

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.auth.FirebaseAuth

class ClientLoginActivity : AppCompatActivity() {

    private lateinit var auth: FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_client_login)

        auth = FirebaseAuth.getInstance()

        val etClientEmail: EditText = findViewById(R.id.etClientEmail)
        val etClientPassword: EditText = findViewById(R.id.etClientPassword)
        val btnClientLogin: Button = findViewById(R.id.btnClientLogin)

        btnClientLogin.setOnClickListener {
            val email = etClientEmail.text.toString().trim()
            val password = etClientPassword.text.toString().trim()

            if (email.isNotEmpty() && password.isNotEmpty()) {
                auth.signInWithEmailAndPassword(email, password)
                    .addOnCompleteListener(this) { task ->
                        if (task.isSuccessful) {
                            // Login successful, navigate to UserDashboard
                            val intent = Intent(this, DashboardActivity::class.java)
                            startActivity(intent)
                            finish() // Finish this activity so the user can't go back to it
                        } else {
                            // Handle login failure
                            Toast.makeText(this, "Login failed: ${task.exception?.message}", Toast.LENGTH_LONG).show()
                        }
                    }
            } else {
                // Prompt user to fill in fields
                Toast.makeText(this, "Please enter both email and password", Toast.LENGTH_SHORT).show()
            }
        }
    }
}
