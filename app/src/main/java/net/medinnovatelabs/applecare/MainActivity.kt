package net.medinnovatelabs.applecare

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.navigation.compose.rememberNavController
import net.medinnovatelabs.applecare.ui.Destinations
import net.medinnovatelabs.applecare.ui.NavigationGraph
import net.medinnovatelabs.applecare.ui.theme.AppleCareTheme

class MainActivity : ComponentActivity() {
	override fun onCreate(savedInstanceState: Bundle?) {
		super.onCreate(savedInstanceState)
		setContent {
			AppleCareTheme {
				NavigationGraph(
					navController = rememberNavController(),
					startDestination = Destinations.OnBoard.name
				)
			}
		}
	}
}
