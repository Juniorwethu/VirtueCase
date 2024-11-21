package com.example.virtuecase.ui

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.example.virtuecase.ui.screens.*
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import androidx.compose.ui.Modifier

@Composable
fun NavGraph(
    navController: NavHostController,
    firestore: FirebaseFirestore,
    auth: FirebaseAuth,
    innerPadding: PaddingValues // Accept padding here
) {
    NavHost(
        navController = navController,
        startDestination = "login",
        modifier = Modifier.padding(innerPadding) // Apply padding directly to the NavHost
    ) {
        composable("login") { LoginScreen(navController) }
        composable("signup") { SignUpScreen(navController) }
        composable("userHome") { UserHomeScreen(navController) }
        composable("hrHome") { HRHomeScreen(navController) }
        composable("viewCases") { ViewCasesScreen(navController, firestore) }
        composable("submitCase") { SubmitCaseScreen(navController, firestore) }
        composable("manageUsers") { ManageUsersScreen(navController, firestore) }

        composable("caseHistory") {
            val currentUser = auth.currentUser
            if (currentUser != null) {
                CaseHistoryScreen(
                    navController = navController,
                    firestore = firestore,
                    complainantId = currentUser.uid // Pass the logged-in user ID
                )
            } else {
                // Navigate to login if no user is authenticated
                navController.navigate("login")
            }
        }
        composable("userProfile") {
            val currentUser = auth.currentUser
            if (currentUser != null) {
                UserProfileScreen(navController, firestore, auth)
            } else {
                // Handle user not logged in (navigate to login screen)
                navController.navigate("login")
            }
        }

        // HR Profile Screen with Logout
        composable("hrProfile") {
            val currentUser = auth.currentUser
            if (currentUser != null) {
                HRProfileScreen(navController = navController, userId = currentUser.uid)
            } else {
                // Redirect to login if user is not authenticated
                navController.navigate("login")
            }
        }

        // Add Case Statistics Screen
        composable("caseStatistics") {
            CaseStatisticsScreen(navController = navController)
        }
    }
}
