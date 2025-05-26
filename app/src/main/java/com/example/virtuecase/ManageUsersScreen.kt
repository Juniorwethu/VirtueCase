package com.example.virtuecase.ui.screens

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
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import com.example.virtuecase.R
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.tasks.await
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material3.Card
import androidx.compose.material3.CardElevation
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults

// Data class for User
data class User(
    val id: String = "",
    val name: String = "",
    val email: String = "",
    val role: String = ""
)

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ManageUsersScreen(navController: NavHostController, firestore: FirebaseFirestore) {
    var userList by remember { mutableStateOf<List<User>>(emptyList()) }
    var isLoading by remember { mutableStateOf(true) }

    // Fetch users from Firestore
    LaunchedEffect(Unit) {
        userList = fetchUsersFromFirestore(firestore)
        isLoading = false
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text(text = "Manage Users", color = Color.White) },
                navigationIcon = {
                    IconButton(onClick = { navController.popBackStack() }) {
                        Icon(Icons.Filled.ArrowBack, contentDescription = "Back", tint = Color.White)
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors()
            )
        }
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            // Add a logo at the top of the screen
            Image(
                painter = painterResource(id = R.drawable.logo), // Add your logo image here
                contentDescription = "Logo",
                modifier = Modifier
                    .size(120.dp)
                    .padding(16.dp)
            )

            Spacer(modifier = Modifier.height(16.dp))

            if (isLoading) {
                CircularProgressIndicator(color = Color.Black)
            } else {
                if (userList.isEmpty()) {
                    Text(text = "No users found.", color = Color.Black)
                } else {
                    LazyColumn(
                        modifier = Modifier.fillMaxSize(),
                        contentPadding = PaddingValues(16.dp),
                        verticalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        items(userList) { user ->
                            UserItem(user = user, firestore = firestore)
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun UserItem(user: User, firestore: FirebaseFirestore) {
    Card(
        modifier = Modifier.fillMaxWidth(),
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Column {
                Text(text = "Name: ${user.name}", style = MaterialTheme.typography.bodyMedium, color = Color.Black)
                Spacer(modifier = Modifier.height(8.dp))
                Text(text = "Email: ${user.email}", color = Color.Black)
                Spacer(modifier = Modifier.height(8.dp))
                Text(text = "Role: ${user.role}", color = Color.Black)
            }
            Row {
                IconButton(onClick = {
                    // Implement edit user functionality
                    // Call a function or navigate to a new screen for editing
                }) {
                    Icon(Icons.Filled.Edit, contentDescription = "Edit", tint = Color.Blue)
                }
                IconButton(onClick = {
                    // Implement delete user functionality
                    deleteUserFromFirestore(firestore, user.id)
                }) {
                    Icon(Icons.Filled.Delete, contentDescription = "Delete", tint = Color.Red)
                }
            }
        }
    }
}

// Function to fetch users from Firestore
suspend fun fetchUsersFromFirestore(firestore: FirebaseFirestore): List<User> {
    return try {
        val userSnapshot = firestore.collection("users").get().await()
        userSnapshot.documents.mapNotNull { doc ->
            doc.toObject(User::class.java)?.copy(id = doc.id)
        }
    } catch (e: Exception) {
        emptyList()
    }
}

// Function to delete user from Firestore
fun deleteUserFromFirestore(firestore: FirebaseFirestore, userId: String) {
    firestore.collection("users").document(userId).delete()
        .addOnSuccessListener {
            // Handle success, e.g., show a toast message or refresh the user list
        }
        .addOnFailureListener {
            // Handle failure, e.g., show an error message
        }
}
