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
import androidx.camera.view.PreviewView
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.sharp.ArrowBack
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.outlined.Cameraswitch
import androidx.compose.material.icons.outlined.Photo
import androidx.compose.material.icons.outlined.PhotoCamera
import androidx.compose.material3.BottomSheetScaffold
import androidx.compose.material3.Button
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.rememberBottomSheetScaffoldState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalLifecycleOwner
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.viewinterop.AndroidView
import androidx.core.content.ContextCompat
import androidx.hilt.navigation.compose.hiltViewModel
import kotlinx.coroutines.launch
import net.medinnovatelabs.applecare.R
import net.medinnovatelabs.applecare.data.models.ClassifiedImage
import net.medinnovatelabs.applecare.utils.TensorFLowHelper
import java.io.File
import java.io.InputStream

@ExperimentalMaterial3Api
@Composable
fun Camera(
	viewModel: CameraViewModel = hiltViewModel(),
	onBack: () -> Unit
) {
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
	val bitmap by viewModel.bitmaps.collectAsState()
	val sheetPeekHeight by viewModel.sheetPeekHeight.collectAsState()

	// ActivityResultLauncher for handling gallery opening and result
	val galleryLauncher = rememberLauncherForActivityResult(
		contract = ActivityResultContracts.GetContent(),
		onResult = { uri ->
			// Handle the selected image URI
			uri?.let { selectedUri ->
				val selectedBitmap = selectedUri.toBitmap(context.contentResolver)
				selectedBitmap?.let { bitmap ->
					// Use viewModel.onTakePhoto(bitmap) to handle the selected image
					// viewModel.onTakePhoto(bitmap)
					val scaledBitmap = Bitmap.createScaledBitmap(
						selectedBitmap,
						TensorFLowHelper.IMAGESIZE,
						TensorFLowHelper.IMAGESIZE, false
					);

					TensorFLowHelper.classifyImage(scaledBitmap, context) {
						scope.launch {
							viewModel.onTakePhoto(it)
						}
					}
				}
			}
		}
	)

	BottomSheetScaffold(
		scaffoldState = scaffoldState,
		sheetPeekHeight = sheetPeekHeight.dp,
		topBar = {
			CenterAlignedTopAppBar(
				navigationIcon = {
					IconButton(onClick = onBack) {
						Icon(
							Icons.AutoMirrored.Sharp.ArrowBack,
							null
						)
					}
				},
				title = {
					Text(
						"Take a Picture",
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
		},
		sheetContent = {
			bitmap?.let {
				PhotoBottomSheetContent(bitmap = it, label = viewModel.label.value) {
					viewModel.reset()
				}
			}
		}
	) { it ->
		Column(
			modifier = Modifier.padding(it)
		) {
			Surface {
				CameraPreview(
					controller = controller,
					modifier = Modifier
						.fillMaxWidth()
						.fillMaxHeight(0.7f)
				)
				IconButton(
					modifier = Modifier
						.padding(horizontal = 24.dp, vertical = 16.dp)
						.size(48.dp)
						.background(
							MaterialTheme.colorScheme.primary,
							shape = CircleShape
						)
						.clickable( // Wrap Box with Clickable
							onClick = {
								Toast
									.makeText(
										context,
										"Camera switching in 1...2..",
										Toast.LENGTH_SHORT
									)
									.show()
								controller.cameraSelector =
									if (controller.cameraSelector == CameraSelector.DEFAULT_BACK_CAMERA) {
										CameraSelector.DEFAULT_FRONT_CAMERA
									} else CameraSelector.DEFAULT_BACK_CAMERA
							}
						),
					onClick = {
						Toast.makeText(
							context,
							"Camera switching in 1...2..",
							Toast.LENGTH_SHORT
						)
							.show()

						controller.cameraSelector =
							if (controller.cameraSelector == CameraSelector.DEFAULT_BACK_CAMERA) {
								CameraSelector.DEFAULT_FRONT_CAMERA
							} else CameraSelector.DEFAULT_BACK_CAMERA
					}
				) {
					Icon(
						imageVector = Icons.Outlined.Cameraswitch,
						contentDescription = "Switch Camera",
						tint = MaterialTheme.colorScheme.onPrimary
					)
				}
			}
			Row(
				horizontalArrangement = Arrangement.SpaceBetween,
				verticalAlignment = Alignment.CenterVertically,
				modifier = Modifier
					.fillMaxWidth()
					.fillMaxHeight()
					.padding(horizontal = 20.dp)
			) {
				var takePhoto by remember { mutableStateOf(false) }
				IconButton(
					modifier = Modifier
						.padding(16.dp)
						.size(72.dp)
						.background(
							MaterialTheme.colorScheme.primary,
							shape = CircleShape
						)
						.clickable(
							onClick = {
								Toast
									.makeText(
										context,
										"A moment please...🫸🏾...Opening your Gallery...📸",
										Toast.LENGTH_SHORT
									)
									.show()
								galleryLauncher.launch("image/*")
							}
						),
					onClick = {
						// Open gallery when clicked
						Toast.makeText(
							context,
							"A moment please...🫸🏾...Opening your Gallery...📸",
							Toast.LENGTH_SHORT
						).show()
						galleryLauncher.launch("image/*")
					}
				) {
					Icon(
						imageVector = Icons.Outlined.Photo,
						contentDescription = "Open Gallery",
						tint = MaterialTheme.colorScheme.onPrimary,
						modifier = Modifier.fillMaxSize(0.5f)
					)
				}

				IconButton(
					modifier = Modifier
						.padding(16.dp)
						.size(72.dp)
						.background(
							MaterialTheme.colorScheme.primary,
							shape = CircleShape
						),
					onClick = {
						if(takePhoto) {
							takePhoto(
								controller = controller,
								context = context,
								onPhotoTaken = {
									scope.launch {
										viewModel.onTakePhoto(it)
									}
								}
							)
						} else {
							takePhoto = true
						}

					}
				) {
					if(takePhoto) {
						Icon(
							imageVector = Icons.Default.Check,
							contentDescription = "Photo Ok",
							tint = MaterialTheme.colorScheme.onPrimary,
							modifier = Modifier
						)

					} else {
						Icon(
							imageVector = Icons.Outlined.PhotoCamera,
							contentDescription = "Photo Ok",
							tint = MaterialTheme.colorScheme.onPrimary,
							modifier = Modifier
								.fillMaxSize(0.5f)
						)
					}


				}
			}
		}
	}
}

@Composable
fun CameraPreview(
	controller: LifecycleCameraController,
	modifier: Modifier = Modifier
) {
	val lifecycleOwner = LocalLifecycleOwner.current
	AndroidView(
		factory = {
			PreviewView(it).apply {
				this.controller = controller
				controller.bindToLifecycle(lifecycleOwner)
			}
		},
		modifier = modifier
	)
}


@Composable
fun PhotoBottomSheetContent(
	bitmap: Bitmap,
	label: String,
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

		Column(
			modifier = Modifier.fillMaxWidth(),
			verticalArrangement = Arrangement.Center,
			horizontalAlignment = Alignment.CenterHorizontally
		) {
			Text(text = "Image is classified as:")
			Text(text = label, fontSize = 20.sp)
		}

		Spacer(modifier = Modifier.padding(8.dp))

		Button(onClick = onDismiss) {
			Text("Dismiss")
		}
	}
}


private fun takePhoto(
	controller: LifecycleCameraController,
	context: Context,
	onPhotoTaken: (ClassifiedImage) -> Unit
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
				Toast.makeText(context, "Photo captured successfully...🍏...🎉🎉", Toast.LENGTH_SHORT)
					.show()

				val scaledBitmap = Bitmap.createScaledBitmap(
					rotatedBitmap,
					TensorFLowHelper.IMAGESIZE,
					TensorFLowHelper.IMAGESIZE, false
				);

				TensorFLowHelper.classifyImage(scaledBitmap, context) {
					onPhotoTaken(it)
				}
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
