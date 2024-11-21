package com.example.virtuecase

import android.os.Bundle
import android.text.TextUtils
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity

class ComplaintFormActivity : AppCompatActivity() {

    private lateinit var editTextNatureOfComplaint: EditText
    private lateinit var editTextDateOfIncident: EditText
    private lateinit var editTextNamesInvolved: EditText
    private lateinit var buttonSubmitComplaint: Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_complaint_form)

        editTextNatureOfComplaint = findViewById(R.id.editTextNatureOfComplaint)
        editTextDateOfIncident = findViewById(R.id.editTextDateOfIncident)
        editTextNamesInvolved = findViewById(R.id.editTextNamesInvolved)
        buttonSubmitComplaint = findViewById(R.id.buttonSubmitComplaint)

        buttonSubmitComplaint.setOnClickListener {
            submitComplaint()
        }
    }

    private fun submitComplaint() {
        val natureOfComplaint = editTextNatureOfComplaint.text.toString().trim()
        val dateOfIncident = editTextDateOfIncident.text.toString().trim()
        val namesInvolved = editTextNamesInvolved.text.toString().trim()

        if (TextUtils.isEmpty(natureOfComplaint)) {
            editTextNatureOfComplaint.error = "Please enter the nature of the complaint"
            return
        }

        if (TextUtils.isEmpty(dateOfIncident)) {
            editTextDateOfIncident.error = "Please enter the date of the incident"
            return
        }

        if (TextUtils.isEmpty(namesInvolved)) {
            editTextNamesInvolved.error = "Please enter the names of individuals involved"
            return
        }

        // Code for saving the complaint to a database will go here later (Firebase)

        Toast.makeText(this, "Complaint submitted successfully", Toast.LENGTH_SHORT).show()

        // Clear the form or navigate to the dashboard
        editTextNatureOfComplaint.setText("")
        editTextDateOfIncident.setText("")
        editTextNamesInvolved.setText("")
    }
}
