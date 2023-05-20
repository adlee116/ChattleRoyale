package com.squadsandshots_android.presentation.signUp

import android.widget.Toast
import androidx.compose.foundation.layout.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Modifier
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalLifecycleOwner
import androidx.compose.ui.res.stringResource
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.lifecycleScope
import com.ramcosta.composedestinations.annotation.Destination
import com.ramcosta.composedestinations.navigation.DestinationsNavigator
import com.squadsandshots_android.R
import com.squadsandshots_android.presentation.destinations.HomePageDestination
import com.squadsandshots_android.presentation.login.SignUpButton
import com.squadsandshots_android.presentation.login.SquadsAndShotsTitle
import com.squadsandshots_android.presentation.login.TextFieldWithHint
import kotlinx.coroutines.flow.collect

@Destination
@Composable
fun SignUpPage(navigator: DestinationsNavigator, viewModel: SignUpViewModel = hiltViewModel()) {
    val signUpModel by rememberSaveable { mutableStateOf(SignUpModel("","","")) }
    val lifecycleOwner = LocalLifecycleOwner.current
    val context = LocalContext.current

    lifecycleOwner.lifecycleScope.launchWhenStarted {
        viewModel.events.collect {
            when(it) {
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
    Column(
        modifier = Modifier
            .fillMaxSize(),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        SquadsAndShotsTitle()
        SignUpTextFields(signUpModel = signUpModel)
        SignUpButton(onClick = { viewModel.signUp(signUpModel) })
    }
}

@Composable
fun SignUpTextFields(signUpModel: SignUpModel) {
    Column(modifier = Modifier.wrapContentSize()) {
        TextFieldWithHint(stringResource(R.string.email), false) { signUpModel.email = it }
        TextFieldWithHint(stringResource(R.string.password), true) { signUpModel.password = it }
        TextFieldWithHint(stringResource(R.string.confirm_password), true) { signUpModel.confirmPassword = it }
    }
}
