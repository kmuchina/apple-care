package net.medinnovatelabs.applecare.ui.Camera

import android.graphics.Bitmap
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import net.medinnovatelabs.applecare.TensorFLowHelper


@Composable
fun PhotoBottomSheetContent(
	bitmap: Bitmap,
	onDismiss: () -> Unit
) {
	Column(
		modifier = Modifier.fillMaxWidth(),
		verticalArrangement = Arrangement.Center,
		horizontalAlignment = Alignment.CenterHorizontally
	) {
		Image(
			bitmap = bitmap.asImageBitmap(),
			contentDescription = "Classified Image",
			Modifier.size(300.dp)
		)
		Spacer(modifier = Modifier.padding(8.dp))

		val scaledBitmap = Bitmap.createScaledBitmap(
			bitmap,
			TensorFLowHelper.IMAGESIZE,
			TensorFLowHelper.IMAGESIZE, false
		);

		TensorFLowHelper.ClassifyImage(scaledBitmap) {
			Column(
				modifier = Modifier.fillMaxWidth(),
				verticalArrangement = Arrangement.Center,
				horizontalAlignment = Alignment.CenterHorizontally
			) {
				Text(text = "Image is classified as:")
				Text(text = it, fontSize = 20.sp)
			}
		}

		Spacer(modifier = Modifier.padding(8.dp))

		Button(onClick = onDismiss) {
			Text("Dismiss")
		}
	}

}