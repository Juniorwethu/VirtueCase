package com.example.virtuecase.ui
import androidx.compose.foundation.layout.*
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore

@Composable
fun HRProfileScreen(navController: NavHostController, userId: String) {
    val firestore = FirebaseFirestore.getInstance()
    var userDetails by remember { mutableStateOf<Map<String, Any>?>(null) }
    var errorMessage by remember { mutableStateOf("") }
    var isLoading by remember { mutableStateOf(true) }
    val auth = FirebaseAuth.getInstance()

    // Fetch HR Profile from Firestore
    LaunchedEffect(key1 = userId) {
        firestore.collection("users").document(userId).get()
            .addOnSuccessListener { document ->
                if (document.exists()) {
                    userDetails = document.data
                } else {
                    errorMessage = "User not found."
                }
                isLoading = false
            }
            .addOnFailureListener { exception ->
                errorMessage = exception.message ?: "An error occurred."
                isLoading = false
            }
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        when {
            isLoading -> {
                Text("Loading...", color = Color.Gray)
            }
            errorMessage.isNotEmpty() -> {
                Text("Error: $errorMessage", color = Color.Red)
            }
            userDetails != null -> {
                // Display user profile details
                Text("HR Profile", style = androidx.compose.material3.MaterialTheme.typography.headlineMedium)
                Spacer(modifier = Modifier.height(16.dp))
                Text("Name: ${userDetails!!["name"] ?: "N/A"}")
                Text("Email: ${userDetails!!["email"] ?: "N/A"}")
                Text("Phone: ${userDetails!!["phoneNumber"] ?: "N/A"}")
                Text("Role: ${userDetails!!["role"] ?: "N/A"}")
                Spacer(modifier = Modifier.height(24.dp))

                // Navigate back to HR Home
                Button(onClick = { navController.navigate("hrHome") }) {
                    Text("Back to Dashboard")
                }
                Spacer(modifier = Modifier.height(16.dp))

                // Logout Button
                Button(
                    onClick = {
                        auth.signOut()
                        navController.navigate("login") {
                            popUpTo(0) // Clear back stack to prevent navigating back after logout
                        }
                    },
                    colors = androidx.compose.material3.ButtonDefaults.buttonColors(Color.Red)
                ) {
                    Text("Logout", color = Color.White)
                }
            }
        }
    }
}
