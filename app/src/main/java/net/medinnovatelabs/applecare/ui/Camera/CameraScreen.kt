package net.medinnovatelabs.applecare.ui.Camera

import android.content.ContentResolver
import android.content.ContentValues
import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.Matrix
import android.net.Uri
import android.provider.MediaStore
import android.util.Log
import android.widget.Toast
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.camera.core.CameraSelector
import androidx.camera.core.ImageCapture
import androidx.camera.core.ImageCaptureException
import androidx.camera.core.ImageProxy
import androidx.camera.view.CameraController
import androidx.camera.view.LifecycleCameraController
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Cameraswitch
import androidx.compose.material.icons.outlined.Photo
import androidx.compose.material.icons.outlined.PhotoCamera
import androidx.compose.material3.BottomSheetScaffold
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.rememberBottomSheetScaffoldState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.core.content.ContextCompat
import androidx.lifecycle.viewmodel.compose.viewModel
import mu.KotlinLogging
import net.medinnovatelabs.applecare.R
import java.io.File
import java.io.InputStream


@ExperimentalMaterial3Api
@Composable
fun Camera() {
	val logger = KotlinLogging.logger {}
	val context = LocalContext.current
	val controller = remember {
		LifecycleCameraController(context).apply {
			setEnabledUseCases(
				CameraController.IMAGE_CAPTURE or
						CameraController.VIDEO_CAPTURE
			)
		}
	}
	val scaffoldState = rememberBottomSheetScaffoldState()
	val scope = rememberCoroutineScope()
	val viewModel = viewModel<MainViewModel>()
	val bitmaps by viewModel.bitmaps.collectAsState()

	// ActivityResultLauncher for handling gallery opening and result
	val galleryLauncher = rememberLauncherForActivityResult(
		contract = ActivityResultContracts.GetContent(),
		onResult = { uri ->
			// Handle the selected image URI
			uri?.let { selectedUri ->
				val selectedBitmap = selectedUri.toBitmap(context.contentResolver)
				selectedBitmap?.let { bitmap ->
					// Use viewModel.onTakePhoto(bitmap) to handle the selected image
					viewModel.onTakePhoto(bitmap)
				}
			}
		}
	)

	BottomSheetScaffold(
		scaffoldState = scaffoldState,
		sheetPeekHeight = 0.dp,
		sheetContent = {
			PhotoBottomSheetContent(
				bitmaps = bitmaps,
				modifier = Modifier.fillMaxWidth()
			)
		}
	) { padding ->
		Box(
			modifier = Modifier
				.fillMaxSize()
				.padding(padding)
		) {
			CameraPreview(
				controller = controller,
				modifier = Modifier.fillMaxSize()
			)
			Row(
				modifier = Modifier
					.align(Alignment.TopStart)
					.padding(16.dp),
				horizontalArrangement = Arrangement.SpaceAround
			) {
				Box(
					contentAlignment = Alignment.Center,
					modifier = Modifier.size(56.dp)
				) {
					Box(
						modifier = Modifier
							.size(48.dp)
							.background(Color.White, shape = CircleShape)
							.clickable( // Wrap Box with Clickable
								onClick = {
									Toast.makeText(context, "Camera switching in 1...2..", Toast.LENGTH_SHORT).show()

									controller.cameraSelector =
										if (controller.cameraSelector == CameraSelector.DEFAULT_BACK_CAMERA) {
											CameraSelector.DEFAULT_FRONT_CAMERA
										} else CameraSelector.DEFAULT_BACK_CAMERA

								}
							)
					) {

						IconButton(
							onClick = {
								Toast.makeText(context, "Camera switching in 1...2..", Toast.LENGTH_SHORT).show()

								controller.cameraSelector =
									if (controller.cameraSelector == CameraSelector.DEFAULT_BACK_CAMERA) {
										CameraSelector.DEFAULT_FRONT_CAMERA
									} else CameraSelector.DEFAULT_BACK_CAMERA
							}
						) {
							Icon(
								imageVector = Icons.Outlined.Cameraswitch,
								contentDescription = "Switch Camera",
								tint = Color(0xFF0D5210)
							)

						}
					}
				}
			}
			// Gallery button
			Row(
				modifier = Modifier
//					.fillMaxWidth()
					.align(Alignment.BottomStart)
					.padding(16.dp),
				horizontalArrangement = Arrangement.SpaceAround
			) {
				Box(
					contentAlignment = Alignment.Center,
					modifier = Modifier.size(56.dp)
				) {
					Box(
						modifier = Modifier
							.size(48.dp)
							.background(Color.White, shape = CircleShape)
							.clickable( // Wrap Box with Clickable
								onClick = {
									Toast.makeText(context, "A moment please...🫸🏾...Opening your Gallery...📸", Toast.LENGTH_SHORT).show()

									galleryLauncher.launch("image/*")
								}
							)
					) {

						IconButton(
							onClick = {
								// Open gallery when clicked
								Toast.makeText(context, "A moment please...🫸🏾...Opening your Gallery...📸", Toast.LENGTH_SHORT).show()

								galleryLauncher.launch("image/*")
							},
							modifier = Modifier
								.align(Alignment.BottomStart)
								.padding(16.dp) .fillMaxSize()
						) {
							Icon(
								imageVector = Icons.Outlined.Photo,
								contentDescription = "Open Gallery",
								tint = Color(0xFF0C3A0E),
								modifier = Modifier.fillMaxSize()
							)
						}
					}
				}
			}


			Box(
				contentAlignment = Alignment.BottomEnd,
				modifier = Modifier.size(56.dp) .offset(x = 299.dp, y = 710.dp)
			) {
				Box(
					modifier = Modifier
						.size(48.dp)
						.background(Color.White, shape = CircleShape)
						.clickable( // Wrap Box with Clickable
							onClick = {
								takePhoto(
									controller = controller,
									context = context,
									onPhotoTaken = viewModel::onTakePhoto
								)
							}
						)
				) {
					IconButton(
						onClick = {
							takePhoto(
								controller = controller,
								context = context,
								onPhotoTaken = viewModel::onTakePhoto
							)
						},
						modifier = Modifier
							.align(Alignment.BottomEnd)
							.padding(16.dp)
					) {
						Icon(
							imageVector = Icons.Outlined.PhotoCamera,
							contentDescription = "Take Photo",
							tint = Color(0xFF0C3A0E)
						)
					}
				}
			}


		}
	}
}


fun takePhoto(
	controller: LifecycleCameraController,
	context: Context,
	onPhotoTaken: (Bitmap) -> Unit
) {
	val cameraSelector = CameraSelector.Builder().build()

	controller.takePicture(
		ContextCompat.getMainExecutor(context),
		object : ImageCapture.OnImageCapturedCallback() {
			override fun onCaptureSuccess(image: ImageProxy) {
				super.onCaptureSuccess(image)

				val bitmap = image.toBitmap()
				image.close()

				val rotationDegrees = image.imageInfo.rotationDegrees
				val rotatedBitmap = rotateBitmap(bitmap, rotationDegrees)

				savePhotoToGallery(context, rotatedBitmap)

				// Show toast message
				Toast.makeText(context, "Photo captured successfully...🍏...🎉🎉", Toast.LENGTH_SHORT).show()

				onPhotoTaken(rotatedBitmap)
			}

			override fun onError(exception: ImageCaptureException) {
				super.onError(exception)
				Log.e("Camera", "Couldn't take photo: 🥲", exception)
			}
		}
	)
}


private fun rotateBitmap(bitmap: Bitmap, degrees: Int): Bitmap {
	val matrix = Matrix()
	matrix.postRotate(degrees.toFloat())
	return Bitmap.createBitmap(bitmap, 0, 0, bitmap.width, bitmap.height, matrix, true)
}

private fun savePhotoToGallery(context: Context, bitmap: Bitmap) {
	val contentValues = ContentValues().apply {
		put(MediaStore.Images.Media.DISPLAY_NAME, "CapturedPhoto_${System.currentTimeMillis()}")
		put(MediaStore.Images.Media.MIME_TYPE, "image/jpeg")
		put(MediaStore.Images.Media.DATE_ADDED, System.currentTimeMillis() / 1000)
		put(MediaStore.Images.Media.DATE_TAKEN, System.currentTimeMillis())
	}

	val uri = context.contentResolver.insert(
		MediaStore.Images.Media.EXTERNAL_CONTENT_URI,
		contentValues
	)

	uri?.let { uri ->
		try {
			context.contentResolver.openOutputStream(uri)?.use { outputStream ->
				bitmap.compress(Bitmap.CompressFormat.JPEG, 100, outputStream)
			}
		} catch (e: Exception) {
			Log.e("Camera", "Error saving photo to gallery: ${e.message}", e)
		}
	}
}

private fun getOutputDirectory(context: Context): File {
	val mediaDir = context.externalMediaDirs.firstOrNull()?.let {
		File(it, context.getString(R.string.app_name)).apply { mkdirs() }
	}
	return if (mediaDir != null && mediaDir.exists())
		mediaDir else context.filesDir
}
fun Uri.toBitmap(contentResolver: ContentResolver): Bitmap? {
	var inputStream: InputStream? = null
	return try {
		inputStream = contentResolver.openInputStream(this)
		BitmapFactory.decodeStream(inputStream)
	} catch (e: Exception) {
		e.printStackTrace()
		null
	} finally {
		inputStream?.close()
	}
}


private const val galleryRequestCode = 100
