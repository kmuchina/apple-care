
package net.medinnovatelabs.applecare.ui.History



import androidx.compose.foundation.background

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn


import androidx.compose.foundation.lazy.items




import androidx.compose.material3.Surface


import androidx.compose.runtime.Composable

import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember

import androidx.compose.ui.Modifier

import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.tooling.preview.Preview


import androidx.compose.ui.unit.dp

import com.example.compose.primaryGray



@Composable
fun HistoryScreen() {


    Surface(
        modifier = Modifier
            .fillMaxSize()
            .background(primaryGray)
            .padding(28.dp)

    ) {
        val list = listOf(
            "Apple scab",
            "Powdery mildew",
            "Fire blight",
            "Cedar apple rust",
            "Apple mosaic virus",
            "Apple canker",
            "Apple tree anthracnose",
            "Black rot",
            "Fruit rots",
            "Blossom end rot"
        )
        History(list = list)
    }
}
@Composable
fun History(list: List<String>) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(primaryGray)
    ) {
        val textState = remember {
            mutableStateOf(TextFieldValue(""))
        }
        SearchBar(state = textState)
        val searchedText = textState.value.text
        Spacer(modifier = Modifier.height(20.dp))
        LazyColumn() {
            items(items = list.filter {
                it.contains(searchedText, ignoreCase = true)
            }, key = { it }) { item ->
                HistoryCards(item = item)
                Spacer(modifier = Modifier.height(15.dp))
            }

        }

    }
}
@Preview
@Composable
fun HistoryPreview(){
    HistoryScreen()
}