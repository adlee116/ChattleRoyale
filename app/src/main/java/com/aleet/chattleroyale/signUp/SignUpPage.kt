package com.aleet.chattleroyale.signUp

import android.widget.Toast
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalLifecycleOwner
import androidx.compose.ui.res.stringResource
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.repeatOnLifecycle
import com.aleet.chattleroyale.destinations.HomePageDestination
import com.aleet.chattleroyale.presentation.login.ChattleRoyaleImage
import com.aleet.chattleroyale.presentation.login.SignUpButton
import com.aleet.chattleroyale.presentation.login.TextFieldWithHint
import com.ramcosta.composedestinations.annotation.Destination
import com.ramcosta.composedestinations.navigation.DestinationsNavigator
import kotlinx.coroutines.launch

@Destination
@Composable
fun SignUpPage(navigator: DestinationsNavigator, viewModel: SignUpViewModel = hiltViewModel()) {
    val signUpModel by rememberSaveable { mutableStateOf(SignUpModel("", "", "")) }
    val lifecycleOwner = LocalLifecycleOwner.current
    val context = LocalContext.current
    val coroutineScope = rememberCoroutineScope()

    LaunchedEffect(key1 = lifecycleOwner) {
        coroutineScope.launch {
            lifecycleOwner.lifecycle.repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.events.collect {
                    when (it) {
                        is SignUpViewModel.SignUpViewEvent.SignUpSuccess -> {
                            Toast.makeText(context, "Sign up successful", Toast.LENGTH_SHORT).show()
                            navigator.navigate(HomePageDestination())
                        }

                        is SignUpViewModel.SignUpViewEvent.SignUpFailed -> {
                            Toast.makeText(context, it.message, Toast.LENGTH_SHORT).show()
                        }

                        null -> {}
                    }
                }
            }
        }
    }
    Column(
        modifier = Modifier
            .fillMaxSize(),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        ChattleRoyaleImage()
        SignUpTextFields(signUpModel = signUpModel)
        SignUpButton(onClick = { viewModel.signUp(signUpModel) })
    }
}

@Composable
fun SignUpTextFields(signUpModel: SignUpModel) {
    Column(modifier = Modifier.wrapContentSize()) {
        TextFieldWithHint(stringResource(com.aleet.chattleroyale.R.string.email), false) { signUpModel.email = it }
        TextFieldWithHint(stringResource(com.aleet.chattleroyale.R.string.password), true) { signUpModel.password = it }
        TextFieldWithHint(stringResource(com.aleet.chattleroyale.R.string.confirmPassword), true) { signUpModel.confirmPassword = it }
    }
}
