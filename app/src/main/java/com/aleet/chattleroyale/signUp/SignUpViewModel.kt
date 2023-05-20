package com.squadsandshots_android.presentation.signUp

import android.text.TextUtils
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.squadsandshots_android.requestModels.LoginRequest
import com.squadsandshots_android.useCases.CreateFireBaseUserUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import java.util.regex.Matcher
import java.util.regex.Pattern
import javax.inject.Inject
import kotlin.math.sign

@HiltViewModel
class SignUpViewModel @Inject constructor(
    private val createFireBaseUserUseCase: CreateFireBaseUserUseCase
) : ViewModel() {

    private val _events: MutableStateFlow<SignUpViewEvent?> = MutableStateFlow(null)
    val events: StateFlow<SignUpViewEvent?> get() = _events.asStateFlow()

    private fun createAccount(loginRequest: LoginRequest) {
        createFireBaseUserUseCase.invoke(viewModelScope, loginRequest) { result ->
            result.result(
                onSuccess = {
                    it.addOnSuccessListener {
                        _events.value = SignUpViewEvent.SignUpSuccess
                        resetStateAfterDelay()
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