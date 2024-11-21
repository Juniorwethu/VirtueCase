package com.example.virtuecase.ui.screens

import com.example.virtuecase.data.CaseSubmission
import android.net.Uri
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
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
import androidx.compose.ui.text.font.FontWeight
import com.google.firebase.storage.StorageReference
import com.example.virtuecase.R
import kotlinx.coroutines.launch
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Scaffold
import com.google.firebase.auth.FirebaseAuth

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SubmitCaseScreen(navController: NavHostController, firestore: FirebaseFirestore) {
    var firstName by remember { mutableStateOf("") }
    var lastName by remember { mutableStateOf("") }
    var natureOfComplaint by remember { mutableStateOf("") }
    var description by remember { mutableStateOf(TextFieldValue("")) }
    var incidentDate by remember { mutableStateOf("") }
    var involvedParties by remember { mutableStateOf("") }
    var caseType by remember { mutableStateOf("") }
    var region by remember { mutableStateOf("") }
    var complainantId by remember { mutableStateOf("") }
    var employerId by remember { mutableStateOf("") }

    // New fields
    var employerName by remember { mutableStateOf("") }
    var employerAddress by remember { mutableStateOf("") }
    var employerEmail by remember { mutableStateOf("") }
    var employerContactNumber by remember { mutableStateOf("") }
    var incomeSalary by remember { mutableStateOf("") }
    var occupation by remember { mutableStateOf("") }

    var documentUri by remember { mutableStateOf<Uri?>(null) }
    var caseNumber by remember { mutableStateOf("CAS-" + System.currentTimeMillis()) }

    val documentPicker = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.GetContent(),
        onResult = { uri -> documentUri = uri }
    )

    val buttonColor = Color(0xFF3E4B8A)
    val scrollState = rememberScrollState()
    val coroutineScope = rememberCoroutineScope()
    val snackbarHostState = remember { SnackbarHostState() }

    // Get current user's email (if logged in)
    val user = FirebaseAuth.getInstance().currentUser
    val userEmail = user?.email ?: ""  // Default to empty if not logged in

    Scaffold(snackbarHost = { SnackbarHost(hostState = snackbarHostState) }) { paddingValues ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .padding(16.dp),
            contentAlignment = Alignment.TopCenter
        ) {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .verticalScroll(scrollState),
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

                Text(
                    text = "CASE LOGGING FORM",
                    style = MaterialTheme.typography.headlineMedium.copy(
                        color = Color.Black,
                        fontWeight = FontWeight.Bold
                    ),
                    modifier = Modifier.padding(vertical = 8.dp)
                )

                Spacer(modifier = Modifier.height(16.dp))

                // First Name
                TextField(
                    value = firstName,
                    onValueChange = { firstName = it },
                    label = { Text("First Name") },
                    modifier = Modifier.fillMaxWidth(),
                    colors = TextFieldDefaults.textFieldColors(focusedIndicatorColor = buttonColor, cursorColor = buttonColor)
                )

                // Last Name
                TextField(
                    value = lastName,
                    onValueChange = { lastName = it },
                    label = { Text("Last Name") },
                    modifier = Modifier.fillMaxWidth(),
                    colors = TextFieldDefaults.textFieldColors(focusedIndicatorColor = buttonColor, cursorColor = buttonColor)
                )

                // User Email (Automatically filled)
                TextField(
                    value = userEmail,
                    onValueChange = {},
                    label = { Text("User Email") },
                    modifier = Modifier.fillMaxWidth(),
                    enabled = false,  // Disable editing
                    colors = TextFieldDefaults.textFieldColors(focusedIndicatorColor = buttonColor, cursorColor = buttonColor)
                )

                // Nature of Complaint
                TextField(
                    value = natureOfComplaint,
                    onValueChange = { natureOfComplaint = it },
                    label = { Text("Nature of Complaint") },
                    modifier = Modifier.fillMaxWidth(),
                    colors = TextFieldDefaults.textFieldColors(focusedIndicatorColor = buttonColor, cursorColor = buttonColor)
                )

                // Description
                TextField(
                    value = description,
                    onValueChange = { description = it },
                    label = { Text("Description") },
                    modifier = Modifier.fillMaxWidth(),
                    colors = TextFieldDefaults.textFieldColors(focusedIndicatorColor = buttonColor, cursorColor = buttonColor),
                    maxLines = 5
                )

                // Incident Date
                TextField(
                    value = incidentDate,
                    onValueChange = { incidentDate = it },
                    label = { Text("Date of Incident") },
                    modifier = Modifier.fillMaxWidth(),
                    colors = TextFieldDefaults.textFieldColors(focusedIndicatorColor = buttonColor, cursorColor = buttonColor)
                )

                // Individuals Involved
                TextField(
                    value = involvedParties,
                    onValueChange = { involvedParties = it },
                    label = { Text("Individuals Involved") },
                    modifier = Modifier.fillMaxWidth(),
                    colors = TextFieldDefaults.textFieldColors(focusedIndicatorColor = buttonColor, cursorColor = buttonColor)
                )

                // Case Type
                TextField(
                    value = caseType,
                    onValueChange = { caseType = it },
                    label = { Text("Case Type") },
                    modifier = Modifier.fillMaxWidth(),
                    colors = TextFieldDefaults.textFieldColors(focusedIndicatorColor = buttonColor, cursorColor = buttonColor)
                )

                // Region
                TextField(
                    value = region,
                    onValueChange = { region = it },
                    label = { Text("Region") },
                    modifier = Modifier.fillMaxWidth(),
                    colors = TextFieldDefaults.textFieldColors(focusedIndicatorColor = buttonColor, cursorColor = buttonColor)
                )

                // Complainant ID
                TextField(
                    value = complainantId,
                    onValueChange = { complainantId = it },
                    label = { Text("Complainant ID") },
                    modifier = Modifier.fillMaxWidth(),
                    colors = TextFieldDefaults.textFieldColors(focusedIndicatorColor = buttonColor, cursorColor = buttonColor)
                )

                // Employer ID
                TextField(
                    value = employerId,
                    onValueChange = { employerId = it },
                    label = { Text("Employer ID") },
                    modifier = Modifier.fillMaxWidth(),
                    colors = TextFieldDefaults.textFieldColors(focusedIndicatorColor = buttonColor, cursorColor = buttonColor)
                )

                // Employer Name
                TextField(
                    value = employerName,
                    onValueChange = { employerName = it },
                    label = { Text("Employer Name") },
                    modifier = Modifier.fillMaxWidth(),
                    colors = TextFieldDefaults.textFieldColors(focusedIndicatorColor = buttonColor, cursorColor = buttonColor)
                )

                // Employer Address
                TextField(
                    value = employerAddress,
                    onValueChange = { employerAddress = it },
                    label = { Text("Employer Address") },
                    modifier = Modifier.fillMaxWidth(),
                    colors = TextFieldDefaults.textFieldColors(focusedIndicatorColor = buttonColor, cursorColor = buttonColor)
                )

                // Employer Email
                TextField(
                    value = employerEmail,
                    onValueChange = { employerEmail = it },
                    label = { Text("Employer Email") },
                    modifier = Modifier.fillMaxWidth(),
                    colors = TextFieldDefaults.textFieldColors(focusedIndicatorColor = buttonColor, cursorColor = buttonColor)
                )

                // Employer Contact Number
                TextField(
                    value = employerContactNumber,
                    onValueChange = { employerContactNumber = it },
                    label = { Text("Employer Contact Number") },
                    modifier = Modifier.fillMaxWidth(),
                    colors = TextFieldDefaults.textFieldColors(focusedIndicatorColor = buttonColor, cursorColor = buttonColor)
                )

                // Income / Salary
                TextField(
                    value = incomeSalary,
                    onValueChange = { incomeSalary = it },
                    label = { Text("Income/Salary") },
                    modifier = Modifier.fillMaxWidth(),
                    colors = TextFieldDefaults.textFieldColors(focusedIndicatorColor = buttonColor, cursorColor = buttonColor)
                )

                // Occupation
                TextField(
                    value = occupation,
                    onValueChange = { occupation = it },
                    label = { Text("Occupation") },
                    modifier = Modifier.fillMaxWidth(),
                    colors = TextFieldDefaults.textFieldColors(focusedIndicatorColor = buttonColor, cursorColor = buttonColor)
                )

                // Submit Button
                Button(
                    onClick = {
                        val caseSubmission = CaseSubmission(
                            caseNumber,
                            firstName,
                            lastName,
                            userEmail,
                            natureOfComplaint,
                            description.text,
                            incidentDate,
                            involvedParties,
                            caseType,
                            region,
                            complainantId,
                            employerId,
                            employerName,
                            employerAddress,
                            employerEmail,
                            employerContactNumber,
                            incomeSalary,
                            occupation
                        )

                        // Upload case to Firestore
                        firestore.collection("cases")
                            .add(caseSubmission)
                            .addOnSuccessListener {
                                coroutineScope.launch {
                                    snackbarHostState.showSnackbar("Case submitted successfully!")
                                }
                            }
                            .addOnFailureListener { exception ->
                                coroutineScope.launch {
                                    snackbarHostState.showSnackbar("Failed to submit case: ${exception.message}")
                                }
                            }
                    },
                    colors = ButtonDefaults.buttonColors(containerColor = buttonColor),
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 16.dp)
                ) {
                    Text(text = "Submit Case", color = Color.White)
                }
            }
        }
    }
}
