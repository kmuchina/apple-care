package net.medinnovatelabs.applecare

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import com.example.compose.AppleCareTheme
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import net.medinnovatelabs.applecare.ui.Destinations
import net.medinnovatelabs.applecare.ui.NavigationGraph

private lateinit var auth: FirebaseAuth

class MainActivity : ComponentActivity() {
	override fun onCreate(savedInstanceState: Bundle?) {
		auth = Firebase.auth
		super.onCreate(savedInstanceState)
		setContent {
			AppleCareTheme {
				val navController: NavHostController = rememberNavController()
				NavigationGraph(
					navController = navController,
					startDestination = Destinations.Auth.name
				)
			}
		}
	}

	public override fun onStart() {
		super.onStart()
		// Check if user is signed in (non-null) and update UI accordingly.
		val currentUser = auth.currentUser
		if (currentUser != null) {
			// User is signed in
			
		} else {
			// No user is signed in
		}
	}
}
