package net.medinnovatelabs.applecare

import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import android.location.Location
import android.net.Uri
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
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import net.medinnovatelabs.applecare.ui.Authentication.AllSet
import net.medinnovatelabs.applecare.ui.Authentication.LogIn
import net.medinnovatelabs.applecare.ui.Authentication.MoreInfo
import net.medinnovatelabs.applecare.ui.Authentication.OnBoardingScreen
import net.medinnovatelabs.applecare.ui.Authentication.SignUp
import net.medinnovatelabs.applecare.ui.Camera.Camera
import net.medinnovatelabs.applecare.ui.Destinations
import net.medinnovatelabs.applecare.ui.Main.Main


class MainActivity : ComponentActivity() {
	lateinit var fusedLocationClient: FusedLocationProviderClient
	private var location: Location? = null

	@OptIn(ExperimentalMaterial3Api::class)
	@RequiresApi(Build.VERSION_CODES.R)
	override fun onCreate(savedInstanceState: Bundle?) {
		super.onCreate(savedInstanceState)
		fusedLocationClient = LocationServices.getFusedLocationProviderClient(this)
		getLocation()

		setContent {
			AppleCareTheme {
				val navController = rememberNavController()
				NavHost(navController = navController, startDestination = Destinations.Main.name) {
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
								this@MainActivity, PERMISSIONS, 0
							)
						}
						Camera()
					}

					composable(route = Destinations.Main.name) {

						val gmmIntentUri = Uri.parse("geo:0,0?z=15&q=agrovet+near+me")
						val mapIntent = Intent(Intent.ACTION_VIEW, gmmIntentUri)
						mapIntent.setPackage("com.google.android.apps.maps")

						Main(
							location = getLocationName(location),
							onFindAgro = { startActivity(mapIntent) },
							onCamera = { navController.navigate("camera") }
						)
					}
				}

			}
		}

	}

	private fun hasRequiredPermissions(): Boolean {
		return PERMISSIONS.all { permission: String ->
			ContextCompat.checkSelfPermission(
				applicationContext,
				permission
			) == PackageManager.PERMISSION_GRANTED
		}
	}

	private fun getLocation() {
		if (ActivityCompat.checkSelfPermission(
				this,
				Manifest.permission.ACCESS_FINE_LOCATION
			) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(
				this,
				Manifest.permission.ACCESS_COARSE_LOCATION
			) != PackageManager.PERMISSION_GRANTED
		) {
			ActivityCompat.requestPermissions(
				this@MainActivity, PERMISSIONS, 0
			)
		}
		fusedLocationClient.lastLocation
			.addOnSuccessListener { it ->
				it?.let {
					location = it
				}
			}
			.addOnFailureListener { exception ->
			}

	}

	private fun getLocationName(location: Location?): String {
		return location?.toString() ?: "Unknown Location"
	}

	companion object {
		val PERMISSIONS = arrayOf(
			Manifest.permission.CAMERA,
			Manifest.permission.ACCESS_COARSE_LOCATION,
			Manifest.permission.ACCESS_FINE_LOCATION
		)
	}

}
