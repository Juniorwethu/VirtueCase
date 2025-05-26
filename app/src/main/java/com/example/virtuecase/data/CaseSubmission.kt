package com.example.virtuecase.data

data class CaseSubmission(
    val natureOfComplaint: String,
    val description: String,
    val dateOfIncident: String,
    val individualsInvolved: String,
    val documents: List<String> // List of document file paths or URLs
)
