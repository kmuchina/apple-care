package net.medinnovatelabs.applecare.ui.Main

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.PageSize
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material.icons.outlined.Settings
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
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
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.util.lerp
import net.medinnovatelabs.applecare.R
import java.time.LocalDate
import java.time.format.DateTimeFormatter
import java.util.Locale
import kotlin.math.absoluteValue

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
			/*BadgedBox(
				badge = {
					Badge(
						modifier = Modifier
							.offset(y = 10.dp, x = (-17).dp)
							.size(10.dp),
					) {
					}
				},

				) {
				IconButton(onClick = { *//*TODO*//* }) {
					Icon(
						Icons.Outlined.Notifications,
						contentDescription = "Notifications",
						tint = MaterialTheme.colorScheme.onPrimary,
						modifier = Modifier.size(20.dp)
					)
				}
			}*/

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

@Composable
fun Home(location: String, modifier: Modifier = Modifier, onFindAgro: () -> Unit) {
	Column(
		horizontalAlignment = Alignment.CenterHorizontally,
		verticalArrangement = Arrangement.Top,
		modifier = modifier
			.fillMaxSize()
			.padding(start = 10.dp, top = 20.dp, end = 10.dp, bottom = 10.dp)
	) {
		WeatherBox(location = location)

		Guides()

		AgroVetNearMe(onFindAgro = onFindAgro)
	}
}

@Composable
fun WeatherBox(location: String, modifier: Modifier = Modifier) {
	Box(
		modifier = modifier
			.fillMaxWidth(0.98f)
			.height(74.dp)
			.border(width = 1.dp, color = Color(0xFFFFDF22), shape = RoundedCornerShape(10.dp))
	) {
		Row(
			verticalAlignment = Alignment.CenterVertically,
			horizontalArrangement = Arrangement.SpaceBetween,
			modifier = Modifier
				.padding(12.dp)
				.fillMaxSize()
		) {
			Row(
				verticalAlignment = Alignment.CenterVertically,
				modifier = Modifier
					.fillMaxHeight()
					.fillMaxWidth(0.7f)
			) {
				Image(
					painterResource(id = R.drawable.location),
					contentDescription = "Location Icon",
					modifier = Modifier
						.size(36.dp)
				)
				Column(
					modifier = Modifier
						.fillMaxWidth()
						.padding(top = 4.dp, start = 4.dp, bottom = 0.dp, end = 0.dp)
				) {
					Text(
						text = location, // Location
						fontSize = 16.sp
					)
					Text(
						text = LocalDate.now().format(
							DateTimeFormatter.ofPattern("EEE, dd MMMM yyyy", Locale.ENGLISH)
						),
						fontSize = 12.sp
					)
				}
			}

			Row {
				Text(
					text = stringResource(id = R.string.temp, 24),
					fontSize = 20.sp,
				)
			}
		}
	}
}

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun Guides(modifier: Modifier = Modifier) {
	val pagerState = rememberPagerState(pageCount = { 3 })

	Column(
		horizontalAlignment = Alignment.CenterHorizontally,
		verticalArrangement = Arrangement.Center
	) {
		Text(
			text = "Guides & Tips",
			textAlign = TextAlign.Start,
			style = MaterialTheme.typography.titleMedium,
			modifier = Modifier
				.padding(top = 20.dp, start = 2.dp)
				.fillMaxWidth()
		)
		HorizontalPager(
			state = pagerState,
			pageSize = PageSize.Fixed(300.dp),
			pageSpacing = 8.dp,
			modifier = modifier
				.padding(bottom = 8.dp)
				.fillMaxWidth()
		) { page ->
			Box(
				modifier = Modifier
					.clip(RoundedCornerShape(12.dp))
					.width(300.dp)
					.height(200.dp)
					.graphicsLayer {
						val pageOffset = (
								(pagerState.currentPage - page) + pagerState.currentPageOffsetFraction
								).absoluteValue

						alpha = lerp(
							start = 0.5f,
							stop = 1f,
							fraction = 1f - pageOffset.coerceIn(0f, 1f)
						)
					}
			) {
				val gradient = Brush.verticalGradient(
					colors = listOf(Color.Transparent, Color.Black)
				)

				Image(
					painter = painterResource(id = R.drawable.wambugu_holding_apples2),
					contentDescription = "Background Image",
					contentScale = ContentScale.Crop,
					modifier = Modifier
						.fillMaxSize()
						.clip(RoundedCornerShape(10.dp))

				)

				Box(
					modifier = Modifier
						.fillMaxWidth()
						.height(44.dp)
						.align(Alignment.BottomStart)
						.background(gradient)
				) {
					Text(
						text = "How to Get Better Yeilds",
						color = Color.White,
						fontSize = 20.sp,
						modifier = Modifier
							.align(Alignment.CenterStart)
							.padding(top = 4.dp, bottom = 4.dp, start = 8.dp)

					)
				}


			}
		}

	}
}

@Composable
fun AgroVetNearMe(modifier: Modifier = Modifier, onFindAgro: () -> Unit) {

	Column(
		horizontalAlignment = Alignment.CenterHorizontally,
		verticalArrangement = Arrangement.Center,
		modifier = modifier
	) {
		Text(
			text = "Agrovet Near Me",
			textAlign = TextAlign.Start,
			style = MaterialTheme.typography.titleMedium,
			modifier = Modifier
				.padding(top = 8.dp, start = 2.dp)
				.fillMaxWidth()
		)
		Column(
			horizontalAlignment = Alignment.CenterHorizontally,
			verticalArrangement = Arrangement.SpaceBetween,
			modifier = Modifier
				.fillMaxHeight()
				.fillMaxWidth()
				.padding(top = 4.dp)
				.border(
					width = 2.dp,
					color = MaterialTheme.colorScheme.primaryContainer,
					shape = RoundedCornerShape(10.dp)
				)
		) {
			Row(
				horizontalArrangement = Arrangement.SpaceBetween
			) {
				Image(
					painter = painterResource(id = R.drawable.wambugu_apples),
					contentDescription = "Pharmacist Image",
					contentScale = ContentScale.Crop,
					modifier = Modifier
						.fillMaxWidth(0.45f)
						.fillMaxHeight(0.79f)
						.padding(8.dp)
						.clip(RoundedCornerShape(5.dp))
				)
				Column(
					modifier = Modifier
						.padding(start = 0.dp, top = 8.dp, bottom = 0.dp, end = 4.dp)
				) {
					Text(
						text = "One-to-One Consultation",
						fontWeight = FontWeight.Bold,
						fontSize = 14.sp,
					)
					Text(
						text = "Acquire precise solutions for your problems. Get answers to your problems.",
						style = MaterialTheme.typography.bodyMedium,
					)
				}
			}
			Button(
				onClick = onFindAgro,
				colors = ButtonDefaults.buttonColors(
					containerColor = MaterialTheme.colorScheme.primaryContainer,
					contentColor = MaterialTheme.colorScheme.onPrimaryContainer
				),
				modifier = Modifier
					.fillMaxWidth(0.9f)
					.height(36.dp)
			) {
				Text("Find AgroVet Near Me")
			}
			Spacer(
				modifier = Modifier
					.fillMaxWidth()
					.height(8.dp)
			)
		}
	}

}


@Preview
@Composable
fun TopAppBarPreview() {
	HomeTopAppBar(onMenuClicked = { })
}

@Preview(showBackground = true, showSystemUi = true)
@Composable
fun HomePreview() {
	Home(onFindAgro = { }, location = "")
}