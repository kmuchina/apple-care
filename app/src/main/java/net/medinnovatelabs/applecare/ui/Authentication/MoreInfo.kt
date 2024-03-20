package net.medinnovatelabs.applecare.ui.Authentication

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.AccountBox
import androidx.compose.material.icons.outlined.Phone
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.ExposedDropdownMenuDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import net.medinnovatelabs.applecare.R

@Composable
fun MoreInfo(
	onCompleteReg: () -> Unit,
	onBack: () -> Unit
) {

	Column {
		Text(
			text = "Just one more step",
			style = TextStyle(
				fontSize = 15.sp,
				lineHeight = 24.sp,
				fontFamily = FontFamily(Font(R.font.inter)),
				fontWeight = FontWeight(600),
				color = Color(0xFF000000),
				textAlign = TextAlign.Center,
			), modifier = Modifier
				.padding(top = 58.dp, start = 110.dp)
		)
		Spacer(modifier = Modifier.height(21.dp))
		IdInputScreen()
		Spacer(modifier = Modifier.height(21.dp))
		PhoneInputScreen()
		Spacer(modifier = Modifier.height(23.dp))
		LocationDetails()

		Spacer(modifier = Modifier.height(48.dp))

		Column(
			modifier = Modifier.fillMaxWidth(),
			horizontalAlignment = Alignment.CenterHorizontally,
			verticalArrangement = Arrangement.Center
		) {
			Button(
				onClick = { onCompleteReg() },
				colors = ButtonDefaults.buttonColors(
					containerColor = Color(0xFF5DB075)
				),
				modifier = Modifier
					.fillMaxWidth(0.9f)
					.height(42.dp)
					.background(
						color = Color(0xFF5DB075),
						shape = RoundedCornerShape(size = 5.dp)
					),
			) {
				Text(
					text = "Complete Registration",
					color = Color.White,
					fontSize = 20.sp
				)
			}

			Spacer(modifier = Modifier.height(16.dp))

			TextButton(onClick = { onBack() }) {
				Text(
					text = "Back",
					color = Color(0xFF000000),
					textAlign = TextAlign.Center
				)

			}
		}

	}


}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun IdInputBox(id: String, onIdChange: (String) -> Unit) {
	OutlinedTextField(
		value = id,
		onValueChange = { onIdChange(it) },
		label = { Text("ID Number", color = Color.Black) },
		textStyle = MaterialTheme.typography.bodySmall.copy(color = Color.Black),
		shape = RoundedCornerShape(8.dp),
		leadingIcon = {
			Icon(
				imageVector = Icons.Outlined.AccountBox,
				contentDescription = "Id Icon",
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
		modifier = Modifier.fillMaxWidth(0.9f)
	)
}

@Composable
fun IdInputScreen() {
	var id by remember { mutableStateOf("") }

	Column(
		modifier = Modifier
			.fillMaxWidth(),
//            .padding(16.dp),
		verticalArrangement = Arrangement.Center,
		horizontalAlignment = Alignment.CenterHorizontally
	) {
		IdInputBox(id = id, onIdChange = { id = it })
	}
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PhoneInputBox(phone: String, onPhoneChange: (String) -> Unit) {
	OutlinedTextField(
		value = phone,
		onValueChange = { onPhoneChange(it) },
		label = { Text("Phone Number", color = Color.Black) },
		textStyle = MaterialTheme.typography.bodySmall.copy(color = Color.Black),
		shape = RoundedCornerShape(8.dp),
		leadingIcon = {
			Icon(
				imageVector = Icons.Outlined.Phone, contentDescription = "Phone Icon", tint = Color(
					0xFF5DB075
				)
			)
		},
		keyboardOptions = KeyboardOptions.Default.copy(imeAction = ImeAction.Next),
		colors = TextFieldDefaults.outlinedTextFieldColors(
			focusedBorderColor = Color.Black,
			unfocusedBorderColor = Color.Black
		), modifier = Modifier.fillMaxWidth(0.9f)
	)
}

@Composable
fun PhoneInputScreen() {
	var phone by remember { mutableStateOf("") }

	Column(
		modifier = Modifier
			.fillMaxWidth(),
//            .padding(16.dp),
		verticalArrangement = Arrangement.Center,
		horizontalAlignment = Alignment.CenterHorizontally
	) {
		PhoneInputBox(phone = phone, onPhoneChange = { phone = it })
	}
}


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun LocationDetails() {
	// State variables for expanded dropdowns and selected values
	var countyExpanded by remember { mutableStateOf(false) }
	var constituencyExpanded by remember { mutableStateOf(false) }
	var wardExpanded by remember { mutableStateOf(false) }
	var selectedCounty by remember { mutableStateOf("") }
	var selectedConstituency by remember { mutableStateOf("") }
	var selectedWard by remember { mutableStateOf("") }

	// counties
	val counties = listOf(
		"Baringo",
		"Bomet",
		"Bungoma",
		"Busia",
		"Elgeyo Marakwet",
		"Embu",
		"Garissa",
		"Homa Bay",
		"Isiolo",
		"Kajiado",
		"Kakamega",
		"Kericho",
		"Kiambu",
		"Kilifi",
		"Kirinyaga",
		"Kisii",
		"Kisumu",
		"Kitui",
		"Kwale",
		"Laikipia",
		"Lamu",
		"Machakos",
		"Makueni",
		"Mandera",
		"Marsabit",
		"Meru",
		"Migori",
		"Mombasa",
		"Murang'a",
		"Nairobi",
		"Nakuru",
		"Nandi",
		"Narok",
		"Nyamira",
		"Nyandarua",
		"Nyeri",
		"Samburu",
		"Siaya",
		"Taita Taveta",
		"Tana River",
		"Tharaka Nithi",
		"Trans Nzoia",
		"Turkana",
		"Uasin Gishu",
		"Vihiga",
		"Wajir",
		"West Pokot"
	)
	val constituenciesByCounty = mapOf(
		"Nairobi" to listOf("Langata", "Kibra", "Dagoretti North" /* ... */),
		"Mombasa" to listOf("Nyali", "Changamwe", "Jomvu" /* ... */),
		/* ... */
	)
	val wardsByConstituency = mapOf(
		"Langata" to listOf("Karen", "South C", "Mugumoini" /* ... */),
		"Kibra" to listOf("Laini Saba", "Lindi", "Sarangombe" /* ... */),
		/* ... */
	)

	Text(
		text = "Where is your farm",
		style = TextStyle(
			fontSize = 15.sp,
			lineHeight = 24.sp,
			fontFamily = FontFamily(Font(R.font.inter)),
			fontWeight = FontWeight(600),
			color = Color(0xFF000000),
			textAlign = TextAlign.Start,
		),
		modifier = Modifier.padding(start = 24.dp, bottom = 4.dp)
	)

	Box(
		modifier = Modifier.fillMaxWidth(),
		contentAlignment = Alignment.Center
	) {

		Column {
			// County dropdown
			ExposedDropdownMenuBox(
				expanded = countyExpanded,
				onExpandedChange = { countyExpanded = it }) {
				TextField(
					value = selectedCounty,
					onValueChange = { },
					placeholder = { Text("Select your county") },
					readOnly = true,
					trailingIcon = {
						ExposedDropdownMenuDefaults.TrailingIcon(expanded = countyExpanded)
					},
					colors = ExposedDropdownMenuDefaults.textFieldColors(
						unfocusedContainerColor = Color.White,
						unfocusedIndicatorColor = Color.Unspecified,
						focusedContainerColor = Color.White,
						focusedIndicatorColor = Color.Unspecified
					),
					modifier = Modifier
						.menuAnchor()
						.fillMaxWidth(0.9f)
						.border(color = Color.Black, width = 1.dp, shape = RoundedCornerShape(8.dp))
				)
				ExposedDropdownMenu(
					expanded = countyExpanded,
					onDismissRequest = { countyExpanded = false }) {
					counties.forEach { county ->
						DropdownMenuItem(
							text = { Text(county) },
							onClick = {
								selectedCounty = county
								constituencyExpanded = false
								wardExpanded = false
								selectedConstituency = ""
								selectedWard = ""
								countyExpanded = false
							}
						)
					}
				}
			}

			Spacer(modifier = Modifier.height(30.dp))

			// Constituency dropdown (populate based on selected county)
			ExposedDropdownMenuBox(
				expanded = constituencyExpanded,
				onExpandedChange = { constituencyExpanded = it }) {
				TextField(
					value = selectedConstituency,
					onValueChange = { },
					placeholder = { Text("Select your constituency") },
					readOnly = true,
					trailingIcon = {
						ExposedDropdownMenuDefaults.TrailingIcon(expanded = constituencyExpanded)
					},
					colors = ExposedDropdownMenuDefaults.textFieldColors(
						unfocusedContainerColor = Color.White,
						unfocusedIndicatorColor = Color.Unspecified,
						focusedContainerColor = Color.White,
						focusedIndicatorColor = Color.Unspecified
					),
					modifier = Modifier
						.menuAnchor()
						.fillMaxWidth(0.9f)
						.border(color = Color.Black, width = 1.dp, shape = RoundedCornerShape(8.dp))
				)
				ExposedDropdownMenu(
					expanded = constituencyExpanded,
					onDismissRequest = { constituencyExpanded = false }) {
					constituenciesByCounty[selectedCounty]?.let { constituencies ->
						constituencies.forEach { constituency ->
							DropdownMenuItem(
								text = { Text(constituency) },
								onClick = {
									selectedConstituency = constituency
									wardExpanded = false
									selectedWard = ""
									constituencyExpanded = false
								}
							)
						}
					}
				}
			}

			Spacer(modifier = Modifier.height(30.dp))

			// Ward dropdown (populate based on selected constituency)
			ExposedDropdownMenuBox(
				expanded = wardExpanded,
				onExpandedChange = { wardExpanded = it }) {
				TextField(
					value = selectedWard,
					onValueChange = { },
					placeholder = { Text("Select your ward") },
					readOnly = true,
					trailingIcon = {
						ExposedDropdownMenuDefaults.TrailingIcon(expanded = wardExpanded)
					},
					colors = ExposedDropdownMenuDefaults.textFieldColors(
						unfocusedContainerColor = Color.White,
						unfocusedIndicatorColor = Color.Unspecified,
						focusedContainerColor = Color.White,
						focusedIndicatorColor = Color.Unspecified
					),
					modifier = Modifier
						.menuAnchor()
						.fillMaxWidth(0.9f)
						.border(color = Color.Black, width = 1.dp, shape = RoundedCornerShape(8.dp))
				)
				ExposedDropdownMenu(
					expanded = wardExpanded,
					onDismissRequest = { wardExpanded = false }) {
					wardsByConstituency[selectedConstituency]?.let { wards ->
						wards.forEach { ward ->
							DropdownMenuItem(
								text = { Text(ward) },
								onClick = {
									selectedWard = ward
									wardExpanded = false
								}
							)
						}
					}
				}
			}
		}
	}
}

@Preview(showSystemUi = true, showBackground = true)
@Composable
fun MoreInfoPreview() {
	MoreInfo(
		onBack = { },
		onCompleteReg = { }
	)
}
