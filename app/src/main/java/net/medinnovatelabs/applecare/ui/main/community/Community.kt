package net.medinnovatelabs.applecare.ui.main.community

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.Comment
import androidx.compose.material.icons.filled.FilterList
import androidx.compose.material.icons.filled.ThumbDown
import androidx.compose.material.icons.filled.ThumbUp
import androidx.compose.material.icons.outlined.MoreVert
import androidx.compose.material.icons.outlined.Search
import androidx.compose.material.icons.rounded.Person
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedCard
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import kotlinx.coroutines.launch
import net.medinnovatelabs.applecare.R
import net.medinnovatelabs.applecare.data.models.CommunityPost
import net.medinnovatelabs.applecare.utils.BitmapConverter.convertStringToBitmap

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CommunityTopAppBar(
	onSettingsClicked: () -> Unit,
	onAddPost: () -> Unit,
	modifier: Modifier = Modifier
) {
	var expanded by remember { mutableStateOf(false) }

	TopAppBar(
		title = {
			androidx.compose.material3.Text(
				stringResource(id = R.string.community),
				style = androidx.compose.material3.MaterialTheme.typography.titleMedium,
				color = androidx.compose.material3.MaterialTheme.colorScheme.onPrimary
			)
		},
		colors = TopAppBarDefaults.topAppBarColors(
			containerColor = androidx.compose.material3.MaterialTheme.colorScheme.primary,
			titleContentColor = androidx.compose.material3.MaterialTheme.colorScheme.onPrimary,
			navigationIconContentColor = androidx.compose.material3.MaterialTheme.colorScheme.onPrimary
		),
		actions = {
			IconButton(onClick = { expanded = !expanded }) {
				Icon(
					Icons.Outlined.MoreVert,
					contentDescription = "More",
					tint = androidx.compose.material3.MaterialTheme.colorScheme.onPrimary
				)
			}
			Box(
				modifier = Modifier.offset(x = (-16).dp, y = 20.dp),
				contentAlignment = Alignment.TopCenter
			) {
				DropdownMenu(
					expanded = expanded,
					onDismissRequest = { expanded = false }
				) {
					DropdownMenuItem(
						text = { androidx.compose.material3.Text("Settings") },
						onClick = { onSettingsClicked() },
					)
					DropdownMenuItem(
						text = { androidx.compose.material3.Text("Add Post") },
						onClick = { onAddPost() },
					)
				}
			}
		}
	)
}

@Preview
@Composable
fun SearchBar() {
	var text by remember { mutableStateOf(TextFieldValue("")) }
	Row(
		horizontalArrangement = Arrangement.SpaceBetween,
		modifier = Modifier
			.fillMaxWidth()
			.padding(bottom = 8.dp)
	) {
		OutlinedTextField(
			value = text,
			onValueChange = { text = it },
			modifier = Modifier
				.weight(1f)
				.height(56.dp),
			placeholder = { Text(text = "Search") },
			leadingIcon = {
				Icon(
					imageVector = Icons.Outlined.Search,
					contentDescription = null,
					tint = Color(0xFF5DB075)
				)
			},
			shape = RoundedCornerShape(16.dp),
			colors = OutlinedTextFieldDefaults.colors(
				focusedBorderColor = Color.Black,
				unfocusedBorderColor = Color.Black
			)
		)
		Spacer(modifier = Modifier.width(8.dp))
		IconButton(
			onClick = { /* Handle filter click */ },
			modifier = Modifier
				.size(56.dp)
		) {
			Icon(
				imageVector = Icons.Default.FilterList,
				contentDescription = null,
				tint = Color(0xFF5DB075),
				modifier = Modifier.size(40.dp)
			)
		}
	}
}

@Composable
fun Community(
	viewModel: CommunityViewModel = hiltViewModel()
) {
	val communityPosts = viewModel.communityPosts.collectAsState().value
	Column(
		modifier = Modifier
			.fillMaxSize()
			.padding(20.dp)

	) {
		SearchBar()
		LazyColumn() {
			items(items = communityPosts) { post ->
				PostCard(
					post,
					viewModel
				)
				Spacer(modifier = Modifier.height(16.dp))
			}
		}
	}
}

@Composable
fun PostCard(
	post: CommunityPost,
	viewModel: CommunityViewModel
) {
	OutlinedCard(
		modifier = Modifier
			.fillMaxWidth()
			.height(330.dp),
		shape = RoundedCornerShape(16.dp),
		border = BorderStroke(1.dp, Color.Black)
	) {
		Column(
			modifier = Modifier
				.padding(16.dp)
				.fillMaxSize()
		) {
			// Top Image
			Image(
				bitmap = convertStringToBitmap(post.postPicture).asImageBitmap(),
				contentDescription = null,
				contentScale = ContentScale.Crop,
				modifier = Modifier
					.fillMaxWidth()
					.height(128.dp)
					.clip(RoundedCornerShape(16.dp))
			)
			Spacer(modifier = Modifier.height(8.dp))
			// Profile Section
			Row(verticalAlignment = Alignment.CenterVertically) {
				Icon(
					Icons.Rounded.Person,
					contentDescription = null,
					modifier = Modifier
						.size(40.dp)
						.clip(CircleShape)
				)

				Spacer(modifier = Modifier.width(8.dp))
				Column {
					Text(
						text = post.userName ?: stringResource(R.string.anonymous),
						fontSize = 16.sp
					)
					Text(
						text = post.postTime,
						fontSize = 14.sp, color = Color.Gray
					)
				}
			}
			Spacer(modifier = Modifier.height(8.dp))
			// Post Title
			Text(
				text = post.title,
				maxLines = 1,
				overflow = TextOverflow.Ellipsis
			)
			Spacer(modifier = Modifier.height(8.dp))
			// Post Content
			Text(
				text = post.content,
				fontSize = 14.sp
			)
			Spacer(modifier = Modifier.height(8.dp))

			// Action Icons
			Row(
				horizontalArrangement = Arrangement.SpaceBetween,
				modifier = Modifier
					.fillMaxWidth()
					.height(50.dp)

			) {
				IconButton(onClick = { /* Handle comment */ }) {
					Icon(imageVector = Icons.AutoMirrored.Filled.Comment, contentDescription = "Comment")
				}
				Row {
					IconButton(onClick = { /* Handle like */ }) {
						Icon(imageVector = Icons.Default.ThumbUp, contentDescription = "Like")
					}
					IconButton(onClick = { /* Handle dislike */ }) {
						Icon(imageVector = Icons.Default.ThumbDown, contentDescription = "Dislike")
					}

					var expanded by remember { mutableStateOf(false) }
					val coroutineScope = rememberCoroutineScope()


					IconButton(onClick = { expanded = !expanded }) {
						Icon(
							Icons.Outlined.MoreVert,
							contentDescription = "More"
						)
					}

					Box(
						contentAlignment = Alignment.TopCenter
					) {
						DropdownMenu(
							expanded = expanded,
							onDismissRequest = { expanded = false }
						) {
							DropdownMenuItem(
								text = { androidx.compose.material3.Text("Share") },
								onClick = { },
							)
							DropdownMenuItem(
								text = { androidx.compose.material3.Text("Delete") },
								onClick = {
									coroutineScope.launch {
										viewModel.deletePost(post)
									}
								},
							)
						}
					}

				}
			}
		}
	}
}
