package com.example.virtuecase.ui.screens

import androidx.compose.foundation.layout.*
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController

@Composable
fun CaseHistoryScreen(navController: NavHostController) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.Top,
        horizontalAlignment = Alignment.Start
    ) {
        Text("Case History", style = MaterialTheme.typography.titleLarge)

        // Replace with actual case history data
        Spacer(modifier = Modifier.height(16.dp))
        Text("List of submitted cases will appear here.", style = MaterialTheme.typography.bodyMedium)

        // Example button to go back to the user home screen
        Spacer(modifier = Modifier.height(16.dp))
        Button(onClick = { navController.popBackStack() }) {
            Text("Back to Home")
        }
    }
}
