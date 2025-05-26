package com.example.virtuecase.ui

import com.example.virtuecase.ui.ViewCasesScreen
import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable


import com.example.virtuecase.ui.screens.*
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore

@Composable
fun NavGraph(navController: NavHostController, firestore: FirebaseFirestore, auth: FirebaseAuth) {
    NavHost(navController = navController, startDestination = "login") {
        composable("login") { LoginScreen(navController) }
        composable("signup") { SignUpScreen(navController) }
        composable("userHome") { UserHomeScreen(navController) }
        composable("hrHome") { HRHomeScreen(navController) }
        composable("viewCases") { ViewCasesScreen(navController, firestore) } // Pass firestore
        composable("submitCase") { SubmitCaseScreen(navController, firestore) }
        composable("reviewCases") { ReviewCasesScreen(navController, firestore) } // Pass firestore
        composable("manageUsers") { ManageUsersScreen(navController, firestore) } // Pass firestore
        composable("caseHistory") { CaseHistoryScreen(navController) }
        composable("notifications") { NotificationsScreen(navController, firestore) }
        composable("userProfile") { UserProfileScreen(navController, firestore, auth) } // Pass firestore and auth
    }
}











