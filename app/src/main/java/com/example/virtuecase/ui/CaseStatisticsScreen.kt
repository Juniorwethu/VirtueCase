package com.example.virtuecase.ui

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import com.example.virtuecase.R
import com.google.firebase.firestore.FirebaseFirestore

@Composable
fun CaseStatisticsScreen(navController: NavHostController) {
    val firestore = FirebaseFirestore.getInstance()

    // State for statistics
    var totalCases by remember { mutableStateOf(0) }
    var pendingCases by remember { mutableStateOf(0) }
    var resolvedCases by remember { mutableStateOf(0) }
    var errorMessage by remember { mutableStateOf("") }

    // Fetch data from Firebase
    LaunchedEffect(Unit) {
        firestore.collection("cases")
            .get()
            .addOnSuccessListener { documents ->
                totalCases = documents.size()
                pendingCases = documents.filter { it.getString("status") == "Pending" }.size
                resolvedCases = documents.filter { it.getString("status") == "Resolved" }.size
            }
            .addOnFailureListener { exception ->
                errorMessage = exception.message ?: "Failed to fetch data"
            }
    }

    // UI for Case Statistics
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        // Top Logo
        Image(
            painter = painterResource(id = R.drawable.logo),
            contentDescription = "Top Logo",
            modifier = Modifier
                .height(100.dp)
                .fillMaxWidth()
                .padding(bottom = 16.dp)
        )

        // Title
        Text(
            text = "Case Statistics",
            fontSize = 24.sp,
            fontWeight = FontWeight.Bold,
            modifier = Modifier.padding(bottom = 16.dp),
            textAlign = TextAlign.Center
        )

        Spacer(modifier = Modifier.height(8.dp))

        if (errorMessage.isNotEmpty()) {
            Text(
                text = errorMessage,
                fontSize = 16.sp,
                color = Color.Red,
                textAlign = TextAlign.Center
            )
        } else {
            // Display Statistics
            Text("Total Cases: $totalCases", fontSize = 20.sp, fontWeight = FontWeight.SemiBold)
            Spacer(modifier = Modifier.height(8.dp))
            Text("Pending Cases: $pendingCases", fontSize = 20.sp, fontWeight = FontWeight.SemiBold)
            Spacer(modifier = Modifier.height(8.dp))
            Text("Resolved Cases: $resolvedCases", fontSize = 20.sp, fontWeight = FontWeight.SemiBold)
        }

        Spacer(modifier = Modifier.weight(1f)) // Push content to the top, leaving space for the bottom logo

        // Bottom Logo
        Image(
            painter = painterResource(id = R.drawable.logo),
            contentDescription = "Bottom Logo",
            modifier = Modifier
                .height(100.dp)
                .fillMaxWidth()
                .padding(top = 16.dp)
        )
    }
}
