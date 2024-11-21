package com.example.virtuecase.ui

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color.Companion.Blue
import androidx.navigation.NavHostController
import com.example.virtuecase.R

@Composable
fun HRHomeScreen(navController: NavHostController) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.Top,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        // Add logo at the top
        Image(
            painter = painterResource(id = R.drawable.logo),
            contentDescription = "Logo",
            modifier = Modifier
                .size(120.dp)
                .padding(bottom = 32.dp)
        )

        // Button to navigate to "View Cases"
        ButtonCard(text = "View Cases", backgroundColor = Blue) {
            navController.navigate("viewCases")
        }
        Spacer(modifier = Modifier.height(16.dp))

        // Button to navigate to "HR Profile"
        ButtonCard(text = "HR Profile", backgroundColor = Blue) {
            navController.navigate("hrProfile")
        }
        Spacer(modifier = Modifier.height(16.dp))

        // Button to navigate to "Case Statistics"
        ButtonCard(text = "Case Statistics", backgroundColor = Blue) {
            navController.navigate("caseStatistics")
        }
    }
}

@Composable
fun ButtonCard(text: String, backgroundColor: Color, onClick: () -> Unit) {
    Button(
        onClick = onClick,
        colors = ButtonDefaults.buttonColors(backgroundColor),
        modifier = Modifier.fillMaxWidth()
    ) {
        Text(text = text, color = Color.White)
    }
}
