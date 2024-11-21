package com.example.virtuecase

import android.os.Bundle
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity

class HRCaseReviewActivity : AppCompatActivity() {

    private lateinit var textViewHRCaseReview: TextView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_hr_case_review)

        textViewHRCaseReview = findViewById(R.id.textViewHRCaseReview)

        // Dummy data for now. Will fetch actual case data from Firebase later.
        textViewHRCaseReview.text = """
            Case 1: Nature of complaint - Discrimination
            Date: 2023-09-01
            Status: Under Investigation

            Case 2: Nature of complaint - Harassment
            Date: 2023-09-10
            Status: Resolved
        """.trimIndent()
    }
}
