package com.example.virtuecase.ui.screens

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.tasks.await

@Composable
fun NotificationsScreen(navController: NavHostController, firestore: FirebaseFirestore) {
    var notifications by remember { mutableStateOf(emptyList<Notification>()) }
    var isLoading by remember { mutableStateOf(true) }

    // Fetch notifications when the screen is loaded
    LaunchedEffect(Unit) {
        notifications = fetchNotifications(firestore)
        isLoading = false
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.Top,
        horizontalAlignment = Alignment.Start
    ) {
        Text("Notifications", style = MaterialTheme.typography.headlineMedium)

        Spacer(modifier = Modifier.height(16.dp))

        if (isLoading) {
            CircularProgressIndicator(modifier = Modifier.align(Alignment.CenterHorizontally))
        } else {
            if (notifications.isEmpty()) {
                Text("No notifications available.", style = MaterialTheme.typography.bodyMedium)
            } else {
                LazyColumn {
                    items(notifications) { notification ->
                        NotificationItem(notification) {
                            // Navigate to details screen or handle notification click
                        }
                    }
                }
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        // Back button
        Button(onClick = { navController.popBackStack() }) {
            Text("Back to Home")
        }
    }
}

// Function to fetch notifications from Firestore
suspend fun fetchNotifications(firestore: FirebaseFirestore): List<Notification> {
    return try {
        val snapshot = firestore.collection("notifications").get().await()
        snapshot.documents.mapNotNull { it.toObject(Notification::class.java) }
    } catch (e: Exception) {
        emptyList()
    }
}

// Data class to represent a Notification
data class Notification(
    val title: String = "",
    val message: String = "",
    val timestamp: Long = System.currentTimeMillis()
)

// Composable function to display a single notification item
@Composable
fun NotificationItem(notification: Notification, onClick: () -> Unit) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 8.dp)
            .clickable(onClick = onClick), // Handle click
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Text(text = notification.title, style = MaterialTheme.typography.headlineMedium)
            Spacer(modifier = Modifier.height(4.dp))
            Text(text = notification.message, style = MaterialTheme.typography.bodySmall)
            Spacer(modifier = Modifier.height(4.dp))
            Text(text = "Received on: ${notification.timestamp}", style = MaterialTheme.typography.bodySmall)
        }
    }
}

