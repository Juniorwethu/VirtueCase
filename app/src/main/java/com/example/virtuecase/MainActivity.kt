package com.example.virtuecase

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.navigation.compose.rememberNavController
import com.example.virtuecase.ui.NavGraph
import com.example.virtuecase.ui.theme.VirtueCaseTheme
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore

class MainActivity : ComponentActivity() {

    private lateinit var auth: FirebaseAuth
    private lateinit var firestore: FirebaseFirestore

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // Initialize Firebase instances
        auth = FirebaseAuth.getInstance()
        firestore = FirebaseFirestore.getInstance()

        setContent {
            VirtueCaseTheme {
                val navController = rememberNavController()

                Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
                    // Pass innerPadding here directly to adjust the NavGraph content
                    NavGraph(
                        navController = navController,
                        firestore = firestore,
                        auth = auth,
                        innerPadding = innerPadding // Pass the padding to NavGraph
                    )
                }
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun GreetingPreview() {
    VirtueCaseTheme {
        val navController = rememberNavController()
        val firestore = FirebaseFirestore.getInstance()  // Initialize Firestore for preview
        val auth = FirebaseAuth.getInstance()  // Initialize FirebaseAuth for preview

        Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
            // Pass the padding to NavGraph in the preview as well
            NavGraph(
                navController = navController,
                firestore = firestore,
                auth = auth,
                innerPadding = innerPadding // Pass the padding here too
            )
        }
    }
}
