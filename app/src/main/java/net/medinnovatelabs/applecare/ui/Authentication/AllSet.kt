package net.medinnovatelabs.applecare.ui.Authentication

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.ThumbUp
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import kotlinx.coroutines.delay
import net.medinnovatelabs.applecare.R

@Composable
fun AllSet(
	onDelay: () -> Unit
){
	Column (
		horizontalAlignment = Alignment.CenterHorizontally,
		verticalArrangement = Arrangement.Center,
		modifier = Modifier.fillMaxSize()
	){
		Row {
			Text(
				text = "You are all set",
				style = TextStyle(
					fontSize = 30.sp,
					fontFamily = FontFamily(Font(R.font.inter)),
					fontWeight = FontWeight(600),
					color = Color(0xFF000000),
					textAlign = TextAlign.Center,
				)
			)

			Icon(
				imageVector = Icons.Outlined.ThumbUp,
				contentDescription = "Wards Icon",
				tint = Color(0xFF5DB075),
				modifier = Modifier
					.padding(start = 5.dp, top = 2.dp)
					.size(36.dp)
			)
		}
		Text(
			text = "You can login to you account",
			style = TextStyle(
				fontSize = 14.sp,
				fontFamily = FontFamily(Font(R.font.inter)),
				fontWeight = FontWeight(400),
				color = Color(0xFF000000),
				textAlign = TextAlign.Center,
			)
		)
	}
	LaunchedEffect(Unit) {
		delay(1500L)
		onDelay()
	}
}

@Preview(showBackground = true, showSystemUi = true)
@Composable
fun AllSetPreview() {
	AllSet(onDelay = { })
}