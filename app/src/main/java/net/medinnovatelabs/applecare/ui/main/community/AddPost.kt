package net.medinnovatelabs.applecare.ui.main.community

import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.sharp.ArrowBack
import androidx.compose.material.icons.outlined.Edit
import androidx.compose.material.icons.outlined.Image
import androidx.compose.material3.Button
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import kotlinx.coroutines.launch
import net.medinnovatelabs.applecare.data.models.CommunityPost
import net.medinnovatelabs.applecare.ui.Camera.toBitmap
import net.medinnovatelabs.applecare.utils.BitmapConverter.convertBitmapToString
import net.medinnovatelabs.applecare.utils.BitmapConverter.convertStringToBitmap
import net.medinnovatelabs.applecare.utils.BitmapConverter.downscale
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import androidx.compose.foundation.layout.consumeWindowInsets
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.runtime.saveable.rememberSaveable
import java.time.LocalDate
import java.time.format.DateTimeFormatter
import java.util.Locale


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AddPostTopAppBar(
	onBack: () -> Unit,
	modifier: Modifier = Modifier
) {
	CenterAlignedTopAppBar(
		navigationIcon = {
			IconButton(onClick = { onBack() }) {
				Icon(Icons.AutoMirrored.Sharp.ArrowBack, null)
			}
		},
		title = {
			Text(
				"Ask Community",
				style = MaterialTheme.typography.titleMedium,
				color = MaterialTheme.colorScheme.onPrimary
			)
		},
		colors = TopAppBarDefaults.topAppBarColors(
			containerColor = MaterialTheme.colorScheme.primary,
			titleContentColor = MaterialTheme.colorScheme.onPrimary,
			navigationIconContentColor = MaterialTheme.colorScheme.onPrimary
		)
	)
}

@Composable
fun AddPost(
	onBack: () -> Unit,
	modifier: Modifier = Modifier,
	viewModel: CommunityViewModel = hiltViewModel()
) {
	Scaffold(
		topBar = { AddPostTopAppBar(onBack = onBack) }
	) { innerPadding ->

		val context = LocalContext.current
		val scope = rememberCoroutineScope()

		var title by rememberSaveable { mutableStateOf("") }
		var content by rememberSaveable { mutableStateOf("") }
		var image: String? by remember { mutableStateOf(null) }
		val imagePreview = remember(image) { image?.let { convertStringToBitmap(it).asImageBitmap() } }

		val galleryLauncher = rememberLauncherForActivityResult(
			contract = ActivityResultContracts.GetContent(),
			onResult = { uri ->
				uri ?: return@rememberLauncherForActivityResult
				scope.launch {
					image = withContext(Dispatchers.IO) {
						uri.toBitmap(context.contentResolver)
							// Full-size photos as Base64 overflow SQLite's ~2 MB row limit
							// and crash the community feed when it is read back.
							?.let { downscale(it, MAX_POST_IMAGE_SIZE) }
							?.let(::convertBitmapToString)
					} ?: image
				}
			}
		)

		Column(
			modifier = modifier
				.padding(innerPadding)
				.consumeWindowInsets(innerPadding)
				.imePadding()
				.verticalScroll(rememberScrollState())
				.padding(horizontal = 20.dp)
		) {
			Spacer(modifier = Modifier.height(20.dp))

			Button(onClick = {
				galleryLauncher.launch("image/*")
			}) {
				Icon(
					image?.let { Icons.Outlined.Edit } ?: Icons.Outlined.Image,
					contentDescription = "Upload Image"
				)
				Spacer(modifier = Modifier.width(8.dp))
				Text("Upload Image")
			}

			Spacer(modifier = Modifier.height(16.dp))

			imagePreview?.let {
				Image(
					it,
					"Uploaded Image",
					modifier = Modifier
						.fillMaxWidth()
						.height(200.dp)
				)
			}

			Column {
				Text(
					text = "Title of your post",
					style = MaterialTheme.typography.titleMedium,
				)
				OutlinedTextField(
					value = title,
					onValueChange = { title = it },
					placeholder = { Text("Subject of your post") },
					modifier = Modifier.fillMaxWidth()
				)

				Spacer(modifier = Modifier.height(16.dp))
			}

			Column {
				Text(
					text = "Description of your post",
					style = MaterialTheme.typography.titleMedium,
				)
				OutlinedTextField(
					value = content,
					onValueChange = { content = it },
					placeholder = { Text("Describe its symptoms such as color of leaves and fruits etc") },
					modifier = Modifier.fillMaxWidth()
				)
				Spacer(modifier = Modifier.height(16.dp))
			}

			Button(
				enabled = title.isNotBlank() && content.isNotBlank(),
				onClick = {
					viewModel.addPost(
						CommunityPost(
							userPicture = null,
							userName = null,
							postTime = LocalDate.now().format(
								DateTimeFormatter.ofPattern("dd/MM/yyyy", Locale.ENGLISH)
							),
							postPicture = image ?: "",
							title = title.trim(),
							content = content.trim(),
						)
					)
					onBack()
				},
				modifier = Modifier.fillMaxWidth()
			) {
				Text("Post")
			}
		}
	}
}

private const val MAX_POST_IMAGE_SIZE = 1024