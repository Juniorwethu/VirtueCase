package com.example.virtuecase.ui.screens

import android.widget.Toast
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import com.google.firebase.firestore.FirebaseFirestore
import com.example.virtuecase.data.CaseSubmission
import kotlinx.coroutines.tasks.await

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ViewCasesScreen(navController: NavHostController, firestore: FirebaseFirestore) {
    val caseList = remember { mutableStateOf<List<CaseSubmission>>(emptyList()) }
    val expandedCaseIndex = remember { mutableStateOf(-1) } // For expanding/collapsing case details
    val emailToSend = remember { mutableStateOf("") } // State for email input
    val showEmailDialog = remember { mutableStateOf(false) } // State to show/hide the email input dialog
    val selectedCase = remember { mutableStateOf<CaseSubmission?>(null) } // Case selected for resolution
    val feedbackText = remember { mutableStateOf("") } // State to hold feedback text

    // Fetching cases from Firestore
    LaunchedEffect(Unit) {
        val querySnapshot = firestore.collection("cases").get().await()
        val cases = querySnapshot.documents.map { doc ->
            doc.toObject(CaseSubmission::class.java) ?: CaseSubmission()
        }
        caseList.value = cases
    }

    // Function to update case status to "Resolved" in Firestore
    fun resolveCase(case: CaseSubmission) {
        val caseRef = firestore.collection("cases").document(case.caseNumber)
        caseRef.update("status", "Resolved").addOnCompleteListener { task ->
            if (task.isSuccessful) {
                Toast.makeText(navController.context, "Case resolved successfully.", Toast.LENGTH_SHORT).show()
            } else {
                Toast.makeText(navController.context, "Failed to resolve case.", Toast.LENGTH_SHORT).show()
            }
        }
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text(text = "View Cases", color = Color.Black) },
                navigationIcon = {
                    IconButton(onClick = { navController.popBackStack() }) {
                        Icon(Icons.Filled.ArrowBack, contentDescription = "Back", tint = Color.Black)
                    }
                }
            )
        }
    ) { innerPadding ->
        LazyColumn(
            contentPadding = innerPadding,
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp)
        ) {
            items(caseList.value) { case ->
                Card(modifier = Modifier.padding(vertical = 8.dp)) {
                    Column(modifier = Modifier.padding(16.dp)) {
                        // Display primary case details
                        Text(
                            text = "Case Number: ${case.caseNumber}",
                            style = MaterialTheme.typography.bodyMedium.copy(color = Color.Black)
                        )
                        Text(
                            text = "Complainant Name: ${case.complainantName}",
                            style = MaterialTheme.typography.bodyMedium.copy(color = Color.Black)
                        )
                        Text(
                            text = "Complainant Email: ${case.complainantEmail}",
                            style = MaterialTheme.typography.bodyMedium.copy(color = Color.Black)
                        )
                        Text(
                            text = "Complainant Phone: ${case.complainantPhone}",
                            style = MaterialTheme.typography.bodyMedium.copy(color = Color.Black)
                        )
                        Text(
                            text = "Case Type: ${case.caseType}",
                            style = MaterialTheme.typography.bodyMedium.copy(color = Color.Black)
                        )
                        Text(
                            text = "Status: ${case.status}",
                            style = MaterialTheme.typography.bodyMedium.copy(color = Color.Black)
                        )
                        Text(
                            text = "Date of Incident: ${case.dateOfIncident}",
                            style = MaterialTheme.typography.bodyMedium.copy(color = Color.Black)
                        )
                        Spacer(modifier = Modifier.height(8.dp))

                        // Toggle additional details
                        IconButton(onClick = {
                            expandedCaseIndex.value = if (expandedCaseIndex.value == -1) caseList.value.indexOf(case) else -1
                        }) {
                            Icon(Icons.Filled.MoreVert, contentDescription = "More options")
                        }

                        // Expanded view with additional case details and resolve button
                        if (expandedCaseIndex.value == caseList.value.indexOf(case)) {
                            Column(modifier = Modifier.padding(top = 8.dp)) {
                                Text(text = "Nature of Complaint: ${case.natureOfComplaint}")
                                Text(text = "Description: ${case.description}")
                                Text(text = "First Name: ${case.firstName}")
                                Text(text = "Last Name: ${case.lastName}")
                                Text(text = "Employer Name: ${case.employerName}")
                                Text(text = "Employer Email: ${case.employerEmail}")
                                Text(text = "Employer Contact: ${case.employerContactNumber}")
                                Text(text = "Employer Address: ${case.employerAddress}")
                                Text(text = "Income/Salary: ${case.incomeSalary}")
                                Text(text = "Occupation: ${case.occupation}")
                                Text(text = "Region: ${case.region}")
                                Text(text = "Individuals Involved: ${case.individualsInvolved}")
                                Text(text = "Documents: ${case.documents.joinToString(", ")}")
                                Text(text = "Complainant ID: ${case.complainantId}")
                                Text(text = "Employer ID: ${case.employerId}")
                                Spacer(modifier = Modifier.height(16.dp))

                                // HR feedback
                                Text("Feedback:")
                                TextField(
                                    value = feedbackText.value,
                                    onValueChange = { feedbackText.value = it },
                                    label = { Text("Provide your feedback here") },
                                    modifier = Modifier.fillMaxWidth()
                                )

                                // Resolve Case Button
                                Button(
                                    onClick = {
                                        resolveCase(case) // Resolve case on button click
                                    },
                                    colors = ButtonDefaults.buttonColors(containerColor = Color.Blue)
                                ) {
                                    Text("Resolve Case", color = Color.White)
                                }
                            }
                        }
                    }
                }
            }
        }

        // Email Input Dialog (If applicable, add email logic here)
        if (showEmailDialog.value) {
            AlertDialog(
                onDismissRequest = { showEmailDialog.value = false },
                title = { Text(text = "Send Case to Investigator") },
                text = {
                    Column {
                        Text("Enter investigator's email:")
                        TextField(
                            value = emailToSend.value,
                            onValueChange = { emailToSend.value = it },
                            placeholder = { Text("Email Address") },
                            modifier = Modifier.fillMaxWidth()
                        )
                        Spacer(modifier = Modifier.height(16.dp))
                        Text("Feedback (optional):")
                        TextField(
                            value = feedbackText.value,
                            onValueChange = { feedbackText.value = it },
                            placeholder = { Text("Enter feedback") },
                            modifier = Modifier.fillMaxWidth()
                        )
                    }
                },
                confirmButton = {
                    Button(
                        onClick = {
                            selectedCase.value?.let { case ->
                                sendCaseDetailsByEmail(emailToSend.value, case, feedbackText.value)
                            }
                            showEmailDialog.value = false
                            emailToSend.value = ""
                            feedbackText.value = ""
                        }
                    ) {
                        Text("Send")
                    }
                },
                dismissButton = {
                    Button(onClick = { showEmailDialog.value = false }) {
                        Text("Cancel")
                    }
                }
            )
        }
    }
}

/**
 * Simulates sending case details by email.
 * Replace this with an actual email-sending service or functionality.
 */
private fun sendCaseDetailsByEmail(email: String, case: CaseSubmission, feedback: String) {
    // Here, you could integrate an email API or Firebase Functions to send the email.
    // For now, we're using a placeholder.
    println("Emailing case details to: $email")
    println("Case Details: $case")
    println("Feedback: $feedback")
}
