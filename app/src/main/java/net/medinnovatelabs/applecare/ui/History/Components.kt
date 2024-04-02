package net.medinnovatelabs.applecare.ui.History

import android.graphics.drawable.Icon
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Icon
import androidx.compose.material3.OutlinedCard
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.unit.dp
import com.example.compose.primaryBlack
import com.example.compose.primaryGreen
import com.example.compose.primaryWhite
import net.medinnovatelabs.applecare.R
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material3.CardDefaults
import androidx.compose.ui.Alignment
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.sp

@Composable
fun SearchTextField(){
    val textValue = remember { mutableStateOf("") }
    OutlinedTextField(
        modifier = Modifier.fillMaxWidth(),
        value = textValue.value,
        colors = OutlinedTextFieldDefaults.colors(
            focusedContainerColor = primaryWhite,
            unfocusedContainerColor = primaryWhite,
            focusedLabelColor = primaryGreen,
            focusedTextColor = primaryBlack,
            cursorColor = primaryBlack,
            focusedBorderColor = primaryBlack

        ),
        shape = RoundedCornerShape(15.dp),
        keyboardOptions = KeyboardOptions(imeAction = ImeAction.Next),
        singleLine = true,
        maxLines = 1,
        placeholder = { Text(text = stringResource(id = R.string.search)) },
        onValueChange = {

        },
        leadingIcon = {
            Icon(painter = painterResource(id = R.drawable.search_icon), contentDescription = "")
        }

    )
}
@Composable

fun SearchBar(state: MutableState<TextFieldValue>) {


    OutlinedTextField(
        modifier = Modifier.fillMaxWidth(),
        value = state.value,
        onValueChange = {value->state.value=value},
        placeholder = {
            Text("Search")
        },
        shape = RoundedCornerShape(10.dp),
        colors = OutlinedTextFieldDefaults.colors(
            focusedContainerColor = primaryWhite,
            unfocusedContainerColor = primaryWhite,
            focusedLabelColor = primaryGreen,
            focusedTextColor = primaryBlack,
            cursorColor = primaryBlack,
            focusedBorderColor = primaryBlack

        ),
        maxLines = 1,
        singleLine = true,
        leadingIcon = {
            Icon(
                painter = painterResource(id = R.drawable.search_icon),
                contentDescription = "Search Icon"
            )
        }
    )
}

@Composable
fun HistoryCards(item:String) {
    OutlinedCard(
        modifier = Modifier
            .fillMaxWidth()
            .height(150.dp),
        colors = CardDefaults.cardColors(
            containerColor = primaryWhite
        ), border = BorderStroke(1.dp, color = primaryBlack)

    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(10.dp), verticalAlignment = Alignment.CenterVertically
        ) {
            Image(
                modifier = Modifier
                    .size(150.dp)
                    .clip(RoundedCornerShape(16.dp)),
                painter = painterResource(id = R.drawable.history_img),
                contentDescription = ""
            )
            Spacer(modifier = Modifier.width(10.dp))
            Column() {
                Text(
                    text = item,
                    modifier = Modifier
                        .fillMaxWidth(),
                    style = TextStyle(
                        fontSize = 20.sp,
                        fontWeight = FontWeight.SemiBold,
                        fontStyle = FontStyle.Normal
                    ),
                    color = primaryBlack,)


                Spacer(modifier = Modifier.height(20.dp))
                Text(
                    text = "19 Mar 2024",
                    modifier = Modifier
                        .fillMaxWidth(),
                    style = TextStyle(
                        fontSize = 14.sp,
                        fontWeight = FontWeight.Normal,
                        fontStyle = FontStyle.Normal
                    ),
                    color = primaryBlack,)

                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(10.dp),
                    verticalAlignment = Alignment.Bottom,
                    horizontalArrangement = Arrangement.End
                ) {
                    Icon(
                        painter = painterResource(id = R.drawable.share_svg),
                        contentDescription = ""
                    )
                    Icon(
                        painter = painterResource(id = R.drawable.trash_svg),
                        contentDescription = ""
                    )
                }
            }

        }
    }
}