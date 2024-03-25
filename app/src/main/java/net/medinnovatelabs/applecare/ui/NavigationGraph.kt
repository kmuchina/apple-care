package net.medinnovatelabs.applecare.ui

import androidx.annotation.StringRes
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.navigation
import net.medinnovatelabs.applecare.R
import net.medinnovatelabs.applecare.ui.Authentication.LastStep
import net.medinnovatelabs.applecare.ui.Authentication.LogIn
import net.medinnovatelabs.applecare.ui.Authentication.MoreInfo
import net.medinnovatelabs.applecare.ui.Authentication.SignUp
import net.medinnovatelabs.applecare.ui.Main.Main

//enum class Destinations(@StringRes val route: Int) {
//	Auth(route = R.string.onboard),
//	Main(route = R.string.main)
//}
//
//@OptIn(ExperimentalMaterial3Api::class)
//@Composable
//fun NavigationGraph(
//	navController: NavHostController,
//	startDestination: String,
//	modifier: Modifier = Modifier
//) {
//	NavHost(
//		navController = navController,
//		startDestination = startDestination,
//		modifier = modifier
//	) {
//
//		navigation(route = Destinations.Auth.name, startDestination = "onboard") {
//			composable(route = "onboard") {
//				OnBoardingScreen(onSkip = {
//					navController.navigate("Login")
//				})
//			}
//
//			composable(route = "Login") {
//				LogIn(
//					onLogin = {
//						navController.navigate(Destinations.Main.name)
//					},
//					onSignUp = {
//						navController.navigate("Signup")
//					}
//				)
//			}
//
//			composable(route = "Signup") {
//				SignUp(
//					onSignUp = { navController.navigate("MoreInfo") },
//					onSignIn = { navController.navigate("Login") }
//				)
//			}
//
//			composable(route = "MoreInfo") {
//				MoreInfo(
//					onCompleteReg = { navController.navigate("AllSet") },
//					onBack = { navController.popBackStack() }
//				)
//			}
//
//			composable(route = "AllSet") {
//				LastStep(nav = {
//					navController.navigate(Destinations.Main.name) {
//						popUpTo(Destinations.Auth.name) { inclusive = true }
//					}
//				})
//			}
//		}
//
//		composable(route = Destinations.Main.name) {
//			Main(onCamera = { })
//		}
//
//	}
//
//
//}