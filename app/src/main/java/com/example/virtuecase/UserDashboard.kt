package com.example.virtuecase

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity
import com.example.virtuecase.ui.screens.LoginScreen
import com.google.firebase.auth.FirebaseAuth

class DashboardActivity : AppCompatActivity() {

    private lateinit var viewCasesButton: Button
    private lateinit var settingsButton: Button
    private lateinit var submitCaseButton: Button
    private lateinit var logoutButton: Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_user_dashboard)

        viewCasesButton = findViewById(R.id.viewCasesButton)
        settingsButton = findViewById(R.id.settingsButton)
        submitCaseButton = findViewById(R.id.submitCaseButton)
        logoutButton = findViewById(R.id.logoutButton)

//        // Set up click listeners
//        viewCasesButton.setOnClickListener {
//            startActivity(Intent(this, ViewCasesScreen::class.java))
//        }
//
//        settingsButton.setOnClickListener {
//            startActivity(Intent(this, SettingsActivity::class.java))
//        }
//
//        submitCaseButton.setOnClickListener {
//            startActivity(Intent(this, SubmitCaseScreen::class.java))
//        }
//
//        logoutButton.setOnClickListener {
//            logoutUser()
//        }
    }

//    private fun logoutUser() {
//        FirebaseAuth.getInstance().signOut()
//        startActivity(Intent(this, LoginScreen::class.java))
//        finish()
//    }
}