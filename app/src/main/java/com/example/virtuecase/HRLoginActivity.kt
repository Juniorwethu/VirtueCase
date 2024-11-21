package com.example.virtuecase

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import android.widget.Button
import android.widget.EditText
import com.google.firebase.auth.FirebaseAuth

class HRLoginActivity : AppCompatActivity() {

    private lateinit var auth: FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_hr_login)

        auth = FirebaseAuth.getInstance()

        val etHREmail: EditText = findViewById(R.id.etHREmail)
        val etHRPassword: EditText = findViewById(R.id.etHRPassword)
        val btnHRLogin: Button = findViewById(R.id.btnHRLogin)

        btnHRLogin.setOnClickListener {
            val email = etHREmail.text.toString().trim()
            val password = etHRPassword.text.toString().trim()

            if (email.isNotEmpty() && password.isNotEmpty()) {
                auth.signInWithEmailAndPassword(email, password)
                    .addOnCompleteListener(this) { task ->
                        if (task.isSuccessful) {
                            val intent = Intent(this, DashboardActivity::class.java)
                            startActivity(intent)
                            finish()
                        } else {
                            // Handle login failure
                        }
                    }
            }
        }
    }
}
