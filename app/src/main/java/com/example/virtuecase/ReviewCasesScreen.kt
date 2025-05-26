package com.example.virtuecase.ui.screens

import android.content.Intent
import android.net.Uri
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import com.example.virtuecase.R
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.tasks.await
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar

// Data class to represent a Case
data class Case(
    val nature: String = "",
    val description: String = "",
    val date: String = "",
    val clientName: String = "",
    val clientEmail: String = "",
    val clientPhone: String = ""
)

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ReviewCasesScreen(navController: NavHostController, firestore: FirebaseFirestore) {
    // State to hold the list of cases and loading status
    var caseList by remember { mutableStateOf<List<Case>>(emptyList()) }
    var isLoading by remember { mutableStateOf(true) }

    // Fetch cases from Firestore
    LaunchedEffect(Unit) {
        caseList = fetchCasesFromFirestore(firestore)
        isLoading = false
    }

    // Scaffold for the screen layout with a TopAppBar and content area
    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text(text = "Review Cases", color = Color.Black) },
                navigationIcon = {
                    IconButton(onClick = { navController.popBackStack() }) {
                        Icon(Icons.Filled.ArrowBack, contentDescription = "Back", tint = Color.Black)
                    }
                }
            )
        }
    ) { innerPadding ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding),
            contentAlignment = Alignment.Center
        ) {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(16.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                // Logo at the top
                Image(
                    painter = painterResource(id = R.drawable.logo), // Ensure the logo is in res/drawable
                    contentDescription = "Logo",
                    modifier = Modifier.size(120.dp),
                    contentScale = ContentScale.Fit
                )

                Spacer(modifier = Modifier.height(16.dp))

                // Title
                Text(text = "Review Submitted Cases", style = MaterialTheme.typography.headlineMedium, color = Color.Black)
                Spacer(modifier = Modifier.height(16.dp))

                // Show loading spinner or case list
                if (isLoading) {
                    CircularProgressIndicator(color = Color.Black)
                } else {
                    if (caseList.isEmpty()) {
                        Text(text = "No cases available for review.", color = Color.Black)
                    } else {
                        LazyColumn(
                            modifier = Modifier.fillMaxSize(),
                            verticalArrangement = Arrangement.spacedBy(16.dp)
                        ) {
                            items(caseList) { case ->
                                CaseItem(case = case)
                            }
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun CaseItem(case: Case) {
    val context = LocalContext.current

    Card(
        modifier = Modifier.fillMaxWidth(),
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Text(text = "Nature of Complaint: ${case.nature}", style = MaterialTheme.typography.headlineMedium, color = Color.Black)
            Spacer(modifier = Modifier.height(8.dp))
            Text(text = "Description: ${case.description}", color = Color.Black)
            Spacer(modifier = Modifier.height(8.dp))
            Text(text = "Date: ${case.date}", color = Color.Black)
            Spacer(modifier = Modifier.height(8.dp))
            Text(text = "Client Name: ${case.clientName}", color = Color.Black)
            Text(text = "Email: ${case.clientEmail}", color = Color.Black)
            Text(text = "Phone: ${case.clientPhone}", color = Color.Black)
            Spacer(modifier = Modifier.height(16.dp))

            // Buttons to email or call client
            Row(horizontalArrangement = Arrangement.spacedBy(16.dp)) {
                Button(
                    onClick = {
                        val emailIntent = Intent(Intent.ACTION_SENDTO).apply {
                            data = Uri.parse("mailto:${case.clientEmail}")
                            putExtra(Intent.EXTRA_SUBJECT, "Regarding your submitted case")
                        }
                        context.startActivity(Intent.createChooser(emailIntent, "Send email..."))
                    },
                    colors = ButtonDefaults.buttonColors(Color.Blue)
                ) {
                    Text(text = "Email", color = Color.White)
                }
                Button(
                    onClick = {
                        val phoneIntent = Intent(Intent.ACTION_DIAL).apply {
                            data = Uri.parse("tel:${case.clientPhone}")
                        }
                        context.startActivity(phoneIntent)
                    },
                    colors = ButtonDefaults.buttonColors(Color.Green)
                ) {
                    Text(text = "Call", color = Color.White)
                }
            }
        }
    }
}

// Function to fetch cases from Firestore
suspend fun fetchCasesFromFirestore(firestore: FirebaseFirestore): List<Case> {
    return try {
        val caseSnapshot = firestore.collection("cases").get().await()
        caseSnapshot.documents.mapNotNull { doc ->
            doc.toObject(Case::class.java)
        }
    } catch (e: Exception) {
        emptyList()
    }
}



