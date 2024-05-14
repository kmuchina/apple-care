package net.medinnovatelabs.applecare.ui.settings

import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.sharp.ArrowBack
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier

@Composable
fun Settings(onBack: () -> Unit) {
	Scaffold(
		topBar = { SettingsTopAppBar(onBack = onBack) }
	) { innerPadding ->
		Surface(
			modifier = Modifier.padding(innerPadding)
		) {

		}
	}
}


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SettingsTopAppBar(
	onBack: () -> Unit,
	modifier: Modifier = Modifier
) {
	CenterAlignedTopAppBar(
		navigationIcon = {
			IconButton(onClick = {
				onBack()
			}) {
				Icon(
					Icons.AutoMirrored.Sharp.ArrowBack,
					null
				)
			}
		},
		title = {
			Text(
				"Settings",
				style = MaterialTheme.typography.titleMedium,
				color = MaterialTheme.colorScheme.onPrimary
			)
		},
		colors = TopAppBarDefaults.mediumTopAppBarColors(
			containerColor = MaterialTheme.colorScheme.primary,
			titleContentColor = MaterialTheme.colorScheme.onPrimary,
			navigationIconContentColor = MaterialTheme.colorScheme.onPrimary
		)
	)
}