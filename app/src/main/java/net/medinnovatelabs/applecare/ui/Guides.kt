package net.medinnovatelabs.applecare.ui

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
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
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import net.medinnovatelabs.applecare.R

@Composable
fun Guide(guideNo: Int, onBack: () -> Unit) {
	Scaffold(
		topBar = { GuideTopAppBar(onBack = onBack) }
	) { innerPadding ->
		Surface(
			modifier = Modifier.padding(innerPadding)
		) {
			LazyColumn(
				modifier = Modifier.padding(horizontal = 16.dp, vertical = 8.dp)
			) {
				item {
					Image(
						painter = painterResource(R.drawable.wambugu_holding_apples2),
						contentDescription = null,
						modifier = Modifier
							.padding(bottom = 20.dp)
							.fillMaxWidth()
					)

					Text(
						text = stringResource(R.string.guide_text_1)
							.replace("\r", "")
							.split("\n")
							.joinToString("\n\n") { it.trim() },
						fontSize = 16.sp,
						textAlign = TextAlign.Start
					)
				}
			}
		}
	}
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun GuideTopAppBar(
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
				"Guides",
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


