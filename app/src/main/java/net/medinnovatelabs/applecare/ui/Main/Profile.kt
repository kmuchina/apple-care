package net.medinnovatelabs.applecare.ui.Main

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Email
import androidx.compose.material.icons.outlined.Person
import androidx.compose.material.icons.outlined.Phone
import androidx.compose.material.icons.rounded.Person
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import net.medinnovatelabs.applecare.firebasedata.UserData

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun Profile(
	userData: UserData?,
	onLogOut: () -> Unit,
	modifier: Modifier = Modifier
) {
	Column(
		horizontalAlignment = Alignment.CenterHorizontally,
		verticalArrangement = Arrangement.Top,
		modifier = modifier
			.fillMaxSize()
			.padding(top = 8.dp, start = 20.dp, end = 20.dp, bottom = 12.dp)
	) {
		// Profile Image
		if (userData?.profilePictureUrl != null) {
			AsyncImage(
				model = userData.profilePictureUrl,
				contentDescription = "Profile picture",
				modifier = Modifier
					.size(80.dp)
					.clip(CircleShape),
				contentScale = ContentScale.Fit
			)
			Spacer(modifier = Modifier.height(16.dp))
		} else {
			Icon(
				Icons.Rounded.Person,
				"null",
				modifier = Modifier
					.size(80.dp)
					.clip(CircleShape)
			)
		}

		// Profile ID

		// Profile Name
		Column(
			modifier = Modifier
				.padding(bottom = 20.dp)
		) {
			Text("Name")
			OutlinedTextField(
				value = userData?.username ?: "John Doe",
				onValueChange = { },
				shape = RoundedCornerShape(8.dp),
				leadingIcon = {
					Icon(
						imageVector = Icons.Outlined.Person,
						contentDescription = "Person"
					)
				},
				keyboardOptions = KeyboardOptions.Default.copy(imeAction = ImeAction.Next),
				colors = OutlinedTextFieldDefaults.colors(
					unfocusedLeadingIconColor = MaterialTheme.colorScheme.primary,
				),
				modifier = Modifier.fillMaxWidth()
			)
		}
		// Profile Email
		Column(
			modifier = Modifier
				.padding(bottom = 20.dp)
		) {
			Text("Email")
			OutlinedTextField(
				value = "johndoe@example.com",
				onValueChange = { },
				shape = RoundedCornerShape(8.dp),
				leadingIcon = {
					Icon(
						imageVector = Icons.Outlined.Email,
						contentDescription = "Email"
					)
				},
				keyboardOptions = KeyboardOptions.Default.copy(imeAction = ImeAction.Next),
				colors = OutlinedTextFieldDefaults.colors(
					unfocusedLeadingIconColor = MaterialTheme.colorScheme.primary,
				),
				modifier = Modifier.fillMaxWidth()
			)
		}

		// Profile Phone Number
		Column(
			modifier = Modifier
				.padding(bottom = 20.dp)
		) {
			Text("Phone Number")
			OutlinedTextField(
				value = "07123456",
				onValueChange = { },
				shape = RoundedCornerShape(8.dp),
				leadingIcon = {
					Icon(
						imageVector = Icons.Outlined.Phone,
						contentDescription = "Phone Number"
					)
				},
				keyboardOptions = KeyboardOptions.Default.copy(imeAction = ImeAction.Next),
				colors = OutlinedTextFieldDefaults.colors(
					unfocusedLeadingIconColor = MaterialTheme.colorScheme.primary,
				),
				modifier = Modifier.fillMaxWidth()
			)
		}

		Spacer(Modifier.fillMaxHeight(0.1f))

		Row(
			horizontalArrangement = Arrangement.SpaceEvenly,
			modifier = Modifier.fillMaxWidth()
		) {
			// Profile Sign Out Button
			Button(
				onClick = onLogOut,
				colors = ButtonDefaults.buttonColors(
					containerColor = MaterialTheme.colorScheme.primaryContainer,
					contentColor = MaterialTheme.colorScheme.onPrimaryContainer
				),
				modifier = Modifier
					.height(36.dp)
			) {
				Text("Sign Out")
			}

			// Profile Save Changes Button
			Button(
				onClick = { /*TODO*/ },
				colors = ButtonDefaults.buttonColors(
					containerColor = MaterialTheme.colorScheme.primaryContainer,
					contentColor = MaterialTheme.colorScheme.onPrimaryContainer
				),
				modifier = Modifier
					.height(36.dp)
			) {
				Text("Save Changes")
			}
		}

		// Profile Delete Button
		Button(
			onClick = { /*TODO*/ },
			colors = ButtonDefaults.buttonColors(
				containerColor = MaterialTheme.colorScheme.error,
				contentColor = MaterialTheme.colorScheme.onError
			),
			modifier = Modifier
				.padding(top = 28.dp)
				.fillMaxWidth(0.9f)
				.height(36.dp)
		) {
			Text("Delete Account")
		}
	}
}

@Preview(showSystemUi = true, showBackground = true)
@Composable
fun ProfilePreview() {
	Profile(userData = null, onLogOut = { })
}