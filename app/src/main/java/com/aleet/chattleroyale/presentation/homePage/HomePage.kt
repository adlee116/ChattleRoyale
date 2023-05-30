package com.aleet.chattleroyale.presentation.homePage

import androidx.appcompat.content.res.AppCompatResources
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Color.Companion.LightGray
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.aleet.chattleroyale.R
import com.aleet.chattleroyale.destinations.UserProfileScreenDestination
import com.aleet.chattleroyale.presentation.theme.AccentColor1
import com.aleet.chattleroyale.presentation.theme.AccentColor2
import com.aleet.chattleroyale.presentation.theme.PrimaryColor
import com.google.accompanist.drawablepainter.rememberDrawablePainter
import com.ramcosta.composedestinations.annotation.Destination
import com.ramcosta.composedestinations.navigation.DestinationsNavigator

@Destination
@Composable
fun HomePage(navController: DestinationsNavigator, viewModel: HomePageViewModel = hiltViewModel()) {

    Surface(color = LightGray) {
        Column(
            modifier = Modifier
                .fillMaxSize(),
            verticalArrangement = Arrangement.SpaceBetween,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Spacer(modifier = Modifier.height(50.dp))

            val drawable = AppCompatResources.getDrawable(LocalContext.current, R.drawable.baseline_person_24)
            Image(
                painter = rememberDrawablePainter(drawable = drawable),
                contentDescription = "content description",
                modifier = Modifier
                    .size(100.dp)
                    .clip(CircleShape)
                    .clickable {
                        navController.navigate(UserProfileScreenDestination())
                    }
            )

            Spacer(modifier = Modifier.height(50.dp))

            Button(
                onClick = { /* TODO: Handle Create game */ },
                colors = ButtonDefaults.buttonColors(containerColor = PrimaryColor),
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp)
            ) {
                Text(text = "Create game", color = Color.White)
            }

            Button(
                onClick = { /* TODO: Handle Join game */ },
                colors = ButtonDefaults.buttonColors(containerColor = AccentColor1),
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp)
            ) {
                Text(text = "Join game", color = Color.White)
            }

            Button(
                onClick = { /* TODO: Handle High scores */ },
                colors = ButtonDefaults.buttonColors(containerColor = AccentColor2),
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp)
            ) {
                Text(text = "High scores", color = Color.White)
            }

            Spacer(modifier = Modifier.height(50.dp))
        }
    }
}

///*
//@Composable
//fun ChatBubble(message: Message, userProfileUrl: String) {
//    val painter = rememberGlidePainter(userProfileUrl)
//
//    Row(modifier = Modifier.padding(8.dp)) {
//        Image(
//            painter = painter,
//            contentDescription = "User profile picture",
//            modifier = Modifier.size(40.dp),
//        )
//        Text(
//            text = message.content,
//            modifier = Modifier.padding(start = 8.dp)
//        )
//    }
//}
//*/
