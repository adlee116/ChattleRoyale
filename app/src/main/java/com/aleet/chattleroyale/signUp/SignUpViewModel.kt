package com.aleet.chattleroyale.signUp

import android.text.TextUtils
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.aleet.chattleroyale.models.User
import com.aleet.chattleroyale.requestModels.LoginRequest
import com.aleet.chattleroyale.useCases.remote.auth.CreateFireBaseUserUseCase
import com.aleet.chattleroyale.useCases.remote.user.CreateInitialUserUseCase
import com.google.firebase.auth.AuthResult
import com.google.firebase.firestore.FieldValue
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import java.util.regex.Matcher
import java.util.regex.Pattern
import javax.inject.Inject

@HiltViewModel
class SignUpViewModel @Inject constructor(
    private val createFireBaseUserUseCase: CreateFireBaseUserUseCase,
    private val createInitialUserUseCase: CreateInitialUserUseCase
) : ViewModel() {

    private val _events: MutableStateFlow<SignUpViewEvent?> = MutableStateFlow(null)
    val events: StateFlow<SignUpViewEvent?> get() = _events.asStateFlow()

    private fun createAccount(loginRequest: LoginRequest) {
        createFireBaseUserUseCase.invoke(viewModelScope, loginRequest) { result ->
            result.result(
                onSuccess = {
                    it.addOnSuccessListener { authResult ->
                        createInitialUser(authResult)
                    }
                    it.addOnFailureListener { exception ->
                        _events.value =
                            SignUpViewEvent.SignUpFailed(exception.message ?: "Sign up failed")
                        resetStateAfterDelay()
                    }
                },
                onFailure = {
                    _events.value = SignUpViewEvent.SignUpFailed(it.message ?: "Sign up failed")
                }
            )
        }
    }

    private fun createInitialUser(authResult: AuthResult) {
        val user = authResult.user
        user?.uid?.let {
            val newUser = User(
                uid = it,
                userName = user.displayName,
                email = user.email,
                profilePicture = user.photoUrl.toString(),
                gamesWon = 0,
                gamesPlayed = 0,
                lastOnline = FieldValue.serverTimestamp(),
                friends = hashMapOf()
            )
            createInitialUserUseCase.invoke(viewModelScope, newUser) { result ->
                result.result(
                    onSuccess = {
                        setSuccessfulSignUp()
                    },
                    onFailure = {
                        _events.value = SignUpViewEvent.SignUpFailed("Registration failed to create a new user")
                    }
                )

            }
        }
    }

    private fun setSuccessfulSignUp() {
        _events.value = SignUpViewEvent.SignUpSuccess
        resetStateAfterDelay()
    }

    fun signUp(signUpModel: SignUpModel) {
        when {
            !emailValid(signUpModel.email) -> {
                _events.value = SignUpViewEvent.SignUpFailed("Invalid email")
            }

            !passwordValid(signUpModel.password) -> {
                _events.value = SignUpViewEvent.SignUpFailed("Invalid password")
            }

            !passwordFieldsMatch(signUpModel.password, signUpModel.confirmPassword) -> {
                _events.value = SignUpViewEvent.SignUpFailed("Passwords don't match")
            }

            else -> {
                createAccount(convertSignUpModelToLoginRequest(signUpModel))
            }
        }
        resetStateAfterDelay()
    }

    private fun resetStateAfterDelay() {
        viewModelScope.launch {
            delay(500)
            _events.value = null
        }
    }

    private fun convertSignUpModelToLoginRequest(signUpModel: SignUpModel): LoginRequest {
        return LoginRequest(signUpModel.email, signUpModel.password)
    }

    private fun passwordFieldsMatch(password1: String, password2: String): Boolean {
        return password1 == password2
    }

    private fun emailValid(email: String): Boolean {
        return if (TextUtils.isEmpty(email)) {
            false
        } else {
            android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches();
        }
    }

    private fun passwordValid(password: String): Boolean {
        val pattern: Pattern
        val passwordPattern = "^(?=.*[0-9])(?=.*[a-z])(?=.*[A-Z])(?=.*[!@#$%^&+=])(?=\\S+$).{4,}$"
        pattern = Pattern.compile(passwordPattern)
        val matcher: Matcher = pattern.matcher(password)
        return matcher.matches()
    }

    sealed class SignUpViewEvent {
        data class SignUpFailed(val message: String) : SignUpViewEvent()
        object SignUpSuccess : SignUpViewEvent()
    }
}