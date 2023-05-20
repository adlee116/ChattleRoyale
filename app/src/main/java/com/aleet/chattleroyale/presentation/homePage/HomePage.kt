package com.squadsandshots_android.presentation.homePage

import android.widget.Toast
import androidx.compose.foundation.Image
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Button
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.capitalize
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.viewmodel.compose.viewModel
import coil.annotation.ExperimentalCoilApi
import coil.compose.rememberImagePainter
import com.ramcosta.composedestinations.annotation.Destination
import com.ramcosta.composedestinations.navigation.DestinationsNavigator
import com.squadsandshots_android.presentation.destinations.HomePageDestination
import com.squadsandshots_android.presentation.destinations.MainGameRoomDestination
import com.squadsandshots_android.presentation.destinations.PreGamePageDestination
import com.squadsandshots_android.presentation.login.TextFieldWithHint
import com.squadsandshots_android.presentation.models.CreateGameStart
import com.squadsandshots_android.presentation.models.GameType
import com.squadsandshots_android.presentation.models.StartGameDetails
import java.util.*

@ExperimentalCoilApi
@Destination
@Composable
fun HomePage(navigator: DestinationsNavigator, viewModel: HomePageViewModel = hiltViewModel()) {
    val context = LocalContext.current
    Box() {
        var yourName by rememberSaveable { mutableStateOf("") }
        var dialogState by rememberSaveable { mutableStateOf(false) }
        GameSelection(dialogState = dialogState, onDismissRequest = {
            dialogState = !it
        }, navigator = navigator, viewModel = viewModel)
        Column(
            modifier = Modifier
                .fillMaxSize(),
            verticalArrangement = Arrangement.Center
        ) {
            TextFieldWithHint(
                labelText = "Your name",
                password = false,
                onValueChanged = {
                    yourName = it
                    viewModel.playerName = it
                })
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceEvenly
            ) {
                HomePageButton("Create game") {
                    if (yourName.isEmpty()) {
                        Toast.makeText(context, "Please add your name", Toast.LENGTH_SHORT).show()
                    } else {
                        dialogState = true
                    }
                }
                HomePageButton("Join game") {}
            }
        }
    }
}

@Composable
fun HomePageButton(buttonText: String, onClick: () -> Unit) {
    Button(onClick = onClick) {
        Text(buttonText, textAlign = TextAlign.Center)
    }
}

@ExperimentalCoilApi
@Composable
fun GameSelection(
    dialogState: Boolean,
    navigator: DestinationsNavigator,
    viewModel: HomePageViewModel,
    onDismissRequest: (dialogState: Boolean) -> Unit
) {
    if (dialogState) {
        Dialog(
            onDismissRequest = { onDismissRequest(dialogState) },
            DialogProperties(dismissOnBackPress = true, dismissOnClickOutside = true)
        ) {
            Surface(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(300.dp),
                shape = RoundedCornerShape(8.dp)
            ) {
                Column(
                    modifier = Modifier.fillMaxSize(),
                    verticalArrangement = Arrangement.SpaceEvenly
                ) {
                    ImageAndText(
                        imageUrl = "https://droidjournal.com/wp-content/uploads/2020/04/COD.jpg",
                        text = GameType.WARZONE.gameName
                    ) {
                        navigator.navigate(PreGamePageDestination(
                            viewModel.createStartGameDetails(GameType.WARZONE)
                        ))
                    }
                    ImageAndText(
                        imageUrl = "https://img.redbull.com/images/c_crop,x_71,y_0,h_1080,w_1620/c_fill,w_1500,h_1000/q_auto,f_auto/redbullcom/2019/02/12/0394f2ac-e96d-4b24-8268-a7346fcbd206/apex-legends",
                        text = GameType.APEX.gameName
                    ) {
                        navigator.navigate(PreGamePageDestination(
                            viewModel.createStartGameDetails(GameType.APEX)
                        ))
                    }
                    ImageAndText(
                        imageUrl = "https://cdn2.unrealengine.com/14br-consoles-1920x1080-wlogo-1920x1080-432974386.jpg",
                        text = GameType.FORTNITE.gameName
                    ) {
                        navigator.navigate(PreGamePageDestination(
                            viewModel.createStartGameDetails(GameType.FORTNITE)
                        ))
                    }
                }
            }
        }
    }
}

@ExperimentalCoilApi
@Composable
fun ImageAndText(imageUrl: String, text: String, clickGame: (String) -> Unit) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clickable { clickGame(text) }
            .border(
                width = 2.dp,
                brush = Brush.horizontalGradient(
                    listOf(
                        Color.Red,
                        Color.Blue
                    )
                ),
                shape = RoundedCornerShape(0.dp)
            ),
        horizontalArrangement = Arrangement.SpaceEvenly
    ) {
        Image(
            painter = rememberImagePainter(imageUrl),
            contentDescription = null,
            modifier = Modifier.size(100.dp)
        )
        Text(text = text, modifier = Modifier.align(Alignment.CenterVertically))
    }
}
