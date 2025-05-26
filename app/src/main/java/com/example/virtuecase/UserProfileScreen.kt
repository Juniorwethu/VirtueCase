package com.example.virtuecase.ui.screens

import android.util.Log
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.material.*
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await
import com.example.virtuecase.R

@Composable
fun UserProfileScreen(navController: NavHostController, firestore: FirebaseFirestore, auth: FirebaseAuth) {
    var isLoading by remember { mutableStateOf(true) }
    var userData by remember { mutableStateOf<Map<String, Any>?>(null) }
    val coroutineScope = rememberCoroutineScope()

    // Fetch user data from Firestore
    LaunchedEffect(Unit) {
        coroutineScope.launch {
            val user = auth.currentUser
            if (user != null) {
                try {
                    val document = firestore.collection("users").document(user.uid).get().await()
                    if (document.exists()) {
                        userData = document.data
                    }
                } catch (e: Exception) {
                    Log.e("UserProfileScreen", "Error fetching user data: ${e.message}")
                }
            }
            isLoading = false // Set loading to false after fetching
        }
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.Top,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        // Logo at the top
        Image(
            painter = painterResource(id = R.drawable.logo), // logo resource
            contentDescription = "Logo",
            modifier = Modifier.size(120.dp) // Adjust size as needed
        )

        Spacer(modifier = Modifier.height(32.dp))

        if (isLoading) {
            CircularProgressIndicator()
        } else {
            if (userData != null) {
                Text("Profile Information", style = MaterialTheme.typography.headlineMedium)

                Spacer(modifier = Modifier.height(16.dp))

                Text("Name: ${userData!!["name"]}", style = MaterialTheme.typography.bodyMedium)
                Text("Surname: ${userData!!["surname"]}", style = MaterialTheme.typography.bodyMedium)
                Text("Phone Number: ${userData!!["phoneNumber"]}", style = MaterialTheme.typography.bodyMedium)
                Text("Email: ${userData!!["email"]}", style = MaterialTheme.typography.bodyMedium)
            } else {
                Text("No user data found.", style = MaterialTheme.typography.bodyMedium)
            }
        }

        Spacer(modifier = Modifier.height(32.dp))

        // Back button
        Button(onClick = { navController.popBackStack() }) {
            Text("Back to Home")
        }
    }
}
