package net.medinnovatelabs.applecare

import android.Manifest
import android.content.pm.PackageManager
import android.os.Build
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.annotation.RequiresApi
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.compose.AppleCareTheme
import net.medinnovatelabs.applecare.ui.Authentication.LastStep
import net.medinnovatelabs.applecare.ui.Authentication.LogIn
import net.medinnovatelabs.applecare.ui.Authentication.MoreInfo
import net.medinnovatelabs.applecare.ui.Authentication.SignUp
import net.medinnovatelabs.applecare.ui.Camera.Camera
import net.medinnovatelabs.applecare.ui.Main.Main
import net.medinnovatelabs.applecare.ui.OnBoardingScreen


class MainActivity : ComponentActivity() {
	@OptIn(ExperimentalMaterial3Api::class)
	@RequiresApi(Build.VERSION_CODES.R)
	override fun onCreate(savedInstanceState: Bundle?) {
		super.onCreate(savedInstanceState)
		setContent {
//			AppleCareTheme {
//				if (!hasRequiredPermissions()) {
//					ActivityCompat.requestPermissions(
//						this@MainActivity, CAMERAX_PERMISSIONS, 0
//					)
//
//				}
//
//				// Camera() - Works here but not inside navgraph
//				NavigationGraph(
//					navController = rememberNavController(),
//					startDestination = Destinations.Main.name
//				)
//
//
//			}
			val navController = rememberNavController()
			NavHost(navController = navController, startDestination = "onboard"){
				composable(route = "onboard") {
					OnBoardingScreen(navController)
				}

				composable(route = "Login") {
					LogIn(
						navController
					)
				}

				composable("Signup") {
					SignUp(
						navController
					)
				}

				composable( "MoreInfo") {
					MoreInfo(
						navController
					)
				}

				composable( "AllSet") {
					LastStep(navController)
				}




			composable("camera"){
				if (!hasRequiredPermissions()){
					ActivityCompat.requestPermissions(
						this@MainActivity, CAMERAX_PERMISSIONS, 0
					)
				}
				Camera(navController = navController)
			}

			}

		}
	}

	private fun hasRequiredPermissions(): Boolean {
		return CAMERAX_PERMISSIONS.all { permission: String ->
			ContextCompat.checkSelfPermission(
				applicationContext,
				permission
			) == PackageManager.PERMISSION_GRANTED
		}
	}

	companion object {
		val CAMERAX_PERMISSIONS = arrayOf(
			Manifest.permission.CAMERA
		)
	}

}
