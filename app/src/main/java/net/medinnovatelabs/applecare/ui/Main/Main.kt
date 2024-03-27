package net.medinnovatelabs.applecare.ui.Main

import androidx.annotation.StringRes
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Clear
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material.icons.outlined.Add
import androidx.compose.material.icons.outlined.Home
import androidx.compose.material.icons.outlined.MoreVert
import androidx.compose.material.icons.outlined.Person
import androidx.compose.material.icons.outlined.Refresh
import androidx.compose.material.icons.outlined.Settings
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.Divider
import androidx.compose.material3.DrawerValue
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FabPosition
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalDrawerSheet
import androidx.compose.material3.ModalNavigationDrawer
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.rememberDrawerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import kotlinx.coroutines.launch
import net.medinnovatelabs.applecare.R

enum class MainDestinations(@StringRes val route: Int) {
	Home(route = R.string.home),
	History(route = R.string.history),
	Community(route = R.string.community),
	Profile(route = R.string.profile)
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun Main(
	modifier: Modifier = Modifier,
	navController: NavHostController = rememberNavController(),
	onCamera: () -> Unit
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

	ModalNavigationDrawer(
		drawerContent = {
			ModalDrawerSheet() {
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
						contentDescription = "Leaflens Logo",
					)
					Text("")
				}
				Divider()

			}
		},
		drawerState = drawerState
	) {
		Scaffold(
			topBar = {
				when (currentScreen.name) {
					"Home" -> {
						HomeTopAppBar(onMenuClicked = {
							scope.launch {
								drawerState.open()
							}
						})
					}

					"History" -> {
						CenterAlignedTopAppBar(
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
								IconButton(onClick = { /*TODO*/ }) {
									Icon(
										Icons.Outlined.MoreVert,
										contentDescription = "More",
										tint = MaterialTheme.colorScheme.onPrimary,
										modifier = Modifier.size(20.dp)
									)
								}
							}
						)
					}

					"Camera" -> {
						// TopAppBar(title = { Text("Take a picture") })
					}

					"Community" -> {
						CenterAlignedTopAppBar(
							title = {
								Text(
									stringResource(id = R.string.community),
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
								IconButton(onClick = { /*TODO*/ }) {
									Icon(
										Icons.Outlined.MoreVert,
										contentDescription = "More",
										tint = MaterialTheme.colorScheme.onPrimary,
										modifier = Modifier.size(20.dp)
									)
								}
							}
						)
					}

					"Profile" -> {
						CenterAlignedTopAppBar(
							title = {
								Text(
									stringResource(id = R.string.profile),
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
				}
			},
			bottomBar = { BottomNavigationBar(navController, currentScreen) },
			floatingActionButtonPosition = FabPosition.Center,
			floatingActionButton = {
				FloatingActionButton(
					onClick = {
						onCamera()
					},
					shape = CircleShape,
					modifier = Modifier
						.size(48.dp)
						.offset(y = 70.dp)
				) {
					Icon(
						Icons.Outlined.Add,
						contentDescription = null
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
					Text("Home")
				}
				composable(MainDestinations.History.name) {
					Text("History")
				}

				composable(MainDestinations.Community.name) {
					Text("Community")
				}
				composable(MainDestinations.Profile.name) {
					Text("Profile")
				}
			}
		}
	}
}


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun BottomNavigationBar(
	navController: NavHostController,
	currentScreen: MainDestinations,
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
				if (currentScreen.name == stringResource(id = R.string.home)) {
					Icon(
						Icons.Filled.Home,
						contentDescription = stringResource(id = R.string.home)
					)
				} else {
					Icon(
						Icons.Outlined.Home,
						contentDescription = stringResource(id = R.string.home)
					)
				}
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
				if (currentScreen.name == stringResource(id = R.string.history)) {
					Icon(
						Icons.Filled.Refresh,
						contentDescription = stringResource(id = R.string.history)
					)
				} else {
					Icon(
						Icons.Outlined.Refresh,
						contentDescription = stringResource(id = R.string.history)
					)
				}

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
					painterResource(id = R.drawable.community_icon),
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
		NavigationBarItem(
			selected = currentScreen.name == stringResource(id = R.string.profile),
			onClick = {
				navController.navigate(MainDestinations.Profile.name) {
					popUpTo(route = MainDestinations.Profile.name) { inclusive = true }
				}
			},
			icon = {
				if (currentScreen.name == stringResource(id = R.string.profile)) {
					Icon(
						Icons.Filled.Person,
						contentDescription = stringResource(id = R.string.profile)
					)
				} else {
					Icon(
						Icons.Outlined.Person,
						contentDescription = stringResource(id = R.string.profile)
					)
				}
			},
			label = {
				Text(
					text = stringResource(id = R.string.profile),
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
	Main(onCamera = { })
}