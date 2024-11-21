package com.example.virtuecase.ui.screens

import android.content.Context
import android.content.Intent
import android.net.Uri
import android.util.Log
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import com.example.virtuecase.R
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.tasks.await

data class User(
    val id: String = "",
    val name: String = "",
    val email: String = "",
    val role: String = "",
    val phone: String = "" // Add phone number field
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
            // Add logo at the top of the screen
            Image(
                painter = painterResource(id = R.drawable.logo),
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
    val context = LocalContext.current

    Card(
        modifier = Modifier.fillMaxWidth()
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
                // Edit User (currently placeholder)
                IconButton(onClick = {
                    // Implement edit user functionality
                }) {
                    Icon(Icons.Filled.Edit, contentDescription = "Edit", tint = Color.Blue)
                }

                // Delete User
                IconButton(onClick = {
                    deleteUserFromFirestore(firestore, user.id)
                }) {
                    Icon(Icons.Filled.Delete, contentDescription = "Delete", tint = Color.Red)
                }

                // Call User
                IconButton(onClick = {
                    callUser(context, user.phone)
                }) {
                    Icon(Icons.Filled.Call, contentDescription = "Call", tint = Color.Green)
                }

                // Email User
                IconButton(onClick = {
                    emailUser(context, user.email)
                }) {
                    Icon(Icons.Filled.Email, contentDescription = "Email", tint = Color.Blue)
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
        Log.e("FirestoreError", "Error fetching users: ${e.message}")
        emptyList()
    }
}

// Function to delete user from Firestore
fun deleteUserFromFirestore(firestore: FirebaseFirestore, userId: String) {
    if (userId.isNotEmpty()) {
        firestore.collection("users").document(userId).delete()
            .addOnSuccessListener {
                Log.d("Firestore", "User deleted successfully")
            }
            .addOnFailureListener { e ->
                Log.e("FirestoreError", "Error deleting user: ${e.message}")
            }
    } else {
        Log.e("FirestoreError", "Invalid user ID")
    }
}

// Function to call user
fun callUser(context: Context, phoneNumber: String) {
    if (phoneNumber.isNotEmpty()) {
        val intent = Intent(Intent.ACTION_DIAL, Uri.parse("tel:$phoneNumber"))
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
        context.startActivity(intent)
    } else {
        Log.e("CallUserError", "Phone number is empty")
    }
}

// Function to email user
fun emailUser(context: Context, email: String) {
    if (email.isNotEmpty()) {
        val intent = Intent(Intent.ACTION_SENDTO, Uri.parse("mailto:$email"))
        intent.putExtra(Intent.EXTRA_SUBJECT, "Subject Here")
        intent.putExtra(Intent.EXTRA_TEXT, "Message Body Here")
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
        context.startActivity(intent)
    } else {
        Log.e("EmailUserError", "Email is empty")
    }
}
