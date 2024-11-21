package com.example.virtuecase.ui

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import com.example.virtuecase.R // Ensure your logo is in the drawable resources

@Composable
fun UserHomeScreen(navController: NavHostController) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.Top,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        // Logo at the top
        Image(
            painter = painterResource(id = R.drawable.logo),
            contentDescription = "App Logo",
            modifier = Modifier.size(128.dp)
        )

        Spacer(modifier = Modifier.height(16.dp))

        Text("User Dashboard", style = MaterialTheme.typography.headlineMedium)

        Spacer(modifier = Modifier.height(16.dp))

        UserButtonCard(text = "Submit a New Case") {
            navController.navigate("submitCase")
        }
        Spacer(modifier = Modifier.height(16.dp))

        UserButtonCard(text = "View Case History") {
            navController.navigate("caseHistory")
        }
        Spacer(modifier = Modifier.height(16.dp))

        UserButtonCard(text = "View Notifications") {
            navController.navigate("notifications")
        }
        Spacer(modifier = Modifier.height(16.dp))

        UserButtonCard(text = "Edit Profile") {
            navController.navigate("userProfile")
        }
    }
}

@Composable
fun UserButtonCard(text: String, onClick: () -> Unit) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .height(60.dp)
            .padding(8.dp),
    ) {
        Button(
            onClick = onClick,
            colors = ButtonDefaults.buttonColors(Color(0xFF3E4B8A)),
            modifier = Modifier.fillMaxSize()
        ) {
            Text(text, color = Color.White)
        }
    }
}


