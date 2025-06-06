package com.example.virtuecase.ui

import android.content.Context
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
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.tasks.await
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardColors
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.ui.platform.LocalContext
import com.example.virtuecase.R

// Data class to represent a Case
data class Case(
    val nature: String = "",
    val description: String = "",
    val date: String = "",
    val parties: String = "",
    val documentUrl: String? = null,
    val userId: String = "" // Assuming each case has an associated user ID
)

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ViewCasesScreen(navController: NavHostController, firestore: FirebaseFirestore) {
    var caseList by remember { mutableStateOf<List<Case>>(emptyList()) }
    var isLoading by remember { mutableStateOf(true) }

    // Fetch cases from Firestore
    LaunchedEffect(Unit) {
        caseList = fetchCasesFromFirestore(firestore)
        isLoading = false
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
                Image(
                    painter = painterResource(id = R.drawable.logo),
                    contentDescription = "Logo",
                    modifier = Modifier.size(128.dp)
                )

                Spacer(modifier = Modifier.height(16.dp))

                if (isLoading) {
                    CircularProgressIndicator(color = Color.Black)
                } else {
                    if (caseList.isEmpty()) {
                        Text(text = "No cases submitted.", color = Color.Black)
                    } else {
                        LazyColumn(
                            modifier = Modifier.fillMaxSize(),
                            contentPadding = PaddingValues(16.dp),
                            verticalArrangement = Arrangement.spacedBy(8.dp)
                        ) {
                            items(caseList) { case ->
                                CaseItem(case = case, firestore = firestore)
                            }
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun CaseItem(case: Case, firestore: FirebaseFirestore) {
    val context = LocalContext.current // Get the current context

    Card(
        modifier = Modifier.fillMaxWidth(),
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Text(text = "Nature of Complaint: ${case.nature}", style = MaterialTheme.typography.bodyMedium, color = Color.Black)
            Spacer(modifier = Modifier.height(8.dp))
            Text(text = "Description: ${case.description}", color = Color.Black)
            Spacer(modifier = Modifier.height(8.dp))
            Text(text = "Date of Incident: ${case.date}", color = Color.Black)
            Spacer(modifier = Modifier.height(8.dp))
            Text(text = "Involved Parties: ${case.parties}", color = Color.Black)
            case.documentUrl?.let {
                Spacer(modifier = Modifier.height(8.dp))
                Text(text = "Document URL: $it", color = Color.Blue)
            }
            Spacer(modifier = Modifier.height(8.dp))

            Button(
                onClick = {
                    fetchUserEmailAndSendEmail(case.userId, firestore, context)
                },
                modifier = Modifier.padding(top = 8.dp)
            ) {
                Text("Email User")
            }

            Button(
                onClick = {
                    initiatePhoneCall(case.userId, context) // Assuming userId is a phone number
                },
                modifier = Modifier.padding(top = 8.dp)
            ) {
                Text("Call User")
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

// Function to send email
fun fetchUserEmailAndSendEmail(userId: String, firestore: FirebaseFirestore, context: Context) {
    firestore.collection("users").document(userId).get().addOnSuccessListener { document ->
        if (document != null && document.exists()) {
            val email = document.getString("email")
            if (email != null) {
                val intent = Intent(Intent.ACTION_SENDTO).apply {
                    data = Uri.parse("mailto:$email")
                    putExtra(Intent.EXTRA_SUBJECT, "Case Follow-up")
                    putExtra(Intent.EXTRA_TEXT, "Hello,")
                }
                context.startActivity(Intent.createChooser(intent, "Send email"))
            }
        }
    }
}

// Function to initiate a phone call
fun initiatePhoneCall(phoneNumber: String, context: Context) {
    val intent = Intent(Intent.ACTION_DIAL).apply {
        data = Uri.parse("tel:$phoneNumber")
    }
    context.startActivity(intent)
}
