package com.example.virtuecase.ui

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonColors
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.Modifier
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

        ButtonCard(text = "View Cases", backgroundColor = Color.Blue) {
            navController.navigate("viewCases")
        }
        Spacer(modifier = Modifier.height(16.dp))
        ButtonCard(text = "Manage Users", backgroundColor = Color.Blue) {
            navController.navigate("manageUsers")
        }
    }
}

@Composable
fun ButtonCard(text: String, backgroundColor: Color, onClick: () -> Unit) {
    Button(
        onClick = onClick,
        colors = ButtonDefaults.buttonColors(backgroundColor),
        modifier = Modifier.fillMaxWidth() // This should work as intended
    ) {
        Text(text = text, color = Color.White)
    }
}


