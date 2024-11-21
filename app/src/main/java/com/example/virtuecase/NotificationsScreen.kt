package com.example.virtuecase.ui.screens

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.text.BasicText
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController

// Sample data class for a notification
data class Notification(
    val title: String,
    val description: String,
    val timestamp: String,
    val isRead: Boolean
)

@Composable
fun NotificationsScreen(navController: NavHostController) {
    // Sample list of notifications (replace this with real data from Firebase or a ViewModel)
    val notifications = listOf(
        Notification("Case Status Update", "Your case has been marked as resolved.", "2 hours ago", false),
        Notification("New Comment on Your Case", "The investigator left a comment on your case.", "1 day ago", true),
        Notification("Case Assigned to Investigator", "Your case has been assigned to an investigator.", "3 days ago", false)
    )

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        Text("Notifications", style = MaterialTheme.typography.headlineMedium)

        Spacer(modifier = Modifier.height(16.dp))

        // If no notifications are present, show an empty state message
        if (notifications.isEmpty()) {
            Text("No new notifications", style = MaterialTheme.typography.bodyLarge)
        } else {
            // LazyColumn for a scrollable list of notifications
            LazyColumn(
                modifier = Modifier.fillMaxSize(),
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                items(notifications.size) { index ->
                    val notification = notifications[index]
                    NotificationItem(notification)
                }
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        // "Clear All" Button - You can implement the logic to clear notifications here
        Button(onClick = { /* Add clear all functionality */ }) {
            Text("Clear All")
        }

        Spacer(modifier = Modifier.height(16.dp))

        // Back button to navigate to the previous screen
        Button(onClick = { navController.popBackStack() }) {
            Text("Back to Home")
        }
    }
}

@Composable
fun NotificationItem(notification: Notification) {
    // Notification item UI with conditional formatting based on read/unread status
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(12.dp)
            .let {
                if (notification.isRead) it else it.padding(8.dp) // Apply different padding or styles for unread
            }
    ) {
        Text(
            text = notification.title,
            style = MaterialTheme.typography.bodyLarge,
            modifier = Modifier.padding(bottom = 4.dp)
        )
        Text(
            text = notification.description,
            style = MaterialTheme.typography.bodyMedium,
            modifier = Modifier.padding(bottom = 8.dp)
        )
        Text(
            text = notification.timestamp,
            style = MaterialTheme.typography.bodySmall,
            modifier = Modifier.padding(bottom = 8.dp)
        )
    }
}
