package net.medinnovatelabs.applecare.ui

import androidx.annotation.StringRes
import net.medinnovatelabs.applecare.R

enum class Destinations(@StringRes val route: Int) {
	Auth(route = R.string.onboard),
	Main(route = R.string.main)
}
