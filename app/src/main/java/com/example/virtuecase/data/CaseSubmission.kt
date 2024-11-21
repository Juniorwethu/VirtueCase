package com.example.virtuecase.data

data class CaseSubmission(
    val caseId: String = "",
    val caseNumber: String = "",
    val caseType: String = "",
    val complainantEmail: String = "",
    val complainantName: String = "",
    val complainantPhone: String = "",
    val dateOfIncident: String = "",
    val firstName: String = "",
    val lastName: String = "",
    val description: String = "",
    val natureOfComplaint: String = "",
    val complainantId: String = "",
    val status: String = "Pending",
    val employerName: String = "",
    val employerEmail: String = "",
    val employerId: String = "",
    val employerAddress: String = "",
    val employerContactNumber: String = "",
    val incomeSalary: String = "",
    val occupation: String = "",
    val region: String = "",
    val individualsInvolved: String = "",
    val documents: List<String> = emptyList(),
    val userEmail: String = ""  // Added userEmail field
) {
}
