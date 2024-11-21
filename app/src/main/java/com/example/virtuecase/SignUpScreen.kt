package com.example.virtuecase.ui.screens

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.material.*
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
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
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.launch
import com.example.virtuecase.R

@Composable
fun SignUpScreen(navController: NavHostController) {
    val email = remember { mutableStateOf("") }
    val password = remember { mutableStateOf("") }
    val confirmPassword = remember { mutableStateOf("") }
    val name = remember { mutableStateOf("") }
    val surname = remember { mutableStateOf("") }
    val phoneNumber = remember { mutableStateOf("") }
    val selectedRole = remember { mutableStateOf("user") } // Default role is regular user
    val signUpError = remember { mutableStateOf("") }
    val signUpSuccess = remember { mutableStateOf(false) }
    val coroutineScope = rememberCoroutineScope()

    // Input validation states
    val isValidEmail = remember { mutableStateOf(true) }
    val isValidPassword = remember { mutableStateOf(true) }
    val isValidConfirmPassword = remember { mutableStateOf(true) }
    val isValidName = remember { mutableStateOf(true) }
    val isValidSurname = remember { mutableStateOf(true) }
    val isValidPhoneNumber = remember { mutableStateOf(true) }

    // Set padding and spacing as constants
    val defaultPadding = 16.dp
    val fieldSpacing = 8.dp
    val buttonSpacing = 16.dp

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(defaultPadding),
        verticalArrangement = Arrangement.Center
    ) {
        // Logo at the top
        Image(
            painter = painterResource(id = R.drawable.logo), // Use your logo's resource ID
            contentDescription = "Logo",
            modifier = Modifier
                .fillMaxWidth()
                .padding(bottom = buttonSpacing)
        )

        // Top label "Fill in the details"
        Text(
            text = "Fill in the details",
            style = MaterialTheme.typography.headlineMedium,
            modifier = Modifier
                .padding(bottom = buttonSpacing)
                .fillMaxWidth(),
            color = MaterialTheme.colorScheme.primary
        )

        // Name Input
        TextField(
            value = name.value,
            onValueChange = { name.value = it },
            label = { Text("Name") },
            modifier = Modifier.fillMaxWidth(),
            isError = !isValidName.value
        )
        if (!isValidName.value) {
            Text("Please enter your name", color = MaterialTheme.colorScheme.error)
        }
        Spacer(modifier = Modifier.height(fieldSpacing))

        // Surname Input
        TextField(
            value = surname.value,
            onValueChange = { surname.value = it },
            label = { Text("Surname") },
            modifier = Modifier.fillMaxWidth(),
            isError = !isValidSurname.value
        )
        if (!isValidSurname.value) {
            Text("Please enter your surname", color = MaterialTheme.colorScheme.error)
        }
        Spacer(modifier = Modifier.height(fieldSpacing))

        // Phone Number Input
        TextField(
            value = phoneNumber.value,
            onValueChange = { phoneNumber.value = it },
            label = { Text("Phone Number") },
            modifier = Modifier.fillMaxWidth(),
            isError = !isValidPhoneNumber.value
        )
        if (!isValidPhoneNumber.value) {
            Text("Please enter a valid phone number", color = MaterialTheme.colorScheme.error)
        }
        Spacer(modifier = Modifier.height(fieldSpacing))

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
        Spacer(modifier = Modifier.height(fieldSpacing))

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
        Spacer(modifier = Modifier.height(fieldSpacing))

        // Confirm Password Input
        TextField(
            value = confirmPassword.value,
            onValueChange = { confirmPassword.value = it },
            label = { Text("Confirm Password") },
            visualTransformation = PasswordVisualTransformation(),
            modifier = Modifier.fillMaxWidth(),
            isError = !isValidConfirmPassword.value
        )
        if (!isValidConfirmPassword.value) {
            Text("Passwords do not match", color = MaterialTheme.colorScheme.error)
        }
        Spacer(modifier = Modifier.height(fieldSpacing))

        // Sign Up Button
        Button(
            onClick = {
                // Validate inputs
                isValidName.value = name.value.isNotEmpty()
                isValidSurname.value = surname.value.isNotEmpty()
                isValidPhoneNumber.value = phoneNumber.value.isNotEmpty() // Add your phone number validation here
                isValidEmail.value = android.util.Patterns.EMAIL_ADDRESS.matcher(email.value).matches()
                isValidPassword.value = password.value.length >= 6
                isValidConfirmPassword.value = password.value == confirmPassword.value

                if (isValidName.value && isValidSurname.value && isValidPhoneNumber.value &&
                    isValidEmail.value && isValidPassword.value && isValidConfirmPassword.value) {
                    coroutineScope.launch {
                        FirebaseAuth.getInstance().createUserWithEmailAndPassword(email.value, password.value)
                            .addOnCompleteListener { task ->
                                if (task.isSuccessful) {
                                    val user = FirebaseAuth.getInstance().currentUser
                                    // Save the user's details in Firestore
                                    val userData = hashMapOf(
                                        "email" to email.value,
                                        "name" to name.value,
                                        "surname" to surname.value,
                                        "phoneNumber" to phoneNumber.value,
                                        "role" to selectedRole.value // Either "user" or "hr"
                                    )
                                    user?.let {
                                        FirebaseFirestore.getInstance().collection("users")
                                            .document(user.uid)
                                            .set(userData)
                                            .addOnSuccessListener {
                                                // Show success message
                                                signUpSuccess.value = true
                                                // Navigate to login after a short delay
                                                navController.navigate("login") {
                                                    popUpTo("signup") { inclusive = true }
                                                }
                                            }
                                            .addOnFailureListener {
                                                signUpError.value = it.message ?: "Sign-up failed"
                                            }
                                    }
                                } else {
                                    signUpError.value = task.exception?.message ?: "Sign-up failed"
                                }
                            }
                    }
                }
            },
            modifier = Modifier.fillMaxWidth()
        ) {
            Text("Sign Up", color = Color.White)
        }
        Spacer(modifier = Modifier.height(buttonSpacing))

        // Display error if sign-up fails
        if (signUpError.value.isNotEmpty()) {
            Text("Error: ${signUpError.value}", color = MaterialTheme.colorScheme.error)
        }

        // Back to Login Button
        Button(
            onClick = { navController.navigate("login") },
            modifier = Modifier.fillMaxWidth()
        ) {
            Text("Back to Login")
        }
    }
} 