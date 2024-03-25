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
import androidx.navigation.navigation
import com.example.compose.AppleCareTheme
import net.medinnovatelabs.applecare.ui.Authentication.AllSet
import net.medinnovatelabs.applecare.ui.Authentication.LogIn
import net.medinnovatelabs.applecare.ui.Authentication.MoreInfo
import net.medinnovatelabs.applecare.ui.Authentication.OnBoardingScreen
import net.medinnovatelabs.applecare.ui.Authentication.SignUp
import net.medinnovatelabs.applecare.ui.Camera.Camera
import net.medinnovatelabs.applecare.ui.Destinations
import net.medinnovatelabs.applecare.ui.Main.Main


class MainActivity : ComponentActivity() {
	@OptIn(ExperimentalMaterial3Api::class)
	@RequiresApi(Build.VERSION_CODES.R)
	override fun onCreate(savedInstanceState: Bundle?) {
		super.onCreate(savedInstanceState)
		setContent {
			AppleCareTheme {
				val navController = rememberNavController()
				NavHost(navController = navController, startDestination = Destinations.Auth.name) {
					navigation(route = Destinations.Auth.name, startDestination = "onboard") {
						composable(route = "onboard") {
							OnBoardingScreen(
								onSkip = { navController.navigate("Login") }
							)
						}

						composable(route = "Login") {
							LogIn(
								onLogin = { navController.navigate(Destinations.Main.name) },
								onSignup = { navController.navigate("Signup") }
							)
						}

						composable(route = "Signup") {
							SignUp(
								onSignUp = { navController.navigate("MoreInfo") },
								onSignIn = { navController.navigate("Login") }
							)
						}

						composable(route = "MoreInfo") {
							MoreInfo(
								onCompleteReg = { navController.navigate("AllSet") },
								onBack = { navController.popBackStack() }
							)
						}

						composable(route = "AllSet") {
							 AllSet(onDelay = {
								navController.navigate(Destinations.Main.name) {
									popUpTo(Destinations.Auth.name) { inclusive = true }
								}
							 })
						}
					}

					composable("camera") {
						if (!hasRequiredPermissions()) {
							ActivityCompat.requestPermissions(
								this@MainActivity, CAMERAX_PERMISSIONS, 0
							)
						}
						Camera()
					}

					composable(route = Destinations.Main.name) {
						Main(onCamera = {
							navController.navigate("camera")
						})
					}
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
