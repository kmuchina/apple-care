package net.medinnovatelabs.applecare.ui.onboarding

import android.app.Activity
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.runtime.DisposableEffect
import androidx.compose.ui.platform.LocalView
import androidx.core.view.WindowCompat
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.safeDrawingPadding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import net.medinnovatelabs.applecare.R

@Composable
fun OnBoardingScreen(
	onSkip: () -> Unit
) {
	val result = remember { mutableIntStateOf(0) }

	// The rest of the app has green top bars with light status bar icons; this screen has a
	// plain background, so use dark icons in light mode while it is shown.
	val view = LocalView.current
	val darkTheme = isSystemInDarkTheme()
	if (!view.isInEditMode) {
		DisposableEffect(darkTheme) {
			val controller = WindowCompat.getInsetsController((view.context as Activity).window, view)
			controller.isAppearanceLightStatusBars = !darkTheme
			onDispose { controller.isAppearanceLightStatusBars = darkTheme }
		}
	}

	val imageResource = when (result.intValue) {
		0 -> R.drawable.onboarding_vector_1
		1 -> R.drawable.onboarding_vector_2
		else -> R.drawable.onboarding_vector_3
	}

	val textResource: String = when (result.intValue) {
		0 -> stringResource(R.string.onboarding_text_1)
		1 -> stringResource(R.string.onboarding_text_2)
		else -> stringResource(R.string.onboarding_text_3)
	}
	Column(
		verticalArrangement = Arrangement.SpaceBetween,
		horizontalAlignment = Alignment.CenterHorizontally,
		modifier = Modifier
			.fillMaxSize()
			.safeDrawingPadding()
			.wrapContentSize(Alignment.Center),
	) {
		Column(
			verticalArrangement = Arrangement.SpaceBetween,
			horizontalAlignment = Alignment.CenterHorizontally,
		) {
			Image(
				painter = painterResource(imageResource),
				contentDescription = null,
				modifier = Modifier
					.size(300.dp)
			)
			Text(
				text = textResource,
				fontSize = 16.sp,
				minLines = 2,
				fontWeight = FontWeight.Bold,
				textAlign = TextAlign.Center
			)
		}

		Spacer(modifier = Modifier.height(196.dp))

		Column(
			verticalArrangement = Arrangement.SpaceBetween,
			horizontalAlignment = Alignment.CenterHorizontally,
		) {
			Button(
				onClick = {
					if (result.intValue < 2) result.intValue++
					else onSkip()
				},
				colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF5DB075)),
				modifier = Modifier
					.width(184.dp)
					.height(36.dp)
			) {
				if (result.intValue == 2) {
					Text(
						text = "Get Started"
					)
				} else {
					Text(
						text = stringResource(R.string.next)
					)
				}
			}

			Spacer(modifier = Modifier.height(8.dp))

			TextButton(onClick = onSkip) {
				Text(
					text = stringResource(R.string.skip),
					color = Color(0xFF000000)
				)

			}
		}

		Spacer(Modifier.height(48.dp))

		Row(
			verticalAlignment = Alignment.Bottom,
			modifier = Modifier
				.fillMaxWidth(),
			horizontalArrangement = Arrangement.Center
		) {
			repeat(3) { iteration ->
				val color = if (result.intValue == iteration) Color(0xFF5DB075)
				else Color(0x405DB075)
				Canvas(
					modifier = Modifier
						.padding(horizontal = 4.dp)
						.size(8.dp),
					onDraw = {
						drawCircle(color = color)
					}
				)
			}
		}
	}
}

@Preview(showBackground = true, showSystemUi = true)
@Composable
fun OnBoardScreenPreview() {
	OnBoardingScreen(onSkip = { })
}
