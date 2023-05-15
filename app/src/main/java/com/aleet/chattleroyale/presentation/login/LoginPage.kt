package com.aleet.chattleroyale.presentation.login

import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Arrangement.Absolute.SpaceEvenly
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.ClickableText
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.drawWithCache
import androidx.compose.ui.graphics.BlendMode
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Color.Companion.Blue
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import com.aleet.chattleroyale.R
import com.aleet.chattleroyale.requestModels.LoginRequest
import com.ramcosta.composedestinations.annotation.Destination
import com.ramcosta.composedestinations.navigation.DestinationsNavigator

@Destination(start = true)
@Composable
fun LoginPage(navController: DestinationsNavigator, viewModel: LoginViewModel = hiltViewModel()) {
    val loginRequest by rememberSaveable { mutableStateOf(LoginRequest()) }
//    SetupViewModelListener(viewModel = viewModel, navController = navController)
    Column(
        modifier = Modifier
            .fillMaxSize(),
        verticalArrangement = Arrangement.SpaceBetween
    ) {
        Spacer(modifier = Modifier.height(10.dp))
        LoginCenterContent(
            loginRequest = loginRequest,
            viewModel = viewModel,
            navigator = navController
        )
//        ResetPasswordButton(modifier = Modifier.fillMaxWidth().padding(vertical = 16.dp)) {
//            navController.navigate(ResetPassWordPageDestination())
//        }
    }
}

//@Composable
//fun SetupViewModelListener(viewModel: LoginViewModel, navController: DestinationsNavigator) {
//    val lifecycleOwner = LocalLifecycleOwner.current
//    val context = LocalContext.current
//
//    viewModel.checkIfUserAlreadyAuthorised()
//    lifecycleOwner.lifecycleScope.launchWhenStarted {
//        viewModel.events.collect {
//            when (it) {
//                is LoginViewModel.LoginViewEvent.CredentialsInvalid -> {
//                    Toast.makeText(context, it.message, Toast.LENGTH_SHORT).show()
//                }
//                is LoginViewModel.LoginViewEvent.LoginFailed -> {
//                    Toast.makeText(context, it.message, Toast.LENGTH_SHORT).show()
//                }
//                LoginViewModel.LoginViewEvent.LoginSuccess -> {
//                    navController.navigate(HomePageDestination())
//                }
//                null -> {}
//            }
//
//        }
//    }
//}
//
@Composable
fun LoginCenterContent(
    loginRequest: LoginRequest,
    viewModel: LoginViewModel,
    navigator: DestinationsNavigator
) {
    Column(
        modifier = Modifier
            .wrapContentSize(),
        verticalArrangement = Arrangement.Center,
    ) {
        SquadsAndShotsTitle()
        UsernameAndPasswordFields(loginRequest)
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp, vertical = 16.dp),
            horizontalArrangement = SpaceEvenly
        ) {
            LoginButton(onClick = {
//                viewModel.validateFieldsAndLogin(it)
            }, loginRequest)
            SignUpButton(onClick = {
//                navigator.navigate(SignUpPageDestination())
            })
        }

    }
}

@Composable
fun UsernameAndPasswordFields(loginRequest: LoginRequest) {
    Column(modifier = Modifier.wrapContentSize()) {
        TextFieldWithHint(stringResource(R.string.email), false) { loginRequest.username = it }
        TextFieldWithHint(stringResource(R.string.password), true) { loginRequest.password = it }
    }
}

@Composable
fun LoginButton(onClick: (LoginRequest) -> Unit, loginRequest: LoginRequest) {
    ClickableText(
        text = AnnotatedString("Login"),
        onClick = { onClick(loginRequest) },
        style = TextStyle(
            color = Blue,
            fontSize = 22.sp,
            fontFamily = FontFamily.SansSerif,
            textAlign = TextAlign.Center
        )
    )
}

@Composable
fun SignUpButton(onClick: () -> Unit) {
    ClickableText(
        text = AnnotatedString("Sign up"),
        onClick = { onClick() },
        style = TextStyle(
            color = Blue,
            fontSize = 22.sp,
            fontFamily = FontFamily.SansSerif,
            textAlign = TextAlign.Center
        )
    )
}

@Composable
fun SquadsAndShotsTitle() {
    Text(
        text = "Squads&Shots",
        fontSize = 42.sp,
        fontWeight = FontWeight.Bold,
        modifier = Modifier
            .graphicsLayer(alpha = 0.99f)
            .drawWithCache {
                val brush = Brush.horizontalGradient(listOf(Color.Red, Blue))
                onDrawWithContent {
                    drawContent()
                    drawRect(brush, blendMode = BlendMode.SrcAtop)
                }
            }
            .fillMaxWidth()
            .padding(vertical = 16.dp),
        textAlign = TextAlign.Center
    )
}
//
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TextFieldWithHint(labelText: String, password: Boolean, onValueChanged: (String) -> Unit) {
    var stringValue by rememberSaveable { mutableStateOf("") }
    TextField(
        value = stringValue,
        onValueChange = {
            stringValue = it
            onValueChanged(it)
        },
        maxLines = 1,
        label = { Text(labelText) },
        visualTransformation = if (password) PasswordVisualTransformation() else VisualTransformation.None,
        keyboardOptions = KeyboardOptions(
            keyboardType = if (password) KeyboardType.Password else KeyboardType.Email
        ),
        modifier = Modifier
            .padding(vertical = 8.dp, horizontal = 16.dp)
            .fillMaxWidth()
            .border(
                width = 3.dp,
                brush = Brush.horizontalGradient(
                    listOf(
                        Color.Red,
                        Blue
                    )
                ),
                shape = RoundedCornerShape(8.dp)
            ),
        colors = TextFieldDefaults.textFieldColors(
            containerColor = Color.White
        )
    )
}

