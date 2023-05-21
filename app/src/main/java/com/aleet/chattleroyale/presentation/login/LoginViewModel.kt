package com.aleet.chattleroyale.presentation.login

import android.text.TextUtils
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.aleet.chattleroyale.presentation.authorisation.SignInResult
import com.aleet.chattleroyale.presentation.authorisation.SignInState
import com.aleet.chattleroyale.requestModels.LoginRequest
import com.aleet.chattleroyale.useCases.LoginUseCase
import com.google.android.gms.auth.api.signin.GoogleSignInAccount
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.common.api.ApiException
import com.google.android.gms.tasks.Task
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.auth.GoogleAuthProvider
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import java.util.regex.Pattern
import javax.inject.Inject

@HiltViewModel
class LoginViewModel @Inject constructor(
    private val loginUseCase: LoginUseCase,
//    private val getCurrentFireBaseUser: GetCurrentFireBaseUserUseCase
) : ViewModel() {

    private val _events: MutableStateFlow<LoginReaction?> = MutableStateFlow(null)
    val events: StateFlow<LoginReaction?> get() = _events.asStateFlow()

    private val _state = MutableStateFlow(SignInState())
    val state = _state.asStateFlow()

    private val _googleSignInClient = MutableLiveData<GoogleSignInClient>()
    val googleSignInClient: LiveData<GoogleSignInClient> = _googleSignInClient

    fun setGoogleSignInClient(googleSignInClient: GoogleSignInClient) {
        _googleSignInClient.value = googleSignInClient
    }

    fun handleSignInResult(completedTask: Task<GoogleSignInAccount>) {
        try {
            val account = completedTask.getResult(ApiException::class.java)
            account?.let { fireBaseAuthWithGoogle(it.idToken!!)}
        } catch (e: ApiException) {
            // The exception status code indicates the detailed failure reason
            // Please refer to the GoogleSignInStatusCodes class reference for more information
            Log.d("Login", "SignInResult:failed code=" + e.statusCode)
            //TODO UpdateUi
        }
    }

    private fun fireBaseAuthWithGoogle(idToken: String) {
        val credential = GoogleAuthProvider.getCredential(idToken, null)
        FirebaseAuth.getInstance().signInWithCredential(credential)
            .addOnCompleteListener { task ->
                if(task.isSuccessful) {
                    // Sign in success, update UI with signed-in user's information
                    val user = FirebaseAuth.getInstance().currentUser
                    updateUserAfterSocialLoginIn(user)
                } else {
                    // TODO If sign in fails display a message to the user
                }
            }
    }

    private fun updateUserAfterSocialLoginIn(user: FirebaseUser?) {

        // TODO Store user locally?????
        // TODO update the user with a use case
        _events.value = LoginReaction.SuccessfulLogin
    }

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

    fun process(viewEvent: LoginViewEvent) {
        when(viewEvent) {
            is LoginViewEvent.LoginClicked -> validateFieldsAndLogin(viewEvent.loginRequest)
            is LoginViewEvent.OtherAuthClicked -> {}
            LoginViewEvent.RegisterClicked -> {}
        }
    }

    private fun login(loginRequest: LoginRequest) {
        loginUseCase.invoke(viewModelScope, loginRequest) { result ->
            result.result(
                onSuccess = {
                    it.addOnSuccessListener { authResult ->
                        _events.value = LoginReaction.SuccessfulLogin
                    }
                    it.addOnFailureListener { authResult ->
                        _events.value = LoginReaction.FailedLogin(authResult.message ?: "Login Failed")
                    }
                },
                onFailure = {
                    _events.value = LoginReaction.FailedLogin(it.message ?: "Login failed")
                }
            )
        }
    }

    private fun emailValid(email: String): Boolean {
        return if (TextUtils.isEmpty(email)) {
            false
        } else {
            android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches();
        }
    }

    private fun passwordValid(password: String): Pair<Boolean, String> {
        val digitPattern = Pattern.compile(".*[0-9].*")
        val lowerCasePattern = Pattern.compile(".*[a-z].*")
        val upperCasePattern = Pattern.compile(".*[A-Z].*")
        val specialCharPattern = Pattern.compile(".*[!@#$%^&+=].*")
        val noSpacePattern = Pattern.compile("\\S+")
        val minLengthPattern = Pattern.compile(".{4,}")

        val feedback = StringBuilder("Password should contain: ")

        if (!digitPattern.matcher(password).matches()) {
            feedback.append("At least one digit. ")
        }
        if (!lowerCasePattern.matcher(password).matches()) {
            feedback.append("At least one lower case letter. ")
        }
        if (!upperCasePattern.matcher(password).matches()) {
            feedback.append("At least one upper case letter. ")
        }
        if (!specialCharPattern.matcher(password).matches()) {
            feedback.append("At least one special character (!@#$%^&+=). ")
        }
        if (!noSpacePattern.matcher(password).matches()) {
            feedback.append("No space. ")
        }
        if (!minLengthPattern.matcher(password).matches()) {
            feedback.append("Minimum length of 4 characters. ")
        }

        val isValid = digitPattern.matcher(password).matches() &&
                lowerCasePattern.matcher(password).matches() &&
                upperCasePattern.matcher(password).matches() &&
                specialCharPattern.matcher(password).matches() &&
                noSpacePattern.matcher(password).matches() &&
                minLengthPattern.matcher(password).matches()

        return Pair(isValid, feedback.toString())
    }

    fun onSignInResult(result: SignInResult) {
        _state.update { it.copy(
            isSignInSuccessful = result.data != null,
            signInError = result.errorMessage
        ) }
    }

    fun resetState() {
        _state.update { SignInState() }
    }

    private fun validateFieldsAndLogin(loginRequest: LoginRequest) {
        val validPassword = passwordValid(loginRequest.password)
        when {
            !emailValid(loginRequest.username) -> _events.value = LoginReaction.InvalidCredentials("Email invalid")
            !validPassword.first -> _events.value = LoginReaction.InvalidCredentials(validPassword.second)
            else -> { login(loginRequest) }
        }
        viewModelScope.launch { resetStateAfterDelay() }
    }

    // Resetting state so we can send the same state again
    private suspend fun resetStateAfterDelay() {
        delay(500)
        _events.value = null
    }

//    val callbackManager = CallbackManager.Factory.create()
//
//    LoginManager.getInstance().logInWithReadPermissions(this, listOf("email", "public_profile"))
//    LoginManager.getInstance().registerCallback(callbackManager,
//    object : FacebookCallback<LoginResult> {
//        override fun onSuccess(loginResult: LoginResult) {
//            handleFacebookAccessToken(loginResult.accessToken)
//        }
//
//        override fun onCancel() {
//            // User cancelled the login process
//        }
//
//        override fun onError(error: FacebookException) {
//            // Login attempt failed
//        }
//    })
//
//    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
//        super.onActivityResult(requestCode, resultCode, data)
//
//        // Pass the activity result back to the Facebook SDK
//        callbackManager.onActivityResult(requestCode, resultCode, data)
//    }
//
//    private fun handleFacebookAccessToken(token: AccessToken) {
//        val credential = FacebookAuthProvider.getCredential(token.token)
//        auth.signInWithCredential(credential)
//            .addOnCompleteListener(this) { task ->
//                if (task.isSuccessful) {
//                    // Sign in success
//                } else {
//                    // Sign in fails
//                }
//            }
//    }


//    private fun saveUser(user: FirebaseUser) {
//        // TODO save the user to database?
//    }

    sealed class LoginViewEvent {
        class LoginClicked(val loginRequest: LoginRequest) : LoginViewEvent()
        object RegisterClicked : LoginViewEvent()
        class OtherAuthClicked(authType: AuthType) : LoginViewEvent()
    }

    sealed class LoginReaction {
        object SuccessfulLogin: LoginReaction()
        class FailedLogin(val message: String): LoginReaction()
        class InvalidCredentials(val message: String): LoginReaction()
    }

}

enum class AuthType {
    FIREBASE,
    FACEBOOK,
    GOOGLE
}

