package net.medinnovatelabs.applecare

import android.Manifest
import android.content.pm.PackageManager
import android.os.Build
import android.os.Bundle
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.compose.setContent
import androidx.activity.result.IntentSenderRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.viewModels
import androidx.annotation.RequiresApi
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navigation
import com.example.compose.AppleCareTheme
import com.google.android.gms.auth.api.identity.Identity
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import net.medinnovatelabs.applecare.firebasedata.GoogleAuthUiClient
import net.medinnovatelabs.applecare.firebasedata.SignInViewModel
import net.medinnovatelabs.applecare.profile.ProfileScreen
import net.medinnovatelabs.applecare.ui.Authentication.AllSet
import net.medinnovatelabs.applecare.ui.Authentication.LogIn
import net.medinnovatelabs.applecare.ui.Authentication.MoreInfo
import net.medinnovatelabs.applecare.ui.Authentication.OnBoardingScreen
import net.medinnovatelabs.applecare.ui.Authentication.SignUp
import net.medinnovatelabs.applecare.ui.Camera.Camera
import net.medinnovatelabs.applecare.ui.Destinations
import net.medinnovatelabs.applecare.ui.Main.Main

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
	private val googleAuthUiClient by lazy {
		GoogleAuthUiClient(
			context = applicationContext,
			oneTapClient = Identity.getSignInClient(applicationContext)
		)
	}
	@OptIn(ExperimentalMaterial3Api::class)
	@RequiresApi(Build.VERSION_CODES.R)
	override fun onCreate(savedInstanceState: Bundle?) {
		super.onCreate(savedInstanceState)
		setContent {
			AppleCareTheme {
				val navController = rememberNavController()
				NavHost(navController = navController, startDestination = "SignUp"){

					composable("SignUp"){
						val viewModel: SignInViewModel by viewModels()

						val state by viewModel.state.collectAsStateWithLifecycle()
						LaunchedEffect(key1 = Unit){
							if(googleAuthUiClient.getSignedInUser() != null){
								navController.navigate("profile")
							}
						}

						val launcher = rememberLauncherForActivityResult(
							contract = ActivityResultContracts.StartIntentSenderForResult(), onResult = {
									result ->
								if(result.resultCode == RESULT_OK){
									lifecycleScope.launch {
										val signInResult = googleAuthUiClient.signInWithIntent(
											intent = result.data ?: return@launch
										)
										viewModel.onSignInResult(signInResult)
									}
								}
							}
						)
						SignUp(
							state = state,
							onSignInClick = {
								lifecycleScope.launch {
									val signInIntentSender = googleAuthUiClient.signIn()
									launcher.launch(
										IntentSenderRequest.Builder(
											signInIntentSender ?: return@launch
										).build()
									)

								}
							},
							navController = navController
						)
					}

					composable("Login"){
						val viewModel: SignInViewModel by viewModels()
						val state by viewModel.state.collectAsStateWithLifecycle()

						LaunchedEffect(key1 = Unit){
							if(googleAuthUiClient.getSignedInUser() == null){
								navController.navigate("profile")
							}
						}

						val launcher = rememberLauncherForActivityResult(
							contract = ActivityResultContracts.StartIntentSenderForResult(), onResult = {
									result ->
								if(result.resultCode == RESULT_OK){
									lifecycleScope.launch {
										val signInResult = googleAuthUiClient.signInWithIntent(
											intent = result.data ?: return@launch
										)
										viewModel.onSignInResult(signInResult)
									}
								}
							}
						)


						LogIn(
							state = state,
							onSignInClick = {
								lifecycleScope.launch {
									val signInIntentSender = googleAuthUiClient.signIn()
									launcher.launch(
										IntentSenderRequest.Builder(
											signInIntentSender ?: return@launch
										).build()
									)

								}
							},
							navController = navController
						)

					}
					composable( "profile"){
						val isProfileShown by remember { mutableStateOf(false) } // Track profile shown state

//						LaunchedEffect(key1 = isProfileShown) { // Trigger after profile shown changes
//							if (isProfileShown) {
//								delay(5000) // Delay for 2 seconds (adjust as needed)
//							}
//						}
						ProfileScreen(
							userData = googleAuthUiClient.getSignedInUser(),
							onSignOut = {
								lifecycleScope.launch {
									googleAuthUiClient.signOut()
									Toast.makeText(
										applicationContext,
										"Signed out...👋🏾",
										Toast.LENGTH_LONG
									).show()

									navController.popBackStack()
								}
							},
							navController = navController

						)
					}
				}
//				val navController = rememberNavController()
//				NavHost(navController = navController, startDestination = Destinations.Main.name) {
//					navigation(route = Destinations.Auth.name, startDestination = "onboard") {
//						composable(route = "onboard") {
//							OnBoardingScreen(
//								onSkip = { navController.navigate("Login") }
//							)
//						}
//
//						composable(route = "Login") {
//							LogIn(
//								onLogin = { navController.navigate(Destinations.Main.name) },
//								onSignup = { navController.navigate("Signup") }
//							)
//						}
//
//						composable(route = "Signup") {
//							SignUp(
//								onSignUp = { navController.navigate("MoreInfo") },
//								onSignIn = { navController.navigate("Login") }
//							)
//						}
//
//						composable(route = "MoreInfo") {
//							MoreInfo(
//								onCompleteReg = { navController.navigate("AllSet") },
//								onBack = { navController.popBackStack() }
//							)
//						}
//
//						composable(route = "AllSet") {
//							AllSet(onDelay = {
//								navController.navigate(Destinations.Main.name) {
//									popUpTo(Destinations.Auth.name) { inclusive = true }
//								}
//							})
//						}
//					}
//
//					composable("camera") {
//						if (!hasRequiredPermissions()) {
//							ActivityCompat.requestPermissions(
//								this@MainActivity, CAMERAX_PERMISSIONS, 0
//							)
//						}
//						Camera()
//					}
//
//					composable(route = Destinations.Main.name) {
//						Main(onCamera = {
//							navController.navigate("camera")
//						})
//					}
//				}

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
