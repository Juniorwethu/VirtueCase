package com.example.virtuecase.ui.screens

import android.app.Activity
import android.content.Intent
import android.net.Uri
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageReference
import com.example.virtuecase.R

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SubmitCaseScreen(navController: NavHostController, firestore: FirebaseFirestore) {
    var natureOfComplaint by remember { mutableStateOf("") }
    var description by remember { mutableStateOf(TextFieldValue("")) }
    var incidentDate by remember { mutableStateOf("") }
    var involvedParties by remember { mutableStateOf("") }
    var documentUri by remember { mutableStateOf<Uri?>(null) }
    var responseMessage by remember { mutableStateOf("") }

    // Launcher for document picker
    val documentPicker = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.GetContent(),
        onResult = { uri ->
            if (uri != null) {
                documentUri = uri
            }
        }
    )

    val buttonColor = Color(0xFF3E4B8A)

    Box(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        contentAlignment = Alignment.TopCenter
    ) {
        Column(
            modifier = Modifier.fillMaxSize(),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            IconButton(
                onClick = { navController.popBackStack() },
                modifier = Modifier.padding(8.dp).align(Alignment.Start)
            ) {
                Icon(imageVector = Icons.Default.ArrowBack, contentDescription = "Back", tint = buttonColor)
            }

            Spacer(modifier = Modifier.height(16.dp))

            Image(
                painter = painterResource(id = R.drawable.logo),
                contentDescription = "Logo",
                modifier = Modifier.size(128.dp)
            )

            Spacer(modifier = Modifier.height(16.dp))

            Text(text = "Submit a New Case", style = MaterialTheme.typography.headlineMedium, color = Color.White)
            Spacer(modifier = Modifier.height(16.dp))

            TextField(
                value = natureOfComplaint,
                onValueChange = { natureOfComplaint = it },
                label = { Text("Nature of Complaint") },
                modifier = Modifier.fillMaxWidth(),
                colors = TextFieldDefaults.textFieldColors(
                    focusedIndicatorColor = buttonColor,
                    cursorColor = buttonColor
                )
            )
            Spacer(modifier = Modifier.height(8.dp))

            TextField(
                value = description,
                onValueChange = { description = it },
                label = { Text("Description") },
                modifier = Modifier.fillMaxWidth(),
                maxLines = 5,
                colors = TextFieldDefaults.textFieldColors(
                    focusedIndicatorColor = buttonColor,
                    cursorColor = buttonColor
                )
            )
            Spacer(modifier = Modifier.height(8.dp))

            TextField(
                value = incidentDate,
                onValueChange = { incidentDate = it },
                label = { Text("Incident Date (YYYY-MM-DD)") },
                modifier = Modifier.fillMaxWidth(),
                colors = TextFieldDefaults.textFieldColors(
                    focusedIndicatorColor = buttonColor,
                    cursorColor = buttonColor
                )
            )
            Spacer(modifier = Modifier.height(8.dp))

            TextField(
                value = involvedParties,
                onValueChange = { involvedParties = it },
                label = { Text("Involved Parties") },
                modifier = Modifier.fillMaxWidth(),
                colors = TextFieldDefaults.textFieldColors(
                    focusedIndicatorColor = buttonColor,
                    cursorColor = buttonColor
                )
            )
            Spacer(modifier = Modifier.height(8.dp))

            Button(
                onClick = {
                    // Launch document picker to select a file
                    documentPicker.launch("*/*")
                },
                colors = ButtonDefaults.buttonColors(buttonColor)
            ) {
                Text(text = "Upload Document", color = Color.White)
            }

            Spacer(modifier = Modifier.height(16.dp))

            Button(
                onClick = {
                    uploadDocumentAndSaveCase(
                        firestore,
                        natureOfComplaint,
                        description.text,
                        incidentDate,
                        involvedParties,
                        documentUri
                    ) { message ->
                        responseMessage = message
                    }
                },
                colors = ButtonDefaults.buttonColors(buttonColor)
            ) {
                Text(text = "Submit Case", color = Color.White)
            }

            if (responseMessage.isNotEmpty()) {
                Spacer(modifier = Modifier.height(16.dp))
                Text(text = responseMessage, style = MaterialTheme.typography.bodyMedium, color = Color.White)
            }
        }
    }
}

private fun uploadDocumentAndSaveCase(
    firestore: FirebaseFirestore,
    natureOfComplaint: String,
    description: String,
    incidentDate: String,
    involvedParties: String,
    documentUri: Uri?,
    onResponse: (String) -> Unit
) {
    val caseData = hashMapOf(
        "nature" to natureOfComplaint,
        "description" to description,
        "date" to incidentDate,
        "parties" to involvedParties
    )

    if (documentUri != null) {
        val storageRef: StorageReference = FirebaseStorage.getInstance().reference.child("documents/${documentUri.lastPathSegment}")
        storageRef.putFile(documentUri)
            .addOnSuccessListener {
                storageRef.downloadUrl.addOnSuccessListener { downloadUrl ->
                    caseData["documentUrl"] = downloadUrl.toString()
                    firestore.collection("cases").add(caseData)
                        .addOnSuccessListener {
                            onResponse("Case submitted successfully!")
                        }
                        .addOnFailureListener { e ->
                            onResponse("Failed to submit case: ${e.message}")
                        }
                }.addOnFailureListener { e ->
                    onResponse("Failed to get document URL: ${e.message}")
                }
            }
            .addOnFailureListener { e ->
                onResponse("Document upload failed: ${e.message}")
            }
    } else {
        firestore.collection("cases").add(caseData)
            .addOnSuccessListener {
                onResponse("Case submitted successfully!")
            }
            .addOnFailureListener { e ->
                onResponse("Failed to submit case: ${e.message}")
            }
    }
}

