package com.example.virtuecase.ui.screens

import android.util.Log
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import com.google.firebase.firestore.FirebaseFirestore
import com.example.virtuecase.data.CaseSubmission
import com.google.firebase.firestore.Query

@Composable
fun CaseHistoryScreen(navController: NavHostController, firestore: FirebaseFirestore, complainantId: String) {
    // State for storing the list of cases
    var cases by remember { mutableStateOf<List<CaseSubmission>>(emptyList()) }
    var filteredCases by remember { mutableStateOf<List<CaseSubmission>>(emptyList()) }
    var isLoading by remember { mutableStateOf(true) }
    var errorMessage by remember { mutableStateOf<String?>(null) }

    // Search query state
    var searchQuery by remember { mutableStateOf("") }
    var searchResultMessage by remember { mutableStateOf("") }

    // Fetch cases from Firestore based on complainantId (not userId)
    LaunchedEffect(complainantId) {
        try {
            firestore.collection("cases")
                .whereEqualTo("complainantId", complainantId) // Use complainantId instead of userId
                .orderBy("dateOfIncident", Query.Direction.DESCENDING) // Optional: Order by incident date
                .get()
                .addOnSuccessListener { documents ->
                    if (!documents.isEmpty) {
                        // Log documents to debug the query result
                        Log.d("CaseHistoryScreen", "Documents retrieved: ${documents.size()}")

                        // Map documents to the CaseSubmission model
                        cases = documents.mapNotNull { document ->
                            try {
                                document.toObject(CaseSubmission::class.java).apply {
                                    Log.d("CaseHistoryScreen", "Case found: $caseNumber")
                                }
                            } catch (e: Exception) {
                                Log.e("CaseHistoryScreen", "Error parsing document: ${document.id}", e)
                                null
                            }
                        }

                        // Initially, show all cases
                        filteredCases = cases

                        if (cases.isEmpty()) {
                            Log.d("CaseHistoryScreen", "No valid cases found for complainantId: $complainantId")
                        }
                    } else {
                        Log.d("CaseHistoryScreen", "No cases found for complainantId: $complainantId")
                    }
                    isLoading = false
                }
                .addOnFailureListener { exception ->
                    errorMessage = "Failed to load cases: ${exception.message}"
                    Log.e("CaseHistoryScreen", "Error loading cases", exception)
                    isLoading = false
                }
        } catch (e: Exception) {
            errorMessage = "Error: ${e.message}"
            Log.e("CaseHistoryScreen", "Exception occurred", e)
            isLoading = false
        }
    }

    // Filter cases based on search query
    fun filterCases(query: String) {
        filteredCases = if (query.isEmpty()) {
            cases
        } else {
            cases.filter {
                it.caseNumber.contains(query, ignoreCase = true) ||
                        it.natureOfComplaint.contains(query, ignoreCase = true) ||
                        it.description.contains(query, ignoreCase = true)
            }
        }
        // Update the message based on whether cases are found
        searchResultMessage = if (filteredCases.isEmpty()) {
            "No cases found matching the search criteria."
        } else {
            "${filteredCases.size} cases found."
        }
    }

    // UI layout
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.Top,
        horizontalAlignment = Alignment.Start
    ) {
        Text("Case History", style = MaterialTheme.typography.titleLarge)

        // Search TextField
        TextField(
            value = searchQuery,
            onValueChange = { query ->
                searchQuery = query
            },
            label = { Text("Search Cases") },
            modifier = Modifier.fillMaxWidth()
        )

        // Search Button to trigger the search action
        Button(
            onClick = { filterCases(searchQuery) },
            modifier = Modifier.fillMaxWidth().padding(top = 8.dp)
        ) {
            Text("Search")
        }

        // Display search result message
        Spacer(modifier = Modifier.height(16.dp))
        Text(searchResultMessage, style = MaterialTheme.typography.bodyMedium)

        // Display loading state
        if (isLoading) {
            Spacer(modifier = Modifier.height(16.dp))
            Text("Loading cases...", style = MaterialTheme.typography.bodyMedium)
        }

        // Display error message if any
        errorMessage?.let {
            Spacer(modifier = Modifier.height(16.dp))
            Text(it, style = MaterialTheme.typography.bodyMedium)
        }

        // Display list of filtered cases or no cases message
        if (filteredCases.isNotEmpty()) {
            Spacer(modifier = Modifier.height(16.dp))
            LazyColumn {
                items(filteredCases) { case ->
                    CaseItemView(case)
                    Spacer(modifier = Modifier.height(8.dp))
                }
            }
        } else if (!isLoading) {
            Spacer(modifier = Modifier.height(16.dp))
            Text("No cases found.", style = MaterialTheme.typography.bodyMedium)
        }

        Spacer(modifier = Modifier.height(16.dp))
        Button(onClick = { navController.popBackStack() }) {
            Text("Back to Home")
        }
    }
}

@Composable
fun CaseItemView(case: CaseSubmission) {
    Column(modifier = Modifier.fillMaxWidth().padding(8.dp)) {
        Text("Case Number: ${case.caseNumber}", style = MaterialTheme.typography.bodyLarge)
        Text("Nature of Complaint: ${case.natureOfComplaint}", style = MaterialTheme.typography.bodyMedium)
        Text("Description: ${case.description}", style = MaterialTheme.typography.bodySmall)
        Text("Incident Date: ${case.dateOfIncident}", style = MaterialTheme.typography.bodySmall)
        Text("Status: ${case.status}", style = MaterialTheme.typography.bodySmall)

        // Display more fields from CaseSubmission if needed
        if (case.individualsInvolved.isNotEmpty()) {
            Text("Involved Parties: ${case.individualsInvolved}", style = MaterialTheme.typography.bodySmall)
        }
        if (case.region.isNotEmpty()) {
            Text("Region: ${case.region}", style = MaterialTheme.typography.bodySmall)
        }
    }
}
