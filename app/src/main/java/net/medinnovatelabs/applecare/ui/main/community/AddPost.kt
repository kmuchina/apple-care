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
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import kotlinx.coroutines.launch
import net.medinnovatelabs.applecare.data.models.CommunityPost
import net.medinnovatelabs.applecare.ui.Camera.toBitmap
import net.medinnovatelabs.applecare.utils.BitmapConverter.convertBitmapToString
import net.medinnovatelabs.applecare.utils.BitmapConverter.convertStringToBitmap
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
		colors = TopAppBarDefaults.mediumTopAppBarColors(
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

		var title by remember { mutableStateOf("") }
		var content by remember { mutableStateOf("") }
		var image: String? by remember { mutableStateOf(null) }

		val galleryLauncher = rememberLauncherForActivityResult(
			contract = ActivityResultContracts.GetContent(),
			onResult = { uri ->
				// Handle the selected image URI
				uri?.let { selectedUri ->
					val bitmap = selectedUri.toBitmap(context.contentResolver)
					if (bitmap != null) image = convertBitmapToString(bitmap)
				}
			}
		)


		Column(
			modifier = modifier
				.padding(innerPadding)
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

			image?.let {
				val bitmap = convertStringToBitmap(it)
				Image(
					bitmap.asImageBitmap(),
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

			val date = LocalDate.now()
			val formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy", Locale.ENGLISH)
			val formattedDate = date.format(formatter)

			val cp = CommunityPost(
				userPicture = null,
				userName = null,
				postTime = formattedDate,
				postPicture = image ?: "",
				title = title,
				content = content,
			)

			val coroutineScope = rememberCoroutineScope()

			Button(
				onClick = {
					coroutineScope.launch {
						viewModel.addPost(cp)
					}
					onBack()
				},
				modifier = Modifier.fillMaxWidth()
			) {
				Text("Post")
			}
		}
	}
}

@Preview
@Composable
fun AddPostPreview() {
	AddPost(onBack = {})
}