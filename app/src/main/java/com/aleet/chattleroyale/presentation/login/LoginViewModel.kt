package com.aleet.chattleroyale.presentation.login

import android.text.TextUtils
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.aleet.chattleroyale.models.User
import com.aleet.chattleroyale.presentation.authorisation.SignInState
import com.aleet.chattleroyale.requestModels.LoginRequest
import com.aleet.chattleroyale.useCases.local.user.SaveUniqueUserIdUseCase
import com.aleet.chattleroyale.useCases.local.user.SaveUserLocallyUseCase
import com.aleet.chattleroyale.useCases.remote.auth.GetCurrentFireBaseUserUseCase
import com.aleet.chattleroyale.useCases.remote.auth.LoginUseCase
import com.aleet.chattleroyale.useCases.remote.user.CreateInitialUserUseCase
import com.aleet.chattleroyale.useCases.remote.user.GetCRUser
import com.aleet.chattleroyale.useCases.remote.user.GetCrUserRequest
import com.aleet.chattleroyale.useCases.remote.user.SaveCrUserUseCase
import com.aleet.chattleroyale.utils.justValue
import com.google.android.gms.auth.api.signin.GoogleSignInAccount
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.common.api.ApiException
import com.google.android.gms.tasks.OnSuccessListener
import com.google.android.gms.tasks.Task
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.auth.GoogleAuthProvider
import com.google.firebase.database.DataSnapshot
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import java.util.regex.Pattern
import javax.inject.Inject

@HiltViewModel
class LoginViewModel @Inject constructor(
    private val loginUseCase: LoginUseCase,
    private val getCurrentFireBaseUser: GetCurrentFireBaseUserUseCase,
    private val saveUserLocallyUseCase: SaveUserLocallyUseCase,
    private val getCRUser: GetCRUser,
    private val createInitialUserUseCase: CreateInitialUserUseCase,
    private val saveCrUserUseCase: SaveCrUserUseCase,
    private val saveUniqueUserIdUseCase: SaveUniqueUserIdUseCase
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
            account?.let { fireBaseAuthWithGoogle(it.idToken!!) }
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
                if (task.isSuccessful) {
                    // Sign in success, update UI with signed-in user's information
                    val user = FirebaseAuth.getInstance().currentUser
                    user?.uid?.let {
                        getCRUser.invoke(viewModelScope, GetCrUserRequest(
                            it,
                            success
                        ) { _events.value = LoginReaction.FailedLogin("") })

                    }
                    createInitialUser(user)
                } else {
                    _events.value = LoginReaction.FailedLogin("Task failed")
                }
            }
    }

    val success = OnSuccessListener<DataSnapshot> {
        val user = it.getValue(User::class.java)
        user?.let { collectedUser ->
            collectedUser.lastOnline = System.currentTimeMillis()
            viewModelScope.launch {
                saveCrUserUseCase.justValue(collectedUser)
                saveUserLocallyUseCase.justValue(collectedUser)
                saveUniqueUserIdUseCase.justValue(collectedUser.uid)
            }
        }
    }

    private fun createInitialUser(firebaseUser: FirebaseUser?) {
        firebaseUser?.uid?.let {
            val newUser = User(
                uid = it,
                userName = firebaseUser.displayName,
                email = firebaseUser.email,
                profilePicture = firebaseUser.photoUrl.toString(),
                gamesWon = 0,
                gamesPlayed = 0,
                lastOnline = System.currentTimeMillis(),
                friends = hashMapOf()
            )
            createInitialUserUseCase.invoke(viewModelScope, newUser) { result ->
                result.result(
                    onSuccess = {
                        _events.value = LoginReaction.SuccessfulLogin
                        storeUser(newUser)
                    },
                    onFailure = {
                        _events.value = LoginReaction.FailedLogin("Registration failed to create a new user")
                    }
                )
            }
        }
    }

    private fun locallySaveUniqueUserId(id: String) {
        saveUniqueUserIdUseCase.invoke(viewModelScope, id)
    }

    private fun getAndStoreUser(id: String) {
        getCRUser.justValue(GetCrUserRequest(id, success) {})
    }

    private fun storeUser(user: User) {
        locallySaveUniqueUserId(user.uid)
        saveUserLocallyUseCase.invoke(viewModelScope, user) { result ->
            result.result(
                onSuccess = {},
                onFailure = {
                    _events.value = LoginReaction.FailedUserPersist("Unable to save user locally")
                }
            )
        }
    }

    fun checkIfUserAlreadyAuthorised() {
        getCurrentFireBaseUser.invoke(viewModelScope) { result ->
            result.result(
                onSuccess = {
                    it?.let {
                        getAndStoreUser(it.uid)
                        _events.value = LoginReaction.SuccessfulLogin
                    }
                },
                onFailure = {}
            )
        }
    }

    fun process(viewEvent: LoginViewEvent) {
        when (viewEvent) {
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
                        authResult.user?.uid?.let { userId -> getAndStoreUser(userId) }
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

    private fun validateFieldsAndLogin(loginRequest: LoginRequest) {
        val validPassword = passwordValid(loginRequest.password)
        when {
            !emailValid(loginRequest.username) -> _events.value = LoginReaction.InvalidCredentials("Email invalid")
            !validPassword.first -> _events.value = LoginReaction.InvalidCredentials(validPassword.second)
            else -> {
                login(loginRequest)
            }
        }
        viewModelScope.launch { resetStateAfterDelay() }
    }

    // Resetting state so we can send the same state again
    private suspend fun resetStateAfterDelay() {
        delay(500)
        _events.value = null
    }

    sealed class LoginViewEvent {
        class LoginClicked(val loginRequest: LoginRequest) : LoginViewEvent()
        object RegisterClicked : LoginViewEvent()
        class OtherAuthClicked(authType: AuthType) : LoginViewEvent()
    }

    sealed class LoginReaction {
        object SuccessfulLogin : LoginReaction()
        class FailedLogin(val message: String) : LoginReaction()
        class InvalidCredentials(val message: String) : LoginReaction()
        class FailedUserPersist(val message: String) : LoginReaction()
    }

}

enum class AuthType {
    FIREBASE,
    FACEBOOK,
    GOOGLE
}

