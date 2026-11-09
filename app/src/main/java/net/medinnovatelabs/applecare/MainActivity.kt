package net.medinnovatelabs.applecare

import android.Manifest
import android.content.ActivityNotFoundException
import android.content.Intent
import android.content.pm.PackageManager
import android.location.Geocoder
import android.location.Location
import android.net.Uri
import android.os.Bundle
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.core.content.ContextCompat
import androidx.core.content.edit
import androidx.lifecycle.lifecycleScope
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import net.medinnovatelabs.applecare.ui.Camera.Camera
import net.medinnovatelabs.applecare.ui.Guide
import net.medinnovatelabs.applecare.ui.main.Main
import net.medinnovatelabs.applecare.ui.main.community.AddPost
import net.medinnovatelabs.applecare.ui.onboarding.OnBoardingScreen
import net.medinnovatelabs.applecare.ui.settings.Settings
import net.medinnovatelabs.applecare.ui.theme.AppleCareTheme
import java.util.Locale

private object Routes {
	const val ONBOARDING = "onboard"
	const val MAIN = "Main"
	const val CAMERA = "camera"
	const val GUIDE = "guides/{guideNo}"
	const val SETTINGS = "settings"
	const val ADD_POST = "add_post"
}

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
	private lateinit var fusedLocationClient: FusedLocationProviderClient
	private var locationName by mutableStateOf(UNKNOWN_LOCATION)

	private val locationPermissionLauncher =
		registerForActivityResult(ActivityResultContracts.RequestMultiplePermissions()) { grants ->
			if (grants.values.any { it }) loadLocation()
		}

	@OptIn(ExperimentalMaterial3Api::class)
	override fun onCreate(savedInstanceState: Bundle?) {
		enableEdgeToEdge()
		super.onCreate(savedInstanceState)
		fusedLocationClient = LocationServices.getFusedLocationProviderClient(this)

		if (hasLocationPermission()) {
			loadLocation()
		} else {
			locationPermissionLauncher.launch(LOCATION_PERMISSIONS)
		}

		val prefs = getSharedPreferences(PREFS_NAME, MODE_PRIVATE)
		val startDestination =
			if (prefs.getBoolean(KEY_ONBOARDING_DONE, false)) Routes.MAIN else Routes.ONBOARDING

		setContent {
			AppleCareTheme {
				val navController = rememberNavController()

				NavHost(navController = navController, startDestination = startDestination) {
					composable(Routes.ONBOARDING) {
						OnBoardingScreen(
							onSkip = {
								prefs.edit { putBoolean(KEY_ONBOARDING_DONE, true) }
								navController.navigate(Routes.MAIN) {
									popUpTo(Routes.ONBOARDING) { inclusive = true }
								}
							}
						)
					}

					composable(Routes.MAIN) {
						Main(
							location = locationName,
							onFindAgro = ::openAgrovetSearch,
							onCamera = { navController.navigate(Routes.CAMERA) },
							onGuideClicked = { navController.navigate("guides/$it") },
							onSettingsClicked = { navController.navigate(Routes.SETTINGS) },
							onAddPostClicked = { navController.navigate(Routes.ADD_POST) }
						)
					}

					composable(Routes.CAMERA) {
						CameraWithPermission(onBack = { navController.popBackStack() })
					}

					composable(
						route = Routes.GUIDE,
						arguments = listOf(navArgument("guideNo") { type = NavType.IntType })
					) { backStackEntry ->
						Guide(
							guideNo = backStackEntry.arguments?.getInt("guideNo") ?: 0,
							onBack = { navController.popBackStack() }
						)
					}

					composable(Routes.SETTINGS) {
						Settings(onBack = { navController.popBackStack() })
					}

					composable(Routes.ADD_POST) {
						AddPost(onBack = { navController.popBackStack() })
					}
				}
			}
		}
	}

	@OptIn(ExperimentalMaterial3Api::class)
	@Composable
	private fun CameraWithPermission(onBack: () -> Unit) {
		var granted by remember { mutableStateOf(hasPermission(Manifest.permission.CAMERA)) }
		val launcher = rememberLauncherForActivityResult(
			ActivityResultContracts.RequestPermission()
		) { granted = it }

		LaunchedEffect(Unit) {
			if (!granted) launcher.launch(Manifest.permission.CAMERA)
		}

		if (granted) {
			Camera(onBack = onBack)
		} else {
			Column(
				modifier = Modifier
					.fillMaxSize()
					.padding(24.dp),
				verticalArrangement = Arrangement.Center,
				horizontalAlignment = Alignment.CenterHorizontally
			) {
				Text(
					"AppleCare needs camera access to scan your apple leaves.",
					textAlign = TextAlign.Center
				)
				Spacer(Modifier.height(16.dp))
				Button(onClick = { launcher.launch(Manifest.permission.CAMERA) }) {
					Text("Grant camera access")
				}
				TextButton(onClick = onBack) { Text("Back") }
			}
		}
	}

	private fun openAgrovetSearch() {
		val uri = Uri.parse("geo:0,0?z=15&q=agrovet+near+me")
		val mapsIntent = Intent(Intent.ACTION_VIEW, uri).setPackage("com.google.android.apps.maps")
		try {
			startActivity(mapsIntent)
		} catch (_: ActivityNotFoundException) {
			// Google Maps isn't installed: let any other map app handle it.
			try {
				startActivity(Intent(Intent.ACTION_VIEW, uri))
			} catch (_: ActivityNotFoundException) {
				Toast.makeText(this, "No maps app found", Toast.LENGTH_SHORT).show()
			}
		}
	}

	private fun hasPermission(permission: String) =
		ContextCompat.checkSelfPermission(this, permission) == PackageManager.PERMISSION_GRANTED

	private fun hasLocationPermission() = LOCATION_PERMISSIONS.any(::hasPermission)

	private fun loadLocation() {
		if (!hasLocationPermission()) return
		try {
			fusedLocationClient.lastLocation.addOnSuccessListener { location ->
				location?.let(::resolveLocationName)
			}
		} catch (_: SecurityException) {
			// Permission revoked between the check and the call; keep the default label.
		}
	}

	private fun resolveLocationName(location: Location) {
		if (!Geocoder.isPresent()) return
		lifecycleScope.launch {
			val name = withContext(Dispatchers.IO) {
				runCatching {
					@Suppress("DEPRECATION") // Blocking API is fine here: we're on Dispatchers.IO.
					Geocoder(this@MainActivity, Locale.getDefault())
						.getFromLocation(location.latitude, location.longitude, 1)
						?.firstOrNull()
						?.let { it.locality ?: it.subAdminArea ?: it.countryName }
				}.getOrNull()
			}
			if (name != null) locationName = name
		}
	}

	companion object {
		private const val PREFS_NAME = "applecare_prefs"
		private const val KEY_ONBOARDING_DONE = "onboarding_done"
		private const val UNKNOWN_LOCATION = "Unknown Location"
		private val LOCATION_PERMISSIONS = arrayOf(
			Manifest.permission.ACCESS_COARSE_LOCATION,
			Manifest.permission.ACCESS_FINE_LOCATION
		)
	}
}
