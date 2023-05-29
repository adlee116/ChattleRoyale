package com.aleet.chattleroyale.presentation.login

import android.app.Activity.RESULT_OK
import android.content.Intent
import android.widget.Toast
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.Image
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Arrangement.Absolute.SpaceEvenly
import androidx.compose.foundation.layout.BoxWithConstraints
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
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Color.Companion.Blue
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalLifecycleOwner
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.lifecycleScope
import com.aleet.chattleroyale.R
import com.aleet.chattleroyale.destinations.HomePageDestination
import com.aleet.chattleroyale.destinations.SignUpPageDestination
import com.aleet.chattleroyale.presentation.authorisation.SignInState
import com.aleet.chattleroyale.presentation.theme.PrimaryColor
import com.aleet.chattleroyale.requestModels.LoginRequest
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.ramcosta.composedestinations.annotation.Destination
import com.ramcosta.composedestinations.annotation.RootNavGraph
import com.ramcosta.composedestinations.navigation.DestinationsNavigator
import kotlinx.coroutines.launch

@RootNavGraph(start = true) // sets this as the start destination of the default nav graph
@Destination
@Composable
fun LoginPage(navController: DestinationsNavigator, viewModel: LoginViewModel = hiltViewModel()) {
    val loginRequest by rememberSaveable { mutableStateOf(LoginRequest()) }
    val lifecycleScope = rememberCoroutineScope()
    val context = LocalContext.current
    val state by viewModel.state.collectAsStateWithLifecycle()
    viewModel.checkIfUserAlreadyAuthorised()

    val launcher = rememberLauncherForActivityResult(contract = ActivityResultContracts.StartActivityForResult()) {result ->
        if(result.resultCode == RESULT_OK) {
            val task = GoogleSignIn.getSignedInAccountFromIntent(result.data)
            viewModel.handleSignInResult(task)
        }
    }

    LaunchedEffect(key1 = Unit) {
        val gso: GoogleSignInOptions = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
            .requestIdToken(context.getString(R.string.webClientId))
            .requestEmail()
            .build()
        val googleSignInClient = GoogleSignIn.getClient(context, gso)
        viewModel.setGoogleSignInClient(googleSignInClient)
    }

    SetupViewModelListener(viewModel = viewModel, navController = navController)
    Column(
        modifier = Modifier.fillMaxSize(),
        verticalArrangement = Arrangement.SpaceBetween
    ) {
        Spacer(modifier = Modifier.height(10.dp))
        ChattleRoyaleImage() // Moved to the top of the Column
        Spacer(modifier = Modifier.weight(0.5f)) // Consumes extra space
        LoginCenterContent(loginRequest = loginRequest)
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp, vertical = 16.dp),
            horizontalArrangement = SpaceEvenly
        ) {
            LoginButton(onClick = {
                viewModel.process(LoginViewModel.LoginViewEvent.LoginClicked(loginRequest))
            }, loginRequest)
            SignUpButton(onClick = {
                navController.navigate(SignUpPageDestination())
            })
        }
        Spacer(modifier = Modifier.weight(2f)) // Consumes extra space and provides more weight than other Spacers, making more space at the bottom.
        // Empty column for other means of login
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 16.dp)
        ) {
            SignInScreen(state = state, onSignInClick = {
                lifecycleScope.launch {
                   val signInIntent: Intent? = viewModel.googleSignInClient.value?.signInIntent
                    launcher.launch(signInIntent)
                }
            })
        }
    }
}

@Composable
fun SetupViewModelListener(viewModel: LoginViewModel, navController: DestinationsNavigator) {
    val lifecycleOwner = LocalLifecycleOwner.current
    val context = LocalContext.current

//    viewModel.checkIfUserAlreadyAuthorised()
    lifecycleOwner.lifecycleScope.launchWhenStarted {
        viewModel.events.collect {
            when (it) {
                is LoginViewModel.LoginReaction.InvalidCredentials -> { Toast.makeText(context, it.message, Toast.LENGTH_SHORT).show() }

                is LoginViewModel.LoginReaction.FailedLogin -> { Toast.makeText(context, it.message, Toast.LENGTH_SHORT).show() }

                is LoginViewModel.LoginReaction.FailedUserPersist -> Toast.makeText(context, it.message, Toast.LENGTH_LONG).show()

                LoginViewModel.LoginReaction.SuccessfulLogin -> { navController.navigate(HomePageDestination()) }

                null -> {}
            }

        }
    }
}

@Composable
fun LoginCenterContent(loginRequest: LoginRequest) {
    Column(
        modifier = Modifier
            .wrapContentSize(),
        verticalArrangement = Arrangement.Center,
    ) {
        UsernameAndPasswordFields(loginRequest)
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
                color = PrimaryColor,
                shape = RoundedCornerShape(8.dp)
            ),
        colors = TextFieldDefaults.textFieldColors(
            containerColor = Color.White
        )
    )
}

@Composable
fun ChattleRoyaleImage() {
    Image(
        painter = painterResource(R.drawable.chattle),
        contentDescription = "My SVG Image"
    )
}

@Composable
fun SignInScreen(
    state: SignInState,
    onSignInClick: () -> Unit
) {
    val context = LocalContext.current
    LaunchedEffect(key1 = state.signInError) {
        state.signInError?.let { error ->
            Toast.makeText(context, error, Toast.LENGTH_LONG).show()
        }
    }

    BoxWithConstraints(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        contentAlignment = Alignment.Center
    ) {
        GoogleButton(
            text = "Login with Google",
            loadingText = "Logging in...",
            onClicked = onSignInClick
        )
    }
}
