package net.medinnovatelabs.applecare.ui.Authentication

import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CutCornerShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.AccountBox
import androidx.compose.material.icons.outlined.Lock
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Checkbox
import androidx.compose.material3.CheckboxDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import kotlinx.coroutines.launch
import net.medinnovatelabs.applecare.R
import net.medinnovatelabs.applecare.firebasedata.SignInState
import net.medinnovatelabs.applecare.firebasedata.SignInViewModel


@Composable
fun LogIn(
	state: SignInState,
	onSignInWithGoogle: () -> Unit,
	onLoginClick: () -> Unit,
	onSignUpClick: () -> Unit,
	viewModel: SignInViewModel = hiltViewModel()
) {
	val context = LocalContext.current
	val scope = rememberCoroutineScope()
	val state1 = viewModel.signInState.collectAsState(initial = null)

	LaunchedEffect(key1 = state.signInError) {
		state.signInError?.let { error ->
			Toast.makeText(
				context,
				error,
				Toast.LENGTH_LONG
			).show()
		}
	}
	Column(
		horizontalAlignment = Alignment.CenterHorizontally,
		modifier = Modifier.fillMaxSize()
	) {
		Text(
			text = "Login in to your account",
			style = TextStyle(
				fontSize = 15.sp,
				fontFamily = FontFamily(Font(R.font.inter)),
				fontWeight = FontWeight(600),
				color = Color(0xFF000000),
				textAlign = TextAlign.Center,
			), modifier = Modifier
				.padding(top = 60.dp, bottom = 40.dp)
				.padding(horizontal = 92.dp)
		)

		LogButton(onLoginClick = onLoginClick)

		Spacer(modifier = Modifier.height(32.dp))

		OutlinedButton(
			onClick = { onSignInWithGoogle() },
			modifier = Modifier
				.width(320.dp)
				.height(40.dp)
				.background(color = Color.Transparent, shape = CutCornerShape(15.dp))
		) {
			Icon(
				painter = painterResource(id = R.drawable.google_icon),
				contentDescription = "Google Icon",
				modifier = Modifier.size(28.dp)
			)
			Spacer(modifier = Modifier.width(8.dp))
			Text("Login with Google")
		}

		Spacer(modifier = Modifier.height(20.dp))

		Row(
			horizontalArrangement = Arrangement.Start,
			verticalAlignment = Alignment.CenterVertically
		) {
			Text(
				"Don't have an account?",
				fontSize = 14.sp
			)
			TextButton(onClick = onSignUpClick) {
				Text(
					text = "Sign Up",
					fontSize = 14.sp,
					color = Color(0xFF5DB075)
				)
			}
		}
	}
}

@Composable
fun LogButton(
	onLoginClick: () -> Unit,
	viewModel: SignInViewModel = hiltViewModel()
) {
	Column(
		modifier = Modifier.padding(horizontal = 24.dp)
	) {
		var password by remember { mutableStateOf("") }
		var email by remember { mutableStateOf("") }
		val context = LocalContext.current
		val scope = rememberCoroutineScope()
		val state1 = viewModel.signInState.collectAsState(initial = null)
		var rememberMeChecked by remember { mutableStateOf(false) }
		var showDialog by remember { mutableStateOf(false) } // Track dialog visibility

		Column(
			verticalArrangement = Arrangement.Center,
			horizontalAlignment = Alignment.CenterHorizontally,
			modifier = Modifier
				.fillMaxWidth(),
		) {
			EmailInputBox(email = email, onEmailChange = { email = it })
			Spacer(modifier = Modifier.height(20.dp))
			PasswordTextField(password = password, onPasswordChange = { password = it })
			RememberMeCheckbox(
				onCheckedChange = { isChecked ->
					rememberMeChecked = isChecked
				},
				onForgotPasswordClick = {
					showDialog = true
				}
			)
			Spacer(modifier = Modifier.height(42.dp))
			Button(
				onClick = {
					scope.launch {
						viewModel.loginUser(email, password)
					}
					onLoginClick()
				},
				colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF5DB075)),
				modifier = Modifier
					.fillMaxWidth()
					.height(44.dp)
					.background(
						color = Color(0xFF5DB075),
						shape = RoundedCornerShape(size = 5.dp)
					),
			) {
				Text(
					text = "Login",
					color = Color.White,
					fontSize = 20.sp
				)
			}
			Spacer(modifier = Modifier.height(32.dp))
			Row {
				Box(
					modifier = Modifier
						.padding(vertical = 10.dp)
						.width(136.29747.dp)
						.height(1.dp)
						.background(color = Color(0xFF000000))
				)
				Spacer(modifier = Modifier.width(15.7.dp))
				Text(
					text = "or",
					style = TextStyle(
						fontSize = 15.sp,
						fontFamily = FontFamily(Font(R.font.inter)),
						fontWeight = FontWeight(600),
						color = Color(0xFF000000),
						textAlign = TextAlign.Center,
					), modifier = Modifier
						.width(16.dp)
						.height(18.dp)
				)
				Spacer(modifier = Modifier.width(15.7.dp))
				Box(
					modifier = Modifier
						.padding(vertical = 10.dp)
						.width(136.29747.dp)
						.height(1.dp)
						.background(color = Color(0xFF000000))
				)
			}
			if (showDialog) {
				ForgotPasswordDialog( // Show the dialog if visible
					onClose = { showDialog = false }, // Hide dialog on close
					onPasswordChanged = {},
					onDismissRequest = { showDialog = false }, // Hide dialog on dismiss
					onSavePassword = { newPassword, confirmPassword ->
						// Implement password save logic here (e.g., validate and save)
						showDialog = false // Hide dialog after save
					}
				)
			}

			if (state1.value?.isLoading == true) {
				CircularProgressIndicator()
			}
		}

		LaunchedEffect(key1 = state1.value?.isSuccess) {
			scope.launch {
				if (state1.value?.isSuccess?.isNotEmpty() == true) {
					val success = state1.value?.isSuccess
					Toast.makeText(context, "${success}", Toast.LENGTH_LONG).show()
				}
			}
		}
		LaunchedEffect(key1 = state1.value?.isError) {
			scope.launch {
				if (state1.value?.isError?.isNotEmpty() == true) {
					val error = state1.value?.isError
					Toast.makeText(context, "${error}", Toast.LENGTH_LONG).show()
				}
			}
		}
	}
}


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun EmailInputBox(email: String, onEmailChange: (String) -> Unit) {
	OutlinedTextField(
		value = email,
		onValueChange = { onEmailChange(it) },
		label = { Text("Email or Phone number", color = Color.Black) },
		textStyle = MaterialTheme.typography.bodySmall.copy(color = Color.Black),
		shape = RoundedCornerShape(8.dp),
		leadingIcon = {
			Icon(
				imageVector = Icons.Outlined.AccountBox,
				contentDescription = "Password Icon",
				tint = Color(
					0xFF5DB075
				)
			)
		},
		keyboardOptions = KeyboardOptions.Default.copy(imeAction = ImeAction.Next),
		colors = TextFieldDefaults.outlinedTextFieldColors(
			focusedBorderColor = Color.Black,
			unfocusedBorderColor = Color.Black
		),
		modifier = Modifier.fillMaxWidth()
	)
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PasswordTextField(password: String, onPasswordChange: (String) -> Unit) {
	OutlinedTextField(
		value = password,
		onValueChange = { onPasswordChange(it) },
		label = { Text("Password", color = Color.Black) },
		textStyle = MaterialTheme.typography.bodySmall.copy(color = Color.Black),
		visualTransformation = PasswordVisualTransformation(),
		leadingIcon = {
			Icon(
				imageVector = Icons.Outlined.Lock,
				contentDescription = "Password Icon",
				tint = Color(
					0xFF5DB075
				)
			)
		},
		shape = RoundedCornerShape(8.dp),
		keyboardOptions = KeyboardOptions.Default.copy(imeAction = ImeAction.Next),
		colors = TextFieldDefaults.outlinedTextFieldColors(
			focusedBorderColor = Color.Black,
			unfocusedBorderColor = Color.Black
		), modifier = Modifier.fillMaxWidth()
	)
}

@Composable
fun RememberMeCheckbox(
	onCheckedChange: (Boolean) -> Unit,
	onForgotPasswordClick: () -> Unit
) {
	val (checked, setChecked) = remember { mutableStateOf(false) }
	var showDialog by remember { mutableStateOf(false) }

	Row(
		horizontalArrangement = Arrangement.SpaceBetween,
		modifier = Modifier
			.fillMaxWidth()
			.padding(bottom = 16.dp)
	) {
		Row {
			Checkbox(
				checked = checked,
				onCheckedChange = {
					setChecked(it)
					onCheckedChange(it)
				},
				colors = CheckboxDefaults.colors(
					checkedColor = Color(
						0xFF5DB075
					)
				)
			)
			Text(
				text = "Remember me",
				fontSize = 14.sp,
				color = Color.Black,
				modifier = Modifier.padding(vertical = 17.dp)
			)
		}

		Text(
			text = "Forgot password?", // Use string resource
			fontSize = 14.sp,
			style = TextStyle(
				textDecoration = TextDecoration.Underline,
				color = Color.Blue
			),
			modifier = Modifier
				.clickable { onForgotPasswordClick() }
				.padding(top = 18.dp, bottom = 18.dp, end = 4.dp)
		)
	}
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ForgotPasswordDialog(
	onClose: () -> Unit,
	onPasswordChanged: (String) -> Unit,
	onDismissRequest: () -> Unit,
	onSavePassword: (String, String) -> Unit
) {
	var passwordsMatch by remember { mutableStateOf(true) }

	val newPasswordState = remember { mutableStateOf("") }
	val confirmPasswordState = remember { mutableStateOf("") }

	AlertDialog(
		onDismissRequest = onDismissRequest,
		title = { Text(text = "Forgot Password?") },
		text = {
			Column(
				modifier = Modifier
					.fillMaxWidth()
					.padding(vertical = 8.dp)
			) {
				Text(text = "Enter new password and confirm")
				OutlinedTextField(
					value = newPasswordState.value,
					onValueChange = {
						newPasswordState.value = it
						onPasswordChanged(it)
					},
					label = { Text("New Password") },
					keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password),
					modifier = Modifier.fillMaxWidth(),
					colors = TextFieldDefaults.outlinedTextFieldColors(
						focusedBorderColor = Color.Black,
						unfocusedBorderColor = Color.Black
					)
				)
				OutlinedTextField(
					value = confirmPasswordState.value,
					onValueChange = {
						confirmPasswordState.value = it
						passwordsMatch = it == newPasswordState.value
					},
					label = { Text("Confirm Password") },
					keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password),
					modifier = Modifier.fillMaxWidth(),
					colors = TextFieldDefaults.outlinedTextFieldColors(
						focusedBorderColor = Color.Black,
						unfocusedBorderColor = Color.Black
					),
					isError = !passwordsMatch,
					singleLine = true,
				)
				if (!passwordsMatch) {
					Text(
						text = "Passwords don't match",
						color = Color.Red,
						modifier = Modifier.padding(start = 8.dp)
					)
				}
			}
		},
		confirmButton = {
			Button(
				onClick = {
					if (passwordsMatch) {
						onSavePassword(newPasswordState.value, confirmPasswordState.value)
					}
				},
				colors = ButtonDefaults.buttonColors(Color(0xFF5DB075))
			) {
				Text(text = "Save Password")
			}
		},
		dismissButton = {
			TextButton(onClick = onClose) {
				Text(text = "Cancel")
			}
		}
	)
}

