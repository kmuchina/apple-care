package net.medinnovatelabs.applecare.ui.Main

import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material.icons.outlined.Notifications
import androidx.compose.material.icons.outlined.Settings
import androidx.compose.material3.Badge
import androidx.compose.material3.BadgedBox
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import net.medinnovatelabs.applecare.R

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeTopAppBar(
	onMenuClicked: () -> Unit,
	modifier: Modifier = Modifier
) {
	var expanded by remember { mutableStateOf(false) }
	CenterAlignedTopAppBar(
		navigationIcon = {
			IconButton(onClick = {
				onMenuClicked()
			}) {
				Icon(
					Icons.Filled.Menu,
					null
				)
			}
		},
		title = {
			Text(
				stringResource(id = R.string.home),
				style = MaterialTheme.typography.titleMedium,
				color = MaterialTheme.colorScheme.onPrimary
			)
		},
		colors = TopAppBarDefaults.mediumTopAppBarColors(
			containerColor = MaterialTheme.colorScheme.primary,
			titleContentColor = MaterialTheme.colorScheme.onPrimary,
			navigationIconContentColor = MaterialTheme.colorScheme.onPrimary
		),
		actions = {
			BadgedBox(
				badge = {
					Badge(
						modifier = Modifier
							.offset (y = 10.dp, x = (-17).dp)
							.size(10.dp),
					) {
					}
				},

			) {
				IconButton(onClick = { /*TODO*/ }) {
					Icon(
						Icons.Outlined.Notifications,
						contentDescription = "Notifications",
						tint = MaterialTheme.colorScheme.onPrimary,
						modifier = Modifier.size(20.dp)
					)
				}
			}

			IconButton(onClick = { /*TODO*/ }) {
				Icon(
					Icons.Outlined.Settings,
					contentDescription = "Settings",
					tint = MaterialTheme.colorScheme.onPrimary,
					modifier = Modifier.size(20.dp)
				)
			}
		}
	)
}

@Preview
@Composable
fun TopAppBarPreview() {
	HomeTopAppBar(onMenuClicked = { })
}