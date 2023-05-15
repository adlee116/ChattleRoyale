package com.aleet.chattleroyale.presentation.login

import androidx.lifecycle.ViewModel


class LoginViewModel (
//    private val loginUseCase: LoginUseCase,
//    private val getCurrentFireBaseUser: GetCurrentFireBaseUserUseCase
) : ViewModel() {

//    private val _events: MutableStateFlow<LoginViewEvent?> = MutableStateFlow(null)
//    val events: StateFlow<LoginViewEvent?> get() = _events.asStateFlow()
//
//    fun checkIfUserAlreadyAuthorised() {
//        getCurrentFireBaseUser.invoke(viewModelScope) { result ->
//            result.result(
//                onSuccess = {
//                    it?.let {
//                        saveUser(it)
//                        _events.value = LoginViewEvent.LoginSuccess
//                    }
//                },
//                onFailure = {
//                    _events.value = LoginViewEvent.CredentialsInvalid(
//                        "Unable to use previous credentials, please log in or sign up"
//                    )
//                }
//            )
//        }
//    }
//
//    fun login(loginRequest: LoginRequest) {
//        loginUseCase.invoke(viewModelScope, loginRequest) { result ->
//            result.result(
//                onSuccess = {
//                    it.addOnSuccessListener { authResult ->
//                        //storeit user
//                        _events.value = LoginViewEvent.LoginSuccess
//                    }
//                    it.addOnFailureListener { authResult ->
//                        _events.value = LoginViewEvent.LoginFailed(authResult.message ?: "Login Failed")
//                    }
//                },
//                onFailure = {
//                    _events.value = LoginViewEvent.LoginFailed(it.message ?: "Login failed")
//                }
//            )
//        }
//    }
//
//    private fun emailValid(email: String): Boolean {
//        return if (TextUtils.isEmpty(email)) {
//            false
//        } else {
//            android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches();
//        }
//    }
//
//    private fun passwordValid(password: String): Boolean {
//        val pattern: Pattern
//        val passwordPattern = "^(?=.*[0-9])(?=.*[a-z])(?=.*[A-Z])(?=.*[!@#$%^&+=])(?=\\S+$).{4,}$"
//        pattern = Pattern.compile(passwordPattern)
//        val matcher: Matcher = pattern.matcher(password)
//        return matcher.matches()
//    }
//
//    fun validateFieldsAndLogin(loginRequest: LoginRequest) {
//        when {
//            !emailValid(loginRequest.username) -> _events.value =
//                LoginViewEvent.CredentialsInvalid("Email invalid")
//            !passwordValid(loginRequest.password) -> _events.value =
//                LoginViewEvent.CredentialsInvalid("Password invalid")
//            else -> {
//                login(loginRequest)
//            }
//        }
//        viewModelScope.launch { resetStateAfterDelay() }
//    }
//
//    // Resetting state so we can send the same state again
//    private suspend fun resetStateAfterDelay() {
//        delay(500)
//        _events.value = null
//    }
//
//    private fun saveUser(user: FirebaseUser) {
//        // TODO save the user to database?
//    }
//
//    sealed class LoginViewEvent {
//        object LoginSuccess : LoginViewEvent()
//        data class LoginFailed(val message: String) : LoginViewEvent()
//        data class CredentialsInvalid(val message: String) : LoginViewEvent()
//    }
}