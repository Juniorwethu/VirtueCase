package com.example.virtuecase.ui.screens

import android.net.Uri
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.compose.foundation.Image
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.Alignment
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.draw.clip
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import coil.compose.rememberImagePainter
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageReference
import com.example.virtuecase.R
import androidx.compose.ui.text.input.TextFieldValue

// Add the following imports for Icons:
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.ui.res.painterResource

@Composable
fun UserProfileScreen(navController: NavHostController, firestore: FirebaseFirestore, auth: FirebaseAuth) {
    val currentUser = auth.currentUser
    var userData by remember { mutableStateOf<UserProfile?>(null) }
    var isLoading by remember { mutableStateOf(true) }
    var errorMessage by remember { mutableStateOf<String?>(null) }
    var imageUri by remember { mutableStateOf<Uri?>(null) }
    var isEditing by remember { mutableStateOf(false) }

    // Editable fields for user data
    var name by remember { mutableStateOf(TextFieldValue()) }
    var surname by remember { mutableStateOf(TextFieldValue()) }
    var phoneNumber by remember { mutableStateOf(TextFieldValue()) }

    val pickImageLauncher = rememberLauncherForActivityResult(ActivityResultContracts.GetContent()) { uri: Uri? ->
        uri?.let { selectedUri ->
            imageUri = selectedUri
            currentUser?.uid?.let { userId ->
                uploadProfileImage(selectedUri, firestore, userId)
            }
        }
    }

    LaunchedEffect(currentUser?.uid) {
        currentUser?.uid?.let { userId ->
            firestore.collection("users").document(userId).get()
                .addOnSuccessListener { document ->
                    if (document.exists()) {
                        userData = document.toObject(UserProfile::class.java)
                        name = TextFieldValue(userData?.name ?: "")
                        surname = TextFieldValue(userData?.surname ?: "")
                        phoneNumber = TextFieldValue(userData?.phoneNumber ?: "")
                    } else {
                        errorMessage = "User not found"
                    }
                    isLoading = false
                }
                .addOnFailureListener { exception ->
                    errorMessage = "Error fetching data: ${exception.message}"
                    isLoading = false
                }
        }
    }

    Column(
        modifier = Modifier.fillMaxSize().padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(8.dp),
        horizontalAlignment = Alignment.Start
    ) {
        // Back button and logo at the top of the screen
        Row(modifier = Modifier.fillMaxWidth()) {
            IconButton(onClick = { navController.popBackStack() }) {
                Icon(Icons.Filled.ArrowBack, contentDescription = "Back")
            }
            Spacer(modifier = Modifier.width(16.dp))
            Image(painter = painterResource(id = R.drawable.logo), contentDescription = "Logo")
        }

        // Profile Image
        val profileImagePainter = rememberImagePainter(
            data = imageUri ?: userData?.profileImageUrl,
            builder = { placeholder(R.drawable.placeholder) }
        )

        Image(
            painter = profileImagePainter,
            contentDescription = "Profile Picture",
            modifier = Modifier
                .size(120.dp)
                .clip(CircleShape)
                .border(2.dp, MaterialTheme.colorScheme.primary, CircleShape)
                .padding(4.dp)
                .clickable {
                    pickImageLauncher.launch("image/*") // Launch the image picker
                },
            contentScale = ContentScale.Crop
        )

        // Display user data or loading state
        if (isLoading) {
            Text("Loading user data...")
        } else {
            if (errorMessage != null) {
                Text("Error: $errorMessage", color = MaterialTheme.colorScheme.error)
            } else {
                if (isEditing) {
                    // Editable fields when in editing mode
                    TextField(
                        value = name,
                        onValueChange = { name = it },
                        label = { Text("Name") },
                        modifier = Modifier.fillMaxWidth()
                    )
                    TextField(
                        value = surname,
                        onValueChange = { surname = it },
                        label = { Text("Surname") },
                        modifier = Modifier.fillMaxWidth()
                    )
                    TextField(
                        value = phoneNumber,
                        onValueChange = { phoneNumber = it },
                        label = { Text("Phone Number") },
                        modifier = Modifier.fillMaxWidth()
                    )

                    // Save button to update the user info
                    Button(onClick = {
                        userData?.let {
                            val updatedUser = it.copy(
                                name = name.text,
                                surname = surname.text,
                                phoneNumber = phoneNumber.text
                            )
                            saveUserDataToFirestore(updatedUser, firestore, currentUser?.uid)
                        }
                        isEditing = false
                    }) {
                        Text("Save")
                    }
                } else {
                    // Display user data when not editing
                    userData?.let {
                        Text("Name: ${it.name} ${it.surname}")
                        Text("Email: ${it.email}")
                        Text("Phone Number: ${it.phoneNumber}")
                        Text("Role: ${it.role}")
                    }

                    // Edit button
                    Button(onClick = { isEditing = true }) {
                        Text("Edit")
                    }
                }
            }
        }

        // Logout button
        Button(onClick = {
            auth.signOut()
            navController.navigate("login") {
                popUpTo("login") { inclusive = true }
            }
        }) {
            Text("Logout")
        }
    }
}

// Function to upload the image to Firebase Storage
fun uploadProfileImage(imageUri: Uri, firestore: FirebaseFirestore, userId: String?) {
    userId?.let { uid ->
        val storageRef = FirebaseStorage.getInstance().reference
        val profileImagesRef: StorageReference = storageRef.child("profile_images/$uid.jpg")

        profileImagesRef.putFile(imageUri)
            .addOnSuccessListener {
                profileImagesRef.downloadUrl.addOnSuccessListener { uri ->
                    val profileImageUrl = uri.toString()
                    updateProfileImageUrlInFirestore(firestore, uid, profileImageUrl)
                }
            }
            .addOnFailureListener { exception ->
                // Handle upload failure
            }
    }
}

// Function to update Firestore with the new profile image URL
fun updateProfileImageUrlInFirestore(firestore: FirebaseFirestore, userId: String, profileImageUrl: String) {
    firestore.collection("users").document(userId)
        .update("profileImageUrl", profileImageUrl)
        .addOnSuccessListener {
            // Profile image URL updated successfully
        }
        .addOnFailureListener {
            // Handle update failure
        }
}

// Function to save updated user data to Firestore
fun saveUserDataToFirestore(updatedUser: UserProfile, firestore: FirebaseFirestore, userId: String?) {
    userId?.let { uid ->
        firestore.collection("users").document(uid)
            .set(updatedUser)
            .addOnSuccessListener {
                // Successfully updated user data
            }
            .addOnFailureListener { exception ->
                // Handle update failure
            }
    }
}

// Data class for user profile (Firestore model)
data class UserProfile(
    val name: String = "",
    val surname: String = "",
    val email: String = "",
    val phoneNumber: String = "",
    val role: String = "",
    val profileImageUrl: String? = null // URL for the profile image, can be null if not set
)
