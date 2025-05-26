package com.example.virtuecase.ui.screens

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.material.*
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.RadioButton
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import com.google.firebase.auth.FirebaseAuth
import kotlinx.coroutines.launch
import com.example.virtuecase.R

@Composable
fun LoginScreen(navController: NavHostController) {
    val email = remember { mutableStateOf("") }
    val password = remember { mutableStateOf("") }
    val loginError = remember { mutableStateOf("") }
    val coroutineScope = rememberCoroutineScope()

    // Input validation states
    val isValidEmail = remember { mutableStateOf(true) }
    val isValidPassword = remember { mutableStateOf(true) }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.Center
    ) {
        // Logo
        Image(
            painter = painterResource(id = R.drawable.logo),
            contentDescription = "Logo",
            modifier = Modifier
                .fillMaxWidth()
                .padding(bottom = 16.dp)
        )

        // Welcome Message
        Text(
            text = "Welcome to VirtueCase!",
            style = MaterialTheme.typography.bodyLarge,
            modifier = Modifier.padding(bottom = 16.dp)
        )

        // Email Input
        TextField(
            value = email.value,
            onValueChange = { email.value = it },
            label = { Text("Email") },
            modifier = Modifier.fillMaxWidth(),
            isError = !isValidEmail.value
        )
        if (!isValidEmail.value) {
            Text("Please enter a valid email address", color = MaterialTheme.colorScheme.error)
        }
        Spacer(modifier = Modifier.height(8.dp))

        // Password Input
        TextField(
            value = password.value,
            onValueChange = { password.value = it },
            label = { Text("Password") },
            visualTransformation = PasswordVisualTransformation(),
            modifier = Modifier.fillMaxWidth(),
            isError = !isValidPassword.value
        )
        if (!isValidPassword.value) {
            Text("Password must be at least 6 characters", color = MaterialTheme.colorScheme.error)
        }
        Spacer(modifier = Modifier.height(16.dp))

        // User Type Selector
        var isHRUser by remember { mutableStateOf(false) }
        Row(modifier = Modifier.fillMaxWidth()) {
            Text("User Type: ")
            Spacer(modifier = Modifier.width(8.dp))
            RadioButton(
                selected = !isHRUser,
                onClick = { isHRUser = false }
            )
            Text("User")
            Spacer(modifier = Modifier.width(8.dp))
            RadioButton(
                selected = isHRUser,
                onClick = { isHRUser = true }
            )
            Text("HR User")
        }
        Spacer(modifier = Modifier.height(16.dp))

        // Login Button
        Button(
            onClick = {
                // Input validation logic
                isValidEmail.value =
                    android.util.Patterns.EMAIL_ADDRESS.matcher(email.value).matches()
                isValidPassword.value = password.value.length >= 6

                if (isValidEmail.value && isValidPassword.value) {
                    coroutineScope.launch {
                        FirebaseAuth.getInstance()
                            .signInWithEmailAndPassword(email.value, password.value)
                            .addOnCompleteListener { task ->
                                if (task.isSuccessful) {
                                    // Navigate based on user type
                                    if (isHRUser) {
                                        navController.navigate("hrHome")
                                    } else {
                                        navController.navigate("userHome")
                                    }
                                } else {
                                    loginError.value = task.exception?.message ?: "Login failed"
                                }
                            }
                    }
                }
            },
            modifier = Modifier.fillMaxWidth()
        ) {
            Text("Login", color = Color.White)
        }
        Spacer(modifier = Modifier.height(8.dp))

        // Error message
        if (loginError.value.isNotEmpty()) {
            Text("Error: ${loginError.value}", color = MaterialTheme.colorScheme.error)
        }
        Spacer(modifier = Modifier.height(16.dp))

        // Navigate to Sign Up
        Button(
            onClick = { navController.navigate("signup") },
            modifier = Modifier.fillMaxWidth()
        ) {
            Text("Sign Up")
        }
    }
}

