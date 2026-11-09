package net.medinnovatelabs.applecare.ui.main

import androidx.annotation.StringRes
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CameraAlt
import androidx.compose.material.icons.filled.Clear
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.outlined.Home
import androidx.compose.material.icons.outlined.Person
import androidx.compose.material.icons.outlined.Settings
import androidx.compose.material.icons.rounded.Person
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.DrawerState
import androidx.compose.material3.DrawerValue
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FabPosition
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalDrawerSheet
import androidx.compose.material3.ModalNavigationDrawer
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.rememberDrawerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch
import net.medinnovatelabs.applecare.R
import net.medinnovatelabs.applecare.ui.main.community.Community
import net.medinnovatelabs.applecare.ui.main.community.CommunityTopAppBar

enum class MainDestinations(@StringRes val route: Int) {
	Home(route = R.string.home),
	History(route = R.string.history),
	Community(route = R.string.community)
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun Main(
	location: String,
	onCamera: () -> Unit,
	onFindAgro: () -> Unit,
	onAddPostClicked: () -> Unit,
	modifier: Modifier = Modifier,
	navController: NavHostController = rememberNavController(),
	onGuideClicked: (Int) -> Unit,
	onSettingsClicked: () -> Unit
) {
	val backStackEntry by navController.currentBackStackEntryAsState()
	val currentScreen = MainDestinations.valueOf(
		backStackEntry?.destination?.route ?: MainDestinations.Home.name
	)

	val drawerState = rememberDrawerState(initialValue = DrawerValue.Closed)
	val scope = rememberCoroutineScope()
	var selectedIndex by rememberSaveable {
		mutableIntStateOf(0)
	}

	var expanded by remember { mutableStateOf(false) }

	ModalNavigationDrawer(
		drawerContent = {
			DrawerContent(
				scope,
				drawerState,
				onSettingsClick = onSettingsClicked
			)
		},
		drawerState = drawerState,
	) {
		Scaffold(
			topBar = {
				when (currentScreen.name) {
					"Home" -> {
						HomeTopAppBar(
							onMenuClicked = { scope.launch { drawerState.open() } },
							onSettingsClicked = onSettingsClicked
						)
					}

					"History" -> {
						TopAppBar(
							title = {
								Text(
									stringResource(id = R.string.history),
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
								IconButton(onClick = { expanded = !expanded }) {
									Icon(
										Icons.Default.MoreVert,
										contentDescription = "More",
										tint = MaterialTheme.colorScheme.onPrimary
									)
								}
								Box(
									modifier = Modifier.offset(x = -16.dp, y = 20.dp),
									contentAlignment = Alignment.TopCenter
								) {
									DropdownMenu(
										expanded = expanded,
										onDismissRequest = { expanded = false }
									) {
										DropdownMenuItem(
											text = { Text("Settings") },
											onClick = { onSettingsClicked() },
										)
									}
								}
							}
						)
					}

					"Camera" -> {}

					"Community" -> {
						CommunityTopAppBar(
							onSettingsClicked = onSettingsClicked,
							onAddPost = onAddPostClicked
						)
					}

				}
			},
			bottomBar = { BottomNavigationBar(navController, currentScreen, onSettingsClicked) },
			floatingActionButtonPosition = FabPosition.Center,
			floatingActionButton = {
				FloatingActionButton(
					onClick = {
						onCamera()
					},
					shape = CircleShape,
					modifier = Modifier
						.size(48.dp)
						.offset(y = 76.dp)
				) {
					Icon(
						Icons.Default.CameraAlt,
						contentDescription = "Take a picture",
						tint = MaterialTheme.colorScheme.onPrimaryContainer
					)
				}
			},
			modifier = modifier
		) { innerPadding ->

			NavHost(
				navController = navController,
				startDestination = MainDestinations.Home.name,
				Modifier.padding(innerPadding)
			) {
				composable(MainDestinations.Home.name) {
					Home(
						location = location,
						onFindAgro = onFindAgro,
						onGuideClicked = onGuideClicked
					)
				}
				composable(MainDestinations.History.name) {
					History()
				}

				composable(MainDestinations.Community.name) {
					Community()
				}


			}
		}
	}
}

@Composable
fun DrawerContent(
	scope: CoroutineScope,
	drawerState: DrawerState,
	onSettingsClick: () -> Unit
) {
	ModalDrawerSheet {
		Row(
			horizontalArrangement = Arrangement.SpaceBetween,
			verticalAlignment = Alignment.CenterVertically,
			modifier = Modifier
				.fillMaxWidth()
				.padding(start = 10.dp, end = 40.dp)
		) {
			IconButton(
				onClick = {
					scope.launch {
						drawerState.close()
					}
				},
				modifier = Modifier.padding(top = 4.dp)
			) {
				Icon(
					Icons.Filled.Clear,
					contentDescription = null
				)
			}
			Icon(
				painter = painterResource(id = R.drawable.logo),
				contentDescription = "Apple Care Logo",
			)
			Text("")
		}

		HorizontalDivider()

		Column(
			modifier = Modifier
				.fillMaxWidth()
				.fillMaxHeight(0.9f)
		) {
			Row(
				verticalAlignment = Alignment.Bottom,
				modifier = Modifier
					.padding(vertical = 12.dp, horizontal = 16.dp)
					.clickable {
						scope.launch {
							drawerState.close()
						}
						onSettingsClick()
					}
			) {
				Icon(
					Icons.Outlined.Settings,
					contentDescription = "Settings",
					modifier = Modifier
						.size(32.dp)
				)
				Spacer(Modifier.width(20.dp))
				Text(
					text = "Settings",
					fontSize = 16.sp,
					modifier = Modifier.padding(bottom = 1.dp)
				)
			}

			Row(
				verticalAlignment = Alignment.Bottom,
				modifier = Modifier
					.padding(vertical = 12.dp, horizontal = 16.dp)
					.clickable { }
			) {
				Icon(
					painterResource(id = R.drawable.ic_help),
					contentDescription = "FAQ",
					modifier = Modifier
						.size(32.dp)
				)
				Spacer(Modifier.width(20.dp))
				Text(
					text = "FAQ",
					fontSize = 16.sp,
					modifier = Modifier.padding(bottom = 1.dp)
				)
			}

		}
	}
}


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun BottomNavigationBar(
	navController: NavHostController,
	currentScreen: MainDestinations,
	onSettingsClicked: () -> Unit,
	modifier: Modifier = Modifier
) {
	NavigationBar {
		NavigationBarItem(
			selected = currentScreen.name == stringResource(id = R.string.home),
			onClick = {
				navController.navigate(MainDestinations.Home.name) {
					popUpTo(route = MainDestinations.Home.name) { inclusive = true }
				}
			},
			icon = {
				Icon(
					if (currentScreen.name == stringResource(id = R.string.home)) Icons.Filled.Home
					else Icons.Outlined.Home,
					contentDescription = stringResource(id = R.string.home)
				)
			},
			label = {
				Text(
					text = stringResource(id = R.string.home),
					fontSize = 10.sp
				)
			}
		)
		NavigationBarItem(
			selected = currentScreen.name == stringResource(id = R.string.history),
			onClick = {
				navController.navigate(MainDestinations.History.name) {
					popUpTo(route = MainDestinations.History.name) { inclusive = true }
				}
			},
			icon = {
				Icon(
					painterResource(id = R.drawable.ic_history),
					contentDescription = stringResource(id = R.string.history)
				)
			},
			label = {
				Text(
					text = stringResource(id = R.string.history),
					fontSize = 10.sp
				)
			}
		)

		// Camera NavItem
		NavigationBarItem(
			selected = currentScreen.name == stringResource(id = R.string.camera),
			onClick = { },
			icon = { },
			label = { }
		)

		NavigationBarItem(
			selected = currentScreen.name == stringResource(id = R.string.community),
			onClick = {
				navController.navigate(MainDestinations.Community.name) {
					popUpTo(route = MainDestinations.Community.name) { inclusive = true }
				}
			},
			icon = {
				Icon(
					if (currentScreen.name == stringResource(id = R.string.community)) painterResource(
						id = R.drawable.ic_community_selected
					)
					else painterResource(id = R.drawable.ic_community_unselected),
					contentDescription = stringResource(id = R.string.community)
				)
			},
			label = {
				Text(
					text = stringResource(id = R.string.community),
					fontSize = 9.9.sp
				)
			}
		)
		// Settings opens its own screen, so it is never shown as the selected tab
		NavigationBarItem(
			selected = false,
			onClick = onSettingsClicked,
			icon = {
				Icon(
					Icons.Outlined.Settings,
					contentDescription = stringResource(id = R.string.settings)
				)
			},
			label = {
				Text(
					text = stringResource(id = R.string.settings),
					fontSize = 10.sp
				)
			},
			modifier = Modifier.padding(end = 0.dp)
		)
	}
}

@Preview
@Composable
fun MainPreview() {
	Main(
		location = "",
		onCamera = { },
		onFindAgro = { },
		onGuideClicked = { },
		onSettingsClicked = { },
		onAddPostClicked = { }
	)
}