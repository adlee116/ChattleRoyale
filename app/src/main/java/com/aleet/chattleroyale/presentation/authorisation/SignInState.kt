package com.aleet.chattleroyale.presentation.authorisation

data class SignInState(
    val isSignInSuccessful: Boolean = false,
    val signInError: String? = null
)