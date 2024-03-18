package net.medinnovatelabs.applecare.ui

import androidx.annotation.StringRes
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import net.medinnovatelabs.applecare.R
import net.medinnovatelabs.applecare.ui.LogIn.LogIn
import net.medinnovatelabs.applecare.ui.Main.Main

enum class Destinations(@StringRes val route: Int) {
	OnBoard(route = R.string.onboard),
	LogIn(route = R.string.login),
	SignUp(route = R.string.signup),
	Main(route = R.string.main)
}

@Composable
fun NavigationGraph(
	navController: NavHostController,
	startDestination: String,
	modifier: Modifier = Modifier
) {
	NavHost(
		navController = navController,
		startDestination = startDestination,
		modifier = modifier
	) {

		composable(route = Destinations.OnBoard.name) {
			OnBoardingScreen(onSkip = {
				navController.navigate(Destinations.LogIn.name)
			})
		}


		composable(route = Destinations.LogIn.name) {
			LogIn(onLogin = {
				navController.navigate(Destinations.Main.name)
			})
		}

		composable(route = Destinations.Main.name) {
			Main()
		}
	}
}