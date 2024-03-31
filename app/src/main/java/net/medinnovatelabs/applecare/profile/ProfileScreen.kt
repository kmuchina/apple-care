package net.medinnovatelabs.applecare.profile

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import coil.compose.AsyncImage
import com.airbnb.lottie.compose.LottieAnimation
import com.airbnb.lottie.compose.LottieCompositionSpec
import com.airbnb.lottie.compose.animateLottieCompositionAsState
import com.airbnb.lottie.compose.rememberLottieComposition
import net.medinnovatelabs.applecare.R
import net.medinnovatelabs.applecare.firebasedata.UserData

@Composable
fun ProfileScreen(
    userData: UserData?,
    navController: NavHostController,
    onSignOut: () -> Unit
){
    Column(
        modifier = Modifier.fillMaxSize() .background(color = Color(0xFF0D6446)),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {

        val composition by rememberLottieComposition(spec = LottieCompositionSpec.RawRes(R.raw.parrot))
        var isPlaying by remember {
            mutableStateOf(true)
        }
        val progress by animateLottieCompositionAsState(
            composition = composition,
            isPlaying = isPlaying
        )
        LaunchedEffect(key1 = progress) {
            if (progress == 0f){
                isPlaying = true
            }
            if (progress == 1f){
                isPlaying = false
            }
        }

        Box (
            modifier = Modifier
                .align(Alignment.CenterHorizontally)
            //.background(color = Color(0xFF0D6446))
        ) {
            LottieAnimation(
                modifier = Modifier
                    .size(340.dp)
                    .offset(y = 30.dp),
                composition = composition,
                alignment = Alignment.Center,
                iterations = 100
            )
        }
        if(userData?.profilePictureUrl != null){
            AsyncImage(model = userData.profilePictureUrl,
                contentDescription = "Profile picture",
                modifier = Modifier
                    .size(150.dp)
                    .clip(CircleShape),
                contentScale = ContentScale.Crop
            )
            Spacer(modifier = Modifier.height(16.dp))
        }
        if (userData?.username != null) {
            Text(
                text = userData.username,
                textAlign = TextAlign.Center,
                fontSize = 36.sp,
                fontWeight = FontWeight.SemiBold
            )
            Spacer(modifier = Modifier.height(16.dp))
        }
        Row {
            OutlinedButton(
                onClick = { navController.navigate("SignUp") },
                shape = RoundedCornerShape(16.dp),
                colors = ButtonDefaults.buttonColors(
                    containerColor = Color(0xFF5DB075),
                    contentColor = Color.White   // Change content (text) color to white for better contrast
                )
            ) {
                Text("Sign out", color = Color.White)
            }
            Spacer(modifier = Modifier.width(12.dp))
            OutlinedButton(
                onClick = {  navController.navigate("dashboard")},
                shape = RoundedCornerShape(16.dp),
                colors = ButtonDefaults.buttonColors(
                    containerColor = Color(0xFF2F6940),
                    contentColor = Color.White   // Change content (text) color to white for better contrast
                )
            ) {
                Text("Continue", color = Color.White)
            }
        }
    }

}