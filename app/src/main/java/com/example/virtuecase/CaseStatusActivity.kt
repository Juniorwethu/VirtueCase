package com.example.virtuecase

import android.os.Bundle
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity

class CaseStatusActivity : AppCompatActivity() {

    private lateinit var textViewCaseStatus: TextView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_case_status)

        textViewCaseStatus = findViewById(R.id.textViewCaseStatus)

        // Placeholder case status. Will retrieve actual data from Firebase later.
        textViewCaseStatus.text = "Case Status: Under Investigation"
    }
}
