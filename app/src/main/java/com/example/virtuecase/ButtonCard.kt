package com.example.virtuecase.ui.screens

import androidx.compose.foundation.layout.*
import androidx.compose.material3.Button // Use material3 if you're using Material 3
import androidx.compose.material3.Text // Use material3 if you're using Material 3
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

@Composable
fun ButtonCard(text: String, onClick: () -> Unit) {
    Button(
        onClick = onClick,
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp)
    ) {
        Text(text = text)
    }
}

